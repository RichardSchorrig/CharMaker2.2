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
package org.RSSoft.CharMaker.control.models;

import javax.swing.AbstractSpinnerModel;

/**
 * This class implements the abstract spinner model to a hexadecimal representation
 * of numbers
 * @author Richard
 */
public class SpinnerHexaModel extends AbstractSpinnerModel {
  
  private final int minVal;
  private final int maxVal;
  private int currentVal;
  private String currentValString;
  private final String FORMAT = "0x%02x";
  
  /**
   * construct a new hexa spinner model.
   * the values available range from minValue to maxValue
   * @param minVal the minimal value available
   * @param maxVal the maximal value available
   */
  public SpinnerHexaModel(int minVal, int maxVal)
  {
    this.minVal = minVal;
    this.maxVal = maxVal;
    this.currentVal = minVal;
    this.currentValString = String.format(FORMAT, currentVal);
  }
  
  /**
   * returns the current value
   * @return the current selected value
   */
  public int getDecimalValue()
  {
    return this.currentVal;
  }
  
  /**
   * set a value, value must be in the range of minValue and maxValue
   * @param val the value to show
   */
  public void setDecimalValue(int val)
  {
    if (val >= minVal && val <= maxVal)
      currentVal = val;
    
    this.currentValString = String.format(FORMAT, currentVal);
  }


  @Override
  public Object getValue() {
    return this.currentValString;
  }

  @Override
  public void setValue(Object value) {
    int val = 0;
    if (value instanceof String)
      val = Integer.decode((String)value);
    else if (value instanceof Integer)
      val = (Integer)value;
    
    if (val >= minVal && val <= maxVal)
      currentVal = val;
    
    this.currentValString = String.format(FORMAT, currentVal);

    this.fireStateChanged();
  }  

  @Override
  public Object getNextValue() {
    if (currentVal < maxVal)
    {
      currentVal += 1;
      this.currentValString = String.format(FORMAT, currentVal);
    }
    return currentValString;
  }

  @Override
  public Object getPreviousValue() {
    if (currentVal > minVal)
    {
      currentVal -= 1;
      this.currentValString = String.format(FORMAT, currentVal);
    }
    return currentValString;
  } 
}
