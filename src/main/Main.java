package main;

import javax.swing.JFrame;

public class Main {
	
	public static JFrame window;

	public static void main(String[] args) {
	
		//Create a Window
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //close app when the window is closed
		window.setResizable(false); //not able to resize the window
		window.setTitle("My Personal 2D Adventure");
		
		GamePanel gamePanel = new GamePanel(); //Connect GamePanel to Main
		window.add(gamePanel);//add the game panel to the window
		
		gamePanel.config.loadConfig();
		if (gamePanel.fullScreenOn == true) {
			window.setUndecorated(true);
		}
		
		window.pack(); //causes the window to be sized to fit the preferred size and layout of GamePanel
		
		window.setLocationRelativeTo(null); //window will be displayed at the center of the screen
		window.setVisible(true);
		
		gamePanel.setupGame();
		
		gamePanel.startGameThread(); //call the method from GamePanel
		
		//TODO: NAME INNKEEPER AFTER BOYD
	}
}