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
package org.RSSoft.CharMaker.control;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.RSSoft.CharMaker.core.GridIterator;
import org.RSSoft.CharMaker.view.CharMakerWindow;
import org.RSSoft.CharMaker.view.GridPane;
import org.RSSoft.CharMaker.view.PictureButton;

/**
 *
 * @author Richard
 */
public class ControlGridTransform implements ActionListener
{
  
  private final GridPane gridpane;
  
  private final PictureButton buttonMoveUp;
  private final PictureButton buttonMoveDown;
  private final PictureButton buttonMoveLeft;
  private final PictureButton buttonMoveRight;
  
  private final PictureButton buttonMirrorH;
  private final PictureButton buttonMirrorV;
  private final PictureButton buttonRotateLeft;
  private final PictureButton buttonRotateRight;
  
  public ControlGridTransform(CharMakerWindow view, ControlGrid gridController) throws IOException
  {
    gridpane = gridController.getGridPane();
    
    JPanel panelMoveButtons = view.getPanelButtonsMove();
    JPanel panelTransformButtons = view.getPanelButtonsTransform();
    
    GridBagConstraints c = new GridBagConstraints();
    
    buttonMoveUp = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/up.png"));
    c.gridx = 1;
    c.gridy = 0;
    panelMoveButtons.add(buttonMoveUp, c);
    
    buttonMoveDown = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/down.png"));
    c.gridx = 1;
    c.gridy = 2;
    panelMoveButtons.add(buttonMoveDown, c);
    
    buttonMoveLeft = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/left.png"));
    c.gridx = 0;
    c.gridy = 1;
    panelMoveButtons.add(buttonMoveLeft, c);
    
    buttonMoveRight = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/right.png"));
    c.gridx = 2;
    c.gridy = 1;
    panelMoveButtons.add(buttonMoveRight, c);
    
    
    buttonMirrorH = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/MirrorH.png"));
    c.gridx = 1;
    c.gridy = 0;
    panelTransformButtons.add(buttonMirrorH, c);
    
    buttonMirrorV = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/MirrorV.png"));
    c.gridx = 1;
    c.gridy = 2;
    panelTransformButtons.add(buttonMirrorV, c);
    
    buttonRotateLeft = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/RotateLeft.png"));
    c.gridx = 0;
    c.gridy = 1;
    panelTransformButtons.add(buttonRotateLeft, c);
    
    buttonRotateRight = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/RotateRight.png"));
    c.gridx = 2;
    c.gridy = 1;
    panelTransformButtons.add(buttonRotateRight, c);
    
    buttonMoveUp.addActionListener(this);
    buttonMoveRight.addActionListener(this);
    buttonMoveLeft.addActionListener(this);
    buttonMoveDown.addActionListener(this);
    
    buttonMirrorH.addActionListener(this);
    buttonMirrorV.addActionListener(this);
    buttonRotateLeft.addActionListener(this);
    buttonRotateRight.addActionListener(this);
  }
  
  public void setLabels()
  {
    buttonMoveUp.set("up");
    buttonMoveRight.set("right");
    buttonMoveLeft.set("left");
    buttonMoveDown.set("down");
    
    buttonMirrorH.set("Mirror horizontally");
    buttonMirrorV.set("Mirror vertically");
    buttonRotateLeft.set("Rotate left");
    buttonRotateRight.set("Rotate right");
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      if (e.getSource().equals(buttonMirrorH))
      {
        gridpane.getGrid().mirror(true, false);
      }
      else if (e.getSource().equals(buttonMirrorV))
      {
        gridpane.getGrid().mirror(false, true);
      }
      else if (e.getSource().equals(buttonRotateLeft))
      {
        gridpane.getGrid().rotate(GridIterator.ROTATION_90);
      }
      else if (e.getSource().equals(buttonRotateRight))
      {
        gridpane.getGrid().rotate(GridIterator.ROTATION_270);
      }
    } catch (Exception ex) {
      Logger.getLogger(ControlGridTransform.class.getName()).log(Level.SEVERE, null, ex);
    }
    gridpane.repaint();
  }
  
}
