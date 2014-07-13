package com.mackthehobbit.mbedit.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class DesktopCameraControl extends CameraControl {
	
	public static final float MOVE_SPEED = 0.4f, TURN_SPEED = 0.1f;
	
	@Override
	public void update() {
		float pitch = this.pitch, yaw = this.yaw;
		if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			yaw = Math.round(yaw / 90) * 90;
			pitch = 0;
		}
		float xoff = (float) (MOVE_SPEED *  Math.sin(Math.toRadians(yaw)));
		float yoff = (float) (MOVE_SPEED * -Math.sin(Math.toRadians(pitch)));
		float zoff = (float) (MOVE_SPEED * Math.cos(Math.toRadians(yaw)));
		if(Gdx.input.isKeyPressed(Keys.D)) {
			x += zoff;
			z -= xoff;
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			x -= zoff;
			z += xoff;
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			x += xoff * Math.cos(Math.toRadians(pitch));
			y += yoff;
			z += zoff * Math.cos(Math.toRadians(pitch));
		}
		if(Gdx.input.isKeyPressed(Keys.W)) {
			x -= xoff * Math.cos(Math.toRadians(pitch));
			y -= yoff;
			z -= zoff * Math.cos(Math.toRadians(pitch));
		}
		if(Gdx.input.isKeyPressed(Keys.Q)) y += MOVE_SPEED;
		if(Gdx.input.isKeyPressed(Keys.Z)) y -= MOVE_SPEED;
		int dx = Gdx.input.getDeltaX(), dy = Gdx.input.getDeltaY();
		this.yaw -= dx * TURN_SPEED;
		this.pitch -= dy * TURN_SPEED;
		if(this.yaw > 360f) this.yaw -= 360f;
		if(this.yaw < 0f) this.yaw += 360f;
		if(this.pitch > 90f) this.pitch = 90f;
		if(this.pitch < -90f) this.pitch = -90f;
		Gdx.input.setCursorCatched(true);
		Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
	}
	
}
