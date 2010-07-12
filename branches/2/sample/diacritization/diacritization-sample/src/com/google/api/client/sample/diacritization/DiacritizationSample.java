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

package com.google.api.client.sample.diacritization;

import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;

import java.io.IOException;


/**
 * @author Yaniv Inbar
 */
public class DiacritizationSample {

  public static void main(String[] args) throws Exception {
    // initialize HTTP transport
    Debug.enableLogging();
    HttpTransport transport = GoogleTransport.create();
    transport.addParser(new JsonCParser());
    diacritize(transport, "مرحبا العالم");
  }

  private static void diacritize(HttpTransport transport, String message)
      throws IOException {
    System.out.println("Arabic message: " + message);
    HttpRequest request = transport.buildGetRequest();
    DiacritizationUrl url = new DiacritizationUrl();
    url.message = message;
    request.url = url;
    DiacritizationResponse response =
        request.execute().parseAs(DiacritizationResponse.class);
    System.out.println(
        "Diacritized Arabic message: " + response.diacritizedText);
  }
}
