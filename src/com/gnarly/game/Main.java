package com.gnarly.game;

import com.gnarly.engine.audio.ALManagement;
import com.gnarly.engine.audio.Sound;
import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;
import com.gnarly.engine.shaders.Shader;
import org.joml.Vector2f;

import java.io.IOException;

public class Main {

	public static int fps;
	public static double dtime;
	
	private ALManagement al;
	
	private Window window;
	private Camera camera;
	
	private GamePanel panel;

	private Sound sound;

	public void start() {
		init();
		int frames = 0;
		long curTime, pastTime, pastSec, nspf = 1000000000 / Window.REFRESH_RATE;
		pastTime = System.nanoTime();
		pastSec = pastTime;
		while(!window.shouldClose()) {
			curTime = System.nanoTime();
			if (curTime - pastTime > nspf) {
				dtime = nspf / 1000000000d;
				update();
				render();
				pastTime += nspf;
				++frames;
			}
			if (curTime - pastSec > 1000000000) {
				fps = frames;
				frames = 0;
				pastSec += 1000000000;
			}
			if (nspf - curTime + pastTime > 10000000) try {
				Thread.sleep(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		al.destroy();
		Window.terminate();
	}
	
	private void init() {
		int position = 15;
		for (int i = 0; i < 16; ++i) {
			int movement = (int) (Math.random() * 11) - 5;
			if (position + movement < 5 || position + movement > 27)
				position -= movement;
			else
				position += movement;
			/*System.out.println(" --- ");
			System.out.println("30 1");
			System.out.println("1 5");
			System.out.println(position + " 0.12903225806");
			System.out.println("1 0.38709677418 ");
			System.out.println((28 - position) + " 0.12903225806");*/
		}

		al = new ALManagement();
		window = new Window("Space Mash", true);
		//window = new Window(768, 432, "Space Mash", true, true, true);
		camera = new Camera(768, 432);
		Shader.init();

		panel = new GamePanel(window, camera);

		sound = new Sound("res/audio/theme.wav");
		sound.play(true);
	}
	
	private void update() {
		window.update();
		panel.update();
		if (panel.restart())
			panel = new GamePanel(window, camera);
		camera.update();
	}
	
	private void render() {
		window.clear();
		panel.render();
		window.swap();
	}
	
	public static void main(String[] args) {
		new Main().start();
	}
}
