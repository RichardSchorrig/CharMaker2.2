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
import java.util.logging.Level;
import org.RSSoft.CharMaker.core.character.CharacterDescriptor;
import org.RSSoft.CharMaker.util.RSLogger;
import org.RSSoft.CharMaker.view.CharMakerWindow;
import org.RSSoft.CharMaker.view.GridPane;

/**
 *
 * @author Richard
 */
@Deprecated
public class ControlSetCharacter implements ActionListener
{
  private CharMakerWindow view;
  private ControlCharacterSet listController;
  private GridPane grid;
  
  public ControlSetCharacter(CharMakerWindow view, ControlCharacterSet listController, GridPane grid)
  {
    this.view = view;
    this.listController = listController;
    this.grid = grid;
    view.getButtonSetChar().addActionListener(this);
    view.getButtonSetChar().setText("Set Character");
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    try {
      CharacterDescriptor charDesc = listController.getSelectedCharacterDescriptor();
      RSLogger.getLogger().log(Level.INFO, "selected character: " + charDesc.getDescriptor());
      charDesc.setGrid(grid.getGrid());
    } catch (Exception ex) {
      RSLogger.getLogger().log(Level.WARNING, null, ex);
    }
  }
}
