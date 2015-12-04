package Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTextArea;

import org.newdawn.slick.state.StateBasedGame;

import uc.UCGame;
import uc.playerChar;
import uc.playerChar;
import NetworkClasses.IExist;
import NetworkClasses.LoginRequest;
import NetworkClasses.LoginResponse;
import NetworkClasses.NewPlayerRequest;
import NetworkClasses.NewPlayerRequest;
import NetworkClasses.SetXY;

import NetworkClasses.PacketAddPlayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class mainServerListener extends Listener  {
	
	public static Map<Integer, playerChar> players = new HashMap<Integer, playerChar>();
	
	List<playerChar> laserList = new ArrayList<playerChar>();

	public void connected(Connection connection){
		playerChar player = new playerChar(800,800);
		player.c = connection;
		
		PacketAddPlayer addPacket = new PacketAddPlayer();
		addPacket.x = 800;
		addPacket.y = 800;
		addPacket.id = connection.getID();
		mainServer.server.sendToAllExceptTCP(connection.getID(), addPacket);
//		for(playerChar p : players.values()){
//			PacketAddPlayer addPacket2 = new PacketAddPlayer();
//			addPacket2.x = p.x;
//			addPacket2.y = p.y;
//			addPacket2.id = p.c.getID();
//			connection.sendTCP(addPacket2);
//		}
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
			//System.out.println("sent back");

			connection.sendTCP(response);
		}
		
		if(object instanceof NetworkClasses.PacketUpdateX){
			NetworkClasses.PacketUpdateX packet = (NetworkClasses.PacketUpdateX) object;
//			if((players.get(connection.getID()).x + packet.x <= 725 )&& 
//					(players.get(connection.getID()).x + packet.x > 0)){
//				players.get(connection.getID()).x += packet.x;			
				
//				mainServer.ServerGame.players2.get(packet.id).x += packet.x;
//				mainServer.ServerGame.players2.get(packet.id).translate(packet.x, 0);
		//	System.out.println("sent back");

				//packet.id = connection.getID();
				mainServer.server.sendToUDP( 1,packet);
		//	}

//			System.out.println("received and sent an update X packet");
			
		}else if(object instanceof NetworkClasses.PacketUpdateY){
			NetworkClasses.PacketUpdateY packet = (NetworkClasses.PacketUpdateY) object;
//			if((players.get(connection.getID()).y + packet.y <= 525 )&& 
//					(players.get(connection.getID()).y + packet.y > 0)){
//				players.get(connection.getID()).y += packet.y;
				
//				mainServer.ServerGame.players2.get(packet.id).y += packet.y;
//				mainServer.ServerGame.players2.get(packet.id).translate(0,packet.y);
				
				//packet.id = connection.getID();
				mainServer.server.sendToUDP(1,packet);
		//	}	
		}
		
		else if(object instanceof NetworkClasses.SetXY){
			NetworkClasses.SetXY packet = (NetworkClasses.SetXY) object;
			mainServer.server.sendToAllExceptUDP(1,packet);
		}
		else if(object instanceof NetworkClasses.FiredGun){
			NetworkClasses.FiredGun packet = (NetworkClasses.FiredGun) object;
			mainServer.server.sendToUDP(1,packet);
		}
		else if(object instanceof NetworkClasses.UpdateChar){
			NetworkClasses.UpdateChar packet = (NetworkClasses.UpdateChar) object;
			mainServer.server.sendToUDP(1,packet);
		}
		else if(object instanceof NetworkClasses.MouseMoved){
			NetworkClasses.MouseMoved packet = (NetworkClasses.MouseMoved) object;
			mainServer.server.sendToUDP(1,packet);
		}
		else if(object instanceof NetworkClasses.CreatePellet){
			NetworkClasses.CreatePellet packet = (NetworkClasses.CreatePellet) object;
							
				packet.id = connection.getID();
				mainServer.server.sendToAllUDP( packet);
		}
		
		else if(object instanceof NetworkClasses.HitRequest){
			NetworkClasses.HitRequest packet = (NetworkClasses.HitRequest) object;
							
				packet.id = connection.getID();
				mainServer.server.sendToAllUDP( packet);
		}
		else if(object instanceof NetworkClasses.NewPlayerRequest){
			
			
			for(playerChar p : players.values()){
				if(p.c.getID() == connection.getID()){
					
				}
				else{
					PacketAddPlayer addPacket2 = new PacketAddPlayer();
					addPacket2.x = p.x;
					addPacket2.y = p.y;
					addPacket2.id = p.c.getID();
					connection.sendTCP(addPacket2);
				}
			}
		}
		else if(object instanceof NetworkClasses.IExist){
			
			IExist addPacket = new IExist();
			addPacket.id = connection.getID();
			mainServer.server.sendToAllExceptTCP(connection.getID(), addPacket);

		}
	}
	

			
}
			
			


	

