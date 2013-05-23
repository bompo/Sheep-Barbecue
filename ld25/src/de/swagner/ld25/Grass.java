package de.swagner.ld25;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.StillModelNode;

public class Grass {

	public Vector3 position;
	public Vector3 direction;
	
	public Vector3 laydown;
	
	public float scale;
	
	public StillModelNode instance;	
	public BoundingBox boundingBox;
	
	public Grass(Vector3 position, Vector3 direction, float scale) {
		this.position = new Vector3().set(position);
		this.direction = new Vector3().set(direction);
		this.scale = scale;
		
		this.boundingBox = new BoundingBox();		
		this.instance = new StillModelNode();
		this.instance.matrix.trn(position);
		this.instance.matrix.scale(scale, scale, scale);
		this.instance.radius = 10;
	}
	
	public void updateBoundingBox(BoundingBox bb) {
		this.boundingBox.set(bb);
		this.boundingBox.mul(this.instance.matrix);
		this.instance.radius = (this.boundingBox.getDimensions().len() / 2);		
	}	
	
	public void update() {
		// get angle direction
		float angle = (float)Math.atan2(direction.x, direction.z) * MathUtils.radiansToDegrees;
		if (angle < 0) angle += 360;	
		
		this.instance.matrix.idt();
		this.instance.matrix.rotate(Vector3.Y, 0);
		this.instance.matrix.rotate(Vector3.Y, angle);
		this.instance.matrix.scale(scale, scale, scale);
		this.instance.matrix.trn(position);
	}
	
	
	public void update(Vector3 position) {
		// get angle direction
		float angle = (float)Math.atan2(direction.x, direction.z) * MathUtils.radiansToDegrees;
		if (angle < 0) angle += 360;
		
		laydown = this.position.cpy().sub(position.cpy());
		// get angle direction
		float layDownAngle = (float)Math.atan2(laydown.x, laydown.z) * MathUtils.radiansToDegrees;
		if (layDownAngle < 0) layDownAngle += 360;
		
		this.instance.matrix.idt();
		this.instance.matrix.rotate(Vector3.Z, this.direction.dot(laydown) * MathUtils.radiansToDegrees *  Math.max(0 , 0.7f - position.dst(this.position)));
		this.instance.matrix.rotate(Vector3.X, this.direction.dot(laydown) * MathUtils.radiansToDegrees *  Math.max(0 , 0.7f - position.dst(this.position)));
//		this.instance.matrix.rotate(Vector3.Z, Math.max(0 , 1 - position.dst(this.position)) * 20.f);
		this.instance.matrix.rotate(Vector3.Y, angle);
		this.instance.matrix.scale(scale, scale, scale);
		this.instance.matrix.trn(this.position);
	}
}
