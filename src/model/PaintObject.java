package model;

import java.awt.Color;
import java.awt.Graphics;

abstract public class PaintObject {
	
	private int oldX, oldY, newX, newY;
	private Color color;
	
	PaintObject(){
		
	}

	public abstract void paintComponent(Graphics g);
	
}
