package de.swagner.ld25;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;


public class SheepAI {
	
	public enum STATE {
		IDLE, MOVING, TURNING_LEFT, TURNING_RIGHT, RUN_AWAY;
	}
	
	public STATE state;
	public STATE oldState;
	private Sheep sheep;
	
	private boolean runAway = false;
	
	private float idlePause = 0;
	
	public float oldDistance = 0;
	
	public Vector3 targetPosition;
	public Vector3 targetDirection;

	public SheepAI(Sheep sheep) {
		this.sheep = sheep;
		this.targetPosition = new Vector3(MathUtils.random(GameInstance.getInstance().min, GameInstance.getInstance().max), MathUtils.random(0, 0), MathUtils.random(GameInstance.getInstance().min, GameInstance.getInstance().max));
		
		this.targetDirection = targetPosition.cpy().sub(sheep.position);
		float angle = (float)Math.atan2(targetDirection.x, targetDirection.z) * MathUtils.radiansToDegrees;
		
		if(angle > 0) {
			state = STATE.TURNING_LEFT;
			oldState = STATE.TURNING_LEFT;
		} else {
			state = STATE.TURNING_RIGHT;
			oldState = STATE.TURNING_RIGHT;
		}
			
	}
	
	public void update() {
		if(sheep.alerted && runAway == false) {	
			runAway = true;
			idlePause = 0;
			
			boolean positionFound = true;
			while(positionFound) {
				positionFound = false;
				this.targetPosition = new Vector3(MathUtils.random(GameInstance.getInstance().min, GameInstance.getInstance().max), MathUtils.random(0, 0), MathUtils.random(GameInstance.getInstance().min, GameInstance.getInstance().max));
				if(this.targetPosition.dst(GameInstance.getInstance().camera.position)< 10) {
						positionFound = true;
				}
			}
			
			this.targetDirection = targetPosition.cpy().sub(sheep.position);
			float angle = (float)Math.atan2(targetDirection.x, targetDirection.z) * MathUtils.radiansToDegrees;
			
			if(angle > 0) {
				state = STATE.TURNING_LEFT;
				oldState = STATE.TURNING_LEFT;
			} else {
				state = STATE.TURNING_RIGHT;
				oldState = STATE.TURNING_RIGHT;
			}			
		}
		
		if(state.equals(STATE.IDLE) && idlePause > 0 && runAway == false) {
			idlePause -= Gdx.graphics.getDeltaTime();
			return;
		}
		
		if(!state.equals(STATE.MOVING) && MathUtils.random(0, 100) == 0) {
			idlePause = MathUtils.random(0,5);
			state = STATE.IDLE;		
			return;
		}
		
		if(state.equals(STATE.MOVING) && targetPosition.dst(sheep.position) < 1) {
			targetPosition.set(new Vector3(MathUtils.random(GameInstance.getInstance().min, GameInstance.getInstance().max), MathUtils.random(0, 0), MathUtils.random(GameInstance.getInstance().min, GameInstance.getInstance().max)));
			
			this.targetDirection = targetPosition.cpy().sub(sheep.position);
			float angle = (float)Math.atan2(targetDirection.x, targetDirection.z) * MathUtils.radiansToDegrees;
			
			if(angle > 0) {
				state = STATE.TURNING_LEFT;
				oldState = STATE.TURNING_LEFT;
			} else {
				state = STATE.TURNING_RIGHT;
				oldState = STATE.TURNING_RIGHT;
			}
			
			sheep.setAlerted(false);
			runAway = false;
		}
		
		if(oldState.equals(STATE.MOVING) && runAway == false ) {
			for(int i = 0; i < GameInstance.getInstance().sheeps.size; i++) {
				Sheep otherSheep = GameInstance.getInstance().sheeps.get(i);
				if(otherSheep.equals(sheep)) continue;
				
				if(sheep.position.dst(otherSheep.position) < 6.f) {			
					targetPosition.set(new Vector3(MathUtils.random(GameInstance.getInstance().min, GameInstance.getInstance().max), MathUtils.random(0, 0), MathUtils.random(GameInstance.getInstance().min, GameInstance.getInstance().max)));
					
					this.targetDirection = targetPosition.cpy().sub(sheep.position.cpy());
					float angle = (float)Math.atan2(targetDirection.x, targetDirection.z) * MathUtils.radiansToDegrees;
					
					if(angle > 0) {
						state = STATE.TURNING_LEFT;
						oldState = STATE.TURNING_LEFT;
					} else {
						state = STATE.TURNING_RIGHT;
						oldState = STATE.TURNING_RIGHT;
					}
				}
			}
		}
		
		this.targetDirection = targetPosition.cpy().sub(sheep.position);
		float angle = (float)Math.atan2(targetDirection.x, targetDirection.z) * MathUtils.radiansToDegrees;
		float targetAngle = (float)Math.atan2(sheep.direction.x, sheep.direction.z) * MathUtils.radiansToDegrees;
		
		if(oldState.equals(STATE.TURNING_LEFT) && angle > targetAngle) {
			state = STATE.TURNING_LEFT;
			oldState = STATE.TURNING_LEFT;
		} else if(oldState.equals(STATE.TURNING_RIGHT) && angle < targetAngle) {
			state = STATE.TURNING_RIGHT;
			oldState = STATE.TURNING_RIGHT;
		} else {
			state = STATE.MOVING;
		}
		
		
		// BUG: correct current direction
		if(targetPosition.dst(sheep.position) > oldDistance) {
			targetPosition.set(new Vector3(MathUtils.random(GameInstance.getInstance().min, GameInstance.getInstance().max), MathUtils.random(0, 0), MathUtils.random(GameInstance.getInstance().min, GameInstance.getInstance().max)));
			
			this.targetDirection = targetPosition.cpy().sub(sheep.position);
			float angle2 = (float)Math.atan2(targetDirection.x, targetDirection.z) * MathUtils.radiansToDegrees;
			
			if(angle2 > 0) {
				state = STATE.TURNING_LEFT;
				oldState = STATE.TURNING_LEFT;
			} else {
				state = STATE.TURNING_RIGHT;
				oldState = STATE.TURNING_RIGHT;
			}
			
			runAway = false;
			sheep.setAlerted(false);
		}
		
		oldDistance = targetPosition.dst(sheep.position);
		oldState = state;
	
	}
	
}
