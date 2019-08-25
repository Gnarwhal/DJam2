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

	private BulletList playerBullets;
	private BulletList enemyBullets;

	private Background background;
	private Player player;

	private ArrayList<Enemy> enemies;

	public static ColRect show;

	public GamePanel(Window window, Camera camera) {
		this.window = window;
		this.camera = camera;

		enemies = new ArrayList<Enemy>();
		enemies.add(new BasicEnemy(camera, 200, 200, 4,
			new float[] {
				3, 1, 3, 1
			},
			new Vector2f[] {
				new Vector2f( 300,  0),
				new Vector2f( 0,  100),
				new Vector2f(-300,  0),
				new Vector2f( 0, -100),
			}
		));
		enemies.add(new BasicEnemy(camera, 200, 200, 0,
				new float[] {
					3, 1, 3, 1
				},
				new Vector2f[] {
					new Vector2f( 300,  0),
					new Vector2f( 0,  100),
					new Vector2f(-300,  0),
					new Vector2f( 0, -100),
				}
		));

		playerBullets = new BulletList(camera);
		enemyBullets = new BulletList(camera);
		enemyBullets.spawnBullet(new Vector2f(camera.getWidth() / 2, 25), (float) Math.PI, BulletList.TYPE_SMOL, 400, 1);

		background = new Background(camera);
		player = new Player(window, camera, playerBullets);

		state = Main.GAME_PANEL;

		show = new ColRect(camera, 0, 0, 0, 100, 100, 1, 0, 0, 0.5f, false);
	}
	
	public void update() {
		background.update();
		player.update();
		for (int i = 0; i < enemies.size(); ++i)
			enemies.get(i).update();

		playerBullets.update(enemies);
		enemyBullets.update(player);

		player.applyMovement();
		for (int i = 0; i < enemies.size(); ++i)
			enemies.get(i).applyMovement();
	}
	
	public void render() {
		background.render();
		enemyBullets.render();
		playerBullets.render();
		for(int i = 0; i < enemies.size(); ++i)
			enemies.get(i).render();
		player.render();
		show.render();
	}
	
	public void reset() {

	}
	
	public int checkState() {
		int state = this.state;
		this.state = Main.GAME_PANEL;
		return state;
	}
}
