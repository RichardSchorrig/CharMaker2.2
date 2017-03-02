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

import java.util.Observable;
import java.util.logging.Level;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import org.RSSoft.CharMaker.control.models.CharacterData;
import org.RSSoft.CharMaker.core.DataGrid;
import org.RSSoft.CharMaker.core.character.CharacterDescriptor;
import org.RSSoft.CharMaker.core.character.CharacterSet;
import org.RSSoft.CharMaker.util.RSLogger;
import org.RSSoft.CharMaker.view.CharMakerWindow;

/**
 * This class is responsible for all actions regarding the list of characters
 * (add, edit, copy, delete, sort, set)
 * @author Richard
 */
public class ControlCharacterSet extends Observable implements ListSelectionListener, ActionListener, Observer
{
  private int mode;
  private final int MODE_ADD = 1;
  private final int MODE_EDIT = 2;
  private final int MODE_COPY = 3;
  
  private final JList<String> characterList;
  private CharacterSet charSet;
  private final ControlGrid grid;
  private int currentSelection;
  
  private final JButton buttonAdd;
  private final JButton buttonRemove;
  private final JButton buttonSort;
  private final JButton buttonSet;
  private final JButton buttonPreview;
  private final JButton buttonEdit;
  private final JButton buttonCopy;
  
  private ControlPreview previewController;
  
  private final JTabbedPane tabPane;
  
  private final ControlShowCharacterDialog addCharacterController;
  
  /**
   * constructs a new controller for the character set
   * uses the class "CharMakerWindow" to extract the user interface
   * @param view the CharMakerWindow to use
   * @param grid the grid controller to use
   */
  public ControlCharacterSet(CharMakerWindow view, ControlGrid grid)
  {
    this.mode = 0;
    
    this.grid = grid;
    this.charSet = new CharacterSet(8, 8, "");
    this.characterList = view.getListChars();
    
    this.buttonAdd = view.getButtonAddChar();
    this.buttonRemove = view.getButtonRemoveChar();
    this.buttonSort = view.getButtonSortChars();
    this.buttonSet = view.getButtonSetChar();
    this.buttonPreview = view.getButtonShowPreview();
    this.buttonEdit = view.getButtonEditChar();
    this.buttonCopy = view.getButtonCopy();
    
    
    this.buttonAdd.addActionListener(this);
    this.buttonRemove.addActionListener(this);
    this.buttonSort.addActionListener(this);
    this.buttonSet.addActionListener(this);
    this.buttonPreview.addActionListener(this);
    this.buttonEdit.addActionListener(this);
    this.buttonCopy.addActionListener(this);
    
    this.characterList.addListSelectionListener(this);
//    this.characterList.addMouseListener(this);  //todo!
    this.characterList.clearSelection();
    this.characterList.removeAll();
    this.characterList.setModel(charSet);
    this.currentSelection = -1;
    
    this.addCharacterController = new ControlShowCharacterDialog(view);
    this.addCharacterController.setLabels();
    this.previewController = null;
    
    this.tabPane = view.getTabbedPaneFont();
  }
  
  /**
   * add an optional preview contoller (to show pixel font preview)
   * @param previewController the preview controller to use
   */
  public void setPreviewController(ControlPreview previewController)
  {
    this.previewController = previewController;
  }
  
  /**
   * set the labels of the user interface items
   * @todo: parameter labelcontainer for localization
   */
  public void setLabels()
  {
    this.buttonAdd.setText("add Character");
    this.buttonRemove.setText("remove Character");
    this.buttonSort.setText("Sort by ASCII value");
    this.buttonPreview.setText("Show Preview");
    this.buttonSet.setText("Set Character");
    this.buttonEdit.setText("Edit Character");
    this.buttonCopy.setText("Copy Character");
    
    this.tabPane.setTitleAt(0, "Output Format");
    this.tabPane.setTitleAt(1, "Character List");
  }
  
