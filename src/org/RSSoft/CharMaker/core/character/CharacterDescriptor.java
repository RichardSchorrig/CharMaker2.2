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
package org.RSSoft.CharMaker.core.character;

import java.io.Serializable;
import java.util.Observable;
import org.RSSoft.CharMaker.core.DataGrid;

/**
 * This class describes a pixel character.
 * It contains a DataGrid, the description string, and the actual character
 * @author Richard
 */
public class CharacterDescriptor extends Observable implements Serializable
{
  static final long serialVersionUID = 125;
  
  private DataGrid characterGrid;
  private String descriptor;
  private char character;
  
  /**
   * construct a new Character Descriptor with a valid grid, a description
   * and the actual character
   * @param grid the grid to use
   * @param description the description of the character c
   * @param c the actual character
   */
  public CharacterDescriptor(DataGrid grid, String description, char c)
  {
    this.characterGrid = grid.copy();
    this.descriptor = description;
    this.character = c;
  }
  
  /**
   * returns the grid of the character
   * @return the DataGrid grid
   */
  public DataGrid getGrid()
  {
    return characterGrid;
  }
  
  /**
   * sets a new grid to the existing character
   * @param grid a new grid to replace the old one
   */
  public void setGrid(DataGrid grid)
  {
    grid.copyTo(this.characterGrid);
    //this.characterGrid.copyTo(grid);
    this.setChanged();
    this.notifyObservers();
  }
  
  /**
   * returns the width of the character grid
   * @return the width in pixel
   */
  public int getWidth()
  {
    return this.characterGrid.getXSize();
  }
  
  /**
   * sets the width of the character grid. A new grid is constructed
   * @param width the new width of the character grid
   */
  public void setWidth(int width)
  {
    this.characterGrid = new DataGrid(width, this.characterGrid.getYSize());
    this.setChanged();
    this.notifyObservers();
  }
  
  /**
   * returns the string description of the character
   * @return the string description of the character
   */
  public String getDescriptor()
  {
    return descriptor;
  }
  
  /**
   * add a new description to the character
   * @param description the new character description
   */
  public void setDescription(String description)
  {
    this.descriptor = description;
    this.setChanged();
    this.notifyObservers();
  }
  
  /**
   * returns the character
   * @return the character of this character descriptor
   */
  public char getCharacter()
  {
    return this.character;
  }
  
  /**
   * set a new character to the character descriptor
   * @param c the new character value
   */
  public void setCharacter(char c)
  {
    this.character = c;
    this.setChanged();
    this.notifyObservers();
  }
}
