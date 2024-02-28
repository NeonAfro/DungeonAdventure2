package model_function;

import javax.swing.JOptionPane;

public class Creature {
	private int hp;
	
	
	public Creature() {
		String name = JOptionPane.showInputDialog("Enter Hero");
		JOptionPane.showMessageDialog(null, "Hero: " + name);
		// ask which hero the player will use
		
	}
}
