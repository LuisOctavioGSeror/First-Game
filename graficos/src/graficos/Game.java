package graficos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{

	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	private final int WIDTH = 240;
	private final int HEIGHT = 220;
	private final int SCALE = 3;
	
	private BufferedImage image;
	
	private Spritesheet sheet;
	private BufferedImage[] player;
	private int  x = 0;
	private boolean volta;
	private int frames = 0;
	private int maxFrames =2;
	private int curAnimation = 0, maxAnimation = 3;
	
	public Game() {
		sheet = new Spritesheet("/imagem.png");
		player = new BufferedImage[3];
		player[0] = sheet.getSprite(31, 23, 32, 32);
		player[1] = sheet.getSprite(63, 23, 32, 32);
		player[2] = sheet.getSprite(95, 23, 32, 32);
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	}
	
	public void initFrame() {
		frame = new JFrame("Game#1");
		frame.add(this);
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
	
	public void Tick() {
		frames++;
		if(frames > maxFrames) {
			frames = 0;
			curAnimation++;
			if(curAnimation >= maxAnimation) {
				curAnimation = 0;
			}
		}
		
		
		if(x < 200 && volta == false) {
			x+=3;
		}
		else if(x >=200) {
			x-=3;
			volta = true;
		}
		else if(volta == true && x > 0) {
			x-=3;
		}
		
		else if(volta == true && x == 0) {
			volta = false;
		}
		
		
	}
	
	public void Render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		
		
		
		g.setColor(new Color(200, 0, 255));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		/*
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, 30, 30);
		g.setColor(Color.red);
		g.fillRect(40, 40, 80, 80);
		g.setColor(Color.yellow);
		g.fillOval(80, 80, 35, 25);
		g.setFont(new Font("Arial",Font.BOLD, 20));
		g.setColor(Color.pink);
		g.drawString("Ola Mundo", 30, 20);
		
		*/
		/*Renderização do jogo */
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(player[curAnimation], x, 90, null);
		//g2.rotate(Math.toRadians(90), 90, 90);
		/*
		g.drawImage(player, x, 10, null);
		g.drawImage(player, x, 50, null);
		g.drawImage(player, x, 90, null);
		g.drawImage(player, x, 130, null);
		*/
		//****//
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
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
		while(isRunning) {
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				Tick();
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

}
