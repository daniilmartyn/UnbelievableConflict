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
import jig.Shape;
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
	public Vector offsetCrouch; // for offsetting weapon entity from char when crouched
	public Vector wepOffSetRight;	// for offsetting the weapon image
	public Vector wepOffSetLeft; // for offsetthing the weapon image
	
	public Vector mouse;
	private Vector wep;
	public float angle = 0;
	public int direction = 0;
	private int prevDirection = direction;
	
	public int resetCoolShotgun = 1000;	// the coolDown time
	public int coolShotgun = 0;		// the counter
	public int resetCoolGrenade = 500;
	public int coolGrenade = 0;
	public int resetCoolBomb = 500;
	public int coolBomb = 0;
	public int resetCoolPistol = 300;
	public int coolPistol = 0;
	
	
	private Shape lightWepBox = new ConvexPolygon(10.0f, 50.0f);
	private Shape mediumWepBox = new ConvexPolygon(20.0f, 50.0f);
	private Shape heavyWepBox = new ConvexPolygon(20.0f, 30.0f);
	
	private boolean active = false;
	
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
			//addShape(new ConvexPolygon(50.0f, 10.0f), Color.transparent, Color.green);
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
			//addShape(new ConvexPolygon(50.0f, 10.0f), Color.transparent, Color.green);
			
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
			//addShape(new ConvexPolygon(50.0f, 10.0f), Color.transparent, Color.green);
			
	//		bombs = new ArrayList<Bomb>(5);
	//		bullets = new ArrayList<Bullet>(5);
			
			rn = new Random();
			break;
		}
		
	}

	public boolean isActive(){
		return active;
	}
	
	public void fire(Char dude){
		
		switch(dude.playingCharacter*3 + select){
		case 0:
			
			if(dude.primaryAmmo <= 0)
				break;
			
			if(coolPistol > 0)
				break;
			else
				coolPistol = resetCoolPistol;
			
			System.out.println("I'ma firin mah lazer");
			Bullet bullet;
			
			Vector bulletOffSet = Vector.getUnit(angle).scale(50);
			
			if(direction == 0)
				bullet = new Bullet(getX() + bulletOffSet.getX(), getY() + bulletOffSet.getY(), Vector.getUnit(angle).scale(1.2f));
			else{
				bullet = new Bullet(getX() + bulletOffSet.getX(), getY() + bulletOffSet.getY(), Vector.getUnit(angle).scale(1.2f));
			}
			bullet.setRotation(angle);
					
			bullet.rotation = angle;
			bullet.setRotation(angle);
			bullet.id = dude.id;

			UCGame.bullets.add(bullet);
			dude.primaryAmmo--;

			ResourceManager.getSound(UCGame.PLAYER_PISTOLSOUND_RSC).play();
			dude.fired = true;
			//System.out.println("array of bullets: " + UCGame.bullets);
			
			break;
		case 1:
			
			if(dude.secondaryAmmo <= 0)
				break;
			
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
			dude.secondaryAmmo--;
			
			
			ResourceManager.getSound(UCGame.PLAYER_MINELAYSOUND_RSC).play();
			dude.fired = true;
	//		System.out.println("array of mines: " + UCGame.mines);

			break;
		case 2:
			
			if(hit != null)
				break;
			
			removeImage(weapon);
			
			if(direction == 0){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.PIPE_HIT_RSC, 99, 80),0,0,6,0,true,50,true);
				addAnimation(hit, new Vector(45f, -23.0f));
				addShape(lightWepBox, new Vector(70.0f, -20.0f), Color.transparent, Color.transparent);
				active = true;
			}else if(direction == 1){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.PIPE_HIT_RSC, 99, 80),0,1,6,1,true,50,true);
				addAnimation(hit, new Vector(-45f, -23.0f));
				addShape(lightWepBox, new Vector(-70.0f, -20.0f), Color.transparent, Color.transparent);
				active = true;
			}
			ResourceManager.getSound(UCGame.PLAYER_LIGHTMELEESOUND_RSC).play();
			dude.fired = true;
			hit.setLooping(false);
			break;
			
			
		case 3:
			
			if(dude.primaryAmmo <= 0)
				break;				
			
			if(coolShotgun > 0)
				break;
			else
				coolShotgun = resetCoolShotgun;
			
			for(int i = 0; i<3; i++){
				bulletOffSet = Vector.getUnit(angle).scale(87);

				float rnAngle = (rn.nextFloat() - 0.5f)*20;

				if(direction == 0){
					bulletOffSet = bulletOffSet.add(Vector.getUnit(angle).getPerpendicular().scale(-10));
					bullet = new Bullet(getX() + bulletOffSet.getX(), getY() + bulletOffSet.getY(), Vector.getUnit(angle+rnAngle).scale(.9f));
				}else{
					bulletOffSet = bulletOffSet.add(Vector.getUnit(angle).getPerpendicular().scale(10));
					bullet = new Bullet(getX() + bulletOffSet.getX(), getY() + bulletOffSet.getY(), Vector.getUnit(angle+rnAngle).scale(.9f));
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
			
			dude.primaryAmmo--;
			ResourceManager.getSound(UCGame.PLAYER_SHOTGUNSOUND_RSC).play();
			dude.fired = true;
			//System.out.println("array of bullets: " + UCGame.bullets);
			break;
		case 4: 
			
			if(dude.secondaryAmmo <= 0)
				break;
			
			if(coolGrenade > 0)
				break;
			else
				coolGrenade = resetCoolGrenade;
			
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
			grenade.rotation = angle;
			grenade.id = dude.id;
			UCGame.grenades.add(grenade);
			dude.secondaryAmmo--;
			
			ResourceManager.getSound(UCGame.PLAYER_BOMBSOUND_RSC).play();
			dude.fired = true;
	//		System.out.println("array of grenades: " + UCGame.grenades);
			break;
		case 5:
			if(hit != null)
				break;
			
			removeImage(weapon);
			
			if(direction == 0){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.SWORDCHOP_RSC, 149, 134),0,0,11,0,true,30,true);
				addAnimation(hit, new Vector(38f, -12f));
				addShape(mediumWepBox, new Vector(70.0f, -20.0f), Color.transparent, Color.transparent);
				active = true;
			}else if(direction == 1){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.SWORDCHOP_RSC, 149, 134),0,1,11,1,true,30,true);
				addAnimation(hit, new Vector(-38f, -12.0f));
				addShape(mediumWepBox, new Vector(-70.0f, -20.0f), Color.transparent, Color.transparent);
				active = true;
			}
			ResourceManager.getSound(UCGame.PLAYER_MEDIUMMELEESOUND_RSC).play();
			dude.fired = true;
			hit.setLooping(false);
			break;
		case 6:
			
			if(dude.primaryAmmo <= 0)
				break;
			
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
				bullet = new Bullet(getX() + bulletOffSet.getX(), getY() + bulletOffSet.getY(), Vector.getUnit(angle+rnAngle).scale(1f));
			}else{
				bulletOffSet = bulletOffSet.add(Vector.getUnit(angle).getPerpendicular().scale(2));
				bullet = new Bullet(getX() + bulletOffSet.getX(), getY() + bulletOffSet.getY(), Vector.getUnit(angle+rnAngle).scale(1f));
			}
			
