package com.quest.ai;

import com.quest.entity.Entity;
import com.quest.entity.EntityStates;
import com.quest.map.GameMap;
import com.quest.movement.MovementOption;
import com.quest.weapon.Weapon;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by z001kkd on 6/1/16.
 */
public class AI {

    private GameMap map;
    private ArrayList<Entity> aiEntities;
    private ArrayList<Entity> playerEntities;

//    private HashMap<Integer, Boolean> waitingForMove;
//    private HashMap<Integer, Boolean> entitiesMoved;
//    private HashMap<Integer, Boolean> entitiesFinished;


    private boolean hasMoved, waitingForMove;
    private int currentEntity;

    public AI(GameMap map) {
        this.map = map;
        this.aiEntities = map.getEnemyEntities();
        this.playerEntities = map.getPlayerEntities();

//        entitiesMoved = new HashMap<Integer, Boolean>();
//        entitiesFinished = new HashMap<Integer, Boolean>();
//        waitingForMove = new HashMap<Integer, Boolean>();
        currentEntity = 0;
        waitingForMove = hasMoved = false;
    }

    public void update(float delta) {
        if (currentEntity == aiEntities.size()) {
            map.endTurn();
            return;
        }
        Entity ent = aiEntities.get(currentEntity);

        if (!hasMoved) {
            // The entity hasn't moved yet
            if (waitingForMove) {
                // We told the entity to move and are waiting for that to happen
                if (ent.state == EntityStates.IDLE) {
                    // The entity finished moving, mark that down
                    hasMoved = true;
                }
            } else {
                // The entity has not been told to move yet
                calculateMove(ent);
            }
        } else if (!ent.hasAttacked) {
            calculateAttack(ent);
        } else {
            currentEntity++;
            hasMoved = false;
            waitingForMove = false;
        }
    }

    private void calculateAttack(Entity ent) {
        Entity closestEntity = findClosestEntity(ent, true);
        if (getDistanceBetweenEntities(ent, closestEntity) <= ent.getWeapon().getRange() * ent.getWeapon().getRange()) {
            ent.interact(closestEntity);
        } else {
            ent.hasAttacked = true;
        }
    }

    private Entity findClosestEntity(Entity ent, boolean isEnemy) {
        ArrayList<Entity> entities;
        if (isEnemy) {
            entities = playerEntities;
        } else {
            entities = new ArrayList<Entity>();
            for (Entity e: aiEntities) {
                if (e != ent) {
                    entities.add(e);
                }
            }
        }

        float minDist = getDistanceBetweenEntities(ent, entities.get(0));
        int entIndex = 0;
        for (int i = 1; i < entities.size(); i++) {
            float dist = getDistanceBetweenEntities(ent, entities.get(i));
            if (dist < minDist) {
                minDist = dist;
                entIndex = i;
            }
        }

        return entities.get(entIndex);
    }


    private void calculateMove(Entity ent) {
        Entity closestEnt = findClosestEntity(ent, ent.getWeapon().getTargetType() == Weapon.TARGET_ENEMY);

        // Find the movement option which is closest
        ArrayList<MovementOption> movementOptions = map.generateMovementOptions(ent);

        MovementOption curLocation = new MovementOption((ent.getX()), ent.getY());
        MovementOption opt = findClosestMovementOption(movementOptions, closestEnt);

        if (getDistanceBetweenEntityOption(closestEnt, curLocation) <= getDistanceBetweenEntityOption(closestEnt, opt)) {
            waitingForMove = true;
        } else {
            map.moveEntity(ent, opt);
            waitingForMove = true;
        }
    }

    private MovementOption findClosestMovementOption(ArrayList<MovementOption> movementOptions, Entity ent) {
        float minDist = getDistanceBetweenEntityOption(ent, movementOptions.get(0));
        int optIndex = 0;
        for (int i = 1; i < movementOptions.size(); i++) {
            float dist = getDistanceBetweenEntityOption(ent, movementOptions.get(i));
            if (dist < minDist) {
                minDist = dist;
                optIndex = i;
            }
        }

        return movementOptions.get(optIndex);
    }

    private float getDistanceBetweenEntityOption(Entity ent, MovementOption opt) {
        return (opt.getX() - ent.getX()) * (opt.getX() - ent.getX()) +
                (opt.getY() - ent.getY()) * (opt.getY() - ent.getY());
    }

    private float getDistanceBetweenEntities(Entity ent1, Entity ent2) {
        return (ent1.getX() - ent2.getX()) * (ent1.getX() - ent2.getX()) +
                (ent1.getY() - ent2.getY()) * (ent1.getY() - ent2.getY());
    }

}
