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
package org.RSSoft.CharMaker.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import javax.swing.JPanel;
import org.RSSoft.CharMaker.core.DataGrid;
import org.RSSoft.CharMaker.core.DataGridPosition;
import org.RSSoft.CharMaker.util.RSLogger;

/**
 * this pane allows the controll of a grid.
 * The grid can be filled at a certain position or along a horizontal or vertical line
 * @author Richard
 */
public class GridPane extends JPanel
{
  private Line2D.Double line;
  private Rectangle2D.Double rectangle;
  
  private DataGrid grid;
  
  private int xSize;
  private int ySize;
  private double stepSize;
  
  private DataGridPosition startFillGrid;
  private DataGridPosition endFillGrid;
  
  private DataGridPosition tempPositionX;
  private DataGridPosition tempPositionY;
  
  /**
   * construct a new pane with a 8 x 8 grid
   */
  public GridPane()
  {
    super();
    line = new Line2D.Double();
    rectangle = new Rectangle2D.Double();
    this.setBackground(Color.white);
    this.setOpaque(true);
    
    grid = new DataGrid(8, 8);
    
    startFillGrid = new DataGridPosition();
    endFillGrid = new DataGridPosition();
    
    tempPositionX = new DataGridPosition();
    tempPositionY = new DataGridPosition();
  }
  
