package de.swagner.ld25;

import com.badlogic.gdx.Game;

public class DesktopStarter extends Game {

	@Override
	public void create() {
		setScreen(new MenuScreen(this));	
	}

}
