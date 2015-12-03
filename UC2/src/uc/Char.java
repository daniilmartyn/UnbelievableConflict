package uc;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Char extends Entity{

	public int id;
	public boolean set;
	
	private int health;
	private float speed = 0.5f;
	private float jump = 1.0f;
	private boolean isJumped = true;
	private int state; // 0 for stand, 1 for run, 2 for crouch, 3 for jump
	private int prevState;
	
	private int direction = 0;  // 0 for facing right, 1 for facing left
	private int prevDirection = direction;
	private Vector velocity;
	private Image current;
	private Image standRight = ResourceManager.getImage(UCGame.STAND_LIGHT_RSC);
	private Image standLeft = standRight.getFlippedCopy(true, false);
	private Image crouchRight = ResourceManager.getImage(UCGame.CROUCH_LIGHT_RSC);
	private Image crouchLeft = crouchRight.getFlippedCopy(true, false);
	private Animation running;
	
	private ConvexPolygon standingBox;
	private ConvexPolygon crouchBox;
	
	private Weapon weapon;
	
	public Char(final float x, final float y) {
		super(x,y);
		set = false;
		health = 100;
		velocity = new Vector(0.0f, 0.0f);
		current = standRight;
		addImage(current);
		standingBox = new ConvexPolygon((float) current.getWidth() - 2, (float) current.getHeight() - 2);
		crouchBox = new ConvexPolygon((float) crouchRight.getWidth() - 20, (float) crouchRight.getHeight() - 5);
		addShape(standingBox, Color.transparent, Color.red);
		state = 0;
		prevState = state;
		
		weapon = new Weapon(x, y);
	}

	public void changeDir(int d){ // used to change direction of char in relation to mouse location
		direction = d;
	}
	
	public void changeRunDir(int d){ // actually change the velocity of the char
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
	
	public void setState(int s){
		state = s;
	}
	
	public int getState(){
		return state;
	}
	
	public Vector getVel(){
		return velocity;
	}
	
	public void goBack(int delta){
		translate(velocity.scale(-delta));
	}
	
	public void cancelFall(){ // reset the y velocity to 0.0
		velocity = new Vector(velocity.getX(), 0.0f);
	}
	
	public void jump(){
		velocity = new Vector(velocity.getY(), -jump);
	}
	
	public void switchWep(int i){
		weapon.changeWeapon(i);
	}
	
	public void fire(){
		weapon.fire(this);
	}
	
	private void changeImg(){
		
		//System.out.println("state is: " + state);
		
		if(state == 2 && prevState != 2){
			removeShape(standingBox);
			addShape(crouchBox, Color.transparent, Color.red);
			if(prevState != 3)
				setY(getY() + 20.0f);
		}else if(state != 2 && prevState == 2){
			removeShape(crouchBox);
			addShape(standingBox, Color.transparent, Color.red);
		}
		
		if(state == 0){ // if char is just standing still
			if(direction == 0 && (prevDirection != direction || prevState != state)){
				removeImage(current);
				removeAnimation(running);
				current = standRight;
				addImage(current);
				prevDirection = direction;
				prevState = state;
			}else if( direction == 1 && (prevDirection != direction || prevState != state)){
				removeImage(current);
				removeAnimation(running);
				current = standLeft;
				addImage(current);
				prevDirection = direction;
				prevState = state;
			}		
		} 
		
		if(state == 1){ // if char is running, and not falling/jumping
			if(direction == 0 && (prevDirection != direction || prevState != state)){
				removeImage(current);
				removeAnimation(running);
				running = new Animation(ResourceManager.getSpriteSheet(UCGame.RUN_LIGHT_RSC, 118, 131),
						0,0,7,0,true, 100, true);
				addAnimation(running);
				prevDirection = direction;
				prevState = state;
			}else if( direction == 1 && (prevDirection != direction || prevState != state)){
				removeImage(current);
				removeAnimation(running);
				running = new Animation(ResourceManager.getSpriteSheet(UCGame.RUN_LIGHT_RSC, 118, 131),
						0,1,7,1,true, 100, true);
				addAnimation(running);
				prevDirection = direction;
				prevState = state;
			}
		} 
		
		if(state == 2){ // char is crouching
			if(direction == 0 && (prevDirection != direction || prevState != state)){
				removeImage(current);
				removeAnimation(running);
				current = crouchRight;
				addImage(current, new Vector(0.0f, 3.0f));
				prevDirection = direction;
				prevState = state;
			}else if( direction == 1 && (prevDirection != direction || prevState != state)){
				removeImage(current);
				removeAnimation(running);
				current = crouchLeft;
				addImage(current, new Vector(0.0f, 3.0f));
				prevDirection = direction;
				prevState = state;
			}	
		}
		
	}
	
	public void renderWep(Graphics g){
		
		if(weapon != null){
			weapon.render(g);
		}
		
		for(Mine m: weapon.getMines()){
			m.render(g);
		}
		
		for(Bullet b: weapon.getBullets()){
			b.render(g);
		}
	}
	
	public void update(float gravity, GameContainer container, int camX, int camY, int delta){
		
		changeImg();
		translate(velocity.scale(delta));
		weapon.setPosition(getX(), getY());
		weapon.update(container, camX, camY, this, delta);
		velocity = velocity.add(new Vector(0.0f, (gravity*delta)));
	}
	
}
