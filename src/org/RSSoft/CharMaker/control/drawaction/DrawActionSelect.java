/*
 * Copyright (C) 2018 Richard.
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
package org.RSSoft.CharMaker.control.drawaction;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import org.RSSoft.CharMaker.core.DataGrid;
import org.RSSoft.CharMaker.core.DataGridPosition;

/**
 *
 * @author Richard
 */
public class DrawActionSelect extends DrawAction {
  
    /**
   * Get the upper left and lower right corner of a selection.
   * If a selection is made, the first point to be set isn't always the upper
   * left corner, it could be the upper right or lower left or lower right.
   * This function calulates the upper left and lower right corner and returs it
   * in the same instances the function is called with.
   * @param start
   * @param end 
   */
  static public void calculateStartEnd(DataGridPosition start, DataGridPosition end)
  {
    int xStart, yStart, xEnd, yEnd;
    xStart = (start.x < end.x ? start.x : end.x);
    yStart = (start.y < end.y ? start.y : end.y);
    xEnd = ((start.x > end.x ? start.x : end.x));
    yEnd = ((start.y > end.y ? start.y : end.y));
    
    start.x = xStart;
    start.y = yStart;
    end.x = xEnd;
    end.y = yEnd;
  }

  @Override
  public void fill(DataGrid grid, DataGridPosition start, DataGridPosition end, Graphics2D g, Color c, double stepSize) {
    
    g.setColor(c);
    g.setStroke(new BasicStroke(4));
    
    Line2D.Double selectionLine = new Line2D.Double();
    
    calculateStartEnd(start, end);
    
    double xStart, yStart, xEnd, yEnd;
    xStart = start.x * stepSize;
    yStart = start.y* stepSize;
    xEnd = (end.x + 1) * stepSize;
    yEnd = (end.y + 1) * stepSize;

    selectionLine.setLine(xStart, yStart, xEnd, yStart);
    g.draw(selectionLine);
    selectionLine.setLine(xEnd, yStart, xEnd, yEnd);
    g.draw(selectionLine);    
    selectionLine.setLine(xEnd, yEnd, xStart, yEnd);
    g.draw(selectionLine);
    selectionLine.setLine(xStart, yEnd, xStart, yStart);
    g.draw(selectionLine);    
  }

  @Override
  public void fill(DataGrid grid, DataGridPosition start, DataGridPosition end) {
  }
  
}
