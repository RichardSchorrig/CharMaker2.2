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
 * This class is a container containing font settings for the c header writer
 * @author Richard
 */
public class FontSettings {
  
  public static final int ROTATION_0 = 0;
  public static final int ROTATION_90 = 1;
  public static final int ROTATION_180 = 2;
  public static final int ROTATION_270 = 3;
  
  public static final int BITORDER_MSB = 0;
  public static final int BITORDER_LSB = 1;
  
  public static final int ENDIANORDER_BIG = 0;
  public static final int ENDIANORDER_LITTLE = 1;
  
  public static final int ALIGNMENT_TOP = 0;
  public static final int ALIGNMENT_BOTTOM = 1;
  
  private final static String DATATYPE_NAME_UCHAR = "unsigned char";
  private final static String DATATYPE_NAME_UINT_8 = "uint8_t";
  private final static String DATATYPE_NAME_UINT_16 = "uint16_t";
  private final static String DATATYPE_NAME_UINT_32 = "uint32_t";
  private final static String DATATYPE_NAME_UINT_64 = "uint64_t";
  
  public final static int DATATYPE_8Bit = 8;
  public final static int DATATYPE_16Bit = 16;
  public final static int DATATYPE_32Bit = 32;
  public final static int DATATYPE_64Bit = 64;
  
  public int rotation;
  public int bitOrder;
  public int endianOrder;
  public int alignment;
  public boolean mirrorHorizontal;
  public boolean mirrorVertical;
  public boolean commentPreview;
  public String fontName;
  public String dataType;
  public int bits;
  
  /**
   * construct a standard container where all fields are 0
   */
  public FontSettings(){}
  
}
