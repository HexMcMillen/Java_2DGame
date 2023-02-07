package main;

import entity.Entity;

public class EventHandler {
	
	GamePanel gamePanel;
	EventRect eventRect[][][];
	
	//EVENT WONT HAPPEN AGAIN UNTIL PLAYER MOVES AWAY BY ONE TILE
	int previousEventX, previousEventY;
	boolean canTouchEvent = true;
	int tempMap, tempCol, tempRow;
	
	public EventHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		
		eventRect = new EventRect[gamePanel.maxMap][gamePanel.maxWorldCol][gamePanel.maxWorldRow];
		
		int map = 0;
		int col = 0;
		int row = 0;
		while (map < gamePanel.maxMap && col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
			eventRect[map][col][row] = new EventRect();
			eventRect[map][col][row].x = 23;
			eventRect[map][col][row].y = 23;
			eventRect[map][col][row].width = 2;
			eventRect[map][col][row].height = 2;
			eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
			eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;
			
			col++;
			if (col == gamePanel.maxWorldCol) {
				col = 0;
				row++;
				
				if (row == gamePanel.maxWorldRow) {
					row = 0;
					map++;
				}
			}
		}
		
		
	}
	
	public void checkEvent() {
		
		//CHECK IF PLAYER CHARACTER IS MORE THAT ONE TILE AWAY FROM LAST EVENT
		int xDistance = Math.abs(gamePanel.player.worldX - previousEventX);
		int yDistance = Math.abs(gamePanel.player.worldY - previousEventY);
		int distance = Math.max(xDistance, yDistance);
		if (distance > gamePanel.tileSize) {
			canTouchEvent = true;
		}
		
		if (canTouchEvent == true) {
			if(hit(0, 22, 25, "down") == true) {healingPool(gamePanel.dialogueState);}
			else if(hit(0, 16, 6, "any") == true) {teleportMap(1, 22, 28);} //method(map going to, coorditnates to start that map in
			else if(hit(1, 22, 28, "any") == true) {teleportMap(0, 16, 6);}
			else if(hit(1, 24, 25, "up") == true) {speak(gamePanel.npc[1][0]);}
		}
		
	}
	
	public boolean hit(int map, int col, int row, String reqDirection) {
		
		boolean hit = false;
		
		if (map == gamePanel.currentMap) {
			
			//PLAYER SOLIDAREA POSITIONS
			gamePanel.player.solidArea.x = gamePanel.player.worldX + gamePanel.player.solidArea.x;
			gamePanel.player.solidArea.y = gamePanel.player.worldY + gamePanel.player.solidArea.y;
			
			//EVENT SOLIDAREA POSITIONS
			eventRect[map][col][row].x = col*gamePanel.tileSize + eventRect[map][col][row].x;
			eventRect[map][col][row].y = row*gamePanel.tileSize + eventRect[map][col][row].y;
			
			if(gamePanel.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone == false) { //if the player area intersects with the event area then a hit happens || event only happens when the event boolean is false
				if (gamePanel.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
					hit = true;
					
					//USED TO CHECK DISTANCE FROM PLAYER AND LAST EVENT
					previousEventX = gamePanel.player.worldX;
					previousEventY = gamePanel.player.worldY;
				}
			}
			
			//RESET SOLIDAREA AFTER THE COLLISION IS CHECKED
			gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
			gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
			eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
			eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
			
		}
		return hit;
	}
	
	public void damagePit(int gameState) { 
		
		gamePanel.gameState = gameState;
		gamePanel.ui.currentDialogue = "You fall into a pit!";
		gamePanel.player.life -= 1;
//		eventRect[col][row].eventDone = true; //This is to make a one time only event
		
		canTouchEvent = false;
	}
	
	public void healingPool(int gameState) {
		
		if (gamePanel.keyHandler.enterPressed == true) {
			gamePanel.gameState = gameState;
			gamePanel.player.attackCanceled = true;
			gamePanel.ui.currentDialogue = "You drink the water. \nYour life and Mana have been recovered!";
			gamePanel.player.life = gamePanel.player.maxLife; //HEAL PLAYER LIFE
			gamePanel.player.mana = gamePanel.player.maxMana; //RESET MANA USAGE
			gamePanel.assetSetter.setMonster(); //RESET MONSTERS WHEN USING THIS
			
			gamePanel.saveLoad.save(); //USE THIS TO SAVE WHEN SELECTING AN EVENT
		}	
	}
	
	public void teleport(int gameState) {
		
		gamePanel.gameState = gameState;
		gamePanel.ui.currentDialogue = "Teleport!";
		gamePanel.player.worldX = gamePanel.tileSize*43;
		gamePanel.player.worldY = gamePanel.tileSize*21;	
	}
	
	public void teleportMap(int map, int col, int row) {
		
		gamePanel.gameState = gamePanel.transitionState;
		tempMap = map;
		tempCol = col;
		tempRow = row;
		
		canTouchEvent = false; //EVENT WONT HAPPEN AGAIN UNTIL THE PLAYER MOVES A TILE LENGTH AWAY
		//TODO: MAKE A SOUND FOR GOING INTO PLACES
		
	}
	
	public void speak(Entity entity) {
		
		if (gamePanel.keyHandler.enterPressed == true) { //IF YOU PRESS ENTER AT THE EVENT LOCATION
			gamePanel.gameState = gamePanel.dialogueState; //CHANGE GAME STATE
			gamePanel.player.attackCanceled = true; //CANCEL THE ATTACK ANIMATION
			entity.speak(); //CALL THE SPEAK METHOD OF THE ENTITY
		}
	}
}
