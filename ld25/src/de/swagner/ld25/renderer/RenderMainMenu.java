package de.swagner.ld25.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderMainMenu {

	SpriteBatch batch;
	BitmapFont font;

	public RenderMainMenu() {
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480);
		font = new BitmapFont(Gdx.files.internal("data/default.fnt"), false);
		font.setScale(2);
	}

	public void render() {
		batch.begin();
		font.setScale(2);
		font.draw(batch, "Sheep Barbecue", 20, 460);
		font.setScale(1);
		font.draw(batch, "a game by @twbompo for LD25", 560, 40);
		
		font.drawMultiLine(batch, "Sneak behind the sheeps to catch them,\ndon't let them see you!\nYou can stand up to get a better overview,\nbut beware that the sheeps can spot you more easy!\n\nControls\npress and move mouse to look\npress SHIFT to stand up\npress SPACE to catch sheep", 20, 200);
		
		batch.end();
	}
	
	public void resize(int width, int height) {
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);

	}

}
