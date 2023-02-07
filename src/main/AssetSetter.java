package main;

import entity.NPC_Ninja;
import entity.NPC_Store;
import monster.GreenSlime;
import object.Axe;
import object.Bed;
import object.Boots;
import object.Chest;
import object.Coin_Bronze;
import object.Door;
import object.Health;
import object.KeyGold;
import object.Lantern;
import object.Mana;
import object.Potion_Small;
import object.Shield_Metal;
import object.Sword_Metal;
import tile_interactive.Tree_Dry;

public class AssetSetter {
	
	GamePanel gamePanel;
	
	public AssetSetter(GamePanel gamePanel) {
		
		this.gamePanel = gamePanel;
	}
	
	
	public void setObject() { //instantiate objects and place them on the map
		
		int mapNumber = 0; //worldMap = mapNumber 0
		int i = 0;
		
		gamePanel.obj[mapNumber][i] = new Boots(gamePanel);
		gamePanel.obj[mapNumber][i].worldX = gamePanel.tileSize*41; //RIGHT
		gamePanel.obj[mapNumber][i].worldY = gamePanel.tileSize*4; //DOWN
		i++;
		
		gamePanel.obj[mapNumber][i] = new Lantern(gamePanel);
		gamePanel.obj[mapNumber][i].worldX = gamePanel.tileSize*20;
		gamePanel.obj[mapNumber][i].worldY = gamePanel.tileSize*20;
		i++;

		gamePanel.obj[mapNumber][i] = new Door(gamePanel);
		gamePanel.obj[mapNumber][i].worldX = gamePanel.tileSize*35;
		gamePanel.obj[mapNumber][i].worldY = gamePanel.tileSize*35;
		i++;
		
		gamePanel.obj[mapNumber][i] = new Door(gamePanel);
		gamePanel.obj[mapNumber][i].worldX = gamePanel.tileSize*35;
		gamePanel.obj[mapNumber][i].worldY = gamePanel.tileSize*38;
		i++;
		
		gamePanel.obj[mapNumber][i] = new Chest(gamePanel);
		gamePanel.obj[mapNumber][i].setLoot(new Bed(gamePanel)); //SETS THE LOOT IN THE CHEST
		gamePanel.obj[mapNumber][i].worldX = gamePanel.tileSize*42;
		gamePanel.obj[mapNumber][i].worldY = gamePanel.tileSize*46;
		i++;
	}
	
	public void setNPC() {
		
		//MAP 0
		int mapNumber = 0;
		int i = 0;
		gamePanel.npc[mapNumber][i] = new NPC_Ninja(gamePanel); //instatiate the npc ninja
		gamePanel.npc[mapNumber][i].worldX = gamePanel.tileSize*21; //Set npc position
		gamePanel.npc[mapNumber][i].worldY = gamePanel.tileSize*21;
		i++;
		
		//MAP 1
		mapNumber = 1;
		i = 0;
		gamePanel.npc[mapNumber][i] = new NPC_Store(gamePanel); //instatiate the npc ninja
		gamePanel.npc[mapNumber][i].worldX = gamePanel.tileSize*24; //Set npc position
		gamePanel.npc[mapNumber][i].worldY = gamePanel.tileSize*23;
		i++;
	}
	
	public void setMonster() {
		
		int mapNumber = 0;
		int i = 0;
		
		gamePanel.monster[mapNumber][i] = new GreenSlime(gamePanel);
		gamePanel.monster[mapNumber][i].worldX = gamePanel.tileSize*42;
		gamePanel.monster[mapNumber][i].worldY = gamePanel.tileSize*20;
		i++;
		
		gamePanel.monster[mapNumber][i] = new GreenSlime(gamePanel);
		gamePanel.monster[mapNumber][i].worldX = gamePanel.tileSize*44;
		gamePanel.monster[mapNumber][i].worldY = gamePanel.tileSize*18;
		i++;
		
		gamePanel.monster[mapNumber][i] = new GreenSlime(gamePanel);
		gamePanel.monster[mapNumber][i].worldX = gamePanel.tileSize*39;
		gamePanel.monster[mapNumber][i].worldY = gamePanel.tileSize*20;
		i++;
	}
	
	public void setInteractiveTile() {
		
		int mapNumber = 0;
		int i = 0;
		
		gamePanel.interactTile[mapNumber][i] = new Tree_Dry(gamePanel);
		gamePanel.interactTile[mapNumber][i].worldX = gamePanel.tileSize*7;
		gamePanel.interactTile[mapNumber][i].worldY = gamePanel.tileSize*4;
		i++;
		
		gamePanel.interactTile[mapNumber][i] = new Tree_Dry(gamePanel);
		gamePanel.interactTile[mapNumber][i].worldX = gamePanel.tileSize*7;
		gamePanel.interactTile[mapNumber][i].worldY = gamePanel.tileSize*5;
		i++;
	}
}
