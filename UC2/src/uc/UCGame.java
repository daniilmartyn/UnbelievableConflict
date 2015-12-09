package uc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import NetworkClasses.LoginRequest;
import NetworkClasses.LoginResponse;
import NetworkClasses.Message;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

public class UCGame extends StateBasedGame{
	
	public static final int MENUSTATE = 0;
	public static final int PLAYSTATE = 1;
	public static final int PLAYSTATE2 = 2;
	public static final int GAMEOVER = 3;

	// snd string stuff here
	public static final String GAME_STARTSOUND_RSC = "uc/sound/start.ogg";
	public static final String GAME_MUSICSOUND_RSC = "uc/sound/boss.ogg";
	public static final String PLAYER_PISTOLSOUND_RSC = "uc/sound/pistol1.wav";
	public static final String PLAYER_RIFLESOUND_RSC = "uc/sound/rifle.wav";
	public static final String PLAYER_SHOTGUNSOUND_RSC = "uc/sound/shotgun.wav";
	public static final String PLAYER_BOMBSOUND_RSC = "uc/sound/boom.wav";
	public static final String PLAYER_MINELAYSOUND_RSC = "uc/sound/beep.ogg";
	public static final String PLAYER_JUMPSOUND_RSC = "uc/sound/jump.wav";

	public static final String PLAYER_HEAVYMELEESOUND_RSC = "uc/sound/heavy melee sound.wav";
	public static final String PLAYER_MEDIUMMELEESOUND_RSC = "uc/sound/medium melee sound.wav";
	public static final String PLAYER_LIGHTMELEESOUND_RSC = "uc/sound/light melee sound.wav";

//	public static final String PLAYER_LASERSOUND_RSC = "BounceShootBounce/soundResource/LaserShot.wav";
//	public static final String PLAYER_NOLASERSOUND_RSC = "BounceShootBounce/soundResource/noLaserSound.wav";
//	public static final String ENEMY_SNIPERSOUND_RSC = "BounceShootBounce/soundResource/sniperShot.wav";
//	public static final String ENEMY_LASERSOUND_RSC = "BounceShootBounce/soundResource/enemyLaser.wav";
//	public static final String ENEMY_BOMBSOUND_RSC = "BounceShootBounce/soundResource/bomb.wav";
//	public static final String ENEMY_DEADSOUND_RSC = "BounceShootBounce/soundResource/enemyDead.wav";
	
	// img string stuff here
	
	public static final String MAINMENU_RSC = "uc/resource/main.png";
	public static final String CONTROLS_RSC = "uc/resource/controls.png";
	public static final String CLASSES_RSC = "uc/resource/classes.png";
	public static final String OBJECTS_RSC = "uc/resource/objects.png";
	public static final String JOIN_RSC = "uc/resource/join.png";
	public static final String HOST_RSC = "uc/resource/host.png";
	public static final String OVER_RSC = "uc/resource/over.png";
	public static final String ARROW_RSC = "uc/resource/arrow.png";

	
	public static final String TEST_RSC = "uc/resource/test.png";
	public static final String DESERT_RSC = "uc/resource/desert.png";

	public static final String STAND_LIGHT_RSC = "uc/resource/stand_light.png";
	public static final String RUN_LIGHT_RSC = "uc/resource/run_light.png";
	public static final String CROUCH_LIGHT_RSC = "uc/resource/crouch_light.png";
	
	public static final String STAND_MED_RSC = "uc/resource/med.png";
	public static final String CROUCH_MED_RSC = "uc/resource/crouch_med.png";
	public static final String RUN_MED_RSC = "uc/resource/run_med.png";

	public static final String STAND_HEV_RSC = "uc/resource/heavy.png";
	public static final String CROUCH_HEV_RSC = "uc/resource/crouch_hev.png";
	public static final String RUN_HEV_RSC = "uc/resource/run_hev.png";


