package de.swagner.ld25.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import de.swagner.ld25.GameInstance;
import de.swagner.ld25.HeightMap;
import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.lights.LightManager;
import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.lights.LightManager.LightQuality;
import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderRegistry;
import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.materials.BlendingAttribute;
import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.materials.Material;
import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.materials.MaterialAttribute;
import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.test.PrototypeRendererGL20;

public class RenderFarm {
	
	static final int LIGHTS_NUM = 0;
	static final float LIGHT_INTESITY = 0f;
	
	boolean highQuality = false;
	
	static final String TAG = "de.swagner.ld25";

	LightManager lightManager;

	float timer;
	float alpha = 1;
	PrototypeRendererGL20 protoRenderer;
	StillModel modelSheep;	
	Texture modelSheepTex;
	
	StillModel modelBarn;	
	Texture modelBarnTex;
	
	StillModel modelBarnShadow;	
	Texture modelBarnShadowTex;
	
	StillModel modelHill;	
	Texture modelHillTex;
	
	StillModel modelGrass;	
	Texture modelGrassTex;
	
	StillModel modelWarning;
	
	StillModel modelTree;	
	Texture modelTreeTex;
	StillModel modelTreeShadow;	
	Texture modelTreeShadowTex;
	
//	KeyframedModel modelClaws;	
	
	StillModel modelTrap;
	
	// GLES20
	Matrix4 model = new Matrix4().idt();
	Matrix4 normal = new Matrix4().idt();
	Matrix4 tmp = new Matrix4().idt();

	Preferences prefs;


