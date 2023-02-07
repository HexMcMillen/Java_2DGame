package object;

import entity.Entity;
import main.GamePanel;

public class Potion_Small extends Entity{
	
	GamePanel gamePanel;
	

	public Potion_Small(GamePanel gamePanel) {
		super(gamePanel);
		this.gamePanel = gamePanel;
		
		type = type_consumable;
		name = "Small Potion";
		value = 5;
		price = 20;
		stackable = true;
		defenseValue = 1;

		down1 = setup("/objects/potion", gamePanel.tileSize, gamePanel.tileSize);
		description = "[" + name + "] \nA small potion that will slightly \nrestore health!";
	}
	
	public boolean use(Entity entity) {
		
		gamePanel.gameState = gamePanel.dialogueState;
		gamePanel.ui.currentDialogue = "You drink the " + name + "!\n" + "Your life has been restored!";
		entity.life += value;
		return true;
		
	}
}

//TODO:ADD SOUND TO POTION DRINKING
//TODO:MAKE A BETTER SPRITE FOR POTIONS