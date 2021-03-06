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

import java.awt.image.Raster;
import java.io.Serializable;
import java.util.logging.Level;
import org.RSSoft.CharMaker.control.models.FontSettings;
import org.RSSoft.CharMaker.util.RSLogger;

/**
 * This class represents a pixel grid relying on a boolean[][] array.
 * Several modifications are possible, including copy, convert (from java.awt.image.Raster)
 * and serializing it to a file
 * @author Richard
 */
public class DataGrid implements Serializable
{
  static final long serialVersionUID = 225;
  
  private boolean grid[][];
  protected int xSize;
  protected int ySize;
  
  /**
   * construct a new data grid of xGrid, yGrid size
   * @param xGrid the width of the grid, if 0, a grid with the width of 1 is created
   * @param yGrid the height of the grid, may not be 0
   */
  public DataGrid(int xGrid, int yGrid)
  {
    if (xGrid == 0)
      this.xSize = 1;
    else
      this.xSize = xGrid;
    
    this.ySize = yGrid;
    grid = new boolean[xSize][ySize];
  }
  
  /**
   * replaces the grid by a new one with xGrid, yGrid size
   * @param xGrid the width of the grid, if 0, a grid with the width of 1 is created
   * @param yGrid the height of the grid, may not be 0
   */
  public void changeGrid(int xGrid, int yGrid)
  {
    if (xGrid == 0)
      this.xSize = 1;
    else
      this.xSize = xGrid;
    
    this.ySize = yGrid;
    grid = new boolean[xSize][ySize];
  }
  
  /**
   * clears all set points in the grid.
   */
  public void clearGrid()
  {
    grid = new boolean[xSize][ySize];
  }
  
  /**
   * sets all points in the grid.
   */
  public void setAll()
  {
    int x = 0;
    int y = 0;
    for (boolean arr[] : grid)
    {      
      for (boolean b : arr)
      {
        this.grid[x][y] = true;
        y += 1;
      }
      y = 0;
      x += 1;
    }
  }
  
  /**
   * invert all points in the grid.
   */
  public void invertAll()
  {
        int x = 0;
    int y = 0;
    for (boolean arr[] : grid)
    {      
      for (boolean b : arr)
      {
        this.grid[x][y] = !this.grid[x][y];
        y += 1;
      }
      y = 0;
      x += 1;
    }
  }
  
  /**
   * returns the width of the grid
   * @return the width of the grid
   */
  public int getXSize()
  {
    return xSize;
  }
  
  /**
   * returns the height of the grid
   * @return the height of the grid
   */
  public int getYSize()
  {
    return ySize;
  }
  
  /**
   * returns the grid in form of a boolean[][] array
   * @return the grid as array
   */
  public boolean[][] getArray()
  {
    return grid;
  }
  
  /**
   * or another grid to this grid (the pixels set in the other grid will 
   * be set in this grid)
   * @param anotherGrid the other grid to logical or.
   */
  public void orGrid(DataGrid anotherGrid)
  {
    int dimX = this.xSize < anotherGrid.xSize ? this.xSize : anotherGrid.xSize;
    int dimY = this.ySize < anotherGrid.ySize ? this.ySize : anotherGrid.ySize;
    
    for (int x = 0; x < dimX; x += 1)
    {
      for (int y = 0; y < dimY; y += 1)
      {
        System.out.println(String.format("Setting grid at %d %d to %s", x, y, anotherGrid.grid[x][y] ? "true" : "false"));
        this.grid[x][y] |= anotherGrid.grid[x][y];
      }
    }
  }
  
  /**
   * ands another grid to this grid (the pixels set in this grid and in
   * the other grid will be set in this grid, intersect)
   * @param anotherGrid the other grid to logical and.
   */
  public void andGrid(DataGrid anotherGrid)
  {
    int dimX = this.xSize < anotherGrid.xSize ? this.xSize : anotherGrid.xSize;
    int dimY = this.ySize < anotherGrid.ySize ? this.ySize : anotherGrid.ySize;
    
    for (int x = 0; x < dimX; x += 1)
    {
      for (int y = 0; y < dimY; y += 1)
      {
        this.grid[x][y] &= anotherGrid.grid[x][y];
      }
    }
  }
  
  /**
   * adds (logical or) another grid to this grid (the pixels set in the
   * other grid will be set in this grid)
   * @param anotherGrid the other grid to add.
   */
  public void addGrid(DataGrid anotherGrid)
  {
    this.orGrid(anotherGrid);
  }

