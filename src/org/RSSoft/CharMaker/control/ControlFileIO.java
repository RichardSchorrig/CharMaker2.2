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

import java.io.File;
import java.util.Observable;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.RSSoft.CharMaker.util.SavedSettings;
import org.RSSoft.CharMaker.view.CharMakerWindow;

/**
 * this class controlls a JFileChooser to open / save files
 * @author Richard
 */
public class ControlFileIO extends Observable {
  
  private final JFileChooser fileChooser;
  private final CharMakerWindow view;
  private String currentPath;
  private String currentFile;
  private int approve;
  
  private SavedSettings settings;
  
  /**
   * construct a controller for the file chooser dialog
   * @param view 
   */
  public ControlFileIO(CharMakerWindow view)
  {
    this.fileChooser = view.getjFileChooser1();
    this.view = view;
    this.settings = null;
  }
  
  /**
   * set the path for the file chooser
   * @param settings 
   */
  public void setDefaults(SavedSettings settings)
  {
    this.currentPath = settings.getProperty("PATH");
    this.settings = settings;
  }
  
  /**
   * set the path for the file chooser
   * @param path the path for the file chooser
   */
  public void setPath(String path)
  {
    this.currentPath = path;
    this.currentFile = "";
  }
  
  /**
   * returns the path set by the file chooser
   * @return the path set by the file chooser
   */
  public String getPath()
  {
    return this.currentPath;
  }
  
  /**
   * returns the approve option set by the file chooser
   * @return the approve option set by the file chooser
   */
  public int getApproveOption()
  {
    return this.approve;
  }
  
  /**
   * sets a new file in the file chooser
   * @param file the filename of a new file
   */
  public void setFile(String file)
  {
    //this.currentPath = this.currentPath.concat(File.separator).concat(file);
    this.currentFile = this.currentPath.concat(File.separator).concat(file);
    this.fileChooser.setCurrentDirectory(new File(this.currentFile));
  }
  
  /**
   * returns the selected file by the file chooser
   * @return the selected file by the file chooser
   */
  public String getFile()
  {
    return this.currentFile;
  }
  
  /**
   * shows a JFileChooser with the open option
   * @param filter the FileFilter to be added to the file chooser, can be null
   */
  public void showOpenDialog(FileFilter filter)
  {
    this.fileChooser.setCurrentDirectory(new File(this.currentPath));
    if (filter != null)
    {
      this.fileChooser.resetChoosableFileFilters();
      this.fileChooser.setFileFilter(filter);
      this.fileChooser.setAcceptAllFileFilterUsed(true);
    }
    this.approve = this.fileChooser.showOpenDialog(view);
    
    this.handleFileDialog();
  }
  
  /**
   * shows a JFileChooser with the save option
   * @param filter the FileFilter to be added to the file chooser, can be null
   */
  public void showSaveDialog(FileFilter filter)
  {
    this.fileChooser.setCurrentDirectory(new File(this.currentPath));
    this.fileChooser.setSelectedFile(new File(this.currentFile));
    if (filter != null)
    {
      this.fileChooser.resetChoosableFileFilters();
      this.fileChooser.setFileFilter(filter);
      this.fileChooser.setAcceptAllFileFilterUsed(true);
    }
    this.approve = this.fileChooser.showSaveDialog(view);
    
    this.handleFileDialog();
  }
  
  private void handleFileDialog()
  {
    if (approve == JFileChooser.APPROVE_OPTION)
    {
      this.currentFile = fileChooser.getSelectedFile().getAbsolutePath();
      this.currentPath = fileChooser.getSelectedFile().getParent();
      if (this.settings != null) {
        this.settings.setProperty("PATH", this.currentPath);
        this.settings.storeToFile();
      }
    }
    this.setChanged();
    this.notifyObservers();
  }
  
}
