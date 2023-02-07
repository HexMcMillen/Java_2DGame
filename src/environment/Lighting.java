package environment;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Lighting {
	
	GamePanel gamePanel;
	BufferedImage darknessFilter;
	public int dayCounter;
	public float filterAlpha = 0f;
	
	//DAY STATE
	public final int day = 0;
	public final int dusk = 1;
	public final int night = 2;
	public final int dawn = 3;
	public int dayState = day;
	
	//THIS CLASS DRAWS A TRANSPARENT SCREEN ONTOP OF THE ORIGINAL SCREEN TO MAKE EVERYTHING DARK
	//THEN A CIRCLE IS DRAWN IN THE MIDDLE WHERE THE PLAYER IS LOCATED, EVERYTHING IS THAT CIRCLE HAS THE TRANSPARENT SCREEN REMOVED
	//TO SIMULATE LIGHTING WHEN HOLDING SPECIFIC OBJECTS
	
	public Lighting(GamePanel gamePanel) { //CIRCLESIZE IS THE SIZE OF THE LIGHTING AREA
		this.gamePanel = gamePanel;
		
		setLightSource();
	}
	
	public void setLightSource() {
		//CREATE A BUFFERED IMAGE
				darknessFilter = new BufferedImage(gamePanel.screenWidth, gamePanel.screenHeight, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2 = (Graphics2D)darknessFilter.getGraphics();
				
				if (gamePanel.player.currentLight == null) { //PLAYER HAS NO LIGHT SOURCE EQUIPPED
					g2.setColor(new Color(0, 0, 0, 0.9f));
				}
				else { //PLAYER HAS A LIGHTING ITEM EQUIPPED
					
					//FIND CENTER OF CIRCLE
					int centerX = gamePanel.player.screenX + (gamePanel.tileSize)/2;
					int centerY = gamePanel.player.screenY + (gamePanel.tileSize)/2;
					
					//CREATE A GRIDATION EFFECT WITHIN THE LIGHT CIRCLE
					Color color[] = new Color[12];
					float fraction[] = new float[12]; //DISTANCE FROM THE LIGHT CIRCLE
					
					color[0] = new Color(0, 0, 0.1f, 0.1f);
					color[1] = new Color(0, 0, 0.1f, 0.42f);
					color[2] = new Color(0, 0, 0.1f, 0.52f);
					color[3] = new Color(0, 0, 0.1f, 0.61f);
					color[4] = new Color(0, 0, 0.1f, 0.69f);
					color[5] = new Color(0, 0, 0.1f, 0.76f);
					color[6] = new Color(0, 0, 0.1f, 0.82f);
					color[7] = new Color(0, 0, 0.1f, 0.87f);
					color[8] = new Color(0, 0, 0.1f, 0.91f);
					color[9] = new Color(0, 0, 0.1f, 0.94f);
					color[10] = new Color(0, 0, 0.1f, 0.96f);
					color[11] = new Color(0, 0, 0.1f, 0.98f);
					
					fraction[0] = 0f;
					fraction[1] = 0.4f;
					fraction[2] = 0.5f;
					fraction[3] = 0.6f;
					fraction[4] = 0.65f;
					fraction[5] = 0.7f;
					fraction[6] = 0.75f;
					fraction[7] = 0.8f;
					fraction[8] = 0.85f;
					fraction[9] = 0.9f;
					fraction[10] = 0.95f;
					fraction[11] = 1f;
					
					//CREATE A GRIDATION PAINT SETTING FOR THE CIRCLE
					RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, gamePanel.player.currentLight.lightRadius, fraction, color);
					
					//SET THE GRADIENT DATA ON g2
					g2.setPaint(gPaint);
				}
				
				g2.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
				
				g2.dispose();
	}
	
	public void resetDay() {
		dayState = day;
		filterAlpha = 0f;
	}
	
	public void update() { //UPDATE IS CALLED WHENEVER THE PLAYER EQUIPS OR UNEQUIPS A LIGHT SOURCE
		
		if (gamePanel.player.lightUpdated == true) {
			setLightSource();
			gamePanel.player.lightUpdated = false;
		}
		
		//CHECK THE STATE OF THE DAY
		if (dayState == day) {
			
			dayCounter++;
			if (dayCounter > 36000) { //TIME IT TAKES UNTIL THE NEXT DAY STATE || 10 MIN
				dayState = dusk;
				dayCounter = 0;
			}
		}
		
		if (dayState == dusk) {
			
			filterAlpha += 0.0001f; //THIS VALUE IS USED FOR THE DARKNESS FILTER
			if (filterAlpha > 1f) {
				filterAlpha = 1f;
				dayState = night;
			}
		}
		
		if (dayState == night) {
			
			dayCounter++;
			if (dayCounter > 36000) { //10 MIN
				dayState = dawn;
				dayCounter = 0;
			}
		}
		
		if (dayState == dawn) {
			
			filterAlpha -= 0.0001f; //CHANGE NUMBER TO INCREASE/REDUCE THE AMOUNT OF TIME IT TAKES TO REACH THE NEXT DAYSTATE
			if (filterAlpha < 0) {
				filterAlpha = 0;
				dayState = day;
			}
		}
	}
	
	public void draw(Graphics2D g2) {
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
		g2.drawImage(darknessFilter, 0, 0, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		
		//DEBUG
		String situation = "";
		switch(dayState) {
			case day: situation = "Day"; break;
			case dusk: situation = "Dusk"; break;
			case night: situation = "Night"; break;
			case dawn: situation = "Dawn"; break;
		}
		
		g2.setColor(Color.white); 
		g2.setFont(g2.getFont().deriveFont(50f));
		g2.drawString(situation, 800, 500);
	}
}