  /**
   * substracts another grid to this grid (the pixels set in the
   * other grid will be unset in this grid)
   * @param anotherGrid the other grid to add.
   */
  public void substractGrid(DataGrid anotherGrid)
  {
    int dimX = this.xSize < anotherGrid.xSize ? this.xSize : anotherGrid.xSize;
    int dimY = this.ySize < anotherGrid.ySize ? this.ySize : anotherGrid.ySize;
    
    for (int x = 0; x < dimX; x += 1)
    {
      for (int y = 0; y < dimY; y += 1)
      {
        this.grid[x][y] &= !anotherGrid.grid[x][y];
      }
    }
  }
  
  /**
   * returns wether the grid is set at a given position
   * @param x the x position of the grid
   * @param y the y position of the grid
   * 
   * @return true if grid pixel is set, false if not
   * @throws Exception in case x or y are out of bounds
   */
  public boolean isSetAt(int x, int y) throws Exception
  {
    if (x >= this.xSize || y >= this.ySize)
      throw new Exception(String.format("the dimension %d, %d is out of bounds (max %d, %d", x, y, this.xSize-1, this.ySize-1));
    return grid[x][y];
  }
  
  /**
   * returns wether the grid is set at a given position
   * @param pos the position of the grid
   * @return true if grid pixel is set, false if not
   * @throws Exception in case x or y are out of bounds
   */
  public boolean isSetAt(DataGridPosition pos) throws Exception
  {
    return this.isSetAt(pos.getX(), pos.getY());
  }
  
  /**
   * sets the pixel at the given position to the value b
   * @param x the x position of the grid
   * @param y the y position of the grid
   * @param b the value to set (true or false)
   * @throws Exception in case x or y are out of bounds
   */
  public void setAt(int x, int y, boolean b) throws Exception
  {
    if (x >= this.xSize || y >= this.ySize)
      throw new Exception(String.format("the dimension %d, %d is out of bounds (max %d, %d", x, y, this.xSize-1, this.ySize-1));
    grid[x][y] = b;
  }
  
  /**
   * sets the pixel at the given position
   * @param x the x position of the grid
   * @param y the y position of the grid
   * @throws Exception in case x or y are out of bounds
   */
  public void setAt(int x, int y) throws Exception
  {
    this.setAt(x, y, true);
  }
  
  /**
   * sets the pixel at the given position
   * @param pos the position of the grid
   * @throws Exception in case x or y are out of bounds 
   */
  public void setAt(DataGridPosition pos) throws Exception
  {
    this.setAt(pos.getX(), pos.getY(), true);
  }
  
  /**
   * unsets the pixel at the given position
   * @param x the x position of the grid
   * @param y the y position of the grid
   * @throws Exception in case x or y are out of bounds
   */
  public void unSetAt(int x, int y) throws Exception
  {
    this.setAt(x, y, false);
  }
  
  /**
   * unsets the pixel at the given position
   * @param pos the position of the grid
   * @throws Exception in case x or y are out of bounds 
   */
  public void unSetAt(DataGridPosition pos) throws Exception
  {
    this.setAt(pos.getX(), pos.getY(), false);
  }
  
  public static DataGrid convert(Raster raster)
  {
    return DataGrid.convert(raster, 0);
  }
  
  public static DataGrid convert(Raster raster, int height)
  {
    return DataGrid.convert(raster, 0, 0, height, 0);
  }
  
  public static DataGrid convert(Raster raster, int height, int width)
  {
    return DataGrid.convert(raster, 0, 0, height, width);
  }
  
  
  /**
   * converts from java.awt.image.Raster (if pixel opacity is over 127 (max value 255)
   * the pixel is set, else unset
   * @param raster a raster with the dimensions of at least height and width
   * @param xOffset an offset into the raster
   * @param yOffset an offset into the raster
   * @param height the total pixels to analyse in height
   * @param width the total pixels to analyse in width
   * @return a new DataGrid with the raster information
   */
  public static DataGrid convert(Raster raster, int xOffset, int yOffset, int height, int width)
  {
    int xSize;
    if (width == 0)
      xSize = raster.getWidth()+xOffset+xOffset;
    else
      xSize = width;
    int ySize;
    if (height == 0)
      ySize = raster.getHeight()+yOffset+yOffset;
    else
      ySize = height;
    DataGrid dgrid = new DataGrid(xSize < 5 ? 5 : xSize, ySize < 5 ? 5 : ySize);
//    RSLogger.getLogger().log(Level.INFO, String.format("Raster details: %d,%d; %d,%d; %d", raster.getMinX(),raster.getMinY(),raster.getWidth(),raster.getHeight(),raster.getNumBands()));
    
    int xStart = xOffset > 0 ? 0 : xOffset * (-1);
    int yStart = yOffset > 0 ? 0 : yOffset * (-1);
    
    for (int i=0; i<xSize; i+=1)
    {      
      for (int j=0; j<ySize; j+=1)
      {
        if (raster.getSample(i+raster.getMinX()+xStart, j+raster.getMinY()+yStart, 0) > 127)
        {
          try {
            dgrid.setAt(i, j);
          } catch (Exception ex) {
            RSLogger.getLogger().log(Level.SEVERE, null, ex);
          }
        }
      }
      
    }
    
    return dgrid;
  }
  
