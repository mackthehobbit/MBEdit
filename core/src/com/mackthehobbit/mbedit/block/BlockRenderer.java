package com.mackthehobbit.mbedit.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.FloatArray;
import com.mackthehobbit.mbedit.util.Direction;
import com.mackthehobbit.mbedit.util.Render;
import com.mackthehobbit.mbedit.world.World;

public abstract class BlockRenderer {
	
	public static BlockRenderer normal = new BlockRenderer() {
		@Override
		public void render(World world, FloatArray vertices, int x, int y, int z, Block block, short flag) {
			for(int d = 0; d < 6; d++) {
				Direction dir = Direction.get(d);
				Block b = Block.blocks[world.get(dir.x + x, dir.y + y, dir.z + z)];
				if(b != null && !b.opaque) continue;
				AtlasRegion region = Render.chunksAtlas.findRegion(block.getTexture(dir.textureName));
				if(region == null) {continue;}
				addFace(vertices, region,
						x + dir.relativeVertices[0],
						y + dir.relativeVertices[1],
						z + dir.relativeVertices[2],
						x + dir.relativeVertices[3],
						y + dir.relativeVertices[4],
						z + dir.relativeVertices[5]
					);
			}
		}
	};
	
	public static BlockRenderer torch = new BlockRenderer() {
		@Override
		public void render(World world, FloatArray vertices, int x, int y, int z, Block block, short flag) {
			AtlasRegion def = Render.chunksAtlas.findRegion(block.getTexture("default"));
			AtlasRegion top = Render.chunksAtlas.findRegion(block.getTexture("top"));
			addFace(vertices, def,
					x, y + 1, z,
					x + 1, y, z + 1
				);
			addFace(vertices, def,
					x + 1, y + 1, z,
					x, y, z + 1
				);
			if(top != def) {
				addFace(vertices, top,
						x, y + 0.1f, z,
						x + 1, y + 0.1f, z + 1
					);
			}
		}
	};
	
	public abstract void render(World world, FloatArray vertices, int x, int y, int z, Block block, short flag);
	
	public static void addFace(FloatArray vertices, AtlasRegion texture, float xTL, float yTL, float zTL, float xBR, float yBR, float zBR) {
		float xTR = xTL, yTR = yTL, zTR = zTL;
		float xBL = xTL, yBL = yTL, zBL = zTL;
		if(yTL == yBR) {
			zBL = zBR;
			xTR = xBR;
		} else {
			yBL = yBR;
			xTR = xBR;
			zTR = zBR;
		}
		// top right
		vertices.add(xTR); vertices.add(yTR); vertices.add(zTR);
		vertices.add(texture.getU2()); vertices.add(texture.getV());
		//top left
		vertices.add(xTL); vertices.add(yTL); vertices.add(zTL);
		vertices.add(texture.getU()); vertices.add(texture.getV());
		//bottom left
		vertices.add(xBL); vertices.add(yBL); vertices.add(zBL);
		vertices.add(texture.getU()); vertices.add(texture.getV2());
		// top right
		vertices.add(xTR); vertices.add(yTR); vertices.add(zTR);
		vertices.add(texture.getU2()); vertices.add(texture.getV());
		// bottom left
		vertices.add(xBL); vertices.add(yBL); vertices.add(zBL);
		vertices.add(texture.getU()); vertices.add(texture.getV2());
		// bottom right
		vertices.add(xBR); vertices.add(yBR); vertices.add(zBR);
		vertices.add(texture.getU2()); vertices.add(texture.getV2());
	}
	
}
