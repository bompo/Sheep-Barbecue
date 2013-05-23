package de.swagner.ld25.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import de.swagner.ld25.DesktopStarter;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(800, 480);
		cfg.antialiasing = true;
		cfg.fps = 60;
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		System.out.println("create");
		return new DesktopStarter();
	}
}