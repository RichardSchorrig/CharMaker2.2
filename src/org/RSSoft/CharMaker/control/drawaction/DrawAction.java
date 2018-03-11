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
import org.RSSoft.CharMaker.core.DataGrid;
import org.RSSoft.CharMaker.core.DataGridPosition;

/**
 *
 * @author Richard
 */
public interface DrawAction {
  
  /**
   * Fills the given grid and sets the graphics depending on the implementation.
   * @param grid the grid to fill
   * @param start the start point inside the grid
   * @param end the end point inside the grid
   * @param g the graphics to use
   * @param c the color to paint
   * @param stepSize the grid size, depending on this value the graphics are drawn.
   */
  public void fill(
          DataGrid grid,
          DataGridPosition start,
          DataGridPosition end,
          Graphics2D g,
          Color c,
          double stepSize);
  
}
