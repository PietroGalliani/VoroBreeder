import processing.core.*;
/**
 * Just a list of game constants, kept in a single file so I don't go mad looking for them
 */
public class MyConstants {
	
    static final Boolean APPLET_MODE = false;	//True to deploy as an applet, false to deploy as an independent application
    
	static final String NONAPPLET_BASE = "http://users.sussex.ac.uk/~pg221/VoroBreeder/"; // Where to find the input files 
	static final String SEEKERS_FILENAME = "seekers.JSON";				// The file containing the list of seekers (on the server)
	static final String AVOIDERS_FILENAME = "avoiders.JSON";			// The file containing the list of avoiders (on the server)
	
	static final int APPLET_WIDTH = 1024;
	static final int APPLET_HEIGHT = 768;
	static final int HEADER_HEIGHT = 50;	// The height of the header 
	static final int ARENA_HEIGHT = APPLET_HEIGHT - HEADER_HEIGHT;
	
	static final int TXTSIZE = 32; 			// The size of the text 
	static final int ACKSIZE = 18;			// The size of acknowledgments
	static final int TITLESIZE= 50;			// the size of the title
	

	static final int SECONDS_PER_GAME = 23; // Seconds in the game (plus one, because at timestep 0 we decrease time by one)
	static final int MILLIS_BETWEEN_STEPS = 10; // Milliseconds between two update steps
	
	
	static final int LIFESPAN = 100; // Number of steps a ball lives for 
	static final float ROTATION = PImage.PI/100;  // How much a ball rotates every step
	static final float DIAMETER = 100; // Ball diameter
	static final float SPEED = 5; // Ball movement speed
	
	/**
	 * IF CHANGING THESE FOUR PROPERTIES, DO NOT LOAD FROM SERVER - CREATE NEW RANDOM POPULATION WITH 'r'
	 */
	static final int NUMSEEKERS = 10;	// Number of seekers
	static final int NUMAVOIDERS = 10;  // Number of avoiders
	static final int NUMSEEKERCHILDREN = 7; // Number of seeker children (< NUMSEEKERS, the difference is kept)
	static final int NUMAVOIDERCHILDREN = 7; // Number of avoider children (< NUMAVOIDERS, the difference is kept)
	
	
	static final float PMUTATE = (float) 0.02; 	// Mutation probability (per gene)
	static final float SIGMAMUTATE_COL = 20; 	// Standard deviation in color mutations (range 0-255 for R, G, B)
	static final float SIGMAMUTATE_ANGLE = PApplet.PI/12; // Standard deviation in angle mutations (for points)
	static final float SIGMAMUTATE_R = 10; // Standard deviation in distance from center mutation (for points)
	
	static final int NUM_TO_SHOW = 10;  // Number of seekers and avoiders to show
	
	static final int MAXCOLORS = 10;	// When creating a random genotype, select at most 10 colors
	static final int MAXPOINTS = 10;	// and at most 10 points
	
	// Filenames for sound effects
	static final String PING_FILENAME = "pingSound.mp3";
	static final String MUSIC_FILENAME = "AlexanderBluSlowMind.mp3";
	

}
