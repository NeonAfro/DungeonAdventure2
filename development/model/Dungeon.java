package model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Dungeon{
	
	private int DEFAULT_SIZE = 6; // 8+9 for size after inbetweens
	private double DOOR_PERCENTAGE = 0.4; // from 0.0 to 1.0
	private int lowerLimitSize = 10; // lowerLimit of acceptable size
	private int upperLimitSize = 15; // upperLimit of acceptable size
	
    private char[][] maze; // maze used for the game
    private Set<int[]> newMaze; // used to create a new cleaner maze
    private int[] playerLocation;

    public Dungeon() {
    	// default size + (default size + 1) created to make the inbetweens
    	maze = new char[DEFAULT_SIZE * 2 + 1][DEFAULT_SIZE * 2 + 1];
    	// odd # indexes implies rooms
    	// even # indexes implies room accessibility
    	createMaze();
    }

    public Dungeon(int rows, int columns) {
        this.maze = new char[rows * 2 + 1][columns * 2 + 1];
        
        createMaze();
    }

    private void createMaze() {
    	playerLocation = new int[2];
    	generateBaseMaze();
    	generateRandomDoors();
    	int size = checkMaxSize();
    	if(size < lowerLimitSize || size > upperLimitSize) {
    		createMaze();
    	} else {
    		System.out.println(size); // used to check size. can remove
        	createNewMaze();
        	generateEntranceAndExit();
            generatePillars();
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
    	dungeon.trimMaze();
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
    	newMaze = new HashSet<>();// used to create a new maze
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
                    	newMaze = currentMaze;
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
    private int[][] directions(int length) {
    	int[][] ret = {{length, 0}, {-length, 0}, {0, length}, {0, -length}};
    	return ret;
    }
    // recreate the maze with only the interconnected rooms
    private void createNewMaze() {
    	char[][] updateMaze = new char[maze.length][maze[0].length];
    	boolean[][] doorsPlaced = new boolean[maze.length][maze[0].length];
    	
    	for(int[] k : newMaze) { // can't use .contains on array, as it is an object
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
   // trim maze down to appropriate size
    private void trimMaze() {
    	int[] xBounds = {Integer.MAX_VALUE, Integer.MIN_VALUE};
    	int[] yBounds = {Integer.MAX_VALUE, Integer.MIN_VALUE};
    	
    	for(int[] k : newMaze) {
    		if(k[1] < xBounds[0]) xBounds[0] = k[1];
    		if(k[1] > xBounds[1]) xBounds[1] = k[1];
    		if(k[0] < yBounds[0]) yBounds[0] = k[0];
    		if(k[0] > yBounds[1]) yBounds[1] = k[0];
    	}
    	System.out.println("x bounds : [" + xBounds[0] + ", " + xBounds[1] + "]");
    	System.out.println("y bounds : [" + yBounds[0] + ", " + yBounds[1] + "]");
    	System.out.println("maze size: " + maze.length + "x" + maze[0].length);
    	int xRange = xBounds[1] - xBounds[0] + 3; // +2 for outer walls
    	int yRange = yBounds[1] - yBounds[0] + 3; // +2 for outer walls
    	char[][] updateMaze = new char[yRange][xRange];
    	System.out.println(updateMaze[0].length + " : " + updateMaze.length);
    	for(int i = 0; i < yRange; i++) {
    		for(int j = 0; j < xRange; j++) {
    			updateMaze[i][j] = maze[i + yBounds[0] - 1][j + xBounds[0] - 1];
    		}
    	}
    	System.out.println(toStringit(updateMaze));
    }
    // private helper method to check trimMaze()
    private String toStringit(char[][] input) {
    	StringBuilder sb = new StringBuilder();
        for(int i = 0; i < input.length; i++) {
        	for(int j = 0; j < input[0].length-1; j++) {
        		sb.append(input[i][j] + " ");
        	}
        	sb.append(input[i][input[0].length-1]);
        	sb.append("\n");
        }
        return sb.toString();
    }
    // helper method to see if coordinates are within bounds
    private boolean isValidCoordinates(int x, int y) {
    	return (x < maze.length && x >= 0 && y >= 0 && y < maze[0].length);
    }
    // randomly generated
    // will keep creating new entrances & exits until one is correct using BFS
    public void generateEntranceAndExit() {
    	
    }
    // randomly generated
    public void generatePillars() {
    	
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
    
    private class Room{
    	Room left, right, up, down;
    	private Room() {
    		
    	}
    	
    }
}
//public class Dungeon {
//	private char[][] maze; // 4x4 grid to test
//	private int[] playerLocation; // [x, y] coordinate
//	
//	public Dungeon() { // pass in difficulty field (feature)
//		generateMaze();
//	}
//	
//	/** 
//	 * example: "*" denotes closed, "-" deonotes open
//	 		*-*
//			|P|
//		 	*-*
//	 */
//	
//	/** 
//	    M - Multiple Items
//		X - Pit
//		i - Entrance (In)
//		O - Exit (Out)
//		V - Vision Potion
//		H - Healing Potion ▪ <space> - Empty Room
//		A, E, I, P - Pillars
//	 */
//	
//	private void generateMaze() { // randomly generate maze (feature)
//		maze = new char[9][9]; // 4x4, 5 slots for edges and relation
//	}
//	public int[] getPos() {
//		
//		return playerLocation;
//	}
//}
