package com.empresa.entities; //esse Bullet ? a municao no caso 

import java.awt.image.BufferedImage;

public class Bullet extends Entity{

	public Bullet(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// classe bullet se refere a muni??o n?o ao tiro
	}

}
