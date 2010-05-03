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

package com.google.api.client.googleapis.xml.atom;

import com.google.api.client.util.ArrayMap;
import com.google.api.client.xml.Xml;
import com.google.api.client.xml.XmlHttpSerializer;
import com.google.api.client.xml.atom.Atom;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;

public final class PatchRelativeToOriginalSerializer extends XmlHttpSerializer {

  public volatile Object patchedEntry;
  public volatile Object originalEntry;

  public String getContentType() {
    return "application/xml";
  }

  public void writeTo(OutputStream out) throws IOException {
    ArrayMap<String, Object> patch =
        GData.computePatch(patchedEntry, originalEntry);
    XmlSerializer serializer = Xml.createSerializer();
    serializer.setOutput(out, "UTF-8");
    this.namespaceDictionary.serialize(serializer, Atom.ATOM_NAMESPACE,
        "entry", patch);
  }
}
