package entity;

import main.GamePanel;

public class Projectile extends Entity {
	
	Entity user;

	public Projectile(GamePanel gamePanel) {
		super(gamePanel);
		
	}
	
	//PARAMETERS
	public void set(int worldX, int worldY, String direction, boolean alive, Entity user) {
		
		this.worldX = worldX;
		this.worldY = worldY;
		this.direction = direction;
		this.alive = alive; //ALIVE LETS YOU ONLY SHOOT ONE PROJECTILE AT A TIME
		this.user = user; //USER IS USED IF YOU WANT MULTIPLE THINGS TO BE ABLE TO USE FIREBALL SUCH AS NPC/MONSTERS
		this.life = this.maxLife;
	}
	
	//MOVEMENT OF THE FIREBALL
	public void update() {
		
		//CHECK TO SEE WHO IS USING IT
		if (user == gamePanel.player) {
			//CHECK TO SEE IF IT HITS A MONSTER
			int monsterIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.monster);
			if (monsterIndex != 999) {
				gamePanel.player.damageMonster(monsterIndex, attack, knockBackPower); //DAMAGE MONSTER
				generateParticle(user.projectile, gamePanel.monster[gamePanel.currentMap][monsterIndex]);
				alive = false; //FIREBALL DISAPPEARS WHEN IT HITS A MONSTER
			}
		}
		if (user != gamePanel.player) {
			//CHECK PLAYER COLLISION
			boolean contactPlayer = gamePanel.collisionChecker.checkPlayer(this);
			if (gamePanel.player.invincible == false && contactPlayer == true) {
				damagePlayer(attack);
				generateParticle(user.projectile, gamePanel.player);
				alive = false;
			}
			
		}
		
		//GIVE THE SAME MOVEMENT AS EVERYTHING ELSE
		switch (direction) {
			case "up": worldY -= speed; break;
			case "down": worldY += speed; break;
			case "right": worldX += speed; break;
			case "left": worldX -= speed; break;
		}
		
		//PROJECTILE DISTANCE || GRADUALLY LOSES LIFE AND WHEN IT HITS 0 IT DISAPPEARS
		life--;
		if (life <= 0) {
			alive = false;
		}
		
		//CHANGE IMAGE
		spriteCounter++;
		if (spriteCounter > 12) { 
			if (spriteNum == 1) {
				spriteNum = 2;
			} else if (spriteNum == 2) {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}
	}
	
	public boolean haveResource(Entity user) { //CAN ONLY BE USED IF THE USER HAS ENOUGH MANA
		
		boolean haveResource = false;
		return haveResource;
	}
	
	public void useResource(Entity user) {}

}
