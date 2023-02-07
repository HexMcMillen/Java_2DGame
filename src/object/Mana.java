package object;

import entity.Entity;
import main.GamePanel;

public class Mana extends Entity{
	
	GamePanel gamePanel;

	public Mana(GamePanel gamePanel) {
		super(gamePanel);
		this.gamePanel = gamePanel;
		
			type = type_pickUpOnly;
			name = "Mana";
			value = 1;
			down1 = setup("/objects/Mana", gamePanel.tileSize, gamePanel.tileSize);
			image = setup("/objects/Mana", gamePanel.tileSize, gamePanel.tileSize);
			image2 = setup("/objects/Mana_Empty", gamePanel.tileSize, gamePanel.tileSize);
		}
	
	public boolean use(Entity entity) {
		
		//TODO: SOUND FOR HEART PICKUP
		gamePanel.ui.addMessage(value + " mana restored!");
		entity.mana += value;
		return true;
	}
}
