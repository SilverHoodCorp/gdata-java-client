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

package com.google.api.client.sample.discovery;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Yaniv Inbar
 */
public class MethodDetails implements Serializable, Comparable<MethodDetails> {
  public String name;
  public ArrayList<String> requiredParameters = new ArrayList<String>();
  public ArrayList<String> optionalParameters = new ArrayList<String>();

  @Override
  public int compareTo(MethodDetails o) {
    if (o == this) {
      return 0;
    }
    return name.compareTo(o.name);
  }
}
