package main;

import entity.Entity;

public class CollisionChecker {
	
	private GamePanel gamePanel;

	public CollisionChecker(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public void checkTile(Entity entity) { //Use entity to check collision for all moving things, not just the player || used to check the boundaries where the collision would happen on the rectangle
		
		int entityLeftWorldX = entity.worldX + entity.solidArea.x; //Left
		int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width; //Right
		int entityTopWorldY = entity.worldY + entity.solidArea.y; //Top
		int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;//Bottom
		
		int entityLeftCol = entityLeftWorldX/gamePanel.tileSize;
		int entityRightCol = entityRightWorldX/gamePanel.tileSize;
		int entityTopRow = entityTopWorldY/gamePanel.tileSize;
		int entityBottomRow = entityBottomWorldY/gamePanel.tileSize;
		
		int tileNumber1, tileNumber2;
		
		//Check to see if only specific tiles on the character sprite are coming into contact with the collision tile 
		
		switch (entity.direction) { //find out what tiles the player is trying to step into
			case "up":
				entityTopRow = (entityTopWorldY - entity.speed)/gamePanel.tileSize;
				tileNumber1 = gamePanel.tileManager.mapTileNumber[gamePanel.currentMap][entityLeftCol][entityTopRow];
				tileNumber2 = gamePanel.tileManager.mapTileNumber[gamePanel.currentMap][entityRightCol][entityTopRow];
				if (gamePanel.tileManager.tile[tileNumber1].collision == true || gamePanel.tileManager.tile[tileNumber2].collision == true) {
					
					entity.collisionOn = true;
				}
				break;
			case "down":
				entityBottomRow = (entityBottomWorldY + entity.speed)/gamePanel.tileSize;
				tileNumber1 = gamePanel.tileManager.mapTileNumber[gamePanel.currentMap][entityLeftCol][entityBottomRow];
				tileNumber2 = gamePanel.tileManager.mapTileNumber[gamePanel.currentMap][entityRightCol][entityBottomRow];
				if (gamePanel.tileManager.tile[tileNumber1].collision == true || gamePanel.tileManager.tile[tileNumber2].collision == true) {
					
					entity.collisionOn = true;
				}
				break;
			case "right":
				entityRightCol = (entityRightWorldX + entity.speed)/gamePanel.tileSize;
				tileNumber1 = gamePanel.tileManager.mapTileNumber[gamePanel.currentMap][entityRightCol][entityTopRow];
				tileNumber2 = gamePanel.tileManager.mapTileNumber[gamePanel.currentMap][entityRightCol][entityBottomRow];
				if (gamePanel.tileManager.tile[tileNumber1].collision == true || gamePanel.tileManager.tile[tileNumber2].collision == true) {
					
					entity.collisionOn = true;
				}
				break;
			case "left":
				entityLeftCol = (entityLeftWorldX - entity.speed)/gamePanel.tileSize;
				tileNumber1 = gamePanel.tileManager.mapTileNumber[gamePanel.currentMap][entityLeftCol][entityTopRow];
				tileNumber2 = gamePanel.tileManager.mapTileNumber[gamePanel.currentMap][entityLeftCol][entityBottomRow];
				if (gamePanel.tileManager.tile[tileNumber1].collision == true || gamePanel.tileManager.tile[tileNumber2].collision == true) {
					
					entity.collisionOn = true;
				}
				break;
		}
	}
	
	public int checkObject(Entity entity, boolean player) {//Check if player is hitting the area of the object || if he is, return the index of the object
		
		int index = 999;
		
		for (int i = 0; i < gamePanel.obj[1].length; i++) {//scan the object array
			if (gamePanel.obj[gamePanel.currentMap][i] != null) { //check if there is an object in index [i]
			
				//Get entity solid area position
				entity.solidArea.x = entity.worldX + entity.solidArea.x;
				entity.solidArea.y = entity.worldY + entity.solidArea.y;
				
				//Get the objects solid area position
				gamePanel.obj[gamePanel.currentMap][i].solidArea.x = gamePanel.obj[gamePanel.currentMap][i].worldX + gamePanel.obj[gamePanel.currentMap][i].solidArea.x;
				gamePanel.obj[gamePanel.currentMap][i].solidArea.y = gamePanel.obj[gamePanel.currentMap][i].worldY + gamePanel.obj[gamePanel.currentMap][i].solidArea.y;
				
				switch(entity.direction) {
					case "up": entity.solidArea.y -= entity.speed; break;
					case "down": entity.solidArea.y += entity.speed; break;
					case "right": entity.solidArea.x += entity.speed; break;
					case "left": entity.solidArea.x -= entity.speed; break;
				}
				
				if(entity.solidArea.intersects(gamePanel.obj[gamePanel.currentMap][i].solidArea)) { //checks if the two rectangles are touching one another || if entity is touching the object
					if (gamePanel.obj[gamePanel.currentMap][i].collision == true) { 
						entity.collisionOn = true;
					}
					if (player == true) { 
						index = i;
					}
				}
				
				entity.solidArea.x = entity.solidAreaDefaultX; //reset the amounts after the switch so the numbers dont continue to increase
				entity.solidArea.y = entity.solidAreaDefaultY;
				
				gamePanel.obj[gamePanel.currentMap][i].solidArea.x = gamePanel.obj[gamePanel.currentMap][i].solidAreaDefaultX;
				gamePanel.obj[gamePanel.currentMap][i].solidArea.y = gamePanel.obj[gamePanel.currentMap][i].solidAreaDefaultY;
			}
		}
		
		return index;
		
	}
	
	//NPC OR MONSTER COLLISION
	public int checkEntity(Entity entity, Entity[][] target) {
		
		int index = 999;
		
		for (int i = 0; i < target[1].length; i++) {//scan the object array
			
			if (target[gamePanel.currentMap][i] != null) { //check if there is an object in index [i]
			
				//Get entity solid area position
				entity.solidArea.x = entity.worldX + entity.solidArea.x;
				entity.solidArea.y = entity.worldY + entity.solidArea.y;
				
				//Get the objects solid area position
				target[gamePanel.currentMap][i].solidArea.x = target[gamePanel.currentMap][i].worldX + target[gamePanel.currentMap][i].solidArea.x;
				target[gamePanel.currentMap][i].solidArea.y = target[gamePanel.currentMap][i].worldY + target[gamePanel.currentMap][i].solidArea.y;
				
				switch(entity.direction) {
					case "up": entity.solidArea.y -= entity.speed; break;
					case "down": entity.solidArea.y += entity.speed; break;
					case "right": entity.solidArea.x += entity.speed; break;
					case "left": entity.solidArea.x -= entity.speed; break;
				}
				
				if(entity.solidArea.intersects(target[gamePanel.currentMap][i].solidArea)) { //checks if the two rectangles are touching one another || if entity is touching the object
					if (target[gamePanel.currentMap][i] != entity) {
						entity.collisionOn = true;
						index = i;						
					}
				}
				
				entity.solidArea.x = entity.solidAreaDefaultX; //reset the amounts after the switch so the numbers dont continue to increase
				entity.solidArea.y = entity.solidAreaDefaultY;
				
				target[gamePanel.currentMap][i].solidArea.x = target[gamePanel.currentMap][i].solidAreaDefaultX;
				target[gamePanel.currentMap][i].solidArea.y = target[gamePanel.currentMap][i].solidAreaDefaultY;
			}
		}
		
		return index;
	}
	
	public boolean checkPlayer (Entity entity) {
		
		boolean contactPlayer = false;
		
		//Get entity solid area position
		entity.solidArea.x = entity.worldX + entity.solidArea.x;
		entity.solidArea.y = entity.worldY + entity.solidArea.y;
		
		//Get the objects solid area position
		gamePanel.player.solidArea.x = gamePanel.player.worldX + gamePanel.player.solidArea.x;
		gamePanel.player.solidArea.y = gamePanel.player.worldY + gamePanel.player.solidArea.y;
		
		switch(entity.direction) {
			case "up": entity.solidArea.y -= entity.speed; break;
			case "down": entity.solidArea.y += entity.speed; break;
			case "right": entity.solidArea.x += entity.speed; break;
			case "left": entity.solidArea.x -= entity.speed; break;
		}
		
		if(entity.solidArea.intersects(gamePanel.player.solidArea)) { //checks if the two rectangles are touching one another || if entity is touching the object
			entity.collisionOn = true;
			contactPlayer = true;
		}
		
		entity.solidArea.x = entity.solidAreaDefaultX; //reset the amounts after the switch so the numbers dont continue to increase
		entity.solidArea.y = entity.solidAreaDefaultY;
		
		gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
		gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
		
		return contactPlayer;
	}

}
