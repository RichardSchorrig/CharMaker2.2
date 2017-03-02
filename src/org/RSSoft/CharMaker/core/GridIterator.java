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
 * This class provides values for iterating through the datagrid.
 * you can initialize the griditerator with a rotation and mirror values and it
 * calculates the x and y iterator for iterating through the data grid in the right
 * direction
 * @author Richard
 */
public class GridIterator {
  
  public int xBegin;
  public int xEnd;
  public int xDirection;
  public int x;
  
  public int yBegin;
  public int yEnd;
  public int yDirection;
  public int y;
  
  public final static int ROTATION_0 = 0;
  public final static int ROTATION_90 = 1;
  public final static int ROTATION_180 = 2;
  public final static int ROTATION_270 = 3;
  
  public final static int MIRROR_NONE = 0;
  public final static int MIRROR_HORIZONTAL = 1;
  public final static int MIRROR_VERTICAL = 2;
  public final static int MIRROR_HORIZONTAL_VERTICAL = 3;
  
  private int rotation;
  
  /**
   * initialize the grid iterator
   * @param grid the grid to iterate through, used for size information
   * @param rotation the rotation value
   * @param mirror
   * @throws Exception 
   */
  public GridIterator(DataGrid grid, int rotation, int mirror) throws Exception
  {
    this(grid, rotation, mirror, null);
  }
  
  public GridIterator(DataGrid grid, int rotation, int mirror, GridIteratorOffset offset) throws Exception
  {
    boolean mirrorHorizontal = false;
    boolean mirrorVertical = false;
    switch (mirror) {
      case HeaderWriter.MIRROR_NONE: {
        mirrorHorizontal = false;
        mirrorVertical = false;
      } break;
      case HeaderWriter.MIRROR_HORIZONTAL: {
        mirrorHorizontal = true;
        mirrorVertical = false;
      } break;
      case HeaderWriter.MIRROR_VERTICAL: {
        mirrorHorizontal = false;
        mirrorVertical = true;
      } break;
      case HeaderWriter.MIRROR_HORIZONTAL_VERTICAL: {
        mirrorHorizontal = true;
        mirrorVertical = true;
      } break;
    }
    this.init(grid, rotation, mirrorHorizontal, mirrorVertical, offset);
  }
  
  public GridIterator(DataGrid grid, int rotation, boolean mirrorHorizontal, boolean mirrorVertical) throws Exception
  {
    this.init(grid, rotation, mirrorHorizontal, mirrorVertical, null);
  }
  
  public GridIterator(DataGrid grid, int rotation, boolean mirrorHorizontal, boolean mirrorVertical, GridIteratorOffset offset) throws Exception
  {
    this.init(grid, rotation, mirrorHorizontal, mirrorVertical, offset);
  }
  
  private void init(DataGrid grid, int rotation, boolean mirrorHorizontal, boolean mirrorVertical, GridIteratorOffset offset) throws Exception
  {
    this.rotation = rotation;             
    
    switch (rotation) {
      case HeaderWriter.ROTATION_0: {
        xBegin = mirrorHorizontal ? grid.getXSize()-1 : 0;
        yBegin = mirrorVertical ? 0 : grid.getYSize()-1;
        xEnd = mirrorHorizontal ? 0 : grid.getXSize();
        yEnd = mirrorVertical ? grid.getYSize() : 0;
        xDirection = mirrorHorizontal ? -1 : 1;
        yDirection = mirrorVertical ? 1 : -1;
        x = xBegin;
        y = yBegin;
      } break;
      case HeaderWriter.ROTATION_90: {
        xBegin = mirrorVertical ? grid.getYSize()-1 : 0;
        yBegin = mirrorHorizontal ? grid.getXSize()-1 : 0;
        xEnd = mirrorVertical ? 0 : grid.getYSize();
        yEnd = mirrorHorizontal ? 0 : grid.getXSize();
        xDirection = mirrorVertical ? -1 : 1;
        yDirection = mirrorHorizontal ? -1 : 1;
        x = xBegin;
        y = yBegin;
      } break;
      case HeaderWriter.ROTATION_180: {
        xBegin = mirrorHorizontal ? 0 : grid.getXSize()-1;
        yBegin = mirrorVertical ? grid.getYSize()-1 : 0;
        xEnd = mirrorHorizontal ? grid.getXSize() : 0;
        yEnd = mirrorVertical ? 0 : grid.getYSize();
        xDirection = mirrorHorizontal ? 1 : -1;
        yDirection = mirrorVertical ? -1 : 1;
        x = xBegin;
        y = yBegin;
      } break;
      case HeaderWriter.ROTATION_270: {
        xBegin = mirrorVertical ? 0 : grid.getYSize()-1;
        yBegin = mirrorHorizontal ? 0 : grid.getXSize()-1;
        xEnd = mirrorVertical ? grid.getYSize() : 0;
        yEnd = mirrorHorizontal ? grid.getXSize() : 0;
        xDirection = mirrorVertical ? 1 : -1;
        yDirection = mirrorHorizontal ? 1 : -1;
        x = xBegin;
        y = yBegin;
      } break;
      default: {
        throw new Exception("invalid parameter");
      }      
    }
    if (offset != null) {
      if (offset.xOffset != -1) {
        xBegin = xBegin < xEnd ? xBegin + offset.xOffset : offset.xEnd > xBegin ? xBegin : offset.xEnd;
        xEnd = xEnd > xBegin ? offset.xEnd > xEnd ? xEnd : offset.xEnd : xEnd + offset.xOffset;
      }
      
      if (offset.yOffset != -1) {
        yBegin = yBegin < yEnd ? yBegin + offset.yOffset : offset.yEnd > yBegin ? yBegin : offset.yEnd;
        yEnd = yEnd > yBegin ? offset.yEnd > yEnd ? yEnd : offset.yEnd : yEnd + offset.yOffset;
      }
    }
  }
  
  public int getColumnIterator()
  {
    if (this.rotation == HeaderWriter.ROTATION_0 || this.rotation == HeaderWriter.ROTATION_180)
      return this.x;
    else
      return this.y;
  }
  
  public int getRowIterator()
  {
    if (this.rotation == HeaderWriter.ROTATION_0 || this.rotation == HeaderWriter.ROTATION_180)
      return this.y;
    else
      return this.x;
  }
  
  public boolean conditionX()
  {
    boolean retVal = false;
    if (this.xDirection == -1)
    {
      if (this.x >= this.xEnd)
        retVal = true;
    }
    else // if (this.xDirection == 1)
    {
      if (this.x < this.xEnd)
        retVal = true;
    }    
    return retVal;
  }
  
  public boolean conditionY()
  {
    boolean retVal = false;
    if (this.yDirection == -1)
    {
      if (this.y >= this.yEnd)
        retVal = true;
    }
    else // if (this.yDirection == 1)
    {
      if (this.y < this.yEnd)
        retVal = true;
    }    
    return retVal;
  }
  
}
