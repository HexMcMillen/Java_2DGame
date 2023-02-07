package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class KeyGold extends Entity{
	
	GamePanel gamePanel;

	public KeyGold(GamePanel gamePanel) {
		super(gamePanel);
		this.gamePanel = gamePanel;

		type = type_consumable;
		name = "Key";
		price = 25;
		stackable = true;
		
		down1 = setup("/objects/key", gamePanel.tileSize, gamePanel.tileSize);
		description = "[" + name + "] \nMay open a door somewhere!";

	}
	
	public boolean use(Entity entity) { //OPEN INVENTORY AND USE THE KEY TO OPEN THE DOOR
		
		gamePanel.gameState = gamePanel.dialogueState;
		
		int objIndex = getDetected(entity, gamePanel.obj, "Door");
		
		if (objIndex != 999) {
			gamePanel.ui.currentDialogue = "You use the key!";
			//TODO: SOUND EFFECT FOR OPENING DOOR
			gamePanel.obj[gamePanel.currentMap][objIndex] = null;
			return true;
		}
		else {
			gamePanel.ui.currentDialogue = "There is nothing to use this on \naround you!";
			return false;
		}
	}

}
