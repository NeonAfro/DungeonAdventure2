package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
	private BufferedImage buttonImage;
    private Rectangle actionButtonBounds;
    
    public GamePanel() {
        setPreferredSize(new Dimension(400, 400)); // Set preferred size for the panel
        
        try {
            buttonImage = ImageIO.read(getClass().getResourceAsStream("/res/up.png")); // Load button image
        } catch (IOException e) {
            e.printStackTrace();
        }

        actionButtonBounds = new Rectangle(0, 0, 100, 100);

        // Add mouse listener to detect clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (actionButtonBounds.contains(e.getPoint())) {
                    // The mouse click occurred within the bounds of the action button
                    System.out.println("Action button clicked!");
                    // Perform the action associated with the button
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            	super.mouseReleased(e);
            	if(actionButtonBounds.contains(e.getPoint())) {
            		System.out.println("Action button released!");
            	}
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the button image at the specified bounds
        g.drawImage(buttonImage, actionButtonBounds.x, actionButtonBounds.y, actionButtonBounds.width
        		, actionButtonBounds.height, null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new GamePanel());
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }
}

