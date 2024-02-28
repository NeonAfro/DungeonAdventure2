package model_attacks;

import model_function.DungeonCharacter;

public class Buff extends Attacks{

	public Buff(String name) {
		super(name);
		
		self = true;
	}
	@Override
	public int executeAttack(DungeonCharacter target) {
		target.heal(damageRange[0]);
		return damageRange[0];
	}
}
