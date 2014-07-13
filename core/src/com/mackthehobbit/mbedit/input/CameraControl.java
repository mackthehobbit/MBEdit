package com.mackthehobbit.mbedit.input;

public abstract class CameraControl {
	
	public float x, y, z;
	public float yaw, pitch;
	
	public abstract void update();
	
}