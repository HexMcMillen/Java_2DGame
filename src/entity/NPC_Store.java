package entity;

import java.util.Random;

import main.GamePanel;
import object.KeyGold;
import object.Potion_Small;
import object.Shield_Metal;
import object.Sword_Metal;

public class NPC_Store extends Entity{

	public NPC_Store(GamePanel gamePanel) {
		
		super(gamePanel);
		
		direction = "down";
		speed = 0;
		
		getImage();
		setDialogue();
		setItems();
	}
	
	public void getImage() {
		
		up1 = setup("/npc/NPC_Store", gamePanel.tileSize, gamePanel.tileSize);
		up2 = setup("/npc/NPC_Store", gamePanel.tileSize, gamePanel.tileSize);
		right1 = setup("/npc/NPC_Store", gamePanel.tileSize, gamePanel.tileSize);
		right2 = setup("/npc/NPC_Store", gamePanel.tileSize, gamePanel.tileSize);
		down1 = setup("/npc/NPC_Store", gamePanel.tileSize, gamePanel.tileSize);
		down2= setup("/npc/NPC_Store", gamePanel.tileSize, gamePanel.tileSize);
		left1 = setup("/npc/NPC_Store", gamePanel.tileSize, gamePanel.tileSize);
		left2 = setup("/npc/NPC_Store", gamePanel.tileSize, gamePanel.tileSize);
	}
	
	public void setDialogue() { //store dialogue text to be said
		
		dialogues[0] = "So you have come to my shop! \nWould you like to trade?";
		
	}
	
	public void speak() {
		
		super.speak();
		gamePanel.gameState = gamePanel.tradeState;
		gamePanel.ui.npc = this;
	}
	
	public void setItems() {
		
		inventory.add(new Potion_Small(gamePanel));
		inventory.add(new KeyGold(gamePanel));
		inventory.add(new Shield_Metal(gamePanel));
		inventory.add(new Sword_Metal(gamePanel));
	}
}
