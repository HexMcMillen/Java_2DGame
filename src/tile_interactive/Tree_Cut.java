package tile_interactive;

import main.GamePanel;

public class Tree_Cut extends InteractiveTile{

	GamePanel gamePanel;

	public Tree_Cut(GamePanel gamePanel, int col, int row) {
		super(gamePanel);
		this.gamePanel = gamePanel;
		
		this.worldX = gamePanel.tileSize * col;
		this.worldY = gamePanel.tileSize * row;
		
		down1 = setup("/tiles_interactive/Tree_Cut", gamePanel.tileSize, gamePanel.tileSize); 
		
		solidArea.x = 0;
		solidArea.y = 0;
		solidArea.width = 0;
		solidArea.height = 0;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
	}
	
}
