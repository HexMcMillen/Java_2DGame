package object;

import entity.Entity;
import main.GamePanel;

public class Sword_Normal extends Entity{

	public Sword_Normal(GamePanel gamePanel) {
		super(gamePanel);
		
		type = type_sword;
		name = "Wooden Sword";
		down1 = setup("/objects/woodsword", gamePanel.tileSize, gamePanel.tileSize);
		attackValue = 1;
		price = 25;
		attackArea.width = 30;
		attackArea.height = 30;
		description = "[" + name + "] \nCrudely made sword! \nMight as well be using a stick!" ;
	}

}
