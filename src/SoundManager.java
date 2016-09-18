import java.net.URL;

import processing.core.*;

import ddf.minim.*;
 
/**
 * Opens sound files, play them, closes them when we are done.
 *
 */
public class SoundManager {
	PApplet parent;
	AudioPlayer pingPlayer;
	AudioPlayer backgroundPlayer;
	Minim minim; //audio context
	Boolean isSoundOn = true;
	

	 SoundManager(PApplet _parent){
		parent = _parent;
		minim = new Minim(parent);
		pingPlayer = minim.loadFile(MyConstants.PING_FILENAME);
		backgroundPlayer = minim.loadFile(MyConstants.MUSIC_FILENAME);
	}
	
	/**
	 * Start playing the background music
	 */
	void playBackground() {
		backgroundPlayer.loop();
		backgroundPlayer.play();
	}
	
	/**
	 * Toggle sound effect and music on-off
	 */
	void toggleSound() {
		if (backgroundPlayer.isPlaying())
			backgroundPlayer.pause();
		else {
			backgroundPlayer.loop();
			backgroundPlayer.play();
		}
	}
	
	/**
	 * Play the 'ping' sound when a ball is hit
	 */
	void playPing(){
		if (isSoundOn) {
			pingPlayer.rewind();
			pingPlayer.play();
		}
	}
	/**
	 * Close all sound files
	 */
	void closeAll(){
		pingPlayer.close();
		backgroundPlayer.close();
	}
	
}
