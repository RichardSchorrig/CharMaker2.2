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

package org.RSSoft.CharMaker.core;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import org.RSSoft.CharMaker.control.models.FontSettings;
import org.RSSoft.CharMaker.core.character.CharacterDescriptor;
import org.RSSoft.CharMaker.core.character.CharacterSet;
import org.RSSoft.CharMaker.util.RSLogger;

/**
 *
 * @author Richard
 */
public class HeaderWriter
{
//  public final static int ROTATION_0 = 0;
//  public final static int ROTATION_90 = 1;
//  public final static int ROTATION_180 = 2;
//  public final static int ROTATION_270 = 3;
  
//  public final static int MIRROR_NONE = 0;
//  public final static int MIRROR_HORIZONTAL = 1;
//  public final static int MIRROR_VERTICAL = 2;
//  public final static int MIRROR_HORIZONTAL_VERTICAL = 3;
  
  public final static String OUTPUT_HEX_8BIT = "0x%02x";
  public final static String OUTPUT_HEX_16BIT = "0x%04x";
  public final static String OUTPUT_HEX_32BIT = "0x%08x";
  public final static String OUTPUT_HEX_64BIT = "0x%016x";
  
  private final static String COMMENT_BEGIN = "/** ";
  private final static String COMMENT_FURTHER = " * ";
  private final static String COMMENT_END = " */";
  private final static String COMMENT_SIGNLELINE = "// ";
  private final static String PRECOMPILER_IFNDEF = "#ifndef";
  private final static String PRECOMPILER_DEFINE = "#define";
  private final static String PRECOMPILER_ENDIF = "#endif";
  private final static String COMMENT_LINEBREAK = "/*============================================*/";
  
/*  
  private final int rotation;
  private final int bitOrder;
  private final boolean alignAtTop;
  private final int bitsHeight;
  private final boolean mirrorHorizontal;
  private final boolean mirrorVertical;
*/  
  private final FontSettings settings;
  
//  private final String datatype_name;
  private final String output_hex;  
  
  public HeaderWriter(FontSettings settings)
  {
  /*
    this.rotation = rotation;
    this.bitOrder = bitOrder;
    this.alignAtTop = alignAtTop;
    this.bitsHeight = type;
    this.mirrorHorizontal = mirrorHorizontal;
    this.mirrorVertical = mirrorVertical;
  */
    this.settings = settings;
    switch (settings.bits) {
      case FontSettings.DATATYPE_8Bit: this.output_hex = this.OUTPUT_HEX_8BIT; break;
      case FontSettings.DATATYPE_16Bit: this.output_hex = this.OUTPUT_HEX_16BIT; break;
      case FontSettings.DATATYPE_32Bit: this.output_hex = this.OUTPUT_HEX_32BIT; break;
      case FontSettings.DATATYPE_64Bit: this.output_hex = this.OUTPUT_HEX_64BIT; break;
      default: this.output_hex = this.OUTPUT_HEX_8BIT; break;
    }

  }
/*  
  public HeaderWriter(int rotation, int bitOrder, boolean alignAtTop, int type, String typename, boolean mirrorHorizontal, boolean mirrorVertical)
  {
    this.rotation = rotation;
    this.bitOrder = bitOrder;
    this.alignAtTop = alignAtTop;
    this.bitsHeight = type;
    this.mirrorHorizontal = mirrorHorizontal;
    this.mirrorVertical = mirrorVertical;
    
    this.datatype_name = typename;    
    switch (type) {
      case DATATYPE_8Bit: this.output_hex = this.OUTPUT_HEX_8BIT; break;
      case DATATYPE_16Bit: this.output_hex = this.OUTPUT_HEX_16BIT; break;
      case DATATYPE_32Bit: this.output_hex = this.OUTPUT_HEX_32BIT; break;
      case DATATYPE_64Bit: this.output_hex = this.OUTPUT_HEX_64BIT; break;
      default: this.output_hex = this.OUTPUT_HEX_8BIT; break;
    }
  }
*/
  
    private static long modifyByteOrder(long value, int bits, int endianess)
    {
        long retVal = 0;
        if (endianess == FontSettings.ENDIANORDER_BIG) {
            return value;
        } else {
            int b = 0;
            
            for (int i = 0; i < bits / 8; i += 1) {
                b = (int)((value >> ((bits) - ( (i + 1) * 8))) & 0xFF);
                retVal |= ((long)b) << (i * 8);
            }
        }
        return retVal;
    }
  
