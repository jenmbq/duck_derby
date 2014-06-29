package org.yess.duckderby.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.yess.framework.Game;
import org.yess.framework.Image;
import org.yess.framework.Screen;
import org.yess.duckderby.game.Assets;
import org.yess.duckderby.game.Level;
import org.yess.duckderby.game.models.Duck;
import org.yess.duckderby.game.models.Pond;
import org.yess.duckderby.game.models.components.Speed;
import org.yess.duckderby.gameworld.GameRenderer;
import org.yess.duckderby.gameworld.GameWorld;
import org.yess.framework.Graphics;
import org.yess.framework.Input.TouchEvent;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;

public class GameScreen extends Screen {

	enum GameState {
		Ready, Running, Paused, LevelCleared, GameOver
	}

	private final String TAG = GameScreen.class.getSimpleName();

	private GameState state = GameState.Ready;
	private ArrayList<Duck> ducks;
	private ArrayList<Image> duckCharacters;
	private Pond pond;
	private Image pondImage;
	private Image sound;

	private Random random;
	private boolean newGame;
	private Paint paint, paint2, paint3, paint4, paint5, paint6, paint7, paint8;
	
	private long startTime;
	private String time;
	private int myScore = 0;
	private int myLevel = 0;
	private boolean paused = false;
	private boolean newHighScore;
	private boolean isMuted;
	private ArrayList<String> funFactsA;
	private ArrayList<String> funFactsB;
	private String funFactA;
	private String funFactB;

	private Timer timer;
	private MyTimerTask timerTask;

	private Level level;

	int timeLeft;
	private boolean gameOver;

	public GameScreen(Game game) {
		super(game);

		newGame = true;
		random = new Random();
		state = GameState.Ready;
		sound = Assets.sound;
		startGame();
	}

	private void getLevel() {
		level = new Level();
		if (Level.newGame) {
			level.start();
			Level.newGame = false;
		} else if (Level.isRestart) {
			level.restart();
		} else {
			level.getNextLevel();
		}

		myLevel = level.currentLevel;
		myScore = Level.score;
	}

	private void startGame() {
		timer = new Timer();
		getLevel();
		loadGame();
	}

	private void loadGame() {
		timeLeft = level.time;
		myScore = Level.score;
		createPond();
		createDucks();
		definePaintObjects();
		createFunFacts();
	}

	private void definePaintObjects() {
		// Defining a paint object
		paint = new Paint();
		paint.setTextSize(30);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);

		paint2 = new Paint();
		paint2.setTextSize(50);
		paint2.setTextAlign(Paint.Align.CENTER);
		paint2.setAntiAlias(true);
		paint2.setColor(Color.WHITE);

		paint3 = new Paint();
		paint3.setTextSize(30);
		paint3.setTextAlign(Paint.Align.CENTER);
		paint3.setAntiAlias(true);
		paint3.setColor(Color.YELLOW);

		paint4 = new Paint();
		paint4.setTextSize(20);
		paint4.setTextAlign(Paint.Align.LEFT);
		paint4.setAntiAlias(true);
		paint4.setColor(Color.YELLOW);

		paint5 = new Paint();
		paint5.setTextSize(20);
		paint5.setTextAlign(Paint.Align.CENTER);
		paint5.setAntiAlias(true);
		paint5.setColor(Color.YELLOW);
		
		paint6 = new Paint();
		paint6.setTextSize(30);
		paint6.setTextAlign(Paint.Align.CENTER);
		paint6.setAntiAlias(true);
		paint6.setColor(Color.GREEN);
		
		paint7 = new Paint();
		paint7.setTextSize(30);
		paint7.setTextAlign(Paint.Align.CENTER);
		paint7.setAntiAlias(true);
		paint7.setColor(Color.BLACK);
		
