package viewTest;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import model_attacks.Attacks;
import model_function.DungeonCharacter;

public class BattleScenePanel extends JPanel{
	
	private static final long serialVersionUID = -5028410426405661234L;
	
	private GameFrame gameFrame;
	private DungeonCharacter player;
	private DungeonCharacter enemy;
	
	private JTextArea battleLog = new JTextArea();
	private JScrollPane scrollPane = new JScrollPane(battleLog); // scrolling backlog of battlelogs
    private JProgressBar playerHealthBar; // player HP bar
    private JProgressBar enemyHealthBar; // enemy HP bar
    
    private AttackTimer[] allAttacks; // mock attacks
	private HashMap<String, Integer> playerAttacksDamage;
	
	public BattleScenePanel(GameFrame inputFrame) {
		gameFrame = inputFrame;
	}
	public BattleScenePanel() { // used for testing.
		
	}
	
	public void initiateBattle(DungeonCharacter player, DungeonCharacter enemy) {
		this.player = player;
		this.enemy = enemy;
		setUpUI();
		startBattle();
	}
	private void setUpUI() {
		setSize(400, 300);
		setLayout(new BorderLayout());
		
		battleLog.setEditable(false);
		scrollPane.setPreferredSize(new Dimension(400, 200));
		add(scrollPane, BorderLayout.SOUTH);
		
		JPanel healthBarPanel = new JPanel();
		healthBarPanel.setLayout(new GridLayout(2, 1));
		
		playerHealthBar = new JProgressBar(0, player.getHealth());
        playerHealthBar.setValue(player.getHealth());
        playerHealthBar.setStringPainted(true);
        playerHealthBar.setString(player.getName() + " Health: " + player.getHealth());
        
        enemyHealthBar = new JProgressBar(0, enemy.getHealth());
        enemyHealthBar.setValue(enemy.getHealth());
        enemyHealthBar.setStringPainted(true);
        enemyHealthBar.setString(enemy.getName() + " Health: " + enemy.getHealth());
        
        healthBarPanel.add(playerHealthBar);
        healthBarPanel.add(enemyHealthBar);

        add(healthBarPanel, BorderLayout.NORTH);

        setVisible(true);
        
	}
	private void startBattle() {
		updateHealthBars();
		updateBattleLog("Initiating Combat...");
		playerAttacksDamage = new HashMap<>();
		
		allAttacks = new AttackTimer[player.getAttacks().size() + enemy.getAttacks().size()];
		int i = 0;
		for(Attacks playerAttack : player.getAttacks()) 
			allAttacks[i++] = new AttackTimer(playerAttack, player, enemy);
		for(Attacks enemyAttack : enemy.getAttacks())
			allAttacks[i++] = new AttackTimer(enemyAttack, enemy, player);
		// start all timers
		for(AttackTimer timer : allAttacks) {
			timer.start();
		}
		
		
	}
	private void stopBattle() {
		for(AttackTimer timer : allAttacks) {
			timer.stop();
		}
	}
	private void victory(DungeonCharacter winner) {
		stopBattle();
		updateBattleLog("\n----The " + winner.getName() + " has won!----");
		if(winner.equals(player)) {
			battleAnalysis();
			rewardScreen();
		}
		else {
			loseScreen();
		}
	}
	private void rewardScreen() {
		//remove all components, then add buttons to upgrade an attack
//		this.removeAll();
//		this.repaint();
		
		//adding three buttons for upgrade choices
		int size = player.getAttacks().size();
		int selection = (int)(Math.random() * (0 - size + 1));
		
		
//		gameFrame.getOutcome();
	}
	private void loseScreen() {
		
	}

	private void updateBattleLog(String text) {
        SwingUtilities.invokeLater(() -> {
            battleLog.append(text +"\n");
            battleLog.setCaretPosition(battleLog.getDocument().getLength());
        });
    }

	private void updateHealthBars() {
        SwingUtilities.invokeLater(() -> {
            playerHealthBar.setValue(player.getHealth());
            playerHealthBar.setString(player.getName() + " Health: " + player.getHealth());
            enemyHealthBar.setValue(enemy.getHealth());
            enemyHealthBar.setString(enemy.getName() + " Health: " + enemy.getHealth());
        });
	}
	
	private class AttackTimer{
		private Timer attackTimer;
		private DungeonCharacter attacker;
		private DungeonCharacter defender;
		
		private AttackTimer(Attacks attack, DungeonCharacter attacker, DungeonCharacter defender) {
			this.attacker = attacker;
			this.defender = defender;
			this.attackTimer = new Timer(attack.getAttackSpeed(), e -> executeAttack(attack));
		}
		private void executeAttack(Attacks attack) {
			if(!attack.isSelf()) {
				int damage = attack.executeAttack(defender);
				if(damage > 0) {
					updateHealthBars();
					updateBattleLog(attacker.getName() + " used " 
							+ attack.getName() + " and dealt " 
							+ damage + " to " + defender.getName());
					if (attacker.equals(player)) {
	                    playerAttacksDamage.put(attack.getName(), 
	                    		playerAttacksDamage.getOrDefault(attack.getName(), 0) + damage);
	                }
					if(defender.getHealth() <= 0) {
						victory(attacker);
					}
				}
			}
			else {
				attack.executeAttack(attacker);
			}
			
		}
		public void start() {
			attackTimer.start();
		}
		public void stop() {
			attackTimer.stop();
		}
	}
	 private void battleAnalysis() {
	        int totalDamage = playerAttacksDamage.values().stream().mapToInt(Integer::intValue).sum();
	        updateBattleLog("\nBattle Analysis:\n");
	        for (Entry<String, Integer> entry : playerAttacksDamage.entrySet()) {
	            double percentage = (double) entry.getValue() / totalDamage * 100;
	            updateBattleLog(String.format("%s: %.2f%% of total damage", entry.getKey(), percentage));
	        }
	    }
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame();
			BattleScenePanel bs = new BattleScenePanel();
			
			frame.add(bs);
		
			DungeonCharacter player1 = new DungeonCharacter("Hero", 100);
			DungeonCharacter enemy1 = new DungeonCharacter("Goblin", 100);
			bs.initiateBattle(player1, enemy1);
			
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);
		});
	}
	
	
	
	
	
}
