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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.RSSoft.CharMaker.control.dialog.DialogShowBaseClass;
import org.RSSoft.CharMaker.control.models.CharacterData;
import org.RSSoft.CharMaker.control.models.SpinnerDecimalModel;
import org.RSSoft.CharMaker.control.models.SpinnerHexaModel;
import org.RSSoft.CharMaker.core.character.Char2Description;
import org.RSSoft.CharMaker.core.character.CharacterDescriptor;
import org.RSSoft.CharMaker.view.CharMakerWindow;

/**
 * shows a dialog to customize character settings (character, grid width, description...)
 * stores those settings in a CharacterDescriptor class when the dialog is confirmed
 * uses the class "CharMakerWindow" to extract the user interface
 * 
 * @author Richard
 */
public class ControlShowCharacterDialog extends DialogShowBaseClass implements ActionListener, ChangeListener {
    
  private final JButton buttonOK;
  private final JButton buttonCancel;
  private final JSpinner spinnerInt;
  private final JSpinner spinnerHex;
  private final JSpinner spinnerColumns;
  private final JTextField textFieldCharacter;
  private final JTextField textFieldDescription;
  
  private final JLabel labelCharacter;
  private final JLabel labelCharacterChar;
  private final JLabel labelCharacterDec;
  private final JLabel labelCharacterHex;
  private final JLabel labelDescription;
  private final JLabel labelColumns;
  
  private final SpinnerDecimalModel spinnerModelDecimal;
  private final SpinnerHexaModel spinnerModelHex;
  private final SpinnerDecimalModel spinnerModelColumns;
  
  private final CharacterData characterData;
  
  public final int CHARACTERDIALOG_OK = 1;
  public final int CHARACTERDIALOG_CANCEL = 0;
  
  /**
   * constructs a new controller for the character settings dialog
   * @param view the CharMakerWindow to use
   */
  public ControlShowCharacterDialog(CharMakerWindow view)
  {
    super(view.getDialogAddCharacter());
    
    view.getDialogButtonCancel().setText("Cancel");
    view.getDialogButtonOK().setText("OK");
    
    this.buttonCancel = view.getDialogButtonCancel();
    this.buttonOK = view.getDialogButtonOK();
    
    this.buttonCancel.addActionListener(this);
    this.buttonOK.addActionListener(this);
    
    this.labelCharacter = view.getDialogLabelCharacter();
    this.labelCharacterChar = view.getDialogLabelCharacterChar();
    this.labelCharacterHex = view.getDialogLabelCharacterHex();
    this.labelCharacterDec = view.getDialogLabelCharacterInt();
    this.labelDescription = view.getDialogLabelDescription();
    this.labelColumns = view.getDialogLabelColumns();
    
    this.spinnerInt = view.getDialogSpinnerCharacterInt();
    this.spinnerHex = view.getDialogSpinnerCharacterHex();
    this.spinnerColumns = view.getDialogSpinnerColumns();
    
    this.spinnerModelDecimal = new SpinnerDecimalModel(0, 255);
    this.spinnerModelHex = new SpinnerHexaModel(0, 255);
    this.spinnerModelColumns = new SpinnerDecimalModel(1, 32);
    
    this.spinnerInt.setModel(spinnerModelDecimal);
    this.spinnerHex.setModel(spinnerModelHex);
    this.spinnerColumns.setModel(spinnerModelColumns);
    
    this.spinnerInt.addChangeListener(this);
    this.spinnerHex.addChangeListener(this);
    
    this.textFieldCharacter = view.getDialogTextFieldCharacterChar();
    this.textFieldDescription = view.getDialogTextFieldDescription();
    
    this.textFieldCharacter.addActionListener(this);
    this.textFieldCharacter.setColumns(1);
    
    this.characterData = new CharacterData();
    
    this.setCharacterChar('a');
  }
  
  /**
   * set the dialog's labels
   * todo: parameter labelcontainer for localization
   */
  public void setLabels()
  {
    this.labelCharacter.setText("Character");
    this.labelCharacterChar.setText("char");
    this.labelCharacterHex.setText("Hex");
    this.labelCharacterDec.setText("Dec");
    this.labelDescription.setText("Description");
    this.labelColumns.setText("Columns");
  }
  