  /**
   * paste a selection into the grid on the given position.
   * if the selection and the offset won't fit into the grid, the rest is skipped.
   * @param selection the data grid to paste, is not changed
   * @param xOffset the offset in x direction
   * @param yOffset the offset in y direction
   * @throws java.lang.Exception
   */
  public void paste(DataGrid selection, int xOffset, int yOffset) throws Exception
  {
    if (xOffset >= this.xSize || yOffset >= this.ySize)
      throw new Exception(String.format("the dimension %d, %d is out of bounds (max %d, %d", xOffset, yOffset, this.xSize-1, this.ySize-1));
    
    int xEnd = selection.xSize + xOffset > xSize ? xSize - xOffset : selection.xSize;
    int yEnd = selection.ySize + yOffset > ySize ? ySize - yOffset : selection.ySize;
    
    for (int x = 0; x < xEnd; x += 1)
    {
      for (int y = 0; y < yEnd; y += 1)
      {
        grid[x + xOffset][y + yOffset] = selection.grid[x][y];
      }
    }
  }
  
  /**
   * copies the entire grid
   * @return a new object containing the same data
   */
  public DataGrid copy()
  {
    DataGrid newgrid = new DataGrid(0, 0);
    copyTo(newgrid);
    return newgrid;
  }
  
  /**
   * copies the entire grid
   * @param anotherGrid an allocated object where the data is pasted into
   */
  public void copyTo(DataGrid anotherGrid)
  {
//    this.xSize = anotherGrid.xSize;
//    this.ySize = anotherGrid.ySize;
//    this.grid = new boolean[anotherGrid.xSize][anotherGrid.ySize];
//    int x = 0;
//    int y = 0;
//    for (boolean arr[] : anotherGrid.getArray())
//    {      
//      for (boolean b : arr)
//      {
//        this.grid[x][y] = b;
//        y += 1;
//      }
//      y = 0;
//      x += 1;
//    }
      try {
        copy(anotherGrid, new GridArea(0, 0, xSize, ySize), false);
      } catch (Exception ex) {
          RSLogger.getLogger().log(Level.SEVERE, null, ex);
      }
  }
  
  public void copyFrom(DataGrid anotherGrid)
  {
      this.changeGrid(anotherGrid.xSize, anotherGrid.ySize);
      try {
        paste(anotherGrid, 0, 0);
      } catch (Exception ex) {          
          RSLogger.getLogger().log(Level.SEVERE, null, ex);
      }
  }
  
  /**
   * copies an area into the given oject
   * @param selection the object where the data is copied into
   * @param xOffset the offset in x direction
   * @param yOffset the offset in y direction
   * @param cut true if the selection should be cleared after the copy, false if
   *             the selection remains
   * @throws Exception 
   */
  public void copy(DataGrid selection, int xOffset, int yOffset, boolean cut) throws Exception
  {
//    if (xOffset >= this.xSize || yOffset >= this.ySize)
//      throw new Exception(String.format("the dimension %d, %d is out of bounds (max %d, %d", xOffset, yOffset, this.xSize-1, this.ySize-1));
//    
//    int xEnd = selection.xSize + xOffset > xSize ? xSize - xOffset : selection.xSize;
//    int yEnd = selection.ySize + yOffset > ySize ? ySize - yOffset : selection.ySize;    
//    
//    for (int x = 0; x < xEnd; x += 1)
//    {
//      for (int y = 0; y < yEnd; y += 1)
//      {
//        selection.grid[x][y] = grid[x + xOffset][y + yOffset];
//        if (cut)
//        {
//          grid[x + xOffset][y + yOffset] = false;
//        }
//      }
//    }
      copy(selection, new GridArea(xOffset, yOffset), cut);
  }
  
