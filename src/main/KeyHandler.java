package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	
	GamePanel gamePanel;
	
	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
	
	//DeBug
	boolean checkDrawTime = false;
	
	public KeyHandler(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {//listener interface to recieve keyboard events when key is pressed
		
		int code = e.getKeyCode(); // returns the number of the key pressed
		
		//TITLE STATE
		if (gamePanel.gameState == gamePanel.titleState) {
			titleState(code);
		}
		
		//PLAY STATE
		else if(gamePanel.gameState == gamePanel.playState) {
			playState(code);
		}
		
		//PAUSE STATE
		else if (gamePanel.gameState == gamePanel.pauseState) {
			pauseState(code);
		}
		
		//DIALOGUE STATE
		else if (gamePanel.gameState == gamePanel.dialogueState) {
			dialogueState(code);
		}
		
		//CHARACTER STATE
		else if (gamePanel.gameState == gamePanel.characterState) {
			characterState(code);
		}
		
		//OPTIONS STATE
		else if (gamePanel.gameState == gamePanel.optionsState) {
			optionsState(code);
		}
		
		//GAME OVER STATE
		else if (gamePanel.gameState == gamePanel.gameOverState) {
			gameOverState(code);
		}
		
		//TRADE STATE
		else if (gamePanel.gameState == gamePanel.tradeState) {
			tradeState(code);
		}
		
		//MAP STATE
		else if (gamePanel.gameState == gamePanel.mapState) {
			mapState(code);
		}
	}
	
	public void titleState(int code) {
		if(code == KeyEvent.VK_W) { //if W is pressed
			gamePanel.ui.commandNumber--;
			if (gamePanel.ui.commandNumber < 0) {
				gamePanel.ui.commandNumber = 2;
			}
		}
		if(code == KeyEvent.VK_S) { //if S is pressed
			gamePanel.ui.commandNumber++;
			if (gamePanel.ui.commandNumber > 2) {
				gamePanel.ui.commandNumber = 0;
			}
		}
		if(code == KeyEvent.VK_ENTER) { //if P is pressed || pause
			if (gamePanel.ui.commandNumber == 0) {
				gamePanel.gameState = gamePanel.playState;
			}
			if (gamePanel.ui.commandNumber == 1) {
				gamePanel.saveLoad.load();
				gamePanel.gameState = gamePanel.playState;
			}
			if (gamePanel.ui.commandNumber == 2) {
				System.exit(0);
			}
		}
	}
	
	public void playState (int code) {
		//MOVEMENT
		if(code == KeyEvent.VK_W) { //if W is pressed
			upPressed = true;
		}
		if(code == KeyEvent.VK_S) { //if S is pressed
			downPressed = true;
		}
		if(code == KeyEvent.VK_A) { //if A is pressed
			leftPressed = true;
		}
		if(code == KeyEvent.VK_D) { //if D is pressed
			rightPressed = true;
		}
		
		//EXTRA KEYS
		if(code == KeyEvent.VK_C) { 
			gamePanel.gameState = gamePanel.characterState;
		}
		if(code == KeyEvent.VK_P) { //if P is pressed || pause
			gamePanel.gameState = gamePanel.pauseState;
		}
		if(code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
		if(code == KeyEvent.VK_F) { 
			shotKeyPressed = true;
		}
		if(code == KeyEvent.VK_ESCAPE) { 
			gamePanel.gameState = gamePanel.optionsState;
		}
		if(code == KeyEvent.VK_M) { 
			gamePanel.gameState = gamePanel.mapState;
		}
		if(code == KeyEvent.VK_N) { 
			if (gamePanel.map.miniMapOn == false) {
				gamePanel.map.miniMapOn = true;
			} else {
				gamePanel.map.miniMapOn = false;
			}
		}
		
		if(code == KeyEvent.VK_T) {
			if(checkDrawTime == false) {
				checkDrawTime = true;
			}
			else if (checkDrawTime == true){
				checkDrawTime = false;
			}
		}
	}
	
	public void pauseState (int code) {
		if(code == KeyEvent.VK_P) { //if P is pressed || resume
			gamePanel.gameState = gamePanel.playState;
		}
	}
	
	public void dialogueState (int code) {
		if(code == KeyEvent.VK_ENTER) {
			gamePanel.gameState = gamePanel.playState;
		}
	}
	
	public void characterState (int code) {
		if(code == KeyEvent.VK_C) { 
			gamePanel.gameState = gamePanel.playState;
		}
		
		if(code == KeyEvent.VK_ENTER) { 
			gamePanel.player.selectItem();
		}
		playerInventory(code);
	}
	
	public void optionsState(int code) {
		if (code == KeyEvent.VK_ESCAPE) {
			gamePanel.gameState = gamePanel.playState;
		}
		if (code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
		
		int maxCommandNumber = 0;
		switch(gamePanel.ui.subState) {
			case 0: maxCommandNumber = 5; break;
			case 3: maxCommandNumber = 1; break;
		}
		
		if (code == KeyEvent.VK_W) {
			gamePanel.ui.commandNumber--;
			//TODO: SOUND EFFECT FOR GOING THROUGH THE MENU
			if (gamePanel.ui.commandNumber < 0) {
				gamePanel.ui.commandNumber = maxCommandNumber;
			}
		}
		if (code == KeyEvent.VK_S) {
			gamePanel.ui.commandNumber++;
			//TODO: SOUND EFFECT
			if (gamePanel.ui.commandNumber > maxCommandNumber) {
				gamePanel.ui.commandNumber = 0;
			}
		}
		if (code == KeyEvent.VK_A) {
			if (gamePanel.ui.subState == 0) {
				if (gamePanel.ui.commandNumber == 1 && gamePanel.music.volumeScale > 0) {
					gamePanel.music.volumeScale--;
					gamePanel.music.checkVolume();
					//TODO:SOUND EFFECT
				}
				if (gamePanel.ui.commandNumber == 2 && gamePanel.se.volumeScale > 0) {
					gamePanel.se.volumeScale--;
					//TODO:SOUND EFFECT
				}
			}
		}
		if (code == KeyEvent.VK_D) {
			if (gamePanel.ui.subState == 0) {
				if (gamePanel.ui.commandNumber == 1 && gamePanel.music.volumeScale < 5) {
					gamePanel.music.volumeScale++;
					gamePanel.music.checkVolume();
					//TODO:SOUND EFFECT
				}
				if (gamePanel.ui.commandNumber == 2 && gamePanel.se.volumeScale < 5) {
					gamePanel.se.volumeScale++;
					//TODO:SOUND EFFECT
				}
			}	
		}
	}

	public void gameOverState(int code) {
		
		if(code == KeyEvent.VK_W) { //if W is unpressed
			gamePanel.ui.commandNumber--;
			if (gamePanel.ui.commandNumber < 0) {
				gamePanel.ui.commandNumber = 1;
				//TODO: SOUND EFFECT
			}
		}
		if(code == KeyEvent.VK_S) { //if S is unpressed
			gamePanel.ui.commandNumber++;
			if (gamePanel.ui.commandNumber > 1) {
				gamePanel.ui.commandNumber = 0;
			}
		}
		if(code == KeyEvent.VK_ENTER) {
			if (gamePanel.ui.commandNumber == 0) {
				gamePanel.gameState = gamePanel.playState;
				gamePanel.resetGame(false);
				gamePanel.playMusic(0);
			}
			else if (gamePanel.ui.commandNumber == 1) {
				gamePanel.gameState = gamePanel.titleState;
				gamePanel.resetGame(true);
			}
		}
	}
	
	public void mapState(int code) {
		if (code == KeyEvent.VK_M) {
			gamePanel.gameState = gamePanel.playState;
		}
	}
	
	public void tradeState(int code) {

		if (code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
		if (gamePanel.ui.subState == 0) {
			if (code == KeyEvent.VK_W) {
				gamePanel.ui.commandNumber--;
				if (gamePanel.ui.commandNumber < 0) {
					gamePanel.ui.commandNumber = 2;
				}
				//TODO:SOUND EFFECT
			}
			if (code == KeyEvent.VK_S) {
				gamePanel.ui.commandNumber++;
				if (gamePanel.ui.commandNumber > 2) {
					gamePanel.ui.commandNumber = 0;
				}
				//TODO:SOUND EFFECT
			}
		}
		if (gamePanel.ui.subState == 1) {
			npcInventory(code);
			if (code == KeyEvent.VK_ESCAPE) {
				gamePanel.ui.subState = 0;
			}
		}
		if (gamePanel.ui.subState == 2) {
			playerInventory(code);
			if (code == KeyEvent.VK_ESCAPE) {
				gamePanel.ui.subState = 0;
			}
		}
	}
	
	public void playerInventory(int code) {
		
		if(code == KeyEvent.VK_W) {
			if (gamePanel.ui.playerSlotRow != 0) {				
				gamePanel.ui.playerSlotRow--;
				//TODO: ADD CURSOR MOVEMENT SOUND
			}
		}
		if(code == KeyEvent.VK_A) {
			if (gamePanel.ui.playerSlotCol != 0) {
				gamePanel.ui.playerSlotCol--;				
			}
		}
		if(code == KeyEvent.VK_S) {
			if (gamePanel.ui.playerSlotRow != 3) {
				gamePanel.ui.playerSlotRow++;				
			}
		}
		if(code == KeyEvent.VK_D) { 
			if (gamePanel.ui.playerSlotCol != 4) {
				gamePanel.ui.playerSlotCol++;				
			}
		}
	}
	
	public void npcInventory(int code) {
		
		if(code == KeyEvent.VK_W) {
			if (gamePanel.ui.npcSlotRow != 0) {				
				gamePanel.ui.npcSlotRow--;
				//TODO: ADD CURSOR MOVEMENT SOUND
			}
		}
		if(code == KeyEvent.VK_A) {
			if (gamePanel.ui.npcSlotCol != 0) {
				gamePanel.ui.npcSlotCol--;				
			}
		}
		if(code == KeyEvent.VK_S) {
			if (gamePanel.ui.npcSlotRow != 3) {
				gamePanel.ui.npcSlotRow++;				
			}
		}
		if(code == KeyEvent.VK_D) { 
			if (gamePanel.ui.npcSlotCol != 4) {
				gamePanel.ui.npcSlotCol++;				
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) { //listener interface to recieve keyboard events when key is released
		
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_W) { //if W is unpressed
			upPressed = false;
		}
		if(code == KeyEvent.VK_S) { //if S is unpressed
			downPressed = false;
		}
		if(code == KeyEvent.VK_A) { //if A is unpressed
			leftPressed = false;
		}
		if(code == KeyEvent.VK_D) { //if D is unpressed
			rightPressed = false;
		}
		if(code == KeyEvent.VK_F) { 
			shotKeyPressed = false;
		}
		
	} 

}
