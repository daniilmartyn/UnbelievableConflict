package uc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

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

	private Image primary;
	private Image secondary;
	private Image melee;
	public Image weaponRight;
	public Image weaponLeft;
	public Image weapon;
	public int select; // 0 for primary, 1 for secondary, 2 for melee
	
	public Animation hit;
	
	public Vector offsetStand; // for offsetting weapon entity from char
	private Vector offsetCrouch; // for offsetting weapon entity from char when crouched
	public Vector wepOffSetRight;	// for offsetting the weapon image
	public Vector wepOffSetLeft; // for offsetthing the weapon image
	
	public Vector mouse;
	private Vector wep;
	public float angle = 0;
	public int direction = 0;
	private int prevDirection = direction;
	
//	private ArrayList<Mine> mines;
//	private ArrayList<Bullet> bullets;
//	private ArrayList<Bomb> bombs;
//	private ArrayList<Grenade> grenades;
	
	private Random rn;
	
	public Weapon(final float x, final float y, int type) { // Use a type represents character type
		super(x,y);
		
		// Start off weapon as being primary
		rn = new Random();

		switch(type){
		case 0:
			primary = ResourceManager.getImage(UCGame.PISTOL_RSC);
			secondary = ResourceManager.getImage(UCGame.MINE_RSC);
			melee = ResourceManager.getImage(UCGame.PIPE_RSC);
			
			offsetStand = new Vector(0.0f, -30f);
			offsetCrouch = new Vector(10f, -15f);
			weaponRight = primary;
			weaponLeft = primary.getFlippedCopy(true, false);
			
			weapon = weaponRight;
			wepOffSetRight = new Vector(primary.getWidth()/2 - 5, primary.getHeight()/2 - 5);
			wepOffSetLeft = new Vector(-wepOffSetRight.getX(), wepOffSetRight.getY());
			
			addImage(weapon, wepOffSetRight);
			addShape(new ConvexPolygon(50.0f, 10.0f), Color.transparent, Color.green);
//			mines = new ArrayList<Mine>(5);
//			bullets = new ArrayList<Bullet>(5);
			break;
		case 1:
			primary = ResourceManager.getImage(UCGame.SHOTGUN_RSC);
			secondary = ResourceManager.getImage(UCGame.LAUNCHER_RSC);
			melee = ResourceManager.getImage(UCGame.SWORD_RSC);
			
			offsetStand = new Vector(0.0f, -30f);
			offsetCrouch = new Vector(25f, -15f);
			weaponRight = primary;
			weaponLeft = primary.getFlippedCopy(true, false);
			
			weapon = weaponRight;
			wepOffSetRight = new Vector(primary.getWidth()/2 - 10, primary.getHeight()/2 - 15);
			wepOffSetLeft = new Vector(-wepOffSetRight.getX(), wepOffSetRight.getY());
			
			addImage(weapon, wepOffSetRight);
			addShape(new ConvexPolygon(50.0f, 10.0f), Color.transparent, Color.green);
			
//			bullets = new ArrayList<Bullet>(5);
//			grenades = new ArrayList<Grenade>(5);
			
			rn = new Random();
			break;
		case 2:
			primary = ResourceManager.getImage(UCGame.GUN_RSC);
			secondary = ResourceManager.getImage(UCGame.CANNON_RSC);
			melee = ResourceManager.getImage(UCGame.HAMMER_RSC);
			
			offsetStand = new Vector(0.0f, -30f);
			offsetCrouch = new Vector(0f, -25f);
			weaponRight = primary;
			weaponLeft = primary.getFlippedCopy(true, false);
			
			weapon = weaponRight;
			wepOffSetRight = new Vector(primary.getWidth()/2 - 5, primary.getHeight()/2 - 5);
			wepOffSetLeft = new Vector(-wepOffSetRight.getX(), wepOffSetRight.getY());
			
			addImage(weapon, wepOffSetRight);
			addShape(new ConvexPolygon(50.0f, 10.0f), Color.transparent, Color.green);
			
	//		bombs = new ArrayList<Bomb>(5);
	//		bullets = new ArrayList<Bullet>(5);
			
			rn = new Random();
			break;
		}
		
	}

