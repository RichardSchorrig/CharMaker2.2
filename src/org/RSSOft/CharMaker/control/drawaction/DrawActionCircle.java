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
public class DrawActionCircle extends DrawAction {
  
  private int getCircleValue(int value)
  {
    int sum = 0;
    int currentValue = 1;
    
    while (sum < value)
    {
      sum += currentValue;
      currentValue += 1;
      System.out.println(String.format("sum is %d, currentValue is %d", sum, currentValue));
    }
    
    return currentValue - 1;
  }

  @Override
  public void fill(DataGrid grid, DataGridPosition start, DataGridPosition end, Graphics2D g, Color c, double stepSize) {
    
    Rectangle2D.Double rectangle = new Rectangle2D.Double();    
    Draw draw;
    
    if (null != g)
    {
      g.setColor(c);
      draw = new DrawReal(rectangle, stepSize, g);
    }
    else
    {
      draw = new DrawFake();
    }
    
    int dx = end.x - start.x;
    int dy = end.y - start.y;
    
    int r = (int)Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    System.out.println(String.format("radius is %d", r));
    
    int residual1 = (r *2) / 3;
    int maxLen = getCircleValue(residual1);
    int residual2 = r - residual1 - maxLen;
    
    System.out.println(String.format("with r1 %d, r2 %d and maxLen %d", residual1, residual2, maxLen));
    
    
    int directionX = dx < 0 ? -1 : 1;
    int directionY = dy < 0 ? -1 : 1;
    
    dx = dx * directionX + 1;
    dy = dy * directionY + 1;
    
    int x = start.x;
    int y = start.y;
    
    try {
      for (int i = 0; i < maxLen; i += 1)
      {
        for (int j = 0; j < maxLen - i; j += 1)
        {
          grid.setAt(x, y, true);
          draw.draw(x, y);
          y += directionY;
        }
        x += directionX;
      }
      for (int i = 0; i < maxLen; i += 1)
      {
        for (int j = maxLen - i; j > 0; j += 1)
        {
          grid.setAt(x, y, true);
          draw.draw(x, y);
          x += directionX;
        }
        y += directionY;
      }

    } catch (Exception ex)
    {
      
    }
    
    
    /*      
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
            
            draw.draw(x, y);

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
            
            draw.draw(x, y);

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
*/
  }

  @Override
  public void fill(DataGrid grid, DataGridPosition start, DataGridPosition end) {
    this.fill(grid, start, end, null, null, 0);
  }
  
}
