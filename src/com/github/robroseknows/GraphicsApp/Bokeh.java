package com.github.robroseknows.GraphicsApp;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Bokeh extends Canvas{
	private BufferedImage myPic; //The picture that will be editted.
	private boolean done = false;
	private ArrayList<Integer> allX;
	private ArrayList<Integer> allY;
	 
	/********************************************
	 * The constructor for the class. It loads	*
	 * two pictures. It then performs image		*
	 * processing on one of the pictures.		* 
	 ********************************************/
	public Bokeh(BufferedImage toProcess)
	{
		super();
				
		myPic = toProcess;
		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				
			}
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				
			}
		});
	}
	
	public void paint(Graphics g)
	{
		
	} //end public void paint(Graphics g)
	
	public void drawArea(Graphics g, ArrayList<Integer> xs, ArrayList<Integer> ys)
	{
		if(xs.size())
		for(int i = 0; i)
	} //end public void drawArea(Graphics g, ArrayList<Integer> xs, ArrayList<Integer> ys)
}