  /**
   * call if a new character set is created, the list of characters will
   * be updated
   * @param charset the new character set to use
   */
  public void setCurrentCharacterSet(CharacterSet charset)
  {
    this.charSet = charset;
    this.currentSelection = -1;
    this.characterList.clearSelection();
    this.characterList.removeAll();
    this.characterList.setModel(charset);
    grid.setDimensions(charset.getFontWidth(), charset.getFontHeight());
    this.setChanged();
    this.notifyObservers();
    RSLogger.getLogger().log(Level.INFO, String.format("set a new CharacterSet %s", charset.getFontName()));
  }
  
  /**
   * returns the character set
   * @return the character set
   */
  public CharacterSet getCurrentCharacterSet()
  {
    return charSet;
  }
  
  /**
   * returns the list selection of the character set
   * @return the Character Descriptor selected by the user
   * @throws Exception if a invalid selection is made (selected index bigger than
   * character set size)
   */
  public CharacterDescriptor getSelectedCharacterDescriptor() throws Exception
  {
    if (this.currentSelection != -1)
      return this.charSet.getCharacterAt(currentSelection);
    
    return null;
  }
  
  /**
   * add a new character descriptor to the character set
   * @param character the character descriptor to add
   */
  public void addCharacter(CharacterDescriptor character)
  {
    try {
      this.charSet.addCharacter(character.getCharacter(), character.getGrid());
      this.setChanged();
      this.notifyObservers();
    } catch (Exception ex) {
      RSLogger.getLogger().log(Level.SEVERE, null, ex);
    }
  }
  
  /**
   * add a new character consisting of the pure character c and the string
   * description of the character, an empty grid with the size according 
   * to the height and width is constructed and added to the character descriptor
   * @param c the character to add
   * @param description the string description to the character
   */
  public void addCharacter(char c, String description)
  {
    try {
      this.charSet.addCharacter(c, description, new DataGrid(charSet.getFontWidth(), charSet.getFontHeight()));
      this.setChanged();
      this.notifyObservers();
    } catch (Exception ex) {
      RSLogger.getLogger().log(Level.SEVERE, null, ex);
    }
  }
  
