package org.yess.framework.implementation.math;

public class Vector2d {
	public int x;
	public int y;
	
	public Vector2d(){	}
	
	public Vector2d(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void set(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
}
