package com.empresa.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.empresa.main.Game;
import com.empresa.world.Camera;

public class Particle extends Entity {
	public int lifeTime = 15;
	public int curLife = 0;
	
	public int spd = 2;
	public double dx = 0;
	public double dy = 0;

	public Particle(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		dx = new Random().nextGaussian();
		dy = new Random().nextGaussian();

	}
	
	public void tick() {
		x += dx*spd;
		y += dy*spd;
		
		curLife++;
		if(curLife == lifeTime) {
			Game.entities.remove(this);
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(this.getX() - Camera.x, this.getY() + 9 - Camera.y, width, height);
	}

}
