package object;

import entity.Entity;
import main.GamePanel;

public class Shield_Wood extends Entity{

	public Shield_Wood(GamePanel gamePanel) {
		super(gamePanel);
		
		type = type_shield;
		name = "Wooden Shield";
		down1 = setup("/objects/shield", gamePanel.tileSize, gamePanel.tileSize);
		defenseValue = 1;
		price = 25;
		description = "[" + name + "] \nA crudely made wooden shield! \nKinda hurts your hands to hold it!";
	}

}
