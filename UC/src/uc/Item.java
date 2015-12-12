package uc;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Shape;

public class Item extends Entity{

	private int respawnTime;
	private int countDown;
	private int type;
	
	private Image health = ResourceManager.getImage(UCGame.HEALTH_RSC);
	private Image ammo = ResourceManager.getImage(UCGame.AMMO_RSC);
	private Image power = ResourceManager.getImage(UCGame.POWER_RSC);
	
	private Shape healthBox;
	private Shape ammoBox;
	private Shape powerBox;
	
	private boolean active;
	
	public Item(final float x, final float y, int type){
		super(x,y);
		
		this.type = type;
		active = true;
		switch(type){
		case 0:		// health pack
			respawnTime = 10000;
			countDown = 0;
			addImage(health);
			healthBox = new ConvexPolygon((float)health.getWidth(), (float) health.getHeight());
			addShape(healthBox, Color.transparent, Color.yellow);
			break;
		case 1:		// ammo pack
			respawnTime = 15000;
			countDown = 0;
			addImage(ammo);
			ammoBox = new ConvexPolygon((float)ammo.getWidth(), (float) ammo.getHeight());
			addShape(ammoBox, Color.transparent, Color.yellow);
			break;
		case 2:		// power up
			respawnTime = 60000;
			countDown = 0;
			addImage(power);
			powerBox = new ConvexPolygon((float)power.getWidth()/4);
			addShape(powerBox, Color.transparent, Color.yellow);
			break;
		}
		
	}
	
	public int getType(){
		return type;
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void respawn(){
		active = false;
		switch(type){
		case 0:
			removeImage(health);
			removeShape(healthBox);
			break;
		case 1:
			removeImage(ammo);
			removeShape(ammoBox);
			break;
		case 2:
			removeImage(power);
			removeShape(powerBox);
			break;
		}
		countDown = respawnTime;
	}
	
	public void update(int delta){
		
		if(countDown > 0){
			countDown -= delta;
		}else if(!active){
			active = true;
			switch(type){
			case 0:
				addImage(health);
				addShape(healthBox, Color.transparent, Color.yellow);
				break;
			case 1:
				addImage(ammo);
				addShape(ammoBox, Color.transparent, Color.yellow);
				break;
			case 2:
				addImage(power);
				addShape(powerBox, Color.transparent, Color.yellow);
				break;
			}
		}
	}
}
