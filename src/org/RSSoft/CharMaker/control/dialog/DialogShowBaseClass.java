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
package org.RSSoft.CharMaker.control.dialog;

import java.awt.Dialog;
import java.awt.Point;
import java.util.Observable;
import javax.swing.JDialog;

/**
 * This class can open a JDialog
 * It is constructed with an existing JDialog object
 * A value represents the return state of the dialog
 * @author Richard
 */
public class DialogShowBaseClass extends Observable {
  
  protected final JDialog dialog;
  protected int dialogReturnValue;
  
  public final int DIALOG_OK = 1;
  public final int DIALOG_CANCEL = 0;

  /**
   * Construct a new Base Class showing a JDialog,
   * the dialog is modal to the running application
   * @param dialog the dialog to show
   */
  public DialogShowBaseClass(JDialog dialog) {
    this.dialog = dialog;
    this.dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
  }
  
  /**
   * returns the dialog return value (the button the user pressed)
   *  -DIALOG_OK
   *  -DIALOG_CANCEL
   * @return the dialog return value
   */
  public int getDialogReturn()
  {
    return this.dialogReturnValue;
  }
  
  /**
   * returns the dialog
   * @return the JDialog member
   */
  public JDialog getDialog()
  {
    return this.dialog;
  }
  
  /**
   * shows the dialog set via the constructor
   * @param p the point to show the dialog, can be null
   */
  public void showDialog(Point p)
  {
    if (p != null)
      this.dialog.setLocation(p);
    this.dialogReturnValue = DIALOG_CANCEL;
    this.dialog.setVisible(true);
    this.dialog.setEnabled(true);
  }
  
  /**
   * shows the dialog set via the constructor
   */
  public void showDialog()
  {
    this.showDialog(null);
  }
  
  /**
   * closes the dialog (sets the dialog invisible)
   */
  public void closeDialog()
  {
    this.dialog.setVisible(false);
    this.dialog.setEnabled(false);
  }
  
  /**
   * closes the dialog (sets the dialog invisible)
   * notifies all observers
   * @param dialogReturn the user interaction / the dialog's return value
   */
  protected void closeDialog(int dialogReturn)
  {
    this.dialogReturnValue = dialogReturn;
    this.dialog.setVisible(false);
    this.dialog.setEnabled(false);
    this.setChanged();
    this.notifyObservers();
  }
  
}
