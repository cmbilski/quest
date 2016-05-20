package com.quest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.quest.screens.Screen_Quest;

public class Quest extends ApplicationAdapter {
	
	private Screen screen;
	
	@Override
	public void create () {
		screen = new Screen_Quest();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		screen.render(Gdx.graphics.getDeltaTime());
	}
}
