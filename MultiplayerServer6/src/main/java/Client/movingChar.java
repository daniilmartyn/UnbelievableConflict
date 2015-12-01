package Client;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;


import com.esotericsoftware.kryonet.Connection;

public class movingChar extends Entity {
	
	public Shape hitbox;
	public float x;
	public float y;
	public int points;
	public float netX;
	public float netY;
	public int id;
//	private Image standRight = ResourceManager.getImage(BounceGameClient.BALL_BALLIMG_RSC);

	public Connection c;
	
	public movingChar(float x1, float y1){
		
		x = x1;
		y = y1;
		points = 0;
		
		hitbox = new Rectangle(x, y, 32, 32);
	//	addImageWithBoundingBox(standRight);
//		addImageWithBoundingBox(ResourceManager
//				.getImage(BounceGameClient.BALL_BALLIMG_RSC));
	}
	
}
