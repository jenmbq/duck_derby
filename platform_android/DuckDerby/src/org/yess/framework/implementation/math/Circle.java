package org.yess.framework.implementation.math;

import android.util.Log;

public class Circle {
	public float x;
	public float y;
	public float radius;
	private final String TAG = Circle.class.getSimpleName();
	
	public Circle(){}
	
	public Circle(float x, float y, float radius)
	{
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public void set(float x, float y, float radius)
	{
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public void setPosition(float x, float y)
	{
		this.x = this.y;
	}
	
	public boolean contains(Circle c)
	{
		float dx = x - c.x;
		float dy = y - c.y;
		
		float maxDistanceSqrd = dx * dx + dy * dy + c.radius * c.radius;
		return maxDistanceSqrd <= radius * radius;
	}
	
	public boolean overlaps(Circle c)
	{
		float dx = x - c.x;
		float dy = y - c.y;
		
		float distance = dx * dx + dy * dy;
		float radiusSum = radius + c.radius;
		
		return distance < radiusSum * radiusSum;
	}
	
	public String toString()
	{
		return x + ", " + y + ", " + radius;
	}

	public boolean contains(int x, int y) {		
			float dx = this.x - y;
			float dy = this.y - x;
			return dx * dx + dy * dy <= radius * radius;
	}

	public boolean contains(Vector2d point) {
		float dx = x - point.x;
		float dy = y - point.y;
		return dx * dx + dy * dy <= radius * radius;
	}

}
