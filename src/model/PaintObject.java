/****
 * Kite Christianson -- Project 6 -- SOLO
 * 
 * Daniel Vaughn, CSC 335, Fall 2015, University of Arizona
 * 
 * An abstract class for any object to be painted.  
 */
package model;

import java.awt.Color;
import java.awt.Graphics;

abstract public class PaintObject {
	
	protected int oldX, oldY, newX, newY;
	protected Color color;
	
	PaintObject(int oldX, int oldY, int newX, int newY, Color color){
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
		this.color = color;
	}

	// the only thing different about subclasses is how I draw them.
	public abstract void draw(Graphics g);
	
}
