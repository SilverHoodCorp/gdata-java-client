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

package com.google.api.client.auth.oauth;

import com.google.api.client.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * OAuth {@code "HMAC-SHA1"} signature method.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public final class OAuthHmacSigner implements OAuthSigner {

  /** Client-shared secret or {@code null} for none. */
  public String clientSharedSecret;

  /** Token-shared secret or {@code null} for none. */
  public String tokenSharedSecret;

  public String getSignatureMethod() {
    return "HMAC-SHA1";
  }

  public String computeSignature(String signatureBaseString)
      throws GeneralSecurityException {
    String clientSharedSecret = this.clientSharedSecret;
    String tokenSharedSecret = this.tokenSharedSecret;
    clientSharedSecret =
        clientSharedSecret == null ? "" : OAuthParameters
            .escape(clientSharedSecret);
    tokenSharedSecret =
        tokenSharedSecret == null ? "" : OAuthParameters
            .escape(tokenSharedSecret);
    String keyString =
        new StringBuilder().append(clientSharedSecret).append('&').append(
            tokenSharedSecret).toString();
    try {
      SecretKey key =
          new SecretKeySpec(keyString.getBytes("UTF-8"), "HmacSHA1");
      Mac mac = Mac.getInstance("HmacSHA1");
      mac.init(key);
      byte[] encoded = Base64.encode(mac.doFinal(signatureBaseString
          .getBytes("UTF-8")));
      return new String(encoded, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError(e);
    }
  }
}
