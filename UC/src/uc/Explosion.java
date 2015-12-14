package uc;

import org.newdawn.slick.Animation;

import jig.Entity;
import jig.ResourceManager;

public class Explosion extends Entity{

	private Animation explosion;
	
	public Explosion(final float x, final float y){
		super(x,y);
		
		explosion = new Animation(ResourceManager.getSpriteSheet(UCGame.EXPLOSION_RSC, 150 ,150 ),
									0,0,8,8,true, 20, true);
		addAnimation(explosion);
		explosion.setLooping(false);
	}
	
	public boolean isFinished(){
		return explosion.isStopped();
	}
}
