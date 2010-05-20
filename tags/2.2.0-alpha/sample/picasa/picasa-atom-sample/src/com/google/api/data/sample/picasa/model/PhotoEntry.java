/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.data.sample.picasa.model;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.util.Key;

import java.io.IOException;
import java.net.URL;

/**
 * @author Yaniv Inbar
 */
public class PhotoEntry extends Entry {

  @Key
  public Category category = Category.newKind("photo");

  @Key("media:group")
  public MediaGroup mediaGroup;

  public static PhotoEntry executeInsert(GoogleTransport transport,
      String feedLink, URL photoUrl, String fileName) throws IOException {
    HttpRequest request = transport.buildPostRequest();
    request.setUrl(feedLink);
    GoogleHeaders.setSlug(request.headers, fileName);
    InputStreamContent content = new InputStreamContent();
    content.inputStream = photoUrl.openStream();
    content.type = "image/jpeg";
    request.content = content;
    return request.execute().parseAs(PhotoEntry.class);
  }
}
