package model_function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

public class Dungeon{
	
	private int DEFAULT_SIZE = 7; // 8+9 for size after inbetweens
	private double DOOR_PERCENTAGE = 0.4; // from 0.0 to 1.0
	private double SPECIAL_ROOM_PERCENTAGE = 0.4; // chance of rooms being special from (0.0 to 1.0)
	private int lowerLimitSize = 15; // lowerLimit of acceptable size
	private int upperLimitSize = 20; // upperLimit of acceptable size
	
    private char[][] maze; // maze used for the game
    private Set<int[]> newRooms; // keeps track of all rooms
    private Room[][] rooms;
    private Room currentRoom;
    private boolean[][] traversedRooms; // keep track of rooms that are cleared

    public Dungeon() {
    	// default size + (default size + 1) created to make the inbetweens
    	maze = new char[DEFAULT_SIZE * 2 + 1][DEFAULT_SIZE * 2 + 1];
    	traversedRooms = new boolean[DEFAULT_SIZE * 2 + 1][DEFAULT_SIZE * 2 + 1];
    	for(int i = 0; i < traversedRooms.length; i++) {
    		for(int j = 0; j < traversedRooms.length; j++) {
    			traversedRooms[i][j] = false;
    		}
    	}
    	// odd # indexes implies rooms
    	// even # indexes implies room accessibility
    	createMaze();
    }

    public Dungeon(int rows, int columns) {
        this.maze = new char[rows * 2 + 1][columns * 2 + 1];
        traversedRooms = new boolean[rows * 2 + 1][columns * 2 + 1];
        createMaze();
    }
    private void setEntrance() {
    	Room entrance = null;
    	for(int[] loc : newRooms) {
    		if(maze[loc[0]][loc[1]] == 'i') {// magic number, need better naming
    			entrance = rooms[loc[0]][loc[1]]; // enumerators could be used here.
    			traversedRooms[loc[0]][loc[1]] = true;
    			break;
    		}
    	}
    	currentRoom = entrance;
    }
    
    public boolean[] getDoorsForRoom() { 
    	// also check off traversedRoom for currentRoom now
    	traversedRooms[currentRoom.location[0]][currentRoom.location[1]] = true;
    	for(int[] dir : directions(1)) {
    		traversedRooms[currentRoom.location[0]+dir[0]][currentRoom.location[1]+dir[1]] = true;
    	}
    	boolean[] doors = new boolean[4]; // order: down, up, right, left
    	if(currentRoom.down != null) doors[0] = true;
    	if(currentRoom.up != null) doors[1] = true;
    	if(currentRoom.right != null) doors[2] = true;
    	if(currentRoom.left != null) doors[3] = true;
    	return doors;
    }
    public boolean hasMonster(int[] directions) {
    	boolean monsterCleared;
    	if(directions[0] == 0) {
			if(directions[1] == 1) monsterCleared = currentRoom.right.monsterCleared;
			else monsterCleared = currentRoom.left.monsterCleared;
		} else {
			if(directions[0] == 1) monsterCleared = currentRoom.down.monsterCleared;
			else monsterCleared = currentRoom.up.monsterCleared;
		}
    	return !monsterCleared;
    }
    public void move(int[] directions) {
    	currentRoom.monsterCleared = true;
    	if(directions[0] == 0) {
			currentRoom =  directions[1] == 1 ? currentRoom.right : currentRoom.left;
		} else {
			currentRoom =  directions[0] == 1 ? currentRoom.down : currentRoom.up;
		}
    }
//    public char[][] getMaze(){ 
//    	// construct a new maze that only shows traversed rooms and doors
//    	char[][] visibleMaze = new char[maze.length][maze[0].length];
//    	for(int i = 0; i < maze.length; i++) {
//    		for(int j = 0; j < maze[0].length; j++) {
//    			visibleMaze[i][j] = '@';
//    		}
//    	}
//    	for(int i = 0; i < maze.length; i++) {
//    		for(int j = 0; j < maze[0].length; j++) {
//    			if(traversedRooms[i][j]) {
//    				
//    				visibleMaze[i][j] = maze[i][j];
//    				
//    				for(int[] dir : directions(1)) {
//    					int newI = i + dir[0];
//    					int newJ = j + dir[1];
//    					if(isValidCoordinates(newI, newJ)) visibleMaze[newI][newJ] = maze[newI][newJ];
//    				}
//    			}
//    		}
//    	}
//    	System.out.println(toString(visibleMaze));
//    	return visibleMaze;
//    } // to test getMaze()
    public char[][] getMaze(){
    	return maze.clone();
    }
    public boolean[][] getVisibleMaze(){
    	return traversedRooms.clone();
    }
    public int[] getMazeSize() {
    	return new int[] {maze.length, maze[0].length};
    }
    public String toString(char[][] input) {
        // Build a string representation of the dungeon maze
    	
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < input.length; i++) {
        	for(int j = 0; j < input[0].length-1; j++) {
        		sb.append(input[i][j]);
        	}
        	sb.append(input[i][input[0].length-1]);
        	sb.append("\n");
        }
        return sb.toString();
    }
