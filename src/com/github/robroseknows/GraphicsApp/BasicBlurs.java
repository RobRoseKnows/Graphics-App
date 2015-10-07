package com.github.robroseknows.GraphicsApp;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class BasicBlurs {
	private final int BOX_BLUR_DEFAULT_SIZE = 3;
	
	/*
	public BufferedImage boxBlur(BufferedImage img, int boxSize) {
		
	}
	
	public BufferedImage boxBlur(BufferedImage img) { return boxBlur(img, BOX_BLUR_DEFAULT_SIZE); }
	*/
	
	public BufferedImage boxBlur(BufferedImage img, int boxSize) {
		return blurRows(img, boxSize);
	}
	
	public BufferedImage blurRows(BufferedImage img, int size) {
		BufferedImage blurred = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int row = 0; row < img.getHeight(); row++) {
			int runningRedSum = 0;
			int runningGreenSum = 0;
			int runningBlueSum = 0;
			int sizeLeft = 0;
			int sizeRight = (size - 1) / 2;
			
			for(int i = 0; i < (size - 1) / 2; i++) {
				Color pixel = new Color(img.getRGB(i, row));
				runningRedSum += pixel.getRed();
				runningGreenSum += pixel.getGreen();
				runningBlueSum += pixel.getBlue();
			}
			
			for(int col = 0; col < img.getWidth(); col++) {				
				if((img.getWidth() - col <= (size - 1) / 2) && (col <= (size - 1) / 2)) {
					Color newPixel = new Color(runningRedSum / (sizeLeft + sizeRight + 1), runningGreenSum / (sizeLeft + sizeRight + 1), runningBlueSum / (sizeLeft + sizeRight + 1));
					blurred.setRGB(col, row, newPixel.getRGB());				
				} else if(col <= (size - 1) / 2) {
					runningRedSum += (img.getRGB(0, row) >> 16 ) & 0x000000FF;
					runningGreenSum += (img.getRGB(0, row) >> 8 ) & 0x0000FF00;
					runningBlueSum += img.getRGB(0, row) & 0x00FF0000;
					sizeLeft++;
					
					Color newPixel = new Color(runningRedSum / (sizeLeft + sizeRight + 1), runningGreenSum / (sizeLeft + sizeRight + 1), runningBlueSum / (sizeLeft + sizeRight + 1));
					blurred.setRGB(col, row, newPixel.getRGB());
				} else if(img.getWidth() - col <= (size - 1) / 2) {
					runningRedSum -= (img.getRGB(img.getWidth() - 1, row) >> 16 ) & 0x000000FF;
					runningGreenSum -= (img.getRGB(img.getWidth() - 1, row) >> 8 ) & 0x0000FF00;
					runningBlueSum -= img.getRGB(img.getWidth() - 1, row) & 0x00FF0000;
					sizeRight--;
					
					Color newPixel = new Color(runningRedSum / (sizeLeft + sizeRight + 1), runningGreenSum / (sizeLeft + sizeRight + 1), runningBlueSum / (sizeLeft + sizeRight + 1));
					blurred.setRGB(col, row, newPixel.getRGB());
				} else {
					runningRedSum += (img.getRGB(col + sizeRight, row) >> 16 ) & 0x000000FF;
					runningGreenSum += (img.getRGB(col + sizeRight, row) >> 8 ) & 0x0000FF00;
					runningBlueSum += img.getRGB(col + sizeRight, row) & 0x00FF0000;
					
					runningRedSum -= (img.getRGB(col - sizeLeft, row) >> 16 ) & 0x000000FF;
					runningGreenSum -= (img.getRGB(col - sizeLeft, row) >> 8 ) & 0x000000FF;
					runningBlueSum -= img.getRGB(col - sizeLeft, row) & 0x000000FF;
					
					
					Color newPixel = new Color(runningRedSum / (sizeLeft + sizeRight + 1), runningGreenSum / (sizeLeft + sizeRight + 1), runningBlueSum / (sizeLeft + sizeRight + 1));
					blurred.setRGB(col, row, newPixel.getRGB());
				}
			}
		}
		
		return blurred;
	}
}
