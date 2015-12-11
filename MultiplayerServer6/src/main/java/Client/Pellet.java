package Client;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.esotericsoftware.kryonet.Connection;


public class Pellet extends Entity {
	private Vector velocity;

	public float x;
	public float y;
	
	public int id;
	
	public Connection c;
	
	public Pellet(float x1, float y1, float vx, float vy){
		velocity = new Vector(vx, vy);

		x = x1;
		y = y1;
		System.out.println(vx +" "+vy);
		
	}
	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta));
	}
	
}
