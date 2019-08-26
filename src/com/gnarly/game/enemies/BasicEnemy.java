package com.gnarly.game.enemies;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.shaders.Shader;
import com.gnarly.engine.shaders.Shader2t;
import com.gnarly.engine.texture.TextureSet;
import com.gnarly.game.BulletList;
import com.gnarly.game.Main;
import org.joml.Vector2f;

public class BasicEnemy extends Enemy {

	final float spf = 1.0f / 16.0f;

	public static final float DIMS = 16;


	private static final Vector2f FIRE_OFFSET = new Vector2f(DIMS / 2, DIMS);

	private static TextureSet textures = null;
	private Shader2t shader = Shader.SHADER2T;

	private float time = 0;
	private float passedTime = 0;
	private int timeIndex = 0;

	private float[] timing;
	private Vector2f[] path;
	private Vector2f start;
	private Vector2f origin;

	private float pastFire;
	private int fireIndex;
	private float[] fireTimes;

	private BulletList bullets;

	public BasicEnemy(Camera camera, float x, float y, float timeOffset, float[] times, Vector2f[] path, float[] fireTimes, BulletList bullets) {
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
		timeOffset = time;
		for (; (timeOffset -= fireTimes[fireIndex]) >= 0; fireIndex = (fireIndex + 1) % fireTimes.length)
			pastFire += fireTimes[fireIndex];
		origin = new Vector2f(x, y);

		this.fireTimes = fireTimes;
		this.bullets = bullets;
	}

	@Override
	public void update() {
		time += Main.dtime;

		position.sub(origin.x, origin.y, 0);
		float interp;
		while ((interp = (time - passedTime) / timing[timeIndex]) > 1) {
			passedTime += timing[timeIndex];
			start.add(path[timeIndex]);
			timeIndex = (timeIndex + 1) % timing.length;
		}
		velocity = path[timeIndex].mul(interp, new Vector2f()).add(start).sub(position.x, position.y);
		position.add(origin.x, origin.y, 0);

		if (time - pastFire > fireTimes[fireIndex]) {
			if (fireIndex != fireTimes.length - 1)
				fire();
			pastFire += fireTimes[fireIndex];
			fireIndex = (fireIndex + 1) % fireTimes.length;
		}
	}

	public void fire() {
		bullets.spawnBullet(new Vector2f(position.x, position.y).add(FIRE_OFFSET), (float) Math.PI, BulletList.TYPE_SMOL, 200, 1);
	}

	@Override
	public void render() {
		textures.get((int) (time / spf) % textures.length()).bind();
		shader.enable();
		shader.setMVP(camera.getMatrix().translate(position).scale(width * scale, height * scale, 1));
		vao.render();
	}
}
