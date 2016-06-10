package com.quest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quest.entity.Entity;
import com.quest.input.Input_Quest;
import com.quest.map.GameMap;
import com.quest.renderers.EntityRenderer;
import com.quest.tiles.Tile;

public class Screen_Quest implements Screen {

	private GameMap map;
	public static final int MAP_SIZE = 50;
	public static final int TILES_TO_RENDER = 20;
	
	private SpriteBatch batch;
	
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer mapRenderer;
	private EntityRenderer entityRenderer;
	
	private Texture clickedGridTexture = new Texture(Gdx.files.internal("sprites/click.png"));
	private int lastTurn;
	private float turnSpriteRender;
	private Texture [] turnSprites = {
			new Texture(Gdx.files.internal("sprites/player1_turn.png")),
			new Texture(Gdx.files.internal("sprites/player2_turn.png"))};

	public Screen_Quest() {
		map = new GameMap(MAP_SIZE);
		
		camera = new OrthographicCamera(TILES_TO_RENDER * Tile.TILE_SIZE, TILES_TO_RENDER * Tile.TILE_SIZE);
		camera.position.x = MAP_SIZE / 2.0f * Tile.TILE_SIZE;
		camera.position.y = MAP_SIZE / 2.0f * Tile.TILE_SIZE;
		// bigger y is more up
//		camera.position.x = MAP_SIZE * Tile.TILE_SIZE;
		camera.update();
		mapRenderer = new OrthogonalTiledMapRenderer(map.getMap());
		this.entityRenderer = new EntityRenderer();
	
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, TILES_TO_RENDER * Tile.TILE_SIZE, TILES_TO_RENDER * Tile.TILE_SIZE);
		
		Input_Quest input = new Input_Quest(this, map);
		Gdx.input.setInputProcessor(input);

		lastTurn = -1;
		turnSpriteRender = -1;
	}

	public Vector2 getGameCoordsFromClick(int x, int y) {
		// Center of the camera is camera.position
		int xOffset = (int) (x - TILES_TO_RENDER * Tile.TILE_SIZE / 2.0);
		int yOffset = (int) (y - TILES_TO_RENDER * Tile.TILE_SIZE / 2.0);

		return new Vector2(camera.position.x + xOffset, camera.position.y + yOffset);
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		map.update(delta);

		mapRenderer.setView(camera);
		mapRenderer.render();

		batch.begin();
		renderEntities(delta);
		
		Vector2 clicked;
		if ((clicked= map.getClicked()) != null) {
			float adjX = (float) (Math.floor(clicked.x) * Tile.TILE_SIZE - (camera.position.x - TILES_TO_RENDER * Tile.TILE_SIZE / 2f));
			float adjY = (float) (Math.floor(clicked.y) * Tile.TILE_SIZE - (camera.position.y - TILES_TO_RENDER * Tile.TILE_SIZE / 2f));
			batch.draw(clickedGridTexture, adjX, adjY);
		}

		int curTurn = map.getTurn();
		if (curTurn != lastTurn) {
			lastTurn = curTurn;
			turnSpriteRender = 1.5f;
			System.out.println("Turn swap");
		}

		if (turnSpriteRender > 0) {
			batch.draw(this.turnSprites[lastTurn], Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
			turnSpriteRender -= delta;
		}

		batch.end();
	}

	private void renderEntities(float delta) {
		Rectangle viewport = getCameraRectangle();
		
		for (Entity e: map.getPlayerEntities()) {
			entityRenderer.render(batch, e, viewport, delta);
		}
		for (Entity e: map.getEnemyEntities()) {
			entityRenderer.render(batch, e, viewport, delta);
		}
	}
	
	private Rectangle getCameraRectangle() {
		Rectangle rect = new Rectangle(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2, 
				camera.viewportWidth, camera.viewportHeight);
		return rect;
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}
