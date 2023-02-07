package object;

import entity.Entity;
import main.GamePanel;

public class Sword_Metal extends Entity{

	public Sword_Metal(GamePanel gamePanel) {
		super(gamePanel);
		
		type = type_sword;
		name = "Metal Sword";
		down1 = setup("/objects/sword", gamePanel.tileSize, gamePanel.tileSize);
		attackValue = 3;
		price = 100;
		attackArea.width = 36;
		attackArea.height = 36;
		description = "[" + name + "] \nFinely made sword!" ;
		knockBackPower = 3;
	}

}
