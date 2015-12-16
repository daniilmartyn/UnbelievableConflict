package uc;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import jig.Collision;
import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Mine extends Entity{

	private boolean active;
	
	private Vector velocity;
	public int id;
	private Image mine;
	private int damage = 30;
	
	public Mine(final float x, final float y, final Vector v) {
		super(x,y);
		velocity = v;
		mine = ResourceManager.getImage(UCGame.THEMINE_RSC);
		addImage(mine);
		addShape(new ConvexPolygon((float)mine.getWidth(), (float)mine.getHeight()), Color.transparent, Color.transparent);
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
		
		
		Collision initial = collides(PlayState.map);
		Collision resolve = null;
		
		if(initial != null){
			
			while((resolve = collides(PlayState.map)) != null){ // collision resolution
				if(resolve.getMinPenetration().getX() != 0)		// need to move mine horizontally
					setPosition(getX()+ resolve.getMinPenetration().getX(),getY());
				if(resolve.getMinPenetration().getY() != 0)		// need to move mine vertically

					setPosition(getX(),getY()+(velocity.getY()* -delta));
			}
			
			velocity = new Vector(0.0f, 0.0f);
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
