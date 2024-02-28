package model_attacks;

import model_function.DungeonCharacter;

public class MultiHit extends Attacks{
	
	private int numberOfHits;
	
	public MultiHit(String name) {
		super(name);
		// parent class will get data from SQLite
		// get numberOfHits from SQLite
	}
	
	public void executeAttack(DungeonCharacter target) {
		for(int i = 0; i < numberOfHits; i++) {
			super.executeAttack(target);
		}
	}
}
