package Client;

import java.util.ArrayList;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jig.Entity;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import NetworkClasses.LoginRequest;
import NetworkClasses.LoginResponse;
import NetworkClasses.Message;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

public class BounceGameClient extends StateBasedGame {

	public static final int PLAYINGSTATE = 0;
	
	public static final String BALL_BALLIMG_RSC = "bounce/resource/ball.png";
	public static final String BALL_PELLETIMG_RSC = "bounce/resource/Block.jpg";
	public static final String BALL_BULLETIMG_RSC = "bounce/resource/bullet.png";

	public final int ScreenWidth;
	public final int ScreenHeight;
	public static int NumberLives;
	
	private int tcpPort;
	private int udpPort;
	private int timeout;	
	public static AppGameContainer app;
	public static Input inputHandler;
	
	public static Client client;
	private Kryo kryo;

	static int id;
	Ball ball;
	
	public static boolean started = false;
	
	public static  movingChar player;
	//static Map<Integer,MPPlayer> players = new HashMap<Integer,MPPlayer>(); 
	public static Map<Integer,movingChar> players2 = new HashMap<Integer,movingChar>(); 
	
	public static List<Pellet> myPellet = new ArrayList<Pellet>();
	public static List<Pellet> otherPellet = new ArrayList<Pellet>();
	
	public final static List<Bullet> bullet = new ArrayList<Bullet>();


	public BounceGameClient(String title, int width, int height, int lives,int tcpPort, int udpPort, int timeout) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
		NumberLives = lives;
			this.tcpPort = tcpPort;
			this.udpPort = udpPort;
			this.timeout = timeout;
			
			client = new Client();
			kryo = client.getKryo();
			registerKryoClasses();
			connect("127.0.0.1");	
		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
		
				
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
	
	private void registerKryoClasses(){
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
		kryo.register(NetworkClasses.UpdatePellet.class);
		kryo.register(NetworkClasses.HitRequest.class);

	}

	public void initStatesList(GameContainer container) throws SlickException {
		ResourceManager.loadImage(BALL_BALLIMG_RSC);
		ResourceManager.loadImage(BALL_PELLETIMG_RSC);
		ResourceManager.loadImage(BALL_BULLETIMG_RSC);
		
		addState(new PlayingState());
		player = new movingChar(200,200);
		player.id = id;
		players2.put(id, player);
		ball = new Ball(200,200,0,0);

			}
	
	
	public static void main(String[] args) {
		try {
			Log.set(Log.LEVEL_INFO);
			org.newdawn.slick.util.Log.setVerbose(false);
		
			BounceGameClient pClient = new BounceGameClient("Bounce!", 800, 600, 5,55555, 55556, 5000);	

			if(client.isConnected()){
				app = new AppGameContainer(pClient);
				app.setUpdateOnlyWhenVisible(false);
				app.setTargetFrameRate(60);
				app.setDisplayMode(800, 600, false);
				app.setVSync(true);
				app.start();  
			}
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
