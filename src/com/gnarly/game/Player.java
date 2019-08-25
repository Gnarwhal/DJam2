package com.gnarly.game;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;
import com.gnarly.engine.model.TexRect;
import com.gnarly.engine.texture.Texture;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends TexRect {

	private static Texture[] frames = null;

	private double spf   = 1.0 / 14.0;
	private double time  = 0;
	private int    frame = 0;
	private float  speed = 150;
	private int    health = 5;

	private Vector2f velocity = new Vector2f();

	private Window window;
	private BulletList bullets;

	public Player(Window window, Camera camera, BulletList bullets) {
		super(camera, (Texture) null, camera.getWidth() / 2 - 16, camera.getHeight() / 2 - 16, -0.1f, 32, 32, 0, false);
		this.window = window;
		this.bullets = bullets;

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
		float speedMultiplier = 1;
		if (window.keyPressed(GLFW_KEY_LEFT_SHIFT) >= Window.BUTTON_PRESSED)
			speedMultiplier *= 1.5;
		else if (window.keyPressed(GLFW_KEY_LEFT_CONTROL) >= Window.BUTTON_PRESSED)
			speedMultiplier *= 0.5;

		velocity.set(0, 0);

		if (window.keyPressed(GLFW_KEY_W) >= Window.BUTTON_PRESSED)
			velocity.y = -speed * (float) Main.dtime * speedMultiplier;
		if (window.keyPressed(GLFW_KEY_A) >= Window.BUTTON_PRESSED)
			velocity.x = -speed * (float) Main.dtime * speedMultiplier;
		if (window.keyPressed(GLFW_KEY_S) >= Window.BUTTON_PRESSED)
			velocity.y =  speed * (float) Main.dtime * speedMultiplier;
		if (window.keyPressed(GLFW_KEY_D) >= Window.BUTTON_PRESSED)
			velocity.x =  speed * (float) Main.dtime * speedMultiplier;

		if (position.x + velocity.x < 0)
			velocity.x = -position.x;
		else if (position.x + width + velocity.x >= camera.getWidth())
			velocity.x = camera.getWidth() - width - position.x;

		if (position.y + velocity.y < 0)
			velocity.y = -position.y;
		else if (position.y + height + velocity.y >= camera.getHeight())
			velocity.y = camera.getHeight() - height - position.y;

		time += Main.dtime;
		while (time >= spf) {
			time -= spf;
			frame = (frame + 1) % frames.length;
			setTexture(frames[frame]);
		}

		if (window.keyPressed(GLFW_KEY_SPACE) == Window.BUTTON_PRESSED)
			bullets.spawnBullet(new Vector2f(position.x + width / 2, position.y - BulletList.getHeight(BulletList.TYPE_PLAYER)), 0, BulletList.TYPE_PLAYER, 400, 1);
	}

	public Collision.Bounds getBounds() {
		return Collision.getBounds(new Vector2f(position.x, position.y), getDimensions(), velocity);
	}

	public Vector2f[] getSegments() {
		return new Vector2f[] {
			new Vector2f(position.x, position.y).add(16, 0),
			new Vector2f( 4,   10),
			new Vector2f( 12,  12),
			new Vector2f(-3,   5 ),
			new Vector2f(-8,   0 ),
			new Vector2f(-5,   5 ),
			new Vector2f(-5,  -5 ),
			new Vector2f(-8,   0 ),
			new Vector2f(-3,  -5 ),
			new Vector2f( 12, -12),
			new Vector2f( 4,  -10),
		};
	}

	public void applyMovement() {
		position.add(velocity.x, velocity.y, 0);
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public void damage(int damage) {
		health -= damage;
	}

	public int getHealth() {
		return health;
	}
}