//	public ArrayList<Mine> getMines(){
//		return mines;
//	}
//	
//	public ArrayList<Bomb> getBombs(){
//		return bombs;
//	}
//	
//	public ArrayList<Bullet> getBullets(){
//		return bullets;
//	}
//	
//	public ArrayList<Grenade> getGrenades(){
//		return grenades;
//	}
	
	public void fire(Char dude){
		
		switch(dude.playingCharacter*3 + select){
		case 0:
			Bullet bullet;
			
			Vector bulletOffSet = Vector.getUnit(angle).scale(50);
			
			if(direction == 0)
				bullet = new Bullet(getX() + bulletOffSet.getX(), getY() + bulletOffSet.getY(), Vector.getUnit(angle).scale(.5f));
			else{
				bullet = new Bullet(getX() + bulletOffSet.getX(), getY() + bulletOffSet.getY(), Vector.getUnit(angle).scale(.5f));
			}
			bullet.setRotation(angle);
					
			bullet.rotation = angle;
			bullet.setRotation(angle);
			bullet.id = dude.id;

			UCGame.bullets.add(bullet);
			ResourceManager.getSound(UCGame.PLAYER_PISTOLSOUND_RSC).play();

			//System.out.println("array of bullets: " + UCGame.bullets);
			
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
			
			Vector mineOffSet = Vector.getUnit(angle).scale(15);
			Mine mine = new Mine(getX() + mineOffSet.getX(), getY() + mineOffSet.getY(), Vector.getUnit(angle).add(dude.getVel()));
			mine.id = dude.id;
			UCGame.mines.add(mine);
			
			ResourceManager.getSound(UCGame.PLAYER_MINELAYSOUND_RSC).play();

	//		System.out.println("array of mines: " + UCGame.mines);

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
			ResourceManager.getSound(UCGame.PLAYER_LIGHTMELEESOUND_RSC).play();

			hit.setLooping(false);
			break;
			
			
		case 3:
							
			
			for(int i = 0; i<3; i++){
				bulletOffSet = Vector.getUnit(angle).scale(87);

				float rnAngle = (rn.nextFloat() - 0.5f)*10;

				if(direction == 0){
					bulletOffSet = bulletOffSet.add(Vector.getUnit(angle).getPerpendicular().scale(-10));
					bullet = new Bullet(getX() + bulletOffSet.getX(), getY() + bulletOffSet.getY(), Vector.getUnit(angle+rnAngle).scale(.5f));
				}else{
					bulletOffSet = bulletOffSet.add(Vector.getUnit(angle).getPerpendicular().scale(10));
					bullet = new Bullet(getX() + bulletOffSet.getX(), getY() + bulletOffSet.getY(), Vector.getUnit(angle+rnAngle).scale(.5f));
				}

//				bullet.setRotation(angle+rnAngle);
//				bullet.id = dude.id;
//
//				UCGame.bullets.add(bullet);
				
				bullet.rotation = angle+rnAngle;
				bullet.setRotation(angle+rnAngle);
				bullet.id = dude.id;
				UCGame.bullets.add(bullet);
			}
			
			ResourceManager.getSound(UCGame.PLAYER_SHOTGUNSOUND_RSC).play();

			//System.out.println("array of bullets: " + UCGame.bullets);
			break;
		case 4: 
			
			Grenade grenade;
			
			Vector greOffSet = Vector.getUnit(angle).scale(40);
			
			if(direction == 0){
				greOffSet = greOffSet.add(Vector.getUnit(angle).getPerpendicular().scale(-14));
				grenade = new Grenade(getX() + greOffSet.getX(), getY() + greOffSet.getY(), Vector.getUnit(angle).scale(1.5f));
			}else{
				greOffSet = greOffSet.add(Vector.getUnit(angle).getPerpendicular().scale(14));
				grenade = new Grenade(getX() + greOffSet.getX(), getY() + greOffSet.getY(), Vector.getUnit(angle).scale(1.5f));
			}
			
			grenade.setRotation(angle);
			grenade.id = dude.id;
			UCGame.grenades.add(grenade);
			
			ResourceManager.getSound(UCGame.PLAYER_BOMBSOUND_RSC).play();

	//		System.out.println("array of grenades: " + UCGame.grenades);
			break;
		case 5:
			if(hit != null)
				break;
			
			removeImage(weapon);
			
			if(direction == 0){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.SWORDCHOP_RSC, 149, 134),0,0,11,0,true,30,true);
				addAnimation(hit, new Vector(38f, -12f));
			}else if(direction == 1){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.SWORDCHOP_RSC, 149, 134),0,1,11,1,true,30,true);
				addAnimation(hit, new Vector(-38f, -12.0f));
			}
			ResourceManager.getSound(UCGame.PLAYER_MEDIUMMELEESOUND_RSC).play();

			hit.setLooping(false);
			//removeAnimation(hit);
			//removeImage(hit);
			break;
		case 6:
			
			if(hit != null)
				break;
			
			removeImage(weapon);
			
			if(direction == 0){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.GUNMOVE_RSC, 109, 13),0,0,3,0,true,30,true);
				addAnimation(hit, new Vector(40f, 1.0f));
			}else if(direction == 1){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.GUNMOVE_RSC, 109, 13),0,1,3,1,true,30,true);
				addAnimation(hit, new Vector(-40f, 1.0f));
			}
			
			hit.setLooping(false);
						
			bulletOffSet = Vector.getUnit(angle).scale(87);
			float rnAngle = (rn.nextFloat() - 0.5f)*10;
			
			if(direction == 0){
				bulletOffSet = bulletOffSet.add(Vector.getUnit(angle).getPerpendicular().scale(-2));
				bullet = new Bullet(getX() + bulletOffSet.getX(), getY() + bulletOffSet.getY(), Vector.getUnit(angle+rnAngle).scale(.5f));
			}else{
				bulletOffSet = bulletOffSet.add(Vector.getUnit(angle).getPerpendicular().scale(2));
				bullet = new Bullet(getX() + bulletOffSet.getX(), getY() + bulletOffSet.getY(), Vector.getUnit(angle+rnAngle).scale(.5f));
			}
			
