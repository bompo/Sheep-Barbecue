package de.swagner.ld25.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderWin {

	SpriteBatch batch;
	BitmapFont font;

	public RenderWin() {
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480);
		font = new BitmapFont(Gdx.files.internal("data/default.fnt"), false);
		font.setScale(2);
	}

	public void render() {

		batch.begin();
		font.setScale(2);
		font.draw(batch, "You are not hungry anymore!", 20, 460);		
		batch.end();
	}
	
	public void resize(int width, int height) {
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);

	}

}
