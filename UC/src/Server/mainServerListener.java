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

		mainServer.jTextArea.append(connection.getID() + " (ID) joined the server (Player.enity)");
		mainServer.jTextArea.append("\n");
	}
	
	public void disconnected(Connection connection){
		players.remove(connection.getID());
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
		
		if(object instanceof NetworkClasses.SetXY){
			NetworkClasses.SetXY packet = (NetworkClasses.SetXY) object;
			mainServer.server.sendToAllExceptUDP(1,packet);
		}
		else if(object instanceof NetworkClasses.UpdateBullet){
			NetworkClasses.UpdateBullet packet = (NetworkClasses.UpdateBullet) object;
			mainServer.server.sendToAllExceptTCP(1,packet);
		}
		else if(object instanceof NetworkClasses.UpdateChar){
			NetworkClasses.UpdateChar packet = (NetworkClasses.UpdateChar) object;
			mainServer.server.sendToUDP(1,packet);
		}
		else if(object instanceof NetworkClasses.NewPlayerRequest){
			NetworkClasses.NewPlayerRequest packet = (NetworkClasses.NewPlayerRequest) object;
			playerChar player = new playerChar(packet.x,packet.y);
			player.c = connection;
			player.Char = packet.player;
			players.put(connection.getID(), player);

			for(playerChar p : players.values()){
				if(p.c.getID() == connection.getID()){
				}
				else{
					PacketAddPlayer addPacket2 = new PacketAddPlayer();
					addPacket2.x = p.x;
					addPacket2.y = p.y;
					addPacket2.Char = p.Char;
					addPacket2.id = p.c.getID();
					connection.sendTCP(addPacket2);
				}
			}
			PacketAddPlayer addPacket2 = new PacketAddPlayer();
			addPacket2.x = packet.x;
			addPacket2.y = packet.y;
			addPacket2.Char = packet.player;
			addPacket2.id = connection.getID();
			mainServer.server.sendToAllExceptTCP(connection.getID(),addPacket2);
		}
	}
}