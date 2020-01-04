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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import org.RSSoft.CharMaker.control.models.FontSettings;
import org.RSSoft.CharMaker.util.RSLogger;
import org.RSSoft.CharMaker.view.CharMakerWindow;
import org.RSSoft.CharMaker.view.PicturePane;

/**
 * This class controls settings for the header writer uses the class
 * "CharMakerWindow" to extract the user interface
 *
 * @author Richard
 */
public class ControlFontSettings implements ActionListener {

    private final ArrayList<JRadioButton> rotationButtons;

    private final ArrayList<JRadioButton> bitOrderButtons;
    private final ArrayList<JRadioButton> endianButtons;
    private final ArrayList<JRadioButton> alignmentButtons;

    private final JPanel panelRotationPreview;
    
    private PicturePane scanDirectionPreview;
    private PicturePane rotationPreview;

    private final JCheckBox checkBoxMirrorHorizontally;
    private final JCheckBox checkBoxMirrorVertically;
    private final JCheckBox checkBoxAlignTop;
    private final JCheckBox checkBoxCharacterPreview;

    private final JComboBox comboBoxDataTypes;
    private final ComboBoxDataTypes dataTypes;

    private final JLabel labelFontName;
    private final JLabel labelDataType;

    private int activeRotationButton;
    private int activeBitOrderButton;
    private int activeEndianOrderButton;
    private int activeAlignmentButton;
    private final JTextField fontName;

    private FontSettings fontSettings;
    private final ScanDirectionControl scanDirectionControl;

    /**
     * construct a new controller for the header writer font settings
     *
     * @param view the CharMakerWindow to be used for the user interface
     * @param scanDirectionControl the other controller to support this one; should not be null
     */
    public ControlFontSettings(CharMakerWindow view, ScanDirectionControl scanDirectionControl) {
        this.rotationButtons = new ArrayList<>();

        this.rotationButtons.add(view.getRadioButton0());
        this.rotationButtons.add(view.getRadioButton90());
        this.rotationButtons.add(view.getRadioButton180());
        this.rotationButtons.add(view.getRadioButton270());

        this.bitOrderButtons = new ArrayList<>();
        this.bitOrderButtons.add(view.getRadioButtonMSB());
        this.bitOrderButtons.add(view.getRadioButtonLSB());

        this.endianButtons = new ArrayList<>();
        this.endianButtons.add(view.getRadioButtonBigEndian());
        this.endianButtons.add(view.getRadioButtonLittleEndian());

        this.alignmentButtons = new ArrayList<>();
        this.alignmentButtons.add(view.getRadioButtonAlignTop());
        this.alignmentButtons.add(view.getRadioButtonAlignBottom());

        this.panelRotationPreview = view.getPanelRotationPreview();

        this.scanDirectionControl = scanDirectionControl;
        this.scanDirectionControl.addActionListener(this);

        try {
            this.rotationPreview = new PicturePane(this.getClass().getClassLoader().getResourceAsStream("media/preview_FontSettings3.png"));
            this.scanDirectionPreview = new PicturePane(this.getClass().getClassLoader().getResourceAsStream("media/ScanDirectionDirect.png"));
            
            this.rotationPreview.validate();
            this.scanDirectionPreview.validate();
            this.scanDirectionPreview.setOpaque(false);
            
            OverlayLayout overlay = new OverlayLayout(this.rotationPreview);
            rotationPreview.setLayout(overlay);
            this.rotationPreview.add(this.scanDirectionPreview);

            Box box = new Box(BoxLayout.Y_AXIS);
            box.add(Box.createVerticalGlue());
            box.add(this.rotationPreview);
            box.add(Box.createVerticalGlue());

            Dimension dim1 = this.rotationPreview.getPreferredSize();
            Dimension dim2 = this.scanDirectionPreview.getPreferredSize();
            
            dim1.height = dim1.height > dim2.height ? dim1.height : dim2.height;
            dim1.width = dim1.width > dim2.width ? dim1.width : dim2.width;
            
            box.setPreferredSize(dim1);
            box.setMaximumSize(dim1);
            box.setMinimumSize(dim1);

            //box.setPreferredSize(new Dimension(this.rotationPreview.getWidth(), box.getHeight()));
            this.panelRotationPreview.add(box);
            
            this.panelRotationPreview.setPreferredSize(dim1);
            this.panelRotationPreview.setMaximumSize(dim1);
            this.panelRotationPreview.setMinimumSize(dim1);

            this.panelRotationPreview.validate();
            this.panelRotationPreview.setBackground(Color.white);
        } catch (IOException ex) {
            RSLogger.getLogger().log(Level.SEVERE, null, ex);
        }

        /*
    Icon image = new ImageIcon(this.getClass().getClassLoader().getResource("media/preview_FontSettings.png"));
    JLabel label = new JLabel(image);
    this.panelRotationPreview.add(label);
         */
        this.checkBoxMirrorHorizontally = view.getCheckBoxMirrorY();
        this.checkBoxMirrorVertically = view.getCheckBoxMirrorX();

        this.checkBoxMirrorHorizontally.addActionListener(this);
        this.checkBoxMirrorVertically.addActionListener(this);

        this.checkBoxAlignTop = view.getCheckBoxAlignAtTop();
        this.checkBoxCharacterPreview = view.getCheckBoxCharacterPreview();

        this.labelFontName = view.getLabelFontName();
        this.labelDataType = view.getLabelDatatype();

        this.comboBoxDataTypes = view.getComboBoxDatatype();
        this.dataTypes = new ComboBoxDataTypes();
        this.comboBoxDataTypes.setModel(dataTypes);
        dataTypes.updateList();

        for (int i = 0; i < 4; i += 1) {
            rotationButtons.get(i).addActionListener(this);
        }
        rotationButtons.get(0).setSelected(true);

        for (JRadioButton b : this.bitOrderButtons) {
            b.addActionListener(this);
        }
        this.bitOrderButtons.get(0).setSelected(true);

        for (JRadioButton b : this.endianButtons) {
            b.addActionListener(this);
        }
        this.endianButtons.get(0).setSelected(true);

        for (JRadioButton b : this.alignmentButtons) {
            b.addActionListener(this);
        }
        this.alignmentButtons.get(0).setSelected(true);

        this.fontName = view.getTextFieldFontName();

        this.fontSettings = new FontSettings();
    }

