/*
 * Copyright (C) 2018 Richard.
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
package org.RSSoft.CharMaker.view;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * This extends JButton to display a picture instead of text
 * @author Richard
 */
public class PictureButton extends JButton {
  
  private final BufferedImage picture;
  
  public PictureButton(String fileName) throws IOException
  {
    this.picture = ImageIO.read(new File(fileName));
  }
  
  public PictureButton(InputStream is) throws IOException
  {
    this.picture = ImageIO.read(is);
  }
  
  public void set(String tooltip)
  {
    this.setToolTipText(tooltip);
    this.setText("");
    this.setIcon(new ImageIcon(picture));
    
    Dimension d = new Dimension(picture.getWidth() + 2, picture.getHeight() + 2);
    
    this.setSize(d);
    this.setPreferredSize(d);
    this.setMaximumSize(d);
  }
  
}
