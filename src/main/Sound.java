package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

	Clip clip; //used to open audio files
	URL soundURL[] = new URL[30];
	FloatControl floatControl; //ACCEPTS NUMBERS FROM -80f(low) TO 6f(high)
	int volumeScale = 3;
	float volume;
	
	public Sound() {
		
		soundURL[0] = getClass().getResource("/sound/ChillAdventure2.wav");
		soundURL[1] = getClass().getResource("/sound/Coin2.wav");
		soundURL[2] = getClass().getResource("/sound/OpenDoor.wav");
		soundURL[3] = getClass().getResource("/sound/UnlockDoor.wav");
		soundURL[4] = getClass().getResource("/sound/Powerup.wav");
		soundURL[5] = getClass().getResource("/sound/Stairs2.wav");
		soundURL[6] = getClass().getResource("/sound/damageMonster.wav");
		soundURL[7] = getClass().getResource("/sound/receiveDamage.wav");
		soundURL[8] = getClass().getResource("/sound/swingSword.wav");
		//TODO:CUTTING DOWN TREES ETC.
		//SLEEPING
		
	}
	
	public void setFile(int i) {
		
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais); //Opening an audio file
			floatControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
			checkVolume();
			
		} catch (Exception e) {
			
		}
	}
	
	public void play() {
		clip.start();
		
	}
	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		
	}
	
	public void stop() {
		clip.stop();
		
	}
	
	public void checkVolume() {
		
		switch(volumeScale) {
			case 0: volume = -80f; break;
			case 1: volume = -20f; break;
			case 2: volume = -12f; break;
			case 3: volume = -5f; break;
			case 4: volume = 1f; break;
			case 5: volume = 6f; break;
		}
		floatControl.setValue(volume);
	}
}
