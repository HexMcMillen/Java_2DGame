package tile;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
	
	GamePanel gamePanel;
	public Tile[] tile;
	public int mapTileNumber[][][];
	
	public TileManager(GamePanel gamePanel) {
		
		this.gamePanel = gamePanel;
		
		tile = new Tile[50]; //create 10 types of tiles
		mapTileNumber = new int[gamePanel.maxMap][gamePanel.maxWorldCol] [gamePanel.maxWorldRow]; //convert the map1 text into a map array
		
		//MAPS
		getTileImage();
		loadMap("/maps/worldMap.txt", 0);
		loadMap("/maps/House_Test.txt", 1);
		
	}
	
	public void getTileImage() { //import the images as tile numbers
			
			//placeholders
			setup(0, "plaingrass", false); //Tile Number, file Path name w/o the /tiles/ and .png, collision status
			setup(1, "plaingrass", false);
			setup(2, "plaingrass", false);
			setup(3, "plaingrass", false);
			setup(4, "plaingrass", false);
			setup(5, "plaingrass", false);
			setup(6, "plaingrass", false);
			setup(7, "plaingrass", false);
			setup(8, "plaingrass", false);
			setup(9, "plaingrass", false);
			//placeholders
			
			//grass
			setup(10, "plaingrass", false);
			setup(11, "somegrass", false);
			setup(12, "flowers", false);
			
			//water
			setup(13, "waterplain", true);
			setup(14, "watertopleftcorner", true);
			setup(15, "watertopline", true);
			setup(16, "watertoprightcorner", true);
			setup(17, "waterrightdown", true);
			setup(18, "waterbottomrightcorner", true);
			setup(19, "waterbottomline", true);
			setup(20, "waterbottomleftcorner", true);
			setup(21, "waterleftdown", true);
			
			//brick
			setup(22, "brick", true);

			
			//sand
			setup(23, "sand", false);
			
			//earth
			setup(24, "earth", false);
			
			//tree
			setup(25, "tree", true);
			
			//HOUSE TEST
			setup(26, "HouseTest", false);
			setup(27, "House_WoodFloor", false);
			setup(28, "House_Table", true);
			
	}
	
	public void setup(int index, String imageName, boolean collision ) {
		
		UtilityTool utilityTool = new UtilityTool();
		
		try {
			tile[index] = new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
			tile[index].image = utilityTool.scaledImage(tile[index].image, gamePanel.tileSize, gamePanel.tileSize);
			tile[index].collision = collision;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadMap(String filePath, int map) {
		
		try {
			
			InputStream is = getClass().getResourceAsStream(filePath); //import text file
			BufferedReader br = new BufferedReader(new InputStreamReader(is)); //read the content of the text file
			
			int col = 0;
			int row = 0;
			
			while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) { //this means the screen has no data beyond the boundary of the max screen col and row
				
				String line = br.readLine(); // read a single line of text
				
				while (col < gamePanel.maxWorldCol) {
					
					String numbers[] = line.split(" "); //splits the screen around matches of the given regular expression || reads each number one at a time from left to right and then top to bottom until its over
					
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNumber[map][col][row] = num;
					col++;
				}
				
				if (col == gamePanel.maxWorldCol) {
					col = 0;
					row++;
				}
				
			}
			br.close();
			
		} catch (Exception e) {
			
		}
	}
	
	public void draw(Graphics g2) { //drawing the tile on the screen || implementing camera 
		
		int worldCol = 0;
		int worldRow = 0;
		
		while (worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow) {
			
			int tileNumber = mapTileNumber[gamePanel.currentMap][worldCol][worldRow];
			
			int worldX = worldCol * gamePanel.tileSize;
			int worldY = worldRow * gamePanel.tileSize;
			int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
			int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY; //this will draw the whole map while allowing your character to stay in the center of the screen and move around the map
			
			if (worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX && //create a boundary from the center of the screen around the player || as long as a tile is in the boundary it is drawn || wont draw every tile at all times this way
				worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
				worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
				worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {
				
				g2.drawImage(tile[tileNumber].image, screenX, screenY, null );
			}
			
			worldCol ++;
			
			if (worldCol == gamePanel.maxWorldCol) {
				worldCol = 0;
				worldRow ++;
			}
			
		}
		
	}
}
