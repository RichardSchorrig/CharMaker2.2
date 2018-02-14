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

import java.awt.image.Raster;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import javax.swing.AbstractListModel;
import org.RSSoft.CharMaker.core.DataGrid;
import org.RSSoft.CharMaker.util.RSLogger;

/**
 * This class represents a list of character descriptors and some more
 * information
 * @author Richard
 */
public class CharacterSet extends AbstractListModel<String> implements Serializable, Observer
{
  static final long serialVersionUID = 25;
  
  private ArrayList<CharacterDescriptor> characters;
  private final int fontHeight;
  private int fontWidth;
  private boolean isVariableWidth;
  private String fontName;
  private boolean changed;
  
  /**
   * construct a new empty list
   * @param width the width of the characters. can be 0 to identify the font is 
   * not monospace
   * @param height the height of the font. the height is the same for all characters
   * @param fontName the name of the font
   */
  public CharacterSet(int width, int height, String fontName)
  {
    characters = new ArrayList<>();
    this.fontHeight = height;
    this.fontWidth = width;
    if (width == 0) {
      this.isVariableWidth = true;
    }
    else {
      this.isVariableWidth = false;
    }
    this.fontName = fontName;
    
    this.changed = false;
    
    RSLogger.getLogger().log(Level.INFO, String.format("new Character set: %s, height: %d, width: %d", fontName, height, width));
  }
  
  /**
   * resets the member changed to false.
   * To be called when serialized to a file.
   */
  public void setSaved()
  {
    this.changed = false;
  }
  
  /**
   * returns whether this set was changed.
   * @return true if recent changes were not saved to a file.
   */
  public boolean isChanged()
  {
    return this.changed;
  }
  
  /**
   * returns the character descriptor at the index
   * @param index the index of the character
   * @return the character descriptor at the index
   * @throws Exception in case the index is bigger than the size of the array
   */
  public CharacterDescriptor getCharacterAt(int index) throws Exception
  {
    if (index > this.characters.size() || index < 0)
      throw new Exception(String.format("Error: index %d is out of range", index));
    return characters.get(index);
  }
  
  /**
   * searches for the character c in the character array.
   * There is always either no match or only one, because adding more than one
   * character descriptor with the same character value is prohibited
   * @param c the character to search for
   * @return a character descriptor if search was successful, or null in case
   * the character was not found
   */
  public CharacterDescriptor getCharacter(char c)
  {
    for (CharacterDescriptor ch: characters)
    {
      if (ch.getCharacter() == c)
        return ch;
    }
    return null;
  }
  
  /**
   * returns the font name
   * @return the font name
   */
  public String getFontName()
  {
    return fontName;
  }
  
  /**
   * sets the font name
   * @param name the font name
   */
  public void setFontName(String name)
  {
    this.fontName = name;
  }
  
  /**
   * returns the height of the font in pixel
   * @return the height of the font
   */
  public int getFontHeight()
  {
    return fontHeight;
  }
  
  /**
   * returns the width of the font in pixel, 0 in case the width is variable
   * @return the width of the font
   */
  public int getFontWidth()
  {
    return fontWidth;
  }
  
  /**
   * returns wether this font width is variable
   * @return true if font width is variable, false if fixed
   */
  public boolean isVariableWidth() {
    return this.isVariableWidth;
  }
  
  /**
   * adds a character to the array
   * @param character the character value
   * @param raster the pixel information of the character
   * @throws Exception in case the character value is already in the array
   */
  public void addCharacter(char character, Raster raster) throws Exception
  {
    if (this.fontWidth == 0)
    {
      this.addCharacter(character, Char2Description.getDescription(character), DataGrid.convert(raster, fontHeight));
    }
    else
    {
      this.addCharacter(character, Char2Description.getDescription(character), DataGrid.convert(raster, fontHeight, fontWidth));
    }
    
    this.fireContentsChanged(this, this.characters.size()-1, this.characters.size());
    RSLogger.getLogger().log(Level.INFO, String.format("new Character: %c", character));
  }
  
  /**
   * adds a new character with an empty grid to the array
   * @param character the character value
   * @param description a description of the character
   * @throws Exception in case the font width is variable, so no information
   * of the width is given, or character is already in the array
   */
  public void addCharacter(char character, String description) throws Exception
  {
    if (this.fontWidth != 0)
    {
      this.addCharacter(character, description, new DataGrid(this.fontWidth, this.fontHeight));
    }
    else
    {
      throw new Exception("Invalid Character: no width specified, width is variable");
    }
  }
  
