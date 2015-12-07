package uc;

import java.util.ArrayList;
import java.util.Iterator;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Weapon extends Entity{

	private Image primary = ResourceManager.getImage(UCGame.PISTOL_RSC);
	private Image secondary = ResourceManager.getImage(UCGame.MINE_RSC);
	private Image melee = ResourceManager.getImage(UCGame.PIPE_RSC);
	private Image weaponRight;
	private Image weaponLeft;
	private Image weapon;
	public int select; // 0 for primary, 1 for secondary, 2 for melee
	
	private Animation hit;
	
	private Vector offsetStand;
	private Vector offsetCrouch;
	private Vector wepOffSetRight;
	private Vector wepOffSetLeft;
	
	public Vector mouse;
	private Vector wep;
	public float angle = 0;
	public int direction = 0;
	private int prevDirection = direction;
	
	private ArrayList<Mine> mines;
//	private ArrayList<Bullet> bullets;
	
	public Weapon(final float x, final float y) { // Use a seperate weapons class per character
		super(x,y);
		
		// Start off weapon as being primary
		
		offsetStand = new Vector(0.0f, -30f);
		offsetCrouch = new Vector(10f, -15f);
		weaponRight = primary;
		weaponLeft = primary.getFlippedCopy(true, false);
		mouse = new Vector(0,0);
		weapon = weaponRight;
		wepOffSetRight = new Vector(primary.getWidth()/2 - 5, primary.getHeight()/2 - 5);
		wepOffSetLeft = new Vector(-wepOffSetRight.getX(), wepOffSetRight.getY());
		
		addImage(weapon, wepOffSetRight);
		addShape(new ConvexPolygon(50.0f, 10.0f), Color.transparent, Color.transparent);
		mines = new ArrayList<Mine>(5);
	//	bullets = new ArrayList<Bullet>(5);
	}

	public ArrayList<Mine> getMines(){
		return mines;
	}
	
//	public ArrayList<Bullet> getBullets(){
//		return bullets;
//	}
	
	public void fire(Char dude){
		
		switch(select){
		case 0:
			Bullet bullet;
			
			Vector bulletOffSet = Vector.getUnit(angle).scale(50);
			
			if(direction == 0)
				bullet = new Bullet(getX() + bulletOffSet.getX(), getY() + bulletOffSet.getY(), Vector.getUnit(angle).scale(.5f).add(dude.getVel()));
			else{
				bullet = new Bullet(getX() + bulletOffSet.getX(), getY() + bulletOffSet.getY(), Vector.getUnit(angle).scale(.5f).add(dude.getVel()));
			}
			bullet.rotation = angle;
			bullet.setRotation(angle);
			UCGame.bullet.add(bullet);
			
			//System.out.println("array of bullets: " + UCGame.bullet);
			System.out.println(bullet.getX() +" "+bullet.getY());

			break;
		case 1:
			
			if(hit != null)
				break;
			
			removeImage(weapon);
			
			if(direction == 0){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.MINE_THROW_RSC, 67, 43),0,0,7,0,true,40,true);
				addAnimation(hit, new Vector(25f, 5.0f));
			}else if(direction == 1){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.MINE_THROW_RSC, 67, 43),0,1,7,1,true,40,true);
				addAnimation(hit, new Vector(-25f, 5.0f));
			}
			
			hit.setLooping(false);
						
			mines.add(new Mine(getX(), getY(), Vector.getUnit(angle).add(dude.getVel())));
			
			System.out.println("array of mines: " + mines);

			break;
		case 2:
			
			if(hit != null)
				break;
			
			removeImage(weapon);
			
			if(direction == 0){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.PIPE_HIT_RSC, 99, 80),0,0,6,0,true,50,true);
				addAnimation(hit, new Vector(45f, -23.0f));
			}else if(direction == 1){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.PIPE_HIT_RSC, 99, 80),0,1,6,1,true,50,true);
				addAnimation(hit, new Vector(-45f, -23.0f));
			}
			
			hit.setLooping(false);
			break;
		}
	}
	
	public void changeWeapon(int w){
			
			switch(w){
			case 0:		// switch to primary weapon
				removeImage(weapon);
				
				weaponRight = primary;
				weaponLeft = primary.getFlippedCopy(true, false);
				
				wepOffSetRight = new Vector(primary.getWidth()/2 - 5, primary.getHeight()/2 - 5);
				wepOffSetLeft = new Vector(-wepOffSetRight.getX(), wepOffSetRight.getY());
								
				if(direction == 0){
					weapon = weaponRight;
					addImage(weapon, wepOffSetRight);
				}else{
					weapon = weaponLeft;
					addImage(weapon, wepOffSetLeft);
				}
				break;
			case 1:		// switch to secondary weapon
				removeImage(weapon);

				weaponRight = secondary;
				weaponLeft = secondary.getFlippedCopy(true, false);

				wepOffSetRight = new Vector(15.0f, primary.getHeight() - 10f);
				wepOffSetLeft = new Vector(-wepOffSetRight.getX(), wepOffSetRight.getY());
				
				if(direction == 0){
					weapon = weaponRight;
					addImage(weapon, wepOffSetRight);
				}else{
					weapon = weaponLeft;
					addImage(weapon, wepOffSetLeft);
				}
				break;
			case 2:		// switch to melee weapon
				removeImage(weapon);
				
				weaponRight = melee;
				weaponLeft = melee.getFlippedCopy(true, false);
										
				wepOffSetRight = new Vector(melee.getWidth()/2 - 5, -17.0f);
				wepOffSetLeft = new Vector(-wepOffSetRight.getX(), wepOffSetRight.getY());
				
				if(direction == 0){
					weapon = weaponRight;
					addImage(weapon, wepOffSetRight);
				}else{
					weapon = weaponLeft;
					addImage(weapon, wepOffSetLeft);
				}
				break;
			}
			
			select = w;
		
	}
	
	public void update(GameContainer container, int camX, int camY, Char dude, int delta){
		Input input = container.getInput();
		
		if(dude.getState() == 2)
			setPosition(getPosition().add(offsetCrouch));
		else
			setPosition(getPosition().add(offsetStand));
		
		
		if(dude.id == 1){
		mouse = new Vector(input.getMouseX(), input.getMouseY()); // get mouse location
		}
		wep = new Vector(getX() - dude.camX, getY() - dude.camY); 		  // get weapon pivot point
		
		angle = (float) wep.angleTo(mouse);						  // find angle of weapon aim
				
		if(angle <= 90 && angle >= -90){
			direction = 0;
		}else{
			direction = 1;
		}

		if(hit != null && hit.isStopped()){
			removeAnimation(hit);
			hit = null;
			changeWeapon(select);
		}

		if(direction == 1 && prevDirection != direction){ // face weapon left
			removeImage(weapon);
			removeAnimation(hit);
			hit = null;
			weapon = weaponLeft;

			offsetStand = offsetStand.setX(-1* offsetStand.getX());
			offsetCrouch = offsetCrouch.setX(-1* offsetCrouch.getX());
			prevDirection = direction;

			if(dude.getState() == 2)
				addImage(weapon, wepOffSetLeft);
			else
				addImage(weapon, wepOffSetLeft);	
			
		}else if(direction == 0 && prevDirection != direction){
			removeImage(weapon);
			removeAnimation(hit);
			hit = null;
			weapon = weaponRight;

			offsetStand = offsetStand.setX(offsetStand.abs().getX());
			offsetCrouch = offsetCrouch.setX(offsetCrouch.abs().getX());
			prevDirection = direction;
			
			if(dude.getState() == 2)
				addImage(weapon, wepOffSetRight);
			else
				addImage(weapon, wepOffSetRight);	
		}
		
		
		if(direction == 0)
			setRotation(angle);
		else{
			setRotation(angle + 180);
		}
		
	/*	for(Mine m : mines){
			m.update(delta);
		}
		
		for(Bullet b: bullets){
			b.update(delta);
		}
		*/

		
		for(Iterator<Mine> m = mines.iterator(); m.hasNext();){
			Mine mine = m.next();
			if(!mine.isActive())
				m.remove();
			else
				mine.update(delta);
		}
		
	}
	
}
