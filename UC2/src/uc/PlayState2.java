package uc;

import java.util.LinkedList;

import jig.ResourceManager;
import jig.Shape;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class PlayState2 extends BasicGameState {

	Char dude;
	public static Map map;
	
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

		g.translate(-camX, -camY);
		
		g.drawImage(map.getImg(), 0, 0);
		map.render(g);
		
//		for(Char Player : UCGame.players.values()){
//			Player.render(g);
//			Player.renderWep(g);
//		}
		
		
		for( int i =1; i< UCGame.players.size()+1; i++){
			Char Player = UCGame.players.get(i);
			Player.render(g);
			Player.renderWep(g);
		}
		
		for( int i =0; i< UCGame.bullets.size(); i++){
			//System.out.println("bullet "+i+" max "+UCGame.bullets.size());
			Bullet bullet = UCGame.bullets.get(i);
			bullet.render(g);
		}
		
		for( int i =0; i< UCGame.mines.size(); i++){
			Mine mine = UCGame.mines.get(i);
			mine.render(g);
		}
		
		for( int i =0; i< UCGame.bombs.size(); i++){
			Bomb bomb = UCGame.bombs.get(i);
			bomb.render(g);
		}
		
		for( int i =0; i< UCGame.grenades.size(); i++){
			Grenade grenade = UCGame.grenades.get(i);
			grenade.render(g);
		}
	
		
//		dude.render(g);
//		dude.renderWep(g);
		
	}

	
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		
		Input input = container.getInput();
		UCGame uc = (UCGame) game;
		
		setCam(input, uc);
		
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
		else if(input.isKeyPressed(Input.KEY_2)){
			packet.switchWep = 1;
//			dude.switchWep(1);
		}
		else if(input.isKeyPressed(Input.KEY_3)){
//			dude.switchWep(2);
			packet.switchWep = 2;
		}
		else{
			packet.switchWep  = dude.weapon.select;
		}
		
		if(input.isKeyPressed(Input.KEY_W) && !dude.isJumped()){ // make dude jump
//			dude.jump();
//			dude.setJump(true);
//			dude.setState(3);
			packet.setJump = true;
			packet.state = 1;
			packet.jump = true;
		}
				
		if((input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_D))){
			if(input.isKeyDown(Input.KEY_A)){
//				dude.changeRunDir(1); 			// run left
//				dude.setState(1);
				packet.runDir=1;
				packet.state=1;
			}else if(input.isKeyDown(Input.KEY_D)){	// run right
//				dude.changeRunDir(0);
//				dude.setState(1);
				packet.runDir=0;
				packet.state=1;
			}
		}else{
//			dude.changeRunDir(-1);			// set run velocity to 0.0
//			dude.setState(0);
			packet.runDir=-1;
			packet.state=0;
		}
		
		if(input.isKeyDown(Input.KEY_S)){ 	// make dude crouch
//			dude.setState(2);
//			if(!dude.isJumped())
//				dude.changeRunDir(-1);
			
			packet.state=2;
			packet.runDir=9;
			packet.id = uc.id;
			if(!dude.isJumped()){
				packet.runDir=-1;
			}
		}

		if(input.isMousePressed(0)){
			dude.fire();
			System.out.println("hey mouse is clicked");
			packet.fire = true;
			packet.angle=dude.weapon.angle;
			packet.direction=dude.weapon.direction;
			packet.select=dude.weapon.select;
		}
		
		packet.id = uc.id;
		packet.mousey = input.getMouseY();
		packet.mousex = input.getMouseX();
		
		packet.camx = camX;
		packet.camy = camY;
		
		uc.client.sendUDP(packet);
		
		/*if(input.isMouseButtonDown(0)){
			dude.fire();
			System.out.println("hey mouse button is down");
		}*/
		
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		UCGame uc = (UCGame) game;
		map = new Map(0,0);
		
		switch(UCGame.character){
		case 0:
			dude = new Char(map.getImg().getWidth()/2, map.getImg().getHeight()/2, 0);
			break;
		case 1: 
			dude = new Char(map.getImg().getWidth()/2, map.getImg().getHeight()/2, 1);
			break;
		case 2:
			dude = new Char(map.getImg().getWidth()/2, map.getImg().getHeight()/2, 2);
			break;
		}
		
		dude.id = uc.id;
		uc.players.put(uc.id, dude);
		
		NetworkClasses.NewPlayerRequest packetX = new NetworkClasses.NewPlayerRequest();
		packetX.id = uc.id;
		uc.client.sendUDP(packetX);
		
		uc.set = true;

		
		offsetMaxX = map.getImg().getWidth() - uc.ScreenWidth; 		// offset min/max X/Y technique taken from nathan
		offsetMaxY = map.getImg().getHeight() - uc.ScreenHeight;		// http://gamedev.stackexchange.com/questions/44256/how-to-add-a-scrolling-camera-to-a-2d-java-game
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
