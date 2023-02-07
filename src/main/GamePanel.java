package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import ai.PathFinder;
import data.SaveLoad;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import tile.Map;
import tile.TileManager;
import tile_interactive.InteractiveTile;

public class GamePanel extends JPanel implements Runnable { // Subclass of JPanel to give it all of its functions ||
															// Runnable allows the use of Thread

	// SCREEN SETTINGS
	final int originalTileSize = 16; // 16x16 default tile size
	final int scale = 3;

	public final int tileSize = originalTileSize * scale; // 48x48 tile size
	public final int maxScreenCol = 20; // 16 tiles horizontally
	public final int maxScreenRow = 12; // 12 tiles vertically

	public final int screenWidth = tileSize * maxScreenCol; // 960 pixels horizontally
	public final int screenHeight = tileSize * maxScreenRow; // 576 pixels vertically

	// WORLD SETTINGS
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int maxMap = 10; //CAN CREATE THIS MANY DIFFERENT MAPS
	public int currentMap = 0; //CURRENT MAP NUMBER

	// FOR FULL SCREEN
	int screenWidth2 = screenWidth;
	int screenHeight2 = screenHeight;
	BufferedImage tempScreen;
	Graphics2D g2;
	public boolean fullScreenOn = false;

	// FPS
	int FPS = 60;

	// SYSTEM
	public TileManager tileManager = new TileManager(this); // instanciate Tile Manager
	public KeyHandler keyHandler = new KeyHandler(this);// instantiate keyHandler
	Sound music = new Sound();// instantiate sound class
	Sound se = new Sound();
	public CollisionChecker collisionChecker = new CollisionChecker(this); // instantiate Collision Checker
	public AssetSetter assetSetter = new AssetSetter(this); // Instantiate Asset Setter Class
	public UI ui = new UI(this);// instantiate UI class
	public EventHandler eventHandler = new EventHandler(this);
	Config config = new Config(this);
	public PathFinder pathFinder = new PathFinder(this);
	EnvironmentManager environmentManager = new EnvironmentManager(this);
	Map map = new Map(this);
	SaveLoad saveLoad = new SaveLoad(this);
	Thread gameThread;// gives the game real time || Can be stopped and started

	// ENTITY AND OBJECT
	public Player player = new Player(this, keyHandler); // Instantiate the player class
	public Entity obj[][] = new Entity[maxMap][20]; // 10 means to prepare 10 slots for objects [0-9] || Can display up to 10 objects at the same time
	public Entity npc[][] = new Entity[maxMap][10];
	public Entity monster[][] = new Entity[maxMap][20];
	public InteractiveTile interactTile[][] = new InteractiveTile[maxMap][50];
	public ArrayList<Entity> projectileList = new ArrayList<>();
	public ArrayList<Entity> particleList = new ArrayList<>();
	ArrayList<Entity> entityList = new ArrayList<>();

	// GAME STATE || Pause and Resume the game
	public int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3;
	public final int characterState = 4;
	public final int optionsState = 5;
	public final int gameOverState = 6;
	public final int transitionState = 7;
	public final int tradeState = 8;
	public final int sleepState = 9;
	public final int mapState = 10;

	// Constructor
	public GamePanel() {

		this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // sets the size of this class (JPanel)
		this.setBackground(Color.black); // sets the background color to black
		this.setDoubleBuffered(true); // if true, all drawing for this component will be done in an offsceen painting buffer || better rendering performance

		this.addKeyListener(keyHandler); // allow the game panel to recognize key input
		this.setFocusable(true); // game panel can be focused to recieve key input
	}

	public void setupGame() {

		assetSetter.setObject();
		assetSetter.setNPC();
		assetSetter.setMonster();
		assetSetter.setInteractiveTile();
		environmentManager.setup();
		playMusic(0);
		gameState = titleState;

		tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB); // BUFFERED IMAGE AS
																								// LARGE AS THE SCREEN
		g2 = (Graphics2D) tempScreen.getGraphics(); // DRAW EVERYTHING ONTO THE TEMP SCREEN
		