    private int aquireCharacter(StringBuffer characterData, DataGrid grid) {
        long value = 0;
        int elements = 0;
        //int x = c.getGrid().getXSize();
        ScanDirectionIterator it;
        try {
            it = new ScanDirectionIterator(grid, settings.scanDirection);
            for (it.outer_init(); it.outer_codition(); it.outer_iterate()) {
                int pos = 0;
                
                for (it.inner_init(); it.inner_codition(); it.inner_iterate()) {
                    int b = 0;
                    if (grid.isSetAt(it.getX(), it.getY())) {
                        if (settings.bitOrder == FontSettings.BITORDER_LSB) {
                            b = 1;
                        } else if (settings.bitOrder == FontSettings.BITORDER_MSB) {
                            b = 1 << (settings.bits - 1);
                        }
                    } else {
                        b = 0;
                    }
                    if (settings.bitOrder == FontSettings.BITORDER_LSB) {
                        value += b << pos;
                    } else if (settings.bitOrder == FontSettings.BITORDER_MSB) {
                        value += b >> pos;
                    }
                    pos += 1;
                    
                    if (pos >= settings.bits) {
                        pos = 0;
                        
                        characterData.append(String.format(this.output_hex, modifyByteOrder(value, settings.bits, settings.endianOrder)));
                        characterData.append(", ");
                        value = 0;
                        
                        elements += 1;
                    }
                }
                if (settings.alignment == FontSettings.ALIGNMENT_TOP) {
                    if ((pos % settings.bits) != 0) {
                        if (settings.bitOrder == FontSettings.BITORDER_MSB) {
                            value <<= (settings.bits - (pos % settings.bits));
                        } else if (settings.bitOrder == FontSettings.BITORDER_LSB) {
                            value >>= (settings.bits - (pos % settings.bits));
                        }
                    }
                }
                if (pos != 0) {
                    characterData.append(String.format(this.output_hex, modifyByteOrder(value, settings.bits, settings.endianOrder)));
                    characterData.append(", ");
                    value = 0;
                    
                    elements += 1;
                }
            }
            characterData.delete(characterData.length() - 2, characterData.length());
            //characterData = characterData.concat(this.closeArray());
        } catch (Exception ex) {
            RSLogger.getLogger().log(Level.SEVERE, null, ex);
        }

        return elements;
    }
  
  private String makeComment(CharacterDescriptor c)
  {
    String output = COMMENT_SIGNLELINE + "\t";
    try {
      int rotationComment = (settings.rotation + 1) % 4;
      
      GridIterator it = new GridIterator(c.getGrid(), rotationComment, settings.mirrorHorizontal, settings.mirrorVertical);
      for (it.x=it.xBegin; it.conditionX(); it.x+=it.xDirection)
      {
        for (it.y=it.yBegin; it.conditionY(); it.y+=it.yDirection)
        {
          int b;
          if (c.getGrid().isSetAt(it.getColumnIterator(), it.getRowIterator()))
          {
            output = output.concat("#\t");
          }
          else
          {
            output = output.concat(".\t");
          }
        }
        output = output.concat("\n");
        output = output.concat(COMMENT_SIGNLELINE);
        output = output.concat("\t");      
      }
      output = output.concat("\n");
    } catch (Exception ex) {
      RSLogger.getLogger().log(Level.SEVERE, null, ex);
    }
    return output;
  }
  
  private String declareFontArray(CharacterSet charaSet, int number)
  {
    String output = settings.dataType;
    if (number == -1) {
      output = output.concat(String.format("* %s[%d] = {\n", 
              charaSet.getFontName().replace(' ', '_'),
              charaSet.getSize()));
    }
    else {
      output = output.concat(String.format("* %s_%d[%d] = {\n", 
              charaSet.getFontName().replace(' ', '_'),
              number,
              charaSet.getSize()));
    }
    return output;
  }
  
  private String addToFontArray(CharacterDescriptor c, int number, boolean lastone)
  {
    String output;
    if (number == -1) {
      output = String.format("\tcharacter_%s", c.getDescriptor());
    }
    else {
      output = String.format("\tcharacter_%s_%d", c.getDescriptor(), number);
    }
    if (lastone)
      output = output.concat("\n");
    else
      output = output.concat(",\n");
    return output;
  }
  
