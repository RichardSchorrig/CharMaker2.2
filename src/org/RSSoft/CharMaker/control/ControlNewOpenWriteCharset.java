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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.RSSoft.CharMaker.control.models.FontSettings;
import org.RSSoft.CharMaker.core.BitmapReader;
import org.RSSoft.CharMaker.core.DataGrid;
import org.RSSoft.CharMaker.core.character.CharacterSet;
import org.RSSoft.CharMaker.util.RSLogger;
import org.RSSoft.CharMaker.view.CharMakerWindow;

/**
 * This class is responsible for the operations
 *  -New: add a new character set that is either empty or filled with ascii
 *         values
 *  -Open (bitmap): attempts to read a xml file and the associated bitmap file, 
 *                    constructs a new character set containing all ascii values
 *                    found in the xml
 *  -Open (saved project): deserialize a saved character set from file
 *  -Save (project): serialize the current character set to file
 *  -Write (Header): writes a c header file with all characters in the set
 * @author richard
 */
public class ControlNewOpenWriteCharset implements ActionListener, Observer
{
    
  private CharMakerWindow view;
  private final ControlCharacterSet list;

  private int operation;
  private final int NOOPERATION = -1;
  private final int OPERATION_OPEN_XML = 0;
  private final int OPERATION_WRITE_HEADER = 1;
  private final int OPERATION_NEW = 2;
  private final int OPERATION_LOAD = 3;
  private final int OPERATION_SAVE = 4;

  private final JButton buttonNew;
  private final JMenuItem itemNew;

  private final JButton buttonOpen_XML;
  private final JMenuItem itemOpen_XML;

  private final JButton buttonWrite_CHeader;
  private final JMenuItem itemWrite_CHeader;

  private final JMenuItem itemSave;
  private final JMenuItem itemLoad;

  private final ControlHeaderWriter headerWriter;
  private final ControlGrid gridController;
  private final ControlFontSettings fontSettings;
  private final ControlFileIO fileController;

  /**
   * construct a new controller for New, open, write operation
   * 
   * @param view the CharMakerWindow containing the user interface
   * @param list the character list controller used for adding characters
   * @param gridController the grid controller used for the "new" dialog
   * @param fileController the file controller for file io
   * @param settings font settings controller for Header file writer
   */
  public ControlNewOpenWriteCharset(CharMakerWindow view,
                           ControlCharacterSet list,
                           ControlGrid gridController,
                           ControlFileIO fileController,
                           ControlFontSettings settings)
  {
    this.view = view;
    this.list = list;

    this.buttonNew = view.getButtonNew();
    this.itemNew = view.getMenuItemNewCharacterSet();

    this.buttonOpen_XML = view.getButtonOpen();
    this.itemOpen_XML = view.getMenuItemLoadXML();

    this.itemLoad = view.getMenuItemLoadCharacterSet();

    this.buttonWrite_CHeader = view.getButtonSave();
    this.itemWrite_CHeader = view.getMenuItemWriteHeader();

    this.itemSave = view.getMenuItemSaveCharacterSet();

    this.buttonNew.addActionListener(this);
    this.itemNew.addActionListener(this);

    this.buttonOpen_XML.addActionListener(this);
    this.itemOpen_XML.addActionListener(this);

    this.buttonWrite_CHeader.addActionListener(this);
    this.itemWrite_CHeader.addActionListener(this);

    this.itemLoad.addActionListener(this);
    this.itemSave.addActionListener(this);        

    this.headerWriter = new ControlHeaderWriter();

    this.gridController = gridController;
    this.fileController = fileController;
    this.fontSettings = settings;
  }

  /**
  * set the labels of the user interface items
  * @todo: parameter labelcontainer for localization
  */
  public void setLabels()
  {
    this.buttonNew.setText("New Character Set");
    this.itemNew.setText("New Character Set");

    this.buttonOpen_XML.setText("Open Bitmap Font");
    this.itemOpen_XML.setText("Open Bitmap Font");

    this.buttonWrite_CHeader.setText("Write Header File");
    this.itemWrite_CHeader.setText("Write Header File");

    this.itemSave.setText("Save Character Set");
    this.itemLoad.setText("Load Character Set");
  }

  /**
   * save the character set by serializing it to a file
   * @param toFile the file path of the file to serialize the character set to
   * @throws IOException in case of IO Error
   */
  public void saveCharacterSet(String toFile) throws IOException
  {
    FileOutputStream fos = new FileOutputStream(new File(toFile));
    BufferedOutputStream buffout = new BufferedOutputStream(fos);
    ObjectOutputStream oos = new ObjectOutputStream(buffout);
    oos.writeObject(this.list.getCurrentCharacterSet());
    oos.writeObject(this.fontSettings.getFontSettings());
    oos.flush();
    oos.close();
    
    this.list.getCurrentCharacterSet().setSaved();
  }

