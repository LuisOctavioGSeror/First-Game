package com.empresa.entities;


import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import com.empresa.main.Game;
import com.empresa.world.Camera;
import com.empresa.world.Node;
import com.empresa.world.Vector2i;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(97, 0, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(113, 0, 16, 16);
	public static BufferedImage SPACE_WEAPON_EN = Game.spritesheetMonsters.getSprite(113, 114, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(97, 16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(113, 16, 16, 16);
	public static BufferedImage ENEMYPURPLE_EN = Game.spritesheetMonsters.getSprite(113, 114, 16, 16);
	public static BufferedImage ENEMYRED_EN = Game.spritesheetMonsters.getSprite(113, 81, 16, 16);
	public static BufferedImage BOSS_EN = Game.spritesheet.getSprite(0, 65, 30, 30);

	//public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(0, 0, 0, 0);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(144, 0, 16, 16);
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(130, 0, 16, 16);
	public static BufferedImage GUN_UP = Game.spritesheet.getSprite(18, 63, 10, 10);
	public static BufferedImage GUN_DOWN = Game.spritesheet.getSprite(18, 50, 10, 10);
	
	public static BufferedImage SPACE_GUN_LEFT = Game.spritesheetMonsters.getSprite(141, 114, 16, 16);
	public static BufferedImage SPACE_GUN_RIGHT = Game.spritesheetMonsters.getSprite(130, 114, 16, 16);
	public static BufferedImage SPACE_GUN_UP = Game.spritesheetMonsters.getSprite(18, 114, 10, 10);
	public static BufferedImage SPACE_GUN_DOWN = Game.spritesheetMonsters.getSprite(18, 126, 10, 10);


	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	protected List<Node> path;
	
	private BufferedImage sprite;
	
	public int maskx, masky, mwidth, mheight;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}
	
	public void setX(int newX) {
		this.x = newX;
	} 
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void tick() {
		
	}
	
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	public boolean isColidding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, mwidth, mheight);
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this)
				continue;
			
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, mwidth, mheight);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
			
		}
		
		return false;
	}
	
	public void followPath(List<Node> path, double speed) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;
				//xprev = x;
				//yprev = y;
				if(x < target.x * 16 && !isColidding(this.getX() + 1, this.getY())) 
					x += speed;
				
				else if(x > target.x * 16 && !isColidding(this.getX() - 1, this.getY()))
					x -= speed;
				
				if(y < target.y * 16 && !isColidding(this.getX(), this.getY() + 1)) 
					y += speed;
				
				else if(y > target.y * 16 && !isColidding(this.getX(), this.getY() - 1))
					y -= speed;
				
				if(x == target.x * 16 && y == target.y * 16)
					path.remove(path.size() - 1);
			}
			
		}
	}
	
	public static boolean isColidding(Entity e1, Entity e2) {
		
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY()+e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY()+e2.masky, e2.mwidth, e2.mheight);

		return e1Mask.intersects(e2Mask);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		//g.setColor(Color.RED);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, mwidth, height);
	}
}