  private String writeFontArray(CharacterSet charaSet) throws Exception {
    String output = "";
    
    if (charaSet.getFontHeight() > settings.bits) {
      int lines = charaSet.getFontHeight() / settings.bits;
      if ( (charaSet.getFontHeight() % settings.bits) != 0) {
        lines += 1;
      }
      for (int i=0; i<lines; i+=1) {
        output = output.concat(this.declareFontArray(charaSet, i));
        for (int j=0; j<charaSet.getSize()-1; j+=1)
        {
          output = output.concat(this.addToFontArray(charaSet.getCharacterAt(j), i, false));
        }
        output = output.concat(this.addToFontArray(charaSet.getCharacterAt(charaSet.getSize()-1), i, true));
        output = output.concat(this.closeArray());
      }
    }
    else {
      output = output.concat(this.declareFontArray(charaSet, -1));
      for (int j=0; j<charaSet.getSize()-1; j+=1)
      {
        output = output.concat(this.addToFontArray(charaSet.getCharacterAt(j), -1, false));
      }
      output = output.concat(this.addToFontArray(charaSet.getCharacterAt(charaSet.getSize()-1), -1, true));
      output = output.concat(this.closeArray());
    }
    
    return output;
  }
  
  private String declareWidthArray(CharacterSet charaSet)
  {
    String output = "int";
    output = output.concat(String.format(" %s_width[%d] = {\n",
            charaSet.getFontName().replace(' ', '_'),
            charaSet.getSize()));
    return output;
  }
  
  private String closeArray()
  {
    String output = "};\n";
    return output;
  }
  
  private String addToWidthArray(CharacterDescriptor c, boolean lastone)
  {
    String output = String.format("%d", c.getWidth());
    if (lastone)
      output = output.concat("\n");
    else
      output = output.concat(",\n");
    return output;
  }
  
  private String declareCharacter(CharacterDescriptor c)
  {
    String output = "";
    
    DataGrid grid = null;
    // first, rotate, mirror and invert as the user whishes:
    try {
        grid = c.getGrid().copy();
        grid.manipulate(settings.rotation, settings.mirrorHorizontal, settings.mirrorVertical);
        if (settings.invert) {
            grid.invertAll();
        }
    } catch (Exception ex) {
        RSLogger.getLogger().log(Level.SEVERE, "could not continue: ", ex);
        output = output.concat("#error: " + ex.getMessage());
        return output;
    }
    
    boolean horizontalOverVertical = (settings.scanDirection & FontSettings.SCANDIRECTION_HORIZONTAL_OVER_VERTICAL) != 0;
    
    int columns = horizontalOverVertical ? grid.getXSize() : grid.getYSize();
    
    int elements = 0;
    StringBuffer characterData = new StringBuffer();
    
    if (settings.organizedInColumns && columns > settings.bits) {
      int lines = columns / settings.bits;
      if ( (columns % settings.bits) != 0) {
        lines += 1;
      }
      
      // iterate in colunms over the grid
      for (int i=0; i<lines; i+=1) {
        GridArea area = new GridArea();
        
        if (horizontalOverVertical) {
            area.xOffset = i*settings.bits;
            area.xEnd = settings.bits;
            area.yOffset = 0;
            area.yEnd = grid.getYSize();
        } else {            
            area.yOffset = i*settings.bits;
            area.yEnd = settings.bits;
            area.xOffset = 0;
            area.xEnd = grid.getXSize();
        }
        
        try {
            DataGrid subGrid = new DataGrid(1, 1);
            grid.copy(subGrid, area, false);

            characterData.append("\t\t");
            elements = this.aquireCharacter(characterData, subGrid);        
            characterData.append("\n");
        } catch (Exception ex) {
            RSLogger.getLogger().log(Level.SEVERE, null, ex);
        }
      }
      
      output = output.concat(String.format(" character_%s[%d] = {", c.getDescriptor(), elements));
    } else {
      
      elements = this.aquireCharacter(characterData, grid);
    }
        
    output = output.concat(settings.dataType);
    output = output.concat(String.format(" character_%s[%d] = {", c.getDescriptor(), elements));
    output = output.concat(characterData + "};\n");
    
    return output;
  }
  
  private String commentScanDirection()
  {
      boolean horizontalOverVertical = (settings.scanDirection & FontSettings.SCANDIRECTION_HORIZONTAL_OVER_VERTICAL) != 0;
      
      String horizontal = (((settings.scanDirection & FontSettings.SCANDIRECTION_RIGHT_LEFT) != 0) ? "right -> left" : "left -> right");
      String vertical = (((settings.scanDirection & FontSettings.SCANDIRECTION_DOWN_UP) != 0) ? "down -> up" : "up -> down");
      
      return String.format("Scan direction: first %s, then %s", 
              (horizontalOverVertical ? horizontal : vertical),
              (horizontalOverVertical ? vertical : horizontal));
  }
  
