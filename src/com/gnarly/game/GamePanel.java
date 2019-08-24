package com.gnarly.game;

import com.gnarly.engine.display.Camera;
import com.gnarly.engine.display.Window;

public class GamePanel extends Panel {

	private Window window;
	private Camera camera;

	public GamePanel(Window window, Camera camera) {
		this.window = window;
		this.camera = camera;
		
		state = Main.GAME_PANEL;
	}
	
	public void update() {

	}
	
	public void render() {

	}
	
	public void reset() {

	}
	
	public int checkState() {
		int state = this.state;
		this.state = Main.GAME_PANEL;
		return state;
	}
}
