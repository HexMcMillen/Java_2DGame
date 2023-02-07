package entity;


import java.util.Random;

import main.GamePanel;


public class NPC_Ninja extends Entity {
	
	public NPC_Ninja(GamePanel gamePanel) {
		
		super(gamePanel);
		
		direction = "down";
		speed = 3;
		
		getImage();
		setDialogue();
	}
	
	public void getImage() {
		
		up1 = setup("/npc/ninja_up1", gamePanel.tileSize, gamePanel.tileSize);
		up2 = setup("/npc/ninja_up2", gamePanel.tileSize, gamePanel.tileSize);
		right1 = setup("/npc/ninja_right1", gamePanel.tileSize, gamePanel.tileSize);
		right2 = setup("/npc/ninja_right2", gamePanel.tileSize, gamePanel.tileSize);
		down1 = setup("/npc/ninja_down1", gamePanel.tileSize, gamePanel.tileSize);
		down2= setup("/npc/ninja_down2", gamePanel.tileSize, gamePanel.tileSize);
		left1 = setup("/npc/ninja_left1", gamePanel.tileSize, gamePanel.tileSize);
		left2 = setup("/npc/ninja_left2", gamePanel.tileSize, gamePanel.tileSize);
	}
	
	public void setDialogue() { //store dialogue text to be said
		
		dialogues[0] = "Hello, lad. \nFollow me to my safe space!";
	}
	
	public void setAction() { //set the characters behavior
		
		if (onPath == true) {
			
			//SET THE PARAMETERS FOR WHERE THEY NEED TO WALK TO
//			int goalCol = 39;
//			int goalRow = 4;
			
			//TO FOLLOW THE PLAYER CHANGE THEM TO THE PLAYERS POSITIONS
			int goalCol = (gamePanel.player.worldX + gamePanel.player.solidArea.x)/gamePanel.tileSize;
			int goalRow = (gamePanel.player.worldY + gamePanel.player.solidArea.y)/gamePanel.tileSize;
			
			searchPath(goalCol, goalRow);
		}
		else {
			
			actionLockCounter++;
			
			if(actionLockCounter == 120) {
				
				Random random = new Random(); //generate a random number from 1 to 100
				int i = random.nextInt(100)+1;
				
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
	
	public void speak() {
		
		super.speak();
		
		speed = (int)(gamePanel.player.speed-1); //CHANGE THIS TO PLAYER SPEED TO MAKE THEM WALK AS FAST AS YOU WHEN FOLLOWING MINUS SMALL AMOUNT SO YOU CANT GET STUCK SOMEWHERE
		onPath = true; //IF YOU SPEAK TO THEM, ONPATH BECOMES TRUE
	}
}
