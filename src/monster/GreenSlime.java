package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.Coin_Bronze;
import object.Health;
import object.Mana;
import object.Rock;

public class GreenSlime extends Entity {

	GamePanel gamePanel;

	public GreenSlime(GamePanel gamePanel) {
		super(gamePanel);
		this.gamePanel = gamePanel;

		type = type_monster;
		name = "Purple Slime";
		defaultSpeed = 1;
		speed = defaultSpeed;
		maxLife = 4;
		life = maxLife;
		attack = 5;
		defense = 0;
		exp = 2;
		projectile = new Rock(gamePanel);

		// GETTING THE SOLID AREA || HITBOX
		solidArea.x = 4;
		solidArea.y = 18;
		solidArea.width = 46;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

		getImage();

	}

	public void getImage() { // SETTING AN IMAGE TO EACH DIRECTION

		up1 = setup("/monster/purpleslime", gamePanel.tileSize, gamePanel.tileSize);
		up2 = setup("/monster/purpleslime2", gamePanel.tileSize, gamePanel.tileSize);
		down1 = setup("/monster/purpleslime", gamePanel.tileSize, gamePanel.tileSize);
		down2 = setup("/monster/purpleslime2", gamePanel.tileSize, gamePanel.tileSize);
		right1 = setup("/monster/purpleslime", gamePanel.tileSize, gamePanel.tileSize);
		right2 = setup("/monster/purpleslime2", gamePanel.tileSize, gamePanel.tileSize);
		left1 = setup("/monster/purpleslime", gamePanel.tileSize, gamePanel.tileSize);
		left2 = setup("/monster/purpleslime2", gamePanel.tileSize, gamePanel.tileSize);
	}
	public void update() { // WHEN PLAYER GETS WITHIN RANGE 
		
		super.update();
		
		int xDistance = Math.abs(worldX - gamePanel.player.worldX);
		int yDistance = Math.abs(worldY - gamePanel.player.worldY);
		int tileDistance = (xDistance + yDistance) /gamePanel.tileSize;
		
		if(onPath == false && tileDistance < 5) {
			
			int i = new Random() .nextInt(100)+1;
			if(i > 50) {
				onPath = true;
			}
		}
//		if(onPath == true && tileDistance >20) {
//			onPath = false;
//		}
	}
	public void setAction() { // GIVING MOVEMENT

		if (onPath == true) {

			// TO FOLLOW THE PLAYER CHANGE THEM TO THE PLAYERS POSITIONS
			int goalCol = (gamePanel.player.worldX + gamePanel.player.solidArea.x) / gamePanel.tileSize;
			int goalRow = (gamePanel.player.worldY + gamePanel.player.solidArea.y) / gamePanel.tileSize;

			searchPath(goalCol, goalRow);
			
			// PROJECTILE
//			int i = new Random().nextInt(100) + 1;
//			if (i > 197 && projectile.alive == false && shotAvailableCounter == 30) {
//				
//				projectile.set(worldX, worldY, direction, true, this);
//				gamePanel.projectileList.add(projectile);
//				shotAvailableCounter = 0;
//			}
		} else {

			actionLockCounter++;

			if (actionLockCounter == 120) {

				Random random = new Random(); // generate a random number from 1 to 100
				int i = random.nextInt(100) + 1;

				if (i <= 25) {
					direction = "up";
				}
				if (i > 25 && i <= 50) {
					direction = "down";
				}
				if (i > 50 && i <= 75) {
					direction = "left";
				}
				if (i > 75 && i <= 100) {
					direction = "right";
				}

				actionLockCounter = 0;
			}
		}

		
	}

	public void damageReaction() { // MOVE IN THE DIRECTION OF THE PLAYER WHEN DAMAGED

		actionLockCounter = 0;
	//	direction = gamePanel.player.direction;
		onPath = true;
	}

	public void checkDrop() { // CALLED WHEN MOSTER DEATH

		int i = new Random().nextInt(100) + 1; // CASE A DIE TO FIND WHAT TO DROP

		// SET MONSTER DROP
		if (i < 50) {
			dropItem(new Coin_Bronze(gamePanel));
		}
		if (i >= 50 && i < 75) {
			dropItem(new Health(gamePanel));
		}
		if (i >= 75) {
			dropItem(new Mana(gamePanel));
		}
	}

}
