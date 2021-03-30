package com.empresa.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;

import com.empresa.world.World;

public class Pause {
	
	public boolean paused = false;
	
	public String[] options = {"Continue", "New Game", "Save Game", "Load Game", "Exit Game"};
	
	public static int currentOption = 0;
	
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter = false;
	
	
	public void tick() {
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		
		if(enter) {
			if(options[currentOption] == "Continue") {
				enter = false;
				currentOption = 0;
				Game.gameState = "NORMAL";
			}
			
			else if(options[currentOption] == "New Game") {
				enter = false;
				currentOption = 0;
				Game.gameState = "NORMAL";
				World.restartGame("level_1.png");
			}
			
			else if(options[currentOption] == "Save Game") {
				enter = false;
				currentOption = 0;
				Game.saveGame = true;
				Game.gameState = "NORMAL";

			}
			
			else if(options[currentOption] == "Load Game") {
				enter = false;
				currentOption = 0;
				File file = new File("save.txt");
				if(file.exists()) {
					String saver = Menu.loadGame(10);
					Menu.applySave(saver);
				}
			}	
			
			else if(options[currentOption] == "Exit Game") {
				System.exit(1);
			}
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,100));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 36));
		g.drawString("PAUSED", (Game.WIDTH*Game.SCALE/2) - 60, (Game.HEIGHT*Game.SCALE/2) - 100 );
		
		//menu options
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.CENTER_BASELINE, 19));
		g.drawString("Continue", (Game.WIDTH*Game.SCALE/2) - 45, (Game.HEIGHT*Game.SCALE/2) - 30 );
		g.drawString("New Game", (Game.WIDTH*Game.SCALE/2) - 45, (Game.HEIGHT*Game.SCALE/2) + 20 );
		g.drawString("Save Game", (Game.WIDTH*Game.SCALE/2) - 45, (Game.HEIGHT*Game.SCALE/2) + 70 );
		g.drawString("Load Game", (Game.WIDTH*Game.SCALE/2) - 48, (Game.HEIGHT*Game.SCALE/2) + 120 );
		g.drawString("Exit Game", (Game.WIDTH*Game.SCALE/2) - 45, (Game.HEIGHT*Game.SCALE/2) + 170 );
		
		if(options[currentOption] == "Continue") {
			g.drawString(">", (Game.WIDTH*Game.SCALE/2) - 90, (Game.HEIGHT*Game.SCALE/2) - 30 );
		}
		if(options[currentOption] == "New Game") {
			g.drawString(">", (Game.WIDTH*Game.SCALE/2) - 90, (Game.HEIGHT*Game.SCALE/2) + 20 );
		}
		if(options[currentOption] == "Save Game") {
			g.drawString(">", (Game.WIDTH*Game.SCALE/2) - 90, (Game.HEIGHT*Game.SCALE/2) + 70 );
		}
		if(options[currentOption] == "Load Game") {
			g.drawString(">", (Game.WIDTH*Game.SCALE/2) - 90, (Game.HEIGHT*Game.SCALE/2) + 120 );
		}
		if(options[currentOption] == "Exit Game") {
			g.drawString(">", (Game.WIDTH*Game.SCALE/2) - 90, (Game.HEIGHT*Game.SCALE/2) + 170 );
		}
	}

}
