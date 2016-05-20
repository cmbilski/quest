package com.quest.renderers;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.quest.entity.Entity;
import com.quest.entity.Entity_Knight;
import com.quest.tiles.Tile;
import com.quest.utility.AssetManager;

public class EntityRenderer {

	private static final Sprite KNIGHT_SPRITE = new Sprite(AssetManager.getRegion(0, 5, Tile.TILE_SIZE));
	
	public void render(SpriteBatch batch, Entity entity, Rectangle viewport) {
		Rectangle entityRec = new Rectangle(entity.getX() * Tile.TILE_SIZE, entity.getY() * Tile.TILE_SIZE, 
				Tile.TILE_SIZE, Tile.TILE_SIZE);
		
		if (viewport.overlaps(entityRec)) {
			Sprite sprite = null;
			if (entity instanceof Entity_Knight) {
				sprite = KNIGHT_SPRITE;
			}
			
			sprite.setPosition(entityRec.x - viewport.x, entityRec.y - viewport.y);
			sprite.draw(batch);
		}
		
	}
	
}
