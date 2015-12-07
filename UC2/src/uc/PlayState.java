package uc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import jig.ResourceManager;
import jig.Shape;
import NetworkClasses.SetXY;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;


public class PlayState extends BasicGameState {

	static Char dude;
	static Image map;
	
	public static float gravity = 0.003f;
	
	//private ParticleSystem particles;
	
	int offsetMaxX;
	int offsetMaxY;
	int offsetMinX;
	int offsetMinY;
	
	int camX;
	int camY;
	
	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// TODO Auto-generated method stub


	}

	private void setCam(Input input, UCGame uc){
		
		camX = (int) (dude.getX()  - uc.ScreenWidth/2 + (input.getAbsoluteMouseX()-uc.ScreenWidth/2)*.5f);
		camY = (int) (dude.getY() - uc.ScreenHeight/2 + (input.getAbsoluteMouseY()-uc.ScreenHeight/2)*.5f);
		
		if(camX > offsetMaxX)
			camX = offsetMaxX;
		if(camX < offsetMinX)
			camX = offsetMinX;
		if(camY > offsetMaxY)
			camY = offsetMaxY;
		if(camY < offsetMinY)
			camY = offsetMinY;
		
	}
	
	
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub
		UCGame uc = (UCGame)game;

		g.translate(-camX, -camY);
		
		g.drawImage(map, 0, 0);
		//dude.render(g);
	//	dude.renderWep(g);
		for(Char Player : UCGame.players.values()){
			Player.render(g);
			Player.renderWep(g);
		}
		
		for(Bullet bullet : UCGame.bullet){
			bullet.render(g);
		}
		g.setColor(Color.red);

		g.drawString("ID: " + uc.id, 800, 1000);
		//g.drawString("Points: " + UCGame.players.get(UCGame.id).points, 10, bg.ScreenHeight-50);
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		
		Input input = container.getInput();
		UCGame uc = (UCGame) game;
		
		setCam(input, uc);
		if(dude.id == 1){
			dude.camX =camX;
			dude.camY = camY;
		}
		
		if(input.getAbsoluteMouseX() < dude.getX() - camX)
			dude.changeDir(1);
		else
			dude.changeDir(0);
		
		if(input.isKeyPressed(Input.KEY_1)){
			dude.switchWep(0);
		}
		if(input.isKeyPressed(Input.KEY_2)){
			dude.switchWep(1);
		}
		if(input.isKeyPressed(Input.KEY_3)){
			dude.switchWep(2);
		}
		
		if(input.isKeyPressed(Input.KEY_W) && !dude.isJumped()){ // make dude jump
			dude.jump();
			dude.setJump(true);
			dude.setState(3);
		}
				
		if((input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_D))){
			if(input.isKeyDown(Input.KEY_A)){
				dude.changeRunDir(1); 			// run left
				dude.setState(1);
			}else if(input.isKeyDown(Input.KEY_D)){	// run right
				dude.changeRunDir(0);
				dude.setState(1);
			}
		}else{
			dude.changeRunDir(-1);			// set run velocity to 0.0
			dude.setState(0);
		}
		
		if(input.isKeyDown(Input.KEY_S)){ 	// make dude crouch
			dude.setState(2);
			if(!dude.isJumped())
				dude.changeRunDir(-1);
			
		}

		if(input.isMousePressed(0)){
			dude.fire();
		//	System.out.println("hey mouse is clicked");
		}
		
		//System.out.println(input.getMouseX()+" "+input.getMouseY());
		
