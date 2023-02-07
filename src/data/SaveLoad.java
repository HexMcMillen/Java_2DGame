package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import entity.Entity;
import main.GamePanel;
import object.Axe;
import object.Bed;
import object.KeyGold;
import object.Lantern;
import object.Potion_Small;
import object.Shield_Metal;
import object.Shield_Wood;
import object.Sword_Metal;
import object.Sword_Normal;

public class SaveLoad {
	
	GamePanel gamePanel;
	
	public SaveLoad(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public Entity getObject(String itemName) {
		
		Entity obj = null;
		
		switch(itemName) {
			case "Axe": obj = new Axe(gamePanel); break;
			case "Bed": obj = new Bed(gamePanel); break;
			case "Key": obj = new KeyGold(gamePanel); break;
			case "Lantern": obj = new Lantern(gamePanel); break;
			case "Small Potion": obj = new Potion_Small(gamePanel); break;
			case "Metal Shield": obj = new Shield_Metal(gamePanel); break;
			case "Wooden Shield": obj = new Shield_Wood(gamePanel); break;
			case "Metal Sword": obj = new Sword_Metal(gamePanel); break;
			case "Wooden Sword": obj = new Sword_Normal(gamePanel); break;
		}
		return obj;
	}
	
	public void save() {
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));
			
			DataStorage dataStorage = new DataStorage();
			
			//PLAYER STATS
			dataStorage.level = gamePanel.player.level;
			dataStorage.maxLife = gamePanel.player.maxLife;
			dataStorage.Life = gamePanel.player.life;
			dataStorage.maxMana = gamePanel.player.maxMana;
			dataStorage.mana = gamePanel.player.mana;
			dataStorage.strength = gamePanel.player.strength;
			dataStorage.dexterity = gamePanel.player.dexterity;
			dataStorage.exp = gamePanel.player.exp;
			dataStorage.nextLevelExp = gamePanel.player.nextLevelExp;
			dataStorage.coin = gamePanel.player.coin;
			
			//PLAYER INVENTORY
			for (int i = 0; i < gamePanel.player.inventory.size(); i++) {
				dataStorage.itemNames.add(gamePanel.player.inventory.get(i).name);
				dataStorage.itemAmounts.add(gamePanel.player.inventory.get(i).amount);
			}
			
			//PLAYER EQUIPMENT
			dataStorage.currentWeaponSlot = gamePanel.player.getCurrentWeaponSlot();
			dataStorage.currentShieldSlot = gamePanel.player.getCurrentShieldSlot();
			
			//MAP OBJECTS
			dataStorage.mapObjectNames = new String[gamePanel.maxMap][gamePanel.obj[1].length];
			dataStorage.mapObjectWorldX = new int[gamePanel.maxMap][gamePanel.obj[1].length];
			dataStorage.mapObjectWorldY = new int[gamePanel.maxMap][gamePanel.obj[1].length];
			dataStorage.mapObjectLootNames = new String[gamePanel.maxMap][gamePanel.obj[1].length];
			dataStorage.mapObjectOpened = new boolean[gamePanel.maxMap][gamePanel.obj[1].length];
			
			for(int mapNum = 0; mapNum < gamePanel.maxMap; mapNum++) {
				
				for(int i = 0; i < gamePanel.obj[1].length; i++) {
					
					if (gamePanel.obj[mapNum][i] == null) {
						dataStorage.mapObjectNames[mapNum][i] = "NA";
					} else {
						dataStorage.mapObjectNames[mapNum][i] = gamePanel.obj[mapNum][i].name;
						dataStorage.mapObjectWorldX[mapNum][i] = gamePanel.obj[mapNum][i].worldX;
						dataStorage.mapObjectWorldY[mapNum][i] = gamePanel.obj[mapNum][i].worldY;
						if (gamePanel.obj[mapNum][i].loot != null) {
							dataStorage.mapObjectLootNames[mapNum][i] = gamePanel.obj[mapNum][i].loot.name;
						}
						dataStorage.mapObjectOpened[mapNum][i] = gamePanel.obj[mapNum][i].opened;
					}
				}
			}
			
			//WRITE DATASTORAGE OBJECT
			oos.writeObject(dataStorage);
			
		} catch(Exception e) {
			System.out.println("Save Exception!");
		}
	}
	
	public void load() {
		
		try {
			
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("save.dat")));
			
			//READ THE DATASTORAGE
			DataStorage dataStorage = (DataStorage)ois.readObject();
			
			//PLAYER STATS
			gamePanel.player.level = dataStorage.level;
			gamePanel.player.maxLife = dataStorage.maxLife;
			gamePanel.player.life = dataStorage.Life;
			gamePanel.player.maxMana = dataStorage.maxMana;
			gamePanel.player.mana = dataStorage.mana;
			gamePanel.player.strength = dataStorage.strength;
			gamePanel.player.dexterity = dataStorage.dexterity;
			gamePanel.player.exp = dataStorage.exp;
			gamePanel.player.nextLevelExp = dataStorage.nextLevelExp;
			gamePanel.player.coin = dataStorage.coin;
			
			//PLAYER INVENTORY
			gamePanel.player.inventory.clear();
			for( int i = 0; i < dataStorage.itemNames.size(); i++) {
				gamePanel.player.inventory.add(getObject(dataStorage.itemNames.get(i)));
				gamePanel.player.inventory.get(i).amount = dataStorage.itemAmounts.get(i);
			}
			
			//PLAYER EQUIPMENT
			gamePanel.player.currentWeapon = gamePanel.player.inventory.get(dataStorage.currentWeaponSlot);
			gamePanel.player.currentShield = gamePanel.player.inventory.get(dataStorage.currentShieldSlot);
			gamePanel.player.getAttack();
			gamePanel.player.getDefense();
			gamePanel.player.getPlayerAttackImage();
			
			//MAP OBJECTS
			for(int mapNum = 0; mapNum < gamePanel.maxMap; mapNum++) {
				
				for(int i = 0; i < gamePanel.obj[1].length; i++) {
					
					if(dataStorage.mapObjectNames[mapNum][i].equals("NA")) {
						gamePanel.obj[mapNum][i] = null;
					} else {
						gamePanel.obj[mapNum][i] = getObject(dataStorage.mapObjectNames[mapNum][i]);
						gamePanel.obj[mapNum][i].worldX = dataStorage.mapObjectWorldX[mapNum][i];
						gamePanel.obj[mapNum][i].worldY = dataStorage.mapObjectWorldY[mapNum][i];
						if(dataStorage.mapObjectNames[mapNum][i] != null) {
							gamePanel.obj[mapNum][i].loot = getObject(dataStorage.mapObjectLootNames[mapNum][i]);
						}
						gamePanel.obj[mapNum][i].opened = dataStorage.mapObjectOpened[mapNum][i];
						if(gamePanel.obj[mapNum][i].opened == true) {
							gamePanel.obj[mapNum][i].down1 = gamePanel.obj[mapNum][i].image2;
						}
					}
				}
			}
			
		} catch (Exception e) {
			System.out.println("Load Excpetion!");
		}
	}
}