		paint8 = new Paint();
		paint8.setTextSize(30);
		paint8.setTextAlign(Paint.Align.CENTER);
		paint8.setAntiAlias(true);
		paint8.setColor(Color.YELLOW);
		
	}

	private void createDucks() {
		ducks = new ArrayList<Duck>();
		duckCharacters = new ArrayList<Image>();
		random = new Random();
		Image duckImg = Assets.duckLeft;
		for (int i = 0; i < level.duckCount; i++) {
			Duck duck = null;
			boolean colliding = false;

			do {
				int speedX = random.nextInt(level.speedMax) + level.speedMin;
				int speedY = random.nextInt(level.speedMax) + level.speedMin;
				int startAX = random.nextInt(400) + 1;
				int startAY = random.nextInt(700) + 35;
				Speed s1 = new Speed(speedX, speedY);
				duck = new Duck(duckImg, startAX, startAY, s1);

				for (Duck otherDuck : ducks) {
					colliding = duck.isColliding(otherDuck);
				}

			} while (duck.inPond || duck.isColliding(pond) || colliding);

			ducks.add(duck);
		}

		for (Duck duck : ducks) {
			duckCharacters.add(Assets.duckLeft);
		}
	}

	private void createPond() {
		pondImage = Assets.pond;

		int pondX = random.nextInt(330);
		int pondY = random.nextInt(670);

		pond = new Pond(pondImage, pondX, pondY);
	}

	private void createFunFacts()
	{
		funFactsA = new ArrayList<String>();
		funFactsB = new ArrayList<String>();
		funFactsA.add("YESS stands for");
		funFactsB.add("Youth Emergency Services and Shelter.");
	    funFactsA.add("YESS helps children from");
	    funFactsB.add("newborn to age 17.");
	    funFactsA.add("YESS provides emergency shelter,");
	    funFactsB.add("crisis intervention and counseling.");
	    funFactsA.add("Adopt a duck.");
	    funFactsB.add("Help a child.");
	    funFactsA.add("YESS helps children whose"); 
	    funFactsB.add("home is not always a safe option.");
	    funFactsA.add("YESS is open 24 hours a day,"); 
	    funFactsB.add("7 days a week, 365 days a year.");
	    funFactsA.add("YESS hosts a REAL duck derby"); 
	    funFactsB.add("every first Saturday in May.");
	    
	}
	
	private void countDown() {
		if (!paused)
		{
			timeLeft -= 1;
			if ( timeLeft == 0)
			{
				newHighScore = checkHighScore();				
			}
		}
		
		String[] funFact = getFunFact();
		funFactA = funFact[0];
		funFactB = funFact[1];
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		switch (state)
		{
		case Ready:
			updateReady(touchEvents);
			break;
		case Running:
			updateRunning(touchEvents, deltaTime);
			break;
		case Paused:
			updatePaused(touchEvents);
			break;
		case LevelCleared:
			updateLevelCleared(touchEvents);
			break;
		case GameOver:
			updateGameOver(touchEvents);
			break;
		default:
			break;
		}
	}

	@Override
	public void paint(float deltaTime) {
		if (gameOver)
		{		
			return;
		}
		
		Graphics g = game.getGraphics();
		time = Integer.toString(timeLeft);

		// Draw background
		g.drawImage(Assets.grass, 0, 0);

		// draw pond
		g.drawImage(pond.getImage(), pond.getX(), pond.getY());

		// Draw ducks
		for (Duck duck : ducks) {
			g.drawImage(duck.getImage(), duck.getX(), duck.getY());
		}
		
		myScore = Level.score;

		g.drawRect(0, 0, 480, 35, Color.argb(155, 0, 0, 0));
		g.drawString(time, 240, 27, paint3);
		g.drawString("Level: " + myLevel, 10, 27, paint4);
		g.drawString("Score: " + myScore, 375, 27, paint5);

		if (timeLeft == 0) {
			paused = true;
			state = GameState.GameOver;			
		}

		// update UI
		if (state == GameState.Ready)
			drawReadyUI();
		if (state == GameState.Running)
			drawRunningUI();
		if (state == GameState.Paused)
			drawPausedUI();		
		if (state == GameState.LevelCleared)
			drawLevelCleared();
		if (state == GameState.GameOver)
			drawGameOverUI();
	}

	private void updateReady(List<TouchEvent> touchEvents) {
		if (newGame) {
			touchEvents.clear();
			newGame = false;
			return;
		}

		if (touchEvents.size() > 0) {
			state = GameState.Running;

			timerTask = new MyTimerTask();
			timer.scheduleAtFixedRate(timerTask, 1000, 1000);
		}
	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {

		// 1. All touch input is handled here:
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				for (Duck duck : ducks) {
					if (!duck.inPond) {
						duck.checkTouch(event.x, event.y);
					}
				}
				
				if (inBounds(event, 0, 35, 48, 48))
				{
					Log.d(TAG, "Mute/Unmute");
					if (isMuted)
					{
						Duck.unmute();
						sound = Assets.sound;
						isMuted = false;
					}
					else
					{
						Duck.mute();
						sound = Assets.mute;
						isMuted = true;
					}
				}
			}

			if (event.type == TouchEvent.TOUCH_DRAGGED) {
				for (Duck duck : ducks) {
					if (duck.isTouched()) {
						duck.set(event.x, event.y);
					}
				}
			}

			if (event.type == TouchEvent.TOUCH_UP) {
				for (Duck duck : ducks) {
					if (duck.isTouched()) {
						duck.setTouched(false);
					}

					if (duck.checkInPond(pond)) {
						if (!duck.inPond) {
							duck.inPond = true;
							Level.score += 10;
							pond.addDuck();
						}
					}
				}
			}
		}

		if (pond.ducksInPond == level.duckCount) {
			state = GameState.LevelCleared;
			paused = true;
		}

		checkCollisions();
	}

	private void checkCollisions() {
		for (int i = 0; i < ducks.size(); i++) {
			Duck duck = ducks.get(i);
			duck.update();
			duck.checkScreenCollision(480, 800);

			if (duck.isTouched()) {
				continue;
			}

			for (int j = i; j < ducks.size(); j++) {
				if (i != j) {
					Duck otherDuck = ducks.get(j);
					if (!duck.inPond && duck.isColliding(otherDuck)) {
						duck.bounceOff(otherDuck);
					}
				}
			}
			if (duck.isColliding(pond)) {
				duck.bounceOff(pond);
			}
		}
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	private void updatePaused(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 0, 0, 480, 400)) {

					if (!inBounds(event, 0, 0, 35, 35)) {
						resume();
					}
				}

				if (inBounds(event, 0, 240, 480, 400)) {
					gameOver = true;
					nullify();
					goToMenu();

				}
			}
		}
	}

	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		gameOver = true;
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				if (event.type == TouchEvent.TOUCH_DOWN) {
					if (inBounds(event, 165, 550, 150, 50)) {
					Level.isRestart = true;
					game.setScreen(new GameScreen(game));
					}
					if (inBounds(event, 165, 625, 150, 50))
					{
						Level.isRestart = true;
						goToMenu();
					}					
					this.dispose();
				}
			}
		}
	}

	private void updateLevelCleared(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				if (event.type == TouchEvent.TOUCH_DOWN) {
					if (inBounds(event, 0, 0, 480, 800)) {
						Level.isRestart = false;
						game.setScreen(new GameScreen(game));
						this.dispose();
					}
				}
			}
		}
	}

	private void nullify() {

		// Set all variables to null. You will be recreating them in the
		// constructor.
		paint = null;
		paint2 = null;
		paint3 = null;
		paint4 = null;
		paint5 = null;
		ducks = null;
		duckCharacters = null;
		random = null;
		pond = null;
		level = null;
		timerTask.cancel();

		// Call garbage collector to clean up memory.
		System.gc();
	}

	private void drawReadyUI() {
		Graphics g = game.getGraphics();

		g.drawARGB(155, 0, 0, 0);
		g.drawString("Tap to Start.", 240, 400, paint);
	}

	private void drawRunningUI() {
		Graphics g = game.getGraphics();
		
		g.drawImage(sound, 0, 35);
	}

	private void drawPausedUI() {
		Graphics g = game.getGraphics();
		// Darken the entire screen so you can display the Paused screen.
		g.drawARGB(155, 0, 0, 0);
		g.drawString("Resume", 240, 200, paint2);
		g.drawString("Menu", 240, 360, paint2);

	}

	private void drawLevelCleared() {
		Graphics g = game.getGraphics();

		g.drawARGB(155, 0, 0, 0);
		g.drawString("You cleared level " + myLevel, 240, 350, paint);
		g.drawString("Tap to start next level", 240, 500, paint);
	}

	private void drawGameOverUI() {
		Graphics g = game.getGraphics();
		g.drawRect(0, 0, 480, 800, Color.BLACK);
		g.drawRect(165, 550, 150, 50, Color.GREEN);
		g.drawRect(165, 625, 150, 50, Color.RED);
		g.drawString("GAME OVER.", 240, 50, paint2);
		g.drawString("Restart", 240, 585, paint7);
		g.drawString("Quit", 240, 660, paint7);
		
		String lossMessage = "You lost in this level!!";
		String lossMessage2 = "But there is always next time :) !!";
		g.drawString(lossMessage, 240, 290, paint8);
		g.drawString(lossMessage2, 240, 325, paint8);			
		
		if (newHighScore)
		{
			g.drawString("You got a new high score!!", 240, 170, paint6);
			g.drawString("" + myScore, 240	, 220, paint2);
		}
		
		g.drawString(funFactA,	240, 740, paint5);
		g.drawString(funFactB, 240, 770, paint5);
	}

	@Override
	public void pause() {
		if (state == GameState.Running) {
			state = GameState.Paused;
			paused = true;
		}
	}

	@Override
	public void resume() {
		if (state == GameState.Paused) {
			state = GameState.Running;
			paused = false;
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void backButton() {
		if (!paused) {
			pause();
		} else {
			level.clear();
			goToMenu();
		}
	}

	private void goToMenu() {
		game.quit();
		Level.isRestart = true;
		this.dispose();
	}

	private boolean checkHighScore()
	{
		return game.receivedHighScore(myScore);
	}
	
	private String[] getFunFact()
	{		
	    int index = random.nextInt(7);
	    String funFactA = funFactsA.get(index);
	    String funFactB = funFactsB.get(index);
	    
	    String[] funFact = new String[2];
	    funFact[0] = funFactA;
	    funFact[1] = funFactB;
	    
	    return funFact;	    
	}
	
	public class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			countDown();
		}
	}
}
