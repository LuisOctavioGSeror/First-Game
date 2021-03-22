package com.empresa.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.empresa.graphics.Spritesheet;
import com.empresa.main.Game;
import com.empresa.main.Sound;
import com.empresa.world.Camera;
import com.empresa.world.World;

public class Player extends Entity {

	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = right_dir;
	public double speed = 1;
		
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	
	private BufferedImage[] rightPlayerDamage;
	private BufferedImage[] leftPlayerDamage;
	private BufferedImage[] upPlayerDamage;
	private BufferedImage[] downPlayerDamage;
	
	private boolean hasGun = false;

	public int ammo = 0, maxAmmo = 100;
	
	public boolean shoot = false, mouseShoot = false;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	public double life = 100, maxLife = 100;
	public int mx,my;

	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		rightPlayerDamage = new BufferedImage[4];
		leftPlayerDamage = new BufferedImage[4];
		upPlayerDamage = new BufferedImage[4];
		downPlayerDamage = new BufferedImage[4];

		
		for(int i = 0; i < 4; i++ ) {
			rightPlayer[i] = Game.spritesheet.getSprite(34 + i*(16), 0, 16, 16);
		}
		
		for(int i = 0; i < 4; i++ ) {
			leftPlayer[i] = Game.spritesheet.getSprite(34 + i*(16), 17, 16, 16);
		}
		
		for(int i = 0; i < 4; i++ ) {
			rightPlayerDamage[i] = Game.spritesheet.getSprite(34 + i*(16),33, 16, 16);
		}
		
		for(int i = 0; i < 4; i++ ) {
			leftPlayerDamage[i] = Game.spritesheet.getSprite(34 + i*(16), 50, 16, 16);
		}
		for(int i = 0; i < 4; i++ ) {
			downPlayer[i] = Game.spritesheet.getSprite(34 + i*(16), 81, 16, 16);
		}
		
		for(int i = 0; i < 4; i++ ) {
			upPlayer[i] = Game.spritesheet.getSprite(34 + i*(16), 97, 16, 16);
		}
		for(int i = 0; i < 4; i++ ) {
			downPlayerDamage[i] = Game.spritesheet.getSprite(34 + i*(16),115, 16, 16);
		}
		
		for(int i = 0; i < 4; i++ ) {
			upPlayerDamage[i] = Game.spritesheet.getSprite(34 + i*(16), 131, 16, 16);
		}
	}

	
	public void tick() {
		moved = false;
		if(right && World.isFree((int)(x+speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		if(up && World.isFree(this.getX(), (int)(y-speed))) {
			moved = true;
			dir = up_dir;
			y-=speed;
		}
		else if(down && World.isFree(this.getX(), (int)(y+speed))) {
			moved = true;
			dir = down_dir;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) 
					index = 0;
				
			}
		}   
		
		checkCollisionLifePack();
		checkCollisionAmmo();
		checkCollisionGun();
		
		if(isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if(shoot && hasGun && ammo > 0) {
			shoot = false; //one shot at a time 
			ammo--;
			
			int dx = 0;
			int dy = 0;
			int px = 0;
			int py = 0;
	
			
			if(dir == right_dir) {
				dx = 1;
				px = 8;
			}
			else if(dir == left_dir){
				dx = -1;
			}
			if(dir == up_dir) {
				px = 6;
				dy = -1;
				py = -4;
			}
			else if(dir == down_dir){
				px = 7;
				dy = 1;
				py = 2;
			}
			
			Sound.shootEffect.play();
			BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
			Game.bullets.add(bullet);
		}
		
		if(mouseShoot) {
			mouseShoot = false;
			
			double angle = Math.atan2(my - (this.getY() + 8 - Camera.y), mx - (this.getX() + 8 - Camera.x));
			//System.out.println(angle);
			if(hasGun && ammo > 0) {
				ammo--;
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				int px = 0, py = 0;
				if(dir == right_dir) {
					dx = 1;
					px = 8;
				}
				else if(dir == left_dir) {
					dx = -1;
					px = 3;
				}
				if(dir == up_dir) {
					px = 6;
					dy = -1;
					py = -4;
				}
				else if(dir == down_dir) {
					px = 7;
					dy = 1;
					py = 2;
				}
				
				Sound.shootEffect.play();
				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
			}
			
			
		}
		
		
		if(life <= 0) {
			Game.gameState = "GAME_OVER";
		}
		
		Camera.x = Camera.clamp(getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
			
	 }
	
	public void checkCollisionGun() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColidding(this, atual)) {
					hasGun = true;
					Game.entities.remove(atual);
					
				}
			}
		}
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColidding(this, atual)) {
					if(ammo < maxAmmo) {
						ammo += 100;
						Game.entities.remove(atual);
					}
				}
			}
		}
	}
	
	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof LifePack) {
				if(Entity.isColidding(this, atual)) {
					life+=10;
					if(life > 100)
						life = 100;
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void render(Graphics g) {
		
		if(!isDamaged){
			
			if(dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.GUN_RIGHT, this.getX() + 8 - Camera.x, this.getY() + 2 - Camera.y, null);

				}
			}
			else if(dir == left_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.GUN_LEFT, this.getX() - 8 - Camera.x, this.getY() + 2 - Camera.y, null);

				}
			}
			else if(dir == up_dir) {
				g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.GUN_UP, this.getX() + 4 - Camera.x, this.getY() - 2 - Camera.y, null);

				}
			}
			else if(dir == down_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					g.drawImage(Entity.GUN_DOWN, this.getX() + 5 - Camera.x, this.getY() + 11 - Camera.y, null);

				}
			}
		} 
		
		else {
			
			if(dir == right_dir) {
				g.drawImage(rightPlayerDamage[index], this.getX() - Camera.x, this.getY() - Camera.y,  null);
				if(hasGun) {
					//draw gun at right
					g.drawImage(Entity.GUN_RIGHT, this.getX() + 8 - Camera.x, this.getY() + 2 - Camera.y, null);
				}	
			}
			else if(dir == left_dir) {
				g.drawImage(leftPlayerDamage[index], this.getX() - Camera.x, this.getY() - Camera.y,  null);
				if(hasGun) {
					//draw gun at left
					g.drawImage(Entity.GUN_LEFT, this.getX() - 8 - Camera.x, this.getY() + 2 - Camera.y, null);

				}
			}
			if(dir == up_dir) {
				g.drawImage(upPlayerDamage[index], this.getX() - Camera.x, this.getY() - Camera.y,  null);
				if(hasGun) {
					//draw gun at right
					g.drawImage(Entity.GUN_UP, this.getX() + 4 - Camera.x, this.getY() - 2 - Camera.y, null);
				}	
			}
			else if(dir == down_dir) {
				g.drawImage(downPlayerDamage[index], this.getX() - Camera.x, this.getY() - Camera.y,  null);
				if(hasGun) {
					//draw gun at left
					g.drawImage(Entity.GUN_DOWN, this.getX() + 5 - Camera.x, this.getY() + 11 - Camera.y, null);

				}
			}
		}
	 }
	
	
	}
