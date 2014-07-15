package com.mackthehobbit.mbedit.world;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.utils.FloatArray;
import com.mackthehobbit.mbedit.block.Block;
import com.mackthehobbit.mbedit.block.BlockRenderer;
import com.mackthehobbit.mbedit.util.Point;

public class Chunk {
	
	public static final int SIZE = 16;
	public static final int MAX_VERTICES =
			SIZE * SIZE * SIZE * 6 * 6;
	public static final int ENTITIES_VERIFY = 0xabcdef01;
	
	public Point position;
	private boolean loadedLight;
	public World world;
	private short[][][] data = new short[SIZE][SIZE][SIZE];
	private byte[][][] light = new byte[SIZE][SIZE][SIZE];
	private Entity[] entities;
	public Mesh chunkMesh = new Mesh(true, MAX_VERTICES, 0,
									new VertexAttribute(Usage.Position, 3, "a_position"),
									new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));
	public int vertices = 0;
	
	public Chunk(World world) {
		this.world = world;
		position = new Point(0, 0, 0);
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				for(int k = 0; k < SIZE; k++) {
					if(j < 8) this.data[i][j][k] = 1;
				}
			}
		}
		rebuild();
	}
	
	public Chunk(World world, DataInputStream stream) throws IOException {
		this.world = world;
		position = new Point(stream.readInt(), stream.readInt(), stream.readInt());
		loadedLight = stream.readBoolean();
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				for(int k = 0; k < SIZE; k++) {
					this.data[k][j][i] = stream.readShort();
				}
			}
		}
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				for(int k = 0; k < SIZE; k++) {
					this.light[k][j][i] = stream.readByte();
				}
			}
		}
		try {
			if(stream.readInt() != ENTITIES_VERIFY) return;
		} catch (EOFException e) {
			return;
		}
		int entitiesSize = stream.readInt();
		entities = new Entity[entitiesSize];
		for(int i = 0; i < entitiesSize; i++) {
			Entity entity = new Entity();
			entity.type = stream.readInt();
			entity.data = new byte[stream.readInt()];
			stream.read(entity.data, 0, entity.data.length);
			entities[i] = entity;
		}
	}
	
	public void finalize() {
		this.chunkMesh.dispose();
	}
	
	public void save(DataOutputStream stream) throws IOException {
		stream.writeInt(position.x);
		stream.writeInt(position.y);
		stream.writeInt(position.z);
		stream.writeBoolean(loadedLight);
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				for(int k = 0; k < SIZE; k++) {
					stream.writeShort(this.data[k][j][i]);
				}
			}
		}
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				for(int k = 0; k < SIZE; k++) {
					stream.writeByte(this.light[k][j][i]);
				}
			}
		}
		stream.writeInt(ENTITIES_VERIFY);
		stream.writeInt(entities.length);
		for(Entity e : entities) {
			stream.writeInt(e.type);
			stream.writeInt(e.data.length);
			for(byte b : e.data) {
				stream.writeByte(b);
			}
		}
	}
	
	public short get(int x, int y, int z) {
		return (short) ((getFlagged(x, y, z) & 0xFF00) >>> 8);
	}
	
	public short getFlagged(int x, int y, int z) {
		try {
			return this.data[x][y][z];
		} catch (ArrayIndexOutOfBoundsException e) {
			return world.get(position.x + x, position.y + y, position.z + z);
		}
	}
	
	public short getFlag(int x, int y, int z) {
		return (short) (this.data[x][y][z] & 0xFF);
	}
	
	public void set(int x, int y, int z, short id) {
		this.data[x][y][z] = id;
	}
	
	public void set(int x, int y, int z, short id, short flag) {
		this.data[x][y][z] = (short) ((id << 8) | (flag & 0xFF));
	}
	
	public void rebuild() {
		FloatArray vertices = new FloatArray();
		for(int x = 0; x < SIZE; x++) {
			for(int y = 0; y < SIZE; y++) {
				for(int z = 0; z < SIZE; z++) {
					short id = this.get(x, y, z);
					Block block = Block.blocks[id];
					BlockRenderer renderer = Block.blockRenderers[id];
					if(block == null || renderer == null) continue;
					renderer.render(this.world, vertices, position.x + x, position.y + y, position.z + z, block, this.getFlag(x, y, z));
				}
			}
		}
		chunkMesh.setVertices(vertices.items);
		this.vertices = vertices.size / 5;
	}
	
}
