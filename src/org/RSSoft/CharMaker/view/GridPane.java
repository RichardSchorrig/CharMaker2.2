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

import java.awt.BasicStroke;
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
  private DataGrid previewGrid;
  
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
    previewGrid = new DataGrid(8, 8);
    
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
    paintComponent(g, false);
  }
  
  public void paintComponent(Graphics g, boolean preview)
  {
    super.paintComponent(g);
    this.calculateDimensions();
    this.fillAllGrid((Graphics2D) g, false);
    this.fillAllGrid((Graphics2D) g, true);
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
    previewGrid.changeGrid(col, rows);
    
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
    this.previewGrid.changeGrid(grid.getXSize(), grid.getYSize());
    
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
    
    if (xPos >= grid.getXSize())
      xPos = grid.getXSize() - 1;
    
    if (yPos >= grid.getYSize())
      yPos = grid.getYSize() - 1;
    
    rv.x = xPos;
    rv.y = yPos;
  }
  
  private void fillOneGrid(DataGridPosition pos)
  {    
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
    this.paintComponent(g2);
    g2.dispose();
  }
  
  /**
   * only to be called when startFillGrid and endFillGrid are not null
   */
  private boolean fillLine()
  {   
    previewGrid.clearGrid();
    
    boolean set = true;
    try {
      set = !grid.isSetAt(startFillGrid);
    } catch (Exception ex) {
    }
    
    Graphics2D g2 = (Graphics2D) this.getGraphics();
    this.paintComponent(g2);
   
    int dx = endFillGrid.x - startFillGrid.x;
    int dy = endFillGrid.y - startFillGrid.y;
    int directionX = dx < 0 ? -1 : 1;
    int directionY = dy < 0 ? -1 : 1;
    
    dx = dx * directionX + 1;
    dy = dy * directionY + 1;
      
    boolean startX = true;    
    int m = 0;
    
    if ((dx == 0 || dy == 0))
    {
      if (dy < dx)
      {
        startX = true;
        m = dx;
      }
      else
      {
        m = dy;
        startX = false;
      }
    }
    else if (dx > dy)
    {
      m = (dx) / (dy);
    }
    else
    {
      m = (dy) / (dx);
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
          while (i != m)
          {
            previewGrid.setAt(x, y, true);

            x += directionX;
            i += directionM;
          }
          
          y += directionY;
          if (dy != 0)
          {
            dy -= 1;
          }
        } while (dy != 0);
      }    
      else
      {
        do {
          int i = 0;
          while (i != m)
          {
            previewGrid.setAt(x, y, true);

            y += directionY;
            i += directionM;
          }

          x += directionX;
          if (dx != 0)
          {
            dx -= 1;
          }
        } while (dx != 0);
      }
    } catch (Exception ex)
    {
      
    }
    this.paintComponent(g2, true);
    g2.dispose();    
    
    return set;
  }
  
  public void previewFill(Point start, Point end)
  {    
    getPositionFrom(start, tempPositionX);
    getPositionFrom(end, tempPositionY);
    
    if (!startFillGrid.equals(tempPositionX) || !endFillGrid.equals(tempPositionY))
    {
      startFillGrid = new DataGridPosition(tempPositionX);
      endFillGrid = new DataGridPosition(tempPositionY);
      fillLine();
      drawSelection();
    }      
  }
  
  public void fill(Point start, Point end)
  {
    getPositionFrom(start, startFillGrid);
    getPositionFrom(end, endFillGrid);
    
    boolean add = this.fillLine();
    
    if (add)
    {
      grid.addGrid(previewGrid);
    }
    else
    {
      grid.substractGrid(previewGrid);
    }
    
    //grid.copy(previewGrid);
    previewGrid.clearGrid();
    
    this.repaint();
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
   
    fillOneGrid(pos);
    
    return pos;    
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
 
  private void fillAllGrid(Graphics2D g2, boolean preview)
  {
    if (preview)
    {
      g2.setColor(Color.CYAN);
    }
    else
    {
      g2.setColor(Color.black);
    }
    
    int xPos = 0;
    int yPos = 0;
    DataGrid g = grid;
    if (preview)
    {
      g = previewGrid;
    }
    for (boolean arr[] : g.getArray())
    {      
      for (boolean b : arr)
      {
        if (b)
        {
          rectangle.setFrame(stepSize*xPos, stepSize*yPos, stepSize, stepSize);
          g2.fill(rectangle);
          g2.draw(rectangle);
        }

        yPos += 1;
      }
      xPos += 1;
      yPos = 0;
    }
  }
  
  private void drawSelection()
  {
    Graphics2D g2 = (Graphics2D) this.getGraphics();
    //this.paintComponent(g2);
    
    g2.setColor(Color.RED);
    g2.setStroke(new BasicStroke(4));
    
    Line2D.Double selectionLine = new Line2D.Double();
    
    double xStart, yStart, xEnd, yEnd;
    xStart = (startFillGrid.x < endFillGrid.x ? startFillGrid.x : endFillGrid.x) * stepSize;
    yStart = (startFillGrid.y < endFillGrid.y ? startFillGrid.y : endFillGrid.y) * stepSize;
    xEnd = ((startFillGrid.x > endFillGrid.x ? startFillGrid.x : endFillGrid.x) + 1) * stepSize;
    yEnd = ((startFillGrid.y > endFillGrid.y ? startFillGrid.y : endFillGrid.y) + 1) * stepSize;
    
    System.out.println(String.format("Selection at %f %f - %f %f", xStart, yStart, xEnd, yEnd));
    
    selectionLine.setLine(xStart, yStart, xEnd, yStart);
    g2.draw(selectionLine);
    selectionLine.setLine(xEnd, yStart, xEnd, yEnd);
    g2.draw(selectionLine);    
    selectionLine.setLine(xEnd, yEnd, xStart, yEnd);
    g2.draw(selectionLine);
    selectionLine.setLine(xStart, yEnd, xStart, yStart);
    g2.draw(selectionLine);
    
    g2.dispose();
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
