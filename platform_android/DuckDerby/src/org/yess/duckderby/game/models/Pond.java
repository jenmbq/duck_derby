package org.yess.duckderby.game.models;

import org.yess.framework.Image;
import org.yess.framework.implementation.math.Circle;
import org.yess.framework.implementation.math.Vector2d;

import android.util.Log;

public class Pond {

	private final String TAG = Pond.class.getSimpleName();
	public Circle circle = new Circle(0, 0, 0);

	private int x;
	private int y;
	private int width;

	private Image image;
	public int ducksInPond;

	public Pond() {
	}

	public Pond(Image image, int x, int y) {
		this.x = x;
		this.y = y;
		this.image = image;
		this.width = image.getWidth();
		int centerX = x + width / 2;
		int centerY = y + width / 2;

		this.circle = new Circle(centerX, centerY, width / 2);
		Log.d(TAG, "CircleX=" + circle.x + " Circle Y= " + circle.y
				+ " Radius= " + circle.radius);
		
		ducksInPond = 0;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void set(int x, int y, int width) {
		this.x = x;
		this.y = y;
		this.width = width;
		int centerX = x + width / 2;
		int centerY = y + width / 2;
		this.circle = new Circle(centerX, centerY, width / 2);
	}

	public Circle getCircle() {
		return this.circle;
	}

	public void update() {

	}

	public boolean contains(Duck duck) {
		Vector2d duckCenter = new Vector2d();
		duckCenter.set(duck.getCenterX(), duck.getCenterY());

		return this.circle.contains(duckCenter);
	}
	
	public void addDuck()
	{
		ducksInPond++;
	}
}
