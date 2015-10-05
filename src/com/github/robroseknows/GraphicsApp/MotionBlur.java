package com.github.robroseknows.GraphicsApp;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MotionBlur
{		
	//This is the area around the center of the image that will not be blurred.
	private static int BUFFER_ROOM = 25; 
	
	//This is the slope of the saturation function.
	private static float SAT_SLOPE = 0.7059f; 
	
	//This is the intercept of the saturation line.
	private static int SAT_INTR = 75; 
	
	
	public MotionBlur()
	{
		super();
	}
	
	public BufferedImage blur(BufferedImage in, int centX, int centY)
	{
		BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
		for(int x = 0; x < out.getWidth(); x++)
		{
			for(int y = 0; y < out.getHeight(); y++)
			{
				double dist = fdist(x, y, centX, centY);
				int n = (int) Math.pow(3, (int)(dist * .5)/BUFFER_ROOM);
			}
		}
		return out;
	}
	
	public double fdist(int nx, int ny, int px, int py)
	{
		return Math.sqrt(Math.pow(Math.abs(nx-px), 2) + Math.pow(Math.abs(ny-py), 2));
	}

	/************************************************************
	 * This method takes a BufferedImage and boosts the level	*
	 * of saturation pixel by pixel. It then returns the new	*
	 * BufferedImage.											*
	 * @param in The BufferedImage to operate on.				*
	 * @return BufferedImage with saturation boosted.			*
	 ************************************************************/
	private BufferedImage saturationFilter(BufferedImage in)
	{
		if(in == null)
			return null;
		
		BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
		for(int x = 0; x < out.getWidth(); x++)
		{
			for(int y = 0; y < out.getHeight(); y++)
			{
				int color = in.getRGB(x, y);
				int r = (color & 0x00ff0000) >>> 16;
				int g = (color & 0x0000ff00) >>> 8;
				int b = (color & 0x000000ff);
				float[] hsvar = Color.RGBtoHSB(r, g, b, null);
				hsvar[1] = (SAT_INTR + (hsvar[1] * 255) * SAT_SLOPE) / 255f;
				color = Color.HSBtoRGB(hsvar[0], hsvar[1], hsvar[2]);
				out.setRGB(x, y, color);
			} //end for
		} //end for
		return out;
	} //end private BufferedImage saturationFilter(BufferedImage in)
} //end public class RoseTestImage extends Canvas