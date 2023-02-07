package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
	
	GamePanel gamePanel;
	
	public Config(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public void saveConfig() {
		
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("config.txt"));
			
			//FULL SCREEN SETTING
			if (gamePanel.fullScreenOn == true) { bufferedWriter.write("On"); }
			if (gamePanel.fullScreenOn == false) { bufferedWriter.write("Off"); }
			bufferedWriter.newLine();
			
			//MUSIC
			bufferedWriter.write(String.valueOf(gamePanel.music.volumeScale));
			bufferedWriter.newLine();
			
			//SOUND EFFECTS
			bufferedWriter.write(String.valueOf(gamePanel.se.volumeScale));
			bufferedWriter.newLine();
			
			bufferedWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadConfig() {
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("config.txt"));
			String s = bufferedReader.readLine();//READ THE CONFIG FILE AND GET IT AS STRING
			
			//FULL SCREEN
			if (s.equals("On")) { gamePanel.fullScreenOn = true; }
			if (s.equals("Off")) { gamePanel.fullScreenOn = false; }
			
			//MUSIC
			s = bufferedReader.readLine();
			gamePanel.music.volumeScale = Integer.parseInt(s);
			
			//SOUND EFFECTS
			s = bufferedReader.readLine();
			gamePanel.se.volumeScale = Integer.parseInt(s);
			
			bufferedReader.close();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
