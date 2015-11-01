/****
 * Kite Christianson -- Project 6 -- SOLO
 * 
 * Daniel Vaughn, CSC 335, Fall 2015, University of Arizona
 * 
 * GUI for NetPaint.
 */

package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.colorchooser.*;

import model.*;
import model.Rectangle;

public class NetPaintGUI extends JPanel implements ChangeListener {
 
	// instanced components
	private JColorChooser tcc;
    private JLabel banner;
    private JRadioButton lineRadioButton;
    private JRadioButton rectangleRadioButton;
    private JRadioButton ovalRadioButton;
    private JRadioButton imageRadioButton;
    
    // network instances
	private Vector<PaintObject> shapes = new Vector<PaintObject>();
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
 
    /**************************************************
     * Constructor. uses Oracle Demo code
     **************************************************/
	
    public NetPaintGUI() {
        super(new BorderLayout());
 
        //the banner is now an invisible element that holds color
        banner = new JLabel("");
        banner.setVisible(false);
 
        // build etch sketch
        JPanel drawingPanel = new EtchPanel();
        drawingPanel.setLayout(new BorderLayout());
        drawingPanel.setPreferredSize(new Dimension(800, 400));
        drawingPanel.add(banner, BorderLayout.CENTER);
        drawingPanel.setBorder(BorderFactory.createTitledBorder("Drawing Area"));
 
        // panel for radios
        JPanel radioPanel = new JPanel(new FlowLayout());
        ButtonGroup radioButtonGroup = new ButtonGroup();
        
        // radios
        lineRadioButton = new JRadioButton("Line", true);  
        rectangleRadioButton = new JRadioButton("Rectangle");
        ovalRadioButton = new JRadioButton("Oval");
        imageRadioButton = new JRadioButton("Image");
        radioButtonGroup.add(lineRadioButton);
        radioButtonGroup.add(rectangleRadioButton);
        radioButtonGroup.add(ovalRadioButton);
        radioButtonGroup.add(imageRadioButton);
        radioPanel.add(lineRadioButton);
        radioPanel.add(rectangleRadioButton);
        radioPanel.add(ovalRadioButton);
        radioPanel.add(imageRadioButton);

        //Set up color chooser for setting text color
        tcc = new JColorChooser(banner.getForeground());
        tcc.getSelectionModel().addChangeListener(this);
        tcc.setBorder(BorderFactory.createTitledBorder("Choose Text Color"));
 
        // add panels to le jframe
        add(drawingPanel, BorderLayout.CENTER);
        add(radioPanel, BorderLayout.EAST);
        add(tcc, BorderLayout.PAGE_END);
    }
    
    /**************************************************
     * Our Drawing Panel
     **************************************************/
    
    // the etch and sketch panel
    class EtchPanel extends JPanel{
    	
    	// panel areas
		private int oldX, oldY, newX, newY;
		private boolean isDrawing;
		
		public EtchPanel(){
			// build panel
			isDrawing = false;
			this.setBackground(Color.WHITE);
			this.setSize(400, 400);
			ListenToMouse listener = new ListenToMouse();
			this.addMouseMotionListener(listener);
			this.addMouseListener(listener);
			
			// start thread stuff
			connect();
			new ServerListener().start();
		}
		
		private class ServerListener extends Thread{
			@Override
			public void run() {
				// Repeatedly accept String objects from the server and add
				try{
					while(true){
						shapes = ((Vector<PaintObject>) ois.readObject());
						repaint();
					}
				}catch (ClassNotFoundException e){
					e.printStackTrace();
				}catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		
		public void connect(){
			try {
				// connect to server
				socket = new Socket("localHost", 9001);

				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		public void sendToServer(){
			for (PaintObject shape : shapes){
				try {
					oos.writeObject(shape);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		@Override
		public void paintComponent(Graphics g) {

			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			// paint all the shapes
			for (int i=0; i<shapes.size(); i++){
				shapes.get(i).draw(g);
			}
			
			// this draws the "preview" if you will, called by mouseMoved
			if (isDrawing) {
				g2.setColor(banner.getForeground());
				if (lineRadioButton.isSelected()){
					new Line(oldX, oldY, newX, newY, banner.getForeground()).draw(g2);
				}
				if (rectangleRadioButton.isSelected()){
					new Rectangle(oldX, oldY, newX, newY, banner.getForeground()).draw(g2);
				}
				if (ovalRadioButton.isSelected()){
					new Oval(oldX, oldY, newX, newY, banner.getForeground()).draw(g2);
				}
				if (imageRadioButton.isSelected()){
					new Doge(oldX, oldY, newX, newY, banner.getForeground()).draw(g2);;
				}
			}
			
		}
    
		private class ListenToMouse implements MouseListener, MouseMotionListener{

			@Override
			public void mouseMoved(MouseEvent e) {
				// constantly get the new coordinates and refresh
				newX = e.getX();
				newY = e.getY();
				repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// on click 1, register it as old coordinate
				if (!isDrawing) {
					oldX = e.getX();
					oldY = e.getY();
				// on click 2, register as a shape and save it 
				}else{
					if (lineRadioButton.isSelected()){
						shapes.add(new Line(oldX, oldY, newX, newY, banner.getForeground()));
					}
					if (rectangleRadioButton.isSelected()){
						shapes.add(new Rectangle(oldX, oldY, newX, newY, banner.getForeground()));
					}
					if (ovalRadioButton.isSelected()){
						shapes.add(new Oval(oldX, oldY, newX, newY, banner.getForeground()));
					}
					if (imageRadioButton.isSelected()){
						shapes.add(new Doge(oldX, oldY, newX, newY, banner.getForeground()));
					}
				}
				// always invert a click state to swap between
				isDrawing = !isDrawing;
				
				// send shapes state to server
				sendToServer();
			}
			

			// unused
			@Override
			public void mouseEntered(MouseEvent e) { }
			@Override
			public void mouseExited(MouseEvent e) { }
			@Override
			public void mousePressed(MouseEvent e) { }
			@Override
			public void mouseReleased(MouseEvent e) { }
			@Override
			public void mouseDragged(MouseEvent e) { }
			
		}
    
    }
    
    /**************************************************
     * Below is all of the stuff from the Oracle Demo.
     **************************************************/
    
 
    public void stateChanged(ChangeEvent e) {
        Color newColor = tcc.getColor();
        banner.setForeground(newColor);
    }
 
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Net Paint Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        JComponent newContentPane = new NetPaintGUI();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
} 

// test for github practice
