package de.swagner.ld25;

import com.badlogic.gdx.backends.lwjgl.LwjglApplet;

public class AppletMain extends LwjglApplet {
	private static final long serialVersionUID = 1L;

	public AppletMain() {
		super(new DesktopStarter(), true);
	}
}
