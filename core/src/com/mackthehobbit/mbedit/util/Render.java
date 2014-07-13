package com.mackthehobbit.mbedit.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Render {
	
	public static ShaderProgram chunksShader = Render.createShader("Chunk");
	public static TextureAtlas chunksAtlas = new TextureAtlas(Gdx.files.internal("Terrain.pack"));
	
	public static ShaderProgram createShader(String name) {
		ShaderProgram.pedantic = false;
		ShaderProgram shader = new ShaderProgram(Gdx.files.internal(name + ".vsh"), Gdx.files.internal(name + ".fsh"));
		String log = shader.getLog();
		if(!shader.isCompiled())
			throw new GdxRuntimeException(log);
		if(log != null && log.length() > 0)
			System.out.println(String.format("Shader '%s': %s", name, log));
		return shader;
	}
	
}
