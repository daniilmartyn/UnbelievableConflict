package uc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import jig.ResourceManager;
import jig.Shape;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
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
	
	private Font font =  new TrueTypeFont(new java.awt.Font("Rockwell Extra Bold", java.awt.Font.PLAIN , 20), true);
	private Font fontTime = new TrueTypeFont(new java.awt.Font("Rockwell Extra Bold", java.awt.Font.PLAIN , 36), true);
	
	private boolean showStat;
	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
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

		UCGame uc = (UCGame) game;
		
		g.translate(-camX, -camY);
		
		g.drawImage(map.getImg(), 0, 0);
		map.render(g);
		
		for(Char Player : UCGame.players.values()){
			Player.render(g);
			Player.renderWep(g);
		}
		

		
//		for( int i =1; i< UCGame.players.size()+1; i++){
//			Char player = UCGame.players.get(i);
//			if(player != null){
//				player.render(g);
//				
//				player.renderWep(g);
//			}
//
//		}
		
		for( int i =0; i< UCGame.items.size(); i++){
			if(UCGame.items.get(i) != null){
				Item items = UCGame.items.get(i);
				if(items != null){
					items.render(g);
				}
			}
		}
		
		for( int i =0; i< UCGame.bullets.size(); i++){
			if(UCGame.bullets.get(i) != null){
				Bullet bullet = UCGame.bullets.get(i);
				if(bullet != null){
					bullet.render(g);
				}
			}
		}
		
		for( int i =0; i< UCGame.mines.size(); i++){
			if(UCGame.mines.get(i) != null){

				Mine mine = UCGame.mines.get(i);
				if(mine != null){
					mine.render(g);
				}
			}
		}
		
		for( int i =0; i< UCGame.bombs.size(); i++){
			if(UCGame.bombs.get(i) != null){
				Bomb bomb = UCGame.bombs.get(i);
				if(bomb != null){
					bomb.render(g);
				}
			}
		}
		
		for( int i =0; i< UCGame.grenades.size(); i++){
			if(UCGame.grenades.get(i) != null){

				Grenade grenade = UCGame.grenades.get(i);
				if(grenade != null){
					grenade.render(g);
				}		
			}
		}
		
		g.setColor(Color.green);
		g.drawString("Health: " + dude.getHealth(), 100 + camX, uc.getHeight()-50 + camY);
		g.drawString("Primary Ammo: " + dude.primaryAmmo, 250 + camX, uc.getHeight()-50 + camY);
		g.drawString("Secondary Ammo: " + dude.secondaryAmmo, 250 + camX, uc.getHeight()-25 + camY);

		if(showStat){
			g.drawImage(ResourceManager.getImage(UCGame.STATS_RSC), 0f + camX, 0f + camY);
			
			g.setColor(Color.yellow);
			
			float offset = 0.0f;
			
			ArrayList<Char> sorted = new ArrayList<Char>(UCGame.players.size());
			
			for(Char dude: UCGame.players.values()){
				sorted.add(dude);
			}
			
			Collections.sort(sorted);
			
			for(Char dude : sorted){
				font.drawString(225f + camX, 200f + camY + offset, "" + dude.id, Color.yellow);
				font.drawString(540f + camX, 200f + camY + offset, "" + dude.getKills(), Color.yellow);
				font.drawString(745f + camX, 200f + camY + offset, "" + dude.getDeaths(), Color.yellow);
				offset += 50.0f;
			}
		}
	}

	
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		
		Input input = container.getInput();
		UCGame uc = (UCGame) game;
		
		setCam(input, uc);
		
		
		
//		if(dude.weapon.hit != null && dude.weapon.hit.isStopped()){
//			dude.weapon.removeAnimation(dude.weapon.hit);
//			dude.weapon.hit = null;
//			//dude.weapon.changeWeapon(dude.weapon.select);
//		}
////		else if (dude.weapon.hit != null && !dude.weapon.hit.isStopped()){
//			if(dude.weapon.weapon != null){
//				dude.weapon.weapon = null;
//			}
//		}
		
		NetworkClasses.UpdateChar packet = new NetworkClasses.UpdateChar();

		
		if(input.getAbsoluteMouseX() < dude.getX() - camX){
			//dude.changeDir(1);
			packet.changeDir = 1;
		}
		else{
//			dude.changeDir(0);
			packet.changeDir = 0;
		}
		
		if(input.isKeyPressed(Input.KEY_TAB))
			showStat = !showStat;
		
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
			packet.id = uc.id;
			if(!dude.isJumped()){
				packet.runDir=-1;
			}
		}

		if(input.isMousePressed(0)){
			//dude.fire();
			System.out.println("hey mouse is clicked");
			packet.fire = true;
			packet.angle=dude.weapon.angle;
			packet.direction=dude.weapon.direction;
			packet.select=dude.weapon.select;
		}
		
		for(Char Player : UCGame.players.values()){
			if(Player.readytofire){
				Player.fire();
				Player.readytofire = false;
			}
			

		}
		
		for(Iterator<Explosion> e = UCGame.explosions.values().iterator(); e.hasNext();){
			Explosion explosion = e.next();
			if(explosion.isFinished()){
				e.remove();
			}			
		}
		
		packet.id = uc.id;
		packet.mousey = input.getMouseY();
		packet.mousex = input.getMouseX();
		
		packet.camx = camX;
		packet.camy = camY;
		
		uc.client.sendTCP(packet);
		
		/*if(input.isMouseButtonDown(0)){
			dude.fire();
			System.out.println("hey mouse button is down");
		}*/
		
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		UCGame uc = (UCGame) game;
		
		container.setSoundOn(UCGame.sound);
		
		showStat = false;
		
		ResourceManager.getSound(UCGame.GAME_MUSICSOUND_RSC).loop();
		
		map = new Map(0,0);
		
		switch(UCGame.character){
		case 0:
			dude = new Char(map.getImg().getWidth()/2, map.getImg().getHeight()/3*4, 0);
			break;
		case 1: 
			dude = new Char(map.getImg().getWidth()/2, map.getImg().getHeight()/3*4, 1);
			break;
		case 2:
			dude = new Char(map.getImg().getWidth()/2, map.getImg().getHeight()/3*4, 2);
			break;
		}
		
		dude.id = uc.id;
		uc.players.put(uc.id, dude);
		
		NetworkClasses.NewPlayerRequest packetX = new NetworkClasses.NewPlayerRequest();
		packetX.id = uc.id;
		packetX.player = uc.character;
		packetX.x = map.getImg().getWidth()/2;
		packetX.y = map.getImg().getHeight()/2;
		uc.client.sendTCP(packetX);
		
		uc.set = true;

		
		offsetMaxX = map.getImg().getWidth() - uc.ScreenWidth; 		// offset min/max X/Y technique taken from nathan
		offsetMaxY = map.getImg().getHeight() - uc.ScreenHeight;		// http://gamedev.stackexchange.com/questions/44256/how-to-add-a-scrolling-camera-to-a-2d-java-game
		offsetMinX = 0;										//
		offsetMinY = 0;										//
		
		//System.out.println("offsetX: " + offsetMaxX + " offsetY: "+offsetMaxY);
		camX = offsetMaxX;
		camY = 0;
	}

	public int getID() {
		return UCGame.PLAYSTATE2;
	}
}
