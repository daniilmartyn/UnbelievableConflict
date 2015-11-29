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

	public static final String PISTOL_RSC = "uc/resource/pistol.png";
	public static final String PIPE_RSC = "uc/resource/pipe.png";
	public static final String PIPE_HIT_RSC = "uc/resource/pipe_hit.png";
	public static final String MINE_RSC = "uc/resource/mine.png";
	public static final String MINE_THROW_RSC = "uc/resource/mine_throw.png";
	
	public static final String THEMINE_RSC = "uc/resource/theMine.png";
	public static final String BULLET_RSC = "uc/resource/bullet.png";


	
	public final int ScreenWidth;
	public final int ScreenHeight;
	
	
	
	boolean sound = true;

	public UCGame(String name, int width, int height) {
		super(name);
		ScreenWidth = width;
		ScreenHeight = height;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.CIRCLE);

	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new PlayState());		
		
		// load imgs and sounds
		
		ResourceManager.loadImage(TEST_RSC);
		
		ResourceManager.loadImage(STAND_LIGHT_RSC);
		ResourceManager.loadImage(RUN_LIGHT_RSC);
		ResourceManager.loadImage(CROUCH_LIGHT_RSC);

		ResourceManager.loadImage(PISTOL_RSC);
		ResourceManager.loadImage(PIPE_RSC);
		ResourceManager.loadImage(PIPE_HIT_RSC);
		ResourceManager.loadImage(MINE_RSC);
		ResourceManager.loadImage(MINE_THROW_RSC);
		
		ResourceManager.loadImage(THEMINE_RSC);
		ResourceManager.loadImage(BULLET_RSC);


		
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