//			bullet.setRotation(angle+rnAngle);
//			UCGame.bullets.add(bullet);
			
			bullet.rotation = angle+rnAngle;
			bullet.setRotation(angle+rnAngle);
			bullet.id = dude.id;
			UCGame.bullets.add(bullet);
			
			
		//	System.out.println("array of bullets: " + UCGame.bullets);
			ResourceManager.getSound(UCGame.PLAYER_RIFLESOUND_RSC).play();

			break;
			
		case 7:
						
			Vector bombOffSet = Vector.getUnit(angle).scale(50);
			
			Bomb bomb = new Bomb(getX() + bombOffSet.getX(), getY() + bombOffSet.getY(), Vector.getUnit(angle).add(dude.getVel()).scale(1.3f));
			bomb.id = dude.id;
			UCGame.bombs.add(bomb);
			
			//System.out.println("array of bombs: " + UCGame.bombs);
			ResourceManager.getSound(UCGame.PLAYER_BOMBSOUND_RSC).play();

			break;
		case 8:
			if(hit != null)
				break;
			
			removeImage(weapon);
			
			if(direction == 0){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.HAMMERHIT_RSC, 126, 201),0,0,5,0,true,60,true);
				addAnimation(hit, new Vector(55f, 0f));
			}else if(direction == 1){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.HAMMERHIT_RSC, 126, 201),0,1,5,1,true,60,true);
				addAnimation(hit, new Vector(-63f, 0f));
			}
			ResourceManager.getSound(UCGame.PLAYER_HEAVYMELEESOUND_RSC).play();

			hit.setLooping(false);
			break;
		}
	}
	
	public void changeWeapon(int w){
			
			switch(UCGame.character*3 + w){
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
				
			case 3:
				removeImage(weapon);
				
				weaponRight = primary;
				weaponLeft = primary.getFlippedCopy(true, false);
				
				wepOffSetRight = new Vector(primary.getWidth()/2 - 10, primary.getHeight()/2 - 15);
				wepOffSetLeft = new Vector(-wepOffSetRight.getX(), wepOffSetRight.getY());
								
				if(direction == 0){
					weapon = weaponRight;
					addImage(weapon, wepOffSetRight);
				}else{
					weapon = weaponLeft;
					addImage(weapon, wepOffSetLeft);
				}
				
				break;
			case 4:
				
				removeImage(weapon);
				
				weaponRight = secondary;
				weaponLeft = secondary.getFlippedCopy(true, false);
				
				wepOffSetRight = new Vector(secondary.getWidth()/2 - 10, secondary.getHeight()/2 - 20);
				wepOffSetLeft = new Vector(-wepOffSetRight.getX(), wepOffSetRight.getY());
								
				if(direction == 0){
					weapon = weaponRight;
					addImage(weapon, wepOffSetRight);
				}else{
					weapon = weaponLeft;
					addImage(weapon, wepOffSetLeft);
				}
				break;
			case 5:
				removeImage(weapon);
				
				weaponRight = melee;
				weaponLeft = melee.getFlippedCopy(true, false);
										
				wepOffSetRight = new Vector(melee.getWidth()/2 - 10, -25.0f);
				wepOffSetLeft = new Vector(-wepOffSetRight.getX(), wepOffSetRight.getY());
				
				if(direction == 0){
					weapon = weaponRight;
					addImage(weapon, wepOffSetRight);
				}else{
					weapon = weaponLeft;
					addImage(weapon, wepOffSetLeft);
				}
				break;
			
			case 6: 
				removeImage(weapon);
				
				weaponRight = primary;
				weaponLeft = primary.getFlippedCopy(true, false);
										
				wepOffSetRight = new Vector(primary.getWidth()/2 - 5, 1.0f);
				wepOffSetLeft = new Vector(-wepOffSetRight.getX(), wepOffSetRight.getY());
				
				if(direction == 0){
					weapon = weaponRight;
					addImage(weapon, wepOffSetRight);
				}else{
					weapon = weaponLeft;
					addImage(weapon, wepOffSetLeft);
				}
				
				break;
			case 7: 
				removeImage(weapon);
				
				weaponRight = secondary;
				weaponLeft = secondary.getFlippedCopy(true, false);
										
				wepOffSetRight = new Vector(secondary.getWidth()/2 - 10, 1.0f);
				wepOffSetLeft = new Vector(-wepOffSetRight.getX(), wepOffSetRight.getY());
				
				if(direction == 0){
					weapon = weaponRight;
					addImage(weapon, wepOffSetRight);
				}else{
					weapon = weaponLeft;
					addImage(weapon, wepOffSetLeft);
				}
				
				break;
			case 8:
				removeImage(weapon);
				
				weaponRight = melee;
				weaponLeft = melee.getFlippedCopy(true, false);
										
				wepOffSetRight = new Vector(melee.getWidth()/2 - 5, 0.0f);
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
		
		if(dude.getState() == 2)
			wep = new Vector(dude.getX() - dude.camX, dude.getY() - dude.camY + offsetCrouch.getY()); 		  // get weapon pivot point
		else
			wep = new Vector(dude.getX() - dude.camX, dude.getY() - dude.camY + offsetStand.getY());
		
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
		
		
		//System.out.println("weapon location x: " + (getX()-camX) + " and y: " + (getY() - camY));
		//System.out.println("weapon directon: " + direction);
		
		if(direction == 1 && prevDirection != direction){ // face weapon left
			removeImage(weapon);
			removeAnimation(hit);
			hit = null;
			weapon = weaponLeft;

			offsetStand = offsetStand.setX(-1* offsetStand.getX());
			offsetCrouch = offsetCrouch.setX(-1* offsetCrouch.getX());
			prevDirection = direction;

			addImage(weapon, wepOffSetLeft);	
			
		}else if(direction == 0 && prevDirection != direction){
			removeImage(weapon);
			removeAnimation(hit);
			hit = null;
			weapon = weaponRight;

			offsetStand = offsetStand.setX(offsetStand.abs().getX());
			offsetCrouch = offsetCrouch.setX(offsetCrouch.abs().getX());
			prevDirection = direction;
			
			addImage(weapon, wepOffSetRight);	
		}
		
		
		if(direction == 0)
			setRotation(angle);
		else{
			setRotation(angle + 180);
		}
		
//		for(Iterator<Bullet> b = bullets.iterator(); b.hasNext();){
//			Bullet bullet = b.next();
//			if(!bullet.isActive())
//				b.remove();
//			else
//				bullet.update(delta);
//		}
		
//		if(UCGame.character == 0){
//			for(Iterator<Mine> m = mines.iterator(); m.hasNext();){
//				Mine mine = m.next();
//				if(!mine.isActive())
//					m.remove();
//				else
//					mine.update(delta);
//			}
//		}
//		
//		if(UCGame.character == 1){
//			for(Iterator<Grenade> gre = grenades.iterator(); gre.hasNext();){
//				Grenade grenade = gre.next();
//				if(!grenade.isActive())
//					gre.remove();
//				else
//					grenade.update(delta);
//			}
//		}
//		
//		if(UCGame.character == 2){
//			for(Iterator<Bomb> b = bombs.iterator(); b.hasNext();){
//				Bomb bomb = b.next();
//				if(!bomb.isActive())
//					b.remove();
//				else
//					bomb.update(delta);
//			}
//		}
	}
	
}
