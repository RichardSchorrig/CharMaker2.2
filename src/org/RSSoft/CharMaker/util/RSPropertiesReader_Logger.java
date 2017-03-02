/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.RSSoft.CharMaker.util;

import java.io.IOException;

/**
 * the extention of the class RSPropertiesReader designed for the logger
 * @author Richard
 */
public class RSPropertiesReader_Logger extends RSPropertiesReader {
  
    /**
   * string representation of the default properties file in case the file cannot be found
   */
  private final String defaultText = "#Do not delete this file\n"
                                  + "#This file was automatically generated\n"
                                  + "#log file:\n"
                                  + "FILE=log.txt\n"
                                  + "#logging level\n"
                                  + "LEVEL=ALL\n"
                                  + "#Append to existing file (true/false)\n"
                                  + "APPEND=true\n";
  
  public RSPropertiesReader_Logger(String file) throws IOException
  {
    super(file);
    if (this.error)
    {
      this.writeDefaultText(defaultText);
    }
  }
  
}
