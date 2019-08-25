package com.gnarly.game.enemies;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.shaders.Shader;
import com.gnarly.engine.shaders.Shader2t;
import com.gnarly.engine.texture.TextureSet;
import com.gnarly.game.Main;
import org.joml.Vector2f;

public class BasicEnemy extends Enemy {

	final float spf = 1.0f / 16.0f;

	public static final float DIMS = 16;

	private static TextureSet textures = null;
	private Shader2t shader = Shader.SHADER2T;

	private float time = 0;
	private float passedTime = 0;
	private int timeIndex = 0;

	private float[] timing;
	private Vector2f[] path;
	private Vector2f start;
	private Vector2f origin;

	public BasicEnemy(Camera camera, float x, float y, float timeOffset, float[] times, Vector2f[] path) {
		super(camera, 0, 0, DIMS, DIMS, 1);
		this.timing = times;
		this.path = path;
		velocity = new Vector2f();
		if (textures == null) {
			textures = new TextureSet(new String[] {
				"res/img/enemies/basic/basic-0.png",
				"res/img/enemies/basic/basic-1.png",
				"res/img/enemies/basic/basic-2.png",
				"res/img/enemies/basic/basic-3.png"
			});
		}
		time = timeOffset;
		start = new Vector2f();
		for (; (timeOffset -= times[timeIndex]) >= 0; timeIndex = (timeIndex + 1) % path.length) {
			passedTime += times[timeIndex];
			start.add(path[timeIndex]);
		}
		origin = new Vector2f(x, y);
	}

	@Override
	public void update() {
		position.sub(origin.x, origin.y, 0);
		time += Main.dtime;
		float interp;
		while ((interp = (time - passedTime) / timing[timeIndex]) > 1) {
			passedTime += timing[timeIndex];
			start.add(path[timeIndex]);
			timeIndex = (timeIndex + 1) % timing.length;
		}
		velocity = path[timeIndex].mul(interp, new Vector2f()).add(start).sub(position.x, position.y);
		position.add(origin.x, origin.y, 0);
		System.out.println(position);
	}

	@Override
	public void render() {
		textures.get((int) (time / spf) % textures.length()).bind();
		shader.enable();
		shader.setMVP(camera.getMatrix().translate(position).scale(width * scale, height * scale, 1));
		vao.render();
	}
}
