package Client;

import org.newdawn.slick.Image;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Bullet extends Entity{

	boolean active;
	
	private Vector velocity;
	private Image bullet;
	
	public Bullet(final float x, final float y, final Vector v) {
		super(x,y);
		velocity = v;
		bullet = ResourceManager.getImage(BounceGameClient.BALL_BULLETIMG_RSC);
		addImage(bullet);
		addShape(new ConvexPolygon(bullet.getWidth()));
		active = true;
	}

	public void update(int delta){
		translate(velocity.scale(delta));
		if(getX() < 0 || getX() > 500  //////////////////change this when map creating is updated
						|| getY() < 0 || getY() > 500)
					active = false;
	//	if(getX() < 0 || getX() > PlayState.map.getWidth()  //////////////////change this when map creating is updated
		//		|| getY() < 0 || getY() > PlayState.map.getHeight())
		//	active = false;
	}
	
	public boolean isActive(){
		return active;
	}
	
}
