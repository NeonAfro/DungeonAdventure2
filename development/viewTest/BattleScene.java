package viewTest;
import javax.swing.*;

import model_function.Character;

import java.awt.*;

public class BattleScene extends JFrame {
    private Character player;
    private Character enemy;
    private JTextArea battleLog = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane(battleLog);
    private JProgressBar playerHealthBar;
    private JProgressBar enemyHealthBar;

    public BattleScene(Character player, Character enemy) {
        this.player = player;
        this.enemy = enemy;
        setupUI();
        startBattle();
    }

    private void setupUI() {
        setTitle("Battle Scene");
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

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void startBattle() {
        new Thread(() -> {
            while (player.isAlive() && enemy.isAlive()) {
                player.attack(enemy);
                updateHealthBars();
                updateBattleLog(player.getName() + " attacks " + enemy.getName() + "!\n");
                if (!enemy.isAlive()) {
                    updateBattleLog(enemy.getName() + " is defeated!\n");
                    break;
                }

                enemy.attack(player);
                updateHealthBars();
                updateBattleLog(enemy.getName() + " attacks " + player.getName() + "!\n");
                if (!player.isAlive()) {
                    updateBattleLog(player.getName() + " is defeated!\n");
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateBattleLog(String text) {
        SwingUtilities.invokeLater(() -> {
            battleLog.append(text);
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

    public static void main(String[] args) {
        // Example usage
        Character player = new Character("Hero", 1000, 20);
        Character enemy = new Character("Goblin", 500, 15);
        new BattleScene(player, enemy);
    }
}
