package model_attacks;

import model_function.DungeonCharacter;

public abstract class Attacks {
    private String attackName;
    private int attackSpeed; // converted from double to use for Timer
    protected double hitChance;
    protected int[] damageRange;
    protected boolean self;
    protected double[][] levels; // levels[x][0] is current level, [x][1] is increase amount

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
    	
    	levels = new double[3][2];
    	levels[0][1] = 3;
    	levels[1][1] = hitChance / 3;
    	
    	double val = ((damageRange[0]+ damageRange[1])/(double)2) 
    			/ 3;
    	levels[2][1] = (val < 1.0) ? 1 : val;
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
    public String[] getRandomStat() {
    	boolean nonViable = true;
    	int val = (int)(Math.random() * 3);
    	String ret[] = new String[2];
    	
    	while(nonViable) {
    		val = (int)(Math.random() * 3);
    		switch(val) {
    		case 0:
    			ret[0] = "attack speed";
    			nonViable = false;
    			break;
    		case 1:
    			if(hitChance >= 1.0) continue;
    			ret[0] = "hit chance";
    			break;
    		case 2:
    			ret[0] = "damage";
    			nonViable = false;
    			break;
    		default:
    			ret[0] = "none";
    			break;
    			}
    	}
    	ret[1] = Integer.toString(val);
    	return ret;
    }
    // String[0] is displayable, String[1] is the selected skill.
    // String[1].charAt(0) is selected skill, String[1].charAt(1) is increase
    public String[] getRandomStatAndUpgrade() { // might not NEED
    	String[] ret = new String[2];
    	return ret;
    }
    public void upgrade(char selection) {// selection = skill
    	switch (selection) {// TODO: enumerated type for stats.
    		case '0': // attack speed
    			attackSpeed -= Math.round(attackSpeed/levels[0][1]);
    		case '1': // hit chance
    			if(hitChance + levels[1][1] > 1.0) hitChance = 1.0;
    			else hitChance += levels[1][1];
    		case '2': // damage range
    			damageRange[0] += (int)levels[2][1];
    			damageRange[1] += (int)levels[2][1];
    		default:
    			break;
    	}
    }
    @Override
    public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Attack name: " + attackName + "\n");
		sb.append("attack speed: " + attackSpeed + "\n");
		sb.append("hit chance: " + hitChance + "\n");
		sb.append("damage range: [" + damageRange[0] + " to " + damageRange[1]+ "] \n");
    	return sb.toString();
    }
}