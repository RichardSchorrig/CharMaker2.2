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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.RSSoft.CharMaker.core.DataGrid;
import org.RSSoft.CharMaker.core.DataGridPosition;

/**
 *
 * @author Richard
 */
public class DrawActionStraightLine implements DrawAction {

  @Override
  public void fill(DataGrid grid, DataGridPosition start, DataGridPosition end, Graphics2D g, Color c, double stepSize) {
    
    Rectangle2D.Double rectangle = new Rectangle2D.Double();
    g.setColor(c);
    
    int dx = end.x - start.x;
    int dy = end.y - start.y;
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
    int x = start.x;
    int y = start.y;
    
    try
    {
      if (startX)
      {
        do {
          int i = 0;
          while (i != m)
          {
            grid.setAt(x, y, true);
            
            rectangle.setFrame(stepSize*x, stepSize*y, stepSize, stepSize);
            g.fill(rectangle);
            g.draw(rectangle);

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
            grid.setAt(x, y, true);
            
            rectangle.setFrame(stepSize*x, stepSize*y, stepSize, stepSize);
            g.fill(rectangle);
            g.draw(rectangle);

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
  }
  
}
