package com.gnarly.game;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;
import com.gnarly.engine.model.TexRect;
import com.gnarly.game.enemies.Enemy;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class GamePanel {

	private static final int
		STATE_PRE     = 0x00,
		STATE_PLAYING = 0x01,
		STATE_DEAD    = 0x02,
		STATE_WIN     = 0x03;

	private static final float
		DELAY = 0.75f;

	private Window window;
	private Camera camera;

	private BulletList playerBullets;
	private BulletList enemyBullets;

	private Background background;
	private Player player;

	private ArrayList<Enemy> enemies;

	private Level level;

	private int state = STATE_PRE;
	private TexRect begin;
	private TexRect dead;
	private TexRect win;

	private boolean restart = false;

	private float time;

	public GamePanel(Window window, Camera camera) {
		this.window = window;
		this.camera = camera;

		enemies = new ArrayList<Enemy>();

		playerBullets = new BulletList(camera);
		enemyBullets = new BulletList(camera);

		level = new Level(camera, "res/levels/level1.txt", enemies, enemyBullets);

		background = new Background(camera);
		player = new Player(window, camera, playerBullets);

		begin = new TexRect(camera, "res/img/ui/begin.png", 0, 0, 0, camera.getWidth(), camera.getHeight(), 0, false);
		dead  = new TexRect(camera, "res/img/ui/dead.png",  0, 0, 0, camera.getWidth(), camera.getHeight(), 0, false);
		win   = new TexRect(camera, "res/img/ui/win.png",   0, 0, 0, camera.getWidth(), camera.getHeight(), 0, false);
	}
	
	public void update() {
		background.update();
		if (state == STATE_PRE) {
			player.updateAnim();

			if (window.keyPressed(GLFW_KEY_SPACE) == Window.BUTTON_PRESSED)
				state = STATE_PLAYING;
		}
		else if (state == STATE_PLAYING) {
			player.update();
			for (int i = 0; i < enemies.size(); ++i)
				enemies.get(i).update();

			playerBullets.update(enemies);
			enemyBullets.update(player);

			if (player.getHealth() <= 0) {
				state = STATE_DEAD;
			}
			else if (enemies.size() == 0) {
				state = STATE_WIN;
			}

			player.applyMovement();
			for (int i = 0; i < enemies.size(); ++i)
				enemies.get(i).applyMovement();
		}
		else if (state == STATE_DEAD) {
			time += Main.dtime;
			if (window.keyPressed(GLFW_KEY_SPACE) == Window.BUTTON_PRESSED && time > DELAY)
				restart = true;
		}
		else if (state == STATE_WIN) {
			player.updateAnim();
			time += Main.dtime;
			if (window.keyPressed(GLFW_KEY_SPACE) == Window.BUTTON_PRESSED && time > DELAY)
				restart = true;
		}
	}
	
	public void render() {
		background.render();
		if (state == STATE_PRE) {
			player.render();
			begin.render();
		}
		else if (state == STATE_PLAYING) {
			enemyBullets.render();
			playerBullets.render();
			for (int i = 0; i < enemies.size(); ++i)
				enemies.get(i).render();
			player.render();
		}
		else if (state == STATE_DEAD) {
			dead.render();
		}
		else if (state == STATE_WIN) {
			player.render();
			win.render();
		}
	}

	public boolean restart() {
		return restart;
	}
}
