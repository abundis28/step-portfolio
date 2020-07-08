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
    const commentsElement = document.getElementById('content-container');
    commentsElement.innerHTML = '';
    for (const comment of commentsJson) {
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
 * Shows the log out button and the form if the user is logged in. 
 * Shows the log in button and hides the form if the user is logged out.
 */
function checkLoginStatus() {
  fetch("/user").then(response => response.json()).then((status) => {
    if (status == "logged") {
      const logoutBtn = document.getElementById("logout-btn");
      const commentForm = document.getElementById("comment-form");
      logoutBtn.style.display = "block";
      commentForm.style.display = "block";
    } else {
      const modalBtn = document.getElementById("modal-btn");
      const loginBtn = document.getElementById("login-btn");
      modalBtn.style.display = "block";
      loginBtn.setAttribute("href", status);
    }
  });
}
