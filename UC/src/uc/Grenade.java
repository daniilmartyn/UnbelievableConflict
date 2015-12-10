package uc;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import jig.Collision;
import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Grenade extends Entity{

private boolean active;
	
	private Vector velocity;
	public int id;
	private Image grenade;
	
	public Grenade(final float x, final float y, Vector v) {
		super(x,y);
		velocity = v;
		grenade = ResourceManager.getImage(UCGame.GRENADE_RSC);
		addImage(grenade);
		addShape(new ConvexPolygon(grenade.getHeight()), Color.transparent, Color.green);
		active = true;	}

	public void update(int delta){
		translate(velocity.scale(delta));
		velocity = velocity.add(new Vector(0.0f, (PlayState.gravity*delta)));
		
		if(getX() < 0 || getX() > PlayState.map.getImg().getWidth()  //////////////////change this when map creating is updated
				|| getY() < 0 || getY() > PlayState.map.getImg().getHeight())
			active = false;
		
		
		Collision collide = null;
		while((collide = collides(PlayState.map)) != null){
			System.out.println("collision test: " + collide.getMinPenetration());
			System.out.println("this shape: " + collide.getThisShape());
			System.out.println("other shape: " + collide.getOtherShape());
			
			translate(collide.getMinPenetration());
			velocity = new Vector(velocity.getX(), 0.0f);
		}
		
	}
	
	public boolean isActive(){
		return active;
	}
	
}
