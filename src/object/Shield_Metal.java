package object;

import entity.Entity;
import main.GamePanel;

public class Shield_Metal extends Entity{

	public Shield_Metal(GamePanel gamePanel) {
		super(gamePanel);

		type = type_shield;
		name = "Metal Shield";
		down1 = setup("/objects/metalshield", gamePanel.tileSize, gamePanel.tileSize);
		defenseValue = 3;
		price = 100;
		description = "[" + name + "] \nA finely made shield!";
	}

}
