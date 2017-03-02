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
package org.RSSoft.CharMaker.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * This panel shows an image that can be rotated / mirrored
 * The panel is set to the size of the picture
 * 
 * TODO: functions to rotate / mirror the picture
 * @author Richard
 */
public class PicturePane extends JPanel {
  
  public static final int MIRRORAXIS_HORIZONTAL = 1;
  public static final int MIRRORAXIS_VERTICAL = 2;
  
  public static final int ROTATION_0 = 0;
  public static final int ROTATION_90 = 1;
  public static final int ROTATION_180 = 2;
  public static final int ROTATION_270 = 3;
  
  private boolean mirrorVertical;
  private boolean mirrorHorizontal;
  
  private int rotation;
  
  private final BufferedImage picture;
  
  /**
   * construct a picture pane with the given picture
   * @param pictureFile the image to show
   * @throws IOException in case image is not found
   */
  public PicturePane(String pictureFile) throws IOException
  {
    this.picture = ImageIO.read(new File(pictureFile));
    this.rotation = 0;
    this.mirrorHorizontal = false;
    this.mirrorVertical = false;
  }
  
  /**
   * construct a picture pane with the given picture
   * @param is an inout stream of the picture to show
   * @throws IOException in case image is not found
   */
  public PicturePane(InputStream is) throws IOException
  {
    this.picture = ImageIO.read(is);
    this.rotation = 0;
    this.mirrorHorizontal = false;
    this.mirrorVertical = false;
  }
  
  /**
   * mirrors the picture in the given axis. previous mirror operations are kept,
   * so the function can be called twice with both axis to mirror the picture in both
   * directions
   * 
   * after operation, this pane is repainted
   * 
   * valid argument for axis are:
   *  MIRRORAXIS_HORIZONTAL
   *  MIRRORAXIS_VERTICAL
   *  0: both mirror axis are reset
   * @param axis the axis to mirror
   */
  public void mirror(int axis) {
    switch (axis) {
      case MIRRORAXIS_HORIZONTAL:
        this.mirrorHorizontal = true;
        break;
      case MIRRORAXIS_VERTICAL:
        this.mirrorVertical = true;
        break;
      default:
        this.mirrorHorizontal = false;
        this.mirrorVertical = false;
        break;
    }
    this.repaint();
  }
  
  /**
   * set the horizontal axis mirrored.
   * 
   * after operation, this pane is repainted
   * 
   * @param mirror mirror
   */
  public void mirrorHorizontal(boolean mirror) {
    this.mirrorHorizontal = mirror;
    this.repaint();
  }
  
  /**
   * set the vertical axis mirrored.
   * 
   * after operation, this pane is repainted
   * 
   * @param mirror mirror
   */
  public void mirrorVertical(boolean mirror) {
    this.mirrorVertical = mirror;
    this.repaint();
  }
  
  /**
   * rotate the picture in 90 degree steps.
   * 
   * after operation, this pane is repainted
   * 
   * valid arguments for rotation are:
   *  ROTATION_0
   *  ROTATION_90
   *  ROTATION_180
   *  ROTATION_270
   * @param rotation the rotation
   */
  public void rotate(int rotation) {
    this.rotation = rotation;
    this.repaint();
  }
  
  private void transform(Graphics2D g2)
  {
    AffineTransform at = g2.getTransform();
    at.setToQuadrantRotation(-this.rotation, picture.getWidth()/2.0, picture.getHeight()/2.0);
    at.scale(this.mirrorHorizontal ? -1 : 1, this.mirrorVertical ? -1 : 1);
    at.translate(this.mirrorHorizontal ? -picture.getWidth() : 0, this.mirrorVertical ? -picture.getHeight() : 0);
    AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
    g2.drawImage(op.filter(picture, null), 0, 0, null);

    this.setSize(this.picture.getWidth(), this.picture.getHeight());
  }
  
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    this.transform((Graphics2D) g);
  }
  
}
