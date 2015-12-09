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
	
	public Bullet(final float x, final float y, final Vector v) {
		super(x,y);
		velocity = v;
		bullet = ResourceManager.getImage(UCGame.BULLET_RSC);
		addImage(bullet);
		addShape(new ConvexPolygon(bullet.getHeight()), Color.transparent, Color.green);
		active = true;
	}

	public void update(int delta){
		translate(velocity.scale(delta));
		if(getX() < 0 || getX() > PlayState.map.getImg().getWidth()  //////////////////change this when map creating is updated
				|| getY() < 0 || getY() > PlayState.map.getImg().getHeight())
			active = false;
		
		
		Collision collide = null;
		if((collide = collides(PlayState.map)) != null){
			System.out.println("collision test: " + collide.getMinPenetration());
			System.out.println("this shape: " + collide.getThisShape());
			System.out.println("other shape: " + collide.getOtherShape());
			
			active = false;
		}
	}
	
	public boolean isActive(){
		return active;
	}
	
}
