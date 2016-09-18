import processing.core.*;

/***
 * 
 * The main applet class. Mostly it interfaces with the other classes of the program, passing messages back and forth, 
 * and takes care of user input.
 *
 */
public class MainApplet extends PApplet {
	UserInterface myUI;
	MainGame myGame;
	SoundManager mySoundManager;
	ShowoffManager myShowoffManager;
	
	DisposeHandler dh; // For closing sound files properly when we exit

	
	/**
	 * Initial status
	 */
	
	Boolean just_started = true;
	Boolean game_active = false;
	Boolean showoff_active = false;
	Boolean localMode = false; // If true, write and read to local file rather than server

	
	int best_score = 0;
	int cur_score = 0; 
	int time_left = MyConstants.SECONDS_PER_GAME-1;
	float last_tick = 0;
	float last_update = 0;
	int numBalls = 0;

	
	/**
	 * Set up the window, create instances of the other classes
	 */
	public void setup() {
		  size(1024, 768);
		  myUI = new UserInterface(this);
		  myGame = new MainGame(this);
		  cursor(CROSS);
		  mySoundManager = new SoundManager(this);
		  myShowoffManager = new ShowoffManager(this);
		  mySoundManager.playBackground();
		  dh = new DisposeHandler(this);
	}
	
	public void draw() {
		// Draw interface
		myUI.draw(just_started, game_active, showoff_active, best_score, cur_score, numBalls, time_left);
		

		if (showoff_active) // we are showing seekers and avoiders
			myShowoffManager.show();
		
		if  (game_active) { // we are playing
			myGame.draw();
			if ((millis() - last_tick >= 1000)) { // A second elapsed
				
				time_left--; // update seconds left 
				
				myGame.throwBall(); // Add a new ball (if any left)
				if (time_left == 0) { // The game has ended
					game_active = false;
					myGame.saveData();
				}
				
				// Update number of balls and score
				numBalls = myGame.numBalls(); 
				cur_score += numBalls; 
				
				last_tick = millis(); // Remember when we last counted a second
			}
		
			if (millis() - last_update > MyConstants.MILLIS_BETWEEN_STEPS) { // Time for an update step
				myGame.update();
				last_update = millis();
			}
		}
	}
	
	@Override
	public void mouseClicked() {
		if (game_active)  // If the game is playing 
			myGame.clicked(mouseX, mouseY); // Pass the coordinates to the MainGame instance
		
	}
	
	/**
	 * Start a new game
	 * @param randomStart Are we starting from random populations?
	 */
	void startGame(Boolean randomStart){
		game_active = true; // we are playing
		
		if (just_started)   // do not show the intro screen anymore
			just_started = false;
		
		if (cur_score > best_score) // update the best score, if necessary
			best_score = cur_score;
		
		cur_score = 0; // reset the current score
		
		myGame.newPopulation(randomStart); // Load/create a new population
		
		time_left = MyConstants.SECONDS_PER_GAME; // start with 23 seconds left
	}
	
	@Override
	public void keyPressed(){
		if ((key=='m') || (key =='M')) {
			mySoundManager.toggleSound(); // toggle sound on/off
		}
		
		if ((key =='s') && (game_active == false)) { // show seekers and avoiders (if not playing)
			showoff_active = true; 
			myShowoffManager.next();
		}
		
		if (key ==' ') {
			if (showoff_active)					// if showing seekers and avoiders, go on
				myShowoffManager.next();
			
			else if (!game_active) {			// otherwise, start game (if it is not already playing)
				startGame(false);				// false = load from server
			}
		}
		
		if ((key == 'r') && (!showoff_active) && (!game_active)) { 
			startGame(true);					// true = start with random population 
		}
		
		if ((key == 'l') && (!game_active) && (!MyConstants.APPLET_MODE)) {
			localMode = !localMode; // In stand-alone mode, we can choose whether to read and write to local files rather than on the server.
		}
	}
	
	void playPing() {	// The game is telling us to play `ping'
		mySoundManager.playPing();  
	}
	void closeMusic(){  // We are exiting, close all music files
		mySoundManager.closeAll();
	}
	
	void done_showing() { // We are done showing seekers and avoiders, stop that
		showoff_active = false;
	}
}
