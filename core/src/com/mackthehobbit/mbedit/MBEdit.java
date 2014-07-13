package com.mackthehobbit.mbedit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.mackthehobbit.mbedit.util.Render;
import com.mackthehobbit.mbedit.world.World;

public class MBEdit extends ApplicationAdapter {
	
	World world;
	
	@Override
	public void create () {
		System.out.println("create()");
		world = new World();
		world.readFromFile("C:\\Users\\Mack\\Downloads\\Electronics.mbsave");
		
		Render.chunksAtlas.getTextures().first().bind();

		Gdx.gl.glClearColor(0.49f, 0.36f, 0.94f, 1);
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glCullFace(GL20.GL_CW);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_LESS);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MAG_FILTER, GL20.GL_NEAREST);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_MIN_FILTER, GL20.GL_NEAREST);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
		
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		
		world.render();
	}
	
	
}
