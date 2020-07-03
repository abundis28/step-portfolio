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
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/** Servlet that returns a JSON string with quotes.
    TODO(aabundis): modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private List<String> comments = new ArrayList<>();
  // Declares instance of Datastore Service
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Prepares results and sorts them in descending order.
    Query query = new Query("entry").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    comments.clear();
    for (Entity entity : results.asIterable()) {
      String comment = (String) entity.getProperty("com");
      String firstN = (String) entity.getProperty("firstN");
      String lastN = (String) entity.getProperty("lastN");
      comments.add(firstN+" "+lastN+":\n"+comment);
    }
    
    String json = convertToJsonUsingGson();
    // Send the JSON as the response.
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Retreives the entry values.
    String comment = request.getParameter("textarea");
    String firstName = request.getParameter("firstName");
    String lastName = request.getParameter("lastName");
    long timestamp = System.currentTimeMillis();
    // TODO(aabundis): Verify that a comment was written, no empty submissions.

    // Creates an Entity for each comment entry.
    Entity entryEntity = new Entity("entry");
    entryEntity.setProperty("firstN",firstName);
    entryEntity.setProperty("lastN",lastName);
    entryEntity.setProperty("com",comment);
    entryEntity.setProperty("timestamp", timestamp);
    // Inserts Entity.
    datastore.put(entryEntity);
    // Redirect to comments page to visualize comment.
    response.sendRedirect("/comments.html");
  }

  /**
   * Converts an ArrayList of messages into a JSON string using the Gson library.
   */
  private String convertToJsonUsingGson() {
    Gson gson = new Gson();
    return gson.toJson(comments);
  }
}
