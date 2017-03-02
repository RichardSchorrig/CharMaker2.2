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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.RSSoft.CharMaker.control.models.SpinnerDecimalModel;
import org.RSSoft.CharMaker.core.DataGrid;
import org.RSSoft.CharMaker.core.character.CharacterDescriptor;
import org.RSSoft.CharMaker.util.RSLogger;
import org.RSSoft.CharMaker.view.CharMakerWindow;

/**
 * This class is responsible for controlling the large preview area
 * @author Richard
 */
public class ControlPreview implements ActionListener {
  
  private final JPanel panelFontPreview;
  private final JTextArea textAreaPreviewText;
  private final JButton buttonAdd;
  private final JButton buttonClear;
  private final SpinnerDecimalModel spinnerPixelSize;
  private final JLabel labelPixelSize;
  
  private final int offset = 2;
  
  private int xPos, yPos;
  
  private final ControlCharacterSet charsetController;
  
  /**
   * construct a new preview controller
   * uses the class "CharMakerWindow" to extract the user interface
   * @param view the CharMakerWindow to use
   * @param charsetController the character set controller for writing text
   *                           and showing it in the preview pane
   */
  public ControlPreview(CharMakerWindow view, ControlCharacterSet charsetController)
  {
    this.charsetController = charsetController;
    this.panelFontPreview = view.getPanelPreviewFont();
    this.buttonAdd = view.getButtonAddText();
    this.buttonClear = view.getButtonClearText();
    this.spinnerPixelSize = new SpinnerDecimalModel(1, 10);
    view.getSpinnerPreviewPixelSize().setModel(spinnerPixelSize);
    this.textAreaPreviewText = view.getTextAreaPreviewInput();
    this.labelPixelSize = view.getLabelPreviewPixelSize();
    
    this.buttonAdd.addActionListener(this);
    this.buttonClear.addActionListener(this);
    
    this.panelFontPreview.setOpaque(true);
    this.panelFontPreview.setBackground(Color.WHITE);
    
    this.xPos = yPos = this.offset;
  }
  
  /**
   * set the labels of the user interface items
   * @todo: parameter labelcontainer for localization
   */
  public void setLabels()
  {
    this.buttonAdd.setText("add");
    this.buttonClear.setText("clear");
    this.labelPixelSize.setText("Preview Pixel Size");
    this.textAreaPreviewText.setText("Preview Text");
  }
  
  /**
   * add a character by the grid information to the preview pane
   * @param grid the pixel information of the character
   */
  public void addCharacter(DataGrid grid)
  {
    Graphics2D g2 = (Graphics2D)this.panelFontPreview.getGraphics();
    g2.setColor(Color.black);    
    int pixelSize = (int)this.spinnerPixelSize.getValue();

//    Rectangle2D.Float pixel = new Rectangle2D.Float(xPos, yPos, pixelSize, pixelSize);

    for (int i=0; i<grid.getXSize(); i+=1)
    {
      for (int j=0; j<grid.getYSize(); j+=1)
      {
        try {
          if (grid.isSetAt(i, j))
          {
            //  pixel.setFrame(xPos+(i*pixelSize), yPos+(j*pixelSize), pixelSize, pixelSize);
            g2.fillRect(xPos+(i*pixelSize), yPos+(j*pixelSize), pixelSize, pixelSize);
            //  g2.draw(pixel);
            
          }
        } catch (Exception ex) {  //this should never happen
          RSLogger.getLogger().log(Level.SEVERE, null, ex);
        }
      }
    }
    this.xPos += (grid.getXSize() + 1) * pixelSize;
    
    g2.dispose();
  }
  
  /**
   * add a character to the preview pane.
   * the character information is obtained via the character controller.
   * if the character is not in the list, nothing is drawn
   * @param c 
   */
  public void addCharacter(char c)
  {
    CharacterDescriptor ch = this.charsetController.getCurrentCharacterSet().getCharacter(c);
    if (ch != null)
    {
      this.addCharacter(ch.getGrid());
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this.buttonAdd)
    {
      String str = this.textAreaPreviewText.getText();
      for (char c : str.toCharArray())
      {
        if (c=='\n')
        {
          this.yPos += (this.charsetController.getCurrentCharacterSet().getFontHeight() + this.offset) * (int)this.spinnerPixelSize.getValue();
          this.xPos = this.offset;
        }
        else
        {
          this.addCharacter(c);
        }
      }      
    }
    else
    {
      this.panelFontPreview.repaint();
      this.xPos = this.yPos = this.offset;
    }
  }
  
}
