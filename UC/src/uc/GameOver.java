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
			font.drawString(225f, 200f + offset, "" + dude.id, Color.yellow);
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
		
		if(uc.canenter)
			System.out.println("I can enter for some reason");
		
		if((mouseX > 568 && mouseY > 481) && ( mouseX < 845 && mouseY < 543)){ // exit to menu
			if(input.isMousePressed(0)){
				uc.disconnect();
				uc.enterState(UCGame.MENUSTATE);
				uc.canenter = false;
			}
		}

		if((mouseX > 136 && mouseY > 481) && (mouseX < 458 && mouseY < 543)){
			if(input.isMousePressed(0))
				System.out.println("need to restart the round!");
		}
	}

	@Override
	public int getID() {
		return UCGame.GAMEOVER;
	}

}