  /**
   * copies the given area into the given grid, and cuts the area from this if wished for.
   * @param selection the object the area is copied into. If the area has no end points, 
   *        the area equals the size of <selection>. If end points are given, <selection>
   *        is resized to hold the area
   * @param area the area with absolute start and end points. The area is limited if
   *        the size exceeds the dimension of this grid.
   * @param cut set to true if the area of this grid should be removed.
   * @throws Exception in case the area does not contain valid start and end points
   */
  public void copy(DataGrid selection, GridArea area, boolean cut) throws Exception
  {
    if (area.xOffset > this.xSize || area.yOffset > this.ySize 
            || area.xEnd > this.xSize || area.yEnd > this.ySize)
      throw new Exception(String.format("the dimension start %d, %d or end %d, %d is out of bounds (max %d, %d)",
              area.xOffset, area.yOffset, area.xEnd, area.yEnd, this.xSize-1, this.ySize-1));
    
    if (area.xOffset < 0 || area.yOffset < 0) {
        throw new Exception(String.format("start point < 0, values are %d, %d", area.xOffset, area.yOffset));
    }
    
    int xEnd;
    int yEnd;
    
    if (area.xEnd < 0 || area.yEnd < 0) {
        xEnd = selection.xSize + area.xOffset > xSize ? xSize - area.xOffset : selection.xSize;
        yEnd = selection.ySize + area.yOffset > ySize ? ySize - area.yOffset : selection.ySize;
    } else {
        xEnd = area.xEnd;
        yEnd = area.yEnd;
        
        if (xEnd > xSize) {
            xEnd = xSize;
        }
        if (yEnd > ySize) {
            yEnd = ySize;
        }
        
        selection.changeGrid(xEnd, yEnd);
    }
    
    for (int x = 0; x < xEnd; x += 1)
    {
      for (int y = 0; y < yEnd; y += 1)
      {
        selection.grid[x][y] = grid[x + area.xOffset][y + area.yOffset];
        if (cut)
        {
          grid[x + area.xOffset][y + area.yOffset] = false;
        }
      }
    }
  }
  
  /**
   * copies an area of the grid and pastes the data into a new object. 
   * the new object has the size given by the start and end parameter
   * @param xStart the x start point
   * @param yStart the y start point
   * @param xEnd the x end point
   * @param yEnd the y end point
   * @param cut true if the selection should be cleared after the copy, false if
   *             the selection remains
   * @return a new object containing the selection
   * @throws Exception 
   */
  public DataGrid copy(int xStart, int yStart, int xEnd, int yEnd, boolean cut) throws Exception
  {
//    if (xStart >= this.xSize || yStart >= this.ySize)
//      throw new Exception(String.format("the dimension %d, %d is out of bounds (max %d, %d", xStart, yStart, this.xSize-1, this.ySize-1));
//    if (xEnd >= this.xSize || yEnd >= this.ySize)
//      throw new Exception(String.format("the dimension %d, %d is out of bounds (max %d, %d", xEnd, yEnd, this.xSize-1, this.ySize-1));
//    
//    if (xStart > xEnd)
//    {
//      int t = xEnd;
//      xEnd = xStart;
//      xStart = t;
//    }
//    if (yStart > yEnd)
//    {
//      int t = yEnd;
//      yEnd = yStart;
//      yStart = t;
//    }
//    
//    DataGrid selection = new DataGrid(xEnd - xStart + 1, yEnd - yStart + 1);
//    
//    copy(selection, xStart, yStart, cut);
//    
//    return selection;
      
      DataGrid selection = new DataGrid(1, 1);
      copy(selection, new GridArea(xStart, yStart, xEnd, yEnd), cut);
      return selection;
  }
  
  public void manipulate(int rotation, boolean mirrorHorizontal, boolean mirrorVertical) throws Exception
  {
    DataGrid newGrid;
    if (rotation == FontSettings.ROTATION_0 || rotation == FontSettings.ROTATION_180)
    {
      newGrid = new DataGrid(xSize, ySize);
    }
    else
    {
      newGrid = new DataGrid(ySize, xSize);
    }
    
    rotation = (rotation + 1) % 4;
    GridIterator it = new GridIterator(this, rotation, mirrorHorizontal, mirrorVertical);
    
    int x = 0;
    int y = 0;
    for (it.x = it.xBegin; it.conditionX(); it.x += it.xDirection)
    {
      for (it.y = it.yBegin; it.conditionY(); it.y += it.yDirection)
      {
        newGrid.grid[x][y] = grid[it.getColumnIterator()][it.getRowIterator()];
        System.out.println(String.format("newGrid[%d][%d] = oldGrid[%d][%d]", x, y, it.getColumnIterator(), it.getRowIterator()));
        x += 1;
      }
      x = 0;
      y += 1;
    }
    
    copyTo(newGrid);
  }
  
