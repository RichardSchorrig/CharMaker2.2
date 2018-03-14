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
import org.RSSoft.CharMaker.control.drawaction.DrawActionSelect;
import org.RSSoft.CharMaker.control.drawaction.DrawActionStraightLine;
import org.RSSoft.CharMaker.control.models.DrawActionButton;
import org.RSSoft.CharMaker.view.CharMakerWindow;
import org.RSSoft.CharMaker.view.DrawActionPictuerToggleButton;
import org.RSSoft.CharMaker.view.GridPane;

/**
 *
 * @author Richard
 */
public class ControlEditorMode implements ActionListener {

  private final DrawActionPictuerToggleButton select;
  private final DrawActionPictuerToggleButton straightLine;
  
  private final GridPane gridPane;
  
  public ControlEditorMode(CharMakerWindow view, GridPane pane) throws IOException
  {
    select = new DrawActionPictuerToggleButton(new DrawActionSelect(), this.getClass().getClassLoader().getResourceAsStream("media/Select.png"));
    select.addActionListener(this);
    
    straightLine = new DrawActionPictuerToggleButton(new DrawActionStraightLine(), this.getClass().getClassLoader().getResourceAsStream("media/StraightLine.png"));
    straightLine.addActionListener(this);
    
    view.getPanelDrawSelect().add(straightLine);
    view.getPanelDrawSelect().add(select);
    gridPane = pane;
  }
  
  public void setLabels()
  {
    select.set("Select");
    straightLine.set("Straight Line");
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    gridPane.setDrawAction( ((DrawActionPictuerToggleButton)e.getSource()).getDrawAction() );
    if (e.getSource().equals(select))
    {
      gridPane.setSelectMode(true);
    }
    else
    {
      gridPane.setSelectMode(false);
    }
  }
  
}