	public RenderFarm() {
		prefs = Gdx.app.getPreferences(TAG);
		
		highQuality = prefs.getBoolean("highQuality", false);
		if(highQuality) {
			lightManager = new LightManager(LIGHTS_NUM, LightQuality.FRAGMENT);
		} else {
			lightManager = new LightManager(LIGHTS_NUM, LightQuality.VERTEX);
		}
		
		lightManager.dirLight = new DirectionalLight();
		lightManager.dirLight.color.set(1f, 1f, 1f, 1);
		lightManager.dirLight.direction.set(-.4f, -1, 0.03f).nor();

		lightManager.ambientLight.set(0.0f, 0.0f, 0.0f, 0f);
		
		protoRenderer = new PrototypeRendererGL20(lightManager);


		modelSheep = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/sheep.g3dt"));
		modelSheepTex = new Texture(Gdx.files.internal("data/sheep_diffuse.png"), true);
		modelSheepTex.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		modelSheepTex.getTextureData().useMipMaps();
		
		modelBarn = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/barn.g3dt"));
		modelBarnTex = new Texture(Gdx.files.internal("data/barn_diffuse.png"), true);
		modelBarnTex.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		modelBarnTex.getTextureData().useMipMaps();
		
		modelBarnShadow = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/barn_shadow.g3dt"));
		modelBarnShadowTex = new Texture(Gdx.files.internal("data/barn_shadow.png"), true);
		modelBarnShadowTex.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		modelBarnShadowTex.getTextureData().useMipMaps();
		
		modelHill = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/hills.g3dt"));
		modelHillTex = new Texture(Gdx.files.internal("data/hills_lightmap.png"), true);
		modelHillTex.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		modelHillTex.getTextureData().useMipMaps();
		
		modelTree = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/tree.g3dt"));
		modelTreeTex = new Texture(Gdx.files.internal("data/tree_diffuse.png"), true);
		modelTreeTex.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		modelTreeTex.getTextureData().useMipMaps();
		
		modelTreeShadow = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/tree_shadow.g3dt"));
		modelTreeShadowTex = new Texture(Gdx.files.internal("data/tree_shadow.png"), true);
		modelTreeShadowTex.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		modelTreeShadowTex.getTextureData().useMipMaps();
		
		modelGrass = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/grass.g3dt"));
		modelGrassTex = new Texture(Gdx.files.internal("data/grass_diffuse.png"), true);
		modelGrassTex.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		modelGrassTex.getTextureData().useMipMaps();
		
		modelWarning = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/alert.g3dt"));
		
		modelTrap = ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/trap.g3dt"));
		
		
//		modelClaws = ModelLoaderRegistry.loadKeyframedModel(Gdx.files.internal("data/claws.g3dt"));
		
		
		GameInstance.getInstance().heightMap = new HeightMap(modelHill);
		
		for(int i = 0; i < GameInstance.getInstance().sheeps.size; i++) {
			BoundingBox bb = new BoundingBox();
			modelSheep.getBoundingBox(bb);
			GameInstance.getInstance().sheeps.get(i).updateBoundingBox(bb);
		}
		
		for(int i = 0; i < GameInstance.getInstance().trees.size; i++) {
			BoundingBox bb = new BoundingBox();
			modelTree.getBoundingBox(bb);
			GameInstance.getInstance().trees.get(i).updateBoundingBox(bb);
		}
		
		{
			BoundingBox bb = new BoundingBox();
			modelBarn.getBoundingBox(bb);
			GameInstance.getInstance().barn.updateBoundingBox(bb);
		}
		
		{
			BoundingBox bb = new BoundingBox();
			modelHill.getBoundingBox(bb);
			GameInstance.getInstance().hill.updateBoundingBox(bb);
		}
		
//		{
//			BoundingBox bb = new BoundingBox();
//			modelClaws.getBoundingBox(bb);
//			LD25.claws.updateBoundingBox(bb);
//			
//			KeyframedAnimation[] animations = modelClaws.getAnimations();
//			LD25.claws.instance.animation = animations[0].name;
//			LD25.claws.instance.time = MathUtils.random(animations[0].totalDuration);
//			LD25.claws.animTime = animations[0].totalDuration;
//			LD25.claws.instance.looping = true;			
//		}
		
		// set materials
		MaterialAttribute fogColor = new ColorAttribute(new Color(0.6f, 0.7f, 1, 1), ColorAttribute.fog);
		
		MaterialAttribute whiteDiffuseColor = new ColorAttribute(new Color(1.0f, 1.0f, 1.0f, 1.0f), ColorAttribute.diffuse);
		
		MaterialAttribute redDiffuseColor = new ColorAttribute(new Color(1.0f,0.1f, 0.1f, 1.0f), ColorAttribute.diffuse);
		
		MaterialAttribute greenDiffuseColor = new ColorAttribute(new Color(0.3f, 0.7f, 0.2f, 1), ColorAttribute.diffuse);
		MaterialAttribute sheepDiffuseTex = new TextureAttribute(modelSheepTex,0,TextureAttribute.diffuseTexture);
		
		MaterialAttribute barnDiffuseTex = new TextureAttribute(modelBarnTex,0,TextureAttribute.diffuseTexture);
		
		MaterialAttribute hillDiffuseTex = new TextureAttribute(modelHillTex,0,TextureAttribute.lightmapTexture);
		
		MaterialAttribute treeDiffuseTex = new TextureAttribute(modelTreeTex,0,TextureAttribute.diffuseTexture);		
		MaterialAttribute treeShadowTex = new TextureAttribute(modelTreeShadowTex,0,TextureAttribute.diffuseTexture);
		
		MaterialAttribute barnShadowTex = new TextureAttribute(modelBarnShadowTex,0,TextureAttribute.diffuseTexture);
		
		MaterialAttribute grassesTex = new TextureAttribute(modelGrassTex,0,TextureAttribute.diffuseTexture);
		
		MaterialAttribute alphaBlending = new BlendingAttribute("translucent");
		
		MaterialAttribute yellowDiffuseColor = new ColorAttribute(new Color(1.0f,0.8f, 0.1f, 1.0f), ColorAttribute.diffuse);
		MaterialAttribute brownDiffuseColor = new ColorAttribute(new Color(0.4f,0.2f, 0.1f, 1.0f), ColorAttribute.diffuse);
		
		
		Material sheepTex = new Material("sheepTex", sheepDiffuseTex, fogColor);
		Material barnTex = new Material("barnTex", barnDiffuseTex, fogColor);
		Material barnShadowMaterial = new Material("barnTexShadow", barnShadowTex, alphaBlending, fogColor);
		Material hillTex = new Material("hillTex", hillDiffuseTex, greenDiffuseColor, fogColor);
		Material treeTex = new Material("treeTex", treeDiffuseTex, fogColor);
		Material treeShadowMaterial = new Material("treeTexShadow", treeShadowTex, alphaBlending, fogColor);
		
		Material fireMaterial = new Material("fireMaterial", yellowDiffuseColor, fogColor);
		Material woodMaterial = new Material("woodMaterial", brownDiffuseColor, fogColor);
		
		Material warningMaterial = new Material("warning", redDiffuseColor, fogColor);
		
		Material grassMaterial = new Material("grassesTex", grassesTex, fogColor);
		
				
		modelSheep.setMaterial(sheepTex);
		
		modelBarn.setMaterial(barnTex);
		modelBarnShadow.setMaterial(barnShadowMaterial);
		
		modelHill.setMaterial(hillTex);
		
		modelTree.setMaterial(treeTex);		
		modelTreeShadow.setMaterial(treeShadowMaterial);
		
		modelGrass.setMaterial(grassMaterial);
		
		modelWarning.setMaterial(warningMaterial);
		
		modelTrap.setMaterial(sheepTex);
		modelTrap.getSubMesh("wood").material = woodMaterial;
		modelTrap.getSubMesh("fire").material = fireMaterial;
				
		
		for(int i = 0; i < GameInstance.getInstance().trees.size; i++) {
			Ray ray = new Ray(new Vector3( GameInstance.getInstance().trees.get(i).position.x, -100,  GameInstance.getInstance().trees.get(i).position.z), Vector3.Y);
			Vector3 localIntersection = new Vector3();
			if (Intersector.intersectRayTriangles(ray, GameInstance.getInstance().heightMap.map, localIntersection)) {
			}
			localIntersection.y += 0;
			GameInstance.getInstance().trees.get(i).position.set(localIntersection);
			
			GameInstance.getInstance().trees.get(i).update();
		}
		
		
		for(int i = 0; i < GameInstance.getInstance().grasses.size; i++) {
			Ray ray = new Ray(new Vector3( GameInstance.getInstance().grasses.get(i).position.x, -100,  GameInstance.getInstance().grasses.get(i).position.z), Vector3.Y);
			Vector3 localIntersection = new Vector3();
			if (Intersector.intersectRayTriangles(ray, GameInstance.getInstance().heightMap.map, localIntersection)) {
			}
			localIntersection.y += 0;
			GameInstance.getInstance().grasses.get(i).position.set(localIntersection);
			
			GameInstance.getInstance().grasses.get(i).update();
		}
		
	}
	