  /**
   * returns the character data set by the dialog
   * when the dialog is cancelled, character data containing the letter 'a'
   * is returned
   * @return the character data as CharacterData object
   */
  public CharacterData getCharacterData()
  {
    return this.characterData;
  }
  
  /**
   * to be called internally
   * sets the dialog's character to c
   * @param c the character to be shown by the two spinner, the field character,
   * and the field description
   */
  private void setCharacterChar(char c)
  {
    this.spinnerHex.removeChangeListener(this);
    this.spinnerInt.removeChangeListener(this);
    
    this.textFieldCharacter.setText(String.format("%c", c));
    this.spinnerModelDecimal.setValue((int) c);
    this.spinnerModelHex.setValue((int) c);
    
    this.textFieldDescription.setText(Char2Description.getDescription(c));
    
    this.spinnerInt.addChangeListener(this);
    this.spinnerHex.addChangeListener(this);
  }
  
  /**
   * to be called internally
   * changes the integer d to a character c and calls
   * setCharacterChar()
   * @param d the value of the character to be set
   */
  private void setCharacterDec(int d)
  {
    if (d <= 0xFF) {
      this.setCharacterChar((char) d);
    }
  }
  
  /**
   * shows the dialog to set the character settings
   * @param desc a description of the character to be changed, the character is
   * set in the dialog (can be null in case a new character is added)
   * @param p a point on the screen to show the dialog
   * @param varColumnWidth wether to enable the spinner for the character width
   * @param width the value to set the spinner to
   */
  public void showDialog(CharacterDescriptor desc, Point p, boolean varColumnWidth, int width)
  {
    this.spinnerColumns.setEnabled(varColumnWidth);
    this.spinnerModelColumns.setDecimalValue(width);
    if (desc != null)
    {
      this.setCharacterChar(desc.getCharacter());
      this.textFieldDescription.setText(desc.getDescriptor());
      this.spinnerModelColumns.setDecimalValue(desc.getWidth());
    }
    this.showDialog(p);
  }
  
  /**
   * shows the dialog to set the character settings
   * @param p a point on the screen to show the dialog
   * @param varColumnWidth wether to enable the spinner for the character width
   * @param width the value to set the spinner to
   */
  public void showDialog(Point p, boolean varColumnWidth, int width)
  {
    this.showDialog(null, p, varColumnWidth, width);
  }

  /**
   * shows the dialog to set the character settings
   * @param desc a description of the character to be changed, the character is
   * set in the dialog (can be null in case a new character is added)
   * @param varColumnWidth wether to enable the spinner for the character width
   * @param width the value to set the spinner to
   */
  public void showDialog(CharacterDescriptor desc, boolean varColumnWidth, int width)
  {
    this.showDialog(desc, null, varColumnWidth, width);
  }

  /**
   * shows the dialog to set the character settings
   * @param varColumnWidth wether to enable the spinner for the character width
   * @param width the value to set the spinner to
   */
  public void showDialog(boolean varColumnWidth, int width)
  {
    this.showDialog(null, null, varColumnWidth, width);
  }

  /**
   * to be called by the action performer (buttons)
   * @param e the action event performed
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    
    if (e.getSource() == this.buttonCancel)
    {
      this.closeDialog(this.CHARACTERDIALOG_CANCEL);
    }
    else if (e.getSource() == this.buttonOK)
    {
      char c = this.textFieldCharacter.getText().charAt(0);
      this.characterData.character = c;
      if (this.textFieldDescription.getText().isEmpty()) {
        this.characterData.description = Char2Description.getDescription(c);
      }
      else {
        this.characterData.description = this.textFieldDescription.getText();
      }
      this.characterData.width = this.spinnerModelColumns.getDecimalValue();
      this.closeDialog(this.CHARACTERDIALOG_OK);
    }
    else if (e.getSource() == this.textFieldCharacter)
    {
      this.characterData.character = this.textFieldCharacter.getText().charAt(0);
      this.setCharacterChar(this.characterData.character);
    }   
  }

  /**
   * to be called by the change event performer (spinner)
   * @param e the change event performed
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    if (e.getSource() == this.spinnerHex)
    {      
      this.setCharacterDec(this.spinnerModelHex.getDecimalValue());      
    }
    else if (e.getSource() == this.spinnerInt)
    {
      this.setCharacterDec(this.spinnerModelDecimal.getDecimalValue());
    }
  }  
}
