import java.util.ArrayList;

import processing.core.*;
import org.json.simple.*;

/**
 * Keeps track of the genotypes of the population - without knowing who is a seeker and who is an avoider, that's a job for BreedingManager - 
 * loading/generating them, saving them, and adding new balls to the game when requested, 
 */
public class GenotypeManager {
	MainApplet parent;	
	int numGenotypes; // How many genotypes do we have?
	
	int currentPos;   // Which is the next one to throw into the game?

	ArrayList<Genotype> genotypes; // My list of genotypes
	BreedingManager myBreedingManager; // Which is generated through BreedingManager
	
	
	GenotypeManager(MainApplet _parent) {
	    parent = _parent;
	    currentPos = 0;
	    myBreedingManager = new BreedingManager(parent);
	    
	}
	
	/**
	 * Load/create a new population, depending on the value of randomStart
	 */
	void newPopulation(Boolean randomStart) {
		if (randomStart)
			randomPopulation(); 
		else
			loadPopulation();
	}
	
	/**
	 * Load a population from the server and create a new generation out of it
	 */
	void loadPopulation(){
		myBreedingManager.loadGenotypes();
	    myBreedingManager.reproduce();
	    myBreedingManager.forgetHistories();  // We have a new generation, forget about which genotypes lived more in the past
	    
	    genotypes = myBreedingManager.allGenotypes(); // Load all genotypes from BreedingManager (in shuffled order, seekers and avoiders together)
	    
	    numGenotypes = genotypes.size();
	    System.out.println("Genotypes used: " + numGenotypes);
	    currentPos = 0;
	}
	
	/**
	 * Generate a random population
	 */
	void randomPopulation(){
		myBreedingManager.loadRandom();
		genotypes = myBreedingManager.allGenotypes();
	    numGenotypes = genotypes.size();
	    System.out.println("Random genotypes created: " + numGenotypes);
	}
	
	/**
	 * Throw a new ball, if there are any
	 */
	BouncingBall newBall() { 
		if (currentPos < numGenotypes) {	// Do we have yet unused genotypes? 
			BouncingBall newBall = new BouncingBall(parent, genotypes.get(currentPos)); // Yes, throw a ball generated from them
			//System.out.format("Given ball %d of %d\n", currentPos+1, numGenotypes);
			currentPos++; 
			return newBall;
		}
		return null; // No genotypes left, sorry
	}
	
	/**
	 * Tell BreedingManager to save the seekers and the avoiders' genotypes back to the server (or to the files, if in local mode)
	 */
	void saveData(){
		myBreedingManager.saveData();
	}
}
