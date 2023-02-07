package object;

import entity.Entity;
import main.GamePanel;

public class Axe extends Entity{

	public Axe(GamePanel gamePanel) {
		super(gamePanel);
		
		type = type_axe;
		name = "Axe";
		down1 = setup("/objects/Axe", gamePanel.tileSize, gamePanel.tileSize);
		attackValue = 2;
		attackArea.width = 30;
		attackArea.height = 30;
		description = "[" + name + "] \nMay be able to cut down trees!" ;
		price = 50;
		knockBackPower = 10;
	}

}
