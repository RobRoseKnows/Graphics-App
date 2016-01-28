package com.github.robroseknows.GraphicsApp;

import java.awt.image.BufferedImage;

public class ImageAverageDiff extends DefaultImageOp {

	@Override
	public BufferedImage filter(BufferedImage arg0, BufferedImage arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int averageImage(int[] pixelsIn) {
		int sum = 0;
		for(int val : pixelsIn) {
			sum += val;
		}
		
		return sum / pixelsIn.length;
	}

}
