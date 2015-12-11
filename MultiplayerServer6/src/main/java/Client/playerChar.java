package Client;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import com.esotericsoftware.kryonet.Connection;


public class playerChar  {
	
	public Shape hitbox;
	public float x;
	public float y;
	
	public float netX;
	public float netY;
	
	public Connection c;
	
	public playerChar(float x1, float y1){
		
		x = x1;
		y = y1;
		
		hitbox = new Rectangle(x, y, 32, 32);
	//	addImageWithBoundingBox(ResourceManager
	//			.getImage(BounceGameClient.BALL_BALLIMG_RSC));
	}
	
}
