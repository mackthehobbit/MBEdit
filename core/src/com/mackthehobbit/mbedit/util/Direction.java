package com.mackthehobbit.mbedit.util;

public enum Direction {
	
	DOWN(0, -1, 0, "bottom", new float[] {0, 0, 1, 1, 0, 0}),
	UP(0, 1, 0, "top", new float[] {0, 1, 0, 1, 1, 1}),
	NORTH(0, 0, -1, "front", new float[] {1, 1, 0, 0, 0, 0}),
	SOUTH(0, 0, 1, "back", new float[] {0, 1, 1, 1, 0, 1}),
	WEST(-1, 0, 0, "left", new float[] {0, 1, 0, 0, 0, 1}),
	EAST(1, 0, 0, "right", new float[] {1, 1, 1, 1, 0, 0}),
	
	NONE(0, 0, 0, null, null);
	
	private static final Direction[] valid = {DOWN, UP, NORTH, SOUTH, WEST, EAST};
	private static final int[] opposites = {1, 0, 3, 2, 5, 4};
	public final int x, y, z, bitmask;
	public final float[] relativeVertices;
	public final String textureName;
	
	private Direction(int x, int y, int z, String textureName, float[] relativeVertices) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.textureName = textureName;
		this.bitmask = 1 << ordinal();
		this.relativeVertices = relativeVertices;
	}
	
	public static Direction get(int id) {
		if(id >= 0 && id < valid.length)
			return valid[id];
		return NONE;
	}
	
	public Direction opposite() {
		return get(opposites[ordinal()]);
	}
	
}
