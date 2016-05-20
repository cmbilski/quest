package com.quest.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.quest.map.GameMap;
import com.quest.screens.Screen_Quest;
import com.quest.tiles.Tile;

public class Input_Quest implements InputProcessor {

	private Screen_Quest screen;
	private GameMap map;

	public Input_Quest(Screen_Quest screenQuest, GameMap map) {
		this.screen = screenQuest;
		this.map = map;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenY = Gdx.graphics.getHeight() - screenY;
		Vector2 coords = screen.getGameCoordsFromClick(screenX, screenY);	
		if (map.click(coords.scl(1f / Tile.TILE_SIZE))) {
			Gdx.input.setInputProcessor(new Input_Quest_Entity(this, map, screen));
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
