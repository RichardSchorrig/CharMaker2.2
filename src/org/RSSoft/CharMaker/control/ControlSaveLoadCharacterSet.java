/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import org.RSSoft.CharMaker.core.character.CharacterSet;
import org.RSSoft.CharMaker.util.RSLogger;
import org.RSSoft.CharMaker.view.CharMakerWindow;

/**
 *
 * @author Richard
 */
@Deprecated
public class ControlSaveLoadCharacterSet implements ActionListener, Observer {
  
  private int operation;
  
  private final int OPERATION_LOAD = 1;
  private final int OPERATION_SAVE = 2;
  private final int NOPERATION = 0;
  
  private final JMenuItem itemSave;
  private final JMenuItem itemLoad;
  
  private final ControlCharacterSet characterSetController;
  private final ControlFileIO fileIO;
  
  public ControlSaveLoadCharacterSet(CharMakerWindow view, ControlCharacterSet characterSetController, ControlFileIO fileIO)
  {
    this.operation = 0;
    
    this.itemLoad = view.getMenuItemLoadCharacterSet();
    this.itemSave = view.getMenuItemSaveCharacterSet();
    
    this.itemLoad.addActionListener(this);
    this.itemSave.addActionListener(this);
    
    this.characterSetController = characterSetController;
    this.fileIO = fileIO;
  }
  
  public void setLabels()
  {
    this.itemLoad.setText("Load Character Set");
    this.itemSave.setText("Save Character Set");
  }
  
  public void saveCharacterSet(String toFile) throws FileNotFoundException, IOException
  {
    FileOutputStream fos = new FileOutputStream(new File(toFile));
    BufferedOutputStream buffout = new BufferedOutputStream(fos);
    ObjectOutputStream oos = new ObjectOutputStream(buffout);
    oos.writeObject(this.characterSetController.getCurrentCharacterSet());
    oos.flush();
    oos.close();
  }
  
  public void loadCharacterSet(String fromFile) throws FileNotFoundException, IOException
  {
    try {
      FileInputStream fis = new FileInputStream(new File(fromFile));
      BufferedInputStream buffin = new BufferedInputStream(fis);
      ObjectInputStream ois = new ObjectInputStream(buffin);
      CharacterSet charSet = (CharacterSet) ois.readObject();
      ois.close();
      
      charSet.validate();
      this.characterSetController.setCurrentCharacterSet(charSet);
    } catch (ClassNotFoundException cnfe)
    {
      //todo: show error message
      RSLogger.getLogger().log(Level.SEVERE, null, cnfe);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.fileIO.addObserver(this);
    if (e.getSource() == this.itemLoad)
    {
      this.operation = OPERATION_LOAD;
      this.fileIO.showOpenDialog(null);
    }
    else if (e.getSource() == this.itemSave)
    {
      this.operation = OPERATION_SAVE;
      this.fileIO.showSaveDialog(null);
    }
  }

  @Override
  public void update(Observable o, Object arg) {
    if (this.fileIO.getApproveOption() == JFileChooser.APPROVE_OPTION)
    { 
      try {
        switch (this.operation) {
          case OPERATION_LOAD: {
            this.loadCharacterSet(this.fileIO.getFile());
            this.operation = NOPERATION;
            break;
          }
          case OPERATION_SAVE: {
            this.saveCharacterSet(this.fileIO.getFile());
            this.operation = NOPERATION;
            break;
          }
          default: break;
        }
      }
      catch (Exception ex)
      {
        RSLogger.getLogger().log(Level.SEVERE, null, ex);
      }
    }
  }
  
}
