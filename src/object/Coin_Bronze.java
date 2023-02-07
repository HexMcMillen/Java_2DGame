package object;

import entity.Entity;
import main.GamePanel;

public class Coin_Bronze extends Entity{
	
	GamePanel gamePanel;

	public Coin_Bronze(GamePanel gamePanel) {
		super(gamePanel);
		this.gamePanel = gamePanel;
		
		type = type_pickUpOnly;
		name = "Bronze Coin";
		value = 1;
		down1 = setup("/objects/Coin_Bronze", gamePanel.tileSize, gamePanel.tileSize);
	}
	
	public boolean use(Entity entity) {
		
		//TODO: COIN PICKUP SOUND EFFECT
		gamePanel.ui.addMessage("Coin + " + value);
		gamePanel.player.coin += value;
		return true;
	}
}
