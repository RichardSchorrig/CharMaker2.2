/*
 * Copyright (C) 2020 Richard.
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
import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.RSSoft.CharMaker.control.models.FontSettings;
import org.RSSoft.CharMaker.view.CharMakerWindow;

/**
 *
 * @author Richard
 */
public class ScanDirectionControl implements ActionListener {
    
    private final static String DIRECTION_UP_DOWN = "up -> down";
    private final static String DIRECTION_DOWN_UP = "down -> up";
    
    private final static String DIRECTION_LEFT_RIGHT = "left -> right";
    private final static String DIRECTION_RIGHT_LEFT = "right -> left";
    
    private ActionListener listener;
    
    private static class ScanDirectionSelection implements ComboBoxModel {

        private ListDataListener listener;
        ArrayList<String> scanDirections;
        private int selectedItem;
        
        private void addHorizontalElements() {
            scanDirections.add(DIRECTION_LEFT_RIGHT);
            scanDirections.add(DIRECTION_RIGHT_LEFT);
        }
        
        private void addVerticalElements() {
            scanDirections.add(DIRECTION_UP_DOWN);
            scanDirections.add(DIRECTION_DOWN_UP);
        }

        /**
         * depending on argument scanDirection, only certain elemets are added
         * all previous items will be removed
         *
         * @param scanDirection set to 0 to have all options set to 1 to have
         * the vertical (up, down) options set to 2 to have the horizontal
         * (left, right) options
         */
        public void setScanDirectionSelection(int scanDirection) {
            scanDirections.clear();
            
            switch (scanDirection) {
                default:
                case 0: {
                    addVerticalElements();
                    addHorizontalElements();
                    break;
                }
                case 1: {
                    addVerticalElements();
                    break;
                }
                case 2: {
                    addHorizontalElements();
                    break;
                }
            }
            updateList();
        }

        /**
         * construct a combo box model with the followning elements: - scan
         * direction up -> down - scan direction down -> up - scan direction
         * left -> right - scan direction right -> left
         *
         * depending on argument scanDirection, only certain elemets are added
         *
         * @param scanDirection set to 0 to have all options set to 1 to have
         * the vertical (up, down) options set to 2 to have the horizontal
         * (left, right) options
         */
        public ScanDirectionSelection(int scanDirection) {
            listener = null;
            scanDirections = new ArrayList<>();
            selectedItem = 0;
            
            setScanDirectionSelection(scanDirection);
        }

        /**
         * set an item selected
         *
         * @param anItem the item to be selected
         */
        @Override
        public void setSelectedItem(Object anItem) {
            if (scanDirections.contains(anItem)) {
                selectedItem = scanDirections.indexOf(anItem);
            }
        }

        /**
         * returns the selected item
         *
         * @return the item selected by the function setSelectedItem()
         */
        @Override
        public Object getSelectedItem() {
            return scanDirections.get(selectedItem);
        }

        /**
         * returns the number of the combobox content (always 5)
         *
         * @return the size of the selection
         */
        @Override
        public int getSize() {
            return scanDirections.size();
        }

        /**
         * returns the index of the current selection
         *
         * @return the index of the current selection
         */
        public int getIndexOfSelectedItem() {
            return selectedItem;
        }

        /**
         * updates all listeners
         */
        public void updateList() {
            selectedItem = 0;
            if (listener != null) {
                listener.contentsChanged(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, 0, scanDirections.size() - 1));
            }
        }

        /**
         * returns the selected object (VarBitdepthID)
         *
         * @param index the index to be returned
         * @return the selected object
         */
        @Override
        public Object getElementAt(int index) {
            return scanDirections.get(index);
        }

        /**
         * adds a data listener
         *
         * @param l the listener to be added (only one at a time)
         */
        @Override
        public void addListDataListener(ListDataListener l) {
            this.listener = l;
        }