	public void updateCamera(PerspectiveCamera cam) {
		protoRenderer.cam = cam;
	}

	public void render() {
		final float delta = Gdx.graphics.getDeltaTime();
		
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl.glDisable(GL10.GL_BLEND);
		
		protoRenderer.begin();
		
		for(int i = 0; i < GameInstance.getInstance().sheeps.size; i++) {
			Ray ray = new Ray(new Vector3( GameInstance.getInstance().sheeps.get(i).position.x, -100,  GameInstance.getInstance().sheeps.get(i).position.z), Vector3.Y);
			Vector3 localIntersection = new Vector3();
			if (Intersector.intersectRayTriangles(ray, GameInstance.getInstance().heightMap.map, localIntersection)) {
			}
			localIntersection.y += 1;
			GameInstance.getInstance().sheeps.get(i).position.set(localIntersection);
			
			protoRenderer.draw(modelSheep, GameInstance.getInstance().sheeps.get(i).instance);	
			
			if(GameInstance.getInstance().sheeps.get(i).alerted) {
				protoRenderer.draw(modelWarning, GameInstance.getInstance().sheeps.get(i).instance);	
			}
		}
		
		
		for(int i = 0; i < GameInstance.getInstance().traps.size; i++) {
			protoRenderer.draw(modelTrap, GameInstance.getInstance().traps.get(i).instance);
		}
		
		
		for(int i = 0; i < GameInstance.getInstance().trees.size; i++) {
			protoRenderer.draw(modelTree, GameInstance.getInstance().trees.get(i).instance);
			protoRenderer.draw(modelTreeShadow, GameInstance.getInstance().trees.get(i).instance);
		}
		
		for(int i = 0; i < GameInstance.getInstance().grasses.size; i++) {
			protoRenderer.draw(modelGrass, GameInstance.getInstance().grasses.get(i).instance);
		}
		
		protoRenderer.draw(modelBarn, GameInstance.getInstance().barn.instance);
		protoRenderer.draw(modelBarnShadow, GameInstance.getInstance().barn.instance);
		
		protoRenderer.draw(modelHill, GameInstance.getInstance().hill.instance);
		
		protoRenderer.end();
	}

}
