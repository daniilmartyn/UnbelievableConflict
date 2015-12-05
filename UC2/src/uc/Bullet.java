package uc;

import java.util.Iterator;

import org.newdawn.slick.Image;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Bullet extends Entity{

	private boolean active;
	
	public Vector velocity;
	private Image bullet;
	public int id;
	
	public Bullet(final float x, final float y, final Vector v) {
		super(x,y);
		velocity = v;
		bullet = ResourceManager.getImage(UCGame.BULLET_RSC);
		addImage(bullet);
		addShape(new ConvexPolygon(bullet.getWidth()));
		active = true;
	}

	public void update(int delta){
		translate(velocity.scale(delta));
		

		
		if(UCGame.isServer){
		if(getX() < 0 || getX() > PlayState.map.getWidth()  //////////////////change this when map creating is updated
				|| getY() < 0 || getY() > PlayState.map.getHeight())
			active = false;
		}
		else{
			if(getX() < 0 || getX() > PlayState2.map.getWidth()  //////////////////change this when map creating is updated
					|| getY() < 0 || getY() > PlayState2.map.getHeight())
				active = false;
			
		}
	}
	
	public boolean isActive(){
		return active;
	}
	
}
