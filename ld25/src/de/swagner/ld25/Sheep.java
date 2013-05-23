package de.swagner.ld25;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.swagner.ld25.SheepAI.STATE;
import de.swagner.ld25.com.badlogic.gdx.graphics.g3d.StillModelNode;

public class Sheep {

	public Vector3 position;
	public Vector3 direction;
	
	public boolean alerted = false;
	public boolean traped = false;
	
	
	private float speed = 0.8f;
	private float turnSpeed = 40;
	private float bounceSpeed = 2;
	
	private SheepAI sheepAI;
	
	public float bounce = MathUtils.random();
	public boolean inAir = false;
	public Vector3 lastPosition;
	
	public StillModelNode instance;	
	public BoundingBox boundingBox;
	
	public Sheep(Vector3 position, Vector3 direction) {
		this.position = new Vector3().set(position);
		this.direction = new Vector3().set(direction);
		
		this.lastPosition = new Vector3().set(position);
		
		this.boundingBox = new BoundingBox();		
		this.instance = new StillModelNode();
		this.instance.matrix.rotate(Vector3.Y, position.dot(direction) * MathUtils.radiansToDegrees);
		this.instance.matrix.trn(position);
		this.instance.matrix.scale(1f, 1f, 1f);
		this.instance.radius = (this.boundingBox.getDimensions().len() / 2);
		
		this.sheepAI = new SheepAI(this);
	}
	
	public void updateBoundingBox(BoundingBox bb) {
		this.boundingBox.set(bb);
		this.boundingBox.mul(this.instance.matrix);
		this.instance.radius = (this.boundingBox.getDimensions().len() / 2);		
	}
	
	public void update() {
		this.sheepAI.update();
		
		if(!traped) {
			if(this.sheepAI.state.equals(STATE.MOVING)) {
				this.position.add(this.direction.cpy().mul(Gdx.graphics.getDeltaTime() * speed));
			} else if(this.sheepAI.state.equals(STATE.TURNING_LEFT)) {
				this.direction.rot(new Matrix4().setToRotation(Vector3.Y, Gdx.graphics.getDeltaTime()*turnSpeed));
			} else if(this.sheepAI.state.equals(STATE.TURNING_RIGHT)) {
				this.direction.rot(new Matrix4().setToRotation(Vector3.Y, -Gdx.graphics.getDeltaTime()*turnSpeed));
			}
		}
			
		// get angle direction
		float angle = (float)Math.atan2(direction.x, direction.z) * MathUtils.radiansToDegrees;
		if (angle < 0) angle += 360;	
		
		this.instance.matrix.idt();
		this.instance.matrix.rotate(Vector3.Y, 0);
		this.instance.matrix.rotate(Vector3.Y, angle);
		this.instance.matrix.trn(position);
		this.instance.matrix.trn(0, (float) Math.log(bounce*20.f + 1f)/5.f - 1.f, 0);	
	
		// bounce if move
		if(lastPosition.dst(position)>0) {
			if(inAir == false) {
				bounce += Gdx.graphics.getDeltaTime()*bounceSpeed;
				if(bounce > 1) {
					inAir = true;
					bounce = 1;
				}
			}
			if(inAir == true) {
				bounce -= Gdx.graphics.getDeltaTime()*bounceSpeed;
				if(bounce < 0) {
					inAir = false;
					bounce = 0;
				}				
			}
		} else {
			if(inAir == false) {
				bounce += Gdx.graphics.getDeltaTime()*bounceSpeed;
				if(bounce > 1) {
					inAir = true;
					bounce = 1;
				}
			}
			if(inAir == true) {
				bounce -= Gdx.graphics.getDeltaTime()*bounceSpeed;
				if(bounce < 0) {
					bounce = 0;
				}				
			}
		}
		
		this.lastPosition.set(position);
	}
	
	public void setAlerted(boolean switchs) {
		if(switchs == true ) {
			this.alerted = true;
			bounceSpeed = 8;
			speed = 4;
			turnSpeed = 200;
		} else {
			this.alerted = false;
			bounceSpeed = 4;
			speed = 0.8f;
			turnSpeed = 40;
		}
		
	}
	
}
