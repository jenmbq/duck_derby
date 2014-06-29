package org.yess.duckderby.game.helpers;

import org.yess.duckderby.game.Assets;
import org.yess.framework.Game;
import org.yess.framework.Graphics;
import org.yess.framework.Graphics.ImageFormat;
import org.yess.framework.Image;
import org.yess.framework.Sound;

public class AssetLoader {

	private static Game myGame;
	
	public static void load(Game game) {		
		myGame = game;
		
		loadImages();
		loadSounds();		
	}
	
	private static void loadImages()
	{
		Graphics g = myGame.getGraphics();
		
		Assets.duckLeft = g.newImage("images/duck-left.png", ImageFormat.ARGB4444);
		Assets.duckRight = g.newImage("images/duck-right.png", ImageFormat.ARGB4444);
		Assets.pond = g.newImage("images/pond.png", ImageFormat.ARGB4444);
		Assets.grass = g.newImage("images/grass.png", ImageFormat.ARGB8888);
		Assets.logo = g.newImage("images/duck_derby_logo.png", ImageFormat.ARGB4444);
		Assets.sound = g.newImage("images/sound.png", ImageFormat.ARGB4444);
		Assets.mute = g.newImage("images/mute.png", ImageFormat.ARGB4444);
	}
	
	private static void loadSounds()
	{
		Assets.quack = myGame.getAudio().createSound("sounds/quack.wav");
	}
}
