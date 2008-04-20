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


package com.google.gdata.data.youtube;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;

/**
 * A YouTube playlist entry.
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_PLAYLIST)
public class PlaylistEntry extends VideoEntry {

  /** Creates an empty playlist entry. */
  public PlaylistEntry() {
    setKindCategory();
  }

  /** Creates a copy of another entry. */
  public PlaylistEntry(BaseEntry original) {
    super(original);
    setKindCategory();
  }

  private void setKindCategory() {
    // PlaylistEntry extends VideoEntry, but they're still different kinds
    getCategories().clear();
    EntryUtils.addKindCategory(this, YouTubeNamespace.KIND_PLAYLIST);
  }

  /**
   * Declares extensions that can appear in this entry.
   *
   * The namespaces should be declared by the feeds themselves
   * if they want them to appear in the top element of the feed.
   *
   * @param extProfile profile to register extensions to
   */
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    
    extProfile.declare(PlaylistEntry.class, YtPosition.class);
    extProfile.declare(PlaylistEntry.class, YtDescription.class);
    extProfile.declareArbitraryXmlExtension(PlaylistEntry.class);
  }

  /** 
   * Gets the 0-based position of this entry in the playlist.
   *
   * @return the position or null if not found
   */
  public Integer getPosition() {
    YtPosition position = getExtension(YtPosition.class);
    return position == null ? null : position.getPosition();
  }

  /**
   * Sets the 0-based position of this entry in the playlist.
   *
   * @param position the new position
   */
  public void setPosition(Integer position) {
    if (position == null) {
      removeExtension(YtPosition.class);
    } else {
      setExtension(new YtPosition(position));
    }
  }

  /** Gets the playlist description. */
  public String getDescription() {
    YtDescription description = getExtension(YtDescription.class);
    return description == null ? null : description.getContent();
  }

  /** Sets the playlist description. */
  public void setDescription(String description) {
    if (description == null) {
      removeExtension(YtDescription.class);
    } else {
      setExtension(new YtDescription(description));
    }
  }
}