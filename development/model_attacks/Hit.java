package model_attacks;

import model_function.DungeonCharacter;

public class Hit extends Attacks{

	public Hit(String name) {
		super(name);
		// parent class will get data from SQLite
		self = false;
	}
	public Hit(String name, int attackSpeed, double hitChance,
    		int[] damageRange, boolean self) {
		super(name, attackSpeed, hitChance, damageRange, self);
	}
	@Override
	public int executeAttack(DungeonCharacter target) {
		if(Math.random() <= hitChance) {
			// roll from damage range to get damage
			int damage = (damageRange[1]- damageRange[0] + 1);
			damage = (int)(Math.random() * damage) + damageRange[0];
			
			target.takeDamage(damage);
			
			return damage;
		}
		else return 0;
	}

}
