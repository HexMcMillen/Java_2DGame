package tile_interactive;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class InteractiveTile extends Entity{
	
	GamePanel gamePanel;
	public boolean destructible = false;

	public InteractiveTile(GamePanel gamePanel) {
		super(gamePanel);
		this.gamePanel = gamePanel;
	}
	
	public boolean correctItem(Entity entity) {
		boolean correctItem = false;
		return correctItem;
	}
	
	public void playSe() {}
	
	public InteractiveTile getDestroyedForm() {
		InteractiveTile tile = null;
		return tile;
	}
	
	public void update() {
		
		if (invincible == true) {
			invincibleCounter++;
			if (invincibleCounter > 20) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
	}
	
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
		int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY; 
		
		if (worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX && 
			worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
			worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
			worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {
			
			
			g2.drawImage(down1, screenX, screenY, null );
			
		}
	}

}
