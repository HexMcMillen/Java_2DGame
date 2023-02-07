package object;

import java.awt.Color;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class FireBall extends Projectile{
	
	GamePanel gamePanel;

	public FireBall(GamePanel gamePanel) {
		super(gamePanel);
		this.gamePanel = gamePanel;
		
		name = "Fireball";
		speed = 10;
		maxLife = 40;
		life = maxLife;
		attack = 2;
		knockBackPower = 1;
		useCost = 1; //MANA NEEDED TO USE
		alive = false;
		
		getImage();
	}
	
	public void getImage() {
		
		up1 = setup("/projectile/fireball_up1", gamePanel.tileSize, gamePanel.tileSize);
		up2 = setup("/projectile/fireball_up2", gamePanel.tileSize, gamePanel.tileSize);
		right1 = setup("/projectile/fireball_right1", gamePanel.tileSize, gamePanel.tileSize);
		right2 = setup("/projectile/fireball_right2", gamePanel.tileSize, gamePanel.tileSize);
		down1 = setup("/projectile/fireball_down1", gamePanel.tileSize, gamePanel.tileSize);
		down2= setup("/projectile/fireball_down2", gamePanel.tileSize, gamePanel.tileSize);
		left1 = setup("/projectile/fireball_left1", gamePanel.tileSize, gamePanel.tileSize);
		left2 = setup("/projectile/fireball_left2", gamePanel.tileSize, gamePanel.tileSize);
	}
	
	public boolean haveResource(Entity user) { //CAN ONLY BE USED IF THE USER HAS ENOUGH MANA
		
		boolean haveResource = false;
		if (user.mana >= useCost) {
			haveResource = true;
		}
		return haveResource;
	}
	
	public void useResource(Entity user) {
		user.mana -= useCost;
	}
	
	public Color getParticleColor() {
		Color color = new Color(240, 50, 0);
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
