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
function addRandomLanguages() {
  const languages =
      ['Español', 'English', 'Deutsch'];

  // Pick a random greeting.
  const random = Math.floor(Math.random() * languages.length);
  const language = languages[random];

  // Add it to the page.
  const languageContainer = document.getElementById('language-container');
  languageContainer.innerText = language;
}
