package org.yess.duckderby.game;

import org.yess.framework.Screen;
import org.yess.framework.implementation.AndroidGame;
import org.yess.duckderby.game.helpers.AssetLoader;
import org.yess.duckderby.screens.GameScreen;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class DuckDerbyGame extends AndroidGame{
	
	private final String TAG = DuckDerbyGame.class.getSimpleName();

	@Override
	public Screen getInitScreen() {
		AssetLoader.load(this);		
		// Change to menu after game is built
		return new GameScreen(this);
	}
	
	@Override
	public void onBackPressed()
	{
		getCurrentScreen().backButton();
	}
	
	@Override
	public void restart()
	{
		super.restart();
	}
	
	@Override 
	public void quit()
	{
		super.quit();
	}
	
	@Override
	public boolean receivedHighScore(int score)
	{
		SharedPreferences prefs = this.getSharedPreferences("DuckDerby", MODE_PRIVATE);
		int highScore = prefs.getInt("HighScore", 0);		
		
		if (score > highScore)
		{
			Editor editor = prefs.edit();
			editor.putInt("HighScore", score);
			editor.commit();
			return true;
		}
		
		return false;
	}

	@Override
	public int getHighScore() {
		SharedPreferences prefs = this.getSharedPreferences("DuckDerby", MODE_PRIVATE);
		int highScore = prefs.getInt("HighScore", 0);		
		return highScore;
	}
}
