package viewTest;

import javax.swing.*;

import control.DungeonAdventure;
import mod.Dungeon;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickableMap extends JFrame {
	
	private static final long serialVersionUID = -8959221219558654747L;
	private TilePanel[][] tiles;
	private Dungeon dungeon;
	private DungeonAdventure dungeonAdventure;
			
    public ClickableMap(DungeonAdventure dungeonAdventure, Dungeon dungeon) {
    	this.dungeon = dungeon;
    	this.dungeonAdventure = dungeonAdventure;
    	
        setTitle("Clickable Map");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 3));
        tiles = new TilePanel[3][3];
//        Color[] color = {Color.PINK, Color.green, Color.red, Color.CYAN, Color.GRAY,
//        		Color.blue, Color.orange, Color.magenta, Color.lightGray};
        
        for(int i = 0; i < 3; i++) {
        	for(int j = 0; j < 3; j++) {
        		tiles[i][j] = new TilePanel(Color.black);
        	}
        }
        tiles[1][1].setBackground(Color.white);
        this.addKeyListener(new KeyListener() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		dungeon.travToString();
        	}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        requestFocus();
    }

    public void setRoom(boolean doors[], String monsters[]) {
    	int index = 0;
    	
    	for(int[] dir : Dungeon.directions(1)) {
    		TilePanel tile = tiles[1 + dir[0]][1 + dir[1]];
    		
    		if(doors[index]) {
    			tile.setBackground(Color.cyan);
    			tile.setActionable(true);
    			tile.setLocation(dir);
    		} else {
    			tile.setBackground(Color.black);
    			tile.setActionable(false);
    		}
    		index++;
    	}
    	update();
    }
    public void update() {
        setLayout(new GridLayout(4, 3));
    	for(int i = 0; i < 3; i++) {
    		for(int j = 0; j < 3; j++) {
    			this.add(tiles[i][j]);
    		}
    	}
    	pack();
    	repaint();
    }

    private class TilePanel extends JPanel {
        private static final long serialVersionUID = 1536048036353336639L;
		private boolean door;
		private int[] location;

        public TilePanel(Color color) {
            setPreferredSize(new Dimension(100, 100));
            setBackground(color);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	if(door) dungeonAdventure.move(location);
                    // Handle tile click action here
                }
            });
        }
        public void setLocation(int[] location) {
        	this.location = location;
        }
        public void setActionable(boolean actionable) {
        	door = actionable;
        }
    }
    private class MapPanel extends JPanel{
    	
    }
}

