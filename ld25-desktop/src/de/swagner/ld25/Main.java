package de.swagner.ld25;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Sheep Barbecue";
		cfg.useGL20 = true;
		cfg.samples = 4;
		cfg.width = 1280;
		cfg.height = 720;
		
		new LwjglApplication(new DesktopStarter(), cfg);
	}
}
