package uc;

import java.util.ArrayList;
import java.util.Iterator;

import jig.ResourceManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class PlayState extends BasicGameState {

	Char dude;
	public static Map map;
	
	public static float gravity = 0.003f;
		
	int offsetMaxX;
	int offsetMaxY;
	int offsetMinX;
	int offsetMinY;
	
	int camX;
	int camY;
	
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
		
		g.drawImage(map.getImg(), 0, 0); // the actual map image
		map.render(g);					// render the collision/shapes
		
		for(Char Player : UCGame.players.values()){
			Player.render(g);
			Player.renderWep(g);
		}
		
		for(Bullet bullet : UCGame.bullets){
			bullet.render(g);
		}
		
		for(Mine mine : UCGame.mines){
			mine.render(g);
		}
		
		for(Bomb bomb : UCGame.bombs){
			bomb.render(g);
		}
		
		for(Grenade grenade : UCGame.grenades){
			grenade.render(g);
		}		
		
		for(Item item: UCGame.items){
			item.render(g);
		}
		
		g.setColor(Color.green);
		g.drawString("Health: " + dude.getHealth(), 100 + camX, uc.getHeight()-50 + camY);
		g.drawString("Primary Ammo: " + dude.primaryAmmo, 250 + camX, uc.getHeight()-50 + camY);
		g.drawString("Secondary Ammo: " + dude.secondaryAmmo, 250 + camX, uc.getHeight()-25 + camY);

		if(showStat){
			g.drawImage(ResourceManager.getImage(UCGame.STATS_RSC), 0f + camX, 0f + camY);
			
			// then render out all of the stats for the players and stuff!
		}
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
		
		if(input.isKeyPressed(Input.KEY_ESCAPE))
			uc.enterState(UCGame.MENUSTATE);
		
		if(input.isKeyPressed(Input.KEY_TAB))
			showStat = !showStat;
		
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
			dude.justjumped = true;
			ResourceManager.getSound(UCGame.PLAYER_JUMPSOUND_RSC).play();

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
			dude.fired = true;
		//	System.out.println("hey mouse is clicked");
		}
		
		/*if(input.isMouseButtonDown(0)){
			dude.fire();
			System.out.println("hey mouse button is down");
		}*/
		for(Char Player : UCGame.players.values()){
			if(Player.readytofire){
				Player.fire();
				Player.readytofire = false;
				Player.fired = true;
			}
		}
		
		NetworkClasses.UpdateBullet packetB = new NetworkClasses.UpdateBullet();
		packetB.bulletx = new ArrayList<Float>();
		packetB.bullety = new ArrayList<Float>();
		
		packetB.bombx = new ArrayList<Float>();
		packetB.bomby = new ArrayList<Float>();
		
		packetB.grenadex = new ArrayList<Float>();
		packetB.grenadey = new ArrayList<Float>();
		
		packetB.minex = new ArrayList<Float>();
		packetB.miney = new ArrayList<Float>();
		
		packetB.rot = new ArrayList<Float>();

		
		for( int i =0; i< UCGame.bullets.size(); i++){
			Bullet bullet = UCGame.bullets.get(i);
			if(!bullet.isActive()){
				UCGame.bullets.remove(i);
				i--;
			}
			else{
				bullet.update(delta);
				packetB.bulletx.add(bullet.getX());
				packetB.bullety.add(bullet.getY());
				packetB.rot.add(bullet.rotation);
			}		
		}
		
		
