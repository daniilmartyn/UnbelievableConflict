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

import uc.playerChar;
import uc.playerClientListener;
import NetworkClasses.LoginRequest;
import NetworkClasses.LoginResponse;
import NetworkClasses.Message;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

public class UCGame extends StateBasedGame{
	
	public static final int PLAYSTATE = 1;
	public static final int PLAYSTATE2 = 2;
	public static final int BUTTONSTATE = 0;

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
	public final static List<Bullet> bullet = new ArrayList<Bullet>();

	
	boolean sound = true;

	public UCGame(String name, int width, int height,int tcpPort, int udpPort, int timeout) {
		super(name);
		ScreenWidth = width;
		ScreenHeight = height;
		
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		this.timeout = timeout;
		
//		client = new Client();
//		kryo = client.getKryo();
//		registerKryoClasses();
//		connect("127.0.0.1");
		
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
		kryo.register(NetworkClasses.PacketUpdateX.class);
		kryo.register(NetworkClasses.PacketUpdateY.class);
		kryo.register(NetworkClasses.PacketAddPlayer.class);
		kryo.register(NetworkClasses.PacketRemovePlayer.class);
		kryo.register(NetworkClasses.CollideRequest.class);
		kryo.register(NetworkClasses.CreatePellet.class);
		kryo.register(NetworkClasses.HitRequest.class);
		kryo.register(NetworkClasses.NewPlayerRequest.class);
		kryo.register(NetworkClasses.IExist.class);
		kryo.register(NetworkClasses.SetXY.class);
		kryo.register(NetworkClasses.FiredGun.class);
		kryo.register(NetworkClasses.MouseMoved.class);
		kryo.register(NetworkClasses.UpdateChar.class);
		kryo.register(NetworkClasses.UpdateBullet.class);
		kryo.register(ArrayList.class);

	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new ButtonState());	
		addState(new PlayState2());		
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
		try {
			UCGame pClient = new UCGame("Oh, the conflict!", 1024, 576,55555, 55556, 5000);	
			
			//if(client.isConnected()){
				app = new AppGameContainer(pClient);
				app.setDisplayMode(1024, 576, false);
				app.setVSync(true);
				app.start();
			//}
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

}
