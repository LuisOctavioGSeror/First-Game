package com.empresa.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.empresa.main.Game;
import com.empresa.world.Camera;
import com.empresa.world.World;

public class Enemy extends Entity {
	
	private double speed = 0.5;
	private boolean moved = false;
	private boolean enemyIsDamaged = false;
	
	private int damagedFrames = 10, damagedCurrent = 0;

	private int maskx = 5, masky = 6, maskw = 10, maskh = 10;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 2;
	
	private BufferedImage[] sprites;
	private BufferedImage[] enemyDamaged;

	private int life = 10;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[3];
		sprites[0] = Game.spritesheet.getSprite(112, 17, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(127, 17, 16, 16);
		sprites[2] = Game.spritesheet.getSprite(143, 17, 16, 16);
		
		enemyDamaged = new BufferedImage[3];
		enemyDamaged[0] = Game.spritesheet.getSprite(112, 33, 16, 16);
		enemyDamaged[1] = Game.spritesheet.getSprite(127, 33, 16, 16);
		enemyDamaged[2] = Game.spritesheet.getSprite(143, 33, 16, 16);

	}

	
	public void tick() {
		moved = false;
		//if(Game.rand.nextInt(100) < 16) {  //usando aleatoriedade para "manipular" velocidade
			if(isColiddingWithPlayer() == false) {
				if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY())
						&& !isColidding((int)(x+speed), this.getY())) {
					
					moved = true;
					x+=speed;
				}
				
				else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY())
						&& !isColidding((int)(x-speed), this.getY())) {
					
					moved = true;
					x-=speed;
				}
				
				if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y+speed))
						&& !isColidding(this.getX(), (int)(y+speed))) {
					
					moved = true;
					y+=speed;
				}
				
				else if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y-speed))
						&& !isColidding(this.getX(), (int)(y-speed))) {
					
					moved = true;
					y-=speed;
				}
				
				//if(moved) {
					frames++;
					if(frames == maxFrames) {
						frames = 0;
						index++;
						if(index > maxIndex) 
							index = 0;
						
					}  
					
				//}	
					
			}
			
			
			else {
				//colidindo
				if(Game.rand.nextInt(100) < 10) {
					Game.player.life-= Game.rand.nextInt(3); //perde aleatoriamente um valor de 0 a 3 de vida
					Game.player.isDamaged = true;
				}
				if(Game.player.life <= 0) {
					//System.exit(1); //desliga GAME OVER
				}
			}
			
		//}
			
		isCollidingWithBullet();
		
		if(life <= 0) {
			destroySelf();
			return;
		}
		
		if(enemyIsDamaged == true) {
			this.damagedCurrent++;
			if(this.damagedCurrent == damagedFrames) {
				this.damagedCurrent = 0;
				this.enemyIsDamaged = false;
			}	
		}
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void isCollidingWithBullet() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if(e instanceof BulletShoot) {
				
				if(Entity.isColidding(this, e)) {
					enemyIsDamaged = true;
					life-=5;
					Game.bullets.remove(i);
					return;
				}
			}
		}
		
		
	}
	
	public boolean isColiddingWithPlayer() {
		
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, maskw, maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);		
		
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColidding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext + maskx, ynext + masky, maskw, maskh);
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this)
				continue;
			
			Rectangle targetEnemy = new Rectangle(e.getX() + maskx, e.getY() + masky, maskw, maskh);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
			
		}
		
		return false;
	}
	
	 
	public void render(Graphics g) {
		if(!enemyIsDamaged)
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		
		else
			g.drawImage(enemyDamaged[index], this.getX() - Camera.x, this.getY() - Camera.y, null);

		//g.setColor(Color.blue);
		//g.fillRect(this.getX() - Camera.x + maskx, this.getY() + masky - Camera.y, maskw, maskh); // testando mascara
	} 
	
}
