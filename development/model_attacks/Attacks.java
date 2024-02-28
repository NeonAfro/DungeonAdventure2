package model_attacks;

import model_function.DungeonCharacter;

public abstract class Attacks {
    private String attackName;
    private int attackSpeed; // converted from double to use for Timer
    protected double hitChance;
    protected int[] damageRange;
    protected boolean self;

    public Attacks(String name) {
    	attackName = name;
    	// look for the name in SQLite
    	// get from SQLite
    }
    public Attacks(String name, int attackSpeed, double hitChance,
    		int[] damageRange, boolean self) { // mock attacks w/o SQLite
    	attackName = name;
    	this.attackSpeed = attackSpeed;
    	this.hitChance = hitChance;
    	this.damageRange = damageRange;
    	this.self = self;
    }
    public int getAttackSpeed() {
    	return attackSpeed;
    }
    public int executeAttack(DungeonCharacter target) {
    	return 0;
    }
    public boolean isSelf() {
    	return self;
    }
    public String getName() {
    	return attackName;
    }
}