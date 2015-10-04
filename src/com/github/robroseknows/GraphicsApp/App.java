package com.github.robroseknows.GraphicsApp;

import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class App {
	private static Frame frame;
	private static BufferedImage image;
	
	public static void main(String[] args) {
		frame = new Frame();
		frame.setMenuBar(createMenuBar());
		try {
			 image = ImageIO.read(new File("default.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static MenuBar createMenuBar() {
		MenuBar bar = new MenuBar();
		bar.add(createFileMenu());
		return bar;
	}

	public static Menu createFileMenu() {
		Menu menu = new Menu("File");
		
		return menu;
	}
	
	/**
	 * This method opens a file dialog to allow a user to select a file that
	 * they wish to do image processing on.
	 * @return A BufferedImage to perform processing on.
	 */
	public static BufferedImage openFileDialog() {
		// http://stackoverflow.com/questions/7211107/how-to-use-filedialog
		FileDialog fd = new FileDialog(frame, "Choose an image", FileDialog.LOAD);
		fd.setDirectory(new File(".").getAbsolutePath()); // http://stackoverflow.com/a/17541023/1021259
		fd.setFilenameFilter(new FilenameFilter() {
		    @Override
		    public boolean accept(File dir, String name) {
		        return (name.endsWith(".png") || name.endsWith(".jpg") || 
		        	name.endsWith(".jpeg") || name.endsWith(".gif"));
		    }
		});
		fd.setVisible(true);
		String filename = fd.getFile();
		if (filename == null) {
			try {
				return ImageIO.read(new File("default.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				return ImageIO.read(new File(filename));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