  /**
   * adds a new character with an empty grid to the array
   * @param character the character value
   * @param description a description of the character
   * @param width the width of the new character
   * @throws Exception in case the character value is already in the array
   */
  public void addCharacter(char character, String description, int width) throws Exception
  {
    this.addCharacter(character, description, new DataGrid(width, this.fontHeight));
  }
  
  /**
   * add a character to the array
   * @param character the character value
   * @param grid the pixel information of the character
   * @throws Exception in case the character value is already in the array
   */
  public void addCharacter(char character, DataGrid grid) throws Exception
  {
    this.addCharacter(character, Char2Description.getDescription(character), grid);
  }
  
  /**
   * add a character to the array
   * @param character the character value
   * @param description a description of the character
   * @param grid the pixel information of the character
   * @throws Exception in case the character value is already in the array
   */
  public void addCharacter(char character, String description, DataGrid grid) throws Exception
  {
    if (characters.isEmpty()) {
      if (this.fontWidth == 0) {
        this.fontWidth = grid.getXSize();
      }
    }
    else {
      if (this.fontWidth != grid.getXSize()) {
        fontWidth = 0;
        this.isVariableWidth = true;
      }
    }
    if (this.getCharacterPosition(character) != -1) {
      throw new Exception(String.format("Character %c already in array", character));
    }
    CharacterDescriptor descriptor = new CharacterDescriptor(grid, description, character);
    descriptor.addObserver(this);
    characters.add(descriptor);
    this.changed = true;
    this.fireContentsChanged(this, this.characters.size()-1, this.characters.size());
    RSLogger.getLogger().log(Level.INFO, String.format("new Character: %c", character));
  }
  
  /**
   * removes the character at the given index
   * @param index the index to remove
   */
  public void removeCharacter(int index)
  {
    this.characters.remove(index);
    this.fireContentsChanged(this, index-1, index);
  }
  
  /**
   * returns the position of the given character
   * @param c the character value which position is returned
   * @return the position of c
   */
  private int getCharacterPosition(char c) {
    int position = -1;
    for (CharacterDescriptor cDesc : this.characters) {
      position += 1;
      if (cDesc.getCharacter() == c) {
        return position;
      }
    }
    
    return -1;
  }
  
  /**
   * sorts the character in ascending ascii value
   * (from 0x20 to 0x7E)
   */
  public void sort()
  {
    ArrayList<CharacterDescriptor> sortedArray = new ArrayList<>();
    for (char c=0x00; c<0xFF; c+=1) {
      int pos = this.getCharacterPosition(c);
      if (pos != -1) {
        sortedArray.add(this.characters.get(pos));
      }
    }
    assert(sortedArray.size() == this.characters.size());
    this.characters = sortedArray;
  }

  @Override
  public int getSize()
  {
    return characters.size();
  }

  @Override
  public String getElementAt(int index)
  {
    return characters.get(index).getDescriptor();
  }
  
  public ArrayList<CharacterDescriptor> getCharacters()
  {
    return characters;
  }
  
  public void update(int index)
  {
    this.fireContentsChanged(this, index-1, index);
  }
  
  /**
   * validates freshly deserialized data (backwards compapility with old serialized data)
   * also adds observer (this) to all characters
   */
  public void validate() {
    for (CharacterDescriptor cd : this.characters) {
      if (cd.getWidth() != this.fontWidth) {
        this.fontWidth = 0;
        this.isVariableWidth = true;
        break;
      }
    }
    for (CharacterDescriptor cd : this.characters) {
      cd.addObserver(this);
    }
    this.changed = false;
  }
  /*
  private void writeObject(java.io.ObjectOutputStream out) throws IOException
  {
    out.write(this.fontHeight);
    out.write(this.fontWidth);
    out.write(this.fontName.length());
    out.write(this.fontName.getBytes());
  }
          
 * private void readObject(java.io.ObjectInputStream in)
 *     throws IOException, ClassNotFoundException;
 * private void readObjectNoData()
 *     throws ObjectStreamException;
*/
  
  @Override
  public void update(Observable o, Object arg) {
    this.changed = true;
  }
}
