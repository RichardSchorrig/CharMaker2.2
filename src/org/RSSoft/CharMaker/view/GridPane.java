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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import javax.swing.JPanel;
import org.RSSoft.CharMaker.control.ControlGridTransform;
import org.RSSoft.CharMaker.control.drawaction.DrawAction;
import org.RSSoft.CharMaker.control.drawaction.DrawActionSelect;
import org.RSSoft.CharMaker.control.drawaction.DrawActionStraightLine;
import org.RSSoft.CharMaker.core.DataGrid;
import org.RSSoft.CharMaker.core.DataGridPosition;
import org.RSSoft.CharMaker.util.RSLogger;

/**
 * this pane allows the controll of a grid.
 * The grid can be filled at a certain position or along a horizontal or vertical line
 * @author Richard
 */
public class GridPane extends JPanel implements KeyListener
{
  private Line2D.Double line;
  private Rectangle2D.Double rectangle;
  
  private DataGrid grid;
  private DataGrid previewGrid;
  private DataGrid copyGrid;
  
  private int xGridSize;
  private int yGridSize;
  private double stepSize;
  
  private DataGridPosition startFillGrid;
  private DataGridPosition endFillGrid;
  
  private DataGridPosition tempStart;
  private DataGridPosition tempEnd;
  
  private ControlGridTransform transformController;
  
  private DrawAction currentDrawMode;
  private boolean selectMode;
  private boolean selectionValid;
  
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
    
    tempStart = new DataGridPosition();
    tempEnd = new DataGridPosition();
    
    this.transformController = null;
    
    this.currentDrawMode = new DrawActionStraightLine();
    this.selectMode = false;
    this.selectionValid = false;
    
