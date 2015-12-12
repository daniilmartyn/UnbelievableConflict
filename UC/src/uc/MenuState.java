package uc;

//import java.awt.Font;

import jig.ResourceManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.esotericsoftware.kryonet.Client;

public class MenuState extends BasicGameState {

	private int menu; // which menu screen.    0 for main menu
											// 1 for join game
											// 2 for host game
											// 3 for help
	private int help; // which help screen	   0 for controls
											// 1 for class info
											// 2 for objects/entities
	
	private int timeLim;		// 0 for 1 min,    1 for 5 min,     2 for 10 min
	private int maxPlayer;		// 0 for 2 player, 1 for 3 player,  2 for 4 player
	private int scoreLim;		// 0 for 10 pts,   1 for 20 pts,    2 for 30 pts
	
	private Image arrow;
	
	private TextField name;
	private TextField address;
	private Font font;
	
	
	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		menu = 0;
		help = 0;
		
		timeLim = 1;
		maxPlayer = 0;
		scoreLim = 0;
		
		font =  new TrueTypeFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN , 16), false);
				
		name = new TextField(container, font, 475, 140, 200, 25);
		name.setBackgroundColor(Color.darkGray);
		name.setTextColor(Color.white);
		name.setCursorVisible(true);
		name.setBorderColor(Color.transparent);
		name.setMaxLength(32);
		
		address = new TextField(container, font, 475, 210, 200, 25);
		address.setBackgroundColor(Color.darkGray);
		address.setTextColor(Color.white);
		address.setCursorVisible(true);
		address.setBorderColor(Color.transparent);
		address.setMaxLength(32);

		arrow = ResourceManager.getImage(UCGame.ARROW_RSC);
	}

	
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		switch(menu){
		case 0:
			g.drawImage(ResourceManager.getImage(UCGame.MAINMENU_RSC), 0f, 0f);
			if(UCGame.sound)
				g.drawString("Yes", 60f, 551f);
			else
				g.drawString("No", 60f, 551f);
			break;
		case 1:
			g.drawImage(ResourceManager.getImage(UCGame.JOIN_RSC), 0f, 0f);
			name.render(container, g);
			address.render(container,g);
			
			switch(UCGame.character){
			case 0:
				g.drawImage(arrow, 565, 265);
				break;
			case 1:
				g.drawImage(arrow, 695, 265);
				break;
			case 2:
				g.drawImage(arrow, 885, 265);
				break;
			}
			break;
		case 2:
			g.drawImage(ResourceManager.getImage(UCGame.HOST_RSC), 0f, 0f);
			name.render(container, g);
			
			switch(UCGame.character){
			case 0:
				g.drawImage(arrow, 858, 198);
				break;
			case 1:
				g.drawImage(arrow, 858, 293);
				break;
			case 2:
				g.drawImage(arrow, 864, 396);
				break;
			}
			
			switch(timeLim){
			case 0:
				g.drawImage(arrow, 410, 180);
				break;
			case 1:
				g.drawImage(arrow, 535, 180);
				break;
			case 2:
				g.drawImage(arrow, 655, 180);
				break;
			}
			
			switch(maxPlayer){
			case 0:
				g.drawImage(arrow, 410, 275);
				break;
			case 1:
				g.drawImage(arrow, 535, 275);
				break;
			case 2:
				g.drawImage(arrow, 655, 275);
				break;
			}
			
			switch(scoreLim){
			case 0:
				g.drawImage(arrow, 410, 340);
				break;
			case 1:
				g.drawImage(arrow, 535, 340);
				break;
			case 2:
				g.drawImage(arrow, 655, 340);
				break;
			}
			
			break;
		case 3:
			if(help == 0)
				g.drawImage(ResourceManager.getImage(UCGame.CONTROLS_RSC), 0f, 0f);
			if(help == 1)
				g.drawImage(ResourceManager.getImage(UCGame.CLASSES_RSC), 0f, 0f);
			if(help == 2)
				g.drawImage(ResourceManager.getImage(UCGame.OBJECTS_RSC), 0f, 0f);
			break;
		}
		
	}

	
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		UCGame uc = (UCGame) game;

		if(uc.canenter){
			uc.enterState(UCGame.PLAYSTATE2);

		}
		
		switch(menu){
		case 0:	// main menu
			if((mouseX > 256 && mouseX < 437) && (mouseY < 309 && mouseY > 216)){
				if(input.isMousePressed(0)){
					System.out.println("Time to join a game!");
					menu = 1;
				}
			}
			
			if((mouseX > 585 && mouseX < 774) && (mouseY < 309 && mouseY > 216)){
				if(input.isMousePressed(0)){
					System.out.println("Time to host a game!");
					menu = 2;
					name.setLocation(370, name.getY());
				}
			}
			
			if((mouseX > 265 && mouseX < 437) && (mouseY < 445 && mouseY > 363)){
				if(input.isMousePressed(0)){
					System.out.println("Time to help!");
					menu = 3;
				}
			}
				
			if((mouseX > 585 && mouseX < 774) && (mouseY < 445 && mouseY > 363)){
				if(input.isMousePressed(0)){
					System.out.println("Need to exit.");
					System.exit(0);
				}
			}
			
			if((mouseX < 90) && (mouseY > 550)){
				if(input.isMousePressed(0)){
					System.out.println("Changed Sound");
					UCGame.sound = !UCGame.sound;
				}
			}
			break;
		case 1: // Join game menu
			if(input.isKeyPressed(Input.KEY_ESCAPE))
				menu = 0;
			
			if((mouseX < 131) && (mouseY > 518)){
				if(input.isMousePressed(0)){
					System.out.println("go back");
					menu = 0;
				}
			}
			
			if((mouseX > 421 && mouseX < 602) && (mouseY < 482 && mouseY > 389)){
				if(input.isMousePressed(0)){
					System.out.println("Begin/Join the Game!");
					System.out.println("The player's name is: " + name.getText());
					System.out.println("The IP/Hostname is: " + address.getText());
					// This is where the client launching code will be called and all that jazz....
					
										
					UCGame.client = new Client();
					uc.kryo = UCGame.client.getKryo();
					uc.registerKryoClasses();
					//uc.connect("ecs225-15");
					uc.connect("127.0.0.1");

				}
			}
			
			if((mouseX > 535 && mouseX < 575) && (mouseY < 335 && mouseY > 270)){
				if(input.isMousePressed(0)){
					UCGame.character = 0;
				}
			}
			if((mouseX > 650 && mouseX < 700) && (mouseY < 340 && mouseY > 260)){
				if(input.isMousePressed(0)){
					UCGame.character = 1;
				}
			}
			if((mouseX > 780 && mouseX < 890) && (mouseY < 335 && mouseY > 260)){
				if(input.isMousePressed(0)){
					UCGame.character = 2;
				}
			}
						
			break;
		case 2: // Host game menu
			if(input.isKeyPressed(Input.KEY_ESCAPE))
				menu = 0;
			
			if((mouseX < 131) && (mouseY > 518)){
				if(input.isMousePressed(0)){
					System.out.println("go back");
					menu = 0;
				}
			}
			
			if((mouseX > 421 && mouseX < 602) && (mouseY < 512 && mouseY > 420)){
				if(input.isMousePressed(0)){
					System.out.println("Begin/Host the Game!");
					System.out.println("The player's name is: " + name.getText());
					// This is where the server launching code will be called and all that jazz....
					uc.enterState(UCGame.PLAYSTATE);

					UCGame.isServer =true;
					
					UCGame.client = new Client();
					uc.kryo = UCGame.client.getKryo();
					uc.registerKryoClasses();
					uc.connect("127.0.0.1");
				}
			}
			
			
			if((mouseX > 830 && mouseX < 860) && (mouseY < 265 && mouseY > 200)){
				if(input.isMousePressed(0)){
					UCGame.character = 0;
				}
			}
			if((mouseX > 813 && mouseX < 860) && (mouseY < 360 && mouseY > 288)){
				if(input.isMousePressed(0)){
					UCGame.character = 1;
				}
			}
			if((mouseX > 764 && mouseX < 871) && (mouseY < 457 && mouseY > 386)){
				if(input.isMousePressed(0)){
					UCGame.character = 2;
				}
			}
		/*
		 * now comes the part of lots of mouse checks to toggle the arrow.....
		 */
			if((mouseX > 395 && mouseX < 505) && (mouseY < 230 && mouseY > 200)){
				if(input.isMousePressed(0)){
					timeLim = 0;
				}
			}
			if((mouseX > 520 && mouseX < 620) && (mouseY < 230 && mouseY > 200)){
				if(input.isMousePressed(0)){
					timeLim = 1;
				}
			}
			if((mouseX > 630 && mouseX < 730) && (mouseY < 230 && mouseY > 200)){
				if(input.isMousePressed(0)){
					timeLim = 2;
				}
			}
			
			if((mouseX > 395 && mouseX < 505) && (mouseY < 320 && mouseY > 295)){
				if(input.isMousePressed(0)){
					maxPlayer = 0;
				}
			}
			if((mouseX > 520 && mouseX < 620) && (mouseY < 320 && mouseY > 295)){
				if(input.isMousePressed(0)){
					maxPlayer = 1;
				}
			}
			if((mouseX > 630 && mouseX < 730) && (mouseY < 320 && mouseY > 295)){
				if(input.isMousePressed(0)){
					maxPlayer = 2;
				}
			}
			
			if((mouseX > 380 && mouseX < 505) && (mouseY < 385 && mouseY > 360)){
				if(input.isMousePressed(0)){
					scoreLim = 0;
				}
			}
			if((mouseX > 500 && mouseX < 620) && (mouseY < 385 && mouseY > 360)){
				if(input.isMousePressed(0)){
					scoreLim = 1;
				}
			}
			if((mouseX > 630 && mouseX < 730) && (mouseY < 385 && mouseY > 360)){
				if(input.isMousePressed(0)){
					scoreLim = 2;
				}
			}
			
			
			break;
		case 3: // Help menu
			if(input.isKeyPressed(Input.KEY_ESCAPE)){
				menu = 0;
				help = 0;
			}
			
			switch(help){
			case 0: // I'm showing the controls of the game
				if((mouseX < 131) && (mouseY > 518)){
					if(input.isMousePressed(0)){
						System.out.println("go back");
						menu = 0;
					}
				}
				
				if((mouseX > 892) && (mouseY > 518)){
					if(input.isMousePressed(0)){
						System.out.println("go forward");
						help = 1;
					}
				}
				break;
			case 1: // showing class info
				if((mouseX < 131) && (mouseY > 518)){
					if(input.isMousePressed(0)){
						System.out.println("go back");
						help = 0;
					}
				}
				
				if((mouseX > 892) && (mouseY > 518)){
					if(input.isMousePressed(0)){
						System.out.println("go forward");
						help = 2;
					}
				}
				break;
			case 2: // showing objects/other entities
				if((mouseX < 131) && (mouseY > 518)){
					if(input.isMousePressed(0)){
						System.out.println("go back");
						help = 1;
					}
				}
				break;
			}
			break;
		}
		
	}
	
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		
		container.setSoundOn(UCGame.sound);
		ResourceManager.getSound(UCGame.GAME_STARTSOUND_RSC).play();
	}

	@Override
	public int getID() {
		return UCGame.MENUSTATE;
	}

	

}
