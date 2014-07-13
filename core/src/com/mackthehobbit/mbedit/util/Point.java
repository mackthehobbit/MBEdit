package com.mackthehobbit.mbedit.util;

public class Point {
	
	public final int x, y, z;
	
	public Point(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point(Point point) {
		x = point.x;
		y = point.y;
		z = point.z;
	}
	
	@Override
	public boolean equals(Object another) {
		if(!(another instanceof Point)) return false;
		Point other = (Point) another;
		return x == other.x && y == other.y && z == other.z;
	}
	
	@Override
	public int hashCode() {
		return x ^ Integer.rotateLeft(y, 8) ^ Integer.rotateLeft(z, 16);
	}
	
}
