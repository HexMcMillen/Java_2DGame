package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class Health extends Entity{
	
	GamePanel gamePanel;

	public Health(GamePanel gamePanel) {
		super(gamePanel);
		this.gamePanel = gamePanel;
		
		type = type_pickUpOnly;
		name = "Heart";
		value = 2;
		down1 = setup("/objects/Heart_Full", gamePanel.tileSize, gamePanel.tileSize);
		image = setup("/objects/Heart_Empty", gamePanel.tileSize, gamePanel.tileSize);
		image2 = setup("/objects/Heart_Half", gamePanel.tileSize, gamePanel.tileSize);
		image3 = setup("/objects/Heart_Full", gamePanel.tileSize, gamePanel.tileSize);
	}
	
	public boolean use(Entity entity) {
		
		//TODO: SOUND FOR HEART PICKUP
		gamePanel.ui.addMessage(value + " life restored!");
		entity.life += value;
		return true;
	}

}
