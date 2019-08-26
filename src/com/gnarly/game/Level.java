package com.gnarly.game;

import com.gnarly.engine.display.Camera;
import com.gnarly.game.enemies.BasicEnemy;
import com.gnarly.game.enemies.Enemy;
import org.joml.Vector2f;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Level {

	private String path;
	private Camera camera;
	private ArrayList<Enemy> enemies;

	public Level(Camera camera, String path, ArrayList<Enemy> enemies, BulletList bullets) {
		try {
			FileInputStream stream = new FileInputStream(path);
			Scanner scanner = new Scanner(stream);
			int numShips = scanner.nextInt();
			for (int i = 0; i < numShips; ++i) {
				int numPath = scanner.nextInt();
				float x = scanner.nextFloat();
				float y = scanner.nextFloat();
				float timeOffset = scanner.nextFloat();
				float[] times = new float[numPath];
				for (int j = 0; j < numPath; ++j)
					times[j] = scanner.nextFloat();
				Vector2f[] shipPath = new Vector2f[numPath];
				for (int j = 0; j < numPath; ++j)
					shipPath[j] = new Vector2f(scanner.nextFloat(), scanner.nextFloat());
				int fireCount = scanner.nextInt();
				float padEnd = scanner.nextFloat();
				float[] fireTimes = new float[fireCount + 1];
				for (int writeIndex = 0; writeIndex < fireCount;) {
					int reps = scanner.nextInt();
					float delta = scanner.nextFloat();
					for (int j = writeIndex; j < writeIndex + reps; ++j)
						fireTimes[j] = delta;
					writeIndex += reps;
				}
				fireTimes[fireCount] = padEnd;
				enemies.add(new BasicEnemy(camera, x, y, timeOffset, times, shipPath, fireTimes, bullets));
			}
			scanner.close();
			stream.close();

			this.camera = camera;
			this.path = path;
			this.enemies = enemies;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
