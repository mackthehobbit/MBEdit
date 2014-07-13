package com.mackthehobbit.mbedit.util;

public class FloatPoint {
	
	public final float x, y, z;
	
	public FloatPoint(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public FloatPoint(FloatPoint point) {
		this.x = point.x;
		this.y = point.y;
		this.z = point.z;
	}
	
}
