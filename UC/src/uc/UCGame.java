package uc;

import java.util.ArrayList;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class UCGame extends StateBasedGame{
	
	public static final int PLAYSTATE = 0;

	// img string stuff here
	
	public static final String TEST_RSC = "uc/resource/test.png";
	public static final String STAND_LIGHT_RSC = "uc/resource/stand_light.png";
	public static final String RUN_LIGHT_RSC = "uc/resource/run_light.png";
	public static final String CROUCH_LIGHT_RSC = "uc/resource/crouch_light.png";


	
	public final int ScreenWidth;
	public final int ScreenHeight;
	
	
	
	boolean sound = true;

	public UCGame(String name, int width, int height) {
		super(name);
		ScreenWidth = width;
		ScreenHeight = height;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);

	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new PlayState());		
		
		// load imgs and sounds
		
		ResourceManager.loadImage(TEST_RSC);
		ResourceManager.loadImage(STAND_LIGHT_RSC);
		ResourceManager.loadImage(RUN_LIGHT_RSC);
		ResourceManager.loadImage(CROUCH_LIGHT_RSC);


		
		//archie = new Archie(ScreenWidth/2, ScreenHeight/4);
		//shield = new Shield(archie.getCoarseGrainedMaxX() + 10, archie.getY());
	}
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new UCGame("Oh, the conflict!", 1024, 576));
			app.setDisplayMode(1024, 576, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

}
