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
package org.RSSoft.CharMaker.control.models;

/**
 * this class is a collection of data regarding character data.
 * Fields:
 *  -character: the character (byte), example: ':'
 *  -description: a description / name of the character, example "colon"
 *  -width: the width in pixels the character takes up in the grid
 * 
 * this is used to pass information from the character dialog
 * (operation new, edit, copy) to the character set. the actual data is kept
 * within the class @see CharacterDescriptor
 * @author Richard
 */
public class CharacterData {
  
  public char character;
  public String description;
  public int width;
  
  public CharacterData(){}
  
}