    /**
     * set the labels of the user interface items
     *
     * @todo: parameter labelcontainer for localization
     */
    public void setLabels() {
        this.labelDataType.setText("Data Type");
        this.labelFontName.setText("Font Name");

        rotationButtons.get(0).setText("Rotate 0°");
        rotationButtons.get(1).setText("Rotate 90°");
        rotationButtons.get(2).setText("Rotate 180°");
        rotationButtons.get(3).setText("Rotate 270°");

        bitOrderButtons.get(0).setText("MSB");
        bitOrderButtons.get(1).setText("LSB");

        endianButtons.get(0).setText("Big Endian");
        endianButtons.get(1).setText("Little Endian");

        alignmentButtons.get(0).setText("Align at Top");
        alignmentButtons.get(1).setText("Align at Bottom");

        this.checkBoxMirrorHorizontally.setText("Mirror Horizontally");
        this.checkBoxMirrorVertically.setText(("Mirror Vertically"));
        this.checkBoxAlignTop.setText("unused, remove");
        this.checkBoxCharacterPreview.setText("Character Preview in Comments");

        this.fontName.setText("font");
        int maxWidth = this.labelFontName.getParent().getWidth() - this.labelFontName.getWidth();
        this.fontName.setPreferredSize(new Dimension(maxWidth, this.fontName.getHeight()));
        this.fontName.validate();
        this.fontName.getParent().validate();
    }

