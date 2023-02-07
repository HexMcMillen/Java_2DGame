package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

import entity.Entity;
import object.Coin_Bronze;
import object.Health;
import object.KeyGold;
import object.Mana;

public class UI {//handles the onscree UI to dsiplay text messages and what not
	
	GamePanel gamePanel;
	Graphics2D g2;
	public Font arial_40, arial_80B;
	BufferedImage heart_full, heart_half, heart_empty, mana_full, mana_empty, coin;
	public boolean messageOn = false;
	ArrayList<String> message = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	public boolean gameFinished = false;
	public String currentDialogue = "";
	public int commandNumber = 0;
	public int playerSlotCol = 0;
	public int playerSlotRow = 0;
	public int npcSlotCol = 0;
	public int npcSlotRow = 0;
	int subState = 0;
	int counter = 0;
	public Entity npc;
	
	public UI(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		
		arial_40 = new Font("Arial", Font.PLAIN, 40); //("Font Name", Font Style, Font Size)
		arial_80B = new Font("Arial", Font.BOLD, 80);
		
		//CREATE HUD HEART
		Entity heart = new Health(gamePanel);
		heart_full = heart.image3;
		heart_half = heart.image2;
		heart_empty = heart.image;
		
		//HUD MANA
		Entity mana = new Mana(gamePanel);
		mana_full = mana.image;
		mana_empty = mana.image2;
		
		//SHOP
		Entity bronzeCoin = new Coin_Bronze(gamePanel);
		coin = bronzeCoin.down1;
	}
	
	public void addMessage(String text) {
		
		message.add(text);
		messageCounter.add(0);
		
	}

	public void draw(Graphics2D g2) {
		
		this.g2 = g2;
		
		g2.setFont(arial_40);
		g2.setColor(Color.white);
		
		//TITLE STATE
		if (gamePanel.gameState ==  gamePanel.titleState) {
			drawTitleScreen();
		}
		
		//PLAY STATE
		if (gamePanel.gameState == gamePanel.playState) {
			drawPlayerLife();
			drawMessage();
		}
		
		//PAUSE STATE
		if (gamePanel.gameState == gamePanel.pauseState) {
			drawPlayerLife();
			drawPauseScreen();
		}
		
		//DIALOGUE STATE
		if (gamePanel.gameState ==  gamePanel.dialogueState) {
			drawDialogueScreen();
		}
		
		//CHARACTER STATE
		if (gamePanel.gameState == gamePanel.characterState) {
			drawCharacterScreen();
			drawInventory(gamePanel.player, true);
		}
		
		//OPTIONS STATE
		if (gamePanel.gameState == gamePanel.optionsState) {
			drawOptionsScreen();
		}
		
		//GAME OVER STATE
		if (gamePanel.gameState == gamePanel.gameOverState) {
			drawGameOverScreen();
		}
		
		//TRANSITION STATE
		if (gamePanel.gameState == gamePanel.transitionState) {
			drawTransition();
		}
		
		//TRADE STATE
		if (gamePanel.gameState == gamePanel.tradeState) {
			drawTradeScreen();
		}
		
		//SLEEP STATE
		if (gamePanel.gameState == gamePanel.sleepState) {
			drawSleepScreen();
		}
	}
	
	public void drawPlayerLife() {
		
		int x = gamePanel.tileSize/2;
		int y = gamePanel.tileSize/2;
		int i = 0;
		
		//DRAW MAX HEALTH
		while (i < gamePanel.player.maxLife/2) {
			g2.drawImage(heart_empty, x, y, null );
			i++;
			x += gamePanel.tileSize;
		}
		
		//RESET VALUES
		x = gamePanel.tileSize/2;
		y = gamePanel.tileSize/2;
		i = 0;
		
		//DRAW CURRENT LIFE
		while (i < gamePanel.player.life) {
			g2.drawImage(heart_half, x, y, null);
			i++;
			if (i < gamePanel.player.life) {
				g2.drawImage(heart_full, x, y, null );
			}
			i++;
			x += gamePanel.tileSize;
		}
		
		//DRAW MAX MANA
		x = (gamePanel.tileSize/2) - 5;
		y = (int) (gamePanel.tileSize*1.5);
		i = 0;
		while (i < gamePanel.player.maxMana) {
			g2.drawImage(mana_empty, x, y, null);
			i++;
			x += 35;
		}
		
		//DRAW MANA
		x = (gamePanel.tileSize/2) - 5;
		y = (int) (gamePanel.tileSize*1.5);
		i = 0;
		while (i < gamePanel.player.mana) {
			g2.drawImage(mana_full, x, y, null);
			i++;
			x += 35;
		}
	}
	
