package uc;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import Server.mainServer;

import com.esotericsoftware.kryonet.Client;


public class ButtonState extends BasicGameState{

	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		UCGame uc = (UCGame) game;
		
		if((input.isKeyDown(Input.KEY_A) )){
			uc.enterState(UCGame.PLAYSTATE);

	//		mainServer main = new mainServer(55555, 55556);
	//		mainServer.createServerInterface();
	//		main.startServer();	
			uc.isServer =true;
			
			uc.client = new Client();
			uc.kryo = uc.client.getKryo();
			uc.registerKryoClasses();
			uc.connect("127.0.0.1");
			
//			client = new Client();
//			kryo = client.getKryo();
//			registerKryoClasses();
//			connect("127.0.0.1");
			

		}		
		else if((input.isKeyDown(Input.KEY_D) )){
			uc.enterState(UCGame.PLAYSTATE2);
	//		mainServer main = new mainServer(55555, 55556);
	//		mainServer.createServerInterface();
	//		main.startServer();	
			
			
			uc.client = new Client();
			uc.kryo = uc.client.getKryo();
			uc.registerKryoClasses();
			uc.connect("127.0.0.1");
		}	
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return UCGame.BUTTONSTATE;
	}

}
