package de.swagner.ld25.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.swagner.ld25.GameInstance;
import de.swagner.ld25.GameScreen;

public class RenderGUI {

	SpriteBatch batch;
	BitmapFont font;

	public RenderGUI() {
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480);
		font = new BitmapFont(Gdx.files.internal("data/default.fnt"), false);
		font.setScale(2);
	}

	public void render() {
		batch.begin();
		font.draw(batch, GameScreen.sheepsCaptured + "/10", 20, 460);
		batch.end();

	}
	
	public void resize(int width, int height) {
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);

	}

}
