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

import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.RSSoft.CharMaker.control.subsystem.VarBitdepthID;

/**
 * This class is an extention to the java swing class @link ComboBoxModel
 * The content is fixed, c99 datatypes can be selected:
 *  unsigned char
 *  -uint8_t
 *  -uint16_t
 *  -uint32_t
 *  -uint64_t
 * @author Richard
 */
public class ComboBoxDataTypes implements ComboBoxModel
{
  private ListDataListener listener;
  private final ArrayList<VarBitdepthID> dataTypes;
  private int selectedItem;
  
  /**
   * construct a new ComboBoxModel with c99 data types to be selected
   */
  public ComboBoxDataTypes()
  {
    dataTypes = new ArrayList<>();
    dataTypes.add(new VarBitdepthID("unsigned char", 8));
    dataTypes.add(new VarBitdepthID("uint8_t", 8));
    dataTypes.add(new VarBitdepthID("uint16_t", 16));
    dataTypes.add(new VarBitdepthID("uint32_t", 32));
    dataTypes.add(new VarBitdepthID("uint64_t", 64));
  }

  /**
   * set an item selected
   * @param anItem the item to be selected
   */
  @Override
  public void setSelectedItem(Object anItem)
  {
    if (anItem instanceof VarBitdepthID)
    {
      if (dataTypes.contains(anItem))
      {
        selectedItem = dataTypes.indexOf(anItem);
      }
    }
  }

  /**
   * returns the selected item
   * @return the item selected by the function setSelectedItem()
   */
  @Override
  public Object getSelectedItem()
  {
    return dataTypes.get(selectedItem);
  }
  
  /**
   * returns the bit depth of the selection (8, 16, 32, 64)
   * @return the bit depth of the selection
   */
  public int getSelectedBitDepth()
  {
    return dataTypes.get(selectedItem).getDepth();
  }
  
  /**
   * returns a datatype identifier (unsigned char, uint8_t, ...)
   * @return a datatype identifier string
   */
  public String getSelectedDatatypeName()
  {
    return dataTypes.get(selectedItem).getIdentifier();
  }

  /**
   * returns the number of the combobox content (always 5)
   * @return the size of the selection
   */
  @Override
  public int getSize()
  {
    return dataTypes.size();
  }
  
  /**
   * returns the index of the current selection
   * @return the index of the current selection
   */
  public int getIndexOfSelectedItem()
  {
    return selectedItem;
  }
  
  /**
   * updates all listeners
   */
  public void updateList()
  {
    if (listener != null)
    {
      listener.contentsChanged(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, dataTypes.size()-1));
    }
  }

  /**
   * returns the selected object (VarBitdepthID)
   * @param index the index to be returned
   * @return the selected object
   */
  @Override
  public Object getElementAt(int index)
  {
    return dataTypes.get(index);
  }

  /**
   * adds a data listener
   * @param l the listener to be added (only one at a time)
   */
  @Override
  public void addListDataListener(ListDataListener l)
  {
    this.listener = l;
  }

  /**
   * removes the data listener
   * @param l the listener to be removed (in fact, the parameter is not checked at all,
   * the function simpy sets the listener to null)
   */
  @Override
  public void removeListDataListener(ListDataListener l)
  {
    this.listener = null;
  }
}
