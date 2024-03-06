package viewTest;

import javax.swing.*;

import control.DungeonAdventure;
import model_function.Dungeon;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickableMap extends JPanel {
	
	private static final long serialVersionUID = -8959221219558654747L;
	private TilePanel[][] tiles;
	private DungeonAdventure dungeonAdventure;
			
    public ClickableMap(DungeonAdventure dungeonAdventure) {
    	this.dungeonAdventure = dungeonAdventure;
        setLayout(new GridLayout(3, 3));
        tiles = new TilePanel[3][3];
//        Color[] color = {Color.PINK, Color.green, Color.red, Color.CYAN, Color.GRAY,
//        		Color.blue, Color.orange, Color.magenta, Color.lightGray};
        
        for(int i = 0; i < 3; i++) {
        	for(int j = 0; j < 3; j++) {
        		tiles[i][j] = new TilePanel(Color.black);
        	}
        }
        tiles[1][1].setBackground(Color.white);

        // healthSpot is clickable, and will update health and health potions.
    	TilePanel healthSpot = tiles[2][0]; // on top right of frame.
    	
    	// mapSpot tells gameFrame to display the Map
    	TilePanel mapSpot = tiles[2][2]; // bottom right of frame.
    	JTextArea mapString = new JTextArea();
    	mapString.setSelectedTextColor(Color.white);
    	mapString.setText("Switch to Map");
    	mapSpot.add(mapString);
    	mapSpot.setBackground(Color.gray);
    	mapSpot.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			dungeonAdventure.showMap();
    		}
    	});
    	// depending on type of room, the middle tile will do something special.
    	TilePanel middle = tiles[1][1];
    	
//    	switch(dungeonAdventure.getRoomType()) {
//    	
//    	}
//    	
    	tiles[1][1].type = 'i';
    	
        this.addKeyListener(new KeyListener() {
        	@Override
        	public void keyPressed(KeyEvent e) {
//        		dungeonAdventure.travToString();
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
    }
    public void setRoom(boolean doors[], String monsters[]) {
    	int index = 0;
    	
    	for(int[] dir : Dungeon.directions(1)) {
    		TilePanel tile = tiles[1 + dir[0]][1 + dir[1]];
    		
    		if(doors[index]) {
//    			tile.monster = true;
    			
    			tile.setBackground(dungeonAdventure.hasMonster(dir) ? Color.red : Color.green);
    			tile.setActionable(true);
    			tile.setDoor(dir);
    		} else {
    			tile.setBackground(Color.black);
    			tile.setActionable(false);
    		}
    		index++;
    	}
    	update();
    }
    public void update() {
        setLayout(new GridLayout(3, 3));
    	for(int i = 0; i < 3; i++) {
    		for(int j = 0; j < 3; j++) {
    			this.add(tiles[i][j]);
    		}
    	}
    	repaint();
    }

    private class TilePanel extends JPanel {
        private static final long serialVersionUID = 1536048036353336639L;
//        private boolean monster;
		private boolean door;
		private int[] location;
		private char type;

        public TilePanel(Color color) {
            setPreferredSize(new Dimension(100, 100));
            setBackground(color);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
            SwingUtilities.invokeLater(() -> {
            	addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
//                    	if(monster) {
//                    		dungeonAdventure.fight(location);
//                    	}
                    	if(door) dungeonAdventure.move(location);
                        // Handle tile click action here
                    }
                });
            });
        }
        public void setDoor(int[] location) {
        	this.location = location;
        }
        public void setActionable(boolean actionable) {
        	door = actionable;
        }
    }
    
}

