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
	
	public static final int
		NUM_PANELS = 1,
		GAME_PANEL = 0;
	
	private ALManagement al;
	
	private Window window;
	private Camera camera;
	
	private Panel[] panels;
	private int panel;

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
		al = new ALManagement();
		//window = new Window("Gamer Time", true);
		window = new Window(768, 432, "Gamer Time", true, true, true);
		camera = new Camera(768, 432);
		Shader.init();
		
		panels = new Panel[NUM_PANELS];
		panels[GAME_PANEL] = new GamePanel(window, camera);
		panel = GAME_PANEL;

		sound = new Sound("res/audio/theme.wav");
		sound.play(true);
	}
	
	private void update() {
		window.update();
		int state = panels[panel].checkState();
		if (state != panel) {
			switch (state) {
				case GAME_PANEL:
					GamePanel game = (GamePanel) panels[GAME_PANEL];
					game.reset();
			}
			panel = state;
		}
		panels[panel].update();
		camera.update();
	}
	
	private void render() {
		window.clear();
		panels[panel].render();
		window.swap();
	}
	
	public static void main(String[] args) {
		new Main().start();
	}
}