//    // used to check if monster still in room.
//    public boolean isTraversed(int[] dir) {
//    	
//    	return traversedRooms[currentRoom.location[0] + dir[0]*2]
//    			[currentRoom.location[1] + dir[1]*2];
//    }
    public void travToString() {
    	StringBuilder sb = new StringBuilder();
        for(int i = 0; i < traversedRooms.length; i++) {
        	for(int j = 0; j < traversedRooms[0].length-1; j++) {
        		sb.append(traversedRooms[i][j] + " ");
        	}
        	sb.append(maze[i][maze[0].length-1]);
        	sb.append("\n");
        }
        System.out.println(sb.toString());
    }
    
    private void createMaze() {
    	generateBaseMaze();
    	generateRandomDoors();
    	int size = checkMaxSize();
    	if(size < lowerLimitSize || size > upperLimitSize) { // TODO: change to iterative [refactor]
    		createMaze();
    	} else {
//    		System.out.println(size); // used to check size. can remove
        	createNewMaze();
        	generateEntranceAndExitAndPillars();
        	generateMiscRooms();
        	createArrayOfRooms();
        	setEntrance();
    	}
//        generate
    }
    // inbetweens need to be created to show room accessibility
    
    private void generateBaseMaze() {
        // generate base frame of maze
        for(int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j+=2) {
                maze[i][j] = '*'; // this is a wall
            }
        }
        for(int i = 0; i < maze.length; i+=2) {
        	for(int j = 0; j < maze[0].length; j++) {
        		maze[i][j] = '*'; // this is a wall
        	}
        }
        // create rooms, denoted as 'R'
        for(int i = 1; i < maze.length; i+=2) {
        	for(int j = 1; j< maze[0].length; j+=2) {
        		maze[i][j] = 'R';
        	}
        }
        // Place entrance and exit and check if they can reach
        
    }
    
    public static void main(String[] args) {
    	Dungeon dungeon = new Dungeon();
    	System.out.println(dungeon.toString());
    	
    	Map<Integer, Integer> map = new HashMap<>();
    	for (Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
    
    public void generateRandomDoors() {
    	for(int i = 1; i < maze.length - 1; i+=2) {
    		for(int j = 2; j < maze[0].length - 1; j+=2) {
    			if(Math.random() < DOOR_PERCENTAGE) maze[i][j] = '|';
    		}
    	}
    	for(int i = 2; i < maze.length - 1; i+=2) {
    		for(int j = 1; j < maze[0].length - 1; j+=2) {
    			if(Math.random() < DOOR_PERCENTAGE) maze[i][j] = '-';
    		}
    	}
    	// findMaxSize, if not, then regenerate.
    }
    // checks the maxSize of the generated interconnected rooms
    // adds the rooms to a set to generate a cleaner maze
    private int checkMaxSize() {
    	newRooms = new HashSet<>();// used to create a new maze
        int maxSize = 0;
        // 2D array utilized instead of HashSet/Map to save space and remove collisions
        boolean[][] visited = new boolean[maze.length][maze[0].length];

        for (int i = 1; i < maze.length; i += 2) {
            for (int j = 1; j < maze[0].length; j += 2) {

                if (!visited[i][j]) {
                    int checkedSize = 0;
                    Queue<int[]> queue = new LinkedList<>();
                    Set<int[]> currentMaze = new HashSet<>();
                    
                    queue.add(new int[]{i, j}); // initial input into queue

                    while (!queue.isEmpty()) { // BFS algorithm to find all interconnected rooms
                        for (int size = queue.size(); size > 0; size--) {

                            int[] check = queue.poll();

                            // Mark the cell as visited after dequeuing it
                            visited[check[0]][check[1]] = true;
                            
                            int[][] directions = directions(2);

                            for (int[] dir : directions) {
                                int newX = check[0] + dir[0];
                                int newY = check[1] + dir[1];
                                // check if the two rooms are connected
                                // connection checked with dir/2 towards the room
                                int accessX = check[0] + dir[0]/2;
                                int accessY = check[1] + dir[1]/2;
                                if(maze[accessX][accessY] != '|' && maze[accessX][accessY] != '-') continue;
                                if (isValidCoordinates(newX, newY) && !visited[newX][newY]) {
                                    queue.add(new int[]{newX, newY});
                                    visited[newX][newY] = true; // Mark the neighbor as visited
                                }
                            }
                            currentMaze.add(new int[] {check[0], check[1]});
                            checkedSize++;
                        }
                    }
                    if(checkedSize > maxSize) {
                    	newRooms = currentMaze;
                    	maxSize = checkedSize;
                    }
                }

            }
        }
//        for(int[] i : newMaze) {
//        	System.out.println(i[0] + " " + i[1]);
//        }
        return maxSize;
    }
    // helper method to remove redundant retrieval of directions
    public static int[][] directions(int length) { // TODO: enumerator
    	int[][] ret = {{length, 0}, {-length, 0}, {0, length}, {0, -length}};
    	return ret;
    }
    // recreate the maze with only the interconnected rooms
    private void createNewMaze() {
    	char[][] updateMaze = new char[maze.length][maze[0].length];
    	
    	for(int[] k : newRooms) { // can't use .contains on array, as it is an object
    		int[][] directions = directions(1);
    		for(int[] dir : directions) {
    			int doorX = k[0] + dir[0];
    			int doorY = k[1] + dir[1];
    			if(isValidCoordinates(doorX, doorY) && maze[doorX][doorY] != '*') {
    				updateMaze[doorX][doorY] = maze[doorX][doorY];
    			}
    		}
    		updateMaze[k[0]][k[1]] = 'R';
    	}
    	for(int i = 0; i < maze.length; i++) {
    		for(int j = 0; j < maze[0].length; j++) {
    			if(updateMaze[i][j] != 'R' && updateMaze[i][j] != '-' 
    					&& updateMaze[i][j] != '|') updateMaze[i][j] = '*';
    		}
    	}
    	// recreate doors on rooms
    	
    	maze = updateMaze; // aware that maze refers to updateMaze, not a copy of it.
    }
//   // trim maze down to appropriate size
//    private void trimMaze() {
//    	int[] xBounds = {Integer.MAX_VALUE, Integer.MIN_VALUE};
//    	int[] yBounds = {Integer.MAX_VALUE, Integer.MIN_VALUE};
//    	
//    	for(int[] k : newMaze) {
//    		if(k[1] < xBounds[0]) xBounds[0] = k[1];
//    		if(k[1] > xBounds[1]) xBounds[1] = k[1];
//    		if(k[0] < yBounds[0]) yBounds[0] = k[0];
//    		if(k[0] > yBounds[1]) yBounds[1] = k[0];
//    	}
////    	System.out.println("x bounds : [" + xBounds[0] + ", " + xBounds[1] + "]");
////    	System.out.println("y bounds : [" + yBounds[0] + ", " + yBounds[1] + "]");
////    	System.out.println("maze size: " + maze.length + "x" + maze[0].length);
//    	int xRange = xBounds[1] - xBounds[0] + 3; // +2 for outer walls
//    	int yRange = yBounds[1] - yBounds[0] + 3; // +2 for outer walls
//    	char[][] updateMaze = new char[yRange][xRange];
//    	
////    	System.out.println(updateMaze[0].length + " : " + updateMaze.length);
//    	
//    	for(int i = 0; i < yRange; i++) {
//    		for(int j = 0; j < xRange; j++) {
//    			updateMaze[i][j] = maze[i + yBounds[0] - 1][j + xBounds[0] - 1];
//    		}
//    	}
//    	maze = updateMaze;
//    }
//    // private helper method to check trimMaze()
//    private String toStringit(char[][] input) {
//    	StringBuilder sb = new StringBuilder();
//        for(int i = 0; i < input.length; i++) {
//        	for(int j = 0; j < input[0].length-1; j++) {
//        		sb.append(input[i][j] + " ");
//        	}
//        	sb.append(input[i][input[0].length-1]);
//        	sb.append("\n");
//        }
//        return sb.toString();
//    }
    // helper method to see if coordinates are within bounds
    private boolean isValidCoordinates(int y, int x) {
    	return (y < maze.length && y >= 0 && x >= 0 && x < maze[0].length);
    }
    // find 6 dead ends, or regenerate maze.
    private void generateEntranceAndExitAndPillars() { // will learn dfs algorithm later
    	int index = 5;
    	char[] denote = {'A', 'E', 'I', 'P', 'i', 'o'};
    	for(int[] room : newRooms) {
    		int[][] directions = directions(1);
    		int doors = 0;
    		for(int[] dir : directions) {
    			if(maze[room[0]+dir[0]][room[1]+dir[1]] != '*') doors++;
    		}
    		if(doors > 1) continue;
    		maze[room[0]][room[1]] = denote[index];
    		index--;
    		if(index < 0) break;
    	}
    	if(index > -1) createMaze();
    }
    /** 
    M - Multiple Items
	X - Pit
	i - Entrance (In)
	O - Exit (Out)
	V - Vision Potion
	H - Healing Potion ▪ <space> - Empty Room
	A, E, I, P - Pillars
     */
    // creates rooms with pit, vision potion, healing potion, or multiple items.
    // X, V, H, M
    private void generateMiscRooms() {
    	char[] denote = {'X', 'V', 'H', 'M'};
    	
    	for(int i = 1; i < maze.length; i+=2) {
    		for(int j = 1; j < maze[0].length; j+=2) {
    			if(maze[i][j] == 'R') {
    				if(Math.random() < SPECIAL_ROOM_PERCENTAGE) {
    					char roomType = denote[(int) Math.round(Math.random() * 3)];
    					maze[i][j] = roomType;
    				}
    				else maze[i][j] = ' ';
    			}
    		}
    	}
    }
    private void createArrayOfRooms() {
    	rooms = new Room[maze.length][maze[0].length];
    	
    	for(int[] loc: newRooms) {
    		rooms[loc[0]][loc[1]] = new Room(loc, maze[loc[0]][loc[1]]);
    	}
    	
    	for(int[] loc: newRooms) {
    		for(int[] dir : directions(1)) {
    			if(isDoor(loc[0] + dir[0], loc[1] + dir[1])){
    				rooms[loc[0]][loc[1]].connect(dir, 
    						rooms[loc[0] + dir[0]*2][loc[1] + dir[1]*2]);
    			}
    		}
    	}
    	
    }
    private boolean isDoor(int y, int x) {
    	return maze[y][x] == '-' || maze[y][x] == '|';
    }
    // used for testing
    private void roomsToString() {
    	for(int[] loc : newRooms) {
    		Room temp = rooms[loc[0]][loc[1]];
    		System.out.println("room type: " + temp.roomType + " | down: " + temp.down + " | right: " + temp.right 
    				+ " | up: " + temp.up + " | left: " + temp.left);
    	}
    }

    @Override
    public String toString() {
        // Build a string representation of the dungeon maze
    	
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < maze.length; i++) {
        	for(int j = 0; j < maze[0].length-1; j++) {
        		sb.append(maze[i][j] + " ");
        	}
        	sb.append(maze[i][maze[0].length-1]);
        	sb.append("\n");
        }
        return sb.toString();
    }
    
    
    private class Room{ // rooms are interconnected through the predefined maze
    	private Room left, right, up, down;
    	private final int[] location; // location of room [y, x]
    	private final char roomType;
    	private boolean monsterCleared = false;
    	
    	private Room(int[] location, char roomType) {
    		this.location = location;
    		this.roomType = roomType;
    	}
    	private void connect(int[] directions, Room connection) {
    		if(directions[0] == 0) {
    			if(directions[1] == 1) right = connection;
    			else left = connection;
    		} else {
    			if(directions[0] == 1) down = connection;
    			else up = connection;
    		}
    	}
    }
}
