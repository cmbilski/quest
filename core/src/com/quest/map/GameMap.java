package com.quest.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.quest.entity.Entity;
import com.quest.entity.Entity_Knight;
import com.quest.movement.MovementOption;
import com.quest.tiles.Tile;

public class GameMap {

	TiledMap map;
	Vector2 clicked;
	Entity clickedEntity;

	Entity [][] entities;
	ArrayList<Entity> playerEntities;
	ArrayList<Entity> enemyEntities;

	TiledMapTileLayer movementLayer;
	ArrayList<MovementOption> movementOptions;
	private int width, height;

	public GameMap(int size) {		
		Random rand = new Random();
		width = size; height = width;

		entities = new Entity[width][height];
		playerEntities = new ArrayList<Entity>();
		enemyEntities = new ArrayList<Entity>();

		map = new TiledMap();
		TiledMapTileLayer layer = new TiledMapTileLayer(width, height, Tile.TILE_SIZE, Tile.TILE_SIZE);
		TiledMapTileLayer gridLayer = new TiledMapTileLayer(width, height, Tile.TILE_SIZE, Tile.TILE_SIZE);
		layer.getProperties().put("width", width);
		layer.getProperties().put("height", height);

		map.getLayers().add(layer);
		map.getLayers().add(gridLayer);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Cell newCell = new Cell();
				newCell.setTile(Tile.GRASS_TILE);
				layer.setCell(i, j, newCell);

				newCell = new Cell();
				newCell.setTile(Tile.GRID_TILE);
				gridLayer.setCell(i, j, newCell);
			}
		}

		addEntity(new Entity_Knight(), 25, 25, 0);
	}

	public void addEntity(Entity entity, int x, int y, int team) {
		entity.setX(x);
		entity.setY(y);
		entities[x][y] = entity;
		if (team == 0) {
			playerEntities.add(entity);
		} else {
			enemyEntities.add(entity);
		}
	}

	public TiledMap getMap() {
		return map;
	}

	public Vector2 getClicked() {
		return clicked;
	}

	public Entity getClickedEntity() {
		return clickedEntity;
	}

	public boolean click(Vector2 coords) {
		System.out.println(coords);
		clicked = coords;
		clickedEntity = entities[(int) coords.x][(int) coords.y];
		return clickedEntity != null;
	}

	public ArrayList<Entity> getPlayerEntities() {
		return playerEntities;
	}

	public ArrayList<Entity> getEnemyEntities() {
		return enemyEntities;
	}

	public void generateMovementLayer() {
		int moveSpeed = clickedEntity.getMovementSpeed();
		generateMovementOptions();
		movementLayer = new TiledMapTileLayer(width, height, Tile.TILE_SIZE, Tile.TILE_SIZE);
		for (int i = 0; i < movementOptions.size(); i++) {
			MovementOption opt = movementOptions.get(i);
			Cell newCell = new Cell();
			newCell.setTile(Tile.MOVE_TILE);
			movementLayer.setCell(opt.x, opt.y, newCell);
		}
		map.getLayers().add(movementLayer);
	}

	private void generateMovementOptions() {
		movementOptions = new ArrayList<MovementOption>();
		ArrayList<MovementOption> optionsToExplore = new ArrayList<MovementOption>();
		MovementOption curLocation = new MovementOption(this.clickedEntity.getX(), this.clickedEntity.getX(), null, clickedEntity.getMovementSpeed());

		optionsToExplore.add(curLocation);
		while (!optionsToExplore.isEmpty()) {
			curLocation = optionsToExplore.remove(optionsToExplore.size() - 1);
			addOptions(optionsToExplore, curLocation);
			movementOptions.add(curLocation);
		}
	}

	private void addOptions(ArrayList<MovementOption> options, MovementOption point) {
		if (point.movementLeft == 0) {
			return;
		}
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
		if (point.x > 0) {
			Tile tile = (Tile) layer.getCell(point.x - 1,  point.y).getTile();
			if (tile.isPassable()) {
				MovementOption newOption = new MovementOption(point.x - 1, point.y, point, point.movementLeft - 1);
				if (!options.contains(newOption) && !movementOptions.contains(newOption)) {
					options.add(newOption);
				}
			}
		}
		if (point.y > 0) {
			Tile tile = (Tile) layer.getCell(point.x, point.y - 1).getTile();
			if (tile.isPassable()) {
				MovementOption newOption = new MovementOption(point.x, point.y - 1, point, point.movementLeft - 1);
				if (!options.contains(newOption) && !movementOptions.contains(newOption)) {
					options.add(newOption);
				}
			}
		}
		if (point.x < width - 1) {
			Tile tile = (Tile) layer.getCell(point.x + 1, point.y).getTile();
			if (tile.isPassable()) {
				MovementOption newOption = new MovementOption(point.x + 1, point.y, point, point.movementLeft - 1);
				if (!options.contains(newOption) && !movementOptions.contains(newOption)) {
					options.add(newOption);
				}
			}
		}
		if (point.y < height - 1) {
			Tile tile = (Tile) layer.getCell(point.x, point.y + 1).getTile();
			if (tile.isPassable()) {
				MovementOption newOption = new MovementOption(point.x, point.y + 1, point, point.movementLeft - 1);
				if (!options.contains(newOption) && !movementOptions.contains(newOption)) {
					options.add(newOption);
				}
			}
		}
	}

	public void destroyMovementLayer() {
		if (movementLayer != null) {
			MapLayers layers = map.getLayers();
			layers.remove(movementLayer);
			movementLayer = null;
		}
	}

	public void entityClick(Vector2 coords) {
		System.out.println(coords);
	}
}
