package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.Bed;
import object.FireBall;
import object.KeyGold;
import object.Shield_Wood;
import object.Sword_Normal;

public class Player extends Entity{
	
	//Impliments
	KeyHandler keyHandler;
	
	public final int screenX;
	public final int screenY; // indicates where we draw player on the screen || these dont change so that your character stays in the middle of the screen at all times
	
	int standCounter = 0;
	public boolean attackCanceled = false;
	public boolean lightUpdated = false;
	

	public Player (GamePanel gamePanel, KeyHandler keyHandler) {
		
		super(gamePanel);
		this.keyHandler = keyHandler;
		
		screenX = gamePanel.screenWidth/2 - (gamePanel.tileSize/2);
		screenY = gamePanel.screenHeight/2 - (gamePanel.tileSize/2);
		
		solidArea = new Rectangle(8, 16, 32, 32); //(x, y, width, height) || parameters where the collision works
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		
		setDefaultValues();	
	}
	
	public void setDefaultValues() {
		
		worldX = gamePanel.tileSize * 22; //this is player starting position on the world map
		worldY = gamePanel.tileSize * 27;
		gamePanel.currentMap = 1;
		
		defaultSpeed = 4; //ADD THIS TO WHATEVER YOU WANT TO GIVE KNOCKBACK EFFECT
		speed = defaultSpeed;
		direction = "down";
		
		//PLAYER STATUS || Each Full Heart represents 2 lives
		level = 1;
		maxLife = 6;
		life = maxLife;
		maxMana = 4;
		mana = maxMana;
		ammo = 10; //AMMO FOR PROJECTILES || NOT MANA
		strength = 1; //MORE STR = MORE DAMAGE
		dexterity = 1; //MORE DEX = LESS DAMAGE TAKEN
		exp = 0;
		nextLevelExp = 5;
		coin = 500;
		currentWeapon = new Sword_Normal(gamePanel);
		currentShield = new Shield_Wood(gamePanel);
		currentLight = null;
		projectile = new FireBall(gamePanel);
		//INSTANTIATE A NEW OBJECT/PROJECTILE FOR THROWING OR SHOOTING
		attack = getAttack(); //STR + WEAPON
		defense = getDefense(); //DEX + SHIELD
		
		getPlayerImage();
		getPlayerAttackImage();
		setItems();
	}
	
	public void setDefaultPositions() {//MODIFY DEPENDING ON HOW THE GAME WORKS
		
		worldX = gamePanel.tileSize * 5;
		worldY = gamePanel.tileSize * 6;
		direction = "down";
	}
	
	public void restoreStatus() {//MODIFY DEPENDING ON HOW THE GAME WORKS
		
		life = maxLife;
		mana = maxMana;
		invincible = false;
		attacking = false;
		lightUpdated = true;
	}
	
	public void setItems() {//CAN MANUALLY ADD OBJECTS TO THE PLAYER INVENTORY
		
		inventory.clear();
		inventory.add(currentWeapon);
		inventory.add(currentShield);
		inventory.add(new KeyGold(gamePanel));
		
	}
	
	public int getAttack() {
		attackArea = currentWeapon.attackArea; //CHANGES DEPENDING ON THE WEAPON
		return attack = strength * currentWeapon.attackValue;
	}
	
