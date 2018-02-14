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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;
import org.RSSoft.CharMaker.control.dialog.DialogShowBaseClass;
import org.RSSoft.CharMaker.util.RSLogger;
import org.RSSoft.CharMaker.view.CharMakerWindow;

/**
 * This class controls the exit of the program.
 * When the character set is not saved, a dialog will pop up asking the user to
 * save to a file.
 * @author Richard
 */
public class ControlExit extends WindowAdapter implements ActionListener {
  
  private final ControlNewOpenWriteCharset saveController;
  private final CharMakerWindow window;
  private final DialogShowBaseClass dialog;
  
  private final JButton buttonYes;
  private final JButton buttonNo;
  private final JButton buttonCancel;
  
  private final JMenuItem itemQuit;
  
  private final JLabel labelDialogQuit;
  
  public ControlExit(ControlNewOpenWriteCharset saveControl, CharMakerWindow window)
  {
    this.saveController = saveControl;
    this.window = window;
    this.dialog = new DialogShowBaseClass((window.getDialogExit()));
    
    this.buttonCancel = window.getButtonExitCancel();
    this.buttonNo = window.getButtonExitNo();
    this.buttonYes = window.getButtonExitYes();
    
    this.itemQuit = window.getMenuItemQuit();
    
    this.labelDialogQuit = window.getLabelSaveBeforeQuit();
    
    window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    window.addWindowListener(this);
    
    this.buttonCancel.addActionListener(this);
    this.buttonNo.addActionListener(this);
    this.buttonYes.addActionListener(this);
    
    this.itemQuit.addActionListener(this);
    
  }
  
    /**
   * set the labels of the user interface items
   * @todo: parameter labelcontainer for localization
   */
  public void setLabels()
  {
    this.labelDialogQuit.setText("Save project before exit?");
    this.buttonYes.setText("Save Project");
    this.buttonNo.setText("Exit without saving");
    this.buttonCancel.setText("return to application");
    
    this.dialog.getDialog().setTitle("Warning: Project not saved");
    
    this.dialog.getDialog().validate();
    
    this.itemQuit.setText("Quit");
  }
  
  private void reactOnUnsavedProject()
  {
    RSLogger.getLogger().log(Level.INFO, "character set not saved");
    
    Point p = new Point();    
    window.getLocation(p);
    p.x += window.getWidth() / 2 - dialog.getDialog().getWidth() / 2;
    p.y += window.getHeight() / 2 - dialog.getDialog().getHeight() / 2;
    
    this.dialog.showDialog(p);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    
    if (e.getSource().equals(this.itemQuit))
    {
      if (this.saveController.isChanged())
      {
        this.reactOnUnsavedProject();
      }
      else
      {
        window.dispose();
        System.exit(0);
      }
    }
    else if (e.getSource().equals(this.buttonCancel))
    {
      this.dialog.closeDialog();
    }
    else if (e.getSource().equals(this.buttonNo))
    {
      this.dialog.closeDialog();
      this.window.dispose();
      System.exit(0);
    }
    else if (e.getSource().equals(this.buttonYes))
    {
      this.dialog.closeDialog();
      this.saveController.showSaveDialog();      
    }
  }

  @Override
  public void windowClosing(WindowEvent e) {
    if (this.saveController.isChanged())
    {
      this.reactOnUnsavedProject();
    }
    else
    {
      window.dispose();
      System.exit(0);
    }
  }
}
