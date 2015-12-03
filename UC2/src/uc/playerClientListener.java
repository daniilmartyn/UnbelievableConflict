package uc;
import jig.ResourceManager;
import jig.Vector;
import NetworkClasses.LoginResponse;

import NetworkClasses.CollideRequest;
import NetworkClasses.CreatePellet;
import NetworkClasses.HitRequest;
import NetworkClasses.IExist;
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
				UCGame.id = response.id;
				

			}
			else{
				Log.info("Login failed");
			}
		}
		
		if(object instanceof PacketAddPlayer){
			PacketAddPlayer packet = (PacketAddPlayer) object;
			//MPPlayer newPlayer = new MPPlayer();
			//BounceGameClient.players.put(packet.id, newPlayer);
			Char newPlayer = new Char(packet.x,packet.y);
			newPlayer.id = packet.id;
			System.out.println(packet.x+" "+packet.y);
			UCGame.players.put(packet.id, newPlayer);
			
			for(Char Player : UCGame.players.values()){
				System.out.println(Player.id);
				
			}
		}
		
		if(UCGame.set){
			
		
		if(object instanceof IExist){
			IExist packet = (IExist) object;
			//MPPlayer newPlayer = new MPPlayer();
			//BounceGameClient.players.put(packet.id, newPlayer);
			Char newPlayer = new Char(800,1000);
			newPlayer.id = packet.id;
			UCGame.players.put(packet.id, newPlayer);
			
			for(Char Player : UCGame.players.values()){
				System.out.println(Player.id);
				
			}
			
		}
		//else 
//			if(object instanceof PacketRemovePlayer){
//			PacketRemovePlayer packet = (PacketRemovePlayer) object;
//			BounceGameClient.players2.remove(packet.id);
//			
//		}
		else
			if(object instanceof PacketUpdateX){
			PacketUpdateX packet = (PacketUpdateX) object;
			//System.out.println("recived");

			if(packet.runDir >3){
				
			}
			else{
	//			UCGame.players.get(packet.id)PlayState.dude.changeRunDir(packet.runDir);			// set run velocity to 0.0

				UCGame.players.get(packet.id).changeRunDir(packet.runDir);			// set run velocity to 0.0
			}
			//PlayState.dude.setState(packet.state);
			UCGame.players.get(packet.id).setState(packet.state);
		}else if(object instanceof PacketUpdateY){
			PacketUpdateY packet = (PacketUpdateY) object;
			
//			PlayState.dude.jump();
//			PlayState.dude.setJump(true);
//			PlayState.dude.setState(packet.state);
	
			UCGame.players.get(packet.id).jump();
			UCGame.players.get(packet.id).setJump(true);
			UCGame.players.get(packet.id).setState(packet.state);
		}
//		else if(object instanceof CollideRequest){
//			CollideRequest packet = (CollideRequest) object;
//			
//			UCGame.players2.get(packet.id).setPosition(300, 300);
//			UCGame.players2.get(packet.id2).points =- 1;
//
//			UCGame.players2.get(packet.id).setPosition(100, 100);
//			UCGame.players2.get(packet.id2).points += 1;
//		}
//		else if(object instanceof CreatePellet){
//			CreatePellet packet = (CreatePellet) object;
//			
//			//if(packet.id == BounceGameClient.id){
//				BounceGameClient.bullet.add(new Bullet(packet.x, packet.y, new Vector(packet.vx, packet.vy)));
//			//}
//			//else{
//			//	BounceGameClient.otherPellet.add(new Pellet(packet.x, packet.y, packet.vx ,packet.vy));
//			//}
//
//		}
//		else if(object instanceof UpdatePellet){
//			UpdatePellet packet = (UpdatePellet) object;
//			
//			if(packet.id == BounceGameClient.id){
//				//BounceGameClient.myPellet.get(packet.numPel).translate(5, 0);
//				BounceGameClient.myPellet.get(packet.numPel).update(packet.delta);
//			}
//			else{
//				BounceGameClient.otherPellet.get(packet.numPel).update(packet.delta);
//			}
//
//		}
//		else if(object instanceof HitRequest){
//			HitRequest packet = (HitRequest) object;
//			
//			BounceGameClient.players2.get(packet.id).points--;
//			
//			if(packet.id == BounceGameClient.id){
//				BounceGameClient.myPellet.remove(packet.numPel);
//			}
//			else{
//				BounceGameClient.otherPellet.remove(packet.numPel);
//			}
//
//		}
		
		}
	}


}
	

