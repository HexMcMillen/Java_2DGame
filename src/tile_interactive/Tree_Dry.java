package tile_interactive;

import java.awt.Color;

import entity.Entity;
import main.GamePanel;

public class Tree_Dry extends InteractiveTile{
	
	GamePanel gamePanel;

	public Tree_Dry(GamePanel gamePanel) {
		super(gamePanel);
		this.gamePanel = gamePanel;
		
		down1 = setup("/tiles_interactive/Tree_Dry", gamePanel.tileSize, gamePanel.tileSize);
		destructible = true;
		life = 3; //HOW MANY HITS IT TAKES TO CUT DOWN THE TREE
	}
	
	public boolean correctItem(Entity entity) {
		boolean correctItem = false;
		
		if (entity.currentWeapon.type == type_axe ) {
			correctItem = true;
		}
		
		return correctItem;
	}
	
	public void playSe() {
		//TODO: PLAY SOUND EFFECT OF CUTTING TREE
	}
	
	public InteractiveTile getDestroyedForm() {
		InteractiveTile tile = new Tree_Cut(gamePanel, worldX/gamePanel.tileSize, worldY/gamePanel.tileSize);
		return tile;
	}
	
	public Color getParticleColor() {
		Color color = new Color(65, 50, 30); //BROWN COLOR FOR THE TREE
		return color;
	}
	
	public int getParticleSize() {
		int size = 6; //PARTICLE SIZE WILL BE 6 PIXELS
		return size;
	}
	
	public int getParticleSpeed() {
		int speed = 1;
		return speed;
	}
	
	public int getParticleMaxLife() { //HOW LONG THE PARTICLES LAST
		int maxLife = 20;
		return maxLife;
	}
	

}
