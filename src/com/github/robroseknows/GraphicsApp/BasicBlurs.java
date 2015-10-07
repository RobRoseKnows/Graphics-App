package com.github.robroseknows.GraphicsApp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BasicBlurs {
	
	// Default size of the Kernel for boxblur. Odd number >1.
	private final int BOX_BLUR_DEFAULT_SIZE = 3;
	
	//I'll add threading eventually.
	private final int BOX_BLUR_NUM_THREADS = 10;
	
	public BufferedImage boxBlur(BufferedImage img, int boxSize) {
		
		// If the kernel is too small or even, don't perform blur.
		if(boxSize % 2 == 0 || boxSize < 3) {
			System.out.println("The kernel is too small and/or not an odd number.");
			return img;
		}
		// The two runnables for horizontal and vertical blurring.
		BoxBlurRowsRunnable horizontalRunnable = new BoxBlurRowsRunnable(img, boxSize);
		BoxBlurRowsRunnable verticalRunnable = new BoxBlurRowsRunnable(img, boxSize);		
		
		// The blurred buffered image from each.
		BufferedImage horizontalBlur = img;
		BufferedImage verticalBlur = img;
		
		BufferedImage rotatedImg = rotateImg(img, 90);
		
        final ExecutorService service;
        final Future<BufferedImage>  taskH;
        final Future<BufferedImage> taskV;

        service = Executors.newFixedThreadPool(2);        
        taskH = service.submit(new BoxBlurRowsRunnable(img, boxSize));
        taskV = service.submit(new BoxBlurRowsRunnable(rotatedImg, boxSize));

        try {
            horizontalBlur = taskH.get();
            verticalBlur = taskV.get();
        } catch(final InterruptedException ex) {
            ex.printStackTrace();
        } catch(final ExecutionException ex) {
            ex.printStackTrace();
        }
        service.shutdownNow();
        
        verticalBlur = rotateImg(verticalBlur, -90);
		
        return rotatedImg;
		//return combineImages(horizontalBlur, verticalBlur);
	}
	
	public BufferedImage combineImages(BufferedImage... imgs) {
		for(int i = 1; i < imgs.length; i++) {
			if(!(imgs[i-1].getHeight() == imgs[i].getHeight() && imgs[i-1].getWidth() == imgs[i].getWidth())) {
				System.out.println("Images are not all the same size, returning image 0.");
				return imgs[0];
			}
		}
		
		BufferedImage newImg = new BufferedImage(imgs[0].getWidth(), imgs[0].getHeight(), imgs[0].getType());
		
		for(int y = 0; y < imgs[0].getHeight(); y++) {
			for(int x = 0; x < imgs[0].getWidth(); x++) {
				int runningRedSum = 0;
				int runningGreenSum = 0;
				int runningBlueSum = 0;
				
				for(int i = 0; i < imgs.length; i++) {
					runningRedSum += (imgs[i].getRGB(x, y) >> 16 ) & 0x000000FF;
					runningGreenSum += (imgs[i].getRGB(x, y) >> 8 ) & 0x000000FF;
					runningBlueSum += imgs[i].getRGB(x, y) & 0x000000FF;
				}
				
				int newRGB = 0xFF000000;
				newRGB |= runningBlueSum/imgs.length;
				newRGB |= (runningGreenSum << 8)/imgs.length;
				newRGB |= (runningRedSum << 16)/imgs.length;
				
				newImg.setRGB(x, y, newRGB);
			}
		}
		
		return newImg;
	}
	
	public BufferedImage rotateImg(BufferedImage src, int angle) {
		double angleInRadians = Math.toRadians(angle);
		BufferedImage result = new BufferedImage(src.getHeight(), src.getWidth(), src.getType());
		
	    int srcWidth = src.getWidth();
	    int srcHeight = src.getHeight();
		
	    double sin = Math.abs(Math.sin(angleInRadians));
	    double cos = Math.abs(Math.cos(angleInRadians));
	    int newWidth = (int) Math.floor(srcWidth * cos + srcHeight * sin);
	    int newHeight = (int) Math.floor(srcHeight * cos + srcWidth * sin);
		
	    Graphics2D g = result.createGraphics();
	    g.translate((newWidth - srcWidth) / 2, (newHeight - srcHeight) / 2);
	    g.rotate(angleInRadians, srcWidth / 2, srcHeight / 2);
	    g.drawRenderedImage(src, null);
	    
		return result;
	}
	
	private class BoxBlurRowsRunnable implements Callable<BufferedImage> {

		private Thread thread;
		private BufferedImage bImg;
		private int kSize = BOX_BLUR_DEFAULT_SIZE;
		
		public BoxBlurRowsRunnable(BufferedImage img, int size) {
			bImg = img;
			kSize = size;
		}
		
		@Override
		public BufferedImage call() {
			// TODO Auto-generated method stub
			return blurRows();
		}
		
		public BufferedImage blurRows() {
			BufferedImage img = bImg;
			int size = kSize;
			BufferedImage blurred = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
			for(int row = 0; row < img.getHeight(); row++) {
				int runningRedSum = 0;
				int runningGreenSum = 0;
				int runningBlueSum = 0;
				int sizeLeft = 0;
				int sizeRight = 0;
				
				for(int i = 0; i < (size - 1) / 2; i++) {
					runningRedSum += (img.getRGB(i, row) >> 16 ) & 0x000000FF;
					runningGreenSum += (img.getRGB(i, row) >> 8 ) & 0x000000FF;
					runningBlueSum += img.getRGB(i, row) & 0x000000FF;
					sizeRight++;
				}
				
				for(int col = 0; col < img.getWidth(); col++) {
					if((img.getWidth() - col <= (size - 1) / 2) && (col <= (size - 1) / 2)) {
						Color newPixel = new Color(runningRedSum / (sizeLeft + sizeRight), runningGreenSum / (sizeLeft + sizeRight), runningBlueSum / (sizeLeft + sizeRight));
						blurred.setRGB(col, row, newPixel.getRGB());				
					} else if(col <= (size - 1) / 2) {
						runningRedSum += (img.getRGB(0, row) >> 16 ) & 0x000000FF;
						runningGreenSum += (img.getRGB(0, row) >> 8 ) & 0x000000FF;
						runningBlueSum += img.getRGB(0, row) & 0x000000FF;
						sizeLeft++;
						
						Color newPixel = new Color(runningRedSum / (sizeLeft + sizeRight), runningGreenSum / (sizeLeft + sizeRight), runningBlueSum / (sizeLeft + sizeRight));
						blurred.setRGB(col, row, newPixel.getRGB());
					} else if(img.getWidth() - col <= (size - 1) / 2) {
						runningRedSum -= (img.getRGB(img.getWidth() - 1, row) >> 16 ) & 0x000000FF;
						runningGreenSum -= (img.getRGB(img.getWidth() - 1, row) >> 8 ) & 0x000000FF;
						runningBlueSum -= img.getRGB(img.getWidth() - 1, row) & 0x000000FF;
						sizeRight--;
						
						Color newPixel = new Color(runningRedSum / (sizeLeft + sizeRight), runningGreenSum / (sizeLeft + sizeRight), runningBlueSum / (sizeLeft + sizeRight));
						blurred.setRGB(col, row, newPixel.getRGB());
					} else {
						runningRedSum += (img.getRGB(col + sizeRight, row) >> 16 ) & 0x000000FF;
						runningGreenSum += (img.getRGB(col + sizeRight, row) >> 8 ) & 0x000000FF;
						runningBlueSum += img.getRGB(col + sizeRight, row) & 0x000000FF;
						
						runningRedSum -= (img.getRGB(col - sizeLeft, row) >> 16 ) & 0x000000FF;
						runningGreenSum -= (img.getRGB(col - sizeLeft, row) >> 8 ) & 0x000000FF;
						runningBlueSum -= img.getRGB(col - sizeLeft, row) & 0x000000FF;
						
						
						Color newPixel = new Color(runningRedSum / (sizeLeft + sizeRight + 1), runningGreenSum / (sizeLeft + sizeRight + 1), runningBlueSum / (sizeLeft + sizeRight + 1));
						blurred.setRGB(col, row, newPixel.getRGB());
					}
				}
				
				// Thread was interrupted so just return what we have.
			    if (Thread.interrupted()) {
			    	return blurred;
			    }
			}
			
			return blurred;
		}
		
		public void start() {
			if (thread == null) {
				thread = new Thread();
				thread.start();
		    }
		}
	}
}