    this.addKeyListener(this);
  }
  
  public void setSelectMode(boolean selectMode)
  {
    this.selectMode = selectMode;
    
    if (!selectMode)
    {
      selectionValid = false;
      transformController.setActive(false);
      repaint();
    }
  }
  
  public void copy(boolean cut)
  {
    if (selectMode && selectionValid)
    {
      System.out.println("trying to copy / cut");
      try {      
        copyGrid = grid.copy(startFillGrid.x, startFillGrid.y, endFillGrid.x, endFillGrid.y, cut);
      } catch (Exception ex) {
        RSLogger.getLogger().log(Level.SEVERE, null, ex);
      }
    }
  }
  
  public void cut()
  {
    copy(true);
  }
  
  public void copy()
  {
    copy(false);
  }
  
  public void paste()
  {
    if (selectMode && selectionValid)
    {
      System.out.println("trying to paste");
      try {
        grid.paste(copyGrid, startFillGrid.x, startFillGrid.y);
        copyGrid = null;
      } catch (Exception ex) {
        RSLogger.getLogger().log(Level.SEVERE, null, ex);
      }
    }
  }
  
  
  public boolean moveSelection(int x, int y)
  {
    boolean retVal = false;
    if (selectMode && selectionValid)
    {
      int xResStart = startFillGrid.x + x;
      int yResStart = startFillGrid.y + y;
      int xResEnd = endFillGrid.x + x;
      int yResEnd = endFillGrid.y + y;
      
      if (xResStart >= 0 && xResEnd < grid.getXSize())
      {
        startFillGrid.x = xResStart;
        endFillGrid.x = xResEnd;
        retVal = true;
        System.out.println(String.format("xStart %d xEnd %x, max %d", xResStart, xResEnd, xGridSize));
      }
      if (yResStart >= 0 && yResEnd < grid.getYSize())
      {
        startFillGrid.y = yResStart;
        endFillGrid.y = yResEnd;
        retVal = true;
        System.out.println(String.format("yStart %d yEnd %x, max %d", yResStart, yResEnd, yGridSize));
      }
    }
    return retVal;
  }
  
  public void addTransformControl(ControlGridTransform transformController)
  {
    this.transformController = transformController;
  }
  
  public void setDrawAction(DrawAction action)
  {
    currentDrawMode = action;
  }
  
  private void calculateDimensions()
  {
    xGridSize = this.getParent().getWidth();
    stepSize = xGridSize / grid.getXSize();
    yGridSize = (int) (stepSize * grid.getYSize());
    if (yGridSize > this.getParent().getHeight())
    {
      yGridSize = this.getParent().getHeight();
      stepSize = yGridSize / grid.getYSize();
      xGridSize = (int) (stepSize * grid.getXSize());
    }
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
  
  public DataGrid getCopyGrid()
  {
    return this.copyGrid;
  }
  
  /**
   * sets all pixels according to dgrid
   * @param dGrid the grid to show
   */
  public void setGrid(DataGrid dGrid)
  {
    dGrid.copyTo(this.grid);
    //this.grid.copy(dGrid);
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
  private boolean fill(Graphics2D g2)
  {   
    previewGrid.clearGrid();
    
    boolean set = true;
    try {
      set = !grid.isSetAt(startFillGrid);
    } catch (Exception ex) {
    }
    
    Color c = Color.CYAN;
    if (set || selectMode)
    {
      c = Color.RED;
    }
    currentDrawMode.fill(previewGrid, startFillGrid, endFillGrid, g2, c, stepSize);  
    
    return set;
  }
  
  private boolean fill()
  {
    return fill(null);
  }
  
  private void select()
  {
    if (!startFillGrid.equals(endFillGrid))
    {
      DrawActionSelect.calculateStartEnd(startFillGrid, endFillGrid);
      selectionValid = true;
      transformController.setActive(true);
    }
    else
    {
      selectionValid = false;
    }
  }
  
  public void previewFill(Point start, Point end)
  {    
    getPositionFrom(start, tempStart);
    getPositionFrom(end, tempEnd);
    
    if (!startFillGrid.equals(tempStart) || !endFillGrid.equals(tempEnd))
    {
      startFillGrid = new DataGridPosition(tempStart);
      endFillGrid = new DataGridPosition(tempEnd);
      
      Graphics2D g2 = (Graphics2D) this.getGraphics();
      this.paintComponent(g2);
      
      fill(g2);
      g2.dispose();  
    }      
  }
  
  public void fill(Point start, Point end)
  {
    getPositionFrom(start, startFillGrid);
    getPositionFrom(end, endFillGrid);
    
    if (selectMode)
    {
      select();
    }
    else
    {
      paintGrid();
    }
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
 
  private void fillAllGrid(Graphics2D g2)
  {
    g2.setColor(Color.black);
    
    int xPos = 0;
    int yPos = 0;
    for (boolean arr[] : grid.getArray())
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
  
  private void fillAllCopyGrid(Graphics2D g2)
  {
    if (null == copyGrid)
    {
      return;
    }
    g2.setColor(Color.black);
    
    int xPos = 0;
    int yPos = 0;
    for (boolean arr[] : copyGrid.getArray())
    {      
      for (boolean b : arr)
      {
        if (b)
        {
          rectangle.setFrame(stepSize*(xPos + startFillGrid.x), stepSize*(yPos + startFillGrid.y), stepSize, stepSize);
          g2.fill(rectangle);
          g2.draw(rectangle);
        }

        yPos += 1;
      }
      xPos += 1;
      yPos = 0;
    }
  }
  
    private void paintGrid()
  {
    boolean add = this.fill();
    
    if (add)
    {
      grid.addGrid(previewGrid);
    }
    else
    {
      grid.substractGrid(previewGrid);
    }
    
    previewGrid.clearGrid();
    
    this.repaint();
  }
  
  private void paintGrid(Graphics2D g2)
  {    
    g2.setColor(Color.black);
    int i;
    for (i=1; i<grid.getXSize(); i+=1)
    {
      line.setLine(stepSize*i, 0, stepSize*i, yGridSize);
      g2.draw(line);
    }
    for (i=1; i<grid.getYSize(); i+=1)
    {
      line.setLine(0, stepSize*i, xGridSize, stepSize*i);
      g2.draw(line);
    }
    this.setSize((int) (grid.getXSize() * stepSize), (int) (grid.getYSize() * stepSize));
  }
  
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    this.calculateDimensions();
    
    this.paintGrid((Graphics2D) g);
    if (selectionValid)
    {
      this.fillAllGrid((Graphics2D) g);
      this.fillAllCopyGrid((Graphics2D) g);
      this.fill((Graphics2D) g);
    }
    else
    {
      this.fillAllGrid((Graphics2D) g);
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    if (e.isControlDown())
    {
      switch (Character.toLowerCase(e.getKeyChar())) {
        case 'c':
          this.copy();
          break;
        case 'x':
          this.cut();
          break;
        case 'v':
          this.paste();
          break;
        default:
          break;
      }
      System.out.println(String.format("pressed Ctrl and %c", e.getKeyChar()));
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }
}
