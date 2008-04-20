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


package com.google.gdata.data;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;


/**
 * Abstract base class for entry content.
 *
 * 
 */
public abstract class Content {


  /** Defines the possible content types. */
  public static class Type {

    public static final int TEXT = 1;
    public static final int HTML = 2;
    public static final int XHTML = 3;
    public static final int OTHER_TEXT = 4;     // inlined text
    public static final int OTHER_XML = 5;      // inlined xml
    public static final int OTHER_BINARY = 6;   // inlined base64 binary
    public static final int MEDIA = 7;          // external media
  }


  /** Returns this content's type. */
  public abstract int getType();


  /** Returns the human language that this content is written in. */
  public abstract String getLang();


  /**
   * Generates XML in the Atom format.
   *
   * @param   w
   *            output writer
   *
   * @throws  IOException
   */
  public abstract void generateAtom(XmlWriter w) throws IOException;


  /**
   * Generates XML in the RSS format.
   *
   * @param   w
   *            output writer
   *
   * @throws  IOException
   */
  public abstract void generateRss(XmlWriter w) throws IOException;


  /**
   * Parses XML in the Atom format.
   *
   * @param   attrs
   *            XML attributes of the Content node.
   *            Used to determine the type of this node.
   *
   * @return  a child handler
   *
   * @throws  ParseException
   * @throws  IOException
   */
  public static ChildHandlerInfo getChildHandler(Attributes attrs)
      throws ParseException, IOException {

    String type = attrs.getValue("", "type");
    ChildHandlerInfo childHandlerInfo = new ChildHandlerInfo();

    String src = attrs.getValue("", "src");
    if (src == null) {
      // In-line content

      if (type == null ||
          type.equals("text") ||
          type.equals("text/plain") ||
          type.equals("html") ||
          type.equals("text/html") ||
          type.equals("xhtml")) {

        TextContent tc = new TextContent();
        TextConstruct.ChildHandlerInfo chi =
            TextConstruct.getChildHandler(attrs);
        tc.setContent(chi.textConstruct);
        childHandlerInfo.handler = chi.handler;
        childHandlerInfo.content = tc;

      } else {

        OtherContent oc = new OtherContent();
        childHandlerInfo.handler = oc.new AtomHandler(attrs);
        childHandlerInfo.content = oc;
      }
    } else {
        OutOfLineContent oolc = new OutOfLineContent();
        childHandlerInfo.handler = oolc.new AtomHandler();
        childHandlerInfo.content = oolc;
    }

    return childHandlerInfo;
  }


  /**
   * Return type for {@link Content#getChildHandler(Attributes)}
   * contains an element handler and a text construct.
   */
  public static class ChildHandlerInfo {
    public XmlParser.ElementHandler handler;
    public Content content;
  }
}