	public static final String PISTOL_RSC = "uc/resource/pistol.png";
	public static final String PIPE_RSC = "uc/resource/pipe.png";
	public static final String PIPE_HIT_RSC = "uc/resource/pipe_hit.png";
	public static final String MINE_RSC = "uc/resource/mine.png";
	public static final String MINE_THROW_RSC = "uc/resource/mine_throw.png";
	
	public static final String SHOTGUN_RSC = "uc/resource/shotgun.png";
	public static final String LAUNCHER_RSC = "uc/resource/launcher.png";
	public static final String SWORD_RSC = "uc/resource/sword.png";
	public static final String SWORDCHOP_RSC = "uc/resource/swordChop.png";

	public static final String GUN_RSC = "uc/resource/gun.png";
	public static final String GUNMOVE_RSC = "uc/resource/gunmove.png";
	public static final String CANNON_RSC = "uc/resource/cannon.png";
	public static final String HAMMER_RSC = "uc/resource/hammer.png";
	public static final String HAMMERHIT_RSC = "uc/resource/hammerHit.png";

	
	public static final String THEMINE_RSC = "uc/resource/theMine.png";
	public static final String BULLET_RSC = "uc/resource/bullet.png";
	public static final String BOMB_RSC = "uc/resource/bomb.png";
	public static final String GRENADE_RSC = "uc/resource/grenade.png";

	
	public final int ScreenWidth;
	public final int ScreenHeight;
	
	public static int character = 2; // 0 for light, 1 for medium, 2 for heavy
	public static boolean sound = true;
	
	
	public static boolean set;
	
	public static boolean isServer;
	
	private int tcpPort;
	private int udpPort;
	private int timeout;	
	public static AppGameContainer app;
	public static Input inputHandler;
	
	public static Client client;
	public Kryo kryo;
	public static int id;
	
	public static Map<Integer,Char> players = new HashMap<Integer,Char>(); 
	public final static List<Bullet> bullets = new ArrayList<Bullet>();
	public final static List<Mine> mines = new ArrayList<Mine>();
	public final static List<Bomb> bombs = new ArrayList<Bomb>();
	public final static List<Grenade> grenades = new ArrayList<Grenade>();


	public UCGame(String name, int width, int height,int tcpPort, int udpPort, int timeout) {
		super(name);
		ScreenWidth = width;
		ScreenHeight = height;
		
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		this.timeout = timeout;
		
		Entity.setCoarseGrainedCollisionBoundary(Entity.CIRCLE);

	}
public void connect(String ip){
		
		try {
			Log.info("connecting..");
			client.start();
			client.connect(timeout, ip, tcpPort, udpPort);
			client.addListener(new playerClientListener());
					
			LoginRequest req = new LoginRequest();
			req.setUserName("raLa");
			req.setUserPassword("test");
			client.sendTCP(req);
			
			
			Log.info("Connected.");
		
			
		} catch (IOException e) {
			Log.info("Eingabe falsch / Server offline");
			e.printStackTrace();
		}
	}
	
	
	public void disconnect(){
		Log.info("disconnecting..");
		client.stop();
	}
	
