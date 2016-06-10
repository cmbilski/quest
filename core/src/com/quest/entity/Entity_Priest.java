package com.quest.entity;

/**
 * Created by z001kkd on 5/24/16.
 */
public class Entity_Priest extends Entity {
    public void interact(Entity ent) {
        ent.stats.modifyHealth(stats.getCurPower() * -1);
        hasAttacked = true;
    }
}
