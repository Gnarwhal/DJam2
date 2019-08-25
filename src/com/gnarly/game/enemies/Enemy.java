package com.gnarly.game.enemies;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.model.Rect;
import com.gnarly.engine.texture.TextureSet;

public abstract class Enemy extends Rect {

	protected int life;

	Enemy(Camera camera, float x, float y, float width, float height, int life) {
		super(camera, x, y, -0.1f, width, height, 0, false);
	}

	public abstract void update();
	public abstract void render();

	public void damage(int damage) {
		life -= damage;
	}

	public boolean isDead() {
		return life <= 0;
	}
}
