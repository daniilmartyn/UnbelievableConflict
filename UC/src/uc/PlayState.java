package uc;

import jig.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class PlayState extends BasicGameState {

	Char dude;
	Image map;
	
	private float gravity = 0.003f;
	
	//private ParticleSystem particles;
	
	int offsetMaxX;
	int offsetMaxY;
	int offsetMinX;
	int offsetMinY;
	
	int camX;
	int camY;
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// TODO Auto-generated method stub

		UCGame uc = (UCGame) game;
		map = ResourceManager.getImage(UCGame.TEST_RSC);
		dude = new Char(map.getWidth()/2, map.getHeight()/2);
		//particles = new ParticleSystem
		
		
		offsetMaxX = map.getWidth() - uc.ScreenWidth; 		// offset min/max X/Y technique taken from nathan
		offsetMaxY = map.getHeight() - uc.ScreenHeight;		// http://gamedev.stackexchange.com/questions/44256/how-to-add-a-scrolling-camera-to-a-2d-java-game
		offsetMinX = 0;										//
		offsetMinY = 0;										//
		
		//System.out.println("offsetX: " + offsetMaxX + " offsetY: "+offsetMaxY);
		camX = offsetMaxX;
		camY = 0;
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
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub

		g.translate(-camX, -camY);
		
		g.drawImage(map, 0, 0);
		dude.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		
		Input input = container.getInput();
		UCGame uc = (UCGame) game;
		
		setCam(input, uc);
		
		if(input.getAbsoluteMouseX() < dude.getX() - camX)
			dude.changeDir(1);
		else
			dude.changeDir(0);
		
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
		
		if(input.isKeyDown(Input.KEY_S)){
			//System.out.println("pressing s key");
			dude.setState(2);
			if(!dude.isJumped())
				dude.changeRunDir(-1);
			
		}

		if(input.isMousePressed(0))
			System.out.println("click!");
		
		dude.update(gravity, delta);
		
		if(dude.getCoarseGrainedMinY() < 0){
			dude.setY(dude.getCoarseGrainedHeight()/2);
		}
		if(dude.getCoarseGrainedMaxY() > map.getHeight()){
			dude.setY(map.getHeight() - dude.getCoarseGrainedHeight()/2);
			dude.cancelFall();
			dude.setJump(false);
		}
		while (dude.getCoarseGrainedMinX() < 0){
			dude.goBack(delta);
		}
		while (dude.getCoarseGrainedMaxX() > map.getWidth()){
			dude.goBack(delta);
		}
		
				
		
		
		
		
		
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		// TODO Auto-generated method stub

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
