package object;

import entity.Entity;
import main.GamePanel;

public class Lantern extends Entity{

	public Lantern(GamePanel gamePanel) {
		super(gamePanel);
		
		type = type_light;
		name = "Lantern";
		price = 200;
		lightRadius = 250;
		
		down1 = setup("/objects/Lantern", gamePanel.tileSize, gamePanel.tileSize);
		description = "[" + name + "] \nCan illuminate your surroundings \nin dark areas!";
	}

}
