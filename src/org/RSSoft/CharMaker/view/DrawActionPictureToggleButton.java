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
package org.RSSoft.CharMaker.view;

import java.io.IOException;
import java.io.InputStream;
import org.RSSoft.CharMaker.control.drawaction.DrawAction;
import org.RSSoft.CharMaker.control.models.DrawActionButton;

/**
 *
 * @author Richard
 */
public class DrawActionPictureToggleButton extends PictureButton implements DrawActionButton {
  
  private final DrawAction drawAction;
  
  public DrawActionPictureToggleButton(DrawAction action, InputStream is) throws IOException
  {
    super(is);
    drawAction = action;
  }

  @Override
  public DrawAction getDrawAction() {
    return drawAction;
  }
  
}
