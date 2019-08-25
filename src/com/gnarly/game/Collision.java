package com.gnarly.game;

import org.joml.Vector2f;

public class Collision {
	public static class Bounds {
		public Vector2f position;
		public Vector2f dimensions;

		public Bounds(Vector2f position, Vector2f dimensions) {
			this.position   = new Vector2f(position);
			this.dimensions = new Vector2f(dimensions);
		}
	}

	public static boolean collide(Vector2f position, Vector2f dimensions, Vector2f boundVelocity, BulletList.Bullet bullet) {
		Bounds bounds = getBounds(position, dimensions, boundVelocity);
		Bounds bulletBounds = getBounds(bullet.boundingPosition, bullet.bounds, bullet.velocity);
		if (overlap(bounds, bulletBounds)) {
			Vector2f[] boundsSegments = new Vector2f[] {
				position,
				new Vector2f( dimensions.x,  0),
				new Vector2f( 0,  dimensions.y),
				new Vector2f(-dimensions.x,  0),
				new Vector2f( 0, -dimensions.y)
			};
			Vector2f[] bulletSegments = new Vector2f[] {
				bullet.position,
				bullet.side0,
				bullet.side1,
				bullet.side0.negate(new Vector2f()),
				bullet.side1.negate(new Vector2f())
			};
			Vector2f curBoundsSegment = new Vector2f();
			for (int i = 0; i < boundsSegments.length - 1; ++i) {
				curBoundsSegment.add(boundsSegments[i]);
				Vector2f curBulletSegment = new Vector2f();
				for (int j = 0; j < bulletSegments.length - 1; ++j) {
					curBulletSegment.add(bulletSegments[i]);
					if (segmentCollision(curBoundsSegment, boundsSegments[i + 1], boundVelocity, curBulletSegment, bulletSegments[j + 1], bullet.velocity))
						return true;
				}
			}
		}
		return false;
	}

	public static boolean overlap(Bounds bounds0, Bounds bounds1) {
		return bounds0.position.x < bounds1.position.x + bounds1.dimensions.x && bounds0.position.x + bounds0.dimensions.x > bounds1.position.x
		    && bounds0.position.y < bounds1.position.y + bounds1.dimensions.y && bounds1.position.y + bounds1.dimensions.y > bounds1.position.y;
	}

	public static Bounds getBounds(Vector2f position, Vector2f dimensions, Vector2f velocity) {
		Bounds bounds = new Bounds(position, dimensions);
		if (velocity.x < 0)
			bounds.position.x += velocity.x;
		else
			bounds.dimensions.x += velocity.x;
		if (velocity.y < 0)
			bounds.position.y += velocity.y;
		else
			bounds.dimensions.y += velocity.y;
		return bounds;
	}

	public static boolean segmentCollision(Vector2f P1, Vector2f D1, Vector2f V1, Vector2f P2, Vector2f D2, Vector2f V2) {
		final float DELTA = 0.000001f;

		if ((V1.length() == 0 && V2.length() > 0)
		 || (V2.length() == 0 && V1.length() > 0)
		 ||  V1.angle(V2) > DELTA) {
			float b = (P1.x - P2.x + (V1.x - V2.x) * (P2.y - P1.y) / (V1.y - V2.y)) / (D2.x - D2.y * (V1.x - V2.x) / (V1.y - V2.y));
			if (rangeCheck(b)) {
				float t = (P1.x - P2.x - D2.x * b) / (V2.x - V1.x);
				if (rangeCheck(t))
					return true;
			}
			float a = (P2.x - P1.x - (V1.x - V2.x) * (P1.y - P2.y) / (V2.y - V1.y)) / (D1.x + D1.y * (V1.x - V2.x) / (V2.y - V1.y));
			if (rangeCheck(a)) {
				float t = (P1.x + D1.x * a - P2.x) / (V2.x - V1.x);
				if (rangeCheck(t))
					return true;
			}
			b = (P1.x + D1.x - P2.x + (V1.x - V2.x) * (P2.y - P1.y - D1.y) / (V1.y - V2.y)) / (D2.x - D2.y * (V1.x - V2.x) / (V1.y - V2.y));
			if (rangeCheck(b)) {
				float t = (P1.x + D1.x - P2.x - D2.x * b) / (V2.x - V1.x);
				if (rangeCheck(t))
					return true;
			}
			a = (P2.x + D2.x - P1.x - (V1.x - V2.x) * (P1.y - P2.y - D2.y) / (V2.y - V1.y)) / (D1.x + D1.y * (V1.x - V2.x) / (V2.y - V1.y));
			if (rangeCheck(a)) {
				float t = (P1.x + D1.x * a - P2.x - D2.x) / (V2.x - V1.x);
				if (rangeCheck(t))
					return true;
			}
		}
		return false;
	}

	public static boolean rangeCheck(float num) {
		return 0 <= num && num <= 1;
	}
}
