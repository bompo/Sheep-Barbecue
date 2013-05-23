package de.swagner.ld25;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

import de.swagner.ld25.renderer.RenderFarm;
import de.swagner.ld25.renderer.RenderGUI;
import de.swagner.ld25.renderer.RenderMainMenu;
import de.swagner.ld25.renderer.RenderWin;

public class WinScreen  extends DefaultScreen implements InputProcessor {
	
	private RenderFarm renderFarm;
	private RenderWin renderGUI;
	
	SpriteBatch batch;
	SpriteBatch fadeBatch;
	SpriteBatch fontbatch;
	BitmapFont font;
	Sprite blackFade;

	float fade = 1.0f;
	boolean finished = false;
	float delta;
	
	public WinScreen(Game game) {	
		super(game);
		
		GameInstance.getInstance().resetGame();
		
		Gdx.input.setInputProcessor(this);
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 800, 480);
		fontbatch = new SpriteBatch();

		blackFade = new Sprite(
				new Texture(Gdx.files.internal("data/black.png")));
		fadeBatch = new SpriteBatch();
		fadeBatch.getProjectionMatrix().setToOrtho2D(0, 0, 1, 1);
		
		GameInstance.getInstance().camera = new PerspectiveCamera(45,w,h);
		GameInstance.getInstance().camera.near = 0.1f;
		GameInstance.getInstance().camera.far = 1000f;
		GameInstance.getInstance().camera.position.set(20, 1f, 20f);
		GameInstance.getInstance().camera.lookAt(0, 0.1f, 0f);
		GameInstance.getInstance().camera.update();
	
		renderGUI = new RenderWin();
		
		for(int i = 0; i <= 100; i++) {
			Vector3 position = new Vector3();
			boolean positionFound = true;
			while(positionFound) {
				positionFound = false;
				position = new Vector3(MathUtils.random(GameInstance.getInstance().min, GameInstance.getInstance().max), MathUtils.random(0, 0), MathUtils.random(GameInstance.getInstance().min, GameInstance.getInstance().max));
				for(int j = 0; j < GameInstance.getInstance().traps.size; j++) {
					if(position.dst(GameInstance.getInstance().traps.get(j).position) < 8) {
						positionFound = true;
						break;
					}			
				}
			}
			position.y = position.y + 1;
			GameInstance.getInstance().traps.add(new Trap(position));
		}
		
		
		for(int i = 0; i <= 300; i++) {
			Vector3 position = new Vector3();
			boolean positionFound = true;
			while(positionFound) {
				positionFound = false;
				position = new Vector3(MathUtils.random(GameInstance.getInstance().min*3, GameInstance.getInstance().max*3), MathUtils.random(0, 0), MathUtils.random(GameInstance.getInstance().min*3, GameInstance.getInstance().max*3));
				for(int j = 0; j < GameInstance.getInstance().trees.size; j++) {
					if(position.dst(GameInstance.getInstance().trees.get(j).position) < 8) {
						positionFound = true;
						break;
					}			
				}
			}
			GameInstance.getInstance().trees.add(new Tree(position, MathUtils.random(1.2f, 1.7f)));
				
		}
		
		GameInstance.getInstance().barn = new Barn(new Vector3(0,-0.05f,0), new Vector3(-1,0,0).nor());
		GameInstance.getInstance().hill = new Hill(new Vector3(0,0,0), new Vector3(-1,0,0).nor());
		GameInstance.getInstance().claws = new Claws(GameInstance.getInstance().camera.position, new Vector3(-1,0,0).nor());
		
		
		renderFarm = new RenderFarm();
		
