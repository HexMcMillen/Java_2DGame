package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class Boots extends Entity {
	
	GamePanel gamePanel;

	public Boots(GamePanel gamePanel) {
		super(gamePanel);
		this.gamePanel = gamePanel;

		type = type_pickUpOnly;
		name = "Boots";
		value = 2;
		down1 = setup("/objects/boot", gamePanel.tileSize, gamePanel.tileSize);
	}
	
	public boolean use(Entity entity) {
		
		gamePanel.ui.addMessage("Speed + " + value);
		gamePanel.player.speed += value;
		return true;
	}
}
