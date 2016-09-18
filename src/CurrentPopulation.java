import java.util.ArrayList;

import processing.core.*;

/**
 * Manages the current population of bouncing balls, updating their positions and removing the dead ones
 *
 */
public class CurrentPopulation {
	MainApplet parent;
	GenotypeManager pManager;
	ArrayList<BouncingBall> balls;
	
	CurrentPopulation(MainApplet _parent) {
	    parent = _parent;
	    balls = new ArrayList<BouncingBall>();
	    pManager = new GenotypeManager(_parent);
	  }
	
	  void newPopulation(Boolean randomStart)
	  {
		  pManager.newPopulation(randomStart);
	  }
	
	  /**
	   * Add a new ball (if any are left)
	   */
	  void addBall() {
		  BouncingBall newBall = pManager.newBall(); 
		  if (newBall != null) // No matter, no balls left
			  balls.add(newBall);
	  }
	
	  
	  /**
	   * Update all balls, remove dead ones
	   */
	  void update() {
		  for (int i = balls.size()-1; i >= 0; i--) {
		      BouncingBall b = balls.get(i);
		      b.update();
		      if (b.isDead()) {
		        balls.remove(i);
		      }
		    }
	  }
	  
	  /**
	   * Display all balls
	   */
	  void display() {
		  for (int i = balls.size()-1; i >= 0; i--) {
		      BouncingBall b = balls.get(i);
		      b.display();
		    }
	  }
	  
	  /**
	   * The user clicked somewhere, tell this to all the balls (so they can check if they got hit)
	   */
	  void clicked(float x, float y){ 
		  for (int i = balls.size()-1; i >= 0; i--) {
			  BouncingBall b = balls.get(i);
			  b.clicked(x, y);
		  }
	  }
	  
	  /**
	   * How many balls are present?
	   */
	  int numBalls(){
		  return balls.size();
	  }
	  
	  /**
	   * OK, now let's save the data to the server
	   */
	  void saveData(){
		  pManager.saveData();
	  }	  
}
