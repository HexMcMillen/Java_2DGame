package object;

import entity.Entity;
import main.GamePanel;

public class Bed extends Entity{
	
	GamePanel gamePanel;
	
	//RIGHT NOW THIS IS AN ITEM THAT CAN BE USED FROM THE INVENTORY TO CHANCE THE DAYSTATE
	//TODO: MAKE A METHOD TO DO TAHT INSTEAD

	public Bed(GamePanel gamePanel) {
		super(gamePanel);
		this.gamePanel = gamePanel;
		
		type = type_consumable;
		name = "Bed";
		price = 500;
		
		down1 = setup("/objects/Bed", gamePanel.tileSize, gamePanel.tileSize);
		description = "[" + name + "] \nCan be used to pass the time!";
	}
	
	public boolean use(Entity entity) {
		
		gamePanel.gameState = gamePanel.sleepState;
		//TODO:SLEEPING SOUND EFFECT
		gamePanel.player.life = gamePanel.player.maxLife;
		gamePanel.player.mana = gamePanel.player.maxMana;
		gamePanel.player.getSleepingImage(down1);
		return true; //TRUE:DISAPPEAR ON USE || FALSE:UNLIMITED USE
	}

}
