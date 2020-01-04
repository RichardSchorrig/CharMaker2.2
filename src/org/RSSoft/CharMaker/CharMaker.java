/*
 * Copyright (C) 2017 Richard Schorrig.
 * richard.schorrig@web.de
 *
 * This application is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package org.RSSoft.CharMaker;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.RSSoft.CharMaker.control.ControlCharacterSet;
import org.RSSoft.CharMaker.control.ControlEditorMode;
import org.RSSoft.CharMaker.control.ControlExit;
import org.RSSoft.CharMaker.control.ControlFileIO;
import org.RSSoft.CharMaker.control.ControlFontSettings;
import org.RSSoft.CharMaker.control.ControlGrid;
import org.RSSoft.CharMaker.control.ControlGridTransform;
import org.RSSoft.CharMaker.control.ControlNewOpenWriteCharset;
import org.RSSoft.CharMaker.control.ControlPreview;
import org.RSSoft.CharMaker.control.ScanDirectionControl;
import org.RSSoft.CharMaker.util.RSLogger;
import org.RSSoft.CharMaker.util.SavedSettings;
import org.RSSoft.CharMaker.view.CharMakerWindow;

/**
 * an application to design pixel fonts and exporting them as c header files
 * @author richard
 */
public class CharMaker {
  
  private ControlGrid gridController;
  
  /**
   * runs the application CharMaker
   */
  public CharMaker()
  {
    try {
      
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      
      CharMakerWindow window = new CharMakerWindow();
      SavedSettings settings = new SavedSettings("settings");
      gridController = new ControlGrid(window);
      gridController.setLabels();
      
      ControlGridTransform transformController = new ControlGridTransform(window, gridController);
      transformController.setLabels();
      transformController.setActive(false);
      
      ControlEditorMode modeController = new ControlEditorMode(window, gridController.getGridPane());
      modeController.setLabels();
      
      gridController.getGridPane().addTransformControl(transformController);
      
      ControlFileIO fileIO = new ControlFileIO(window);
      fileIO.setDefaults(settings);
      
      ControlCharacterSet charListController = new ControlCharacterSet(window, gridController);
      charListController.setLabels();
      
      //ControlFileOperation fileController = new ControlFileOperation(window, settings);
      //fileController.addCharacterListController(charListController);
      ScanDirectionControl scanDirectionControl = new ScanDirectionControl(window);
      
      ControlFontSettings fontController = new ControlFontSettings(window, scanDirectionControl);
      fontController.setLabels();
      //outputController.setObservable(charListController).setObservable(fileController);
      
      ControlNewOpenWriteCharset charsetController = new ControlNewOpenWriteCharset(window, charListController, gridController, fileIO, fontController);
      charsetController.setLabels();
      
      ControlExit exitController = new ControlExit(charsetController, window);
      exitController.setLabels();
      
      ControlPreview preview = new ControlPreview(window, charListController);
      preview.setLabels();
      charListController.setPreviewController(preview);
      
//      ControlSaveLoadCharacterSet io = new ControlSaveLoadCharacterSet(window, charListController, fileIO);
//      io.setLabels();
      
      window.setVisible(true);
      
    } catch (IOException ex) {
      RSLogger.getLogger().log(Level.SEVERE, null, ex);
    } catch (ClassNotFoundException ex) {
      RSLogger.getLogger().log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      RSLogger.getLogger().log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      RSLogger.getLogger().log(Level.SEVERE, null, ex);
    } catch (UnsupportedLookAndFeelException ex) {
      RSLogger.getLogger().log(Level.SEVERE, null, ex);
    }
    
  }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new CharMaker();
    }
    
}
