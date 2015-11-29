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

public class PlayState extends BasicGameState {

	Char dude;
	static Image map;
	
	public static float gravity = 0.003f;
	
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
		dude.renderWep(g);
		
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
		
		if(input.isKeyDown(Input.KEY_S)){
			//System.out.println("pressing s key");
			dude.setState(2);
			if(!dude.isJumped())
				dude.changeRunDir(-1);
			
		}

		if(input.isMousePressed(0))
			dude.fire();
		
		dude.update(gravity, container, camX, camY, delta);
		
		LinkedList<Shape> bounds = dude.getGloballyTransformedShapes();
		
		if(dude.getCoarseGrainedMinY() < 0){				// if dude is higher than than the map
			dude.setY(dude.getCoarseGrainedHeight()/2);
		}
		if(bounds.element().getMaxY() > map.getHeight()){	// if dude is lower than the map
			dude.setY(map.getHeight() - bounds.element().getHeight()/2);
			dude.cancelFall();
			dude.setJump(false);
		}
		if (bounds.element().getMinX() < bounds.element().getWidth()){			// if dude is too far left of map
			dude.setX(bounds.element().getWidth()*1.5f);
		}
		if (bounds.element().getMaxX() > map.getWidth() - bounds.element().getWidth()){ // if dude is too far right of map
			dude.setX(map.getWidth() - bounds.element().getWidth()*1.5f);
		}
		
		//System.out.println("minX: " + dude.getGloballyTransformedShapes());
		
		
		
		
		
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
