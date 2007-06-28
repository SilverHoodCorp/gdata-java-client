/* Copyright (c) 2006 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.data.batch;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.Namespaces;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines a batch operation (update, insert, delete, but not query).
 *
 * This enum is the java equivalent of &lt;batch: operation type="..."/&gt.
 * 
 * 
 */
public enum BatchOperationType {

  /** Query for the entry by id. */
  QUERY("query"),
  
  /** Insert the current entry. */
  INSERT("insert"),

  /** Update the current entry identified by its id. */
  UPDATE("update"),

  /** Delete the current entry identified by its id. */
  DELETE("delete");

  private static final Map<String, BatchOperationType> BY_NAME =
      new HashMap<String, BatchOperationType>();
  static {
    for (BatchOperationType op : BatchOperationType.values()) {
      BY_NAME.put(op.getName(), op);
    }
  }

  private final String name;
  private BatchOperationType(String name) {
    this.name = name;
  }

  /**
   * Gets the operation name as it appears in XML.
   * 
   * @return operation name
   */
  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

  /**
   * Gets a BatchOperation by name (lowercase), as it appears in XML.
   * 
   * @param name
   * @return a BatchOperation or null
   */
  public static BatchOperationType forName(String name) {
    return BY_NAME.get(name);
  }

  /**
   * Generates a batch:operation element. 
   * 
   * @param w
   * @throws IOException
   */
  public void generateAtom(XmlWriter w) throws IOException {
    List<XmlWriter.Attribute> attrs = Collections
        .singletonList(new XmlWriter.Attribute("type", getName()));
    w.simpleElement(Namespaces.batchNs, "operation", attrs, null);
  }
}