package com.gnarly.game;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.model.ColRect;
import com.gnarly.engine.model.Rect;
import com.gnarly.engine.model.Vao;
import com.gnarly.engine.shaders.Shader;
import com.gnarly.engine.shaders.Shader2t;
import com.gnarly.engine.texture.TextureSet;
import org.joml.Vector2f;

import java.util.ArrayList;

public class BulletList {

	private static final double SPF = 1.0 / 20.0;

	private static TextureSet[] sprites = null;
	private static Vao vao;
	private Shader2t shader = Shader.SHADER2T;

	public static class Bullet {
		public double lifetime;
		public int sprite;
		public Vector2f position;
		public Vector2f side0;
		public Vector2f side1;
		public Vector2f velocity;

		public float width;
		public float height;
		public float rotation;

		public Vector2f boundingPosition;
		public Vector2f bounds;
	}

	public static final Vector2f[] DIMENSIONS = new Vector2f[] {
		new Vector2f(4, -8)
	};

	public static final float[] SPEEDS = new float[] {
		20
	};

	public static final int
		TYPE_PLAYER = 0x00;

	private Camera camera;

	private ArrayList<Bullet> bullets;

	public BulletList(Camera camera) {
		this.camera = camera;
		bullets = new ArrayList<>();

		if (sprites == null) {
			sprites = new TextureSet[1];
			sprites[0] = new TextureSet(new String[] {
				"res/img/bullets/bullet-0/bullet-0.png",
				"res/img/bullets/bullet-0/bullet-1.png",
				"res/img/bullets/bullet-0/bullet-2.png",
				"res/img/bullets/bullet-0/bullet-3.png",
			});
			float vertices[] = {
				1, 1, 0, // Top left
				1, 0, 0, // Bottom left
				0, 0, 0, // Bottom right
				0, 1, 0  // Top right
			};
			int indices[] = {
				0, 1, 3,
				1, 2, 3
			};
			float[] texCoords = {
				1, 0,
				1, 1,
				0, 1,
				0, 0
			};
			vao = new Vao(vertices, indices);
			vao.addAttrib(texCoords, 2);
		}
	}

	public void update(Rect rect, Vector2f velocity) {
		for (int i = 0; i < bullets.size(); ++i) {
			Bullet bullet = bullets.get(i);
			Vector2f deltaV = bullet.velocity.mul((float) Main.dtime, new Vector2f());
			if (Collision.collide(rect.getPosition(), rect.getDimensions(), velocity, bullet))
				System.out.println("collision");
			bullet.position.add(deltaV);
			bullet.boundingPosition.add(deltaV);
			bullet.lifetime += Main.dtime;

			if (bullet.boundingPosition.x > camera.getWidth()   || bullet.boundingPosition.y > camera.getHeight()
			 || bullet.boundingPosition.x + bullet.bounds.x < 0 || bullet.boundingPosition.y + bullet.bounds.y < 0) {
				bullets.remove(i);
				--i;
			}
		}
	}

	public void render() {
		for (int i = 0; i < bullets.size(); ++i) {
			Bullet bullet = bullets.get(i);
			sprites[bullet.sprite].get((int) (bullet.lifetime / SPF) % sprites[bullet.sprite].length()).bind();
			shader.enable();
			shader.setMVP(camera.getMatrix().translate(bullet.position.x, bullet.position.y, -0.25f).rotateZ(bullet.rotation).scale(bullet.width, bullet.height, 1));
			vao.render();
		}
	}

	public void spawnBullet(Vector2f position, float rotation, int type) {
		spawnBullet(position, DIMENSIONS[type], rotation, SPEEDS[type], type);
	}

	private void spawnBullet(Vector2f position, Vector2f dimensions, float rotation, float velocity, int sprite) {
		float sin = (float) -Math.sin(rotation);
		float cos = (float)  Math.cos(rotation);

		float initialPosition = -dimensions.x / 2;

		/**************************************
		 *                                    *
		 *   2D Rotations                     *
		 *                                    *
		 **************************************
		 *                                    *
		 *   x + yi * cos + sini              *
	     *   (xcos - ysin) + (xsin + ycos)i   *
		 *                                    *
		 **************************************/
		Bullet bullet = new Bullet();
		bullet.position = new Vector2f(initialPosition * cos, initialPosition * sin);
		bullet.position.add(position);
		bullet.side0 = new Vector2f( dimensions.x * cos, dimensions.x * sin);
		bullet.side1 = new Vector2f(-dimensions.y * sin, dimensions.y * cos);
		bullet.velocity = new Vector2f(velocity * sin, -velocity * cos);

		bullet.width  = dimensions.x;
		bullet.height = dimensions.y;
		bullet.rotation = -rotation;

		bullet.lifetime = 0;
		bullet.sprite = sprite;

		Vector2f p0 = new Vector2f(bullet.position);
		Vector2f p1 = new Vector2f(bullet.position).add(bullet.side0);
		Vector2f p2 = new Vector2f(bullet.position).add(bullet.side1);
		Vector2f p3 = new Vector2f(bullet.position).add(bullet.side0).add(bullet.side1);

		bullet.boundingPosition = new Vector2f(
			min(new float[]{ p0.x, p1.x, p2.x, p3.x }),
			min(new float[]{ p0.y, p1.y, p2.y, p3.y })
		);
		bullet.bounds = new Vector2f(
			max(new float[]{ p0.x, p1.x, p2.x, p3.x }),
			max(new float[]{ p0.y, p1.y, p2.y, p3.y })
		).sub(bullet.boundingPosition);

		bullets.add(bullet);
	}

	public float getWidth(int type) {
		return DIMENSIONS[type].x;
	}

	public float getHeight(int type) {
		return DIMENSIONS[type].y;
	}

	private float min(float[] nums) {
		float min = nums[0];
		for (int i = 1; i < nums.length; ++i)
			if (nums[i] < min)
				min = nums[i];
		return min;
	}

	private float max(float[] nums) {
		float max = nums[0];
		for (int i = 1; i < nums.length; ++i)
			if (nums[i] > max)
				max = nums[i];
		return max;
	}
}
