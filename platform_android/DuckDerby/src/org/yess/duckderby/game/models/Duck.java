package org.yess.duckderby.game.models;

import org.yess.duckderby.game.Assets;
import org.yess.duckderby.game.models.components.Speed;
import org.yess.framework.Image;
import org.yess.framework.implementation.math.Circle;
import org.yess.framework.implementation.math.Vector2d;

import android.graphics.Canvas;
import android.util.Log;

public class Duck {
	private Image image;
	private int x;
	private int y;
	private int centerX;
	private int centerY;
	private int width;
	private int height;
	private boolean touched;
	private Speed speed;
	private Circle circle = new Circle(0, 0, 0);
	private final static String TAG = Duck.class.getSimpleName();
	public boolean inPond;
	private static boolean isMuted;

	public Duck(Image image, int x, int y, Speed speed) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.speed = speed;
		this.centerX = this.x + this.width / 2;
		this.centerY = this.y + this.height / 2;
		this.inPond = false;
		this.isMuted = false;
		updateCircle();
	}

	public Speed getSpeed() {
		return this.speed;
	}

	public void setSpeed(Speed s) {
		this.speed = s;
	}

	public Image getImage() {
		return image;
	}

	public void setBitmap(Image image) {
		this.image = image;
	}

	public int getX() {
		return x;
	}

	public int getCenterX() {
		return x + width / 2;
	}

	public void setCenterX(int x) {
		this.x = x - width / 2;
	}

	public void setCenterY(int y) {
		this.y = y + height / 2;
	}

	public int getCenterY() {
		return y + height / 2;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void set(int x, int y) {
		this.x = x - width / 2;
		this.y = y - height / 2;

		updateCircle();
	}

	public int getWidth()
	{
		return this.image.getWidth();
	}
	
	public static void mute()
	{
		isMuted = true;
	}
	
	public static void unmute()
	{
		isMuted = false;
	}
	
	public boolean isTouched() {
		return this.touched;
	}

	public void setTouched(boolean touched) {
		this.touched = touched;
	}

	public void setInPond(boolean isInPond)
	{
		this.inPond = isInPond;
	}
	
	public void update() {
		if (!touched) {
			x += (speed.getXv() * speed.getxDirection());
			y += (speed.getYv() * speed.getyDirection());
			centerX = x + width / 2;
			centerY = y + width / 2;
			;
		}

		updateCircle();
	}

	private void updateCircle() {
		int radius = image.getWidth() / 2;

		this.circle = new Circle(getCenterX(), getCenterY(), radius);
	}

	public void checkTouch(int eventX, int eventY) {
		if (eventX >= (x) && (eventX <= (x + image.getWidth()))) {
			if (eventY >= (y) && (eventY <= (y + image.getHeight()))) {
				setTouched(true);
				quack();
			} else {
				setTouched(false);
			}
		} else {
			setTouched(false);
		}
	}

	public void contains(Vector2d point) {
		// Vector2d point = new Vector2d(eventX, eventY);

		if (this.circle.contains(point)) {
			setTouched(true);
			Assets.quack.play(0.85f);
		} else {
			setTouched(false);
		}
	}

	public void checkScreenCollision(int screenWidth, int screenHeight) {
		if (this.getSpeed().getxDirection() == Speed.DIRECTION_RIGHT
				&& this.getX() + this.image.getWidth() >= screenWidth) {
			this.getSpeed().setxDirection(Speed.DIRECTION_LEFT);
		}
		// check collision with left wall if heading left
		if (this.getSpeed().getxDirection() == Speed.DIRECTION_LEFT
				&& this.getX() <= 0) {
			this.getSpeed().setxDirection(Speed.DIRECTION_RIGHT);
		}
		// check collision with bottom wall if heading down
		if (this.getSpeed().getyDirection() == Speed.DIRECTION_DOWN
				&& this.getY() + this.image.getHeight() >= screenHeight) {
			this.getSpeed().setyDirection(Speed.DIRECTION_UP);
		}
		// check collision with top wall if heading up
		if (this.getSpeed().getyDirection() == Speed.DIRECTION_UP
				&& this.getY() <= 35) {
			this.getSpeed().setyDirection(Speed.DIRECTION_DOWN);
		}

		updateImage();
	}

	public boolean isColliding(Object object) {

		if (object instanceof Duck) {
			Duck duck = (Duck) object;
			return this.circle.overlaps(duck.circle);
		}
		
		if (object instanceof Pond)
		{
			Pond pond = (Pond)object;
			return this.circle.overlaps(pond.circle);
		}
		
		return false;
	}

	private void updateImage() {
		if (this.speed.getxDirection() == Speed.DIRECTION_RIGHT) {
			this.image = Assets.duckRight;
		} else {
			this.image = Assets.duckLeft;
		}
	}

	public void bounceOff(Object obj) {
		Pond pond = null;
		Duck otherDuck = null;
		Circle otherCircle = null;
		if (obj instanceof Duck) {
			otherDuck = (Duck) obj;
			otherCircle = otherDuck.circle;
		} else if (obj instanceof Pond) {
			pond = (Pond) obj;
			otherCircle = pond.getCircle();
		}

		int collisionX = (int) ((this.circle.x * otherCircle.radius + otherCircle.x
				* this.circle.radius) / (this.circle.radius + otherCircle.radius));
		int collisionY = (int) ((this.circle.y * otherCircle.radius
				+ otherCircle.y + this.circle.radius) / (this.circle.radius + otherCircle.radius));

		Vector2d collisionPoint = new Vector2d(collisionX, collisionY);

		alterDirection(this, collisionPoint);
		if (pond == null)
		{
			this.quack();
		}

		if (otherDuck != null) {
			alterDirection(otherDuck, collisionPoint);
			otherDuck.quack();
		}
	}

	private void alterDirection(Duck duck, Vector2d collisionPoint) {
		if (collisionPoint.getX() <= duck.centerX - duck.circle.radius / 2
				|| collisionPoint.getX() >= duck.centerX + duck.circle.radius
						/ 2) {
			duck.getSpeed().toggleXDirection();

			if (collisionPoint.getY() >= duck.centerY - duck.circle.radius / 2
					&& collisionPoint.getY() <= duck.centerY
							+ duck.circle.radius / 2) {
				duck.getSpeed().toggleYDirection();
			}
		}
		if (collisionPoint.getY() < duck.centerY - duck.circle.radius / 2
				|| collisionPoint.getY() > duck.centerY + duck.circle.radius
						/ 2) {
			duck.getSpeed().toggleYDirection();
		}
	}

	private void quack() {
		if (!isMuted)
			Assets.quack.play(0.85f);
	}

	public boolean checkInPond(Pond pond)
	{
		return pond.contains(this);
	}
}
