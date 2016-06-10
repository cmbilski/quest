package com.quest.entity;

import com.quest.movement.MovementOption;
import com.quest.weapon.Weapon;

public class Entity {

    private static int counter = 0;

    private int id;
    private int team;
//	protected int [] stats;
	protected static final int CUR_MOVEMENT_SPEED = 0;
	protected static final int OG_MOVEMENT_SPEED = 1;
    protected static final int CUR_ATTACK_RANGE = 2;
    protected static final int CUR_POWER  = 3;
    protected static final int OG_POWER = 4;
    protected static final int OG_HEALTH = 5;
    protected static final int CUR_HEALTH = 6;

    public boolean hasAttacked;

    protected Entity_Stats stats;
    protected Weapon weapon;

	public int state;
    private MovementOption movementOption;
	// Remember, these are ALWAYS in tiles
	private int x, y;
	
	public Entity() {
        id = counter++;
        team = 0;
        stats = new Entity_Stats(this.getClass());
        weapon = new Weapon(this);
//		stats = new int[7];
//		state = EntityStates.IDLE;
//		stats[CUR_MOVEMENT_SPEED] = 0;
//		stats[OG_MOVEMENT_SPEED] = 0;
//        stats[CUR_ATTACK_RANGE] = 1;
//        stats[CUR_POWER] = 1;
//        stats[OG_POWER] = 1;
        hasAttacked = false;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public void interact(Entity ent) {
        ent.stats.modifyHealth(stats.getCurPower());
        hasAttacked = true;
    }

    public int getID() {
        return id;
    }

    public void moveTo(MovementOption option) {
        this.movementOption = option;
        this.state = EntityStates.MOVING;
    }

    public MovementOption getMovementTarget() {
        return this.movementOption;
    }

    public void setMovementTarget(MovementOption opt) {
        this.movementOption = opt;
    }

    public boolean isDead() {
        return stats.getCurHealth() > 0;
    }

    public Entity_Stats getStats() {
        return stats;
    }

    public Weapon getWeapon() {
        return weapon;
    }
}