    /**
     * set the font settings read from a file
     *
     * @param fontSettings the read font settings, can be null (nothing happens
     * in this case)
     */
    public void setFontSettings(FontSettings fontSettings) {
        if (null == fontSettings) {
            return;
        }

        this.fontSettings = fontSettings;
        dataTypes.setBitDepth(fontSettings.bits);
        fontName.setText(fontSettings.fontName);
        checkBoxMirrorHorizontally.setSelected(fontSettings.mirrorHorizontal);
        checkBoxMirrorVertically.setSelected(fontSettings.mirrorVertical);
        checkBoxCharacterPreview.setSelected(fontSettings.commentPreview);
        
        setActiveRotationButtonIndex(fontSettings.rotation);
        setActiveAlignmendButtonIndex(fontSettings.alignment);
        setActiveBitOrderButtonIndex(fontSettings.bitOrder);
        setActiveEndianOrderButtonIndex(fontSettings.endianOrder);

        scanDirectionControl.setFontSettings(fontSettings);
    }

    /**
     * returns the container FontSettings with settings based on the user action
     *
     * @return the container FontSettings
     */
    public FontSettings getFontSettings() {
        fontSettings.bits = this.dataTypes.getSelectedBitDepth();
        fontSettings.dataType = this.dataTypes.getSelectedDatatypeName();
        fontSettings.fontName = this.fontName.getText();
        fontSettings.mirrorHorizontal = this.checkBoxMirrorHorizontally.isSelected();
        fontSettings.mirrorVertical = this.checkBoxMirrorVertically.isSelected();

        fontSettings.alignment = this.getActiveAlignmentButtonIndex();
        fontSettings.rotation = this.getActiveRotationButtonIndex();
        fontSettings.bitOrder = this.getActiveBitOrderButtonIndex();
        fontSettings.endianOrder = this.getActiveEndianOrderButtonIndex();

        fontSettings.commentPreview = this.checkBoxCharacterPreview.isSelected();

        scanDirectionControl.updateFontSettings(fontSettings);
        
        return this.fontSettings;
    }

    /**
     * returns the content of the field fontName
     *
     * @return the string content of the field fontName
     */
    public String getFontName() {
        return this.fontName.getText();
    }

    /**
     * returns the selected rotation button
     *
     * @return the selected rotation button
     */
    @Deprecated
    public JRadioButton getActiveButton() {
        return rotationButtons.get(activeRotationButton);
    }

    /**
     * returns the index of the active rotation button. the value can be
     * compared to FontSettings.ROTATION_XXX
     *
     * @return the rotation's information
     */
    public int getActiveRotationButtonIndex() {
        return activeRotationButton;
    }
    
    private void setActiveRotationButtonIndex(int index) {
        actionPerformed(new ActionEvent(rotationButtons.get(index), 0, ""));
    }

    /**
     * returns the index of the active bit order button. the value can be
     * compared to FontSettings.BITORDER_XXX
     *
     * @return the bit order information
     */
    public int getActiveBitOrderButtonIndex() {
        return activeBitOrderButton;
    }
    
    private void setActiveBitOrderButtonIndex(int index) {
        actionPerformed(new ActionEvent(bitOrderButtons.get(index), 0, ""));
    }

    /**
     * returns the index of the active endian button. the value can be compared
     * to FontSettings.ENDIANORDER_XXX
     *
     * @return the endianes information
     */
    public int getActiveEndianOrderButtonIndex() {
        return activeEndianOrderButton;
    }
    
    private void setActiveEndianOrderButtonIndex(int index) {
        actionPerformed(new ActionEvent(endianButtons.get(index), 0, ""));
    }

    /**
     * returns the index of the active alignment button. the value can be
     * compared to FontSettings.ALIGNMENT_XXX
     *
     * @return the alignment information
     */
    public int getActiveAlignmentButtonIndex() {
        return activeAlignmentButton;
    }
    
    private void setActiveAlignmendButtonIndex(int index) {
        actionPerformed(new ActionEvent(alignmentButtons.get(index), 0, ""));
    }
    
