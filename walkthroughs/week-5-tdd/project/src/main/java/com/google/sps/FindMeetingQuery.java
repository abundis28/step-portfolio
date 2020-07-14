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
import java.util.List;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // Creates the resulting collection of available TimeRange options for the queried meeting.
    Collection<TimeRange> possibleTimeSlots = new ArrayList();
    // No attendees = no time restrictions = all day available.
    if(request.getAttendees().isEmpty()) {
      possibleTimeSlots.add(TimeRange.WHOLE_DAY);
    }
    // At this tage it may seem unnecessary to have the double return but it'll make more sense
    // once the logic of the other test cases is implemented. 
    // Could've implemented this solution with an if block, checking if the duration is less or equal
    // than the whole day but nesting the whole algorithm inside an if at this early didn't convinced me.
    if(request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return possibleTimeSlots;
    }
    return possibleTimeSlots;
  }
}
