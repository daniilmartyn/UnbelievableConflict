package uc;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Char extends Entity{

	private int health;
	private float speed = 0.5f;
	private float jump = 1.0f;
	private boolean isJumped = true;
	
	private int direction = 0;  // 0 for facing right, 1 for facing left
	private int prevDirection = direction;
	private Vector velocity;
	private Image standing;
	
	public Char(final float x, final float y) {
		super(x,y);
		velocity = new Vector(0.0f, 0.0f);
		standing = ResourceManager.getImage(UCGame.STAND_LIGHT_RSC);
		addImage(standing);
		addShape(new ConvexPolygon((float) standing.getWidth() - 2, (float) standing.getHeight() - 2), Color.transparent, Color.red);
	}

	public void changeDir(int d){
		direction = d;
	}
	
	public void changeRunDir(int d){
		if(d == 0)
			velocity = new Vector(speed, velocity.getY());
		else if( d == 1)
			velocity = new Vector(-speed, velocity.getY());
		else
			velocity = new Vector(0.0f, velocity.getY());
	}
	
	public boolean isJumped(){
		return isJumped;
	}
	
	public void setJump(boolean j){
		isJumped = j;
	}
	
	public void goBack(int delta){
		translate(velocity.scale(-delta));
	}
	
	public void cancelFall(){
		velocity = new Vector(velocity.getX(), 0.0f);
	}
	
	public void jump(){
		velocity = new Vector(velocity.getY(), -jump);
	}
	
	private void changeImg(){
		if(velocity.getX() == 0 && velocity.getY() < 0.02){
			if(direction == 0 && prevDirection != direction){
				removeImage(standing);
				standing = standing.getFlippedCopy(true, false);
				addImage(standing);
				prevDirection = direction;
			}else if( direction == 1 && prevDirection != direction){
				removeImage(standing);
				standing = standing.getFlippedCopy(true, false);
				addImage(standing);
				prevDirection = direction;
			}		
		}
		
	}
	
	public void update(float gravity, int delta){
		
		changeImg();
		translate(velocity.scale(delta));
		velocity = velocity.add(new Vector(0.0f, (gravity*delta)));
	}
	
}
