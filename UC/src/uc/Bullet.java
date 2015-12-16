package uc;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import jig.Collision;
import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Bullet extends Entity{

	private boolean active;
	
	public Vector velocity;
	private Image bullet;
	public int id;
	public Float rotation;

	private int damage;
	
	public Bullet(final float x, final float y, final Vector v) {
		super(x,y);
		velocity = v;
		bullet = ResourceManager.getImage(UCGame.BULLET_RSC);
		addImage(bullet);
		addShape(new ConvexPolygon(bullet.getHeight()), Color.transparent, Color.transparent);
		active = true;
		switch(UCGame.character){
		case 0:
			damage = 10;
			break;
		case 1:
			damage = 10;
			break;
		case 2:
			damage = 15;
			break;
		}
	}

	public int getDamage(){
		return damage;
	}
	
	public void update(int delta){
		translate(velocity.scale(delta));
		if(getX() < 0 || getX() > PlayState.map.getImg().getWidth()  //////////////////change this when map creating is updated
				|| getY() < 0 || getY() > PlayState.map.getImg().getHeight())
			active = false;
		
		
		Collision collide = collides(PlayState.map);
		if(collide!= null){		
			active = false;
		}
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void notActive(){
		active = false;
	}
	
}
