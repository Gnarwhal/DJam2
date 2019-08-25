package com.gnarly.game.enemies;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.shaders.Shader;
import com.gnarly.engine.shaders.Shader2t;
import com.gnarly.engine.texture.TextureSet;
import com.gnarly.game.Main;

public class BasicEnemy extends Enemy {

	final float spf = 1.0f / 16.0f;

	public static final float DIMS = 16;

	private static TextureSet textures = null;
	private Shader2t shader = Shader.SHADER2T;

	private float time = 0;

	public BasicEnemy(Camera camera, float x, float y) {
		super(camera, x, y, DIMS, DIMS, 1);
		if (textures == null) {
			textures = new TextureSet(new String[] {
				"res/img/enemies/basic/basic-0.png",
				"res/img/enemies/basic/basic-1.png",
				"res/img/enemies/basic/basic-2.png",
				"res/img/enemies/basic/basic-3.png"
			});
		}
	}

	@Override
	public void update() {
		time = (float) (time + Main.dtime) % (spf * textures.length());
	}

	@Override
	public void render() {
		textures.get((int) (time / spf)).bind();
		shader.enable();
		shader.setMVP(camera.getMatrix().translate(position).scale(width * scale, height * scale, 1));
		vao.render();
	}
}
