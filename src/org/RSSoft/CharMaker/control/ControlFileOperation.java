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
import java.io.File;
import java.util.Observable;
import javax.swing.JFileChooser;
import org.RSSoft.CharMaker.core.BitmapReader;
import org.RSSoft.CharMaker.util.SavedSettings;
import org.RSSoft.CharMaker.view.CharMakerWindow;

/**
 *
 * @author Richard
 */
@Deprecated
public class ControlFileOperation extends Observable implements ActionListener
{
  private final CharMakerWindow view;
  
 // private final JButton buttonSave;
 // private final JButton buttonNew;
  
  private ControlCharacterSet characterListController;
  private File folderPath;
  private final SavedSettings settings;
//  private BitmapReader reader;
  public ControlFileOperation(CharMakerWindow view, SavedSettings settings)
  {
    this.view = view;
    view.getButtonOpen().addActionListener(this);
    this.settings = settings;
    this.folderPath = new File(settings.getProperty("PATH"));
  }
  
  public void addCharacterListController(ControlCharacterSet characterListController)
  {
    this.characterListController = characterListController;
  }
  
  private void setFolderPath(String path)
  {
    this.folderPath = new File(path);
  }
  
  public File getFolderPath()
  {
    return folderPath;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
//    view.getFileChooser().setCurrentDirectory(folderPath);
//    view.getFileChooser().setDialogType(JFileChooser.OPEN_DIALOG);
//    view.getFileChooser().setVisible(true);
    
    System.out.println(folderPath.getAbsolutePath());
    view.getjFileChooser1().setCurrentDirectory(folderPath);
    int choice = view.getjFileChooser1().showOpenDialog(view);

    if (choice == JFileChooser.APPROVE_OPTION)
    {
      File file = view.getjFileChooser1().getSelectedFile();
      if (characterListController != null)
      {
        BitmapReader.readBitmap(file, characterListController);
      }
      else
      {
        BitmapReader.readBitmap(file, null);
      }
      
      folderPath = file.getParentFile();
      this.settings.setProperty("FILE", folderPath.getAbsolutePath());
      this.setChanged();
      this.notifyObservers();
    }
  }
}
