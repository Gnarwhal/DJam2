package com.gnarly.game;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;
import com.gnarly.engine.model.TexRect;
import com.gnarly.engine.texture.Texture;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends TexRect {

	private double spf   = 1.0 / 14.0;
	private double time  = 0;
	private int    frame = 0;

	private static Texture[] frames = null;

	private Window window;

	public Player(Window window, Camera camera) {
		super(camera, (Texture) null, 0, 0, -0.1f, 32, 32, 0, false);
		this.window = window;

		if (frames == null) {
			frames = new Texture[4];
			frames[0] = new Texture("res/img/player-ship/player-ship-0.png");
			frames[1] = new Texture("res/img/player-ship/player-ship-1.png");
			frames[2] = new Texture("res/img/player-ship/player-ship-2.png");
			frames[3] = new Texture("res/img/player-ship/player-ship-3.png");
		}

		setTexture(frames[0]);
	}

	public void update() {
		final int SPEED = 150;
		if (window.keyPressed(GLFW_KEY_W) >= Window.BUTTON_PRESSED)
			position.y -= SPEED * Main.dtime;
		if (window.keyPressed(GLFW_KEY_A) >= Window.BUTTON_PRESSED)
			position.x -= SPEED * Main.dtime;
		if (window.keyPressed(GLFW_KEY_S) >= Window.BUTTON_PRESSED)
			position.y += SPEED * Main.dtime;
		if (window.keyPressed(GLFW_KEY_D) >= Window.BUTTON_PRESSED)
			position.x += SPEED * Main.dtime;

		if (position.x < 0)
			position.x = 0;
		else if (position.x + width >= camera.getWidth())
			position.x = camera.getWidth() - width;

		if (position.y < 0)
			position.y = 0;
		else if (position.y + height >= camera.getHeight())
			position.y = camera.getHeight() - height;

		time += Main.dtime;
		while (time >= spf) {
			time -= spf;
			frame = (frame + 1) % frames.length;
			setTexture(frames[frame]);
		}
	}
}
