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

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Array;
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
  
  private final PictureButton[] buttons;
  
  public ControlGridTransform(CharMakerWindow view, ControlGrid gridController) throws IOException
  {
    gridpane = gridController.getGridPane();
    
    JPanel panelMoveButtons = view.getPanelButtonsMove();
    JPanel panelTransformButtons = view.getPanelButtonsTransform();
    
    GridBagConstraints c = new GridBagConstraints();
    
    buttons = new PictureButton[8];
    
    buttonMoveUp = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/up.png"));
    buttonMoveUp.setInactivePicture(this.getClass().getClassLoader().getResourceAsStream("media/up_inactive.png"));
    c.gridx = 1;
    c.gridy = 0;
    panelMoveButtons.add(buttonMoveUp, c);
    buttons[0] = buttonMoveUp;
    
    buttonMoveDown = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/down.png"));
    buttonMoveDown.setInactivePicture(this.getClass().getClassLoader().getResourceAsStream("media/down_inactive.png"));
    c.gridx = 1;
    c.gridy = 2;
    panelMoveButtons.add(buttonMoveDown, c);
    buttons[1] = buttonMoveDown;
    
    buttonMoveLeft = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/left.png"));
    buttonMoveLeft.setInactivePicture(this.getClass().getClassLoader().getResourceAsStream("media/left_inactive.png"));
    c.gridx = 0;
    c.gridy = 1;
    panelMoveButtons.add(buttonMoveLeft, c);
    buttons[2] = buttonMoveLeft;
    
    buttonMoveRight = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/right.png"));
    buttonMoveRight.setInactivePicture(this.getClass().getClassLoader().getResourceAsStream("media/right_inactive.png"));
    c.gridx = 2;
    c.gridy = 1;
    panelMoveButtons.add(buttonMoveRight, c);
    buttons[3] = buttonMoveRight;
    
    
    buttonMirrorH = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/MirrorH.png"));
    buttonMirrorH.setInactivePicture(this.getClass().getClassLoader().getResourceAsStream("media/MirrorH_inactive.png"));
    c.gridx = 1;
    c.gridy = 0;
    panelTransformButtons.add(buttonMirrorH, c);
    buttons[4] = buttonMirrorH;
    
    buttonMirrorV = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/MirrorV.png"));
    buttonMirrorV.setInactivePicture(this.getClass().getClassLoader().getResourceAsStream("media/MirrorV_inactive.png"));
    c.gridx = 1;
    c.gridy = 2;
    panelTransformButtons.add(buttonMirrorV, c);
    buttons[5] = buttonMirrorV;
    
    buttonRotateLeft = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/RotateLeft.png"));
    buttonRotateLeft.setInactivePicture(this.getClass().getClassLoader().getResourceAsStream("media/RotateLeft_inactive.png"));
    c.gridx = 0;
    c.gridy = 1;
    panelTransformButtons.add(buttonRotateLeft, c);
    buttons[6] = buttonRotateLeft;
    
    buttonRotateRight = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/RotateRight.png"));
    buttonRotateRight.setInactivePicture(this.getClass().getClassLoader().getResourceAsStream("media/RotateRight_inactive.png"));
    c.gridx = 2;
    c.gridy = 1;
    panelTransformButtons.add(buttonRotateRight, c);
    buttons[7] = buttonRotateRight;
    
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
  
  public void setActive(boolean activate)
  {
    for (PictureButton b : buttons)
    {
      b.setEnabled(activate);
    }
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
