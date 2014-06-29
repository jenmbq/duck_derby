package org.yess.duckderby.game;

import org.yess.duckderby.game.helpers.AssetLoader;
import org.yess.duckderby.screens.HighScoreScreen;
import org.yess.framework.Screen;
import org.yess.framework.implementation.AndroidGame;

import android.content.SharedPreferences;

public class HighScore extends AndroidGame {

	@Override
	public Screen getInitScreen() {
		AssetLoader.load(this);
		return new HighScoreScreen(this);
	}
	
	@Override
	public int getHighScore()
	{
		SharedPreferences prefs = this.getSharedPreferences("DuckDerby", MODE_PRIVATE);
		int highScore = prefs.getInt("HighScore", 0);		
		
		return highScore;
	}

	@Override
	public boolean receivedHighScore(int score) {
		return false;
	}
}
