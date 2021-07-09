package com.empresa.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.empresa.main.Game;
import com.empresa.main.Sound;
import com.empresa.world.AStar;
import com.empresa.world.Camera;
import com.empresa.world.Vector2i;

public class EnemyRed extends Entity {
	
	

	public double speed = 0.5;
	//private boolean moved = false;
	private boolean enemyIsDamaged = false;
	
	private int damagedFrames = 10, damagedCurrent = 0;
		
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 2;
	
	private BufferedImage[] sprites;
	private BufferedImage[] enemyDamaged;
	
	private double initialTime = System.currentTimeMillis();
	private double daley;
		
	

	
	private int life = 100;

	public EnemyRed(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[3];
		sprites[0] = Game.spritesheetMonsters.getSprite(112, 81, 16, 16);
		sprites[1] = Game.spritesheetMonsters.getSprite(127, 81, 16, 16);
		sprites[2] = Game.spritesheetMonsters.getSprite(143, 81, 16, 16);
		
		enemyDamaged = new BufferedImage[3];
		enemyDamaged[0] = Game.spritesheetMonsters.getSprite(112, 81, 16, 16);
		enemyDamaged[1] = Game.spritesheetMonsters.getSprite(127, 81, 16, 16);
		enemyDamaged[2] = Game.spritesheetMonsters.getSprite(143, 81, 16, 16);

	}

	
	public void tick() {
		
		//masky = 5;
		//maskx = 4;
		mwidth = 10;
		mheight = 10;
		
		daley = System.currentTimeMillis();
		
		/* without A* method
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
				
				
		
					
			}
				
			
			*/
			if(!isColiddingWithPlayer() && ((this.calculateDistance((int)this.x, (int)this.y, (int)Game.player.getX(), (int)Game.player.getY()) < 150) || Game.enemies.size() <= 10)) {
				if(path == null || path.size() == 0 || (daley - initialTime >= 500)) {
					initialTime = System.currentTimeMillis();
					
					Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
					Vector2i end = new Vector2i((int)(Game.player.x/16), (int)(Game.player.y/16));
					
					path  = AStar.findPath(Game.world, start, end);
				}
			}
		
			else if(isColiddingWithPlayer()) {
				//colidindo
				if(Game.rand.nextInt(100) < 10) {
					Sound.hurtEffect.play();
					Game.player.life-= Game.rand.nextInt(3); //perde aleatoriamente um valor de 0 a 3 de vida
					Game.player.isDamaged = true;
					//Sound.hurt.play(); 
				}
				if(Game.player.life <= 0) {
					//System.exit(1); //desliga GAME OVER
				}
			}
			
			followPath(path, speed);
		
			//if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) 
					index = 0;
				
			}
			
			
		//}	
			
		//}
			
		isCollidingWithBullet();
		
		if(life <= 0) {
			destroySelf();
			Sound.zombieHurt.play();
			if(Player.PlayerGun == "SpaceGun")
				BulletShoot.generateParticles(100, (int)x, (int)y);
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
		Game.enemiesRed.remove(this);
		Game.entities.remove(this);
		Player.points += 100;
	}
	
	public void isCollidingWithBullet() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if(e instanceof BulletShoot) {
				
				if(Entity.isColidding(this, e)) {
					enemyIsDamaged = true;
					life-= BulletShoot.bulletShootDamage;
					Player.points += 5;
					Game.bullets.remove(i);
					return;
				}
			}
		}
		
		
	}
	 
	public boolean isColiddingWithPlayer() {
		
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskx, this.getY() + masky, mwidth, mheight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);		
		
		return enemyCurrent.intersects(player);
	}
	
	
	 
	public void render(Graphics g) {
		if(!enemyIsDamaged) {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}	
		else
			g.drawImage(enemyDamaged[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			
		//g.setColor(Color.blue);
		//g.fillRect(this.getX() - Camera.x + maskx, this.getY() + masky - Camera.y, mwidth, mheight); // testando mascara
	} 
	
}


