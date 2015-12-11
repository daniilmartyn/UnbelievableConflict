package Client;

import java.util.Iterator;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/**
 * This state is active when the Game is being played. In this state, sound is
 * turned on, the bounce counter begins at 0 and increases until 10 at which
 * point a transition to the Game Over state is initiated. The user can also
 * control the ball using the WAS & D keys.
 * 
 * Transitions From StartUpState
 * 
 * Transitions To GameOverState
 */
class PlayingState extends BasicGameState {
	int bounces;
	int lives;
	int blocksLeft;
	
	public static Input inputHandler;


	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
	//	bounces = 0;
		//BounceGameClient bg = (BounceGameClient)game;
	//	container.setSoundOn(true);
		
	}
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		BounceGameClient bg = (BounceGameClient)game;
		g.setColor(Color.red);

		for(movingChar mpPlayer : bg.players2.values()){
			//g.drawRect(mpPlayer.x, mpPlayer.y, 32, 32);
			if(mpPlayer.getNumImages() ==0){
			mpPlayer.addImageWithBoundingBox(ResourceManager.
					getImage(BounceGameClient.BALL_BALLIMG_RSC),new Vector(mpPlayer.x,mpPlayer.y));
			}
			mpPlayer.render(g);
		}
		
		for(Bullet mpPlayer : BounceGameClient.bullet){
			mpPlayer.render(g);

		}
		
//		for(Pellet mpPlayer : bg.myPellet){
//			//g.drawRect(mpPlayer.x, mpPlayer.y, 32, 32);
//			if(mpPlayer.getNumImages() ==0){
//			mpPlayer.addImageWithBoundingBox(ResourceManager.
//					getImage(BounceGameClient.BALL_PELLETIMG_RSC),new Vector(mpPlayer.x,mpPlayer.y));
//			}
//			mpPlayer.render(g);
//		}
//		
//		for(Pellet mpPlayer : bg.otherPellet){
//			//g.drawRect(mpPlayer.x, mpPlayer.y, 32, 32);
//			if(mpPlayer.getNumImages() ==0){
//			mpPlayer.addImageWithBoundingBox(ResourceManager.
//					getImage(BounceGameClient.BALL_PELLETIMG_RSC),new Vector(mpPlayer.x,mpPlayer.y));
//			}
//			mpPlayer.render(g);
//		}
		
		g.drawString("ID: " + bg.id, 10, bg.ScreenHeight-30);
		g.drawString("Points: " + bg.players2.get(bg.id).points, 10, bg.ScreenHeight-50);
	}

	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {
		
		BounceGameClient bg = (BounceGameClient)game;

		//bg.ball.update(delta);
		inputHandler = container.getInput();
		
	//	bg.player.update();
		
		
		if((inputHandler.isKeyDown(Input.KEY_W))){
			NetworkClasses.PacketUpdateY packetY = new NetworkClasses.PacketUpdateY();
			packetY.y = -5;
			bg.client.sendUDP(packetY);
			//y -= 5;
		}
		if((inputHandler.isKeyDown(Input.KEY_S))){
			NetworkClasses.PacketUpdateY packetY = new NetworkClasses.PacketUpdateY();
			packetY.y = 5;
			bg.client.sendUDP(packetY);		
			}
		if((inputHandler.isKeyDown(Input.KEY_A))){
			NetworkClasses.PacketUpdateX packetX = new NetworkClasses.PacketUpdateX();
			packetX.x = -5;
			bg.client.sendUDP(packetX);
		}
		if((inputHandler.isKeyDown(Input.KEY_D))){
			NetworkClasses.PacketUpdateX packetX = new NetworkClasses.PacketUpdateX();
			packetX.x = 5;
			bg.client.sendUDP(packetX);
		}
		if((inputHandler.isKeyPressed(Input.KEY_SPACE))){
			NetworkClasses.CreatePellet packet = new NetworkClasses.CreatePellet();

		    float x = bg.players2.get(bg.id).x;
		    float y = bg.players2.get(bg.id).y;
		    
			
			packet.vx = .1f;
			packet.vy = 0;
			packet.x = x;
			packet.y = y;

			bg.client.sendUDP(packet);
		}
		
		  for (int i = 0; i < bg.bullet.size(); ++i) {
			  Bullet bull = bg.bullet.get(i);
			  bull.update(delta);
				
				if(!bull.active){
					bg.bullet.remove(bull);
					i--;
				}
		  }
		
//		for(Bullet mpPlayer : bg.bullet){
//
//
//		}
//		int q = 0;
//		for(Pellet myPellets : bg.myPellet){
//			NetworkClasses.UpdatePellet packet = new NetworkClasses.UpdatePellet();
//			packet.numPel =q;
//			packet.delta = delta;
//			bg.client.sendUDP(packet);
//			q++;
//
//		}
//		
//		q = 0;
//		for(Pellet myPellet : bg.myPellet){
//			for(movingChar players : bg.players2.values()){
//				if(myPellet.collides(players) != null && players.id != bg.id){
//					NetworkClasses.HitRequest packet = new NetworkClasses.HitRequest();
//					packet.numPel =q;
//					packet.playerHit = players.id;
//					bg.client.sendUDP(packet);
//					q++;
//				}
//			}
//
//
//		}
//		
		
		
//		if( inputHandler.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
//			    int mouseX = inputHandler.getMouseX();
//			    int mouseY = inputHandler.getMouseY();
//			    float x = bg.players2.get(bg.id).x;
//			    float y = bg.players2.get(bg.id).y;
//			    
//			    float pathX = mouseX -x;
//			    float pathY = mouseY -y;
//			    
//			    float distance = (float) Math.sqrt(pathX * pathX + pathY * pathY);
//			    float directionX = pathX / distance;
//			    float directionY = pathY / distance;
//			    
//			    float movementX = directionX * 0.35f;
//			    float movementY = directionY * 0.35f;
//			    
//			    
//				NetworkClasses.CreatePellet packet = new NetworkClasses.CreatePellet();
//				
//				packet.vx = movementX;
//				packet.vy = movementY;
//				packet.x = x;
//				packet.y = y;
//
//				bg.client.sendUDP(packet);
//
//		}
		
		

	}

	@Override
	public int getID() {
		return BounceGameClient.PLAYINGSTATE;
	}
	
}