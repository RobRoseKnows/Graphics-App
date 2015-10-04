package com.github.robroseknows.GraphicsApp;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.robroseknows.GraphicsApp.MyWindowListener;

public class App extends Canvas{
	private static Frame frame;
	private BufferedImage image;
	private BufferedImage imageProcessed;
	private Canvas currentFilter;
	
	public App() {
		frame = new Frame();
		frame.setMenuBar(createMenuBar());
		try {
			 image = ImageIO.read(new File("default.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentFilter = new Canvas();
		currentFilter.setSize(image.getWidth(), image.getHeight());
	}
	
	
	
	public static void main(String[] args) {
		App myCanvas=new App();
		Frame fr = frame;
		myCanvas.setPreferredSize(new Dimension(myCanvas.image.getWidth(), myCanvas.image.getHeight()));
		fr.add(myCanvas);
		fr.pack();
		fr.addWindowListener(new MyWindowListener());
		fr.setResizable(false);
		fr.setVisible(true);
	}
	
	
	
	/**
	 * This method fires when stuff is updated. It's overridden
	 * in order to prevent the image from flickering.
	 * @param g The graphics object for the Canvas.
	 */
	public void update(Graphics g)
	{
		paint(g);
	} //end public void update(Graphics g)
	
	
	
	/**
	 * This draws the processed image onto the canvas.	
	 * @param g The graphics object for the Canvas.				
	 */
	public void paint(Graphics g)
	{
		if(imageProcessed != null)
			g.drawImage(imageProcessed, 0, 0, null);
		else
			g.drawImage(image, 0, 0, null);
	}
	
	
	
	/**
	 * Function generates the MenuBar that allows users to load and save
	 * images as well as select their desired process.
	 * @return
	 */
	public MenuBar createMenuBar() {
		MenuBar bar = new MenuBar();
		bar.add(createFileMenu());
		return bar;
	}
	
	
	
	/**
	 * Function constructs the File menu which contains the Open and Save options.
	 * @return The generated File menu.
	 */
	public Menu createFileMenu() {
		Menu menu = new Menu("File");
		MenuItemListener menuItemListener = new MenuItemListener();
		
		// Creates file->open option. Allows you to open a file using the menu or by pressing Shift+O
		MenuItem fileOpenItem = new MenuItem("Open", new MenuShortcut(79, true));
		fileOpenItem.setActionCommand("open");
		fileOpenItem.addActionListener(menuItemListener);
		menu.add(fileOpenItem);
		
		// Creates file->save option. Allows you to save a file using the menu or by pressing Shift+S
		MenuItem fileSaveItem = new MenuItem("Save", new MenuShortcut(83, true));
		fileSaveItem.setActionCommand("save");
		fileSaveItem.addActionListener(menuItemListener);
		menu.add(fileSaveItem);
		
		return menu;
	}
	
	
	
	/**
	 * This method opens a file dialog to allow a user to select a file that
	 * they wish to do image processing on.
	 * @return A BufferedImage to perform processing on.
	 */
	public BufferedImage openFileDialog() {
	    JFileChooser fileopen = new JFileChooser(new File(".").getAbsolutePath());
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "Image files", "jpg", "gif", "jpeg", "png");
	    fileopen.setFileFilter(filter);
	    fileopen.setApproveButtonText("Open image");
	    
	    int ret = fileopen.showOpenDialog(null);

	    if (ret == JFileChooser.APPROVE_OPTION) {
	    	File file = fileopen.getSelectedFile();
	      	try {
	    	  return ImageIO.read(file);
	      	} catch (IOException e) {
	    	  // TODO Auto-generated catch block
	    	  e.printStackTrace();
	      	}
	    }
	    
	    return null;
	}

	
	
	/**
	 * Method prompts user to select a location to save the generated image and
	 * returns the File object representing the location.
	 * @return A file where the image will be stored.
	 */
	public File saveFileDialog() {
	    JFileChooser fileopen = new JFileChooser(new File(".").getAbsolutePath());
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "Image files", "png");
	    fileopen.setFileFilter(filter);
	    fileopen.setApproveButtonText("Save image");

	    int ret = fileopen.showSaveDialog(null);

	    if (ret == JFileChooser.APPROVE_OPTION) {
	    	File file = fileopen.getSelectedFile();
	    	return file;
	    }
	    
	    return null;
	}
	
	/**
	 * This inner class handles what happens when people click on certain menu
	 * items. This includes saving files, changing operations and opening files.
	 * @author Robert Rose
	 *
	 */
	class MenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch(e.getActionCommand()) {
			
				// Save the processed image by opening a dialog to prompt for location.
				case "save":
					File file = saveFileDialog();
					if(file != null) {
						try {
							FileOutputStream out = new FileOutputStream(file);
							// Check to make sure that the image has been processed
							if(imageProcessed != null)
								ImageIO.write(imageProcessed, "png", file);
							else
								ImageIO.write(image, "png", file);
							out.close();
						} catch (FileNotFoundException e2) {
							e2.printStackTrace();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}
					break;
				
				// Open a new image by prompting a user to navigate to a file.
				case "open":
					BufferedImage inputImg = openFileDialog();
					if(inputImg != null)
						image = inputImg;
					break;
			}
		}
	}
}
