package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class Chest extends Entity{
	
	GamePanel gamePanel;

	public Chest(GamePanel gamePanel) {
		super(gamePanel);
		this.gamePanel = gamePanel;
		
		type = type_obstacle;
		name = "Chest";
		image = setup("/objects/Chest-Closed", gamePanel.tileSize, gamePanel.tileSize);
		image2 = setup("/objects/Chest_Open", gamePanel.tileSize, gamePanel.tileSize);
		down1 = image;
		collision = true;
		
		//SET SOLID AREA
		solidArea.x = 4;
		solidArea.y = 16;
		solidArea.width = 40;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
	}
	
	public void setLoot(Entity loot) {
		this.loot = loot;
	}
	
	public void interact() {
		gamePanel.gameState = gamePanel.dialogueState;
		
		if (opened == false) {
			//TODO:SOUND EFFECT
			
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("You open the chest and find a " + loot.name + "!");
			
			if (gamePanel.player.canObtainItem(loot) == false) {
				stringBuilder.append("\n... But you cannot carry anymore!");
			}
			else {
				stringBuilder.append("\nYou obtain the " + loot.name + "!");
				down1 = image2;
				opened = true;
			}
			gamePanel.ui.currentDialogue = stringBuilder.toString();
		}
		else {
			gamePanel.ui.currentDialogue = "Chest has already been looted!";
		}
	}
}