	public void drawMessage() {
		int messageX = gamePanel.tileSize;
		int messageY = gamePanel.tileSize *4;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
		
		for (int i = 0; i < message.size(); i++) {
			if (message.get(i) != null) {
				
				g2.setColor(Color.black);
				g2.drawString(message.get(i), messageX+2, messageY+2);
				g2.setColor(Color.white);
				g2.drawString(message.get(i), messageX, messageY);
				
				int counter = messageCounter.get(i) + 1; //MESSAGE COUNTER ++
				messageCounter.set(i, counter); //SET COUNTER TO THE ARRAY
				messageY += 50;
				
				if (messageCounter.get(i) > 180) {
					message.remove(i);
					messageCounter.remove(i);
				}
			}
		}
		
	}
	
	public void drawTitleScreen() {
		
		//BACKGROUND COLOR
		g2.setColor(new Color(0, 0, 0));
		g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
		
		//TITLE NAME
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 50F));
		String text = "My Personal 2D Adventure";
		int x = getXforCenter(text);
		int y = gamePanel.tileSize*3;
		
		//SHADOW
		g2.setColor(Color.gray);
		g2.drawString(text, x+5, y+5);
		
		//MAIN COLOR
		g2.setColor(Color.WHITE);
		g2.drawString(text, x, y);
		
		//CHARACTER IMAGE
		x = gamePanel.screenWidth/2 - (gamePanel.tileSize*2)/2;
		y += gamePanel.tileSize*2;
		g2.drawImage(gamePanel.player.down1, x, y, gamePanel.tileSize*2, gamePanel.tileSize*2, null );
		
		//MENU
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
		
		text = "NEW GAME";
		x = getXforCenter(text);
		y += gamePanel.tileSize*3.5;
		g2.drawString(text, x, y);
		if (commandNumber == 0) {
			g2.drawString("<", x-gamePanel.tileSize, y);
			g2.drawString(">", 600, y);
		}
		
		text = "LOAD GAME";
		x = getXforCenter(text);
		y += gamePanel.tileSize;
		g2.drawString(text, x, y);
		if (commandNumber == 1) {
			g2.drawString("<", x-gamePanel.tileSize, y);
			g2.drawString(">", 608, y);
		}
		
		text = "EXIT";
		x = getXforCenter(text);
		y += gamePanel.tileSize;
		g2.drawString(text, x, y);
		if (commandNumber == 2) {
			g2.drawString("<", x-gamePanel.tileSize, y);
			g2.drawString(">", 535, y);
		}
	}
	
	public void drawPauseScreen() { //drawing things only when the game state is paused
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
		String text = "PAUSED";
		int x = getXforCenter(text);
		int y = gamePanel.screenHeight/2;
		
		g2.drawString(text, x, y);
	}
	
	public void drawDialogueScreen() {
		
		//DIALOGUE WINDOW
		int x = gamePanel.tileSize*3;
		int y = gamePanel.tileSize/2;
		int width = gamePanel.screenWidth - (gamePanel.tileSize*6); 
		int height = gamePanel.tileSize*4;
		drawSubWindow(x, y, width, height);
		
		//DIALOGUE PLACEMENT
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
		x += gamePanel.tileSize;
		y += gamePanel.tileSize;
		
		for(String line : currentDialogue.split("\n")) { //split the text in the dialogue at the key word
			g2.drawString(line, x, y); // after the split increase the y so the next line will be displayed under the first one
			y += 40;
		}
	}
	
	public void drawCharacterScreen() {
		
		//FRAME
		final int frameX = gamePanel.tileSize*2;
		final int frameY = gamePanel.tileSize;
		final int frameWidth = gamePanel.tileSize*5;
		final int frameHeight = gamePanel.tileSize*10;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		//TEXT
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		int textX = frameX + 20;
		int textY = frameY + gamePanel.tileSize;
		final int lineHeight = 35;
		
		//PARAMETERS
		g2.drawString("Level:", textX, textY);
		textY += lineHeight;
		g2.drawString("Life:", textX, textY);
		textY += lineHeight;
		g2.drawString("Mana:", textX, textY);
		textY += lineHeight;
		g2.drawString("Strength:", textX, textY);
		textY += lineHeight;
		g2.drawString("Dexterity:", textX, textY);
		textY += lineHeight;
		g2.drawString("Attack:", textX, textY);
		textY += lineHeight;
		g2.drawString("Defense:", textX, textY);
		textY += lineHeight;
		g2.drawString("EXP:", textX, textY);
		textY += lineHeight;
		g2.drawString("Next Level:", textX, textY);
		textY += lineHeight;
		g2.drawString("Coin:", textX, textY);
		textY += lineHeight + 12;
		g2.drawString("Weapon:", textX, textY);
		textY += lineHeight + 12;
		g2.drawString("Shield:", textX, textY);
		textY += lineHeight;
		
		//VALUES
		int tailX = (frameX + frameWidth) - 30;
		//RESET TEXT Y
		textY = frameY + gamePanel.tileSize;
		String value;
		
		value = String.valueOf(gamePanel.player.level);
		textX = getXforAlignRight(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gamePanel.player.life + "/" + gamePanel.player.maxLife);
		textX = getXforAlignRight(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gamePanel.player.mana + "/" + gamePanel.player.maxMana);
		textX = getXforAlignRight(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gamePanel.player.strength);
		textX = getXforAlignRight(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gamePanel.player.dexterity);
		textX = getXforAlignRight(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gamePanel.player.attack);
		textX = getXforAlignRight(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gamePanel.player.defense);
		textX = getXforAlignRight(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gamePanel.player.exp);
		textX = getXforAlignRight(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gamePanel.player.nextLevelExp);
		textX = getXforAlignRight(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gamePanel.player.coin);
		textX = getXforAlignRight(value, tailX);
		g2.drawString(value, textX, textY);
		textY += lineHeight - 25;
		
		g2.drawImage(gamePanel.player.currentWeapon.down1, tailX - gamePanel.tileSize, textY, null);
		textY += gamePanel.tileSize;
		
		g2.drawImage(gamePanel.player.currentShield.down1, tailX - gamePanel.tileSize, textY, null);
			
	}
	
	public void drawInventory(Entity entity, boolean cursor) {
		
		int frameX;
		int frameY;
		int frameWidth;
		int frameHeight;
		int slotCol;
		int slotRow;
		
		if (entity == gamePanel.player) {
			frameX = gamePanel.tileSize*12;
			frameY = gamePanel.tileSize;
			frameWidth = gamePanel.tileSize*6;
			frameHeight = gamePanel.tileSize*5;
			slotCol = playerSlotCol;
			slotRow = playerSlotRow;
		}
		else {
			frameX = gamePanel.tileSize*2;
			frameY = gamePanel.tileSize;
			frameWidth = gamePanel.tileSize*6;
			frameHeight = gamePanel.tileSize*5;
			slotCol = npcSlotCol;
			slotRow = npcSlotRow;
		}
		
		//FRAME
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		//SLOTS
		final int slotXstart = frameX + 20;
		final int slotYstart = frameY + 20;
		int slotX = slotXstart;
		int slotY = slotYstart;
		int slotSize = gamePanel.tileSize + 3;
		
		//DRAW INVENTORY
		for (int i = 0; i < entity.inventory.size(); i++) {
			
			//EQUIP CURSOR
			if (entity.inventory.get(i) == entity.currentWeapon || entity.inventory.get(i) == entity.currentShield || entity.inventory.get(i) == entity.currentLight) {
				
				g2.setColor(new Color(240, 190, 90));
				g2.fillRoundRect(slotX, slotY, gamePanel.tileSize, gamePanel.tileSize, 10, 10);
			}
			
			g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);
			
			//DISPLAY THE AMOUNT OF THE OBJECT YOU HAVE
			if (entity == gamePanel.player && entity.inventory.get(i).amount > 1) {
				g2.setFont(g2.getFont().deriveFont(32f));
				int amountX;
				int amountY;
				
				String s = "" + entity.inventory.get(i).amount;
				amountX = getXforAlignRight(s, slotX + 44);
				amountY = slotY + gamePanel.tileSize;
				
				//SHADOW
				g2.setColor(new Color(60, 60, 60));
				g2.drawString(s, amountX, amountY);
				
				//Number
				g2.setColor(Color.white);
				g2.drawString(s, amountX-3, amountY-3);

			}
			
			slotX += slotSize;
			
			if (i == 4 || i == 9 || i == 14) {
				slotX = slotXstart;
				slotY += slotSize;
			}
		}
		
		//CURSOR
		if (cursor == true) {
			
			int cursorX = slotXstart + (slotSize * slotCol);
			int cursorY = slotYstart + (slotSize * slotRow);
			int cursorWidth = gamePanel.tileSize;
			int cursorHeight = gamePanel.tileSize;
			
			//DRAW CURSOR
			g2.setColor(Color.white);
			g2.setStroke(new BasicStroke(3));
			g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
			
			//ITEM DESCRIPTION FRAME
			int dFrameX = frameX;
			int dFrameY = frameY + frameHeight;
			int dFrameWidth = frameWidth;
			int dFrameHeight = gamePanel.tileSize*3;
			
			
			//DRAW DESCRIPTION
			int textX = dFrameX + 20;
			int textY = dFrameY + gamePanel.tileSize;
			g2.setFont(g2.getFont().deriveFont(16F));
			
			int itemIndex = getItemIndex(slotCol, slotRow);
			if(itemIndex < entity.inventory.size()) {
				
				drawSubWindow(dFrameX, dFrameY + 10, dFrameWidth, dFrameHeight);
				
				for(String line: entity.inventory.get(itemIndex).description.split("\n")) {
					g2.drawString(line, textX, textY);
					textY += 28;
				}
			}
		}
	}
	
	public void drawGameOverScreen() {
		
		g2.setColor(new Color(0, 0, 0, 150));
		g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
		
		int x;
		int y;
		String text;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));
		
		text = "GAME OVER!";
		
		//SHADOW
		g2.setColor(Color.black);
		x = getXforCenter(text);
		y = gamePanel.tileSize*4;
		g2.drawString(text, x, y);
		
		//MAIN TEXT
		g2.setColor(Color.white);
		g2.drawString(text, x-4, y-4);
		
		//OPTIONS
		g2.setFont(g2.getFont().deriveFont(50f));
		text = "Retry";
		x = getXforCenter(text);
		y += gamePanel.tileSize*4;
		g2.drawString(text, x, y);
		if (commandNumber == 0) {
			g2.drawString(">", x-40, y);
		}
		
		text = "Quit";
		x = getXforCenter(text);
		y += 55;
		g2.drawString(text, x, y);
		if (commandNumber == 1) {
			g2.drawString(">", x-40, y);
		}

	}
	
	public void drawOptionsScreen() {
		
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		//SUBWINDOW
		int frameX = gamePanel.tileSize*6;
		int frameY = gamePanel.tileSize;
		int frameWidth = gamePanel.tileSize*8;
		int frameHeight = gamePanel.tileSize*10;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		switch(subState) {
			case 0: options_Top(frameX, frameY); break;
			case 1: options_fullScreenNotification(frameX, frameY); break;
			case 2: options_control(frameX, frameY); break;
			case 3: options_endGameConfirm(frameX, frameY); break;
		}
		gamePanel.keyHandler.enterPressed = false;
	}
	
	public void options_Top(int frameX, int frameY) {
		
		int textX;
		int textY;
		
		//TITLE
		String text = "Options";
		textX = getXforCenter(text);
		textY = frameY + gamePanel.tileSize;
		g2.drawString(text, textX, textY);
		
		//FULL SCREEN ON/OFF
		textX = frameX + gamePanel.tileSize;
		textY += gamePanel.tileSize*2;
		g2.drawString("Full Screen", textX, textY);
		if (commandNumber == 0) {
			g2.drawString(">", textX - 25, textY);
			if (gamePanel.keyHandler.enterPressed == true) {
				if(gamePanel.fullScreenOn == false) {
					gamePanel.fullScreenOn = true;
				}
				else if (gamePanel.fullScreenOn == true) {
					gamePanel.fullScreenOn = false;
				}
				subState = 1;
			}
		}
		
		//MUSIC
		textY += gamePanel.tileSize;
		g2.drawString("Music", textX, textY);
		if (commandNumber == 1) {
			g2.drawString(">", textX - 25, textY);
		}
		
		//Sound Effects
		textY += gamePanel.tileSize;
		g2.drawString("Sounds", textX, textY);
		if (commandNumber == 2) {
			g2.drawString(">", textX - 25, textY);
		}
		
		//CONTROLS
		textY += gamePanel.tileSize;
		g2.drawString("Controls", textX, textY);
		if (commandNumber == 3) {
			g2.drawString(">", textX - 25, textY);
			if (gamePanel.keyHandler.enterPressed == true) {
				subState = 2;
				commandNumber = 0;
			}
		}
		
		//END GAME
		textY += gamePanel.tileSize;
		g2.drawString("End Game", textX, textY);
		if (commandNumber == 4) {
			g2.drawString(">", textX - 25, textY);
			if (gamePanel.keyHandler.enterPressed == true) {
				subState = 3;
				commandNumber = 0;
			}
		}
		
		//CLOSE WINDOW
		textY += gamePanel.tileSize*2;
		g2.drawString("Back", textX, textY);
		if (commandNumber == 5) {
			g2.drawString(">", textX - 25, textY);
			if (gamePanel.keyHandler.enterPressed ==  true) {
				gamePanel.gameState = gamePanel.playState;
				commandNumber = 0;
			}
		}
		
		//FULLSCREEN CHECK BOX
		textX = frameX + (int)(gamePanel.tileSize*4.5);
		textY = frameY + gamePanel.tileSize*2 + 24;
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(textX, textY, 24, 24);
		if (gamePanel.fullScreenOn == true) {
			g2.fillRect(textX, textY, 24, 24);
		}
		
		//MUSIC VOLUME
		textY += gamePanel.tileSize;
		g2.drawRect(textX, textY, 120, 24);
		int volumeWidth = 24 * gamePanel.music.volumeScale;
		g2.fillRect(textX, textY, volumeWidth, 24);
		
		//SOUND EFFECT VOLUME
		textY += gamePanel.tileSize;
		g2.drawRect(textX, textY, 120, 24);
		volumeWidth = 24 * gamePanel.se.volumeScale;
		g2.fillRect(textX, textY, volumeWidth, 24);
		
		gamePanel.config.saveConfig(); //EVERYTIME OPTIONS ARE ALTERED THE CURRENT SETTINGS ARE SAVED
	}
	
	public void options_fullScreenNotification(int frameX, int frameY) {
		
		int textX = frameX + gamePanel.tileSize;
		int textY = frameY + gamePanel.tileSize*3;
		
		currentDialogue = "Game must restart in \norder for the changes \nto apply!";
		for (String line: currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;	
		}
		
		//BACK
		textY = frameY + gamePanel.tileSize*9;
		g2.drawString("Back", textX, textY);
		if (commandNumber == 0) {
			g2.drawString(">", textX-25, textY);
			if (gamePanel.keyHandler.enterPressed == true) {
				subState = 0;
			}
		}	
	}
	
	public void options_control(int frameX, int frameY) {
		
		int textX;
		int textY;
		
		//TITLE
		String text = "Controls";
		textX = getXforCenter(text);
		textY = frameY + gamePanel.tileSize;
		g2.drawString(text, textX, textY);
		
		//OPTIONS
		textX = frameX + gamePanel.tileSize - 30;
		textY += gamePanel.tileSize;
		g2.drawString("Movement", textX, textY); textY += gamePanel.tileSize;
		g2.drawString("Confirm/Attack", textX, textY); textY += gamePanel.tileSize;
		g2.drawString("Magic/Shoot", textX, textY); textY += gamePanel.tileSize;
		g2.drawString("Inventory", textX, textY); textY += gamePanel.tileSize;
		g2.drawString("Pause", textX, textY); textY += gamePanel.tileSize;
		g2.drawString("Options", textX, textY); textY += gamePanel.tileSize;
		
		textX = frameX + (gamePanel.tileSize*6) - 25;
		textY = frameY + gamePanel.tileSize*2;
		g2.drawString("WASD", textX, textY); textY += gamePanel.tileSize;
		g2.drawString("ENTER", textX, textY); textY += gamePanel.tileSize;
		g2.drawString("F", textX, textY); textY += gamePanel.tileSize;
		g2.drawString("C", textX, textY); textY += gamePanel.tileSize;
		g2.drawString("P", textX, textY); textY += gamePanel.tileSize;
		g2.drawString("ESC", textX, textY); textY += gamePanel.tileSize;
		
		//BACK
		textX = frameX + gamePanel.tileSize;
		textY = frameY + gamePanel.tileSize*9;
		g2.drawString("Back", textX, textY);
		if (commandNumber == 0) {
			g2.drawString(">", textX-25, textY);
			if (gamePanel.keyHandler.enterPressed == true) {
				subState = 0;
				commandNumber = 3;
			}
		}
	}
	
	public void options_endGameConfirm(int frameX, int frameY) {
		
		int textX = frameX + gamePanel.tileSize;
		int textY = frameY + gamePanel.tileSize*3;
		
		currentDialogue = "Quit the game and \nreturn to the title \nscreen?";
		for (String line: currentDialogue.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;	
		}
		
		//YES
		String text = "Yes";
		textX = getXforCenter(text);
		textY += gamePanel.tileSize*3;
		g2.drawString(text, textX, textY);
		if (commandNumber == 0) {
			g2.drawString(">", textX-25, textY);
			if (gamePanel.keyHandler.enterPressed == true) {
				subState = 0;
				gamePanel.gameState = gamePanel.titleState;
				gamePanel.resetGame(true);
			}
		}
		
		//NO
		text = "No";
		textX = getXforCenter(text);
		textY += gamePanel.tileSize;
		g2.drawString(text, textX, textY);
		if (commandNumber == 1) {
			g2.drawString(">", textX-25, textY);
			if (gamePanel.keyHandler.enterPressed == true) {
				subState = 0;
				commandNumber = 4;
			}
		}
	}
	
	public void drawTransition() {
		
		counter++;
		g2.setColor(new Color(0, 0, 0, counter*5));
		g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
		
		if (counter == 50) {
			counter = 0;
			gamePanel.gameState = gamePanel.playState; //CHANGE GAME STATE
			gamePanel.currentMap = gamePanel.eventHandler.tempMap; //LOAD THE NEW MAP
			
			//READ PLAYER VARIABLES
			gamePanel.player.worldX = gamePanel.tileSize * gamePanel.eventHandler.tempCol;
			gamePanel.player.worldY = gamePanel.tileSize * gamePanel.eventHandler.tempRow;
			gamePanel.eventHandler.previousEventX = gamePanel.player.worldX;
			gamePanel.eventHandler.previousEventY = gamePanel.player.worldY;
		}
	}
	
	public void drawTradeScreen() {
		
		switch(subState) {
			case 0: trade_select(); break;
			case 1: trade_buy(); break;
			case 2: trade_sell(); break;
		}
		gamePanel.keyHandler.enterPressed = false;
	}
	
	public void trade_select() {
		
		drawDialogueScreen();
		
		//DRAW WINDOW
		int x = gamePanel.tileSize*15;
		int y = gamePanel.tileSize*4;
		int width = gamePanel.tileSize*3;
		int height = (int) (gamePanel.tileSize*3.5);
		drawSubWindow(x, y, width, height);
		
		//DRAW TEXT
		x += gamePanel.tileSize;
		y += gamePanel.tileSize;
		g2.drawString("Buy", x, y);
		if (commandNumber == 0) {
			g2.drawString(">", x-25, y);
			if (gamePanel.keyHandler.enterPressed == true) {
				subState = 1;
			}
		}
		y += gamePanel.tileSize;
		
		
		g2.drawString("Sell", x, y);
		if (commandNumber == 1) {
			g2.drawString(">", x-25, y);
			if (gamePanel.keyHandler.enterPressed == true) {
				subState = 2;
			}
		}
		y += gamePanel.tileSize;
		
		g2.drawString("Cancel", x, y);
		if (commandNumber == 2) {
			g2.drawString(">", x-25, y);
			if (gamePanel.keyHandler.enterPressed == true) {
				commandNumber = 0;
				gamePanel.gameState = gamePanel.dialogueState;
				currentDialogue = "Come again sometime!";
			}
		}
	}
	
	public void trade_buy() {
		
		//DRAW PLAYER INVENTORY
		drawInventory(gamePanel.player, false);
		
		//DRAW NPC INVENTORY
		drawInventory(npc, true);
		
		//DRAW HINT WINDOW
		int x = gamePanel.tileSize*2;
		int y = gamePanel.tileSize*9;
		int width = gamePanel.tileSize*6;
		int height = gamePanel.tileSize*2;
		drawSubWindow(x, y, width, height);
		g2.drawString("[ESC] Back", x+24, y+60);
		
		//PLAYER COIN WINDOW
		x = gamePanel.tileSize*12;
		y = gamePanel.tileSize*9;
		width = gamePanel.tileSize*6;
		height = gamePanel.tileSize*2;
		drawSubWindow(x, y, width, height);
		g2.drawString("Your Coin: " + gamePanel.player.coin, x+24, y+60);
		
		//PRICE WINDOW
		int itemIndex = getItemIndex(npcSlotCol, npcSlotRow);
		if (itemIndex < npc.inventory.size()) {
			x = (int)(gamePanel.tileSize*5.5);
			y = (int)(gamePanel.tileSize*5.5);
			width = (int)(gamePanel.tileSize*2.5);
			height = gamePanel.tileSize;
			drawSubWindow(x, y, width, height);
			g2.drawImage(coin, x+10, y+8, 32, 32, null);
			
			int price = npc.inventory.get(itemIndex).price;
			String text = "" + price;
			x = getXforAlignRight(text, gamePanel.tileSize*8-20);
			g2.drawString(text, x, y+34);
			
			//BUY AN ITEM
			if (gamePanel.keyHandler.enterPressed == true) {
				if (npc.inventory.get(itemIndex).price > gamePanel.player.coin) {
					subState = 0;
					gamePanel.gameState = gamePanel.dialogueState;
					currentDialogue = "Item costs too much for you to buy!";
					drawDialogueScreen();
				}
				else {
					if (gamePanel.player.canObtainItem(npc.inventory.get(itemIndex)) == true) {
						gamePanel.player.coin -= npc.inventory.get(itemIndex).price;
					}
					else {
						subState = 0;
						gamePanel.gameState = gamePanel.dialogueState;
						currentDialogue = "Not enough inventory slots!";
					}
				}
			}
		}
	}
	
	public void trade_sell() {
		
		//DRAW PLAYER INVENTORY
		drawInventory(gamePanel.player, true);
		
		int x;
		int y;
		int width;
		int height;
		
		//DRAW HINT WINDOW
		x = gamePanel.tileSize*2;
		y = gamePanel.tileSize*9;
		width = gamePanel.tileSize*6;
		height = gamePanel.tileSize*2;
		drawSubWindow(x, y, width, height);
		g2.drawString("[ESC] Back", x+24, y+60);
				
		//PLAYER COIN WINDOW
		x = gamePanel.tileSize*12;
		y = gamePanel.tileSize*9;
		width = gamePanel.tileSize*6;
		height = gamePanel.tileSize*2;
		drawSubWindow(x, y, width, height);
		g2.drawString("Your Coin: " + gamePanel.player.coin, x+24, y+60);
				
		//PRICE WINDOW
		int itemIndex = getItemIndex(playerSlotCol, playerSlotRow);
		if (itemIndex < gamePanel.player.inventory.size()) {
			x = (int)(gamePanel.tileSize*15.5);
			y = (int)(gamePanel.tileSize*5.5);
			width = (int)(gamePanel.tileSize*2.5);
			height = gamePanel.tileSize;
			drawSubWindow(x, y, width, height);
			g2.drawImage(coin, x+10, y+8, 32, 32, null);
					
			int price = gamePanel.player.inventory.get(itemIndex).price/2;
			String text = "" + price;
			x = getXforAlignRight(text, gamePanel.tileSize*18-20);
			g2.drawString(text, x, y+34);
					
			//SELL AN ITEM
			if (gamePanel.keyHandler.enterPressed == true) {
				if (gamePanel.player.inventory.get(itemIndex) == gamePanel.player.currentWeapon || gamePanel.player.inventory.get(itemIndex) == gamePanel.player.currentShield) {
					commandNumber = 0;
					subState = 0;
					gamePanel.gameState = gamePanel.dialogueState;
					currentDialogue = "Cannot sell equiped items!";
				}
				else {
					if (gamePanel.player.inventory.get(itemIndex).amount > 1) {
						gamePanel.player.inventory.get(itemIndex).amount--;
					}
					else {
						gamePanel.player.inventory.remove(itemIndex);	
					}
					gamePanel.player.coin += price;
				}
			}
		}
	}
	
	public void drawSleepScreen() {
		
		counter++;
		
		if (counter < 120) {
			gamePanel.environmentManager.lighting.filterAlpha += 0.01f;
			if (gamePanel.environmentManager.lighting.filterAlpha > 1f) {
				gamePanel.environmentManager.lighting.filterAlpha = 1f;
			}
		}
		if (counter >= 120) {
			gamePanel.environmentManager.lighting.filterAlpha -= 0.01f;
			if (gamePanel.environmentManager.lighting.filterAlpha <= 0f) {
				gamePanel.environmentManager.lighting.filterAlpha = 0f;
				counter = 0;
				gamePanel.environmentManager.lighting.dayState = gamePanel.environmentManager.lighting.day;
				gamePanel.environmentManager.lighting.dayCounter = 0;
				gamePanel.gameState = gamePanel.playState;
				gamePanel.player.getPlayerImage();
			}
		}
	}
	
	public int getItemIndex(int slotCol, int slotRow) {
		
		int itemIndex = slotCol + (slotRow*5);
		return itemIndex;
	}
	
	public void drawSubWindow(int x, int y, int width, int height) {
		
		//INSIDE THE WINDOW
		Color c = new Color(0, 0, 0, 210); //RGB Color Black || last is transperacy
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35); //last 2 numbers are border curve control
		
		//WINDOW BORDER
		c = new Color(255, 255, 255); //RGB Color White
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5)); // number inside is the width
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
		
	}
	
	public int getXforCenter(String text) {
		
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gamePanel.screenWidth/2 - length/2;
		return x;
	}
	
	public int getXforAlignRight(String text, int tailX) {
		
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - length;
		return x;
	}
}
