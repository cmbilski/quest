package com.quest.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {

	public static final Texture TILESET = new Texture(Gdx.files.internal("sprites/toen_edge.png"));
	
	public static TextureRegion getRegion(int x, int y, int tileSize) {
		// We want to offset by 1 starting at 0
		// And then add on tileSizes
		int xCoord = 1 + (x * 2) + (x * tileSize);
		int yCoord = 1 + (y * 2) + (y * tileSize);
		return new TextureRegion(TILESET, xCoord, yCoord, tileSize, tileSize);
	}
	
}
