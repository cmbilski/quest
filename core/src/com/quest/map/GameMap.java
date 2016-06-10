package com.quest.map;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.quest.ai.AI;
import com.quest.entity.*;
import com.quest.movement.MovementOption;
import com.quest.tiles.Tile;
import com.quest.weapon.Weapon;

public class GameMap {

	TiledMap map;
	Vector2 clicked;
	Entity clickedEntity;

	Entity [][] entities;
	ArrayList<Entity> playerEntities;
	ArrayList<Entity> enemyEntities;
	ArrayList<Entity> attackOptions;

	TiledMapTileLayer uiLayer;
	ArrayList<MovementOption> movementOptions;
	private int width, height;
	private int turn;
    private boolean canClick;

    private AI enemyAI;

	public GameMap(int size) {
        canClick = true;

		Random rand = new Random();
		width = size; height = width;

		entities = new Entity[width][height];
		playerEntities = new ArrayList<Entity>();
		enemyEntities = new ArrayList<Entity>();
		turn = 0;

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
				if (rand.nextFloat() < .9) {
					newCell.setTile(Tile.GRASS_TILE);
				} else {
					newCell.setTile(Tile.WATER_TILE);
				}
				layer.setCell(i, j, newCell);

				newCell = new Cell();
				newCell.setTile(Tile.GRID_TILE);
				gridLayer.setCell(i, j, newCell);
			}
		}

		addEntity(new Entity_Knight(), 25, 25, 0);
		addEntity(new Entity_Archer(), 20, 20, 1);
        addEntity(new Entity_Priest(), 21, 21, 1);
        addEntity(new Entity_Knight(), 22, 22, 1);
		addEntity(new Entity_Archer(), 25, 24, 0);
        addEntity(new Entity_Priest(), 25, 23, 0);
	}

	public void addEntity(Entity entity, int x, int y, int team) {
		entity.setX(x);
		entity.setY(y);
		entity.setTeam(team);
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
        if (!canClick) {
            return false;
        }
		System.out.println(coords);
		clicked = coords;
		clickedEntity = entities[(int) coords.x][(int) coords.y];
		if (clickedEntity != null && clickedEntity.getTeam() == 0) {
			if (clickedEntity.getStats().getCurMovementSpeed() == 0 && clickedEntity.hasAttacked) {
				return false;
			}
			return true;
		}
		return false;
	}

	public ArrayList<Entity> getPlayerEntities() {
		return playerEntities;
	}

	public ArrayList<Entity> getEnemyEntities() {
		return enemyEntities;
	}

	public void generateEntityOptions() {
		int moveSpeed = clickedEntity.getStats().getCurMovementSpeed();
		attackOptions = new ArrayList<Entity>();
		movementOptions = generateMovementOptions(clickedEntity);
		if (!clickedEntity.hasAttacked) {
            attackOptions = generateAttackOptions(clickedEntity);
		}
		uiLayer = new TiledMapTileLayer(width, height, Tile.TILE_SIZE, Tile.TILE_SIZE);
		for (int i = 0; i < movementOptions.size(); i++) {
			MovementOption opt = movementOptions.get(i);
			Cell newCell = new Cell();
			newCell.setTile(Tile.MOVE_TILE);
			uiLayer.setCell(opt.x, opt.y, newCell);
		}
        Tile entityTile;
        if (clickedEntity.getWeapon().getTargetType() == Weapon.TARGET_ENEMY) {
            entityTile = Tile.ATTACK_TILE;
        } else {
            entityTile = Tile.FRIENDLY_TILE;
        }
		for (int i = 0; i < attackOptions.size(); i++) {
			Entity ent = attackOptions.get(i);
			Cell newCell = new Cell();
			newCell.setTile(entityTile);
			uiLayer.setCell(ent.getX(), ent.getY(), newCell);
		}
		map.getLayers().add(uiLayer);
	}

	public ArrayList<Entity> generateAttackOptions(Entity entity) {
        ArrayList<Entity> entities;
        if (entity.getWeapon().getTargetType() == Weapon.TARGET_ENEMY) {
            entities = enemyEntities;
        } else {
            entities = playerEntities;
        }
		ArrayList<Entity> attackOptions = new ArrayList<Entity>();
		for (int i = 0; i < entities.size(); i++) {
			if (entity.getWeapon().inAttackRange(entities.get(i))) {
				attackOptions.add(entities.get(i));
			}
		}

		return attackOptions;
	}

	public ArrayList<MovementOption> generateMovementOptions(Entity ent) {
		ArrayList<MovementOption> movementOptions = new ArrayList<MovementOption>();
		ArrayList<MovementOption> optionsToExplore = new ArrayList<MovementOption>();
		MovementOption startLocation = new MovementOption(ent.getX(), ent.getY(), null,
				ent.getStats().getCurMovementSpeed());
		MovementOption curLocation = startLocation;

		optionsToExplore.add(curLocation);
		while (!optionsToExplore.isEmpty()) {
			curLocation = optionsToExplore.remove(optionsToExplore.size() - 1);
			addOptions(movementOptions, optionsToExplore, curLocation);
			movementOptions.add(curLocation);
		}
		movementOptions.remove(startLocation);
        return movementOptions;
	}

	private void addOptions(ArrayList<MovementOption> movementOptions, ArrayList<MovementOption> options, MovementOption point) {
		if (point.movementLeft == 0) {
			return;
		}
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
		if (point.x > 0) {
			Tile tile = (Tile) layer.getCell(point.x - 1,  point.y).getTile();
			if (tile.isPassable() && entities[point.x - 1][point.y] == null) {
				MovementOption newOption = new MovementOption(point.x - 1, point.y, point, point.movementLeft - 1);
				if (!options.contains(newOption) && !movementOptions.contains(newOption)) {
					options.add(newOption);
				}
			}
		}
		if (point.y > 0) {
			Tile tile = (Tile) layer.getCell(point.x, point.y - 1).getTile();
			if (tile.isPassable() && entities[point.x][point.y - 1] == null) {
				MovementOption newOption = new MovementOption(point.x, point.y - 1, point, point.movementLeft - 1);
				if (!options.contains(newOption) && !movementOptions.contains(newOption)) {
					options.add(newOption);
				}
			}
		}
		if (point.x < width - 1) {
			Tile tile = (Tile) layer.getCell(point.x + 1, point.y).getTile();
			if (tile.isPassable() && entities[point.x + 1][point.y] == null) {
				MovementOption newOption = new MovementOption(point.x + 1, point.y, point, point.movementLeft - 1);
				if (!options.contains(newOption) && !movementOptions.contains(newOption)) {
					options.add(newOption);
				}
			}
		}
		if (point.y < height - 1) {
			Tile tile = (Tile) layer.getCell(point.x, point.y + 1).getTile();
			if (tile.isPassable() && entities[point.x][point.y + 1] == null) {
				MovementOption newOption = new MovementOption(point.x, point.y + 1, point, point.movementLeft - 1);
				if (!options.contains(newOption) && !movementOptions.contains(newOption)) {
					options.add(newOption);
				}
			}
		}
	}

	public void destroyMovementLayer() {
		if (uiLayer != null) {
			MapLayers layers = map.getLayers();
			layers.remove(uiLayer);
			uiLayer = null;
		}
		movementOptions = new ArrayList<MovementOption>();
	}

	public boolean entityClick(Vector2 coords) {
		MovementOption move = new MovementOption((int) coords.x, (int) coords.y, null, 0);
		if (movementOptions.contains(move)) {
			int index = movementOptions.indexOf(move);
			move = movementOptions.get(index);

			moveEntity(this.clickedEntity, move);
			return true;
		}

		for (int i = 0; i < attackOptions.size(); i++) {
			Entity attackOption = attackOptions.get(i);
			if (attackOption.getX() == (int) coords.x && attackOption.getY() == (int) coords.y) {
				clickedEntity.interact(attackOption);
				clickedEntity.hasAttacked = true;
                return true;
			}
		}

		return false;
	}

	public void moveEntity(Entity ent, MovementOption newLoc) {
        MovementOption opt = newLoc;
        while (opt.getPrevious() != null) {
            opt.getPrevious().setNext(opt);
            opt = opt.getPrevious();
        }

		this.entities[ent.getX()][ent.getY()] = null;
		ent.setX(newLoc.getX()); ent.setY(newLoc.getY());
		this.entities[ent.getX()][ent.getY()] = ent;
		ent.getStats().setCurrentMovementSpeed(newLoc.movementLeft);
        ent.moveTo(newLoc);
	}

	public void clearClicked() {
		this.clicked = null;
		this.clickedEntity = null;
		destroyMovementLayer();
		this.attackOptions = null;
		tryEndTurn();
	}

	private void tryEndTurn() {
		boolean endTurn = true;
		if (turn == 0) {
			for (int i = 0; i < playerEntities.size(); i++) {
				Entity ent = playerEntities.get(i);
				if (ent.getStats().getCurMovementSpeed() != 0) {
					endTurn = false;
					break;
				}
				if (!ent.hasAttacked) {
					ArrayList<Entity> options = generateAttackOptions(ent);
					if (!options.isEmpty()) {
						endTurn = false;
						break;
					}
				}
                if (ent.state != EntityStates.IDLE) {
                    endTurn = false;
                    break;
                }
			}
		}

		if (endTurn) {
			endTurn();
		}
	}

	public void endTurn() {
		System.out.println("Ending turn");
		turn = (turn + 1) % 2;
        System.out.println("Turn " + turn);
		ArrayList<Entity> entities = null;
		if (turn == 0) {
			entities = playerEntities;
            enemyAI = null;
            canClick = true;
		} else {
			entities = enemyEntities;
            enemyAI = new AI(this);
            canClick = false;
		}

		for (int i = 0; i < entities.size(); i++) {
			Entity ent = entities.get(i);
			ent.getStats().setCurrentMovementSpeed(ent.getStats().getOGMovementSpeed());
			ent.hasAttacked = false;
		}
	}

	public int getTurn() {
		return turn;
	}

    public void update(float delta) {
        if (turn == 0) {
            tryEndTurn();
        } else if (turn == 1 && enemyAI != null) {
            enemyAI.update(delta);
        }
    }
}
