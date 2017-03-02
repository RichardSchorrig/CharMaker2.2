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

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.RSSoft.CharMaker.control.ControlCharacterSet;
import org.RSSoft.CharMaker.core.character.CharacterSet;
import org.RSSoft.CharMaker.util.RSLogger;

/**
 * this class holds a static function to read pixel characters from a bitmap
 * by parsing an xml file, reading the image information from it (filename, x and y position)
 * and adding characters to a character set via the controller
 * @author Richard
 */
public class BitmapReader
{
  private BitmapReader() {}
  
  /**
   * reads an xml file, extracts position information, and adds a character
   * to the given character set
   * @param filePath the path to the xml file
   * @param charaListController the controller used to add characters
   */
  public static void readBitmap(File filePath, ControlCharacterSet charaListController)
  {
    Document doc;
    ArrayList<BufferedImage> images = new ArrayList<>();
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      doc = db.parse(filePath);   
    }
    catch (Exception ex)
    {
      RSLogger.getLogger().log(Level.WARNING, "", ex);
      return;
    }
    
    NodeList nd = doc.getDocumentElement().getElementsByTagName("page");
    for (int i=0; i<nd.getLength(); i+=1)
    {
      Node n = nd.item(i);
      NamedNodeMap attributes = n.getAttributes();
      String imageName = attributes.getNamedItem("file").getNodeValue();
      File path = filePath.getParentFile();
      RSLogger.getLogger().log(Level.INFO, String.format("Opening image %s", path.getAbsolutePath() + File.separator + imageName));
      
      try
      {
        images.add(ImageIO.read(new File(path.getAbsolutePath()+File.separator+imageName)));
      }
      catch (IOException ex)
      {
        RSLogger.getLogger().log(Level.SEVERE, null, ex);
      }      
    }
    
    nd = doc.getDocumentElement().getElementsByTagName("info");
    NamedNodeMap info = nd.item(0).getAttributes();
    
    int size = Integer.decode(info.getNamedItem("size").getNodeValue());
    
    CharacterSet charSet = new CharacterSet(
            0,
            size,
            info.getNamedItem("face").getNodeValue());
    
    if (charaListController != null)
    {
      charaListController.setCurrentCharacterSet(charSet);
    }
    
    nd = doc.getDocumentElement().getElementsByTagName("char");
    for (int i=0; i<nd.getLength(); i+=1)
    {
      Node n = nd.item(i);
      NamedNodeMap attributes = n.getAttributes();
      int xStart = Integer.decode(attributes.getNamedItem("x").getNodeValue());
      int yStart = Integer.decode(attributes.getNamedItem("y").getNodeValue());
      int xOffset = Integer.decode(attributes.getNamedItem("xoffset").getNodeValue());
      int yOffset = Integer.decode(attributes.getNamedItem("yoffset").getNodeValue());
      int xEnd = Integer.decode(attributes.getNamedItem("width").getNodeValue());
      int yEnd = Integer.decode(attributes.getNamedItem("height").getNodeValue());
      int page = Integer.decode(attributes.getNamedItem("page").getNodeValue());
      char c = (char) ((Integer.decode(attributes.getNamedItem("id").getNodeValue())) & 0xFF);
      
      try {
        //      RSLogger.getLogger().log(Level.INFO, String.format("character %s @ %d,%d,%d,%d", c, xStart,yStart,xEnd,yEnd));
        charSet.addCharacter(c, DataGrid.convert(images.get(page).getData(new Rectangle(xStart, yStart, xEnd, yEnd)), xOffset, yOffset, size, 0));
      } catch (Exception ex) {
        RSLogger.getLogger().log(Level.WARNING, null, ex);
      }
      
    }
  }
  
}