//		dude.update(gravity, container, camX, camY, delta);
		
		NetworkClasses.UpdateBullet packetB = new NetworkClasses.UpdateBullet();
	//	packetB.bullet = new ArrayList<UpdateBulletInfo>();
		packetB.bulletx = new ArrayList<Float>();
		packetB.bullety = new ArrayList<Float>();
		packetB.rot = new ArrayList<Float>();

		for(Iterator<Bullet> b = UCGame.bullet.iterator(); b.hasNext();){
			Bullet bullet = b.next();
			if(!bullet.isActive()){
				b.remove();
			}
			else{
				bullet.update(delta);
			//  UpdateBulletInfo news = new UpdateBulletInfo();				
			//	news.x =bullet.getX();
			//	news.y =bullet.getY();
			//	news.vector = bullet.velocity;
			//	packetB.bullet.add(news);
				
				packetB.bulletx.add(bullet.getX());
				packetB.bullety.add(bullet.getY());
				
				packetB.rot.add(bullet.rotation);
//				packetB.bullet[q ]=((int)bullet.getX());
//				packetB.bullet[q+1] =((int)bullet.getY());
				//packetB.x = bullet.getX();
			//	packetB.y = bullet.getY();
			}
		}
		if(packetB.bulletx.size()>0){
		uc.client.sendUDP(packetB);
		}

		

		for(Char Player : UCGame.players.values()){
			Player.update(gravity, container, camX, camY, delta);
		

			LinkedList<Shape> bounds = Player.getGloballyTransformedShapes();
			
			if(Player.getCoarseGrainedMinY() < 0){				// if dude is higher than than the map
				Player.setY(Player.getCoarseGrainedHeight()/2);
			}
			if(bounds.element().getMaxY() > map.getHeight()){	// if dude is lower than the map
				Player.setY(map.getHeight() - bounds.element().getHeight()/2);
				Player.cancelFall();
				Player.setJump(false);
			}
			if (bounds.element().getMinX() < bounds.element().getWidth()){			// if dude is too far left of map
				Player.setX(bounds.element().getWidth()*1.5f);
			}
			if (bounds.element().getMaxX() > map.getWidth() - bounds.element().getWidth()){ // if dude is too far right of map
				Player.setX(map.getWidth() - bounds.element().getWidth()*1.5f);
			}
			
		//System.out.println("minX: " + dude.getGloballyTransformedShapes());
		
			NetworkClasses.SetXY packet = new NetworkClasses.SetXY();
			packet.x=Player.getX();
			packet.y=Player.getY();
			packet.x1 = Player.weapon.getX();
			packet.y1 = Player.weapon.getY();
			packet.jumped = Player.isJumped();
			packet.state = Player.state;
			packet.run = Player.direction;
	
			packet.rotate = Player.weapon.angle;
			packet.id = Player.id;
	
			uc.client.sendUDP(packet);
		}
		
		
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		UCGame uc = (UCGame) game;
		
		map = ResourceManager.getImage(UCGame.TEST_RSC);
		
		dude = new Char(map.getWidth()/2, map.getHeight()/2);
		dude.id = uc.id;
		uc.players.put(uc.id, dude);
		
		NetworkClasses.NewPlayerRequest packetX = new NetworkClasses.NewPlayerRequest();
		packetX.id = uc.id;
		uc.client.sendUDP(packetX);
		
		uc.set = true;
		
//		NetworkClasses.IExist IExist = new NetworkClasses.IExist();
//		IExist.id = uc.id;
//		uc.client.sendUDP(IExist);
//		
//		NetworkClasses.NewPlayerRequest packetX = new NetworkClasses.NewPlayerRequest();
//		packetX.id = uc.id;
//		uc.client.sendUDP(packetX);
		
		
		//particles = new ParticleSystem
		
		
		offsetMaxX = map.getWidth() - uc.ScreenWidth; 		// offset min/max X/Y technique taken from nathan
		offsetMaxY = map.getHeight() - uc.ScreenHeight;		// http://gamedev.stackexchange.com/questions/44256/how-to-add-a-scrolling-camera-to-a-2d-java-game
		offsetMinX = 0;										//
		offsetMinY = 0;										//
		
		//System.out.println("offsetX: " + offsetMaxX + " offsetY: "+offsetMaxY);
		camX = offsetMaxX;
		camY = 0;
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	public int getID() {
		// TODO Auto-generated method stub
		return UCGame.PLAYSTATE;
	}
}
