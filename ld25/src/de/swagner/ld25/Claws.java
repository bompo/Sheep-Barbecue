package de.swagner.ld25;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.AnimatedModelNode;

public class Claws {

	public Vector3 position;
	public Vector3 direction;
	
	public AnimatedModelNode instance;	
	public BoundingBox boundingBox;
	
	public float animTime;
	
	public Claws(Vector3 position, Vector3 direction) {
		this.position = new Vector3().set(position);
		this.direction = new Vector3().set(direction);
		
		this.boundingBox = new BoundingBox();		
		this.instance = new AnimatedModelNode();
		this.instance.matrix.rotate(Vector3.Y, position.dot(direction) * MathUtils.radiansToDegrees);
		this.instance.matrix.trn(position);
		this.instance.matrix.scale(1f, 1f, 1f);
		this.instance.radius = (this.boundingBox.getDimensions().len() / 2);		
	}
	
	public void updateBoundingBox(BoundingBox bb) {
		this.boundingBox.set(bb);
		this.boundingBox.mul(this.instance.matrix);
		this.instance.radius = (this.boundingBox.getDimensions().len() / 2);		
	}	
	
	public void update() {
		this.instance.matrix.idt();
		this.instance.matrix.trn(position);
		
		this.instance.time += Gdx.graphics.getDeltaTime();
		if (this.instance.time > this.animTime) {			
			this.instance.time = 0;
		}
	}
	
}