    private void rotateScanDirectionPreview(int rotation, boolean colunms)
    {
        try {
            if (colunms) {
                scanDirectionPreview.replaceImage(this.getClass().getClassLoader().getResourceAsStream("media/ScanDirectionBroken.png"));
            } else {                
                scanDirectionPreview.replaceImage(this.getClass().getClassLoader().getResourceAsStream("media/ScanDirectionDirect.png"));
            }
            
        } catch (IOException ex) {
            RSLogger.getLogger().log(Level.SEVERE, null, ex);
        }
        
        scanDirectionPreview.mirror(PicturePane.MIRRORAXIS_NONE);
        scanDirectionPreview.rotate(PicturePane.ROTATION_0);
        
        if ((rotation & FontSettings.SCANDIRECTION_VERTICAL_OVER_HORIZONTAL) != 0) {
            scanDirectionPreview.rotate(PicturePane.ROTATION_90);
            scanDirectionPreview.mirror(PicturePane.MIRRORAXIS_HORIZONTAL);
            
            if ((rotation & FontSettings.SCANDIRECTION_DOWN_UP) != 0) {
                scanDirectionPreview.mirrorHorizontal(false);
            }

            if ((rotation & FontSettings.SCANDIRECTION_RIGHT_LEFT) != 0) {
                scanDirectionPreview.mirror(PicturePane.MIRRORAXIS_VERTICAL);
            } 
        } else {
            scanDirectionPreview.rotate(PicturePane.ROTATION_0);
            scanDirectionPreview.mirror(PicturePane.MIRRORAXIS_NONE);
            
            if ((rotation & FontSettings.SCANDIRECTION_DOWN_UP) != 0) {
                scanDirectionPreview.mirror(PicturePane.MIRRORAXIS_VERTICAL);
            }

            if ((rotation & FontSettings.SCANDIRECTION_RIGHT_LEFT) != 0) {
                scanDirectionPreview.mirror(PicturePane.MIRRORAXIS_HORIZONTAL);
            } 
        }
        
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JRadioButton) {
            if (rotationButtons.contains(e.getSource())) {
                activeRotationButton = rotationButtons.indexOf(e.getSource());
                rotationButtons.stream().forEach((b)
                        -> {
                    b.setSelected(false);
                });
                rotationButtons.get(activeRotationButton).setSelected(true);
                this.rotationPreview.rotate(activeRotationButton);
                this.rotationPreview.repaint();
            } else if (bitOrderButtons.contains(e.getSource())) {
                activeBitOrderButton = bitOrderButtons.indexOf(e.getSource());
                for (JRadioButton b : bitOrderButtons) {
                    b.setSelected(false);
                }
                bitOrderButtons.get(activeBitOrderButton).setSelected(true);
            } else if (endianButtons.contains(e.getSource())) {
                activeEndianOrderButton = endianButtons.indexOf(e.getSource());
                for (JRadioButton b : endianButtons) {
                    b.setSelected(false);
                }
                endianButtons.get(activeEndianOrderButton).setSelected(true);
            } else if (alignmentButtons.contains(e.getSource())) {
                activeAlignmentButton = alignmentButtons.indexOf(e.getSource());
                for (JRadioButton b : alignmentButtons) {
                    b.setSelected(false);
                }
                alignmentButtons.get(activeAlignmentButton).setSelected(true);
            }
        } else if (e.getSource() instanceof JCheckBox) {
            if (e.getSource() == this.checkBoxMirrorHorizontally) {
                this.rotationPreview.mirrorHorizontal(this.checkBoxMirrorHorizontally.isSelected());
            } else if (e.getSource() == this.checkBoxMirrorVertically) {
                this.rotationPreview.mirrorVertical(this.checkBoxMirrorVertically.isSelected());
            }
            this.rotationPreview.repaint();
        } else if (e.getSource().equals(scanDirectionControl)) {
            rotateScanDirectionPreview(scanDirectionControl.getScanDirectionFromSelection(), scanDirectionControl.isOrganizedInColunms());
            scanDirectionPreview.repaint();
        }
    }
}
