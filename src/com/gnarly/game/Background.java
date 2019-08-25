package com.gnarly.game;

import com.gnarly.engine.model.Rect;
import com.gnarly.engine.shaders.Shader2b;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.shaders.Shader;
import com.gnarly.engine.shaders.Shader2t;
import com.gnarly.engine.texture.Texture;

import static org.lwjgl.opengl.GL11.GL_REPEAT;

public class Background extends Rect {

	private Texture layer0;
	private Texture layer1;
	private Shader2b shader = Shader.SHADER2B;
	private float offset;

	private double speed = -0.25;

	public Background(Camera camera) {
		super(camera, 0, 0, -0.5f, camera.getWidth(), camera.getHeight(), 0, false);
		layer0 = new Texture("res/img/background/background-layer-0.png", GL_REPEAT);
		layer1 = new Texture("res/img/background/background-layer-1.png", GL_REPEAT);
	}

	public void update() {
		offset = (float) (offset + speed * Main.dtime) % 2;
	}

	public void render() {
		layer0.bind(0);
		layer1.bind(1);
		shader.enable();
		shader.setOffset(offset, offset * 0.5f);
		shader.setMVP(camera.getMatrix().translate(position).scale(width * scale, height * scale, 1));
		vao.render();
	}

	public void setCenter(float x, float y) {
		position.x = x - width  / 2;
		position.y = y - height / 2;
	}
}
