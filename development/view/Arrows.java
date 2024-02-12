package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Arrows extends JPanel{

    private static final long serialVersionUID = 1L;
    
	private BufferedImage image;
    String imagePath;
    
    public void setImage(boolean pressed){
        try {
        	image = pressed ? ImageIO.read(getClass().getResourceAsStream("/res/"+ imagePath +"_pressed.png"))
        			: ImageIO.read(getClass().getResourceAsStream("/res/"+ imagePath +".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public Arrows() {
    	setPreferredSize(new Dimension(100, 100)); // Set preferred size for the panel
    }
    public Arrows(String path) {
    	imagePath = path;
        setPreferredSize(new Dimension(100, 100)); // Set preferred size for the panel
        setImage(false);
        // Add mouse listener to detect clicks
        addMouseListener(new MouseAdapter() {
        	
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                setImage(true);
                repaint(); // Repaint the panel to update the appearance
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseClicked(e);
                setImage(false);
                repaint(); // Repaint the panel to update the appearance
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        System.out.println("x: " + getX() +" | y: " + getY());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Arrow Button Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3)); // Create a panel for arranging arrow buttons

        // Create arrow button panels and add them to the button panel
        buttonPanel.add(new Arrows());
        buttonPanel.add(new Arrows("up"));
        buttonPanel.add(new Arrows());
        
        buttonPanel.add(new Arrows("left"));
        buttonPanel.add(new Arrows());
        buttonPanel.add(new Arrows("right"));
        
        buttonPanel.add(new Arrows());
        buttonPanel.add(new Arrows("down"));
        buttonPanel.add(new Arrows());
        
        frame.getContentPane().add(buttonPanel);

        frame.pack();
        System.out.println("frame WxH: " + frame.getWidth() +" | "+ frame.getHeight());
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }
}
