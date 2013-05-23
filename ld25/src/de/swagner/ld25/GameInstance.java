package de.swagner.ld25;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.Array;

public class GameInstance {

	public static GameInstance instance;
	
	public Array<Sheep> sheeps;
	public Array<Trap> traps;
	public Array<Tree> trees;
	public Array<Grass> grasses;
	public Barn barn;
	public Hill hill;
	public Claws claws;
	public HeightMap heightMap;
	public float min;
	public float max;	
	public static PerspectiveCamera camera;

	Sound sneak01;
	Sound sneak02;
	Sound sneak03;
	Sound sneak04;
	Sound mah01;
	Sound mah02;
	Sound alert01;
	Sound win;
	
	private GameInstance() {
		sheeps = new Array<Sheep>(20);
		traps = new Array<Trap>(20);
		trees = new Array<Tree>();
		grasses = new Array<Grass>(201);
		
		min = -50;
		max = 50;
		
		sneak01 = Gdx.audio.newSound(Gdx.files.internal("data/sneak_v2_01.ogg"));
		sneak02 = Gdx.audio.newSound(Gdx.files.internal("data/sneak_v2_02.ogg"));
		sneak03 = Gdx.audio.newSound(Gdx.files.internal("data/sneak_v2_03.ogg"));
		sneak04 = Gdx.audio.newSound(Gdx.files.internal("data/sneak_v2_04.ogg"));
		
		mah01 = Gdx.audio.newSound(Gdx.files.internal("data/mah_01.ogg"));
		mah02 = Gdx.audio.newSound(Gdx.files.internal("data/mah_02.ogg"));
		
		alert01 = Gdx.audio.newSound(Gdx.files.internal("data/alert_01.ogg"));
		
		win = Gdx.audio.newSound(Gdx.files.internal("data/win.ogg"));
	}
	
	public static GameInstance getInstance() {
		if (instance == null) {
			instance = new GameInstance();
		}
		return instance;
	}

	public void resetGame() {
		sheeps.clear();
		traps.clear();
		trees.clear();
		grasses.clear();
		sheeps = new Array<Sheep>(20);
		traps = new Array<Trap>(20);
		trees = new Array<Tree>();
		grasses = new Array<Grass>(201);
	}
}