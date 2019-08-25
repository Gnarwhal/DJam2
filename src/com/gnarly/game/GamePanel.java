package com.gnarly.game;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;
import com.gnarly.engine.model.ColRect;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class GamePanel extends Panel {

	private Window window;
	private Camera camera;

	private BulletList bullets;

	private Background background;
	private Player player;

	private float rotation = 0;

	private ColRect testRect;

	public GamePanel(Window window, Camera camera) {
		this.window = window;
		this.camera = camera;

		bullets = new BulletList(camera);

		background = new Background(camera);
		player = new Player(window, camera);

		testRect = new ColRect(camera, 100, 100, 0, 25, 25, 1, 0, 0, 1, false);

		state = Main.GAME_PANEL;
	}
	
	public void update() {
		bullets.update(testRect, new Vector2f(0, 100));
		background.update();
		player.update();

		if (window.keyPressed(GLFW_KEY_SPACE) == Window.BUTTON_PRESSED)
			bullets.spawnBullet(new Vector2f(player.getX() + (player.getWidth()) / 2, player.getY() + bullets.getHeight(BulletList.TYPE_PLAYER)), rotation += Math.PI / 16, BulletList.TYPE_PLAYER);
	}
	
	public void render() {
		testRect.render();
		bullets.render();
		background.render();
		player.render();
	}
	
	public void reset() {

	}
	
	public int checkState() {
		int state = this.state;
		this.state = Main.GAME_PANEL;
		return state;
	}
}
