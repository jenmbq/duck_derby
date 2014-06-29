package org.yess.duckderby.screens;

import org.yess.duckderby.game.Assets;
import org.yess.framework.Game;
import org.yess.framework.Graphics;
import org.yess.framework.Image;
import org.yess.framework.Screen;

import android.graphics.Color;
import android.graphics.Paint;

public class HighScoreScreen extends Screen {

	private Paint paint, paint2;
	private Image logo, grass, duck;
	
	public HighScoreScreen(Game game) {
		super(game);
		
		logo = Assets.logo;
		grass = Assets.grass;
		duck = Assets.duckLeft;
		
		createPaint();
		
	}
	
	private void createPaint()
	{
		paint = new Paint();
		paint.setTextSize(30);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);

		paint2 = new Paint();
		paint2.setTextSize(50);
		paint2.setTextAlign(Paint.Align.CENTER);
		paint2.setAntiAlias(true);
		paint2.setColor(Color.YELLOW);
	}

	@Override
	public void update(float deltaTime) {		
		
	}

	@Override
	public void paint(float deltaTime) {
		
		int highScore = game.getHighScore();	
		
		Graphics g = game.getGraphics();
		g.drawImage(grass, 0, 0);
		
		g.drawString("High Score", 240, 150, paint);
		g.drawString("" + highScore, 240, 200, paint2);
		g.drawImage(logo, 0, 400);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void backButton() {
		game.quit();
		this.dispose();
		
	}

}
