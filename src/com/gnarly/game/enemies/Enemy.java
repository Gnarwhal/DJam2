package com.gnarly.game.enemies;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.model.Rect;
import org.joml.Vector2f;

public abstract class Enemy extends Rect {

	protected int life;

	protected Vector2f velocity;

	Enemy(Camera camera, float x, float y, float width, float height, int life) {
		super(camera, x, y, -0.1f, width, height, 0, false);
	}

	public abstract void update();
	public abstract void render();

	public Vector2f getVelocity() {
		return velocity;
	}

	public void applyMovement() {
		position.add(velocity.x, velocity.y, 0);
	}

	public void damage(int damage) {
		life -= damage;
	}

	public boolean isDead() {
		return life <= 0;
	}
}
