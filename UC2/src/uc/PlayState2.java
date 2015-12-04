package uc;

import java.util.LinkedList;

import jig.ResourceManager;
import jig.Shape;
import jig.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;


public class PlayState2 extends BasicGameState {

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
		
		//dude.weapon.mouse = new Vector(input.getMouseX(),input.getMouseY());
		
		NetworkClasses.UpdateChar packet = new NetworkClasses.UpdateChar();
		
		if(input.getAbsoluteMouseX() < dude.getX() - camX){
			//dude.changeDir(1);
			packet.changeDir = 1;
		}
		else{
//			dude.changeDir(0);
			packet.changeDir = 0;
		}
		
		if(input.isKeyPressed(Input.KEY_1)){
			packet.switchWep = 0;
			//dude.switchWep(0);
		}
		if(input.isKeyPressed(Input.KEY_2)){
			packet.switchWep = 1;

//			dude.switchWep(1);
		}
		if(input.isKeyPressed(Input.KEY_3)){
//			dude.switchWep(2);
			packet.switchWep = 1;
		}		
		if(input.isKeyPressed(Input.KEY_W) && !dude.isJumped()){ // make dude jump
			//NetworkClasses.PacketUpdateY packetY = new NetworkClasses.PacketUpdateY();
			packet.setJump = true;
			packet.state = 1;
			packet.jump = true;
			packet.id = uc.id;
			//uc.client.sendUDP(packetY);
		}
				
		if((input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_D))){
			if(input.isKeyDown(Input.KEY_A)){
			//	NetworkClasses.PacketUpdateX packetX = new NetworkClasses.PacketUpdateX();
				packet.runDir=1;
				packet.state=1;
				packet.id = uc.id;				
				//uc.client.sendUDP(packetX);
			}else if(input.isKeyDown(Input.KEY_D)){	// run right
				
			//	NetworkClasses.PacketUpdateX packetX = new NetworkClasses.PacketUpdateX();
				packet.runDir=0;
				packet.state=1;
				packet.id = uc.id;
				//uc.client.sendUDP(packetX);
			}
		}else{
			//NetworkClasses.PacketUpdateX packetX = new NetworkClasses.PacketUpdateX();
			packet.runDir=-1;
			packet.state=0;
			packet.id = uc.id;
		//	uc.client.sendUDP(packetX);
		}
		
		if(input.isKeyDown(Input.KEY_S)){
			
		//	NetworkClasses.PacketUpdateX packetX = new NetworkClasses.PacketUpdateX();
			packet.state=2;
			packet.runDir=9;
			packet.id = uc.id;
			if(!dude.isJumped()){
				packet.runDir=-1;
			}
		//	uc.client.sendUDP(packetX);

		}

		if(input.isMousePressed(0)){
			
		//	NetworkClasses.FiredGun packet = new NetworkClasses.FiredGun();
			packet.fire = true;
			packet.angle=dude.weapon.angle;
			packet.direction=dude.weapon.direction;
			packet.select=dude.weapon.select;
			packet.id = uc.id;

			//uc.client.sendUDP(packet);
			
			//dude.fire();
		}
		
		packet.mousey = input.getMouseY();
		packet.mousex = input.getMouseX();
		
		packet.camx = camX;
		packet.camy = camY;
		
		//System.out.println(dude.weapon.mouse);

		uc.client.sendUDP(packet);

		
//		Vector mouse = new Vector(input.getMouseX(), input.getMouseY()); // get mouse location
//		packetM.mouse = mouse;
//		NetworkClasses.MouseMoved packetM = new NetworkClasses.MouseMoved();
//		packetM.id = uc.id;
//		packetM.mouse = mouse;
//		uc.client.sendUDP(packetM);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		UCGame uc = (UCGame) game;
		
		map = ResourceManager.getImage(UCGame.TEST_RSC);
		
		dude = new Char(map.getWidth()/2, map.getHeight()/2);
		dude.id = uc.id;
		uc.players.put(uc.id, dude);
		//dude.setJump(false);

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
		return UCGame.PLAYSTATE2;
	}
}
