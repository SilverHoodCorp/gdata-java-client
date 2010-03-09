// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http.apache;

import com.google.api.data.client.http.HttpRequest;
import com.google.api.data.client.http.HttpResponse;
import com.google.api.data.client.http.HttpSerializer;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.IOException;

final class ApacheHttpRequest implements HttpRequest {
  private final HttpClient httpClient;

  private final HttpRequestBase request;

  ApacheHttpRequest(HttpClient httpClient, HttpRequestBase request) {
    this.httpClient = httpClient;
    this.request = request;
  }

  public void addHeader(String name, String value) {
    this.request.addHeader(name, value);
  }

  public HttpResponse execute() throws IOException {
    return new ApacheHttpResponse(this.httpClient.execute(request));
  }

  public void setContent(HttpSerializer serializer) {
    GDataEntity entity =
        new GDataEntity(serializer.getContentLength(), serializer);
    entity.setContentEncoding(serializer.getContentEncoding());
    entity.setContentType(serializer.getContentType());
    ((HttpEntityEnclosingRequest) this.request).setEntity(entity);
  }
}
