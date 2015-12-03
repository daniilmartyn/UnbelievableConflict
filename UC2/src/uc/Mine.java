package uc;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Mine extends Entity{

	private boolean active;
	
	private Vector velocity;
	private Image mine;
	
	public Mine(final float x, final float y, final Vector v) {
		super(x,y);
		velocity = v;
		mine = ResourceManager.getImage(UCGame.THEMINE_RSC);
		addImage(mine);
		addShape(new ConvexPolygon((float)mine.getWidth(), (float)mine.getHeight()), Color.transparent, Color.green);
		active = true;
	}

	
	public void update(int delta){
		
		translate(velocity.scale(delta));
		velocity = velocity.add(new Vector(0.0f, (PlayState.gravity*delta)));
		
		if(getX() < 0 || getX() > PlayState.map.getWidth()  //////////////////change this when map creating is updated
				|| getY() < 0 || getY() > PlayState.map.getHeight())
			active = false;
		
	}
	
	public boolean isActive(){
		return active;
	}
}
