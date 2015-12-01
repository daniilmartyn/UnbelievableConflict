package Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTextArea;

import org.newdawn.slick.state.StateBasedGame;

import Client.BounceGameClient;
import Client.movingChar;
import Client.playerChar;
import NetworkClasses.LoginRequest;
import NetworkClasses.LoginResponse;

import NetworkClasses.PacketAddPlayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class mainServerListener extends Listener  {
	
	public static Map<Integer, playerChar> players = new HashMap<Integer, playerChar>();
	
	List<playerChar> laserList = new ArrayList<playerChar>();

	public void connected(Connection connection){
		playerChar player = new playerChar(200,200);
		player.c = connection;
		
		PacketAddPlayer addPacket = new PacketAddPlayer();
		addPacket.x = 200;
		addPacket.y = 200;
		addPacket.id = connection.getID();
		mainServer.server.sendToAllExceptTCP(connection.getID(), addPacket);
		
		for(playerChar p : players.values()){
			PacketAddPlayer addPacket2 = new PacketAddPlayer();
			addPacket2.x = p.x;
			addPacket2.y = p.y;
			addPacket2.id = p.c.getID();
			connection.sendTCP(addPacket2);
		}
//		mainServer.ServerGame.players2.put(connection.getID(), new movingChar(200, 200)) ;
		players.put(connection.getID(), player);
		mainServer.jTextArea.append(connection.getID() + " (ID) joined the server (Player.enity)");
		mainServer.jTextArea.append("\n");
	}
	
	public void disconnected(Connection connection){
		players.remove(connection.getID());
//		mainServer.ServerGame.players2.remove(connection.getID()) ;
		NetworkClasses.PacketRemovePlayer removePacket = new NetworkClasses.PacketRemovePlayer(); 
		removePacket.id = connection.getID();
		mainServer.server.sendToAllExceptTCP(connection.getID(), removePacket);
		mainServer.jTextArea.append(connection.getID() + " (ID) left the server (Player.enity)");
		mainServer.jTextArea.append("\n");
		mainServer.jTextArea.append("\n");
	}
	
	public void received(Connection connection, Object object){
		if(object instanceof LoginRequest){		
			
			LoginRequest request = (LoginRequest) object;
			LoginResponse response = new LoginResponse();
			response.id=connection.getID();
			if(request.getUserName().equalsIgnoreCase("raLa") && request.getUserPassword().equalsIgnoreCase("test")){
				response.setResponseText("ok");
				mainServer.jTextArea.append(connection.getRemoteAddressTCP() + " connected.");
				mainServer.jTextArea.append("\n");
				mainServer.jTextArea.append("\n");
				
			}
			else{
				response.setResponseText("no");
				mainServer.jTextArea.append(connection.getRemoteAddressTCP() + " connected, but with invalid userdata");
				mainServer.jTextArea.append("\n");
				mainServer.jTextArea.append("\n");
				
			}
			
			connection.sendTCP(response);
		}
		
		if(object instanceof NetworkClasses.PacketUpdateX){
			NetworkClasses.PacketUpdateX packet = (NetworkClasses.PacketUpdateX) object;
			if((players.get(connection.getID()).x + packet.x <= 725 )&& 
					(players.get(connection.getID()).x + packet.x > 0)){
				players.get(connection.getID()).x += packet.x;			
				
//				mainServer.ServerGame.players2.get(packet.id).x += packet.x;
//				mainServer.ServerGame.players2.get(packet.id).translate(packet.x, 0);
				
				packet.id = connection.getID();
				mainServer.server.sendToAllUDP( packet);
			}

//			System.out.println("received and sent an update X packet");
			
		}else if(object instanceof NetworkClasses.PacketUpdateY){
			NetworkClasses.PacketUpdateY packet = (NetworkClasses.PacketUpdateY) object;
			if((players.get(connection.getID()).y + packet.y <= 525 )&& 
					(players.get(connection.getID()).y + packet.y > 0)){
				players.get(connection.getID()).y += packet.y;
				
//				mainServer.ServerGame.players2.get(packet.id).y += packet.y;
//				mainServer.ServerGame.players2.get(packet.id).translate(0,packet.y);
				
				packet.id = connection.getID();
				mainServer.server.sendToAllUDP(packet);
			}	
		}
		
		if(object instanceof NetworkClasses.CreatePellet){
			NetworkClasses.CreatePellet packet = (NetworkClasses.CreatePellet) object;
							
				packet.id = connection.getID();
				mainServer.server.sendToAllUDP( packet);
		}
		
		if(object instanceof NetworkClasses.UpdatePellet){
			NetworkClasses.UpdatePellet packet = (NetworkClasses.UpdatePellet) object;
							
				packet.id = connection.getID();
				mainServer.server.sendToAllUDP( packet);
		}
		if(object instanceof NetworkClasses.HitRequest){
			NetworkClasses.HitRequest packet = (NetworkClasses.HitRequest) object;
							
				packet.id = connection.getID();
				mainServer.server.sendToAllUDP( packet);
		}
		
		
	}
	

			
}
			
			


	

