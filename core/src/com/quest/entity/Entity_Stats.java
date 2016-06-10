package com.quest.entity;

import java.util.HashMap;

/**
 * Created by z001kkd on 6/1/16.
 */
public class Entity_Stats {

    protected static final int CUR_MOVEMENT_SPEED = 0;
    protected static final int OG_MOVEMENT_SPEED = 1;
    protected static final int CUR_ATTACK_RANGE = 2;
    protected static final int CUR_POWER  = 3;
    protected static final int OG_POWER = 4;
    protected static final int OG_HEALTH = 5;
    protected static final int CUR_HEALTH = 6;


    private static final HashMap<Class, int []> entityStats;
    static
    {
        entityStats = new HashMap<Class, int[]>();
        entityStats.put(Entity_Knight.class, new int[]{3, 3, 1, 1, 1, 5, 5});
        entityStats.put(Entity_Archer.class, new int[]{2, 2, 2, 1, 1, 5, 5});
        entityStats.put(Entity_Priest.class, new int[]{2, 2, 2, 1, 1, 5, 5});
    }

    private int [] stats;

    public Entity_Stats(Class entClass) {
        stats = entityStats.get(entClass).clone();
    }

    public int getOGMovementSpeed() {
        return stats[OG_MOVEMENT_SPEED];
    }

    public int getCurMovementSpeed() {
        return stats[CUR_MOVEMENT_SPEED];
    }

    public void setCurrentMovementSpeed(int currentMovementSpeed) {
        stats[CUR_MOVEMENT_SPEED] = currentMovementSpeed;
    }

    public int getCurPower() {
        return stats[CUR_POWER];
    }

    public void modifyHealth(int mod) {
        stats[CUR_HEALTH] -= mod;
        if (stats[CUR_HEALTH] < 0) {
            stats[CUR_HEALTH] = 0;
        } else if (stats[CUR_HEALTH] > stats[OG_HEALTH]) {
            stats[CUR_HEALTH] = stats[OG_HEALTH];
        }
        System.out.println("Health remaining: " + stats[CUR_HEALTH]);
    }

    public int getCurHealth() {
        return stats[CUR_HEALTH];
    }

}
