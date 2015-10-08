package com.github.robroseknows.GraphicsApp;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;

/**
 * A super-class to make writing new ImageOps easier by hosting
 * commonly used functions in the interface. Inspiration taken
 * from the implementations found at:
 * http://www.jhlabs.com/ip/blurring.html
 * 
 * @author Robert Rose
 *
 */
public abstract class DefaultImageOp implements BufferedImageOp {

	@Override
	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
		if(destCM == null)
			destCM = src.getColorModel();
		return new BufferedImage(destCM, destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), destCM.isAlphaPremultiplied(), null);
	}

	@Override
	public Rectangle2D getBounds2D(BufferedImage src) {
		return new Rectangle(0,0,src.getWidth(),src.getHeight());
	}

	@Override
	public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
		if(dstPt == null) {
			dstPt = new Point2D.Double(srcPt.getX(), srcPt.getY());
			return dstPt;
		} else {
			dstPt.setLocation(srcPt.getX(), srcPt.getY());
			return dstPt;
		}
	}

	@Override
	public RenderingHints getRenderingHints() {
		return null;
	}
	
	/**
	 * A convenience method for getting ARGB pixels from an image. This tries to avoid the performance
	 * penalty of BufferedImage.getRGB unmanaging the image.
	 * Credit to http://www.jhlabs.com/ip/blurring.html
	 */
	public int[] getRGB( BufferedImage image, int x, int y, int width, int height, int[] pixels ) {
		int type = image.getType();
		if ( type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB )
			return (int [])image.getRaster().getDataElements( x, y, width, height, pixels );
		return image.getRGB( x, y, width, height, pixels, 0, width );
    }

	/**
	 * A convenience method for setting ARGB pixels in an image. This tries to avoid the performance
	 * penalty of BufferedImage.setRGB unmanaging the image.
	 * Credit to http://www.jhlabs.com/ip/blurring.html
	 */
	public void setRGB( BufferedImage image, int x, int y, int width, int height, int[] pixels ) {
		int type = image.getType();
		if ( type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB )
			image.getRaster().setDataElements( x, y, width, height, pixels );
		else
			image.setRGB( x, y, width, height, pixels, 0, width );
    }

}