		if (fullScreenOn == true) {
			setFullScreen();			
		}
	}
	
	public void resetGame(boolean restart) {
		
		player.setDefaultPositions();
		player.restoreStatus();
		assetSetter.setNPC();
		assetSetter.setMonster();
		
		if (restart == true) {
			
			player.setDefaultValues();
			assetSetter.setObject();
			assetSetter.setInteractiveTile();
			environmentManager.lighting.resetDay();
		}
	}
	
	public void setFullScreen() {
		
		//GET PRESONAL SCREEN
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(Main.window);
		
		//GET FULL SCREEN WIDTH AND HEIGHT
		screenWidth2 = Main.window.getWidth();
		screenHeight2 = Main.window.getHeight();
	}

	public void startGameThread() {

		gameThread = new Thread(this); // passing GamePanel(this) through the Thread
		gameThread.start(); // call on the run method
	}

	@Override
	public void run() { // Game loop

		double drawInterval = 1000000000 / FPS; // 1 seconds or 1 billion nanoseconds || 1/60
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		

		while (gameThread != null) {

			currentTime = System.nanoTime();
			
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;
			
			if (delta >= 1) {
				update();
				drawToTempScreen(); //DRAW EVERYTHING TO THE BUFFERED IMAGE
				drawToScreen(); //DRAW BUFFERED IMAGE TO THE SCREEN
				delta--;
				drawCount++;
			}
			
			if (timer >= 1000000000) {
				drawCount = 0;
				timer = 0;
			}
		}
	}

	public void update() { // change player position by the x and y axis'

		if (gameState == playState) {

			// PLAYER
			player.update(); // call the update method from Player class

			// NPC
			for (int i = 0; i < npc[1].length; i++) {
				if (npc[currentMap][i] != null) {
					npc[currentMap][i].update();
				}
			}

			// MONSTER
			for (int i = 0; i < monster[1].length; i++) {
				if (monster[currentMap][i] != null) {
					if (monster[currentMap][i].alive == true && monster[currentMap][i].dying == false) { // DEATH ANIMATION
						monster[currentMap][i].update();
					}
					if (monster[currentMap][i].alive == false) {
						monster[currentMap][i].checkDrop(); // CHECK THE MOSTER DROPS BEFORE ITS COMPLETE DEATH
						monster[currentMap][i] = null;
						;
					}
				}
			}

			// PROJECTILE
			for (int i = 0; i < projectileList.size(); i++) {
				if (projectileList.get(i) != null) {
					if (projectileList.get(i).alive == true) {
						projectileList.get(i).update();
					}
					if (projectileList.get(i).alive == false) {
						projectileList.remove(i);
						;
					}
				}
			}

			// PARTICLES
			for (int i = 0; i < particleList.size(); i++) {
				if (particleList.get(i) != null) {
					if (particleList.get(i).alive == true) {
						particleList.get(i).update();
					}
					if (particleList.get(i).alive == false) {
						particleList.remove(i);
						;
					}
				}
			}

			// INTERACTIVE TILES
			for (int i = 0; i < interactTile[1].length; i++) {
				if (interactTile[currentMap][i] != null) {
					interactTile[currentMap][i].update();
				}
			}
			
			//LIGHT SOURCE
			environmentManager.update();
		}
		if (gameState == pauseState) {
			// NOTHING
		}
	}

	public void drawToTempScreen() {
		// DEBUG
		long drawStart = 0;
		if (keyHandler.checkDrawTime == true) {
			drawStart = System.nanoTime();
		}

		// TITLE SCREEN
		if (gameState == titleState) {

			ui.draw(g2);

		}
		
		//MAP SCREEN
		else if (gameState == mapState) {
			map.drawMapScreen(g2);
		}

		// OTHERS
		else {

			// TILES
			tileManager.draw(g2); // needs to be before the player so the player is ontop of the background

			// INTERACTIVE TILES
			for (int i = 0; i < interactTile[1].length; i++) {
				if (interactTile[currentMap][i] != null) {
					interactTile[currentMap][i].draw(g2);
				}
			}

			// ADD ENTITIES TO THE LIST
			entityList.add(player);

			// NPC
			for (int i = 0; i < npc[1].length; i++) {
				if (npc[currentMap][i] != null) {
					entityList.add(npc[currentMap][i]);
				}
			}

			// OBJECT
			for (int i = 0; i < obj[1].length; i++) {
				if (obj[currentMap][i] != null) {
					entityList.add(obj[currentMap][i]);
				}
			}

			// MONSTER
			for (int i = 0; i < monster[1].length; i++) {
				if (monster[currentMap][i] != null) {
					entityList.add(monster[currentMap][i]);
				}
			}

			// PROJECTILE
			for (int i = 0; i < projectileList.size(); i++) {
				if (projectileList.get(i) != null) {
					entityList.add(projectileList.get(i));
				}
			}

			// PARTICLES
			for (int i = 0; i < particleList.size(); i++) {
				if (particleList.get(i) != null) {
					entityList.add(particleList.get(i));
				}
			}

			// SORTING THE ORDER OF ARRAYLIST BY WORLDY
			Collections.sort(entityList, new Comparator<Entity>() {
				@Override
				public int compare(Entity e1, Entity e2) {
					int result = Integer.compare(e1.worldY, e2.worldY);
					return result;
				}
			});

			// DRAW ENTITIES
			for (int i = 0; i < entityList.size(); i++) {
				entityList.get(i).draw(g2);
			}

			// EMPTY ENTITY LIST
			entityList.clear();
			
			//ENVIRONMENT
			environmentManager.draw(g2);
			
			//MINI MAP
			map.drawMiniMap(g2);

			// UI
			ui.draw(g2);
		}

		// DeBug
		if (keyHandler.checkDrawTime == true) {
			long drawEnd = System.nanoTime();
			long passed = drawEnd - drawStart;// this will tell us how long it takes to draw all of the components
			g2.setColor(Color.white);
			int x = 10;
			int y = 400;
			int lineHeight = 20;
			
			g2.drawString("World X: " + player.worldX, x, y); y += lineHeight;
			g2.drawString("World Y: " + player.worldY, x, y); y += lineHeight;
			g2.drawString("Col: " + (player.worldX + player.solidArea.x)/tileSize, x, y); y += lineHeight;
			g2.drawString("Row: " + (player.worldY + player.solidArea.y)/tileSize, x, y); y += lineHeight;
			g2.drawString("Draw Time: " + passed, x, y);
		}
	}
	
	public void drawToScreen() {
		
		Graphics g = getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
		g.dispose();
	}

	public void playMusic(int i) { // play anything that needs to loop
		music.setFile(i);
		music.play();
		music.loop();
	}

	public void stopMusic() {
		music.stop();
	}

	public void playSE(int i) { // Sound Effect
		se.setFile(i);
		se.play();
	}
}
