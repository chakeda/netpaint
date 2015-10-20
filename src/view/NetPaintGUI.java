package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;

import javax.swing.colorchooser.*;

import model.PaintObject;
import model.Line;
 

public class NetPaintGUI extends JPanel implements ChangeListener {
 
    protected JColorChooser tcc;
    protected JLabel banner;
 
    public NetPaintGUI() {
        super(new BorderLayout());
 
        //the banner is an invisible element that holds color
        banner = new JLabel("");
        banner.setVisible(false);
 
        // build etch sketch
        JPanel drawingPanel = new EtchPanel();
        drawingPanel.setLayout(new BorderLayout());
        drawingPanel.setPreferredSize(new Dimension(400, 400));
        drawingPanel.add(banner, BorderLayout.CENTER);
        drawingPanel.setBorder(BorderFactory.createTitledBorder("Drawing Area"));
 
        // panel for radio
        JPanel radioPanel = new JPanel();
        ButtonGroup radioButtonGroup = new ButtonGroup();

        // radios
        JRadioButton lineRadioButton = new JRadioButton("Line",true);  
        JRadioButton rectangleRadioButton = new JRadioButton("Rectangle");
        JRadioButton ovalRadioButton = new JRadioButton("Oval");
        JRadioButton imageRadioButton = new JRadioButton("Image");
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
 
        add(drawingPanel, BorderLayout.CENTER);
        add(radioPanel, BorderLayout.EAST);
        add(tcc, BorderLayout.PAGE_END);
    }
    
    // the etch and sketch panel
    class EtchPanel extends JPanel{
    	
		private int oldX, oldY, newX, newY;
		private boolean isDrawing;
		private ArrayList<Shape> lines = new ArrayList<Shape>();
		
		public EtchPanel(){
			isDrawing = false;
			this.setBackground(Color.WHITE);
			this.setSize(400, 400);
			ListenToMouse listener = new ListenToMouse();
			this.addMouseMotionListener(listener);
			this.addMouseListener(listener);
		}
		
		 public void paintComponent(Graphics g) {

		    super.paintComponent(g);
		    Graphics2D g2 = (Graphics2D)g;
		
		    g2.setColor(Color.RED);
		    g2.drawLine(oldX, oldY, newX, newY);

		}
    
		private class ListenToMouse implements MouseListener, MouseMotionListener{

			@Override
			public void mouseDragged(MouseEvent e) {
				
				int x = e.getX();
				int y = e.getY();
				
			}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				newX = e.getX();
				newY = e.getY();
				repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				oldX = newX;
				oldY = newY;
				newX = e.getX();
				newY = e.getY();
				Shape line = new Line2D.Float(oldX, oldY, newX, newY);
				lines.add(line);
				repaint();
			}


			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		}
    
    }
    
    
 
    public void stateChanged(ChangeEvent e) {
        Color newColor = tcc.getColor();
        banner.setForeground(newColor);
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
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
