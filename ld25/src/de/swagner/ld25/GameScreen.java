package de.swagner.ld25;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

import de.swagner.ld25.renderer.RenderFarm;
import de.swagner.ld25.renderer.RenderGUI;

public class GameScreen  extends DefaultScreen {

	private PlayerControls controls;
	
	private RenderFarm renderFarm;
	private RenderGUI renderGUI;
	
	SpriteBatch batch;
	SpriteBatch fadeBatch;
	SpriteBatch fontbatch;
	BitmapFont font;
	Sprite blackFade;
	
	PerspectiveCamera customCullingCamera;
	
	float lastSoundPlayed = 1;
	Array<Long> playedSounds = new Array<Long>();
	
	float distanceSound = 0;
	int currentDistanceSound = 0;
	
	float oldDistance = 0;
	Vector3 oldPosition = new Vector3();
	
	float fade = 1.0f;
	static boolean finished = false;
	float delta;
	
	int grassesNearby = 0;
	public static int sheepsCaptured = 0;
	
	public GameScreen(Game game) {	
		super(game);
		finished = false;
		
		GameInstance.getInstance().resetGame();
		
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
		GameInstance.getInstance().camera.position.set(25, 1f, 40f);
		GameInstance.getInstance().camera.lookAt(0, 0.1f, 0f);
		GameInstance.getInstance().camera.update();
		
		customCullingCamera = new PerspectiveCamera(45,w,h);
		customCullingCamera.near = 0.1f;
		customCullingCamera.far = 1000f;
		customCullingCamera.position.set(25, 1f, 40f);
		customCullingCamera.lookAt(0, 0.1f, 0f);
		customCullingCamera.update();
		
		controls = new PlayerControls(GameInstance.getInstance().camera);
		Gdx.input.setInputProcessor(controls);
		
		renderGUI = new RenderGUI();
		
		for(int i = 0; i <= 19; i++) {
			Vector3 position = new Vector3();
			boolean positionFound = true;
			while(positionFound) {
				positionFound = false;
				position = new Vector3(MathUtils.random(GameInstance.getInstance().min, GameInstance.getInstance().max), MathUtils.random(0, 0), MathUtils.random(GameInstance.getInstance().min, GameInstance.getInstance().max));
				for(int j = 0; j < GameInstance.getInstance().sheeps.size; j++) {
					if(position.dst(GameInstance.getInstance().sheeps.get(j).position) < 8) {
						positionFound = true;
						break;
					}			
				}
			}
			GameInstance.getInstance().sheeps.add(new Sheep(position, new Vector3(MathUtils.random(-1.f, 1.f), MathUtils.random(0, 0), MathUtils.random(-1.f, 1.f)).nor()));
				
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
		if(GameInstance.getInstance().grasses.size > 1500) return;
		
		Vector3 position = new Vector3();
		boolean positionFound = true;
		if(init) {
			position = new Vector3(MathUtils.random(GameInstance.getInstance().camera.position.x - 30, GameInstance.getInstance().camera.position.x + 30), MathUtils.random(0, 0), MathUtils.random(GameInstance.getInstance().camera.position.z - 30, GameInstance.getInstance().camera.position.z + 30));
		} else {
			while(positionFound) {
				positionFound = false;
				position = new Vector3(MathUtils.random(GameInstance.getInstance().camera.position.x - 30, GameInstance.getInstance().camera.position.x + 30), MathUtils.random(0, 0), MathUtils.random(GameInstance.getInstance().camera.position.z - 30, GameInstance.getInstance().camera.position.z + 30));
				
				if(!GameInstance.getInstance().camera.frustum.sphereInFrustum(position, 5)) {
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
		
		lastSoundPlayed -= delta;
		
		if(lastSoundPlayed < 1 && MathUtils.random(0, 1000) == 0) {
			int randomSound = MathUtils.random(0, 1);
			
			GameInstance.getInstance().mah01.stop();
			GameInstance.getInstance().mah02.stop();
			
			if(randomSound == 0) {
				playedSounds.add(GameInstance.getInstance().mah01.play(MathUtils.random(0.1f, 0.4f)));
			}
			if(randomSound == 1) {
				playedSounds.add(GameInstance.getInstance().mah02.play(MathUtils.random(0.1f, 0.4f)));
			}
			lastSoundPlayed = 2;
		} 
		
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
		
		oldPosition.set(GameInstance.getInstance().camera.position);
		
		controls.update(Gdx.graphics.getDeltaTime());
		
		distanceSound += GameInstance.getInstance().camera.position.dst(oldPosition);
		
		
		if(distanceSound > 1.2f) {

			currentDistanceSound += 1;

			GameInstance.getInstance().sneak01.stop();
			GameInstance.getInstance().sneak02.stop();
			GameInstance.getInstance().sneak03.stop();
			GameInstance.getInstance().sneak04.stop();
			
			if(currentDistanceSound == 4) {
				playedSounds.add(GameInstance.getInstance().sneak01.play(0.5f));
				currentDistanceSound  = 0;
			} else if(currentDistanceSound == 3) {
				playedSounds.add(GameInstance.getInstance().sneak02.play(0.5f));
			} else if(currentDistanceSound == 2) {
				playedSounds.add(GameInstance.getInstance().sneak03.play(0.5f));
			} else if(currentDistanceSound == 1) {
				playedSounds.add(GameInstance.getInstance().sneak04.play(0.5f));

			}
			
			distanceSound = 0;
		}
		
		//collision detection with cam
		Ray ray = new Ray(new Vector3( GameInstance.getInstance().camera.position.x, -100,  GameInstance.getInstance().camera.position.z), Vector3.Y);
		Vector3 localIntersection = new Vector3();
		if (Intersector.intersectRayTriangles(ray, GameInstance.getInstance().heightMap.map, localIntersection)) {
		}
		if(controls.standing) {
			localIntersection.y += 0.5f;
		} else {
			localIntersection.y += 0.15f;
		}
		
		GameInstance.getInstance().camera.position.set(localIntersection);
		
		// update custom culling cam
		customCullingCamera.position.set(GameInstance.getInstance().camera.position);
		customCullingCamera.direction.set(GameInstance.getInstance().camera.direction);
		customCullingCamera.direction.y = 0;
		customCullingCamera.direction.nor();
		customCullingCamera.update();
		
		//check if all grass patches are in view range, if not remove and add new one
		for(int i = 0; i < GameInstance.getInstance().grasses.size; i++) {
			GameInstance.getInstance().grasses.get(i).update(customCullingCamera.position);
			
			if(!customCullingCamera.frustum.sphereInFrustum(GameInstance.getInstance().grasses.get(i).position, 10)) {
				GameInstance.getInstance().grasses.removeIndex(i);
				addGrassPatch(false);
			}
		}	
		
		if(grassesNearby<10) {
			grassesNearby = 0;
			for(int j = 0; j < GameInstance.getInstance().grasses.size; j++) {
				if(customCullingCamera.position.dst(GameInstance.getInstance().grasses.get(j).position) < 1) {
					grassesNearby++;
				}
				
				if(grassesNearby>10) break;
				else addGrassPatch(false);
			}
		}
		
		
		//check if a sheep sees you!
		for(int i = 0; i < GameInstance.getInstance().sheeps.size; i++) {
			//collision detection with cam
			Ray collisionRay = new Ray(GameInstance.getInstance().sheeps.get(i).position.cpy(), GameInstance.getInstance().sheeps.get(i).direction);
			Vector3 collision = new Vector3();
			if(controls.standing) {
				if (Intersector.intersectRaySphere(collisionRay, GameInstance.getInstance().camera.position.cpy(), 10, collision)) {
					if(GameInstance.getInstance().camera.position.dst(GameInstance.getInstance().sheeps.get(i).position) < 30) {
						GameInstance.getInstance().sheeps.get(i).setAlerted(true);
						
						if(lastSoundPlayed < 1) {
							GameInstance.getInstance().alert01.stop();
							playedSounds.add(GameInstance.getInstance().alert01.play((30 - GameInstance.getInstance().camera.position.dst(GameInstance.getInstance().sheeps.get(i).position))/60.f));
							lastSoundPlayed = 2;
						} 
					}
				}
			} else {
				if (Intersector.intersectRaySphere(collisionRay, GameInstance.getInstance().camera.position.cpy(), 2, collision)) {
					if(GameInstance.getInstance().camera.position.dst(GameInstance.getInstance().sheeps.get(i).position) < 15) {
						GameInstance.getInstance().sheeps.get(i).setAlerted(true);
						
						if(lastSoundPlayed < 1) {
							GameInstance.getInstance().alert01.stop();
							playedSounds.add(GameInstance.getInstance().alert01.play((15 - GameInstance.getInstance().camera.position.dst(GameInstance.getInstance().sheeps.get(i).position))/30.f));
							lastSoundPlayed = 2;
						} 
					}
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
				game.setScreen(new WinScreen(game));
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
				GameInstance.getInstance().win.play(0.7f);
				
				sheepsCaptured = sheepsCaptured + 1;
			}		
		}	
		
		if(sheepsCaptured >= 10) {
			finished = true;
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
}
