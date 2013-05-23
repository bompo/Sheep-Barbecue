package de.swagner.ld25;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.StillModelNode;

public class Trap {

	public Vector3 position;
	public Vector3 direction;
	
	public float rotate;
	
	public StillModelNode instance;	
	public BoundingBox boundingBox;
	
	public Trap(Vector3 position) {
		this.position = new Vector3().set(position);
		this.boundingBox = new BoundingBox();		
		this.instance = new StillModelNode();
		this.instance.matrix.trn(position);
		this.instance.radius = 10;
	}
	
	public void updateBoundingBox(BoundingBox bb) {
		this.boundingBox.set(bb);
		this.boundingBox.mul(this.instance.matrix);
		this.instance.radius = (this.boundingBox.getDimensions().len() / 2);		
	}	
	
	public void update() {
		this.instance.matrix.idt();
		
		rotate += Gdx.graphics.getDeltaTime() * 20.f;
		this.instance.matrix.trn(position);

		this.instance.matrix.trn(0, -1, 0);
		
	}
}
