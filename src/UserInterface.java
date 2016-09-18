
import processing.core.*;


/***
 * Draws the header, the title, the score and the acknowledgments
 */
public class UserInterface {
	
	MainApplet parent;			// The main PApplet instance on which to draw
	
	UserInterface(MainApplet _parent) {
		parent = _parent;
	}
	
	void draw(Boolean justStarted, Boolean gameOn, Boolean showOff, int maxScore, int curScore, int numBalls, int timeLeft) {
		parent.background(0);
		
		drawHeader(maxScore, curScore, numBalls, timeLeft); //Draw always the header
		
		if ((!gameOn) && (!showOff)){ // We are not playing nor showing seekers and avoiders
			if (justStarted) 		  // We just started, show the intro screen
				showIntro();
			else
				showMessage(maxScore, curScore); // We played before, tell if the score has improved
			
			showAcknowl();
		}
			
	}
	
	// Write the header information: the max score, the current score, the number of balls in play, and the time left
	void drawHeader(int maxScore, int curScore, int numBalls, int timeLeft) {
		parent.fill(255);
		parent.rect(0, 0, parent.width, MyConstants.HEADER_HEIGHT);
		parent.fill(0);
		parent.textSize(MyConstants.TXTSIZE);
		parent.textAlign(PApplet.LEFT);
		parent.text("Best score: " + maxScore, 10, 35); 
		parent.textAlign(PApplet.CENTER);
		parent.text("Current score:" + curScore + "( + " +numBalls+")", parent.width/2, 35);
		parent.textAlign(PApplet.RIGHT); 
		if(timeLeft <= 5)
			parent.fill(255, 0, 0);
		parent.text("Seconds left: " + timeLeft, parent.width-10, 35);
	}
	
	// Write whether the score has improved, remind about commands
	void showMessage(int maxScore, int curScore){
		parent.fill(255);
		parent.textAlign(PApplet.CENTER);
		if (maxScore < curScore) 
			parent.text("Congratulations! Your best score is now " + curScore + "!", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2-150);
		else
			parent.text("Too bad, your best score is still " + maxScore + "...", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2-150);
		
		parent.text("Press space to play again", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2);
		parent.text("'s' for seeing the two populations", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2+50);
		parent.text("'r' for starting from random (erases saved populations)", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2+100);
		
		if (!MyConstants.APPLET_MODE) {
			if (parent.localMode)
				parent.text("Current mode: local", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2+150);
			else
				parent.text("Current mode: non-local", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2+150);
		}
		
	}
	
	// Write the acknowledgments for music and sound effects
	void showAcknowl(){
		parent.fill(255);
		parent.textSize(MyConstants.ACKSIZE);
		parent.textAlign(PApplet.CENTER);
		parent.text("Music: Alexander Blu, Slow Mind (Creative Commons - CC, SA - license)", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2+200);
		parent.text("'Ping' sound effect from http://www.freesfx.co.uk", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2+250);
		parent.text("Press 'M' to mute/unmute", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2+300);
		parent.textSize(MyConstants.TXTSIZE);
	
	}
	
	// Show the start screen
	void showIntro(){
		parent.fill(255);
		parent.textAlign(PApplet.CENTER);
		parent.textSize(MyConstants.TITLESIZE);
		parent.text("VoroBreeder", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2-200);
		parent.textSize(MyConstants.TXTSIZE);
		parent.text("Click on the balls before they fade to restore them", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2 - 100);
		parent.text("Your points grow with the number of balls on screen", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2);
		if (MyConstants.APPLET_MODE)
			parent.text("Press space to begin", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2+100);
		else {
			parent.text("Press space to begin, or 'l' for local mode", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2+100);
			showLocalMode();
		}
	}
	void showLocalMode() { 	// In stand-alone mode, we can choose whether to read/write to server or to local files
		if (parent.localMode)
			parent.text("Current mode: local", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2+150);
		else
			parent.text("Current mode: non-local", parent.width/2, (parent.height - MyConstants.HEADER_HEIGHT)/2+150);
	}
}
