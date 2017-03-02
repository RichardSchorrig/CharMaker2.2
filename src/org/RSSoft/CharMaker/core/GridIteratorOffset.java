/*
 * Copyright (C) 2017 Richard Schorrig.
 * richard.schorrig@web.de
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.RSSoft.CharMaker.core;

/**
 * a class used for offset information
 * if values are -1, do not use the information
 * @author Richard
 */
public class GridIteratorOffset {
  public int xOffset;
  public int yOffset;
  public int xEnd;
  public int yEnd;
  
  /**
   * inits all fields to -1;
   */
  public GridIteratorOffset() {
    this.xOffset = -1;
    this.xEnd = -1;
    this.yOffset = -1;
    this.yEnd = -1;
  }
}
