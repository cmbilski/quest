package com.quest.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.quest.utility.AssetManager;

public class Tile extends StaticTiledMapTile {

	public static final int TILE_SIZE = 32;
	private int passable;
	
	public boolean isPassable() {
		return passable == 0;
	}
	
	public Tile(TextureRegion textureRegion, int passable) {
		super(textureRegion);
		this.passable = passable;
	}

	public static final Tile GRASS_TILE = new Tile(AssetManager.getRegion(0, 0, TILE_SIZE), 0);
	public static final Tile WATER_TILE = new Tile(AssetManager.getRegion(0, 3, TILE_SIZE), 1);
	public static final Tile GRID_TILE = new Tile(AssetManager.getRegion(4, 0, TILE_SIZE), 0);
	public static final Tile MOVE_TILE = new Tile(AssetManager.getRegion(2, 4, TILE_SIZE), 0);
    public static final Tile ATTACK_TILE = new Tile(AssetManager.getRegion(3, 4, TILE_SIZE), 0);
	public static final Tile FRIENDLY_TILE = new Tile(AssetManager.getRegion(4, 4, TILE_SIZE), 0);
}
