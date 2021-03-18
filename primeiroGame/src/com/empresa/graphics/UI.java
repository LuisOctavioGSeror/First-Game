package com.empresa.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.empresa.entities.Player;
import com.empresa.main.Game;

public class UI {
	
	
	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(4, 8, 50, 8);
		g.setColor(Color.GREEN);
		g.fillRect(4, 8, (int)((Game.player.life/Game.player.maxLife)*50), 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.drawString((int)Game.player.life+"/"+(int)Game.player.maxLife, 18, 15);
	}

}
