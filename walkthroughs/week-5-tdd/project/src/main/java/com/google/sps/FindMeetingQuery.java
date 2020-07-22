// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FindMeetingQuery {
  /*
   * Assigns attendees from request to local HashMap to search for them in constant time.
   */
  private HashSet<String> requestAttendeesToSet(MeetingRequest request) {
    HashSet<String> requestAttendees = new HashSet<String>();
    for (String attendee : request.getAttendees()) {
      requestAttendees.add(attendee);
    }
    return requestAttendees;
  }

  /*
   * Adds a TimeRange depending on the scenario. Some additions use a recursive call to this same
   * function because if a new TimeRange is formed from a merge or an existing TimeRange is replaced
   * because of a containment issue, it needs to be checked against all the previously added
   * TimeRanges for new overlap or containment issues.
   */
  private ArrayList<TimeRange> addTimeRange(ArrayList<TimeRange> occupiedTimeSlots,
      TimeRange addedTimeSlot) {
    for (TimeRange timeSlot : occupiedTimeSlots) {
      if (timeSlot.contains(addedTimeSlot)) {
        // New TimeRange is already contained, so it is not added.
        return occupiedTimeSlots;
      }
      if (addedTimeSlot.contains(timeSlot)) {
        // New TimeRange contains an already existing meeting, so the new replaces the existing.
        occupiedTimeSlots.remove(timeSlot);
        addTimeRange(occupiedTimeSlots, addedTimeSlot);
        return occupiedTimeSlots;
      }
      if (timeSlot.overlaps(addedTimeSlot)) {
        // New TimeRange overlaps with an already existing meeting.
        addTimeRange(occupiedTimeSlots, mergeTimeRanges(timeSlot, addedTimeSlot));
        return occupiedTimeSlots;
      }
    }
    // No special-casing applied to TimeRange added normally.
    occupiedTimeSlots.add(addedTimeSlot);
    return occupiedTimeSlots;
  }

  /*
   * Merges overlapping TimeRanges into a single TimeRange that includes both of them.
   */
  private TimeRange mergeTimeRanges(TimeRange a, TimeRange b) {
    return TimeRange.fromStartEnd(a.start() < b.start() ? a.start() : b.start(),
        a.end() > b.end() ? a.end() : b.end(), /*inclusive=*/false);
  }

  /*
   * Returns a collection with TimeRanges that are not available for meetings.
   */
  private ArrayList<TimeRange> getOccupiedTimeSlots(Collection<Event> events,
      HashSet<String> requestAttendees) {
    ArrayList<TimeRange> occupiedTimeRangesForRequestAttendees = new ArrayList<TimeRange>();
    for (Event meeting : events) {
      for (String attendee : meeting.getAttendees()) {
        if (requestAttendees.contains(attendee)) {
          // If the meeting contains a requested attendee it is added.
          occupiedTimeRangesForRequestAttendees = addTimeRange(occupiedTimeRangesForRequestAttendees,
              meeting.getWhen());
          break;
        }
      }
    }
    Collections.sort(occupiedTimeRangesForRequestAttendees, TimeRange.ORDER_BY_START);
    return occupiedTimeRangesForRequestAttendees;
  }

  /*
   * Calculates whether requested meeting's duration fits in between occupied TimeRanges.
   */
  private Collection<TimeRange> getFreeTimeSlots(ArrayList<TimeRange> occupiedTimeSlots, 
      long duration) {
    Collection<TimeRange> possibleTimeSlots = new ArrayList<>();
    if (occupiedTimeSlots.isEmpty()) {
      // Return all available if there are no occupied TimeRanges.
      possibleTimeSlots.add(TimeRange.WHOLE_DAY);
      return possibleTimeSlots;
    }
    if (occupiedTimeSlots.get(0).start() - TimeRange.START_OF_DAY >= duration) {
      // From midnight to the first meeting.
      possibleTimeSlots.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, 
          occupiedTimeSlots.get(0).start(), false));
    }
    int previousEnd = occupiedTimeSlots.get(0).end();
    // The for loop starts in the 1 slot because the 0 slot was already processed out of the loop.
    for (int index = 1; index < occupiedTimeSlots.size(); index++) {
      // Check for spaces between occupied TimeRanges.
      int currentStart = occupiedTimeSlots.get(index).start();
      if (currentStart - previousEnd >= duration) {
        // If the duration fits between the end of the previous occupied TimeRange and the start of
        // the start of the current occupied TimeRange, then a free TimeRange is created and added.
        possibleTimeSlots.add(TimeRange.fromStartEnd(previousEnd, currentStart, false));
      }
      previousEnd = occupiedTimeSlots.get(index).end();
    }
    if (TimeRange.END_OF_DAY - previousEnd >= duration) {
      // From last meeting to right before midnight.
      possibleTimeSlots.add(TimeRange.fromStartEnd(previousEnd, TimeRange.END_OF_DAY, true));
    }
    return possibleTimeSlots;
  }
  
  /*
   * Returns possible time slots for the meeting request.
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<TimeRange> possibleTimeSlots = new ArrayList<>();
    long meetingDuration = request.getDuration();
    HashSet<String> requestAttendees = requestAttendeesToSet(request);
    if (requestAttendees.isEmpty()) {
      // No attendees = no time restrictions = all day available.
      possibleTimeSlots.add(TimeRange.WHOLE_DAY);
      return possibleTimeSlots;
    }
    if (request.getDuration() > 1440) {
      // Duration lasts more than the whole day = not possible to have the meeting.
      return possibleTimeSlots;
    }
    // Define available space by first defining the already occupied TimeRanges.
    possibleTimeSlots = getFreeTimeSlots(getOccupiedTimeSlots(events, requestAttendees), 
        request.getDuration());
    return possibleTimeSlots;
  }
}
