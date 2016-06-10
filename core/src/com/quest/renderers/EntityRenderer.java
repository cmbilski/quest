package com.quest.renderers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quest.entity.*;
import com.quest.movement.MovementOption;
import com.quest.tiles.Tile;
import com.quest.utility.AssetManager;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityRenderer {

    private HashMap<Integer, Integer> entityStates;
    private ArrayList<HashMap<Integer, Float>> animationTimes;
    private HashMap<Class, ArrayList<Animation []>> animations;

    private HashMap<Integer, Vector2> movingTo;
    private HashMap<Integer, Vector2> movingFrom;
    private HashMap<Integer, Float> previousMoveTime;

    private static final float MOVE_TIME = 1f;

    public EntityRenderer() {
        entityStates = new HashMap<Integer, Integer>();
        int [] test = new int[5];
        animationTimes = new ArrayList<HashMap<Integer, Float>>();
        animationTimes.add(new HashMap<Integer, Float>());
        animationTimes.add(new HashMap<Integer, Float>());

        animations = new HashMap<Class, ArrayList<Animation []>>();
        animations.put(Entity_Knight.class, createEntityAnimations(6, 0, 2, 2, .5f));
        animations.put(Entity_Archer.class, createEntityAnimations(6, 2, 2, 2, .5f));
        animations.put(Entity_Priest.class, createEntityAnimations(6, 4, 2, 2, .5f));
        movingTo = new HashMap<Integer, Vector2>();
        movingFrom = new HashMap<Integer, Vector2>();
        previousMoveTime = new HashMap<Integer, Float>();

    }

    private ArrayList<Animation []> createEntityAnimations(int x, int y, int states, int length, float frametime) {
        ArrayList<Animation []> animationsArray = new ArrayList<Animation []>();
        for (int i = 0; i < states; i++) {
            Animation [] animations = new Animation[2];
            animations[0] = createAnimation(x, y + i, length, frametime);
            animations[1] = createAnimation(x + length, y + i, length, frametime);
            animationsArray.add(animations);
        }
//        System.out.println(animationsArray.size());

        return animationsArray;
    }

    private Animation createAnimation(int x, int y, int length, float frameTime) {
        TextureRegion [] regions = new TextureRegion[length];
        for (int i = 0; i  < length; i++) {
            regions[i] = AssetManager.getRegion(x + i, y, Tile.TILE_SIZE);
        }

        Animation animation = new Animation(frameTime, regions);
        return animation;
    }
	
	public void render(SpriteBatch batch, Entity entity, Rectangle viewport, float delta) {
		Rectangle entityRec = new Rectangle(entity.getX() * Tile.TILE_SIZE, entity.getY() * Tile.TILE_SIZE, 
				Tile.TILE_SIZE, Tile.TILE_SIZE);

		if (viewport.overlaps(entityRec)) {
            ArrayList<Animation []> animations = this.animations.get(entity.getClass());
            int entID = entity.getID();
            int entState = entity.state;

            // Are we transitioning?
            Float time = 0f;
            if (entityStates.containsKey(entID)) {
                Integer prevState = entityStates.get(entID);
//                System.out.println(prevState + " " + entState);
                if (prevState.equals(entState)) {
                    // Just add on time
                    time = animationTimes.get(prevState).get(entID);
                    time += delta;
                    animationTimes.get(prevState).put(entID, time);
                } else {
                    // Delete old state
                    animationTimes.get(prevState).remove(entID);
                    // Create new one
                    animationTimes.get(entState).put(entID, time);
                    entityStates.put(entID, entState);
                }
            } else {
                animationTimes.get(entState).put(entID, time);
                entityStates.put(entID, entState);
            }

            float x, y;
            TextureRegion frame = animations.get(entState)[entity.getTeam()].getKeyFrame(time, true);

            if (entState != EntityStates.MOVING) {
                x = entityRec.x - viewport.x;
                y = entityRec.y - viewport.y;
            } else {
                if (!movingTo.containsKey(entID)) {
                    if (!movingFrom.containsKey(entID)) {
                        MovementOption opt = entity.getMovementTarget();
                        while (opt.getPrevious() != null) {
//                            System.out.println(opt.getNext() + " " + opt.getPrevious());
                            opt = opt.getPrevious();
                        }

                        opt.getNext().setPrevious(null);
                        movingFrom.put(entID, new Vector2(opt.getX(), opt.getY()));
                        previousMoveTime.put(entID, 0f);
                    }

                    // We need to get the last movement option on the stack
                    MovementOption opt = entity.getMovementTarget();
                    if (opt.getPrevious() == null) {
                        entity.setMovementTarget(null);
                    }

                    // Dig until we find the last one
                    while (opt.getPrevious() != null) {
                        opt = opt.getPrevious();
                    }

                    // Opt is the last movement option, the one we are moving to
                    // Break off it's nexts previous
                    // If it has a next
                    if (opt.getNext() != null) {
                        opt.getNext().setPrevious(null);
                    }
                    movingTo.put(entID, new Vector2(opt.getX(), opt.getY()));
                }

                Vector2 from = movingFrom.get(entID);
                Vector2 to = movingTo.get(entID);
                float curTime = time % this.MOVE_TIME;
                float percentMove = curTime / this.MOVE_TIME;
                boolean finished = false;

                if (previousMoveTime.get(entID) > curTime) {
                    movingFrom.put(entID, to);
                    movingTo.remove(entID);
                    percentMove = 1f;

                    if (entity.getMovementTarget() == null) {
                        finished = true;
                    }
                }

                float xDif = (to.x - from.x) * percentMove;
                float yDif = (to.y - from.y) * percentMove;
//                System.out.println(xDif + " " + yDif);

                x = ((from.x + xDif) * Tile.TILE_SIZE) - viewport.x;
                y = ((from.y + yDif) * Tile.TILE_SIZE) - viewport.y;

                previousMoveTime.put(entID, curTime);

                if (finished) {
                    entity.state = 0;
                    movingFrom.remove(entID);
                    movingTo.remove(entID);
                    previousMoveTime.remove(entID);
                }
            }

			batch.draw(frame, x, y);
		}
		
	}
	
}
