package org.yess.duckderby.game;

import org.yess.framework.Game;

public class Level {
	private final int startLevel = 1;
	private final int startDucks = 5;
	private final int startTime = 20;
	private final int startMinSpeed = 1;
	private final int startMaxSpeed = 2;
	
	public static int score;
	public static int nextStartTime;
	public static int nextDuckCount;
	public static int nextLevel;
	public static int nextMinSpeed;
	public static int nextMaxSpeed;
	
	public static boolean newGame = true;
	public static boolean isRestart = false;

	public int currentLevel;
	public int duckCount;
	public int time;
	public int speedMin;
	public int speedMax;
	
	public Level() {	}
	
	public void start(){
		currentLevel = startLevel;
		duckCount = startDucks;
		time = startTime;
		speedMin = startMinSpeed;
		speedMax = startMaxSpeed;
		Level.score = 0;
		Level.nextStartTime = startTime;
		Level.nextDuckCount = startDucks;
		Level.nextMinSpeed = startMinSpeed;
		Level.nextMaxSpeed = startMaxSpeed;
		Level.nextLevel = startLevel;
	}
	
	public void restart(){
		start();
	}
	
	public void getNextLevel()	{
		duckCount = ++nextDuckCount;
		time = --nextStartTime;
		currentLevel = ++nextLevel;
		speedMin  = ++nextMinSpeed;
		speedMax = ++nextMaxSpeed;
	}
	
	public void clear()
	{
		start();
	}
}
