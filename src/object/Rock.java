package object;

import java.awt.Color;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class Rock extends Projectile{
	
	GamePanel gamePanel;

	public Rock(GamePanel gamePanel) {
		super(gamePanel);
		this.gamePanel = gamePanel;
		
		name = "Rock";
		speed = 6;
		maxLife = 40;
		life = maxLife;
		attack = 2;
		useCost = 1;
		alive = false;
		
		getImage();
	}
	
	public void getImage() {
		
		up1 = setup("/projectile/rock", gamePanel.tileSize, gamePanel.tileSize);
		up2 = setup("/projectile/rock", gamePanel.tileSize, gamePanel.tileSize);
		right1 = setup("/projectile/rock", gamePanel.tileSize, gamePanel.tileSize);
		right2 = setup("/projectile/rock", gamePanel.tileSize, gamePanel.tileSize);
		down1 = setup("/projectile/rock", gamePanel.tileSize, gamePanel.tileSize);
		down2= setup("/projectile/rock", gamePanel.tileSize, gamePanel.tileSize);
		left1 = setup("/projectile/rock", gamePanel.tileSize, gamePanel.tileSize);
		left2 = setup("/projectile/rock", gamePanel.tileSize, gamePanel.tileSize);
	}
	
	public boolean haveResource(Entity user) { //CAN ONLY BE USED IF THE USER HAS ENOUGH AMMO
		
		boolean haveResource = false;
		if (user.ammo >= useCost) {
			haveResource = true;
		}
		return haveResource;
	}
	
	public void useResource(Entity user) {
		user.ammo -= useCost;
	}
	
	public Color getParticleColor() {
		Color color = new Color(40, 50, 0);
		return color;
	}
	
	public int getParticleSize() {
		int size = 10; //PARTICLE SIZE WILL BE 6 PIXELS
		return size;
	}
	
	public int getParticleSpeed() {
		int speed = 1;
		return speed;
	}
	
	public int getParticleMaxLife() { //HOW LONG THE PARTICLES LAST
		int maxLife = 20;
		return maxLife;
	}

}