	public void registerKryoClasses(){
		kryo.register(LoginRequest.class);
		kryo.register(LoginResponse.class);
		kryo.register(Message.class);
		kryo.register(playerChar.class);
		kryo.register(org.newdawn.slick.geom.Rectangle.class);
		kryo.register(float[].class);
		kryo.register(NetworkClasses.PacketAddPlayer.class);
		kryo.register(NetworkClasses.PacketRemovePlayer.class);
		kryo.register(NetworkClasses.CollideRequest.class);
		kryo.register(NetworkClasses.NewPlayerRequest.class);
		kryo.register(NetworkClasses.SetXY.class);
		kryo.register(NetworkClasses.UpdateChar.class);
		kryo.register(NetworkClasses.UpdateBullet.class);
		kryo.register(ArrayList.class);

	}
	
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new MenuState());
		addState(new PlayState());
		addState(new PlayState2());		
		addState(new GameOver());
		
		
		// load imgs and sounds
		
		ResourceManager.loadImage(MAINMENU_RSC);
		ResourceManager.loadImage(CONTROLS_RSC);
		ResourceManager.loadImage(CLASSES_RSC);
		ResourceManager.loadImage(OBJECTS_RSC);
		ResourceManager.loadImage(JOIN_RSC);
		ResourceManager.loadImage(HOST_RSC);
		ResourceManager.loadImage(OVER_RSC);
		ResourceManager.loadImage(ARROW_RSC);


		ResourceManager.loadImage(TEST_RSC);
		ResourceManager.loadImage(DESERT_RSC);

		
		ResourceManager.loadImage(STAND_LIGHT_RSC);
		ResourceManager.loadImage(RUN_LIGHT_RSC);
		ResourceManager.loadImage(CROUCH_LIGHT_RSC);
		
		ResourceManager.loadImage(STAND_MED_RSC);
		ResourceManager.loadImage(CROUCH_MED_RSC);
		ResourceManager.loadImage(RUN_MED_RSC);

		ResourceManager.loadImage(STAND_HEV_RSC);
		ResourceManager.loadImage(CROUCH_HEV_RSC);
		ResourceManager.loadImage(RUN_HEV_RSC);


		ResourceManager.loadImage(PISTOL_RSC);
		ResourceManager.loadImage(PIPE_RSC);
		ResourceManager.loadImage(PIPE_HIT_RSC);
		ResourceManager.loadImage(MINE_RSC);
		ResourceManager.loadImage(MINE_THROW_RSC);
		
		ResourceManager.loadImage(SHOTGUN_RSC);
		ResourceManager.loadImage(LAUNCHER_RSC);
		ResourceManager.loadImage(SWORD_RSC);
		ResourceManager.loadImage(SWORDCHOP_RSC);

		ResourceManager.loadImage(GUN_RSC);			// machine gun, specifically
		ResourceManager.loadImage(GUNMOVE_RSC);
		ResourceManager.loadImage(CANNON_RSC);
		ResourceManager.loadImage(HAMMER_RSC);
		ResourceManager.loadImage(HAMMERHIT_RSC);

		ResourceManager.loadImage(THEMINE_RSC);
		ResourceManager.loadImage(BULLET_RSC);
		ResourceManager.loadImage(BOMB_RSC);
		ResourceManager.loadImage(GRENADE_RSC);

		ResourceManager.loadSound(GAME_STARTSOUND_RSC);
		ResourceManager.loadSound(GAME_MUSICSOUND_RSC);
		ResourceManager.loadSound(PLAYER_PISTOLSOUND_RSC);
		ResourceManager.loadSound(PLAYER_RIFLESOUND_RSC);
		ResourceManager.loadSound(PLAYER_SHOTGUNSOUND_RSC);
		ResourceManager.loadSound(PLAYER_BOMBSOUND_RSC);
		ResourceManager.loadSound(PLAYER_MINELAYSOUND_RSC);
		ResourceManager.loadSound(PLAYER_JUMPSOUND_RSC);

		ResourceManager.loadSound(PLAYER_MEDIUMMELEESOUND_RSC);
		ResourceManager.loadSound(PLAYER_HEAVYMELEESOUND_RSC);
		ResourceManager.loadSound(PLAYER_LIGHTMELEESOUND_RSC);

		//archie = new Archie(ScreenWidth/2, ScreenHeight/4);
		//shield = new Shield(archie.getCoarseGrainedMaxX() + 10, archie.getY());
	}
	
	public static void main(String[] args) {
		try {
			UCGame pClient = new UCGame("Oh, the conflict!", 1024, 576,55555, 55556, 5000);	
			
			app = new AppGameContainer(pClient);
			app.setDisplayMode(1024, 576, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

}