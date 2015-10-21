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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Doge extends PaintObject {
	
	public Doge(int startX, int startY, int endX, int endY, Color color) {
		super(startX, startY, endX, endY, color);
	}

	@Override
	public void draw(Graphics g) {
		// doge is smart enough to draw without negative control.
		// much buffer
		BufferedImage doge = null; // wow
		try{ 
			doge = ImageIO.read(new File("doge.jpeg")); // much jpeg
		} catch (IOException e) {
		    e.printStackTrace(); // such error handling
		}//wow
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(doge, oldX, oldY, newX-oldX, newY-oldY, null);
	}
	
	
	
	
	
}
