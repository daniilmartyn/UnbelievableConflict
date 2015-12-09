package uc;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import jig.Collision;
import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Shape;
import jig.Vector;

public class Char extends Entity{

	public int id;
	public boolean set;

	private int health;
	private float speed;
	private float jump = 1.0f;
	private boolean isJumped = true;
	public int state; // 0 for stand, 1 for run, 2 for crouch, 3 for jump
	private int prevState;
	
	public int direction = 0;  // 0 for facing right, 1 for facing left
	private int prevDirection = direction;
	private Vector velocity;
	private Image current;
	private Image standRight;
	private Image standLeft;
	private Image crouchRight;
	private Image crouchLeft;
	
	private Animation running;
	private Animation runLeft;
	private Animation runRight;
	
	private ConvexPolygon standingBox;
	private ConvexPolygon crouchBox;
	
	public Weapon weapon;

	public int playingCharacter;
	
	public int camX;
	public int camY;

	public Char(final float x, final float y, int character) {
		super(x,y);
		set = false;

		switch(character){
		case 0:
			speed = 0.5f;
			jump = 1.0f;
			standRight = ResourceManager.getImage(UCGame.STAND_LIGHT_RSC);
			standLeft = standRight.getFlippedCopy(true, false);
			crouchRight = ResourceManager.getImage(UCGame.CROUCH_LIGHT_RSC);
			crouchLeft = crouchRight.getFlippedCopy(true, false);
			
			runRight = new Animation(ResourceManager.getSpriteSheet(UCGame.RUN_LIGHT_RSC, 118, 131),
					0,0,7,0,true, 100, true);
			runLeft = new Animation(ResourceManager.getSpriteSheet(UCGame.RUN_LIGHT_RSC, 118, 131),
					0,1,7,1,true, 100, true);
			
			health = 100;
			velocity = new Vector(0.0f, 0.0f);
			current = standRight;
			addImage(current);
			standingBox = new ConvexPolygon((float) current.getWidth() - 2, (float) current.getHeight() - 2);
			crouchBox = new ConvexPolygon((float) crouchRight.getWidth() - 20, (float) crouchRight.getHeight() - 5);
			addShape(standingBox, Color.transparent, Color.red);
			state = 0;
			prevState = state;

			weapon = new Weapon(x, y, character);
			break;
		case 1:
			speed = 0.3f;
			jump = 1.0f;
			standRight = ResourceManager.getImage(UCGame.STAND_MED_RSC);
			standLeft = standRight.getFlippedCopy(true, false);
			crouchRight = ResourceManager.getImage(UCGame.CROUCH_MED_RSC);
			crouchLeft = crouchRight.getFlippedCopy(true, false);
			
			runRight = new Animation(ResourceManager.getSpriteSheet(UCGame.RUN_MED_RSC, 92, 151),
					0,0,7,0,true, 100, true);
			runLeft = new Animation(ResourceManager.getSpriteSheet(UCGame.RUN_MED_RSC, 92, 151),
					0,1,7,1,true, 100, true);
			
			health = 150;
			velocity = new Vector(0.0f, 0.0f);
			current = standRight;
			addImage(current);
			standingBox = new ConvexPolygon((float) current.getWidth() - 2, (float) current.getHeight() - 2);
			crouchBox = new ConvexPolygon((float) crouchRight.getWidth() - 5, (float) crouchRight.getHeight() - 5);
			addShape(standingBox, Color.transparent, Color.red);
			state = 0;
			prevState = state;
			
			weapon = new Weapon(x, y, character);
			break;
		case 2:
			speed = 0.1f;
			jump = 1.0f;
			standRight = ResourceManager.getImage(UCGame.STAND_HEV_RSC);
			standLeft = standRight.getFlippedCopy(true, false);
			crouchRight = ResourceManager.getImage(UCGame.CROUCH_HEV_RSC);
			crouchLeft = crouchRight.getFlippedCopy(true, false);
			
			runRight = new Animation(ResourceManager.getSpriteSheet(UCGame.RUN_HEV_RSC, 175, 131),
					0,0,3,0,true, 100, true);
			runLeft = new Animation(ResourceManager.getSpriteSheet(UCGame.RUN_HEV_RSC, 175, 131),
					0,1,3,1,true, 100, true);
			
			health = 200;
			velocity = new Vector(0.0f, 0.0f);
			current = standRight;
			addImage(current);
			float[] stand = {87.5f,65 ,0, -65, -87.5f, 65};
			float[] crouch = {88f,65 ,0, -45, -88f, 65};
			standingBox = new ConvexPolygon(stand);
			crouchBox = new ConvexPolygon(crouch);
			addShape(standingBox, Color.transparent, Color.red);
			state = 0;
			prevState = state;
			
			weapon = new Weapon(x, y, character);
			break;
		}
		playingCharacter = character;
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
	
	public void changeImg(){
		
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
				running = runRight;
				addAnimation(running);
				prevDirection = direction;
				prevState = state;
			}else if( direction == 1 && (prevDirection != direction || prevState != state)){
				removeImage(current);
				removeAnimation(running);
				running = runLeft;
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
	
	public void renderWep(Graphics g){ //************* will need to create probaly a speperate method for rendering projectiles//
		
		if(weapon != null){
			//Weapon wep = weapon;
			weapon.render(g);
			
//			if(UCGame.character == 0){
//				for(Mine m: weapon.getMines()){
//					m.render(g);
//				}
//			}
//			
//			if(UCGame.character == 1){
//				for(Grenade gre: weapon.getGrenades()){
//					gre.render(g);
//				}
//			}
			
//			if(UCGame.character == 2){
//				for(Bomb b: weapon.getBombs()){
//					b.render(g);
//				}
//			}
			
//			for(Bullet b: weapon.getBullets()){
//				b.render(g);
//			}
		}		
	}
	
	public void update(GameContainer container, int camX, int camY, int delta){
		
		changeImg();
		translate(velocity.scale(delta));
		
		Collision collide = null;
		while((collide = collides(PlayState.map)) != null){
//			System.out.println("collision test: " + collide.getMinPenetration());
//			System.out.println("this shape: " + collide.getThisShape());
//			System.out.println("other shape: " + collide.getOtherShape());
			
			translate(collide.getMinPenetration());
			velocity = new Vector(velocity.getX(), 0.0f);
			isJumped = false;
		}
		
		weapon.setPosition(getX(), getY());
		weapon.update(container, camX, camY, this, delta);
		velocity = velocity.add(new Vector(0.0f, (PlayState.gravity*delta)));
	}
	
}
