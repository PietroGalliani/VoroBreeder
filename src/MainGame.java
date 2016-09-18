import processing.core.*;

/***
 * The main class for taking care of game events. 
 * 
 * Nothing much to say about this, it just passes messages along to CurrentPopulation - 
 * mostly, it's here to provide some encapsulation
 *
 */
public class MainGame {
MainApplet parent;
CurrentPopulation myPopulation;

	MainGame(MainApplet _parent) {
		parent = _parent;
		myPopulation = new CurrentPopulation(parent);
	}
	
	/**
	 * Let's load/generate a new population
	 */
	void newPopulation(Boolean randomStart)
	{
		myPopulation.newPopulation(randomStart);
	}
	
	/**
	 * Let's draw the individuals on the screen
	 */
	void draw() {
		myPopulation.display();
	}
	
	/**
	 * Let's update the individuals
	 */
	void update() {
		myPopulation.update();
	}
	
	/**
	 * Let's add a new ball (if there are any left)
	 */
	void throwBall() {
		myPopulation.addBall();
	}
	
	/**
	 * The user clicked somewhere - did they hit a ball?
	 */
	void clicked(float mouseX, float mouseY){
		myPopulation.clicked(mouseX, mouseY);
	}
	
	/**
	 * How many balls are currently alive?
	 */
	int numBalls(){
		return myPopulation.numBalls();
	}
	
	/**
	 * OK, now let's save the data to the server
	 */
	void saveData(){
		myPopulation.saveData();
	}

}
