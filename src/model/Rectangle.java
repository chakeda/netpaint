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
import java.awt.geom.Rectangle2D;

public class Rectangle extends PaintObject {
	
	public Rectangle(int startX, int startY, int endX, int endY, Color color) {
		super(startX, startY, endX, endY, color);
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		// newx - oldx = width. remember geometry?
		int width = newX - oldX;
		int height = newY - oldY;
		int actualX = oldX;
		int actualY = oldY;
		// negative coords : flip
		if (newX - oldX < 0 && newY - oldY < 0){
			width = oldX - newX;
			height = oldY - newY;
			actualX = newX;
			actualY = newY;
		}
		// negative x, flip
		if (newX - oldX < 0 && newY - oldY >= 0){
			width = oldX - newX;
			actualX = newX;		
		}
		// negative y, flip
		if (newX - oldX >= 0 && newY - oldY < 0){
			height = oldY - newY;
			actualY = newY;
		}
		Rectangle2D.Double rect = new Rectangle2D.Double(
				actualX, actualY, width, height);
		g2.fill(rect);
	}
	
	
	
}
