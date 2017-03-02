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
 * This class implements the abstract spinner model to a decimal representation
 * of numbers
 * @author richard
 */
public class SpinnerDecimalModel extends AbstractSpinnerModel {
  
  private int minVal;
  private int maxVal;
  private int currentVal;
  
  /**
   * construct a new decimal spinner model.
   * the values available range from minValue to maxValue
   * @param minValue the minimal value available
   * @param maxValue the maximal value available
   */
  public SpinnerDecimalModel(int minValue, int maxValue)
  {    
    this.maxVal = maxValue;
    this.minVal = minValue;
    this.currentVal = minValue;
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
    
    this.fireStateChanged();
  }

  @Override
  public Object getValue() {
    return this.currentVal;
  }

  @Override
  public void setValue(Object value) {
    int val = (Integer) value;
    if (val >= minVal && val <= maxVal)
      currentVal = val;
    
    this.fireStateChanged();
  }

  @Override
  public Object getNextValue() {
    if (currentVal < maxVal)
    {
      currentVal += 1;
    }
    return currentVal;
  }

  @Override
  public Object getPreviousValue() {
    if (currentVal > minVal)
    {
      currentVal -= 1;
    }
    return currentVal;
  }  
}
