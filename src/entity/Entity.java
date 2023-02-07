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
import main.UtilityTool;

//This will be the parent class for all players, monsters, NPCs, etc.

public class Entity {
	
	GamePanel gamePanel;
	public BufferedImage up1, up2, right1, right2, down1, down2, left1, left2; //beffered image describes an image with an accessible buffer
	public BufferedImage attackUp1, attackUp2, attackRight1, attackRight2, attackDown1, attackDown2, attackLeft1, attackLeft2;
	public BufferedImage image, image2, image3;
	public Rectangle solidArea = new Rectangle(0, 0, 40, 40); //store data in an invisible rectangle to help with tile collision
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0); //Attack collision
	public int solidAreaDefaultX, solidAreaDefaultY;
	public Boolean collision = false;
	String dialogues[] = new String[20];

	//STATE
	public int worldX, worldY;
	public String direction = "down";
	public int spriteNum = 1;
	int dialogueIndex = 0;
	public boolean collisionOn = false;
	public boolean invincible = false;
	boolean attacking = false;
	public boolean alive = true;
	public boolean dying = false;
	boolean hpBarOn = false;
	public boolean onPath = false;
	public boolean knockBack = false;
	public Entity loot;
	public boolean opened = false;
	
	//Counter
	public int spriteCounter = 0;
	public int actionLockCounter = 0;
	public int invincibleCounter = 0;
	public int shotAvailableCounter = 0;
	int dyingCounter = 0;
	int hpBarCounter = 0;
	int knockBackCounter = 0;
		
	//CHARACTER ATTRIBUTES
	public String name;
	public int defaultSpeed;
	public int speed;
	public int maxLife;
	public int life;
	public int maxMana;
	public int mana;
	public int ammo; //PROJECTILES
	public int level;
	public int strength;
	public int dexterity;
	public int attack;
	public int defense;
	public int exp;
	public int nextLevelExp;
	public int coin;
	public Entity currentWeapon;
	public Entity currentShield;
	public Entity currentLight;
	public Projectile projectile;
	
	//ITEM ATTRIBUTES
	public ArrayList<Entity> inventory = new ArrayList<>();
	public final int maxInventorySize = 20;
	public int value;
	public int attackValue;
	public int defenseValue;
	public String description = "";
	public int useCost;
	public int price;
	public int knockBackPower;
	public boolean stackable = false;
	public int amount = 1;
	public int lightRadius; //DIFFERENT THINGS CAN HAVE DIFFERENT LIGHT CIRCLES
	
	//TYPE
	public int type;
	public final int type_player = 0;
	public final int type_npc = 1;
	public final int type_monster = 2;
	public final int type_sword = 3;
	public final int type_shield = 4;
	public final int type_consumable = 5;
	public final int type_pickUpOnly = 6;
	public final int type_axe = 7;
	public final int type_obstacle = 8;
	public final int type_light = 9;
	
	
	public Entity(GamePanel gamePanel) {
		
		this.gamePanel = gamePanel;
	}
	
	public int getLeftX() {
		return worldX + solidArea.x;
	}
	public int getRightX() {
		return worldX + solidArea.x + solidArea.width;
	}
	public int getTopY() {
		return worldY + solidArea.y;
	}
	public int getBottomY() {
		return worldY + solidArea.y + solidArea.height;
	}
	public int getCol() {
		return (worldX + solidArea.x)/gamePanel.tileSize;
	}
	public int getRow() {
		return (worldY + solidArea.y)/gamePanel.tileSize;
	}
	
	public void setLoot(Entity loot) {}
	
	public void setAction() {}
	public void damageReaction() {}
	
	public void speak() {
		
		if (dialogues[dialogueIndex] == null) {
			dialogueIndex = 0;
		}
		gamePanel.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;
		
		switch (gamePanel.player.direction) { //changing the direction of the npc depending on the direction the player is when dialogue starts
			case "up": direction = "down"; break;
			case "down": direction = "up"; break;
			case "right": direction = "left"; break;
			case "left": direction = "right"; break;
		}
	}
	
	public void interact() {}
	
	public boolean use(Entity entity) {return false;}
	
	public void checkDrop() {}
	public void dropItem(Entity droppedItem) {
		
		for (int i = 0; i < gamePanel.obj[1].length; i++) {
			if (gamePanel.obj[gamePanel.currentMap][i] == null) {
				gamePanel.obj[gamePanel.currentMap][i] = droppedItem;
				gamePanel.obj[gamePanel.currentMap][i].worldX = worldX; //DEAD MONSTER COORDINATES
				gamePanel.obj[gamePanel.currentMap][i].worldY = worldY;
				break;
			}
		}
	}
	
	public Color getParticleColor() {
		Color color = null;
		return color;
	}
	
	public int getParticleSize() {
		int size = 0;
		return size;
	}
	
	public int getParticleSpeed() {
		int speed = 0;
		return speed;
	}
	
	public int getParticleMaxLife() { //HOW LONG THE PARTICLES LAST
		int maxLife = 0;
		return maxLife;
	}
	
	public void generateParticle(Entity generator, Entity target) {
		Color color = generator.getParticleColor();
		int size = generator.getParticleSize();
		int speed = generator.getParticleSpeed();
		int maxLife = generator.getParticleMaxLife();
		
		Particle p1 = new Particle(gamePanel, target, color, size, speed, maxLife, -2, -1);//TOP LEFT DIRECTION
		Particle p2 = new Particle(gamePanel, target, color, size, speed, maxLife, 2, -1);//TOP RIGHT DIRECTION
		Particle p3 = new Particle(gamePanel, target, color, size, speed, maxLife, -2, 1);//BOTTOM LEFT DIRECTION
		Particle p4 = new Particle(gamePanel, target, color, size, speed, maxLife, 2, 1);//BOTTOM RIGHT DIRECTION
		gamePanel.particleList.add(p1);
		gamePanel.particleList.add(p2);
		gamePanel.particleList.add(p3); 
		gamePanel.particleList.add(p4);

	}
	
	public void checkCollision() {
		collisionOn = false;
		gamePanel.collisionChecker.checkTile(this); //npc collision with tiles
		gamePanel.collisionChecker.checkObject(this, false); //npc collision with objects
		gamePanel.collisionChecker.checkEntity(this, gamePanel.npc); //npc collision with other npc
		gamePanel.collisionChecker.checkEntity(this, gamePanel.monster); //npc collision with monsters
		gamePanel.collisionChecker.checkEntity(this, gamePanel.interactTile);//npc collision with interactible tiles
		boolean contactPlayer = gamePanel.collisionChecker.checkPlayer(this);// npc collision with player
		
		//DAMAGE
		if (this.type == type_monster && contactPlayer == true) {
			damagePlayer(attack);
		}
	}
	
	public void update() {
		
		if (knockBack == true) {
			
			checkCollision();
			
			if (collisionOn == true) { //STOP THE KNOCKBACK EFFECT IF IT HITS A SOLID TILE
				knockBackCounter = 0;
				knockBack = false;
				speed = defaultSpeed;
			}
			else if (collisionOn == false) {
				switch(gamePanel.player.direction) {
					case "up": worldY -= speed; break;
					case "down": worldY += speed; break;
					case "right": worldX += speed; break;
					case "left": worldX -= speed; break;
				
				}
			}
			knockBackCounter++;
			if (knockBackCounter == 10) { //INCREASE IN NUMBER INCREASES THE KNOCKBACK DISTANCE
				knockBackCounter = 0;
				knockBack = false;
				speed = defaultSpeed;
			}
		}
		else {
			
			setAction();
			checkCollision();
			
			//If collision is false, npc can move
			if (collisionOn == false) {
				
				switch(direction) {
					case "up": worldY -= speed; break;
					case "down": worldY += speed; break;
					case "right": worldX += speed; break;
					case "left": worldX -= speed; break;
				}
			}			
		}
		
		
		spriteCounter++;
		if (spriteCounter > 12) { //npc image changes every 12 frames
			if (spriteNum == 1) {
				spriteNum = 2;
			} else if (spriteNum == 2) {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}
		
		if (invincible == true) {
			invincibleCounter++;
			if (invincibleCounter > 40) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		
		if (shotAvailableCounter < 30) {
			shotAvailableCounter++;
		}
	}
	
	public void damagePlayer(int attack) {
		if (gamePanel.player.invincible == false) {
			gamePanel.playSE(7);
			
			int damage = attack - gamePanel.player.defense;
			if (damage < 0) {
				damage = 0;
			}
			gamePanel.player.life -= damage;
			
			gamePanel.player.invincible = true;
		}
	}
	
	public void draw(Graphics2D g2) { //draw the entity on the map
		BufferedImage image = null;
		int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
		int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY; 
		
		if (worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX && 
			worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
			worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
			worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {
			
			switch(direction) { //each key direction input draws the corresponding image || character movement
				case "up":
					if (spriteNum == 1) { image = up1; }
					if (spriteNum == 2) { image = up2; }
					break;
				case "down":
					if (spriteNum == 1) { image = down1; }
					if (spriteNum == 2) { image = down2; }
					break;
				case "right":
					if (spriteNum == 1) { image = right1; }
					if (spriteNum == 2) { image = right2; }
					break;
				case "left":
					if (spriteNum == 1) { image = left1; }
					if (spriteNum == 2) { image = left2; }
					break;
				}
			
			//MONSTER HEALTH BAR
			if (type == 2 && hpBarOn == true) {
				
				double oneScale = (double)gamePanel.tileSize/maxLife;
				double hpBarValue = oneScale*life;
				
				g2.setColor(new Color(35, 35, 35));
				g2.fillRect(screenX-1, screenY-16, gamePanel.tileSize+2, 12);
				
				g2.setColor(new Color(255, 0, 30));
				g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);
				
				hpBarCounter++;
				
				if (hpBarCounter > 600) {
					hpBarCounter = 0;
					hpBarOn = false;
				}
			}
			
			//VISUAL EFFECT FOR INVINCIBLE
			if (invincible == true) {
				hpBarOn = true;
				hpBarCounter = 0;
				changeAlpha(g2, 0.4f);
			}
			
			//DYING ANIMATION
			if (dying == true) {
				dyingAnimation(g2);
			}
			
			g2.drawImage(image, screenX, screenY, null );
			
			changeAlpha(g2, 1f);
		}
	}
	
	public void dyingAnimation(Graphics2D g2) {//BLINKING DYING EFFECT EVERY 5 FRAMES
		
		dyingCounter++;
		
		int i = 5;
		
		if (dyingCounter <= i) { changeAlpha(g2, 0f); }
		if (dyingCounter > i && dyingCounter <= i*2) { changeAlpha(g2, 1f); }
		if (dyingCounter > i*2 && dyingCounter <= i*3) { changeAlpha(g2, 0f); }
		if (dyingCounter > i*3 && dyingCounter <= i*4) { changeAlpha(g2, 1f); }
		if (dyingCounter > i*4 && dyingCounter <= i*5) { changeAlpha(g2, 0f); }
		if (dyingCounter > i*5 && dyingCounter <= i*6) { changeAlpha(g2, 1f); }
		if (dyingCounter > i*6 && dyingCounter <= i*7) { changeAlpha(g2, 0f); }
		if (dyingCounter > i*7 && dyingCounter <= i*8) { changeAlpha(g2, 1f); }
		if (dyingCounter > i*8) {
			alive = false;
		}
	}
	
	public void changeAlpha(Graphics2D g2, float alphaValue) {
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}
	

	public BufferedImage setup(String imagePath, int width, int height) {
		UtilityTool utilityTool = new UtilityTool();
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = utilityTool.scaledImage(image, width, height);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public void searchPath(int goalCol, int goalRow) {
		
		int startCol = (worldX + solidArea.x)/gamePanel.tileSize;
		int startRow = (worldY + solidArea.y)/gamePanel.tileSize;
		
		gamePanel.pathFinder.setNodes(startCol, startRow, goalCol, goalRow);
		
		if (gamePanel.pathFinder.search() == true) {//RETURNING TRUE MEANS THAT IT HSA FOUND A PATH
			
			//NEXT WORLDX AND WORLDY
			int nextX = gamePanel.pathFinder.pathList.get(0).col * gamePanel.tileSize;
			int nextY = gamePanel.pathFinder.pathList.get(0).row * gamePanel.tileSize;
			
			//ENTITY SOLID AREA POSITIONS
			int entityLeftX = worldX + solidArea.x;
			int entityRightX = worldX + solidArea.x + solidArea.width;
			int entityTopY = worldY + solidArea.y;
			int entityBottomY = worldY + solidArea.y + solidArea.height;
			
			//BASED ON THE ENTITY POSITION || FIND RELATIVE DIRECTION OF THE NEXT NODE
			if (entityTopY > nextY && entityLeftX >= nextX && entityRightX < nextX + gamePanel.tileSize) {
				direction = "up";
			}
			else if (entityTopY < nextY && entityLeftX >= nextX && entityRightX < nextX + gamePanel.tileSize) {
				direction = "down";
			}
			else if (entityTopY >= nextY && entityBottomY < nextY + gamePanel.tileSize) {
				if (entityLeftX > nextX) {
					direction = "left";
				}
				if (entityLeftX < nextX) {
					direction = "right";
				}
			}
			else if (entityTopY > nextY && entityLeftX > nextX) {
				direction = "up";
				checkCollision();
				if (collisionOn == true) {
					direction = "left";
				}
			}
			else if (entityTopY > nextY && entityLeftX < nextX) {
				direction = "up";
				checkCollision();
				if (collisionOn == true) {
					direction = "right";
				}
			}
			else if (entityTopY < nextY && entityLeftX > nextX) {
				direction = "down";
				checkCollision();
				if (collisionOn == true) {
					direction = "left";
				}
			}
			else if (entityTopY < nextY && entityLeftX < nextX) {
				direction = "down";
				checkCollision();
				if (collisionOn == true) {
					direction = "right";
				}
			}
			
			//IF THEY REACH THEIR GOAL (DISABLE IF YOU WANT THEM TO FOLLOW THE PLAYER)
//			int nextCol = gamePanel.pathFinder.pathList.get(0).col;
//			int nextRow = gamePanel.pathFinder.pathList.get(0).row;
//			if (nextCol == goalCol && nextRow == goalRow) {
//				onPath = false;
//			}
		}
	}
	
	public int getDetected(Entity user, Entity target[][], String targetName) {
		
		int index = 999;
		
		//CHECK THE SURROUNDING OBJECT
		int nextWorldX = user.getLeftX();
		int nextWorldY = user.getTopY();
		
		switch(user.direction) {
			case "up": nextWorldY = user.getTopY() - 1; break;
			case "down": nextWorldY = user.getBottomY() + 1; break;
			case "left": nextWorldX = user.getLeftX() - 1; break;
			case "right": nextWorldX = user.getLeftX() + 1; break;
		}
		
		int col = nextWorldX/gamePanel.tileSize;
		int row = nextWorldY/gamePanel.tileSize;
		
		for (int i = 0; i < target[1].length; i++) {
			if (target[gamePanel.currentMap][i] != null) {
				if (target[gamePanel.currentMap][i].getCol() == col && target[gamePanel.currentMap][i].getRow() == row && target[gamePanel.currentMap][i].name.equals(targetName)) {
					
					index = i;
					break;
				}
			}
		}
		return index;
	}
}
