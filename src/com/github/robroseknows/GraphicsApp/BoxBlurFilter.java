package com.github.robroseknows.GraphicsApp;

import java.awt.image.BufferedImage;

public class BoxBlurFilter extends DefaultImageOp {

	private int horizontalRadius = 100;
	private int verticalRadius = 100;
	
	@Override
	public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        int width = src.getWidth();
        int height = src.getHeight();

        if ( dest == null )
            dest = createCompatibleDestImage( src, null );

        int[] inPixels = new int[width*height];
        int[] outPixels = new int[width*height];
        inPixels = getRGB(src, 0, 0, width, height, inPixels);
        blur(inPixels, outPixels, width, height, horizontalRadius);
        blur(outPixels, inPixels, height, width, verticalRadius);
       
        dest = setRGB(dest, width, height, outPixels);
		return dest;
	}
	
	public static void blur(int[] pixelsIn, int[] pixelsOut, int width, int height, int radius) {
		int indexIn = 0;
		
		for(int y = 0; y < height; y++) {
			int indexOut = 0;
			
			int runningAlpha = 0;
			int runningRed = 0;
			int runningGreen = 0;
			int runningBlue = 0;
			
			// Add the ones before box is full size.
			for(int i = -radius; i <= radius; i++) {
				int addToIn = i;
				if(i < 0)
					addToIn = 0;
				if(i > width-1)
					addToIn = width-1;
				int rgb = pixelsIn[indexIn + addToIn];
				runningAlpha += (rgb >> 24) & 0x000000ff;
				runningRed += (rgb >> 16) & 0x000000ff;
				runningGreen += (rgb >> 8) & 0x000000ff;
				runningBlue += rgb & 0x000000ff;
			}
			
			// Add and subtract the rest.
			for(int x = 0; x < width; x++) {
				pixelsOut[indexOut] = ((runningAlpha / (radius + 1)) << 24) | ((runningRed / (radius + 1)) << 16) |
						((runningGreen / (radius + 1)) << 8) | (runningBlue / (radius + 1));
				
				int indexToLeft = x-radius;
				if(indexToLeft < 0)
					indexToLeft = 0;
				
				int indexToRight = x+radius+1;
				if(indexToRight > width-1)
					indexToRight = width-1;
				
				int rgbLeft = pixelsIn[indexIn + indexToLeft];
				int rgbRight = pixelsIn[indexIn + indexToRight];
				
				runningAlpha -= (rgbLeft >> 24) & 0x000000ff;
				runningRed -= (rgbLeft >> 16) & 0x000000ff;
				runningGreen -= (rgbLeft >> 8) & 0x000000ff;
				runningBlue -= rgbLeft & 0x000000ff;
				
				runningAlpha += (rgbRight >> 24) & 0x000000ff;
				runningRed += (rgbRight >> 16) & 0x000000ff;
				runningGreen += (rgbRight >> 8) & 0x000000ff;
				runningBlue += rgbRight & 0x000000ff;				
				
				indexOut += height;
			}
			indexIn += width;
		}
	}
	
	public void setRadius(int radius) {
		horizontalRadius = radius;
		verticalRadius = radius;
	}

	
}