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

package com.google.sps.classes;

import com.google.gson.Gson;
import com.google.sps.classes.UserAuthentication;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods used across classes.
 */
public final class UtilityClass {
  /**
   * Converts ArrayList of comments to JSON using GSON class.
   */
  public static String convertToJsonUsingGson(List<String> comments) {
    Gson gson = new Gson();
    return gson.toJson(comments);
  }

  /**
   * Overloaded convertToJsonUsingGson() function that converts user object 
   * to JSON using GSON class.
   */
  public static String convertToJsonUsingGson(UserAuthentication user) {
    Gson gson = new Gson();
    return gson.toJson(user);
  }
}
