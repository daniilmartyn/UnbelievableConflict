package uc;
import jig.ResourceManager;
import jig.Vector;
import NetworkClasses.LoginResponse;

//import NetworkClasses.CollideRequest;
import NetworkClasses.PacketAddPlayer;
import NetworkClasses.PacketRemovePlayer;
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
				UCGame.canenter = true;

			}
			else{
				Log.info("Login failed");
			}
		}

		if(object instanceof PacketAddPlayer){
			PacketAddPlayer packet = (PacketAddPlayer) object;
			Char newPlayer = new Char(packet.x,packet.y, packet.Char);
			newPlayer.id = packet.id;
			System.out.println(packet.x+" "+packet.y);
			UCGame.players.put(packet.id, newPlayer);

			for(Char Player : UCGame.players.values()){
				System.out.println(Player.id);

			}
		}

		if(UCGame.set){

			if(object instanceof PacketRemovePlayer){
				PacketRemovePlayer packet = (PacketRemovePlayer) object;
				UCGame.players.remove(packet.id);
			}
			else if(object instanceof SetXY){
				SetXY packet = (SetXY) object;


				if(UCGame.players.get(packet.id).weapon.hit != null && UCGame.players.get(packet.id).weapon.hit.isStopped()){
					UCGame.players.get(packet.id).weapon.removeAnimation(UCGame.players.get(packet.id).weapon.hit);
					UCGame.players.get(packet.id).weapon.hit = null;
					UCGame.players.get(packet.id).weapon.weapon = null;
					UCGame.players.get(packet.id).weapon.changeWeapon(packet.weapon);
				}
				
				else if(UCGame.players.get(packet.id).weapon.hit == null){			
					
					UCGame.players.get(packet.id).weapon.direction = packet.wepDirection;
					UCGame.players.get(packet.id).weapon.changeWepDir();
				}

				if(packet.fired){
					UCGame.players.get(packet.id).readytofire = true;
				}
				if(packet.kaboom){
					ResourceManager.getSound(UCGame.PLAYER_KABOOMSOUND_RSC).play();
				}
				if(packet.hurt){
					if(UCGame.players.get(packet.id).playingCharacter == 2){
						ResourceManager.getSound(UCGame.PLAYER_HEAVYHURTSOUND_RSC).play();
					}
					else{
						ResourceManager.getSound(UCGame.PLAYER_LIGHTHURTSOUND_RSC).play();
					}
				}
				if(UCGame.players.get(packet.id).weapon.select != (packet.weapon)){
					UCGame.players.get(packet.id).weapon.changeWeapon(packet.weapon);
					UCGame.players.get(packet.id).weapon.select =(packet.weapon);
				}

				UCGame.players.get(packet.id).weapon.setRotation(packet.rotate);
				
				if(packet.wepDirection == 0)
					UCGame.players.get(packet.id).weapon.angle = (packet.rotate);
				else
					UCGame.players.get(packet.id).weapon.angle = (packet.rotate + 180) ;
				
				UCGame.players.get(packet.id).setHealth(packet.health);
				UCGame.players.get(packet.id).primaryAmmo = packet.priAmmo;
				UCGame.players.get(packet.id).secondaryAmmo = packet.secAmmo;
				UCGame.players.get(packet.id).setDeaths(packet.deaths);
				UCGame.players.get(packet.id).setKills(packet.kills);
				
				UCGame.players.get(packet.id).setX(packet.x);
				UCGame.players.get(packet.id).setY(packet.y);
				UCGame.players.get(packet.id).weapon.setX(packet.x1);
				UCGame.players.get(packet.id).weapon.setY(packet.y1);
				UCGame.players.get(packet.id).setJump(packet.jumped);

				if(packet.justjumped){
					ResourceManager.getSound(UCGame.PLAYER_JUMPSOUND_RSC).play();

				}

				UCGame.players.get(packet.id).changeDir(packet.run);
				UCGame.players.get(packet.id).state= (packet.state);
				UCGame.players.get(packet.id).changeImg();
			}		
			else if(object instanceof UpdateChar){
				UpdateChar packet = (UpdateChar) object;

				UCGame.players.get(packet.id).weapon.select =((packet.switchWep  ));
				UCGame.players.get(packet.id).weapon.changeWeapon(packet.switchWep);

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
					ResourceManager.getSound(UCGame.PLAYER_JUMPSOUND_RSC).play();
				}
				if(packet.fire){
					//	UCGame.players.get(packet.id).weapon.angle = packet.angle;
					//	UCGame.players.get(packet.id).weapon.direction = packet.direction;
					///	UCGame.players.get(packet.id).weapon.select = packet.select;

					//				if(packet.select == 2){
					//					if(UCGame.players.get(packet.id).weapon.hit != null){
					//						UCGame.players.get(packet.id).weapon.removeImage(UCGame.players.get(packet.id).weapon.weapon);
					//						System.out.println("got her");
					//						UCGame.players.get(packet.id).weapon.fire(UCGame.players.get(packet.id));		
					//					}
					//				}
					//				else{
					UCGame.players.get(packet.id).readytofire = true;		
					//				}
				}
			}
			else if(object instanceof UpdateBullet){
				UpdateBullet packet = (UpdateBullet) object;
				UCGame.bullets.clear();
				for(int i =0; i < packet.bulletx.size(); i++){
					UCGame.bullets.add(new Bullet(packet.bulletx.get(i),
							packet.bullety.get(i), new Vector(0,0)));
					UCGame.bullets.get(i).rotate(packet.rot.get(i));
				}

				UCGame.mines.clear();
				for(int i =0; i < packet.minex.size(); i++){
					UCGame.mines.add(new Mine(packet.minex.get(i),
							packet.miney.get(i), new Vector(0,0)));
				}

				UCGame.bombs.clear();
				for(int i =0; i < packet.bombx.size(); i++){
					UCGame.bombs.add(new Bomb(packet.bombx.get(i),
							packet.bomby.get(i), new Vector(0,0)));
				}

				UCGame.grenades.clear();
				for(int i =0; i < packet.grenadex.size(); i++){
					UCGame.grenades.add(new Grenade(packet.grenadex.get(i),
							packet.grenadey.get(i), new Vector(0,0)));
				}

				UCGame.items.clear();
				
				for(int i =0; i < packet.itemtype.size(); i++){
					if(packet.itemtype.get(i) == 0)
						UCGame.items.add(new Item(1909f, 760f, 0));
					if(packet.itemtype.get(i) == 1)
						UCGame.items.add(new Item(1682f, 883f, 1));
					if(packet.itemtype.get(i) == 2)
						UCGame.items.add(new Item(1027f, 665f, 2));
				}
				
			}
		}
	}
}
	