		for(int i = 0; i < 200; i++) {
			addGrassPatch(true);
		}
	}

	private void addGrassPatch(boolean init) {
		Vector3 position = new Vector3();
		boolean positionFound = true;
		if(init) {
			position = new Vector3(MathUtils.random(GameInstance.getInstance().camera.position.x - 3, GameInstance.getInstance().camera.position.x + 3), MathUtils.random(0, 0), MathUtils.random(GameInstance.getInstance().camera.position.z - 3, GameInstance.getInstance().camera.position.z + 3));
		} else {
			while(positionFound) {
				positionFound = false;
				position = new Vector3(MathUtils.random(GameInstance.getInstance().camera.position.x - 3, GameInstance.getInstance().camera.position.x + 3), MathUtils.random(0, 0), MathUtils.random(GameInstance.getInstance().camera.position.z - 3, GameInstance.getInstance().camera.position.z + 3));
				
				if(position.dst(GameInstance.getInstance().camera.position)<2) {
					positionFound = true;
				}
			}
		}
			
		Grass grass = new Grass(position, new Vector3(MathUtils.random(-1.f, 1.f), MathUtils.random(0, 0), MathUtils.random(-1.f, 1.f)).nor(), MathUtils.random(1.2f, 1.7f));

		Ray ray = new Ray(new Vector3(grass.position.x, -100, grass.position.z), Vector3.Y);
		Vector3 localIntersection = new Vector3();
		if (Intersector.intersectRayTriangles(ray, GameInstance.getInstance().heightMap.map,localIntersection)) {
		}
		localIntersection.y -= 0.05f;
		grass.position.set(localIntersection);

		grass.update();

		GameInstance.getInstance().grasses.add(grass);		
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render(float delta) {		
		
		
		Gdx.gl.glClearColor(0.6f, 0.7f, 1, 1);
//		Gdx.gl.glClearColor(0.3f, 0.7f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT
				| (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		
		Gdx.gl.glEnable(GL10.GL_CULL_FACE);
		Gdx.gl.glFrontFace(GL10.GL_CW);
		Gdx.gl.glCullFace(GL10.GL_FRONT);

		Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
		Gdx.gl.glDepthMask(true);
		
		for(int i = 0; i < GameInstance.getInstance().sheeps.size; i++) {
			GameInstance.getInstance().sheeps.get(i).update();
		}
		
		for(int i = 0; i < GameInstance.getInstance().traps.size; i++) {
			GameInstance.getInstance().traps.get(i).update();
		}
		
		//check if all grass patches are in view range, if not remove and add new one
		for(int i = 0; i < GameInstance.getInstance().grasses.size; i++) {
			GameInstance.getInstance().grasses.get(i).update(GameInstance.getInstance().camera.position);
			
			if(GameInstance.getInstance().grasses.get(i).position.dst(GameInstance.getInstance().camera.position)>3) {
				GameInstance.getInstance().grasses.removeIndex(i);
				addGrassPatch(false);
			}
		}	
		
		
		//check if a sheep sees you!
		for(int i = 0; i < GameInstance.getInstance().sheeps.size; i++) {
			
			//collision detection with cam
			Ray collisionRay = new Ray(GameInstance.getInstance().sheeps.get(i).position.cpy(), GameInstance.getInstance().sheeps.get(i).direction);
			Vector3 collision = new Vector3();
			if (Intersector.intersectRaySphere(collisionRay, GameInstance.getInstance().camera.position.cpy(), 2, collision)) {
				if(GameInstance.getInstance().camera.position.dst(GameInstance.getInstance().sheeps.get(i).position) < 15) {
					GameInstance.getInstance().sheeps.get(i).setAlerted(true);
				}
			}		
			
		}	
		
//		claws.position.set(camera.position);
//		claws.position.y -= .1f;
//		claws.position.z += .1f;
//		claws.update();
		
		GameInstance.getInstance().camera.update();
		renderFarm.updateCamera(GameInstance.getInstance().camera);
		
		renderFarm.render();
		
		renderGUI.render();
		
		
		// FadeInOut
		if (!finished && fade > 0) {
			fade = Math.max(fade - (delta), 0);
			fadeBatch.begin();
			blackFade.setColor(blackFade.getColor().r, blackFade.getColor().g,
					blackFade.getColor().b, fade);
			blackFade.draw(fadeBatch);
			fadeBatch.end();
		}

		if (finished) {
			fade = Math.min(fade + (delta), 1);
			fadeBatch.begin();
			blackFade.setColor(blackFade.getColor().r, blackFade.getColor().g,
					blackFade.getColor().b, fade);
			blackFade.draw(fadeBatch);
			fadeBatch.end();
			if (fade >= 1) {
				game.setScreen(new MenuScreen(game));
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		GameInstance.getInstance().camera.viewportHeight = height;
		GameInstance.getInstance().camera.viewportWidth = width;
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public static void tryToCatch() {
		for(int i = 0; i < GameInstance.getInstance().sheeps.size; i++) {
			
			if(GameInstance.getInstance().camera.position.dst(GameInstance.getInstance().sheeps.get(i).position)< 2) {

				GameInstance.getInstance().traps.add(new Trap(GameInstance.getInstance().sheeps.get(i).position.cpy()));
				
				GameInstance.getInstance().sheeps.removeIndex(i);
				
				
			}		
			
		}	
	}


	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean keyDown(int keycode) {
		finished = true;
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		finished = true;
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
