import java.util.ArrayList;

import processing.core.*;

/**
 * This is for showing on the screen first a number of seekers, then a number of avoiders
 */
public class ShowoffManager {
	MainApplet parent;
	BreedingManager br;
	
	int phase = 0; // Phase 0 = we are not showing anything; Phase 1 = we are showing seekers; Phase 2 = we are showing avoiders
	
	ArrayList<BouncingBall> balls; // The balls which we are showing
	
	ShowoffManager(MainApplet _parent) {
		parent = _parent;
		br = new BreedingManager(parent);
		balls = new ArrayList<BouncingBall>();
	}
	
	void next() {	// Move to next phase
		if (phase == 0) {
			
			phase = 1; // Now we show the seekers
			br.loadGenotypes();
			balls.clear();
			for (int i = 0; i < MyConstants.NUM_TO_SHOW; i++)
				balls.add(new BouncingBall(parent, br.seekers.get(i)));

		} else if (phase == 1) { //Now we show the receivers
			phase = 2;
			balls.clear();
			for (int i = 0; i < MyConstants.NUM_TO_SHOW; i++)
				balls.add(new BouncingBall(parent, br.avoiders.get(i)));
		}
		else { //We are finished, tell the applet so
			phase = 0;
			parent.done_showing();
		}
	}
	
	/**
	 * Show all the balls in our list, all together, and write if they are seekers or avoiders
	 */
	void show() { 
		for (int i = 0; i < balls.size(); i++)
			balls.get(i).display();
		parent.fill(255);
		parent.textAlign(PApplet.CENTER);
		if (phase == 1) // 
			parent.text("These were attention seekers", parent.width/2, parent.height/2-100);
		else
			parent.text("These were attention avoiders", parent.width/2, parent.height/2-100);
		parent.text("Press space or 's' to continue...", parent.width/2, parent.height/2+100);
		parent.fill(0);
	}
}
