package uc;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import jig.Collision;
import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Grenade extends Entity{

	public int id;
	
	private boolean active;
	
	private Vector velocity;
	private Image grenade;
	private int damage = 35;
	
	public Grenade(final float x, final float y, Vector v) {
		super(x,y);
		velocity = v;
		grenade = ResourceManager.getImage(UCGame.GRENADE_RSC);
		addImage(grenade);
		addShape(new ConvexPolygon(grenade.getHeight()), Color.transparent, Color.green);
		active = true;	}

	public int getDamage(){
		return damage;
	}
	
	public void update(int delta){
		translate(velocity.scale(delta));
		velocity = velocity.add(new Vector(0.0f, (PlayState.gravity*delta)));
		
		if(getY() > PlayState.map.getImg().getHeight())
			active = false;
		
		
		Collision collide = collides(PlayState.map);
		if(collide!= null){
			//System.out.println("collision test: " + collide.getMinPenetration());
			//System.out.println("this shape: " + collide.getThisShape());
			//System.out.println("other shape: " + collide.getOtherShape());
			ResourceManager.getSound(UCGame.PLAYER_KABOOMSOUND_RSC).play();
			UCGame.kaboom = true;
			active = false;
			UCGame.explosions.put(new Vector(getX(), getY()), new Explosion(getX(), getY()));
			System.out.println("Grenade EXPLODES!");
		}
		
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void notActive(){
		active = false;
	}
	
}
