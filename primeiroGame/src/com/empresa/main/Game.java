package com.empresa.main;


import java.awt.Canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.empresa.entities.Boss;
import com.empresa.entities.BulletShoot;
import com.empresa.entities.Enemy;
import com.empresa.entities.Entity;
import com.empresa.entities.Player;
import com.empresa.graphics.Spritesheet;
import com.empresa.graphics.UI;
import com.empresa.world.Camera;
import com.empresa.world.World;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	
	private int CUR_LEVEL = 1, MAX_LEVEL = 3;
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Boss> boss;
	public static List<BulletShoot> bullets;

	public static Spritesheet spritesheet;
	
	public static World world;
	
	public static Player player;
	
	public static Random rand;
	
	public UI ui;
	
	public static String gameState = "MENU";
	
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	public static boolean saveGame = false;
	
	public Menu menu;
	
	public Pause pause;
	
	public Game() {
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		//setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize())); //full screen
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		//inicializando objetos
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		boss = new ArrayList<Boss>();

		bullets = new ArrayList<BulletShoot>();
		spritesheet = new Spritesheet("/spritesheet3.png");
		player = new Player(0, 0, 16, 16, spritesheet.getSprite(34, 0, 16, 16));
		entities.add(player);
		world = new World("/map_level_1.png");
		
		
		menu = new Menu();
		pause = new Pause();

	}
	
	public void initFrame() {
		frame = new JFrame("Game#1");
		frame.add(this);
		//frame.setUndecorated(true); //for full screen
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		Game game = new Game();
		game.start();
	}
	
	public void tick() {
		
		if(gameState == "NORMAL") {
			
			if(this.saveGame == true) {
				this.saveGame = false;
				String[] opt1 = {"level"};
				int[] opt2 = {this.CUR_LEVEL};
				Menu.saveGame(opt1, opt2, 10);
				System.out.println("Jogo salvo!");
			}
			this.restartGame = false;
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			
			for(int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();	
			}
			
			if(enemies.size() == 0) {
				//System.out.println("proximo level");
				CUR_LEVEL++;
				if(CUR_LEVEL > MAX_LEVEL) //TEM UMA BOA RAZAO PRA NAO SER CUR_LEVEL<MAX_LEVEL CUR_LEVEL++ 
					CUR_LEVEL = MAX_LEVEL;
				
				String newWorld = "level_" + CUR_LEVEL + ".png";
				World.restartGame(newWorld);
			}
		}
		
		else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
			if(this.framesGameOver == 35) {
				this.framesGameOver = 0;
				if(this.showMessageGameOver) 
					this.showMessageGameOver = false;
				else 
					this.showMessageGameOver = true;
			}
			
			if(restartGame) {
				CUR_LEVEL = 1;
				this.restartGame = false;
				this.gameState = "NORMAL";
				String newWorld = "level_" + CUR_LEVEL + ".png";
				World.restartGame(newWorld);
			}
		}
		
		else if(gameState == "MENU") {
			Sound.musicBackground.loop(); //nao ta funcionando n sei pq
			menu.tick();
		}
		
		else if(gameState == "PAUSED") {
			pause.tick();
		}
	}
	
	public void Render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		/*Renderização do jogo */
		//Graphics2D g2 = (Graphics2D) g;
		//****//
		world.render(g);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g); 
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);	
		}
		
		ui.render(g);
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		g.setFont(new Font("arial", Font.BOLD, 20));
		g.setColor(Color.white);
		g.drawString("Ammo: "+ player.ammo, 15, 75);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,100));
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("arial", Font.BOLD, 36));
			g.setColor(Color.white);
			g.drawString("GAME OVER", WIDTH*SCALE/2 - 80, HEIGHT*SCALE/2 );
			g.setFont(new Font("arial", Font.CENTER_BASELINE, 32));
			if(showMessageGameOver)
				g.drawString("Pressione enter para reiniciar", WIDTH*SCALE/2 - 200, HEIGHT*SCALE/2 + 80 );

		}
		
		else if(gameState == "MENU") {
			menu.render(g);
		}
		
		else if(gameState == "PAUSED") {
			pause.render(g);
		}
		
		bs.show();
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime(); //maneira profissional para controlar o fps
		double amountOfTicks = 60.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus(); //foco automatico ao iniciar
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				Render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS " + frames);
				frames = 0;
				timer+=1000;
			}
		}
		
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if(gameState == "MENU") {
				menu.up = true;
			}
			else if(gameState == "PAUSED") {
				pause.up = true;
			}
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			if(gameState == "MENU") {
				menu.down = true;
			}
			else if(gameState == "PAUSED") {
				pause.down = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X) {
			player.shoot = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(gameState == "MENU") {
				menu.enter = true;
			}
			else if(gameState == "PAUSED") {
				pause.enter = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if(gameState == "NORMAL") {
				gameState = "PAUSED";
			}
			else if(gameState == "PAUSED") {
				Pause.currentOption = 0;
				gameState = "NORMAL";
			}
			
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
			if(gameState == "MENU") {
				menu.up = false;
			}
			else if(gameState == "PAUSED") {
				pause.up = false;
			}
		}
		
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
			if(gameState == "MENU") {
				menu.up = false;
			}
			else if(gameState == "PAUSED") {
				pause.down = false;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X) {
			player.shoot = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = false;
			if(gameState == "MENU") {
				menu.enter = false;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseShoot = true;
		player.mx = (e.getX()/3);
		player.my = (e.getY()/3);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}

