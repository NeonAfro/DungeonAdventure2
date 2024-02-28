package viewTest;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import control.DungeonAdventure;
import model_function.Dungeon;

public class GameFrame extends JFrame{

	private static final long serialVersionUID = 1216688833581782134L;
	
	
	private DungeonAdventure dungeonAdventure;
	private Dungeon dungeon;
	// JPanel and CardLayout utilized to swap panels
	// - useful for swapping between Maze, Room, BattleScene panel
	private CardLayout cardLayout = new CardLayout();
	private JPanel containerPanel = new JPanel();
	// individual JPanels in cardLayout.
	private ClickableMap roomPanel;
	private BattleScenePanel battleScenePanel;
	private MapPanel mapPanel;
	
	public GameFrame(DungeonAdventure dungeonAdventure, Dungeon dungeon) {
		this.dungeon = dungeon;
		this.dungeonAdventure = dungeonAdventure;
		
		// initial configuration of this frame
		
		setTitle("Dungeon Adventure");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// create panels
		roomPanel = new ClickableMap(dungeonAdventure, dungeon);
		battleScenePanel = new BattleScenePanel(this);
		mapPanel = new MapPanel();
		
		// initial setup of panels and cardlayout
		containerPanel.setLayout(cardLayout);
		containerPanel.add(roomPanel, "Room");
		containerPanel.add(battleScenePanel, "Battle");
		containerPanel.add(mapPanel, "Map");
		
		setSize(300, 385);
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
		setTitle("Battle Scene");
		cardLayout.show(containerPanel, "Battle");
		battleScenePanel.initiateBattle(dungeonAdventure.getPlayer(), 
				dungeonAdventure.getEnemy()); // mock enemy and mock player
	}
	public void getOutcome() {
		
	}
	public void switchToRoom() {
		setTitle("Dungeon Adventure");
		cardLayout.show(containerPanel, "Room");
	}
	
}
