package com.empresa.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.empresa.main.Game;
import com.empresa.world.Camera;

public class BulletShoot extends Entity {
	
	private double dx;
	private double dy;
	private double spd = 8; //bullet speed
	
	private int lifeTime = 30, curLifeTime = 0;
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		
		x+=dx*spd;
		y+=dy*spd;
		curLifeTime++;
		if(curLifeTime == lifeTime) {
			Game.bullets.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(this.getX() - Camera.x, this.getY() + 9 - Camera.y, width, height);
	}

}
