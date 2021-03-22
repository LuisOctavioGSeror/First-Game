package com.empresa.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {
	
	public String[] options = {"New Game", "Load Game", "Exit Game"};
	
	public int currentOption = 0;
	
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter = false;
	
	public int i = 0;
	
	
	public void tick() {
		if(up) {
			Sound.menuChangeItem.play(); 
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		
		if(down) {
			Sound.menuChangeItem.play(); 
			down = false;
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		
		if(enter) {
			if(options[currentOption] == "New Game") {
				Game.gameState = "NORMAL";
			}
			else if(options[currentOption] == "Exit Game") {
				System.exit(1);
			}
		}
		
		
	}
	
	public void render(Graphics g) {
		
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
			g.setColor(Color.red);
			g.setFont(new Font("arial", Font.BOLD, 36));
			g.drawString("Survive", (Game.WIDTH*Game.SCALE/2) - 60, (Game.HEIGHT*Game.SCALE/2) - 100 );
			
			//menu options
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.CENTER_BASELINE, 19));
			g.drawString("New Game", (Game.WIDTH*Game.SCALE/2) - 45, (Game.HEIGHT*Game.SCALE/2) - 30 );
			g.drawString("Load Game", (Game.WIDTH*Game.SCALE/2) - 48, (Game.HEIGHT*Game.SCALE/2) + 20 );
			g.drawString("Exit Game", (Game.WIDTH*Game.SCALE/2) - 45, (Game.HEIGHT*Game.SCALE/2) + 70 );
			
			if(options[currentOption] == "New Game") {
				g.drawString(">", (Game.WIDTH*Game.SCALE/2) - 90, (Game.HEIGHT*Game.SCALE/2) - 30 );
			}
			if(options[currentOption] == "Load Game") {
				g.drawString(">", (Game.WIDTH*Game.SCALE/2) - 90, (Game.HEIGHT*Game.SCALE/2) + 20 );
			}
			if(options[currentOption] == "Exit Game") {
				g.drawString(">", (Game.WIDTH*Game.SCALE/2) - 90, (Game.HEIGHT*Game.SCALE/2) + 70 );
			}
		
	}
	
	
}
