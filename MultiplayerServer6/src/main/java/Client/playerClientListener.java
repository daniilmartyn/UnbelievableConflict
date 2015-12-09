package Client;




import jig.ResourceManager;
import jig.Vector;
import NetworkClasses.LoginResponse;



import NetworkClasses.CollideRequest;
import NetworkClasses.CreatePellet;
import NetworkClasses.HitRequest;
import NetworkClasses.PacketAddPlayer;
import NetworkClasses.PacketRemovePlayer;
import NetworkClasses.PacketUpdateX;
import NetworkClasses.PacketUpdateY;
import NetworkClasses.UpdatePellet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class playerClientListener extends Listener {
					
	public void received(Connection connection, Object object){

		if(object instanceof LoginResponse){
			LoginResponse response = (LoginResponse) object;
			
			
			if(response.responseText.equalsIgnoreCase("ok")){
				Log.info("Login ok");
				BounceGameClient.id = response.id;
			}
			else{
				Log.info("Login failed");
			}
		}
		
		if(object instanceof PacketAddPlayer){
			PacketAddPlayer packet = (PacketAddPlayer) object;
			//MPPlayer newPlayer = new MPPlayer();
			//BounceGameClient.players.put(packet.id, newPlayer);
			movingChar newPlayer2 = new movingChar(packet.x,packet.y);
			newPlayer2.id = packet.id;
			System.out.println(packet.x+" "+packet.y);
			BounceGameClient.players2.put(packet.id, newPlayer2);
			
		}else if(object instanceof PacketRemovePlayer){
			PacketRemovePlayer packet = (PacketRemovePlayer) object;
			BounceGameClient.players2.remove(packet.id);
			
		}else if(object instanceof PacketUpdateX){
			PacketUpdateX packet = (PacketUpdateX) object;
			BounceGameClient.players2.get(packet.id).x += packet.x;
			BounceGameClient.players2.get(packet.id).translate(packet.x, 0);
			
		}else if(object instanceof PacketUpdateY){
			PacketUpdateY packet = (PacketUpdateY) object;
			BounceGameClient.players2.get(packet.id).y += packet.y;
			BounceGameClient.players2.get(packet.id).translate(0, packet.y);
		}
		else if(object instanceof CollideRequest){
			CollideRequest packet = (CollideRequest) object;
			
			BounceGameClient.players2.get(packet.id).setPosition(300, 300);
			BounceGameClient.players2.get(packet.id2).points =- 1;

			BounceGameClient.players2.get(packet.id).setPosition(100, 100);
			BounceGameClient.players2.get(packet.id2).points += 1;
		}
		else if(object instanceof CreatePellet){
			CreatePellet packet = (CreatePellet) object;
			
			//if(packet.id == BounceGameClient.id){
				BounceGameClient.bullet.add(new Bullet(packet.x, packet.y, new Vector(packet.vx, packet.vy)));
			//}
			//else{
			//	BounceGameClient.otherPellet.add(new Pellet(packet.x, packet.y, packet.vx ,packet.vy));
			//}

		}
		else if(object instanceof UpdatePellet){
			UpdatePellet packet = (UpdatePellet) object;
			
			if(packet.id == BounceGameClient.id){
				//BounceGameClient.myPellet.get(packet.numPel).translate(5, 0);
				BounceGameClient.myPellet.get(packet.numPel).update(packet.delta);
			}
			else{
				BounceGameClient.otherPellet.get(packet.numPel).update(packet.delta);
			}

		}
		else if(object instanceof HitRequest){
			HitRequest packet = (HitRequest) object;
			
			BounceGameClient.players2.get(packet.id).points--;
			
			if(packet.id == BounceGameClient.id){
				BounceGameClient.myPellet.remove(packet.numPel);
			}
			else{
				BounceGameClient.otherPellet.remove(packet.numPel);
			}

		}
		
		
	}


}
	

