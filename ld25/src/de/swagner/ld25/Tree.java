package de.swagner.ld25;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.StillModelNode;

public class Tree {

	public Vector3 position;
	public Vector3 direction;
	
	public float scale;
	
	public StillModelNode instance;	
	public BoundingBox boundingBox;
	
	public Tree(Vector3 position, float scale) {
		this.position = new Vector3().set(position);
		this.scale = scale;
		
		this.boundingBox = new BoundingBox();		
		this.instance = new StillModelNode();
		this.instance.matrix.trn(position);
		this.instance.matrix.scale(scale, scale, scale);
		this.instance.radius = (this.boundingBox.getDimensions().len() / 2);
	}
	
	public void updateBoundingBox(BoundingBox bb) {
		this.boundingBox.set(bb);
		this.boundingBox.mul(this.instance.matrix);
		this.instance.radius = (this.boundingBox.getDimensions().len() / 2);		
	}	
	
	public void update() {
		this.instance.matrix.idt();
		this.instance.matrix.scale(scale, scale, scale);
		this.instance.matrix.trn(position);
	}
}
