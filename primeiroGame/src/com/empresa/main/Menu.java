package com.empresa.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.empresa.world.World;

public class Menu {
	
	public String[] options = {"New Game", "Load Game", "Exit Game"};
	
	public int currentOption = 0;
	
	public int maxOption = options.length - 1;
	
	public boolean up, down, enter = false;
	
	public static boolean saveExist = false;
	
	public static boolean saveGame = false;
	
	public int i = 0;
	
	
	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExist = true;
		}
		else {
			saveExist = false;
		}
		
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
				//file = new File("save.txt"); // para deletar jogo ao dar um new game
				//file.delete();
			}
			else if(options[currentOption] == "Load Game") {
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			}
			else if(options[currentOption] == "Exit Game") {
				System.exit(1);
			}
		}
		
		
	}
	
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) {
				case "level":
					System.out.println("simmm");
					World.restartGame("level_" + spl2[1] + ".png");
					Game.gameState = "NORMAL";
					break;
			}
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i = 0; i < val.length; i++) {
							val[i] -= encode;
							trans[1] += val[i];
						}
						line += trans[0];
						line += ":";
						line += trans[1];
						line += "/";
					}
				}catch(IOException e) {
					
				}
			}catch(FileNotFoundException e) {
				
			}
		}
		
		return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current =  val1[i];
			current += ":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int j = 0; j < value.length; j++) {
				value[j] += encode;
				current += value[j];
			}
			
			try {
				write.write(current);
				if(i < val1.length - 1)
					write.newLine();
			}catch(IOException e) {
				
			}
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e) {
			
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
