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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Point;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.RSSoft.CharMaker.control.dialog.DialogShowBaseClass;
import org.RSSoft.CharMaker.control.models.SpinnerDecimalModel;
import org.RSSoft.CharMaker.view.CharMakerWindow;
import org.RSSoft.CharMaker.view.GridPane;

/**
 * This class controlls the grid's settings (size) and grid user interaction
 * @author Richard
 */
public class ControlGrid extends DialogShowBaseClass implements ActionListener, MouseListener, MouseMotionListener, ChangeListener
{
  private int gridXNumber;
  private int gridYNumber;
  private final GridPane grid;
  private boolean varColumnNumber;
  
  private final JSpinner spinnerColumns;
  private final JSpinner spinnerRows;
  private final SpinnerDecimalModel spinnerModelColumns;
  private final SpinnerDecimalModel spinnerModelRows;
  private final JCheckBox checkBoxVariableColumns;
  
  private final JRadioButton radioButtonAllCharacters;
  private final JRadioButton radioButtonNoCharacters;
  
  private final JButton buttonSetGridOK;
  private final JButton buttonSetGridCancel;
  
  private final JLabel labelColumns;
  private final JLabel labelRows;
  
  private Point rightClickPoint;
  
  /**
   * Construct a new controller for the CharMaker's grid
   * uses the class "CharMakerWindow" to extract the user interface
   * @param view 
   */
  public ControlGrid(CharMakerWindow view)
  {    
    super(view.getDialogNewCharSet());
    
    gridXNumber = 8;
    gridYNumber = 8;
    this.varColumnNumber = false;

    grid = new GridPane();
    grid.setGrid(gridXNumber, gridYNumber);
    view.getPanelEditor().add(grid);
    grid.setVisible(true);
    
    this.buttonSetGridOK = view.getDialogButtonGridOK();
    this.buttonSetGridOK.addActionListener(this);
    this.buttonSetGridCancel = view.getDialogButtonGridCancel();
    this.buttonSetGridCancel.addActionListener(this);
    
    this.radioButtonAllCharacters = view.getDialogNewRadioButtonAllCharacter();
    this.radioButtonNoCharacters = view.getDialogNewRadioButtonNoCharacter();
    this.radioButtonAllCharacters.addActionListener(this);
    this.radioButtonNoCharacters.addActionListener(this);
    
    this.spinnerModelColumns = new SpinnerDecimalModel(5, 32);
    this.spinnerModelRows = new SpinnerDecimalModel(5, 32);
    this.spinnerColumns = view.getSpinnerColumns();
    this.spinnerRows = view.getSpinnerRows();
    this.spinnerColumns.setModel(spinnerModelColumns);
    this.spinnerRows.setModel(spinnerModelRows);
    
    this.spinnerColumns.setValue(gridXNumber);
    this.spinnerRows.setValue(gridYNumber);
    
    this.spinnerColumns.addChangeListener(this);
    this.spinnerRows.addChangeListener(this);
    
    this.checkBoxVariableColumns = view.getCheckBoxColumns();
    this.checkBoxVariableColumns.addActionListener(this);
    this.checkBoxVariableColumns.setSelected(varColumnNumber);
    
    grid.addMouseListener(this);
    grid.addMouseMotionListener(this);
    
    this.labelColumns = view.getLabelColumns();
    this.labelRows = view.getLabelRows();
    
    this.setAllCharacters(false);
  }
  
  @Override
  public void showDialog()
  {
    this.setDimensions(this.gridXNumber, this.gridYNumber);
    super.showDialog();
  }
  
  @Override
  public void showDialog(Point p)
  {
    this.setDimensions(this.gridXNumber, this.gridYNumber);
    super.showDialog(p);
  }
  
  /**
   * set the labels of the user interface items
   * @todo: parameter labelcontainer for localization
   */
  public void setLabels()
  {
    this.labelColumns.setText("Columns");
    this.labelRows.setText("Rows");
    this.buttonSetGridOK.setText("Create Character Set");
    this.buttonSetGridCancel.setText("Cancel");
    this.checkBoxVariableColumns.setText("Variable Number of Columns");
    this.radioButtonAllCharacters.setText("Complete Character Set");
    this.radioButtonNoCharacters.setText("Empty Character Set");
  }
  
