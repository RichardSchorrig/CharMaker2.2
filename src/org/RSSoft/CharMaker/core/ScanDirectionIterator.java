/*
 * Copyright (C) 2020 Richard Schorrig.
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
package org.RSSoft.CharMaker.core;

import org.RSSoft.CharMaker.control.models.FontSettings;

/**
 * This class provides values for iterating through the datagrid for a given scan direction.
 * Usage: 
 * use two nested for(;;) loops: the outer loop and the inner loop. This class provides
 * functions for each the outer and the inner loop: outer_init(), outer_iterate() and outer_condition(),
 * so the for-loops looks like this:
 * ...
 * for (outer_init(); outer_condition(); outer_iterate()) {
 *   for (inner_init(); inner_condition(); inner_iterate() {
 *     ...
 *   }
 * }
 *
 * @author Richard
 */
public class ScanDirectionIterator {

    public int xBegin;
    public int xEnd;
    public int xDirection;
    public int x;

    public int yBegin;
    public int yEnd;
    public int yDirection;
    public int y;

    private int horizontalOverVertical;

    /**
     * initialize the grid iterator
     *
     * @param grid the grid to iterate through, used for size information
     * @param scanDirection the scan direction constists of three values:
     * - At offset 16, the value defines if horizontal lines are scanned first over
     *   vertical columns
     * - At offset 8, the direction of the vertical direction is given (left, right)
     * - At offset 0, the direction of the horizontal direction is given (up, down)
     * @throws Exception
     */
    public ScanDirectionIterator(DataGrid grid, int scanDirection) throws Exception {
        init(grid, scanDirection, null);
    }

    public ScanDirectionIterator(DataGrid grid, int scanDirection, GridArea offset) throws Exception {
        init(grid, scanDirection, offset);
    }

    private void init(DataGrid grid, int scanDirection, GridArea offset) throws Exception {
        horizontalOverVertical = (scanDirection & 0x300);

        switch (scanDirection & 0x30) {
            case FontSettings.SCANDIRECTION_RIGHT_LEFT: {
                xBegin = grid.getXSize() - 1;
                xEnd = 0;
                xDirection = -1;
                x = xBegin;
            }
            break;
            default:
            case FontSettings.SCANDIRECTION_LEFT_RIGHT: {
                xBegin = 0;
                xEnd = grid.getXSize();
                xDirection = 1;
                x = xBegin;
            }
            break;
        }

        switch (scanDirection & 0x3) {
            case FontSettings.SCANDIRECTION_DOWN_UP: {
                yBegin = grid.getYSize() - 1;
                yEnd = 0;
                yDirection = -1;
                y = yBegin;
            }
            break;
            default:
            case FontSettings.SCANDIRECTION_UP_DOWN: {
                yBegin = 0;
                yEnd = grid.getYSize();
                yDirection = 1;
                y = yBegin;
            }
            break;
        }

        if (offset != null) {
            if (offset.xOffset != -1) {
                xBegin = xBegin < xEnd ? xBegin + offset.xOffset : offset.xEnd > xBegin ? xBegin : offset.xEnd;
                xEnd = xEnd > xBegin ? offset.xEnd > xEnd ? xEnd : offset.xEnd : xEnd + offset.xOffset;
            }

            if (offset.yOffset != -1) {
                yBegin = yBegin < yEnd ? yBegin + offset.yOffset : offset.yEnd > yBegin ? yBegin : offset.yEnd;
                yEnd = yEnd > yBegin ? offset.yEnd > yEnd ? yEnd : offset.yEnd : yEnd + offset.yOffset;
            }
        }
    }
    
    public void outer_init() {
        if (horizontalOverVertical == FontSettings.SCANDIRECTION_HORIZONTAL_OVER_VERTICAL) {
            this.y = yBegin;
        } else {
            this.x = xBegin;
        }        
    }
    
    public void inner_init() {
        if (horizontalOverVertical == FontSettings.SCANDIRECTION_HORIZONTAL_OVER_VERTICAL) {
            this.x = xBegin;
        } else {
            this.y = yBegin;
        }
    }
    
    public void outer_iterate() {
        if (horizontalOverVertical == FontSettings.SCANDIRECTION_HORIZONTAL_OVER_VERTICAL) {
            this.y += this.yDirection;
        } else {
            this.x += this.xDirection;
        }
    }
    
    public void inner_iterate() {
        if (horizontalOverVertical == FontSettings.SCANDIRECTION_HORIZONTAL_OVER_VERTICAL) {
            this.x += this.xDirection;            
        } else {
            this.y += this.yDirection;
        }
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }

    @Deprecated
    public int getOuterIterator() {
        if (horizontalOverVertical == FontSettings.SCANDIRECTION_HORIZONTAL_OVER_VERTICAL) {
            return this.y;
        } else {
            return this.x;
        }
    }

    @Deprecated
    public int getInnerIterator() {
        if (horizontalOverVertical == FontSettings.SCANDIRECTION_HORIZONTAL_OVER_VERTICAL) {
            return this.x;
        } else {
            return this.y;
        }
    }
    
    private boolean x_condition() {
        boolean retVal = false;
        if (this.xDirection == -1) {
            if (this.x >= this.xEnd) {
                retVal = true;
            }
        } else // if (this.xDirection == 1)
        {
            if (this.x < this.xEnd) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    private boolean y_condition() {
        boolean retVal = false;
        if (this.yDirection == -1) {
            if (this.y >= this.yEnd) {
                retVal = true;
            }
        } else // if (this.xDirection == 1)
        {
            if (this.y < this.yEnd) {
                retVal = true;
            }
        }
        return retVal;
    }

    public boolean outer_codition() {
        if (horizontalOverVertical == FontSettings.SCANDIRECTION_HORIZONTAL_OVER_VERTICAL) {
            return y_condition();
        } else {
            return x_condition();
        }
    }
    
    public boolean inner_codition() {
        if (horizontalOverVertical == FontSettings.SCANDIRECTION_HORIZONTAL_OVER_VERTICAL) {
            return x_condition();
        } else {
            return y_condition();
        }
    }

}