  /**
   * add a new character consisting of the pure character c and the string
   * description of the character, an empty grid with the size according 
   * to the height and width is constructed and added to the character descriptor
   * @param c the character to add
   * @param description the string description to the character
   * @param width the width of the character grid
   */
  public void addCharacter(char c, String description, int width)
  {
    try {
      if (this.charSet.isVariableWidth())
        this.charSet.addCharacter(c, description, width);
      else
      {
        try {
          this.charSet.addCharacter(c, description);
        } catch (Exception ex) {
          RSLogger.getLogger().log(Level.SEVERE, null, ex);
        }
      }
      this.setChanged();
      this.notifyObservers();
    } catch (Exception ex) {
      RSLogger.getLogger().log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public void valueChanged(ListSelectionEvent e)
  {
    this.currentSelection = this.characterList.getSelectedIndex();
    if (this.currentSelection >= 0)
    {
      try {
        grid.setDimensions(charSet.getCharacterAt(this.currentSelection).getGrid().getXSize(),
                charSet.getCharacterAt(this.currentSelection).getGrid().getYSize());
        grid.getGridPane().setGrid(charSet.getCharacterAt(this.currentSelection).getGrid());
      } catch (Exception ex) {
        RSLogger.getLogger().log(Level.SEVERE, null, ex);
      }
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == this.buttonAdd)
    {
      this.mode = MODE_ADD;
      this.addCharacterController.addObserver(this);
      this.addCharacterController.showDialog(this.buttonAdd.getLocationOnScreen(),
//                                             this.grid.isVariableColumnNumber(),
                                             this.charSet.isVariableWidth(),
                                             this.charSet.getFontWidth());
    }
    else if (e.getSource() == this.buttonRemove)
    {
      if (this.characterList.getSelectedIndex() != -1)
      {
        int selection = this.characterList.getSelectedIndex();
        this.characterList.clearSelection();
        this.charSet.removeCharacter(selection);
        this.characterList.setSelectedIndex(selection);
      }
    }
    else if (e.getSource() == this.buttonSort)
    {
      this.charSet.sort();
    }
    else if (e.getSource() == this.buttonPreview)
    {
      if (this.currentSelection != -1 && this.previewController != null)
      {
        try {
          CharacterDescriptor selection = this.charSet.getCharacterAt(this.currentSelection);
          this.previewController.addCharacter(selection.getGrid());
        } catch (Exception ex) {
          RSLogger.getLogger().log(Level.SEVERE, null, ex);
        }
      }
    }
    else if (e.getSource() == this.buttonSet)
    {
      if (this.currentSelection != -1)
      {
        try {
          CharacterDescriptor selection = this.charSet.getCharacterAt(this.currentSelection);
          selection.setGrid(grid.getGridPane().getGrid());
        } catch (Exception ex) {
          RSLogger.getLogger().log(Level.SEVERE, null, ex);
        }
      }
    }
    else if (e.getSource() == this.buttonEdit)
    {
      if (this.currentSelection != -1)
      {
        try {
          this.mode = MODE_EDIT;
          this.addCharacterController.addObserver(this);
          CharacterDescriptor selection = this.charSet.getCharacterAt(this.currentSelection);
          this.addCharacterController.showDialog(selection,
                  this.buttonEdit.getLocationOnScreen(),
//                  this.grid.isVariableColumnNumber(),
                  this.charSet.isVariableWidth(),
                  this.charSet.getFontWidth());
        } catch (Exception ex) {
          RSLogger.getLogger().log(Level.SEVERE, null, ex);
        }
      }
    }
    else if (e.getSource() == this.buttonCopy) {
      if (this.currentSelection != -1) {
        this.mode = MODE_COPY;
        this.addCharacterController.addObserver(this);
        this.addCharacterController.showDialog(this.buttonAdd.getLocationOnScreen(),
//                                               this.grid.isVariableColumnNumber(),
                                               this.charSet.isVariableWidth(),
                                               this.charSet.getFontWidth());
      }
    }
  }

  @Override
  public void update(Observable o, Object arg) {
    int decicion = this.addCharacterController.getDialogReturn();
    if (decicion == this.addCharacterController.CHARACTERDIALOG_OK)
    {
      CharacterData d = this.addCharacterController.getCharacterData();
      switch (mode) {
      case MODE_ADD: {          
          this.addCharacter(d.character, d.description, d.width);
          this.currentSelection = this.charSet.getSize()-1;
          this.characterList.setSelectedIndex(this.currentSelection);
          this.characterList.ensureIndexIsVisible(this.currentSelection);
          this.valueChanged(null);
          break;
        }
        case MODE_EDIT: {
        try {
          this.getSelectedCharacterDescriptor().setCharacter(d.character);
          this.getSelectedCharacterDescriptor().setDescription(d.description);
          if (d.width != this.getSelectedCharacterDescriptor().getWidth())
          {
            this.getSelectedCharacterDescriptor().setWidth(d.width);
          }
          this.charSet.update(this.characterList.getSelectedIndex());
          this.characterList.setSelectedIndex(this.currentSelection);
          this.characterList.ensureIndexIsVisible(this.currentSelection);
          this.valueChanged(null);
          break;
        } catch (Exception ex) {
          RSLogger.getLogger().log(Level.SEVERE, null, ex);
        }
        }
        case MODE_COPY: {
        try {
          this.addCharacter(d.character, d.description, d.width);
          DataGrid dgrid = this.getSelectedCharacterDescriptor().getGrid();
          this.currentSelection = this.charSet.getSize()-1;
          this.characterList.setSelectedIndex(this.currentSelection);
          this.characterList.ensureIndexIsVisible(this.currentSelection);
          this.getSelectedCharacterDescriptor().setGrid(dgrid);
          this.valueChanged(null);
          break;
          
        } catch (Exception ex) {
          RSLogger.getLogger().log(Level.SEVERE, null, ex);
        }
        
        }
        default: break;
      }
      
    }
    this.addCharacterController.deleteObserver(this);
  }
}
