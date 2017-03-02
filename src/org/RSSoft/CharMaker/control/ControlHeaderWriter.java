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

package org.RSSoft.CharMaker.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import org.RSSoft.CharMaker.control.models.FontSettings;
import org.RSSoft.CharMaker.core.HeaderWriter;
import org.RSSoft.CharMaker.core.character.CharacterSet;
import org.RSSoft.CharMaker.util.RSLogger;

/**
 * This class controls the header writer
 * @author Richard
 */

//TODO: rewrite
public class ControlHeaderWriter
{
 
  //private ControlFileOperation fileController;
  
  public ControlHeaderWriter()
  {
  }
  
  /**
   * commands the header writer to write out the character set to a new
   * file with the given font settings
   * @param charset the character set to write
   * @param settings settings for the character set (orientation, endianess...)
   * @param file the string of the file to write to
   */
  public void writeOut(CharacterSet charset, FontSettings settings, String file)
  {    
    HeaderWriter writer = new HeaderWriter(settings);
    charset.setFontName(settings.fontName);
    try {
      writer.writeHeader(charset, new File(file), settings.commentPreview);
    }    
    catch (FileNotFoundException fnfe)
    {
      RSLogger.getLogger().log(Level.WARNING, "File not found", fnfe);
    }
    catch (IOException ioex)
    {
      RSLogger.getLogger().log(Level.SEVERE, "File not writeable", ioex);
    }
  }
}
