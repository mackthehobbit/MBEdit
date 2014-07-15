package com.mackthehobbit.mbedit.world;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.mackthehobbit.mbedit.input.CameraControl;
import com.mackthehobbit.mbedit.input.DesktopCameraControl;
import com.mackthehobbit.mbedit.util.Point;
import com.mackthehobbit.mbedit.util.Render;

public class World {
	
	public static final int VIEW_DISTANCE = 4;
	
	private long seed, gameMode;
	private String biomesXml;
	public Point spawn;
	public CameraControl control;
	
	private Map<Point, Chunk> loadedChunks;
	
	public World() {
		loadedChunks = new HashMap<Point, Chunk>();
		if(Gdx.app.getType() == ApplicationType.Desktop) control = new DesktopCameraControl();
	}
	
	public void render() {
		control.update();
		
		Camera cam = new PerspectiveCamera(80, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.near = 0.1f;
		cam.rotate(control.pitch, 1, 0, 0);
		cam.rotate(control.yaw, 0, 1, 0);
		cam.translate(control.x, control.y, control.z);
		cam.update();
		
		Render.chunksShader.begin();
		Render.chunksAtlas.getTextures().first().bind();
		
		Render.chunksShader.setUniformMatrix("u_projTrans", cam.combined);
		Render.chunksShader.setUniformi("u_texture", 0);
		
		int cameraChunkX = ((int) control.x & 0xF);
		int cameraChunkY = ((int) control.y & 0xF);
		int cameraChunkZ = ((int) control.z & 0xF);
		
		for(int x = cameraChunkX - VIEW_DISTANCE; x <= cameraChunkX + VIEW_DISTANCE; x++) {
			for(int y = cameraChunkY - VIEW_DISTANCE; y <= cameraChunkY + VIEW_DISTANCE; y++) {
				for(int z = cameraChunkZ - VIEW_DISTANCE; z <= cameraChunkZ + VIEW_DISTANCE; z++) {
					
				}
			}
		}
		
		Render.chunksShader.end();
	}
	
	public short get(int x, int y, int z) {
		Point chunkCoord = new Point(x & ~0xF, y & ~0xF, z & ~0xF);
		Chunk c = loadedChunks.get(chunkCoord);
		if(c == null) {
			return 0;
		}
		//System.out.println(String.format("%d,%d,%d => %d,%d,%d", x, y, z, x & ~0xF, y & ~0xF, z & ~0xF));
		return c.get(x & 0xF, y & 0xF, z & 0xF);
	}
	
	public short getFlagged(int x, int y, int z) {
		Chunk c = loadedChunks.get(new Point(x &~ 0xF, y & ~0xF, z & ~0xF));
		if(c == null) {
			return 0;
		}
		return c.getFlagged(x & 0xF, y & 0xF, z & 0xF);
	}
	
	public void readFromFile(String path) {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:" + path);
			PreparedStatement statement = con.prepareStatement("SELECT * FROM minebuilder;");
			ResultSet set = statement.executeQuery();
			
			while(set.next()) {
				String blockname = set.getString("blockname");
				byte[] compressedBytes = set.getBytes("blockdata");
				DataInputStream compressedBuf = new DataInputStream(new ByteArrayInputStream(compressedBytes));
				
				if(blockname.equals("seed")) {
					this.seed = set.getLong("blockdata");
				} else if(blockname.equals("game-mode")) {
					this.gameMode = set.getLong("blockdata");
				} else if(blockname.equals("biomes.xml")) {
					this.biomesXml = set.getString("blockdata");
				} else if(blockname.equals("spawn")) {
					spawn = new Point(compressedBuf.readInt(), compressedBuf.readInt(), compressedBuf.readInt());
				} else if(blockname.equals("player-host")) {
				} else if(blockname.equals("player-items")) {
				} else if(blockname.equals("max-hearts-host")) {
				} else {
					int orig_size = compressedBuf.readInt();
					int comp_size = compressedBuf.readInt();
					if(orig_size < 0) continue;
					byte[] inflatedBytes = new byte[orig_size];
					
					Inflater inflater = new Inflater();
					inflater.setInput(compressedBytes, 8, comp_size);
					inflater.inflate(inflatedBytes, 0, orig_size);
					
					Chunk chunk = new Chunk(this, new DataInputStream(new ByteArrayInputStream(inflatedBytes)));
					this.loadedChunks.put(chunk.position, chunk);
					for(Chunk c : loadedChunks.values()) c.rebuild();
				}
			}
		} catch(Exception e) { // TODO handle exceptions lol
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void writeToFile(String path) {
		
	}
	
}