  public void rotate(int rotation) throws Exception
  {
    manipulate(rotation, false, false);
  }
  
  public void mirror(boolean mirrorHorizontal, boolean mirrorVertical) throws Exception
  {
    manipulate(FontSettings.ROTATION_0, mirrorHorizontal, mirrorVertical);
  }
  
  public int getSizeInBytes() {
    int nOfLines;
    nOfLines = this.ySize / 8;
    if (this.ySize % 8 != 0) {
      nOfLines += 1;
    }
    return (this.xSize * nOfLines);
  }
  
  private void setGridByByteArray(int xSize, int ySize, byte[] arr) throws Exception {    
    this.changeGrid(xSize, ySize);
    
    int pos_arr = 0;
    int nOfLines;
    nOfLines = this.ySize / 8;
    if (this.ySize % 8 != 0) {
      nOfLines += 1;
    }
    
    for (int i=0; i<nOfLines; i+=1) {
      GridArea offset = new GridArea();
      offset.yOffset = 8*i;
      offset.yEnd = 8*i + 7;
      GridIterator it = new GridIterator(this, FontSettings.ROTATION_0, FontSettings.MIRROR_NONE, offset);
      for (it.x=it.xBegin; it.conditionX(); it.x+=it.xDirection)
      {
        //int y = c.getGrid().getYSize();
        char pos = 0x80;
        byte value = 0;
        for (it.y=it.yBegin; it.conditionY(); it.y+=it.yDirection)
        {
          value = arr[pos_arr];
          if ((value & pos) != 0) {
            this.isSetAt(it.x, it.y);
          }
          pos >>= 1;
        }
        pos_arr += 1;
      }


    }
  }
  
  public byte[] getGridAsByteArray() throws Exception {
    byte[] arr;
      
    int nOfLines;
    nOfLines = this.ySize / 8;
    if (this.ySize % 8 != 0) {
      nOfLines += 1;
    }
    arr = new byte[this.xSize * nOfLines];

    int pos_arr = 0;

    for (int i=0; i<nOfLines; i+=1) {
      GridArea offset = new GridArea();
      offset.yOffset = 8*i;
      offset.yEnd = 8*i + 7;
      GridIterator it = new GridIterator(this, FontSettings.ROTATION_0, FontSettings.MIRROR_NONE, offset);
      for (it.x=it.xBegin; it.conditionX(); it.x+=it.xDirection)
      {
        //int y = c.getGrid().getYSize();
        int pos = 0;
        byte value = 0;
        for (it.y=it.yBegin; it.conditionY(); it.y+=it.yDirection)
        {
          int b;
          if (this.isSetAt(it.getColumnIterator(), it.getRowIterator()))
          {
            b = 1;
          }
          else
          {
            b = 0;
          }
          value += b << pos;
          pos += 1;
        }
        arr[pos_arr] = value;
        pos_arr += 1;
      }


    }

    return arr;
  }
/*  
  private void writeObject(java.io.ObjectOutputStream out) throws IOException
  {
    out.writeInt(this.xSize);
    out.writeInt(this.ySize);
    out.writeInt(this.getSizeInBytes());
    try {
      out.write(this.getGridAsByteArray());
    } catch (Exception ex) {
      RSLogger.getLogger().log(Level.SEVERE, null, ex);
    }
  }
  private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    this.xSize = in.readInt();
    this.ySize = in.readInt();
    int gridSize = in.readInt();
    byte[] arr = new byte[gridSize];
    if (gridSize != this.getSizeInBytes()) {
      RSLogger.getLogger().log(Level.SEVERE, String.format("The size %d in the Input Stream is not equal to the size given by x(%d) and y(%d) = %d",
                                        gridSize, this.xSize, this.ySize, this.getSizeInBytes()));
    }
    int readBytes = in.read(arr, 0, gridSize);
    if (readBytes != gridSize) {
      RSLogger.getLogger().log(Level.SEVERE, String.format("cannot read %d bytes from Input Stream, only %d bytes read",
                                        gridSize, readBytes));
    }
    try {
      this.setGridByByteArray(xSize, ySize, arr);
    } catch (Exception ex) {
      RSLogger.getLogger().log(Level.SEVERE, null, ex);
    }
    
  }
  private void readObjectNoData() throws ObjectStreamException 
  {
    this.changeGrid(5, 5);
  }
*/
}
