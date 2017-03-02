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

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

/**
 * a class that extends class properties
 * @see Properties
 * 
 * @author Richard
 */
public class RSPropertiesReader extends Properties
{
  protected String filepath;
  protected boolean error;

  /**
   * @param file: the name of the properties file, without the extention ".properties"
   * the file should be located in the folder of the application.
   * If the file does not exist, a default file will be written with the specified name
   * 
   * @exception IOException in case the default file cannot be written
   * 
   */
  public RSPropertiesReader(String file) throws IOException
  {
    this.filepath = file;
    this.error = false;
    FileInputStream fileInputStream;

    try
    {
      fileInputStream = new FileInputStream(file + ".properties");
      this.load(fileInputStream);
    }
    catch (FileNotFoundException ex)
    {
      this.error = true;
    }
  }
  
  protected void writeDefaultText(String defaultText) throws IOException
  {
    DataOutputStream ostream = new DataOutputStream(
              new FileOutputStream(filepath + ".properties"));
    ostream.writeBytes(defaultText);
    ostream.close();
    
    FileInputStream fileInputStream = new FileInputStream(filepath + ".properties");
    this.load(fileInputStream);
  }
  
  public void storeToFile()
  {
    try {
      DataOutputStream ostream = new DataOutputStream(
              new FileOutputStream(filepath + ".properties", false));
      this.store(ostream, "");
      ostream.close();
    } catch (Exception ex) {
      RSLogger.getLogger().log(Level.WARNING, null, ex);
    }
  }
}
