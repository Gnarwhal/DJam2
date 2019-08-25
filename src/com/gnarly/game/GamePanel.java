package com.gnarly.game;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;
import com.gnarly.engine.model.ColRect;
import com.gnarly.game.enemies.BasicEnemy;
import com.gnarly.game.enemies.Enemy;
import org.joml.Vector2f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class GamePanel extends Panel {

	private Window window;
	private Camera camera;

	private BulletList bullets;

	private Background background;
	private Player player;

	private float rotation = 0;

	private ArrayList<Enemy> enemies;

	public GamePanel(Window window, Camera camera) {
		this.window = window;
		this.camera = camera;

		enemies = new ArrayList<Enemy>();
		enemies.add(new BasicEnemy(camera, 200, 200));

		bullets = new BulletList(camera);

		background = new Background(camera);
		player = new Player(window, camera);

		state = Main.GAME_PANEL;
	}
	
	public void update() {
		for (int i = 0; i < enemies.size(); ++i)
			enemies.get(i).update();

		bullets.update(enemies);
		background.update();
		player.update();

		if (window.keyPressed(GLFW_KEY_SPACE) == Window.BUTTON_PRESSED)
			bullets.spawnBullet(new Vector2f(player.getX() + (player.getWidth()) / 2, player.getY() - bullets.getHeight(BulletList.TYPE_PLAYER)), 0, BulletList.TYPE_PLAYER);
	}
	
	public void render() {
		for(int i = 0; i < enemies.size(); ++i)
			enemies.get(i).render();
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
