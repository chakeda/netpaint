/****
 * Kite Christianson -- Project 6 -- SOLO
 * 
 * Daniel Vaughn, CSC 335, Fall 2015, University of Arizona
 * 
 * A PaintObject subclass. 
 */
package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Line extends PaintObject {
	
	public Line(int startX, int startY, int endX, int endY, Color color) {
		super(startX, startY, endX, endY, color);
	}

	@Override
	public void draw(Graphics g) {
	    Graphics2D g2 = (Graphics2D)g;
	    g2.setColor(color);
	    g2.drawLine(oldX, oldY, newX, newY);
	}
	
	
	
}
