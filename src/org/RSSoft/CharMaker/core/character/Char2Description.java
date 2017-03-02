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

/**
 * a class to get a description of every ascii value from 0x20 (space)
 * to 0x7E (tilde)
 * @author Richard
 */
public class Char2Description {
  
  private Char2Description(){}
  
  /**
   * returns a string description of the character c, used to make 
   * C variable names valid (no special characters show up in declaration),
   * letters are described as is ('U' becomes "U"), but special characters
   * and numbers get a description ('#' becomes "hash")
   * @param c the character to get the description
   * @return a string description / identifier of c
   */
  public static String getDescription(char c)
  {
    String description;
    switch (c) {
      case 0x20: description = "space"; break;
      case 0x21: description = "exclamation"; break;
      case 0x22: description = "quotation"; break;
      case 0x23: description = "hash"; break;
      case 0x24: description = "dollar"; break;
      case 0x25: description = "percent"; break;
      case 0x26: description = "ampersand"; break;
      case 0x27: description = "apostrophe"; break;
      case 0x28: description = "parentheses_open"; break;
      case 0x29: description = "parentheses_close"; break;
      case 0x2A: description = "asterisk"; break;
      case 0x2B: description = "plus"; break;
      case 0x2C: description = "comma"; break;
      case 0x2D: description = "minus"; break;
      case 0x2E: description = "fullstop"; break;
      case 0x2F: description = "slash"; break;
      case 0x30: description = "zero"; break;
      case 0x31: description = "one"; break;
      case 0x32: description = "two"; break;
      case 0x33: description = "three"; break;
      case 0x34: description = "four"; break;
      case 0x35: description = "five"; break;
      case 0x36: description = "six"; break;
      case 0x37: description = "seven"; break;
      case 0x38: description = "eight"; break;
      case 0x39: description = "nine"; break;
      case 0x3A: description = "colon"; break;
      case 0x3B: description = "semicolon"; break;
      case 0x3C: description = "less"; break;
      case 0x3D: description = "equals"; break;
      case 0x3E: description = "more"; break;
      case 0x3F: description = "question"; break;
      case 0x40: description = "at"; break;
      case 0x41: description = "A"; break;
      case 0x42: description = "B"; break;
      case 0x43: description = "C"; break;
      case 0x44: description = "D"; break;
      case 0x45: description = "E"; break;
      case 0x46: description = "F"; break;
      case 0x47: description = "G"; break;
      case 0x48: description = "H"; break;
      case 0x49: description = "I"; break;
      case 0x4A: description = "J"; break;
      case 0x4B: description = "K"; break;
      case 0x4C: description = "L"; break;
      case 0x4D: description = "M"; break;
      case 0x4E: description = "N"; break;
      case 0x4F: description = "O"; break;
      case 0x50: description = "P"; break;
      case 0x51: description = "Q"; break;
      case 0x52: description = "R"; break;
      case 0x53: description = "S"; break;
      case 0x54: description = "T"; break;
      case 0x55: description = "U"; break;
      case 0x56: description = "V"; break;
      case 0x57: description = "W"; break;
      case 0x58: description = "X"; break;
      case 0x59: description = "Y"; break;
      case 0x5A: description = "Z"; break;
      case 0x5B: description = "square_bracket_open"; break;
      case 0x5C: description = "backslash"; break;
      case 0x5D: description = "square_bracket_close"; break;
      case 0x5E: description = "caret"; break;
      case 0x5F: description = "underscore"; break;
      case 0x60: description = "grave"; break;
      case 0x61: description = "a"; break;
      case 0x62: description = "b"; break;
      case 0x63: description = "c"; break;
      case 0x64: description = "d"; break;
      case 0x65: description = "e"; break;
      case 0x66: description = "f"; break;
      case 0x67: description = "g"; break;
      case 0x68: description = "h"; break;
      case 0x69: description = "i"; break;
      case 0x6A: description = "j"; break;
      case 0x6B: description = "k"; break;
      case 0x6C: description = "l"; break;
      case 0x6D: description = "m"; break;
      case 0x6E: description = "n"; break;
      case 0x6F: description = "o"; break;
      case 0x70: description = "p"; break;
      case 0x71: description = "q"; break;
      case 0x72: description = "r"; break;
      case 0x73: description = "s"; break;
      case 0x74: description = "t"; break;
      case 0x75: description = "u"; break;
      case 0x76: description = "v"; break;
      case 0x77: description = "w"; break;
      case 0x78: description = "x"; break;
      case 0x79: description = "y"; break;
      case 0x7A: description = "z"; break;
      case 0x7B: description = "bracket_open"; break;
      case 0x7C: description = "vertical_bar"; break;
      case 0x7D: description = "bracket_close"; break;
      case 0x7E: description = "tilde"; break;      
      default: description = ""; break;
    }
    return description;
  }
  
}
