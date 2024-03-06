package viewTest;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import control.DungeonAdventure;

public class GameFrame extends JFrame{

	private static final long serialVersionUID = 1216688833581782134L;
	
	
	private DungeonAdventure dungeonAdventure;
	// JPanel and CardLayout utilized to swap panels
	// - useful for swapping between Maze, Room, BattleScene panel
	private CardLayout cardLayout = new CardLayout();
	private JPanel containerPanel = new JPanel();
	// individual JPanels in cardLayout.
	private ClickableMap roomPanel;
	private BattleScenePanel battleScenePanel;
	private MapPanel mapPanel;
	private VisiblePanel[][] visibleMaze;
	
	public GameFrame(DungeonAdventure dungeonAdventure) {
		this.dungeonAdventure = dungeonAdventure;
		
		// initial configuration of this frame
		
		setTitle("Dungeon Adventure");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// create panels
		roomPanel = new ClickableMap(this.dungeonAdventure);
		battleScenePanel = new BattleScenePanel(this);
		mapPanel = new MapPanel();
		
		// initial setup of panels and cardlayout
		containerPanel.setLayout(cardLayout);
		containerPanel.add(roomPanel, "Room");
		containerPanel.add(battleScenePanel, "Battle");
		
		// Setting up mapPanel
		// gridLayout of row x col from dungeon
		containerPanel.add(mapPanel, "Map");
		int[] size = dungeonAdventure.getMazeSize();
		mapPanel.setLayout(new GridLayout(size[0], size[1]));
		// create visibleMaze based on getMaze()
		// utilize updateMaze to keep JPanel visibleMaze updated to latest visibleMaze
		visibleMaze = new VisiblePanel[size[0]][size[1]];
		// retrieve maze from dungeon
		char[][] maze = dungeonAdventure.getMaze();
		for(int i = 0; i < size[0]; i++) {
			for(int j = 0; j < size[1]; j++) {
				visibleMaze[i][j] = new VisiblePanel(maze[i][j]);
				mapPanel.add(visibleMaze[i][j]);
			}
		}
		SwingUtilities.invokeLater(() -> { // adding mouse click to switch to room view
			mapPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					switchToRoom();
				}
			});
		});
		setSize(300, 300);
		add(containerPanel);
		cardLayout.show(containerPanel, "Room");
		// ending configuration
		setLocationRelativeTo(null);
		setVisible(true);
		requestFocus();
	}
	public void setRoom(boolean[] doors, String[] monsters) {
		roomPanel.setRoom(doors, monsters);
	}
	
	public void switchToBattle() {
		SwingUtilities.invokeLater(() -> {
			setTitle("Battle Scene");
			setSize(400, 300);
			repaint();
			cardLayout.show(containerPanel, "Battle");
			battleScenePanel.initiateBattle(dungeonAdventure.getPlayer(), 
					dungeonAdventure.getEnemy()); // mock enemy and mock player
		});
	}
	public void getOutcome() {
		
	}
	public void switchToRoom() {
		SwingUtilities.invokeLater(() -> {
			setTitle("Dungeon Adventure");
			setSize(300, 300);
			repaint();
			cardLayout.show(containerPanel, "Room");
		});
	}
	// enumerators for characters.
	public void switchToMap() {
		SwingUtilities.invokeLater(() -> {
			setTitle("Map");
			setSize(300, 300);
			repaint();
			// update visibleMaze
			boolean[][] maze = dungeonAdventure.getVisibleMaze();
			for(int i = 0; i < visibleMaze.length; i++) {
				for(int j = 0; j < visibleMaze[0].length; j++) {
					if(maze[i][j]) visibleMaze[i][j].setVisibleColor(true);
					else visibleMaze[i][j].setVisibleColor(false);
				}
			}
			cardLayout.show(containerPanel, "Map");
		});
	}
	public class VisiblePanel extends JPanel{

		private static final long serialVersionUID = 8278072992557424397L;
		private Color color;
		
		private VisiblePanel(char type) {
			setColor(type);
			this.setBackground(color);
		}
		private void setVisibleColor(boolean visible) {
			if(visible) this.setBackground(color);
			else this.setBackground(Color.black);
		}
		// Undiscovered = black
		// Wall = black
		// Door = gray
		private void setColor(char type) {
			switch(type){
			case '|' :
				color = Color.gray;
				break;
			case '-':
				color = Color.gray;
				break;
			case '@':
				color = Color.black;
				break;
			case '*':
				color = Color.black;
				break;
			default:
				color = Color.green;
				break;
			}
		}
	}
}
