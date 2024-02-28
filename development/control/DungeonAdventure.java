package control;

import javax.swing.SwingUtilities;

import model_function.Dungeon;
import viewTest.ClickableMap;

public class DungeonAdventure {
	
	private ClickableMap gameWindow;
	private Dungeon dungeon;
	
	public DungeonAdventure() { 
		dungeon = new Dungeon();
		SwingUtilities.invokeLater(() -> { // utilized for UI-responsiveness
            gameWindow = new ClickableMap(this, dungeon);
            runGame();
        });
	}
	
	public static void main(String[] args) {
		DungeonAdventure da = new DungeonAdventure();
		System.out.println(da.dungeon.toString());
//		DungeonAdventure currentGame = new DungeonAdventure();
		
	}
	
//	private void createGame() {
//		adventurer = new Creature();
//	}
	
	public String[] availableMonsters = {"Ogre", "Skeleton", "Gremlin"};
	
	private void runGame() {
		boolean[] doors = dungeon.getDoorsForRoom(); // order: up, down, right, left
		
		String[] monsters = new String[4];
		for(int i = 0; i < doors.length; i++) {
			if(doors[i]) monsters[i] = availableMonsters[(int)Math.round(Math.random() * 2)];
		}
		gameWindow.setRoom(doors, monsters);
	}
	public void move(int[] directions) {
		dungeon.move(directions);
		gameWindow.setRoom(dungeon.getDoorsForRoom(), availableMonsters);
	}
}