	public int getCurrentWeaponSlot() {
		int currentWeaponSlot = 0;
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i) == currentWeapon) {
				currentWeaponSlot = i;
			}
		}
		return currentWeaponSlot;
	}
	
	public int getCurrentShieldSlot() {
		int currentShieldSlot = 0;
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i) == currentShield) {
				currentShieldSlot = i;
			}
		}
		return currentShieldSlot;
	}
	
	public int getDefense() {
		return defense = dexterity * currentShield.defenseValue;
	}
	
	public void getPlayerImage() {
			
			up1 = setup("/player/boys_up_1", gamePanel.tileSize, gamePanel.tileSize);
			up2 = setup("/player/boys_up_2", gamePanel.tileSize, gamePanel.tileSize);
			right1 = setup("/player/boys_right_1", gamePanel.tileSize, gamePanel.tileSize);
			right2 = setup("/player/boys_right_2", gamePanel.tileSize, gamePanel.tileSize);
			down1 = setup("/player/boys_down_1", gamePanel.tileSize, gamePanel.tileSize);
			down2= setup("/player/boys_down_2", gamePanel.tileSize, gamePanel.tileSize);
			left1 = setup("/player/boys_left_1", gamePanel.tileSize, gamePanel.tileSize);
			left2 = setup("/player/boys_left_2", gamePanel.tileSize, gamePanel.tileSize);
	}
	
	public void getSleepingImage(BufferedImage image) {
		
		up1 = image;
		up2 = image;
		right1 = image;
		right2 = image;
		down1 = image;
		down2= image;
		left1 = image;
		left2 = image;
	}
	
	public void getPlayerAttackImage() { //ADD ADDITIONAL IF STATEMENTS AND SPRITES FOR DIFFERENT WEAPON TYPES
		
		if (currentWeapon.type == type_sword) {
			
			attackUp1 = setup("/player/attack_up1", gamePanel.tileSize, gamePanel.tileSize*2);
			attackUp2 = setup("/player/attack_up2", gamePanel.tileSize, gamePanel.tileSize*2);
			attackRight1 = setup("/player/attack_right1", gamePanel.tileSize*2, gamePanel.tileSize);
			attackRight2 = setup("/player/attack_right2", gamePanel.tileSize*2, gamePanel.tileSize);
			attackDown1 = setup("/player/attack_down1", gamePanel.tileSize, gamePanel.tileSize*2);
			attackDown2= setup("/player/attack_down2", gamePanel.tileSize, gamePanel.tileSize*2);
			attackLeft1 = setup("/player/attack_left1", gamePanel.tileSize*2, gamePanel.tileSize);
			attackLeft2 = setup("/player/attack_left2", gamePanel.tileSize*2, gamePanel.tileSize);
		}
		
		//TODO: ADD DIFFERENT WEAPONS AND SPRITES TO GO ALONG WITH THEM
	}
	
	public void update() {
		
		if (attacking == true) {
			attackingMethod();
		}
		
		else if(keyHandler.upPressed == true || keyHandler.downPressed == true || keyHandler.leftPressed == true || keyHandler.rightPressed == true || keyHandler.enterPressed == true) {//if a key is pressed then the counter will increase and the player will move else the character will stand still
			
			if (keyHandler.upPressed ==  true) { //if W is pressed the player will go up 4 pixels
				direction = "up";
				
			} 
			else if (keyHandler.downPressed == true) {// if S is pressed the player will go down 4 pixels
				direction = "down";
				
			}
			else if (keyHandler.leftPressed == true) {// if A is pressed the player will go left 4 pixels
				direction = "left";
				
			}
			else if (keyHandler.rightPressed == true) {// if D is pressed the player will go right 4 pixels
				direction = "right";
				
			}
			
			//CHECK TILE COLLISION
			collisionOn = false;
			gamePanel.collisionChecker.checkTile(this); 
			
			//CHECK OBJECT COLLISION
			int objIndex = gamePanel.collisionChecker.checkObject(this, true);
			pickUpObject(objIndex);
			
			//CHECK NPC COLLISION
			int npcIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.npc);
			interactNPC(npcIndex);
			
			//CHECK MONSTER COLLISION
			int monsterIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.monster);
			contactMonster(monsterIndex);
			
			//CHECK INTERACTIVE TILE COLLISION
			int interactTileIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.interactTile);
			
			
			//CHECK EVENT
			gamePanel.eventHandler.checkEvent();

			//If collision is false, player can move
			if (collisionOn == false && keyHandler.enterPressed == false) {
				
				switch(direction) {
					case "up": worldY -= speed; break;
					case "down": worldY += speed; break;
					case "right": worldX += speed; break;
					case "left": worldX -= speed; break;
				}
			}
			
			if(keyHandler.enterPressed == true && attackCanceled == false) {
				gamePanel.playSE(8);
				attacking = true;
				spriteCounter = 0;
			}
			
			attackCanceled = false;
			gamePanel.keyHandler.enterPressed = false;
			
			spriteCounter++;
			if (spriteCounter > 12) { //player image changes every 12 frames
				if (spriteNum == 1) {
					spriteNum = 2;
				} else if (spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}
		}
		else { //standstill || releasing the key brings the sprite back to the standstill position
			standCounter++;
			
			if (standCounter == 20) {
				spriteNum = 1;
				standCounter = 0;
			}
		}
		
		//PROJECTILE
		if (gamePanel.keyHandler.shotKeyPressed == true && projectile.alive == false && shotAvailableCounter == 30 && projectile.haveResource(this) == true) {
			
			//SET DEFAULT COORDINATES, DIRECTION, AND USER
			projectile.set(worldX, worldY, direction, true, this);
			
			//USER RESOURCE
			projectile.useResource(this);
			
			//ADD TO THE ARRAY LIST
			gamePanel.projectileList.add(projectile);
			//TODO: FIREBALL SOUND
			
			//RESET COUNTER TO START TIME UNTIL ANOTHER FIREBALL CAN BE SHOT
			shotAvailableCounter = 0;
		}
		
		//HITFRAMES FOR WHEN YOU CAN TAKE DAMAGE AGAIN
		if (invincible == true) {
			invincibleCounter++;
			if (invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		
		//TIME NEEDED TO SHOOT ANOTHER MAGIC SPELL
		if (shotAvailableCounter < 30) {
			shotAvailableCounter++;
		}
		
		//STOPPING LIFE TOTAL FROM GOING OVER MAX
		if (life > maxLife) {
			life = maxLife;
		}
		
		//STOPPING MANA TOTAL FROM GOING OVER MAX
		if (mana > maxMana) {
			mana = maxMana;
		}
		
		//GAME OVER
		if (life <= 0) {
			gamePanel.gameState = gamePanel.gameOverState;
			gamePanel.ui.commandNumber = -1;
			gamePanel.stopMusic();
			//TODO: PLAY GAME OVER SOUND
		}
	}
	
	public void attackingMethod() {
		
		spriteCounter++;
		
		if (spriteCounter <= 5) {
			spriteNum = 1;
		}
		if (spriteCounter > 5 && spriteCounter <= 25) {
			spriteNum = 2;
			
			//SAVE THE CURRENT VARIABLES
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			
			//ADJUST PLAYER VARIABLES FOR ATTACK AREA 
			switch (direction) {
				case "up": worldY -= attackArea.height; break;
				case "down": worldY += attackArea.height; break;
				case "right": worldX += attackArea.width; break;
				case "left": worldX -= attackArea.width; break;
			}
			
			//ATTACK AREA BECOMES SOLID AREA
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			
			//CHECK MONSTER COLLISION WITH UPDATED SOLID AREA
			int monsterIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.monster);
			damageMonster(monsterIndex, attack, currentWeapon.knockBackPower);
			
			//INTERACTIVE TILES
			int interactTileIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.interactTile);
			damageInteractiveTile(interactTileIndex);
			
			//AFTER CHECKING COLLISION, RESTORE VALUES
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;
		}
		if (spriteCounter > 25) {
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
		}
	}
	
	public void pickUpObject(int i) {
		if( i != 999) {
			
			//PICKUP ITEMS
			if (gamePanel.obj[gamePanel.currentMap][i].type == type_pickUpOnly) {
				
				gamePanel.obj[gamePanel.currentMap][i].use(this); //UPON PICKUP THE USE METHOD IN THE CLASS IS USED
				gamePanel.obj[gamePanel.currentMap][i] = null; //OBJECT DISAPPEARS UPON PICKUP
				
			}
			
			//OBSTACLES
			else if (gamePanel.obj[gamePanel.currentMap][i].type == type_obstacle) {
				if (keyHandler.enterPressed == true) {
					attackCanceled = true;
					gamePanel.obj[gamePanel.currentMap][i].interact();
				}
			}
			
			//INVENTORY ITEMS
			else {
				String text;
				
				if (canObtainItem(gamePanel.obj[gamePanel.currentMap][i]) == true) {
					//TODO: ADD SOUND EFFECT FOR PICKUP
					text = "Picked up a " + gamePanel.obj[gamePanel.currentMap][i].name + "!";
				}
				else {
					text = "You cannot carry any more!";
				}
				gamePanel.ui.addMessage(text);
				gamePanel.obj[gamePanel.currentMap][i] = null;
			}
		}
	}
	
	public void interactNPC(int i) {
		
		if (gamePanel.keyHandler.enterPressed == true) {
			if( i != 999) {
				attackCanceled = true;
				gamePanel.gameState = gamePanel.dialogueState;
				gamePanel.npc[gamePanel.currentMap][i].speak();
			}
			
		}
	}
	
	public void contactMonster(int i) {//PlAYER LOSES HEALTH WHEN TOUCHING A MONSTER
		if( i != 999) {
			
			if (invincible ==  false  && gamePanel.monster[gamePanel.currentMap][i].dying == false) {
				gamePanel.playSE(7);
				
				int damage = gamePanel.monster[gamePanel.currentMap][i].attack - defense;
				if (damage < 0) {
					damage = 0;
				}
				
				life -= damage;
				invincible = true;
				
			}
		}
	}
	
	public void damageMonster(int i, int attack, int knockBackPower) {
		if (i != 999) {
			if (gamePanel.monster[gamePanel.currentMap][i].invincible == false) {
				gamePanel.playSE(6);
				
				if (knockBackPower > 0) {
					knockBack(gamePanel.monster[gamePanel.currentMap][i], knockBackPower);					
				}
				
				int damage = attack - gamePanel.monster[gamePanel.currentMap][i].defense;
				if (damage < 0) {
					damage = 0;
				}
				
				gamePanel.monster[gamePanel.currentMap][i].life -= damage;
				gamePanel.ui.addMessage(damage + " damage!");
				
				gamePanel.monster[gamePanel.currentMap][i].invincible = true;
				gamePanel.monster[gamePanel.currentMap][i].damageReaction();
				
				if (gamePanel.monster[gamePanel.currentMap][i].life <= 0) { //IF MONSTER LIFE = 0 REMOVE MONSTERwds
					gamePanel.monster[gamePanel.currentMap][i].dying = true;
					gamePanel.ui.addMessage("Killed " + gamePanel.monster[gamePanel.currentMap][i].name);
					gamePanel.ui.addMessage("Gained " + gamePanel.monster[gamePanel.currentMap][i].exp + " exp");
					exp += gamePanel.monster[gamePanel.currentMap][i].exp;
					checkLevelUp();
				}
			}
		}
	}
	
	public void knockBack(Entity entity, int knockBackPower) {
		
		entity.direction = direction;
		entity.speed += knockBackPower;
		entity.knockBack = true;
	}
	
	public void damageInteractiveTile(int i) {
		
		if ( i != 999 && gamePanel.interactTile[gamePanel.currentMap][i].destructible == true && gamePanel.interactTile[gamePanel.currentMap][i].correctItem(this) ==  true && gamePanel.interactTile[gamePanel.currentMap][i].invincible == false) {
			
			//TODO: ADD CUT SOUND
			gamePanel.interactTile[gamePanel.currentMap][i].life--; //DECREASE ITS LIFE EACH HIT
			gamePanel.interactTile[gamePanel.currentMap][i].invincible = true;
			
			//GENERATE PARTICLE
			generateParticle(gamePanel.interactTile[gamePanel.currentMap][i], gamePanel.interactTile[gamePanel.currentMap][i]);
			
			if (gamePanel.interactTile[gamePanel.currentMap][i].life == 0) {
				gamePanel.interactTile[gamePanel.currentMap][i] = gamePanel.interactTile[gamePanel.currentMap][i].getDestroyedForm();				
			}
		}
	}
	
	public void checkLevelUp() {
		if (exp >= nextLevelExp) {
			
			level++;
			nextLevelExp = nextLevelExp * 2;
			maxLife += 2;
			strength++;
			dexterity++;
			attack = getAttack();
			defense = getDefense();
			//TODO: SOUND FOR LEVELING UP
			
			gamePanel.gameState = gamePanel.dialogueState;
			gamePanel.ui.currentDialogue = "You are now level" + level + "!\n" + "You feel Stronger!";
			exp = 0;
			life = maxLife;
			mana = maxMana;
		}
	}
	
	public void selectItem() {
		
		int itemIndex = gamePanel.ui.getItemIndex(gamePanel.ui.playerSlotCol, gamePanel.ui.playerSlotRow);
		if (itemIndex < inventory.size()) {
			
			Entity selectedItem = inventory.get(itemIndex); //ADD PARAMETERS FOR DIFFERENT TYPES OF THINGS
			
			if (selectedItem.type == type_sword || selectedItem.type == type_axe) { 
				currentWeapon = selectedItem;
				attack = getAttack();
				getPlayerAttackImage();
				//TODO: NEW IMAGES FOR EACH WEAPON TYPE BEING USED
			}
			if (selectedItem.type == type_shield) { 
				currentShield = selectedItem;
				defense = getDefense();
			}
			if (selectedItem.type == type_light) {
				if (currentLight == selectedItem) {//IF THE SELECTED ITEM IS ALREADY THE CURRENT LIGHT, THEN UNSELECT THE ITEM
					currentLight = null;
				}
				else {
					currentLight = selectedItem; //IF YOU DONT HAVE A LIGHT EQUIPPED, THE SELECTED ONE BECOMES THE NEW LIGHT
				}
				lightUpdated = true;
			}
			if (selectedItem.type == type_consumable) { 
				if (selectedItem.use(this) == true) {
					if (selectedItem.amount > 1) {
						selectedItem.amount--;
					}
					else {
						inventory.remove(itemIndex);											
					}
				}
			}
		}
	}
	
	public int searchItemInventory(String itemName) { //SCAN INVENTORY TO SEE IF THE SAME ITEM IS THERE || CAN ALSO BE USED FOR QUEST ITEMS
		
		int itemIndex = 999;
		
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i).name.equals(itemName)) {
				itemIndex = i;
				break;
			}
		}
		return itemIndex;
	}
	
	public boolean canObtainItem(Entity item) {
		
		boolean canObtain = false;
		
		//CHECK IF THE ITEM IS STACKABLE
		if (item.stackable == true) {
			
			int index = searchItemInventory(item.name);
			
			if (index != 999) {
				inventory.get(index).amount++;
				canObtain = true;
			}
			else { //NEW ITEM || NEED TO HAVE AN EMPTY SPOT
				if (inventory.size() != maxInventorySize) {
					inventory.add(item);
					canObtain = true;
				}
			}
		}
		else { //ITEM NOT STACKABLE || CHECK FOR AN OPEN SPOT
			if (inventory.size() != maxInventorySize) {
				inventory.add(item);
				canObtain = true;
			}
		}
		return canObtain;
	}
	
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int tempScreenX =  screenX;
		int tempScreenY = screenY;
		
		switch(direction) { //each key direction input draws the corresponding image || character movement
		case "up":
			if (attacking == false) {
				if (spriteNum == 1) { image = up1; }
				if (spriteNum == 2) { image = up2; }				
			}
			if (attacking == true) {
				tempScreenY = screenY - gamePanel.tileSize;
				if (spriteNum == 1) { image = attackUp1; }
				if (spriteNum == 2) { image = attackUp2; }				
			}
			break;
		case "down":
			if (attacking == false) {
				if (spriteNum == 1) { image = down1; }
				if (spriteNum == 2) { image = down2; }	
			}
			if (attacking == true) {
				if (spriteNum == 1) { image = attackDown1; }
				if (spriteNum == 2) { image = attackDown2; }	
			}
			break;
		case "right":
			if (attacking == false) {
				if (spriteNum == 1) { image = right1; }
				if (spriteNum == 2) { image = right2; }	
			}
			if (attacking == true) {
				if (spriteNum == 1) { image = attackRight1; }
				if (spriteNum == 2) { image = attackRight2; }	
			}
			break;
		case "left":
			if (attacking == false) {
				if (spriteNum == 1) { image = left1; }
				if (spriteNum == 2) { image = left2; }	
			}
			if (attacking == true) {
				tempScreenX = screenX - gamePanel.tileSize;
				if (spriteNum == 1) { image = attackLeft1; }
				if (spriteNum == 2) { image = attackLeft2; }	
			}
			break;
		}
		
		//VISUAL EFFECT FOR INVINCIBLE
		if (invincible == true) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		}
		
		g2.drawImage(image, tempScreenX, tempScreenY, null); //draw the image on the screen
		
		//RESET EFFECT
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
}