//		for(Iterator<Bullet> b = UCGame.bullets.iterator(); b.hasNext();){
//			Bullet bullet = b.next();
//			if(!bullet.isActive()){
//				b.remove();
//			}
//			else{
//				bullet.update(delta);
//				packetB.bulletx.add(bullet.getX());
//				packetB.bullety.add(bullet.getY());
//				packetB.rot.add(bullet.rotation);
//			}
//		}
		
		for(Iterator<Bomb> b = UCGame.bombs.iterator(); b.hasNext();){
			Bomb bomb = b.next();
			if(!bomb.isActive()){
				b.remove();
			}
			else{
				bomb.update(delta);
				packetB.bombx.add(bomb.getX());
				packetB.bomby.add(bomb.getY());
			}
		}
		
		for(Iterator<Mine> b = UCGame.mines.iterator(); b.hasNext();){
			Mine mine = b.next();
			if(!mine.isActive()){
				b.remove();
			}
			else{
				mine.update(delta);
				packetB.minex.add(mine.getX());
				packetB.miney.add(mine.getY());
			}
		}
		
		
		for( int i =0; i< UCGame.grenades.size(); i++){
			Grenade grenade = UCGame.grenades.get(i);
			if(!grenade.isActive()){
				UCGame.grenades.remove(i);
				i--;
			}
			else{
				grenade.update(delta);
				packetB.grenadex.add(grenade.getX());
				packetB.grenadey.add(grenade.getY());
			}		
		}
		
		
		for(Item i : UCGame.items)
			i.update(delta);
		
//		if(packetB.bulletx.size()>0 ||
//		packetB.grenadex.size()>0 ||
//		packetB.minex.size()>0 ||
//		packetB.bombx.size()>0){

			UCGame.client.sendUDP(packetB);
//		}
		
	
		
		
		
		
		for(Char Player : UCGame.players.values()){
			Player.update( container, camX, camY, delta);
		
			NetworkClasses.SetXY packet = new NetworkClasses.SetXY();
			packet.x=Player.getX();
			packet.y=Player.getY();
			packet.x1 = Player.weapon.getX();
			packet.y1 = Player.weapon.getY();
			packet.jumped = Player.isJumped();
			if(Player.justjumped){
				packet.justjumped =true;
				Player.justjumped = false;
			}
			packet.state = Player.state;
			packet.run = Player.direction;
			
			
//			if(Player.weapon.hit != null){
//				packet.hit =true;
//				packet.hitisStopped = Player.weapon.hit.isStopped();
//
//			}
//			else{
//				packet.hit =false;
//				packet.hitisStopped = true;
//			}
			if(Player.fired){
				packet.fired = true;
				Player.fired = false;
			}
			else{
				packet.fired = false;
			}
			if(Player.hurt){
				packet.hurt = true;
				Player.hurt = false;
			}
			else{
				packet.hurt = false;
			}
			packet.weapon = Player.weapon.select;
			packet.rotate = Player.weapon.angle;
			packet.id = Player.id;
	
			UCGame.client.sendUDP(packet);
		}		
		
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		// TODO Auto-generated method stub

		UCGame uc = (UCGame) game;
		
		container.setSoundOn(UCGame.sound);
		
		ResourceManager.getSound(UCGame.GAME_MUSICSOUND_RSC).loop();

		map = new Map(0,0);
		showStat = false;
		
		switch(UCGame.character){
		case 0:
			dude = new Char(map.getImg().getWidth()/2, map.getImg().getHeight()/4*3, 0);
			break;
		case 1: 
			dude = new Char(map.getImg().getWidth()/2, map.getImg().getHeight()/4*3, 1);
			break;
		case 2:
			dude = new Char(map.getImg().getWidth()/2, map.getImg().getHeight()/4*3, 2);
			break;
		}
		
		dude.id = 1;
		UCGame.players.put(UCGame.id, dude);
		
		NetworkClasses.NewPlayerRequest packetX = new NetworkClasses.NewPlayerRequest();
		packetX.player = UCGame.character;
		packetX.x = map.getImg().getWidth()/2;
		packetX.y = map.getImg().getHeight()/2;

		packetX.id = UCGame.id;
		UCGame.client.sendTCP(packetX);
		
		UCGame.set = true;

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
		return UCGame.PLAYSTATE;
	}
}