  public void writeHeader(CharacterSet charaSet, File file, boolean commentEnable) throws FileNotFoundException, IOException
  {
//    String fontName = charaSet.getFontName().replace(' ', '_');
//    String fileName = String.format("FONT_%s.h", fontName.toUpperCase());
    DataOutputStream fontHeader = new DataOutputStream(
              new FileOutputStream(file));
    
    System.out.println("New File: " + file.toString());
    
    String comment = COMMENT_BEGIN + "\n"
                     + COMMENT_FURTHER + "@author: CharMaker by RSoft" + "\n"
                     + COMMENT_FURTHER + "\n"
                     + COMMENT_FURTHER + "This file represents the font " + charaSet.getFontName() + "\n"
                     + COMMENT_FURTHER + "Character count:" + String.format("%d", charaSet.getSize()) + "\n"
                     + COMMENT_FURTHER + "Character height:" + String.format("%d", charaSet.getFontHeight()) + "\n"
                     + COMMENT_FURTHER + "Character width (0 is variable):" + String.format("%d", charaSet.getFontWidth()) + "\n"
                     + COMMENT_FURTHER + "\n"
                     + COMMENT_FURTHER + "Rotation: " + String.format("%d", settings.rotation*90)+ "\n"
                     + COMMENT_FURTHER + "Mirrored horizontally: " + Boolean.toString(settings.mirrorHorizontal) + "\n"
                     + COMMENT_FURTHER + "Mirrored vertically: " + Boolean.toString(settings.mirrorVertical) + "\n"
                     + COMMENT_FURTHER + "Inverted: " + (settings.invert ? "true" : "false") + "\n"
                     + COMMENT_FURTHER + "\n"
                     + COMMENT_FURTHER + "First Bit: " + (settings.bitOrder == FontSettings.BITORDER_LSB ? "LSB" : "MSB") + "\n"
                     + COMMENT_FURTHER + commentScanDirection() + "\n";
    
    if (settings.bits > 8) {
      comment = comment.concat(COMMENT_FURTHER + "Byte Order: "
                               + (settings.endianOrder == FontSettings.ENDIANORDER_BIG ? "big endian" : "little endian") + "\n");
    }
    comment = comment.concat(COMMENT_END + "\n");
    
    fontHeader.writeBytes(comment);
    
    String precompiler_filename = file.getName().toUpperCase().replace('.', '_') + "_";
    String font_name = "FONT_" + charaSet.getFontName().toUpperCase();
    fontHeader.writeBytes(PRECOMPILER_IFNDEF + " " + precompiler_filename + "\n");
    fontHeader.writeBytes(PRECOMPILER_DEFINE + " " + precompiler_filename + "\n\n");
    
    fontHeader.writeBytes(PRECOMPILER_DEFINE + " " + font_name + "_WIDTH ");
    fontHeader.writeBytes(String.format("%d\n", charaSet.getFontWidth()));
    fontHeader.writeBytes(PRECOMPILER_DEFINE + " " + font_name + "_HEIGHT ");
    fontHeader.writeBytes(String.format("%d\n", charaSet.getFontHeight()));
    
    for (CharacterDescriptor c : charaSet.getCharacters())
    {
      fontHeader.writeBytes("// ");
      fontHeader.writeByte(c.getCharacter());
      fontHeader.writeBytes("\n");
      
      if (commentEnable) {
        fontHeader.writeBytes(this.makeComment(c));
        fontHeader.writeBytes("\n");
      }
      fontHeader.writeBytes(this.declareCharacter(c));
      fontHeader.writeBytes(COMMENT_LINEBREAK);
      fontHeader.writeBytes("\n\n");
    }
    
    fontHeader.writeByte('\n');
    
//    try {
//      
//      fontHeader.writeBytes(this.writeFontArray(charaSet));
//      
//      if (charaSet.getFontWidth() == 0)
//      {
//        fontHeader.writeByte('\n');
//        fontHeader.writeBytes(this.declareWidthArray(charaSet));
//
//        for (int i=0; i<charaSet.getSize()-1; i+=1)
//        {
//          fontHeader.writeBytes(this.addToWidthArray(charaSet.getCharacterAt(i), false));
//        }
//        fontHeader.writeBytes(this.addToWidthArray(charaSet.getCharacterAt(charaSet.getSize()-1), true));
//        fontHeader.writeBytes(this.closeArray());
//      }
//    }
//    catch (Exception ex) {
//      RSLogger.getLogger().log(Level.WARNING, null, ex);
//    }
    
    fontHeader.writeBytes(PRECOMPILER_ENDIF + " " + COMMENT_BEGIN + precompiler_filename + COMMENT_END);
    fontHeader.writeBytes("\n\n");
    
    fontHeader.close();
  }
}
