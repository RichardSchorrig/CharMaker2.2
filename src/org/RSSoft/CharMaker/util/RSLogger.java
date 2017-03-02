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
package org.RSSoft.CharMaker.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * a more convenient logger, can be controlled by a file named logger.properties
 * @author Richard
 */
public class RSLogger
{
  private static Logger instance = null;
  
  private RSLogger()
  {
  }
  
  /**
   * returns the instance logger.
   * If instance is not available yet, it will be initialized and returned
   * @return 
   */
  public static Logger getLogger()
  {
    if (instance == null)
    {
      instance = Logger.getLogger("RSLogger");
      initLogger();
    }
    return instance;
  }
  
  private static void initLogger()
  {
    try
    {
      RSPropertiesReader_Logger propsReader = new RSPropertiesReader_Logger("logger");
      
      FileHandler fh = new FileHandler(propsReader.getProperty("FILE"), 
              10000, 1, Boolean.parseBoolean(propsReader.getProperty("APPEND")));
      fh.setFormatter(new RSFormatter());
      instance.addHandler(fh);
      instance.setLevel(Level.parse(propsReader.getProperty("LEVEL")));
    }
    catch (IOException ex)
    {
      System.err.println(ex);
    }
    catch (NullPointerException npex)
    {
      System.err.println(npex);
    }
    catch (IllegalArgumentException iae)
    {
      instance.setLevel(Level.ALL);
      instance.severe("Could not parse level from properties file" + iae.toString());
    }
  }
}
