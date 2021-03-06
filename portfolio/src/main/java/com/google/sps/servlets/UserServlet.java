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
 
package com.google.sps.servlets;
 
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.classes.UtilityClass;
import com.google.sps.classes.UserAuthentication;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
/**
 * Servlet that returns the login status of a user and generates Login URLs.
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
 
  /**
   * Returns a JSON string with the login status and the correspondin login/logout URL.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    UserAuthentication user = new UserAuthentication();
    // Destination URL is the same whether the user is signed in or not.
    String redirectUrl = "/comments.html";
    boolean status = userService.isUserLoggedIn();
    user.redirectUrl = status ? userService.createLogoutURL(redirectUrl) : 
        userService.createLoginURL(redirectUrl);
    user.loginStatus = status ? true : false;
 
    // Use convertToJsonUsingGson() function in UtilityClass to convert and send JSON as response.
    String json = UtilityClass.convertToJsonUsingGson(user);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