        /**
         * removes the data listener
         *
         * @param l the listener to be removed (in fact, the parameter is not
         * checked at all, the function simpy sets the listener to null)
         */
        @Override
        public void removeListDataListener(ListDataListener l) {
            this.listener = null;
        }
        
    }
    
    private final JComboBox scanDirection1;
    private final JComboBox scanDirection2;
    private final JCheckBox organizedInColumns;
    
    public ScanDirectionControl(CharMakerWindow window) {
        scanDirection1 = window.getComboBoxScanDirection1();
        scanDirection2 = window.getComboBoxScanDirection2();
        
        organizedInColumns = window.getCheckBoxOrganizeInColons();
        
        scanDirection1.setModel(new ScanDirectionSelection(0));
        scanDirection1.addActionListener(this);
        
        scanDirection2.setModel(new ScanDirectionSelection(2));
        scanDirection2.addActionListener(this);
        
        organizedInColumns.setText("Colunms");
        organizedInColumns.addActionListener(this);
    }
    
    /**
     * update the given font settings to the values of this controller.
     * @param fontSettings the font settings to update
     */
    public void updateFontSettings(FontSettings fontSettings)
    {
        fontSettings.scanDirection = getScanDirectionFromSelection();
        fontSettings.organizedInColunms = organizedInColumns.isSelected();
    }
    
    private boolean bitsSet(int value, int bits)
    {
        return (value & bits) == bits;
    }
    
    /**
     * update the view of this controller to match the font settings (which are not modified)
     * @param fontSettings 
     */
    public void setFontSettings(FontSettings fontSettings)
    {
        int scanDirection = fontSettings.scanDirection;
        boolean verticalWins = false;
        if (bitsSet(scanDirection, FontSettings.SCANDIRECTION_HORIZONTAL_OVER_VERTICAL)) {
            verticalWins = false;
            ((ScanDirectionSelection)scanDirection2.getModel()).setScanDirectionSelection(2);
        } else {
            verticalWins = true;
            ((ScanDirectionSelection)scanDirection2.getModel()).setScanDirectionSelection(1);            
        }
        
        switch (scanDirection & 0x30) {
            default:
            case FontSettings.SCANDIRECTION_LEFT_RIGHT: {
                if (verticalWins) {
                    scanDirection1.getModel().setSelectedItem(DIRECTION_LEFT_RIGHT);
                } else {
                    scanDirection2.getModel().setSelectedItem(DIRECTION_LEFT_RIGHT);
                }
                break;
            }
            case FontSettings.SCANDIRECTION_RIGHT_LEFT: {
                if (verticalWins) {
                    scanDirection1.getModel().setSelectedItem(DIRECTION_RIGHT_LEFT);
                } else {
                    scanDirection2.getModel().setSelectedItem(DIRECTION_RIGHT_LEFT);
                }
                break;
            }
        }
        
        switch (scanDirection & 0x3) {
            default:
            case FontSettings.SCANDIRECTION_UP_DOWN: {
                if (verticalWins) {
                    scanDirection2.getModel().setSelectedItem(DIRECTION_UP_DOWN);
                } else {
                    scanDirection1.getModel().setSelectedItem(DIRECTION_UP_DOWN);
                }
                break;
            }
            case FontSettings.SCANDIRECTION_DOWN_UP: {
                if (verticalWins) {
                    scanDirection2.getModel().setSelectedItem(DIRECTION_DOWN_UP);
                } else {
                    scanDirection1.getModel().setSelectedItem(DIRECTION_DOWN_UP);
                }
                break;
            }
        }
    }
    
    public int getScanDirectionFromSelection()
    {
        int direction = 0;
        if (scanDirection1.getModel().getSelectedItem().equals(DIRECTION_UP_DOWN)) {
            direction |= FontSettings.SCANDIRECTION_HORIZONTAL_OVER_VERTICAL | FontSettings.SCANDIRECTION_UP_DOWN;
        } else if (scanDirection1.getModel().getSelectedItem().equals(DIRECTION_DOWN_UP)) {
            direction |= FontSettings.SCANDIRECTION_HORIZONTAL_OVER_VERTICAL | FontSettings.SCANDIRECTION_DOWN_UP;
        } else if (scanDirection1.getModel().getSelectedItem().equals(DIRECTION_LEFT_RIGHT)) {
            direction |= FontSettings.SCANDIRECTION_VERTICAL_OVER_HORIZONTAL | FontSettings.SCANDIRECTION_LEFT_RIGHT;
        } else if (scanDirection1.getModel().getSelectedItem().equals(DIRECTION_RIGHT_LEFT)) {
            direction |= FontSettings.SCANDIRECTION_VERTICAL_OVER_HORIZONTAL | FontSettings.SCANDIRECTION_RIGHT_LEFT;
        }
        
        if (scanDirection2.getModel().getSelectedItem().equals(DIRECTION_UP_DOWN)) {
            direction |= FontSettings.SCANDIRECTION_UP_DOWN;
        } else if (scanDirection2.getModel().getSelectedItem().equals(DIRECTION_DOWN_UP)) {
            direction |= FontSettings.SCANDIRECTION_DOWN_UP;
        } else if (scanDirection2.getModel().getSelectedItem().equals(DIRECTION_LEFT_RIGHT)) {
            direction |= FontSettings.SCANDIRECTION_LEFT_RIGHT;
        } else if (scanDirection2.getModel().getSelectedItem().equals(DIRECTION_RIGHT_LEFT)) {
            direction |= FontSettings.SCANDIRECTION_RIGHT_LEFT;
        }
        
        return direction;
    }
    
    public boolean isOrganizedInColunms()
    {
        return organizedInColumns.isSelected();
    }
    
    void addActionListener(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(scanDirection1))
        {
            if (scanDirection1.getModel().getSelectedItem().equals(DIRECTION_UP_DOWN)
                    || scanDirection1.getModel().getSelectedItem().equals(DIRECTION_DOWN_UP)) {
                ((ScanDirectionSelection)scanDirection2.getModel()).setScanDirectionSelection(2);
            } else {
                ((ScanDirectionSelection)scanDirection2.getModel()).setScanDirectionSelection(1);
            }
        }
        
        if (null != listener)
        {
            listener.actionPerformed(new ActionEvent(this, 0, ""));
        }
        
    }
    
}