//			bullet.setRotation(angle+rnAngle);
//			UCGame.bullets.add(bullet);
			
			bullet.rotation = angle+rnAngle;
			bullet.setRotation(angle+rnAngle);
			bullet.id = dude.id;
			UCGame.bullets.add(bullet);
			dude.primaryAmmo--;
			
		//	System.out.println("array of bullets: " + UCGame.bullets);
			ResourceManager.getSound(UCGame.PLAYER_RIFLESOUND_RSC).play();
			dude.fired = true;
			break;
			
		case 7:
						
			if(dude.secondaryAmmo <= 0)
				break;
			
			if(coolBomb > 0)
				break;
			else
				coolBomb = resetCoolBomb;
			
			Vector bombOffSet = Vector.getUnit(angle).scale(50);
			
			Bomb bomb = new Bomb(getX() + bombOffSet.getX(), getY() + bombOffSet.getY(), Vector.getUnit(angle).add(dude.getVel()).scale(1.3f));
			bomb.id = dude.id;
			UCGame.bombs.add(bomb);
			
			dude.secondaryAmmo--;
			//System.out.println("array of bombs: " + UCGame.bombs);
			ResourceManager.getSound(UCGame.PLAYER_BOMBSOUND_RSC).play();
			dude.fired = true;
			break;
		case 8:
			if(hit != null)
				break;
			
			removeImage(weapon);
			
			if(direction == 0){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.HAMMERHIT_RSC, 126, 201),0,0,5,0,true,60,true);
				addAnimation(hit, new Vector(55f, 0f));
				addShape(heavyWepBox, new Vector(100f, 0f), Color.transparent, Color.transparent);
				active = true;
			}else if(direction == 1){
				hit = new Animation(ResourceManager.getSpriteSheet(UCGame.HAMMERHIT_RSC, 126, 201),0,1,5,1,true,60,true);
				addAnimation(hit, new Vector(-63f, 0f));
				addShape(heavyWepBox, new Vector(-100f, 0f), Color.transparent, Color.transparent);
				active = true;
			}
			ResourceManager.getSound(UCGame.PLAYER_HEAVYMELEESOUND_RSC).play();
			dude.fired = true;
			hit.setLooping(false);
			break;
		}
	}
	
	public void changeWeapon(int w){
			
			switch(UCGame.character*3 + w){
			case 0:		// switch to primary weapon
				removeImage(weapon);
				
				if(hit != null){
					removeAnimation(hit);
					hit = null;
					meleeHit();
				}
				
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
				if(hit != null){
					removeAnimation(hit);
					hit = null;
					meleeHit();
				}
				
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
				if(hit != null){
					removeAnimation(hit);
					hit = null;
					meleeHit();
				}
				
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
				if(hit != null){
					removeAnimation(hit);
					hit = null;
					meleeHit();
				}
				
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
				if(hit != null){
					removeAnimation(hit);
					hit = null;
					meleeHit();
				}
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
				if(hit != null){
					removeAnimation(hit);
					hit = null;
					meleeHit();
				}
				
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
				if(hit != null){
					removeAnimation(hit);
					hit = null;
					meleeHit();
				}
				
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
				if(hit != null){
					removeAnimation(hit);
					hit = null;
					meleeHit();
				}
				
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
				if(hit != null){
					removeAnimation(hit);
					hit = null;
					meleeHit();
				}
				
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
	
	
	public void meleeHit(){			// this removes all the shapes associated with the weapon
		for(Shape s : getShapes())
			removeShape(s);
		
		active = false;
	}
	
	public void changeWepDir(){
		if(direction == 1 && prevDirection != direction){ // face weapon left
			removeImage(weapon);
			removeAnimation(hit);
			hit = null;
			meleeHit();
			weapon = weaponLeft;

			offsetStand = offsetStand.setX(-1* offsetStand.getX());
			offsetCrouch = offsetCrouch.setX(-1* offsetCrouch.getX());
			prevDirection = direction;

			addImage(weapon, wepOffSetLeft);	
			
		}else if(direction == 0 && prevDirection != direction){
			removeImage(weapon);
			removeAnimation(hit);
			hit = null;
			meleeHit();
			weapon = weaponRight;

			offsetStand = offsetStand.setX(offsetStand.abs().getX());
			offsetCrouch = offsetCrouch.setX(offsetCrouch.abs().getX());
			prevDirection = direction;
			
			addImage(weapon, wepOffSetRight);	
		}
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
			meleeHit();
			changeWeapon(select);
		}
		
		
		//System.out.println("weapon location x: " + (getX()-camX) + " and y: " + (getY() - camY));
		//System.out.println("weapon directon: " + direction);
		
		changeWepDir();
		
		
		if(direction == 0)
			setRotation(angle);
		else{
			setRotation(angle + 180);
		}
		
		if(coolShotgun > 0)
			coolShotgun -= delta;
		if(coolGrenade > 0)
			coolGrenade -= delta;
		if(coolBomb > 0)
			coolBomb -= delta;
		if(coolPistol > 0)
			coolPistol -= delta;
	}
	
}
