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

/**
 * Adds a random language to the page.
 */
function addRandomLanguage() {
  const languages =
  	['Español','English','Deutsch'];
 
  // Pick a random language.
  const language = languages[Math.floor(Math.random()*languages.length)];
 
  // Add it to the page.
  const languageContainer = document.getElementById('language-container');
  languageContainer.innerText = language;
}

/**
 * Renders the page depending on the login status of the users.
 */
function loadPage(maxNumComments) {
  checkLoginStatus();
  showComments(maxNumComments);
}

/**
 * Appends previously made comments that populated the servlet as children list elements to the page.
 */
function showComments(maxNumComments) {
  fetch('/data?max=' + maxNumComments).then(response => response.json()).then((commentsJson) => {
    console.log("Comments displayed" + maxNumComments);
    const commentsElement = document.getElementById('content-container');
    commentsElement.innerHTML = '';
    for (const comment of commentsJson) {
      console.log(comment);
      commentsElement.appendChild(createListElement(comment));
    }
  });
}

/**
 * Creates an <li> element containing text.
 */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  liElement.setAttribute("class","list-group-item")
  return liElement;
}

/**
 * Handles showing of buttons depending on the fetched status of the user.
 * Assigns the corresponding URL to the modal buttons (login or logout, depending).
 */
function checkLoginStatus() {
  fetch("/user").then(response => response.json()).then((user) => {
    if (user.loginStatus) {
      // Shows the log out button (in navbar and modal) and the form if the user is logged in. 
      const commentForm = document.getElementById("comment-form");
      const logoutBtn = document.getElementById("logout-btn");
      const logoutModalBtn = document.getElementById("logout-modal-btn"); 
      commentForm.style.display = "block";
      logoutBtn.style.display = "block";
      logoutModalBtn.setAttribute("href", user.url);
      logoutModalBtn.style.display = "block";
    } else {
      // Shows the log in button (in navbar and modal) if the user is logged out.
      const loginBtn = document.getElementById("login-btn");
      const loginModalBtn = document.getElementById("login-modal-btn");
      loginBtn.style.display = "block";
      loginModalBtn.setAttribute("href", user.url);
      loginModalBtn.style.display = "block";
    }
  });
}
