package uc;

import java.util.ArrayList;
import java.util.Collections;

import jig.ResourceManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameOver extends BasicGameState {

	
	private Font font =  new TrueTypeFont(new java.awt.Font("Rockwell Extra Bold", java.awt.Font.PLAIN , 20), true);

	
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
	}

	
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		
		g.drawImage(ResourceManager.getImage(UCGame.OVER_RSC), 0f, 0f);
		
		g.setColor(Color.yellow);

		float offset = 0.0f;

		ArrayList<Char> sorted = new ArrayList<Char>(UCGame.players.size());

		for(Char dude: UCGame.players.values()){
			sorted.add(dude);
		}

		Collections.sort(sorted);

		for(Char dude : sorted){
			font.drawString(220f, 200f + offset, "" + dude.name, Color.yellow);
			font.drawString(540f, 200f + offset, "" + dude.getKills(), Color.yellow);
			font.drawString(745f, 200f + offset, "" + dude.getDeaths(), Color.yellow);
			offset += 50.0f;
		}

	}

	
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = container.getInput();
		UCGame uc = (UCGame) game;
		
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		
		if((mouseX > 568 && mouseY > 481) && ( mouseX < 845 && mouseY < 543)){ // exit to menu
			if(input.isMousePressed(0)){
				UCGame.GameOver = false;

				if(!UCGame.isServer){
					
				}else{
				}
			}
		}

		if((mouseX > 136 && mouseY > 481) && (mouseX < 458 && mouseY < 543)){
			if(input.isMousePressed(0)){
				System.out.println("need to restart the round!");

				UCGame.GameOver = false;
				UCGame.Restart = true;

				if(!UCGame.isServer){ // restarting the round on client side
					uc.enterState(UCGame.PLAYSTATE2);
				}else{				// restarting the round on server side
					UCGame.timer = UCGame.timeSelection;
					
					for(Char dude : UCGame.players.values()){
						dude.setKills(0);
						dude.setDeaths(0);
						dude.respawn();
					}
					
					UCGame.bullets.clear();
					UCGame.mines.clear();
					UCGame.grenades.clear();
					UCGame.bombs.clear();
					UCGame.explosions.clear();
					
					System.out.println("scoreLImit: " + UCGame.scoreLimit);
					
					uc.enterState(UCGame.PLAYSTATE);
				}
			}
		}
	}

	@Override
	public int getID() {
		return UCGame.GAMEOVER;
	}

}
