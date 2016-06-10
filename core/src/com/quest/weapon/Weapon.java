package com.quest.weapon;

import com.quest.entity.Entity;
import com.quest.entity.Entity_Archer;
import com.quest.entity.Entity_Knight;
import com.quest.entity.Entity_Priest;

import java.util.HashMap;

/**
 * Created by z001kkd on 6/1/16.
 */
public class Weapon {

    public static final int TARGET_ENEMY = 0;
    public static final int TARGET_FRIENDLY = 1;

    private int [] stats;
    private Entity entity;
    private static final HashMap<Class, int []> weapons;
    static
    {
        weapons = new HashMap<Class, int[]>();
        weapons.put(Entity_Knight.class, new int[]{TARGET_ENEMY, 1});
        weapons.put(Entity_Archer.class, new int[]{TARGET_ENEMY, 2});
        weapons.put(Entity_Priest.class, new int[]{TARGET_FRIENDLY, 2});
    }

    public Weapon(Entity entity) {
        stats = weapons.get(entity.getClass()).clone();
        this.entity = entity;
    }


    public int getTargetType() {
        return stats[0];
    }
    public int getRange() {
        return stats[1];
    }

    public void interact(Entity targetEntity) {
        int modifier = this.stats[1] == TARGET_ENEMY ? 1 : 0;
        targetEntity.getStats().modifyHealth(modifier * entity.getStats().getCurPower());
    }

    public boolean inAttackRange(Entity targetEntity) {
        float dist = ((targetEntity.getX() - entity.getX()) * (targetEntity.getX() - entity.getX())) +
                ((targetEntity.getY() - entity.getY()) * (targetEntity.getY() - entity.getY()));
        if (dist <= getRange() * getRange()) {
            return true;
        }
        return false;
    }
}
