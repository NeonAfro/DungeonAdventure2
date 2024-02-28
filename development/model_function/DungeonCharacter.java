package model_function;

import java.util.LinkedList;

import model_attacks.Attacks;
import model_attacks.Hit;

public class DungeonCharacter { // mock character, will get official from SQLite
	private String name; // name of enemy
	private int maxHealthPoints;
	private int healthPoints;
	private LinkedList<Attacks> attacks;
	
	public DungeonCharacter(String name, int hp) {
		this.name = name;
		maxHealthPoints = hp;
		healthPoints = maxHealthPoints;
		createAttacks();
	}
	// mock attacks
	private void createAttacks() {
		attacks = new LinkedList<Attacks>();
		if(name.equals("Hero")) { 
			// String name, int attackSpeed, double hitChance, int[] damageRange, boolean self
			attacks.add(new Hit("Stab", 500, 1.0, new int[] {5, 10}, false));
			attacks.add(new Hit("Lunge", 330, 1.0, new int[] {0, 2}, false));
			attacks.add(new Hit("Slash", 250, 1.0, new int[] {0, 1}, false));
		}
		else {
			attacks.add(new Hit("Punch", 1000, 0.5, new int[] {5, 10}, false));
			attacks.add(new Hit("Smack", 750, 0.4, new int[] {3, 5}, false));
			attacks.add(new Hit("Bite", 500, 0.3, new int[] {2, 3}, false));
		}
	}
	public LinkedList<Attacks> getAttacks() {
		return attacks;
	}
	public String getName() {
		return name;
	}
	public int getHealth() {
		return healthPoints;
	}
	public void takeDamage(int damage) {
		healthPoints -= damage;
	}
	public void heal(int healing) {
		healthPoints += healing;
	}
	public void attack(DungeonCharacter enemy, int damage) {
		enemy.takeDamage(damage);
	}
	public boolean isAlive() {
		return healthPoints >= 0;
	}
}