  /**
   * load an existing character set by deserializing it from a file
   * @param fromFile the file to read from
   * @throws FileNotFoundException in case parameter fromFile is erroneous
   * @throws IOException in case of IO Error
   */
  public void loadCharacterSet(String fromFile) throws FileNotFoundException, IOException
  {
    try {
      FileInputStream fis = new FileInputStream(new File(fromFile));
      BufferedInputStream buffin = new BufferedInputStream(fis);
      ObjectInputStream ois = new ObjectInputStream(buffin);
      CharacterSet charSet = (CharacterSet) ois.readObject();
      
      try {
          FontSettings settings = (FontSettings)ois.readObject();
          fontSettings.setFontSettings(settings);
      } catch (IOException ex1) {
          RSLogger.getLogger().log(Level.INFO, "file was saved with an older version...", ex1);
      }
      ois.close();

      charSet.validate();
      this.list.setCurrentCharacterSet(charSet);
    } catch (ClassNotFoundException cnfe)
    {
      //todo: show error message
      RSLogger.getLogger().log(Level.SEVERE, null, cnfe);
    }
  }
  
  /**
   * returns if the current character set was changed
   * @return true if character set not saved to a file
   */
  public boolean isChanged()
  {
    return this.list.getCurrentCharacterSet().isChanged();
  }
  
  /**
   * shows the dialog to save the project to a file
   */
  public void showSaveDialog()
  {
      this.operation = this.OPERATION_SAVE;
      this.fileController.addObserver(this);
      FileFilter filter = new FileNameExtensionFilter("CharMakerFile", "cmfnt", "*");
      this.fileController.setFile(this.fontSettings.getFontName() + ".cmfnt");
      this.fileController.showSaveDialog(filter);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == this.buttonNew || e.getSource() == this.itemNew)
    {
      this.operation = OPERATION_NEW;
      this.gridController.addObserver(this);
      this.gridController.showDialog(this.buttonNew.getLocationOnScreen());
    }
    else if (e.getSource() == this.buttonOpen_XML || e.getSource() == this.itemOpen_XML)
    {
      this.operation = OPERATION_OPEN_XML;
      this.fileController.addObserver(this);
      FileFilter filter = new FileNameExtensionFilter("XML Font", "fnt", "*");
      this.fileController.showOpenDialog(filter);
    }
    else if (e.getSource() == this.buttonWrite_CHeader || e.getSource() == this.itemWrite_CHeader)
    {
      this.operation = OPERATION_WRITE_HEADER;
      this.fileController.setFile(String.format("FONT_%s.h", this.fontSettings.getFontName()));
      this.fileController.addObserver(this);
      FileFilter filter = new FileNameExtensionFilter("C Header Files", "h");
      this.fileController.showSaveDialog(filter);
    }
    else if (e.getSource() == this.itemLoad) {
      this.operation = this.OPERATION_LOAD;
      this.fileController.addObserver(this);
      FileFilter filter = new FileNameExtensionFilter("CharMakerFile", "cmfnt", "*");
      this.fileController.showOpenDialog(filter);
    }
    else if (e.getSource() == this.itemSave) {
      this.showSaveDialog();
    }
  }    

  @Override
  public void update(Observable o, Object arg) {
    switch (this.operation) {
      case OPERATION_NEW: {
        if (this.gridController.getDialogReturn() == this.gridController.DIALOG_OK)
          {
          int x = this.gridController.getXDimension();
          int y = this.gridController.getYDimension();
          String name = view.getTextFieldFontName().getText();
          CharacterSet set = new CharacterSet(x, y, name);
          if (this.gridController.isCompleteCharacterSet())
          {
            for (char c = ' '; c <= '~'; c += 1)
            {
              try {
                set.addCharacter(c, new DataGrid(x, y));
              } catch (Exception ex) {
                RSLogger.getLogger().log(Level.WARNING, null, ex);
              }
            }
          }
          list.setCurrentCharacterSet(set);
        }
        this.gridController.deleteObserver(this);
      } break;
      
      case OPERATION_OPEN_XML: {
        this.fileController.deleteObserver(this);
        if (this.fileController.getApproveOption() == JFileChooser.APPROVE_OPTION)
        {
          File f = new File(this.fileController.getFile());
          BitmapReader.readBitmap(f, this.list);
        }        
      } break;
      
      case OPERATION_WRITE_HEADER: {
        this.fileController.deleteObserver(this);
        if (this.fileController.getApproveOption() == JFileChooser.APPROVE_OPTION)
        {
          this.headerWriter.writeOut(this.list.getCurrentCharacterSet(),
                                   this.fontSettings.getFontSettings(),
                                   this.fileController.getFile());
        }
      } break;
      
      case OPERATION_LOAD: {
        this.fileController.deleteObserver(this);
        if (this.fileController.getApproveOption() == JFileChooser.APPROVE_OPTION) {
          try {
            this.loadCharacterSet(fileController.getFile());
          } catch (IOException ex) {
            RSLogger.getLogger().log(Level.SEVERE, null, ex);
          }
        }
      }
      
      case OPERATION_SAVE: {
        this.fileController.deleteObserver(this);
        if (this.fileController.getApproveOption() == JFileChooser.APPROVE_OPTION) {
          try {
            this.saveCharacterSet(fileController.getFile());
          } catch (IOException ex) {
            RSLogger.getLogger().log(Level.SEVERE, null, ex);
          }
        }
      }
      default: break;
    }
    this.operation = NOOPERATION;
  }
}
