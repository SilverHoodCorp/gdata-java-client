/* Copyright (c) 2008 Google Inc.
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


package com.google.gdata.wireformats;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.AttributeMetadata;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.QName;
import com.google.gdata.model.ValidationContext;
import com.google.gdata.model.ContentModel.Cardinality;
import com.google.gdata.util.ParseException;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.List;

/**
 * XML handler that translates XML content from a wire format into
 * an in-memory representation.
 */
public class XmlHandler extends XmlParser.ElementHandler {

  /**
   * Validation context, used to accumulate metadata validation
   * errors discovered during parsing.
   */
  protected final ValidationContext vc;

  /**
   * Parent element, if non-null the element will be added to the parent after
   * it has been fully parsed.
   */
  protected final Element parentElement;
  
  /**
   * Element being parsed.
   */
  protected final Element element;

  /**
   * Construct an xml parser that will add the element to its parent after
   * parsing is completed.
   */
  public XmlHandler(ValidationContext vc, Element parent, Element element) {
    this.vc = vc;
    this.parentElement = parent;
    this.element = element;
  }

  /**
   * @return element that was parsed
   */
  public Element getElement() {
    return element;
  }

  @Override
  public void processAttribute(QName qn, String value)
      throws ParseException {

    if (element.hasAttribute(qn)) {
      throw new ParseException(
          CoreErrorDomain.ERR.duplicateAttributeValue.withInternalReason(
              "Duplicate value for attribute " + qn));
    }
    ElementMetadata<?, ?> metadata = element.getMetadata();
    AttributeMetadata<?> attMeta = metadata.findAttribute(qn);
    if (attMeta != null) {
      AttributeKey<?> attKey = attMeta.getKey();
      element.addAttribute(
          attKey, ObjectConverter.getValue(value, attKey.getDatatype()));
    } else {
      element.addAttribute(qn, value);
    }
  }

  /**
   * Default child handler for xml.  This will parse into a {@link Element} if
   * the element has not been declared, otherwise it will parse into the type
   * defined by the metadata.
   * 
   * @throws ParseException if a non-repeating element is repeated, or if the
   *     element type requested cannot be created.
   * @throws IOException from overriding code.  Not thrown by the default
   *     implementation.
   */
  @Override
  public XmlHandler getChildHandler(QName qName, Attributes attrs,
      List<XmlNamespace> namespaces)
      throws ParseException, IOException {
    
    Element childElement = createChildElement(qName);
    ElementMetadata<?, ?> childMetadata = childElement.getMetadata();
    
    // "SET" cardinality elements cannot be added to the parent element until
    // fully initialized, otherwise we'll have duplicates.  So we track the
    // parent element and add to it after the element has been processed.
    Element parent;
    if (childMetadata.getCardinality() == Cardinality.SET) {
      parent = element;
    } else {
      element.addElement(childElement);
      parent = null;
    }
    return createHandler(qName, parent, childElement);
  }
  
  /**
   * Hook to allow subclasses to change the type of handler being returned.
   */
  protected XmlHandler createHandler(QName qName, Element parent,
      Element child) {
    return new XmlHandler(vc, parent, child);
  }
  
  /**
   * Create a child element for the given qualified name.
   */
  protected Element createChildElement(QName qName) throws ParseException {
    Element childElement;
    
    ElementMetadata<?, ?> metadata = element.getMetadata();
    ElementMetadata<?, ?> childMeta = metadata.findElement(qName);
    if (childMeta == null) {
      // qualified name has not been declared (foreign xml)
      childElement = new Element(qName);
    } else if (childMeta.getCardinality() == Cardinality.SINGLE
        && element.hasElement(childMeta.getKey())) {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.elementNotRepeatable);
      pe.setInternalReason("Element is not repeatable: " + qName);
      throw pe;
    } else {
      try {
        childElement = childMeta.createElement();
      } catch (ContentCreationException e) {
        // to ElementHandler interface?
        throw new ParseException(e);
      }
    }
    return childElement;
  }

  @Override
  public void processEndElement() throws ParseException {

    if (value != null && !value.trim().equals("")) {
      ElementKey<?, ?> elementKey = element.getElementKey();
      element.setTextValue(
          ObjectConverter.getValue(value, elementKey.getDatatype()));
    }
    
    if (parentElement != null) {
      parentElement.addElement(element);
    }
  }
}
