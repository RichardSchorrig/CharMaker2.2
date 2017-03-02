/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.RSSoft.CharMaker.util;

import java.io.IOException;

/**
 *
 * @author Richard
 */
public class SavedSettings extends RSPropertiesReader {
  
  private final String defaultSettings = "#Do not delete this file\n"
                                       + "#This file was automatically generated\n"
                                       + "\n"
                                       + "#default path:\n"
                                       + "PATH=./\n";
    
  public SavedSettings(String file) throws IOException
  {
    super(file);
    
    if (this.error)
    {
      this.writeDefaultText(defaultSettings);
    }
  }    
}
