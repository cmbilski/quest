package com.quest.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.quest.map.GameMap;
import com.quest.screens.Screen_Quest;
import com.quest.tiles.Tile;

public class Input_Quest_Entity implements InputProcessor {

	private Input_Quest input;
	private GameMap map;
	private Screen_Quest screen;
	
	public Input_Quest_Entity(Input_Quest input, GameMap map, Screen_Quest screen) {
		this.input = input;
		this.map = map;
		this.screen = screen;
		
		map.generateEntityOptions();
	}
	
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
		if (button == 1) {
			map.clearClicked();
			Gdx.input.setInputProcessor(input);
		} else if (button == 0) {
			if (map.entityClick(screen.getGameCoordsFromClick(screenX, screenY).scl(1f / Tile.TILE_SIZE))) {
				map.clearClicked();
				Gdx.input.setInputProcessor(input);
			}
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
