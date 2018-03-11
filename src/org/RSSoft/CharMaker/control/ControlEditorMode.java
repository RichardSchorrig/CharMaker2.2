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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.RSSoft.CharMaker.control.models.DrawActionButton;
import org.RSSoft.CharMaker.view.CharMakerWindow;
import org.RSSoft.CharMaker.view.PictureButton;

/**
 *
 * @author Richard
 */
public class ControlEditorMode implements ActionListener {
/*  
  private final DrawActionButton select;
  private final DrawActionButton straightLine;
  
  public ControlEditorMode(CharMakerWindow view) throws IOException
  {
    select = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/Select.png"));
    select.addActionListener(this);
    
    straightLine = new PictureButton(this.getClass().getClassLoader().getResourceAsStream("media/StraightLine.png"));
    straightLine.addActionListener(this);
    
    view.getPanelDrawSelect().add(straightLine);
    view.getPanelDrawSelect().add(select);
  }
*/
  @Override
  public void actionPerformed(ActionEvent e) {
    
  }
  
}