  /**
   * set the dimensions of the displayed grid
   * @param xSize the width of the grid in fields
   * @param ySize the height of the grid in fields
   */
  public void setDimensions(int xSize, int ySize)
  {
    this.gridXNumber = xSize;
    this.gridYNumber = ySize;
    
    if (xSize == 0)
      checkBoxVariableColumns.setSelected(true);
    else
    {
      checkBoxVariableColumns.setSelected(false);
      this.spinnerColumns.setValue(xSize);
    }
    
    this.spinnerRows.setValue(ySize);
    
    this.grid.setGrid(xSize, ySize);
    this.grid.repaint();
  }
  
  /**
   * returns the x size of the grid.
   * In case there are variable columns, 0 is returned (which is not the actual
   * size of the grid)
   * @return the x size of the grid or 0 if variable columns
   */
  public int getXDimension()
  {
    if (this.varColumnNumber)
      return 0;
    else
      return this.gridXNumber;
  }
  
  /**
   * returns the y size of the grid
   * @return the y size of the grid
   */
  public int getYDimension()
  {
    return this.gridYNumber;
  }
  
  /**
   * returns wether or not this is variable columns
   * @return true if column number is variable, false if column number is fixed
   */
  public boolean isVariableColumnNumber()
  {
    return this.varColumnNumber;
  }
  
  /**
   * returns wether or not the radio button for the complete ascii character
   * list is selected
   * @return true if all ascii values are in the list, false if list is empty
   */
  public boolean isCompleteCharacterSet()
  {
    return this.radioButtonAllCharacters.isSelected();
  }
  
  /**
   * sets the radio buttons to show the new state:
   * - complete character set (allCharacters true)
   * - empty character set (allCharacters false)
   * @param allCharacters the new state of the radio buttons
   */
  public void setAllCharacters(boolean allCharacters)
  {
    if (allCharacters)
    {
      this.radioButtonAllCharacters.setSelected(true);
      this.radioButtonNoCharacters.setSelected(false);
    }
    else
    {
      this.radioButtonAllCharacters.setSelected(false);
      this.radioButtonNoCharacters.setSelected(true);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == this.buttonSetGridCancel)
    {
      this.closeDialog(this.DIALOG_CANCEL);
    }
    else if (e.getSource() == this.buttonSetGridOK)
    {
      this.varColumnNumber = this.checkBoxVariableColumns.isSelected();
      
      if (varColumnNumber)
        this.gridXNumber = 0;
      else
        this.gridXNumber = this.spinnerModelColumns.getDecimalValue();
      
      this.gridYNumber = this.spinnerModelRows.getDecimalValue();
      grid.setGrid(gridXNumber, gridYNumber);
      this.grid.repaint();
      this.closeDialog(this.DIALOG_OK);
    }
    else if (e.getSource() == this.checkBoxVariableColumns)
    {
      this.spinnerColumns.setEnabled(!this.checkBoxVariableColumns.isSelected());
    }
    else if (e.getSource() == this.radioButtonAllCharacters)
    {
      this.radioButtonNoCharacters.setSelected(false);
    }
    else if (e.getSource() == this.radioButtonNoCharacters)
    {
      this.radioButtonAllCharacters.setSelected(false);
    }

  }
  
  /**
   * returns the Grid Pane
   * @return the grid pane
   */
  public GridPane getGridPane()
  {
    return this.grid;
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {
//    grid.fillOneGrid(e.getPoint());
  }

  @Override
  public void mousePressed(MouseEvent e)
  {
    if (e.getButton() == MouseEvent.BUTTON1)
    {
      grid.fillOneGrid(e.getPoint());
    }
    else if (e.getButton() == MouseEvent.BUTTON3) {
      this.rightClickPoint = e.getPoint();
    }
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    if (this.rightClickPoint != null)
    {
      grid.fillLine(rightClickPoint, e.getPoint());
    }
    this.rightClickPoint = null;
  }

  @Override
  public void mouseEntered(MouseEvent e)
  {
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
  }

  @Override
  public void stateChanged(ChangeEvent e) {
  /*
    if (e.getSource() == this.spinnerColumns)
    {
      this.gridXNumber = (int) this.spinnerColumns.getValue();
    }
    else if (e.getSource() == this.spinnerRows)
      this.gridYNumber = (int) this.spinnerRows.getValue();
  */
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (this.rightClickPoint != null) {
      grid.previewFillLine(rightClickPoint, e.getPoint());
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
  }
}