  private void calculateDimensions()
  {
    xSize = this.getParent().getWidth();
    stepSize = xSize / grid.getXSize();
    ySize = (int) (stepSize * grid.getYSize());
    if (ySize > this.getParent().getHeight())
    {
      ySize = this.getParent().getHeight();
      stepSize = ySize / grid.getYSize();
      xSize = (int) (stepSize * grid.getXSize());
    }
  }

  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    this.calculateDimensions();
    this.fillAllGrid((Graphics2D) g);
    this.paintGrid((Graphics2D) g);    
  }

  /**
   * sets the grid size to columns, rows
   * @param columns the grid x size
   * @param rows the grid y size
   */
  public void setGrid(int columns, int rows)
  {
    int col;
    if (columns != 0)
    {
      col = columns;
    }
    else
    {
      col = 1;
    }
    grid.changeGrid(col, rows);
    
    this.repaint();
    
  }
  
  /**
   * returns the underlying DataGrid data
   * @return the DataGrid grid
   */
  public DataGrid getGrid()
  {
    return this.grid;
  }
  
  /**
   * sets all pixels according to dgrid
   * @param dGrid the grid to show
   */
  public void setGrid(DataGrid dGrid)
  {
    this.grid.copy(dGrid);
    
    this.repaint();
  }
  
  /**
   * returns the position as DataGridPosition of the point p
   * @param p the point to calculate
   * @return a new DataGridPosition representing p
   */
  private DataGridPosition getPositionFrom(Point p)
  {
    DataGridPosition pos = new DataGridPosition();
    getPositionFrom(p, pos);
    return pos;
  }
  
    /**
   * returns the position as DataGridPosition of the point p
   * @param p the point to calculate
   * @param rv an object were the result is saved, no new Object is allocated
   */
  private void getPositionFrom(Point p, DataGridPosition rv)
  {
    int xPos = (int) (p.getX() / stepSize);
    int yPos = (int) (p.getY() / stepSize);
    
    if (xPos > grid.getXSize())
      xPos = grid.getXSize();
    
    if (xPos > grid.getYSize())
      xPos = grid.getYSize();
    
    rv.x = xPos;
    rv.y = yPos;
    
    System.out.println(String.format("Position at %d %d", xPos, yPos));
  }
  
  /**
   * toggles one pixel.
   * If grid is set, the position is cleared, else it is set
   * @param p the point on the pane to toggle
   * @return a position of the point toggled in the grid
   */
  public DataGridPosition fillOneGrid(Point p)
  {
    this.repaint();
    DataGridPosition pos = getPositionFrom(p);
    
    rectangle.setFrame(pos.getX()*stepSize, pos.getY()*stepSize, stepSize, stepSize);
    Graphics2D g2 = (Graphics2D) this.getGraphics();
    try {
      if (grid.isSetAt(pos))
      {
        g2.setColor(Color.white);
        grid.unSetAt(pos);
      }
      else
      {
        g2.setColor(Color.black);
        grid.setAt(pos);
      }
      g2.fill(rectangle);
      g2.draw(rectangle);
    }
    catch (Exception ex) {
      RSLogger.getLogger().log(Level.WARNING, null, ex);
    }
    this.paintGrid(g2);
    g2.dispose();
    
    return pos;    
  }
  
  /**
   * only to be called when startFillGrid and endFillGrid are not null
   */
  private void fillLine(boolean permanent)
  {
    //this.repaint();
    if (null == startFillGrid || null == endFillGrid)
    {
      return;
    }
    
    boolean set = false;
    
    Graphics2D g2 = (Graphics2D) this.getGraphics();
    try {
      if (grid.isSetAt(startFillGrid))
      {
        g2.setColor(Color.white);
        set = false;
      }
      else
      {
        g2.setColor(Color.black);
        set = true;
      }
      g2.fill(rectangle);
    }
    catch (Exception ex) {
      RSLogger.getLogger().log(Level.SEVERE, null, ex);
    }
    
    int dx = endFillGrid.x - startFillGrid.x;
    int dy = endFillGrid.y - startFillGrid.y;
    int directionX = dx < 0 ? -1 : 1;
    int directionY = dy < 0 ? -1 : 1;
    
    System.out.println(String.format("dx: %d, dy: %d", dx, dy));
      
    boolean startX = true;    
    int m = 0;
    
    dx = dx * directionX;
    dy = dy * directionY;
    
    if ((dx == 0 || dy == 0))
    {
      if (dy < dx)
      {
        startX = false;
      }
    }
    else if (dx > dy)
    {
      m = dx / dy;
    }
    else
    {
      m = dy / dx;
      startX = false;
    }
    
    int directionM = m < 0 ? -1 : 1;
    int x = startFillGrid.x;
    int y = startFillGrid.y;
    try
    {
      if (startX)
      {
        do {
          int i = 0;
          do {         
            rectangle.setFrame(x*stepSize, y*stepSize, stepSize, stepSize);
            g2.draw(rectangle);
            System.out.println(String.format("StartX: drawing at %d %d", x, y));
            if (permanent)
            {
              grid.setAt(x, y, set);
            }
            if (i != m)
            {
              x += directionX;
              i += directionM;
            }
          } while (i != m);

          if (y != endFillGrid.y)
          {
            y += directionY;
          }
        } while (y != endFillGrid.y);
      }    
      else
      {
        do {
          int i = 0;
          do {          
            rectangle.setFrame(x*stepSize, y*stepSize, stepSize, stepSize);
            g2.draw(rectangle);          
            System.out.println(String.format("StartY: drawing at %d %d", x, y));
            if (permanent)
            {
              grid.setAt(x, y, set);
            }
            if (i != m)
            {
              y += directionY;
              i += directionM;
            }
          } while (i != m);

          if (x != endFillGrid.x)
          {
            x += directionX;
          }
        } while (x != endFillGrid.x);
      }
    } catch (Exception ex)
    {
      
    }
    
    this.paintGrid(g2);
    g2.dispose();
  }
  
  public void previewFill(Point start, Point end)
  {
    boolean refresh = false;
    
    getPositionFrom(start, tempPositionX);
    getPositionFrom(end, tempPositionY);
    
    if (!startFillGrid.equals(tempPositionX) || !endFillGrid.equals(tempPositionY))
    {
      startFillGrid = new DataGridPosition(tempPositionX);
      endFillGrid = new DataGridPosition(tempPositionY);
      refresh = true;
    }
/*    
    if (null == startFillGrid)
    {
      System.out.println("new start point");
      getPositionFrom(start, tempPosition);
      startFillGrid = new DataGridPosition(tempPosition);
      refresh = true;
    }
    if (null == endFillGrid)
    {
      System.out.println("new end point");
      getPositionFrom(end, tempPosition);
      endFillGrid = new DataGridPosition(tempPosition);
      refresh = true;
    }
  
    if (!refresh)
    {
      getPositionFrom(end, tempPosition);
      if (!endFillGrid.equals(tempPosition))
      {
        endFillGrid = new DataGridPosition(tempPosition);
        refresh = true;
      }
    }
*/    
    if (refresh)
    {
      fillLine(false);
    }    
  }
  
  public void fill(Point start, Point end)
  {
    getPositionFrom(start, startFillGrid);
    getPositionFrom(end, endFillGrid);
    
    fillLine(true);
  }
  
  public void previewFillLine(Point start, Point end) {
    int xPosStart = (int) (start.getX() / stepSize);
    int yPosStart = (int) (start.getY() / stepSize);
    
    int xPosEnd = (int) (end.getX() / stepSize);
    int yPosEnd = (int) (end.getY() / stepSize);
    
    if (xPosStart > grid.getXSize())
      xPosStart = grid.getXSize();
    
    if (yPosStart > grid.getYSize())
      yPosStart = grid.getYSize();
    
    if (xPosEnd > grid.getXSize())
      xPosEnd = grid.getXSize();
    
    if (yPosEnd > grid.getYSize())
      yPosEnd = grid.getYSize();
    
    DataGridPosition pos = new DataGridPosition(xPosStart, yPosStart);
    
    boolean set;
    
    Graphics2D g2 = (Graphics2D) this.getGraphics();
    try {
      if (grid.isSetAt(pos))
      {
        g2.setColor(Color.white);
        set = false;
      }
      else
      {
        g2.setColor(Color.black);
        set = true;
      }
      g2.fill(rectangle);
    }
    catch (Exception ex) {
      RSLogger.getLogger().log(Level.WARNING, null, ex);
    }
    
    int x = xPosStart < xPosEnd ? xPosEnd - xPosStart : xPosStart - xPosEnd;
    int y = yPosStart < yPosEnd ? yPosEnd - yPosStart : yPosStart - yPosEnd;
    
    int xStart = xPosStart < xPosEnd ? xPosStart : xPosEnd;
    int yStart = yPosStart < yPosEnd ? yPosStart : yPosEnd;
    
    if (x >= y) {
      for (int i=0; i<x; i+=1) {
        rectangle.setFrame((xStart+i)*stepSize, yPosStart*stepSize, stepSize, stepSize);
        g2.draw(rectangle);
      }
    }
    else {
      for (int i=0; i<y; i+=1) {
        rectangle.setFrame(xPosStart*stepSize, (yStart+i)*stepSize, stepSize, stepSize);
        g2.draw(rectangle);
      }
    }
    this.paintGrid(g2);
    g2.dispose();
  }
