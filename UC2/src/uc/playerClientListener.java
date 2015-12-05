package uc;
import jig.ResourceManager;
import jig.Vector;
import NetworkClasses.LoginResponse;

import NetworkClasses.CollideRequest;
import NetworkClasses.CreatePellet;
import NetworkClasses.FiredGun;
import NetworkClasses.HitRequest;
import NetworkClasses.IExist;
import NetworkClasses.MouseMoved;
import NetworkClasses.PacketAddPlayer;
import NetworkClasses.PacketRemovePlayer;
import NetworkClasses.PacketUpdateX;
import NetworkClasses.PacketUpdateY;
import NetworkClasses.SetXY;
import NetworkClasses.UpdateBullet;
import NetworkClasses.UpdateChar;
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
		else 
			if(object instanceof PacketRemovePlayer){
			PacketRemovePlayer packet = (PacketRemovePlayer) object;
			UCGame.players.remove(packet.id);
		}
		else
			if(object instanceof PacketUpdateX){
			PacketUpdateX packet = (PacketUpdateX) object;
				if(packet.runDir >3){
				}
			else{
				UCGame.players.get(packet.id).changeRunDir(packet.runDir);			// set run velocity to 0.0
			}
			UCGame.players.get(packet.id).setState(packet.state);
		}else if(object instanceof PacketUpdateY){
			PacketUpdateY packet = (PacketUpdateY) object;
	
			UCGame.players.get(packet.id).jump();
			UCGame.players.get(packet.id).setJump(true);
			UCGame.players.get(packet.id).setState(packet.state);
		}
		else if(object instanceof SetXY){
			SetXY packet = (SetXY) object;
			
//			if(packet.run == 0){
//				UCGame.players.get(packet.id).weapon.setRotation(packet.rotate);
//
//			}
//			else{
//				UCGame.players.get(packet.id).weapon.setRotation(packet.rotate);
//				//UCGame.players.get(packet.id).weapon.;
//
//			}
			UCGame.players.get(packet.id).weapon.setRotation(packet.rotate);
			UCGame.players.get(packet.id).weapon.angle = (packet.rotate);
			UCGame.players.get(packet.id).setX(packet.x);
			UCGame.players.get(packet.id).setY(packet.y);
			UCGame.players.get(packet.id).weapon.setX(packet.x);
			UCGame.players.get(packet.id).weapon.setY(packet.y);
			UCGame.players.get(packet.id).setJump(packet.jumped);
			
			UCGame.players.get(packet.id).changeDir(packet.run);
			UCGame.players.get(packet.id).state= (packet.state);
			UCGame.players.get(packet.id).changeImg();
		}		
		else if(object instanceof UpdateChar){
			UpdateChar packet = (UpdateChar) object;
			
			UCGame.players.get(packet.id).camX =((packet.camx));
			UCGame.players.get(packet.id).camY =((packet.camy));

			UCGame.players.get(packet.id).changeDir((packet.changeDir));
			UCGame.players.get(packet.id).weapon.mouse = (new Vector(packet.mousex,packet.mousey));

			if(packet.runDir >3){
			}
			else{
				UCGame.players.get(packet.id).changeRunDir(packet.runDir);			// set run velocity to 0.0
			}
			
			UCGame.players.get(packet.id).setState(packet.state);
	
			if(packet.jump){
				UCGame.players.get(packet.id).jump();
				UCGame.players.get(packet.id).setJump(true);
				UCGame.players.get(packet.id).setState(packet.state);
			}
			if(packet.fire){
			//	UCGame.players.get(packet.id).weapon.angle = packet.angle;
			//	UCGame.players.get(packet.id).weapon.direction = packet.direction;
			///	UCGame.players.get(packet.id).weapon.select = packet.select;
				UCGame.players.get(packet.id).weapon.fire(UCGame.players.get(packet.id));		
			}

		}
		else if(object instanceof UpdateBullet){
			UpdateBullet packet = (UpdateBullet) object;
	
//			UCGame.bullet.clear();
//			for(int i =0; i < packet.bullet.size(); i++){
//				UCGame.bullet.add(new Bullet(packet.bullet.get(i).x,
//						packet.bullet.get(i).x,packet.bullet.get(i).vector));
//			}
			
			UCGame.bullet.clear();
			for(int i =0; i < packet.bulletx.size(); i++){
				UCGame.bullet.add(new Bullet(packet.bulletx.get(i),
						packet.bullety.get(i), new Vector(0,0)));
				UCGame.bullet.get(i).rotate(packet.rot.get(i));
			}
			
		}
		else if(object instanceof FiredGun){
			FiredGun packet = (FiredGun) object;
	
			UCGame.players.get(packet.id).weapon.angle = packet.angle;
			UCGame.players.get(packet.id).weapon.direction = packet.direction;
			UCGame.players.get(packet.id).weapon.select = packet.select;
			UCGame.players.get(packet.id).weapon.fire(UCGame.players.get(packet.id));
		}
		else if(object instanceof MouseMoved){
			MouseMoved packet = (MouseMoved) object;
	
			UCGame.players.get(packet.id).weapon.mouse = packet.mouse;
		}
	}
	}

}
	

