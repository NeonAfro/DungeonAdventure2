package control;

import javax.swing.SwingUtilities;

import model_function.Dungeon;
import model_function.DungeonCharacter;
import viewTest.GameFrame;

public class DungeonAdventure {
	// code standards TODO:
	private GameFrame gameWindow;
	private DungeonCharacter adventurer;
	private Dungeon dungeon;
	
	public DungeonAdventure() { 
		dungeon = new Dungeon();
		SwingUtilities.invokeLater(() -> { // utilized for UI-responsiveness & EDT thread safety
            gameWindow = new GameFrame(this);
            setRoom();
        });
		// mock player
		adventurer = new DungeonCharacter("Hero", 100);
	}
	
	public static void main(String[] args) {
		DungeonAdventure da = new DungeonAdventure();
		System.out.println(da.dungeon.toString());
//		DungeonAdventure currentGame = new DungeonAdventure();
		
	}
	public DungeonCharacter getPlayer() {
		return adventurer;
	}
	public DungeonCharacter getEnemy() {
		return new DungeonCharacter("Ogre", 100); // mock enemy
	}
	
	public String[] availableMonsters = {"Ogre", "Skeleton", "Gremlin"}; // used for runGame
	
	private void setRoom() {
		boolean[] doors = dungeon.getDoorsForRoom(); // order: up, down, right, left
		
		String[] monsters = new String[4];
		for(int i = 0; i < doors.length; i++) {
			if(doors[i]) monsters[i] = availableMonsters[(int)Math.round(Math.random() * 2)];
		}
		gameWindow.setRoom(doors, monsters);
	}
	public void move(int[] directions) {
		if(dungeon.hasMonster(directions)) gameWindow.switchToBattle();
		dungeon.move(directions);
		gameWindow.setRoom(dungeon.getDoorsForRoom(), availableMonsters);
	}
	public boolean hasMonster(int[] directions) {
		return dungeon.hasMonster(directions);
	}
	public void showMap() {
		gameWindow.switchToMap();
	}
	public char[][] getMaze(){
		return dungeon.getMaze();
	}
	public boolean[][] getVisibleMaze(){
		return dungeon.getVisibleMaze();
	}
	public int[] getMazeSize() {
		return dungeon.getMazeSize();
	}
//	public char getRoomType() {
//		return dungeon.get
//	}
//	public void fight(int[] location) {
//		if(!dungeon.isTraversed(location)) gameWindow.switchToBattle();
//	}
}