/*  
  public DataGridPosition fillLine(Point start, Point end) {
    int xPosStart = (int) (start.getX() / stepSize);
    int yPosStart = (int) (start.getY() / stepSize);
    
    int xPosEnd = (int) (end.getX() / stepSize);
    int yPosEnd = (int) (end.getY() / stepSize);
    
    if (xPosStart > grid.getXSize())
      xPosStart = grid.getXSize();
    
    if (yPosStart > grid.getYSize())
      yPosStart = grid.getYSize();
    
    if (xPosEnd > grid.getXSize())
      xPosEnd = grid.getXSize();
    
    if (yPosEnd > grid.getYSize())
      yPosEnd = grid.getYSize();
    
    DataGridPosition pos = new DataGridPosition(xPosStart, yPosStart);
    
    boolean set = false;
    
    Graphics2D g2 = (Graphics2D) this.getGraphics();
    try {
      if (grid.isSetAt(pos))
      {
        g2.setColor(Color.white);
        grid.unSetAt(pos);
        set = false;
      }
      else
      {
        g2.setColor(Color.black);
        grid.setAt(pos);
        set = true;
      }
      g2.fill(rectangle);
    }
    catch (Exception ex) {
      RSLogger.getLogger().log(Level.WARNING, null, ex);
    }
    
    int x = xPosStart < xPosEnd ? xPosEnd - xPosStart : xPosStart - xPosEnd;
    int y = yPosStart < yPosEnd ? yPosEnd - yPosStart : yPosStart - yPosEnd;
    
    int xStart = xPosStart < xPosEnd ? xPosStart : xPosEnd;
    int yStart = yPosStart < yPosEnd ? yPosStart : yPosEnd;
    
    try {
      if (x >= y) {
        for (int i=0; i<x; i+=1) {
          rectangle.setFrame((xStart+i)*stepSize, yPosStart*stepSize, stepSize, stepSize);
          g2.draw(rectangle);
          if (set) {
            grid.setAt(xStart+i, yPosStart);
          }
          else {
            grid.unSetAt(xStart+i, yPosStart);
          }
        }
      }
      else {
        for (int i=0; i<y; i+=1) {
          rectangle.setFrame(xPosStart*stepSize, (yStart+i)*stepSize, stepSize, stepSize);
          g2.draw(rectangle);
          if (set) {
            grid.setAt(xPosStart, yStart+i);
          }
          else {
            grid.unSetAt(xPosStart, yStart+i);
          }
        }
      }
    }
    catch (Exception ex) {
      RSLogger.getLogger().log(Level.WARNING, null, ex);
    }
    this.paintGrid(g2);
    g2.dispose();
    return pos;
  }
*/  
  private void fillAllGrid(Graphics2D g2)
  {
    int xPos = 0;
    int yPos = 0;
    for (boolean arr[] : grid.getArray())
    {      
      for (boolean b : arr)
      {
        if (b)
        {
          g2.setColor(Color.black);
        }
        else
        {
          g2.setColor(Color.white);          
        }
        rectangle.setFrame(stepSize*xPos, stepSize*yPos, stepSize, stepSize);
        g2.fill(rectangle);
        g2.draw(rectangle);
        yPos += 1;
      }
      xPos += 1;
      yPos = 0;
    }
  }
  
  private void paintGrid(Graphics2D g2)
  {    
    g2.setColor(Color.black);
    int i;
    for (i=1; i<grid.getXSize(); i+=1)
    {
      line.setLine(stepSize*i, 0, stepSize*i, ySize);
      g2.draw(line);
    }
    for (i=1; i<grid.getYSize(); i+=1)
    {
      line.setLine(0, stepSize*i, xSize, stepSize*i);
      g2.draw(line);
    }
    this.setSize((int) (grid.getXSize() * stepSize), (int) (grid.getYSize() * stepSize));
  }
}
