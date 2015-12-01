package Server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.Position;

import Client.playerChar;
import Client.BounceGameClient;
import NetworkClasses.LoginRequest;
import NetworkClasses.LoginResponse;
import NetworkClasses.Message;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.minlog.Log;

public class mainServer {
	
	private int tcpPort;
	private int udpPort;
	public static Server server;
	private Kryo kryo;
	
	static JFrame jFrame;
	static JTextArea jTextArea;
	static mainServerListener listener = new mainServerListener();
		
	public mainServer(int tcpPort, int udpPort){		
		this.tcpPort = tcpPort;
		this.udpPort = udpPort;
		server = new Server();
		
		kryo = server.getKryo();
		registerKryoClasses();
		
	}
	
	public void startServer(){
		Log.info("Starting Server");
		jTextArea.append("Starting Server..");
		jTextArea.append("\n");
		server.start();
		try {
			server.bind(tcpPort, udpPort);
			server.addListener(listener);
			Log.info("Server online.");
			jTextArea.append("Server online! \n");
			jTextArea.append("----------------------------");
			jTextArea.append("\n");
			update();
		} catch (IOException e) {
			Log.info("Port already used");
			jTextArea.append("Port already in use.");
			jTextArea.append("\n");
			e.printStackTrace();
		}
	}
	
	public static void stopServer(){
		Log.info("Server stopped");
		jTextArea.append("Server stopped.");
		jTextArea.append("\n");
		server.stop();
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
	
	public void update( ){
		while(true){
			
		}
	}
	
	public static void createServerInterface(){
		jFrame = new JFrame("GameServerInterface");
		jTextArea = new JTextArea();
		jTextArea.append("\n");
		
	    jTextArea.setLineWrap(true);
	    jTextArea.setEditable(false);
	    
		jFrame.add( jTextArea );	
		
		jFrame.setSize(400, 600);
		jFrame.setVisible(true);
		
		jFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
                int i=JOptionPane.showConfirmDialog(null, "You want to shut down the Server?");
                if(i==0)
                	
                	stopServer();
                    System.exit(0);
            }
		});
		
		JScrollPane scrollPane = new JScrollPane(jTextArea);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		jFrame.add(scrollPane);		

	}
	

	public static void main(String[] args) {
	Log.set(Log.LEVEL_INFO);
	
	mainServer main = new mainServer(55555, 55556);
	createServerInterface();
	main.startServer();		
	}
}
