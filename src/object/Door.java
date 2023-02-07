package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class Door extends Entity {
	
	GamePanel gamePanel;

	public Door(GamePanel gamePanel) {
		super(gamePanel);
		this.gamePanel = gamePanel;

		type = type_obstacle;
		name = "Door";
		down1 = setup("/objects/door", gamePanel.tileSize, gamePanel.tileSize);
		collision = true;
		
		
	}

	public void interact() { //OVERRIDE WHAT HAPPENS WHEN YOU PRESS ENTER ON THE OBJECT
		
		gamePanel.gameState = gamePanel.dialogueState;
		gamePanel.ui.currentDialogue = "A key is needed to open this!";
	}
}
