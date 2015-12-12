package uc;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;

import jig.Collision;
import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Bomb extends Entity{

	private boolean active;
	public int id;
	private Vector velocity;
	private Animation bomb;
	
	private int boom; // countdown to detonation
	private int damage = 40; 
	
	public Bomb(final float x, final float y, Vector v) {
		super(x,y);
		velocity = v;
		boom = 3000;
		bomb = new Animation(ResourceManager.getSpriteSheet(UCGame.BOMB_RSC, 24, 36), 0, 0, 3, 0, true, 100, true);
		addAnimation(bomb);
		addShape(new ConvexPolygon(11f), new Vector(0.0f, 7.0f), Color.transparent, Color.green);
		active = true;
	}

	public int getDamage(){
		return damage;
	}
	
	public void update(int delta){
		translate(velocity.scale(delta));
		
		if(getX() < 0 || getX() > PlayState.map.getImg().getWidth()  //////////////////change this when map creating is updated
				|| getY() < 0 || getY() > PlayState.map.getImg().getHeight())
			active = false;
		
		if(boom < 0){
			active = false;
			ResourceManager.getSound(UCGame.PLAYER_KABOOMSOUND_RSC).play();
			System.out.println("BOOM!");
		}
		else
			boom -= delta;
		
		Collision initial = collides(PlayState.map);
		Collision resolve = null;
		
		if(initial != null){
			//System.out.println("collision test: " + collide.getMinPenetration());
			//System.out.println("this shape: " + collide.getThisShape());
			//System.out.println("other shape: " + collide.getOtherShape());
			
			while((resolve = collides(PlayState.map)) != null){ // collision resolution
				
				//translate(resolve.getMinPenetration());
				if(resolve.getMinPenetration().getX() != 0)		// need to move bomb horizontally
					setPosition(getX()+ resolve.getMinPenetration().getX(),getY());
				if(resolve.getMinPenetration().getY() != 0)		// need to move bomb vertically
					setPosition(getX(),getY()+(velocity.getY()* -delta));
			}
			
			if(Math.abs(initial.getMinPenetration().getX()) > 0.5f){ // if the bomb hits a wall, slightly bounce off
				velocity = velocity.bounce(90);
				velocity = velocity.setX(velocity.getX() * 0.2f);
			}
			if(initial.getMinPenetration().getY() < 0.0f){  // bomb hits a horizontal surface from the top
				velocity = velocity.bounce(0);
				velocity = new Vector(velocity.getX()* 0.9f, velocity.getY() * 0.2f); //dampen the bounce, and provide friction
			}else
				velocity = velocity.setY(0.0f);
		}
		
		velocity = velocity.add(new Vector(0.0f, (PlayState.gravity*delta)));
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void notActive(){
		active = false;
	}
}
