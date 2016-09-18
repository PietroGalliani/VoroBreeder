import java.util.ArrayList;

import processing.core.PApplet;


/**
 * Takes care of extracting (through the roulette wheel method) genotypes, reproducing them, and adding mutations
 *
 */
public class CrossoverMaker {
	MainApplet parent;
	
	
	CrossoverMaker(MainApplet _parent) {
		parent = _parent;
	}
	
	
	void mutateGenotype(Genotype g){
		GeneData d = g.geneData;			// Load the genotype data
		
		for (int i = 0; i < d.ncolors; i++) {		
			if (parent.random(1)<MyConstants.PMUTATE) { // Mutate all colors with probability PMUTATE
				
				float red = parent.red(d.colors[i]);
				float green = parent.green(d.colors[i]);
				float blue = parent.blue(d.colors[i]);
														// Mutate the red, green, blue tones
				float newred =   limit(red + parent.randomGaussian()*MyConstants.SIGMAMUTATE_COL, 0, 255);
				float newgreen = limit(green + parent.randomGaussian()*MyConstants.SIGMAMUTATE_COL, 0, 255); 
				float newblue =  limit(blue + parent.randomGaussian()*MyConstants.SIGMAMUTATE_COL, 0, 255);
				//System.out.format("Mutated color: (%f, %f, %f) -> (%f, %f, %f)\n", red, green, blue, newred, newgreen, newblue);
														// Store the new colors 
				d.colors[i] = parent.color(newred, newgreen, newblue);
			}
		}
		
		for (int i = 0; i < d.npoints;i++) {
			if (parent.random(1)<MyConstants.PMUTATE) {	// Mutate the angles of all points with probability PMUTATE
				//System.out.format("Mutated theta: %f", d.thetas[i]);
				
				d.thetas[i] += parent.randomGaussian()*MyConstants.SIGMAMUTATE_ANGLE;  // Mutate the angle 
				
				// Keep the range in 0-2PI, looping if necessary
				if (d.thetas[i] > 2 * PApplet.PI)
					  d.thetas[i] -= 2*PApplet.PI; 
				if (d.thetas[i]<0)
					d.thetas[i] += 2*PApplet.PI;
				
				//System.out.format(" -> %f\n", d.thetas[i]);
			}
			if (parent.random(1)<MyConstants.PMUTATE) { // Mutate the radii of all points with probability PMUTATE
				//System.out.format("Mutated r: %f", d.rs[i]);
				d.rs[i] = limit(d.rs[i] + parent.randomGaussian()*MyConstants.SIGMAMUTATE_R, 0, 100);
				//System.out.format("-> %f\n", d.rs[i]);
			}
		}
	}

	/** 
	 * Just a simple utility function, to keep xv in the range minv-maxv
	 */
	float limit(float xv, float minv, float maxv) {
		if (xv < minv)
			return minv;
		if (xv > maxv)
			return maxv; 
		return xv;
	}

	/**
	 * Create num_children children of the seekers through crossover
	 */
	ArrayList<Genotype> crossoverSeekers(ArrayList<Genotype> seekers, int num_children) {
		ArrayList<Float> weights = new ArrayList<Float>();
		
		for (int i = 0; i < seekers.size(); i++) {
			weights.add((float) seekers.get(i).lifetime); // A seeker genotype is more likely to reproduce if it had higher lifetime 
		}
		
		return crossoverGenotypes(seekers, weights, num_children); // Create the children
	}
	
	/**
	 * Create num_children children of the avoiders through crossover
	 */
	ArrayList<Genotype> crossoverAvoiders(ArrayList<Genotype> avoiders, int num_children) {
		ArrayList<Float> weights = new ArrayList<Float>();
		
		for (int i = 0; i < avoiders.size(); i++) {
			weights.add(1/((float) avoiders.get(i).lifetime)); // An avoider genotype is more likely to reproduce if it had lower lifetime
		}
		
		return crossoverGenotypes(avoiders, weights, num_children); // Create the children
	}
	
	/**
	 * Create num_children children of a population of genotypes, given the weights of each genotype - higher-weight
	 * genotypes are more likely to be selected for reproduction
	 */
	ArrayList<Genotype> crossoverGenotypes(ArrayList<Genotype> genotypes, ArrayList<Float> weights, int num_children) {
		ArrayList<Genotype> children = new ArrayList<Genotype>();
		
		/*
		 * Normalize the weights
		 */
		float sum_weights = 0;
		for (int i = 0; i < weights.size(); i++)
			sum_weights +=weights.get(i);
		
		ArrayList<Float> normalizedWeights = new ArrayList<Float>();
		
		for (int i = 0; i < weights.size(); i++)
			normalizedWeights.add(weights.get(i)/sum_weights);
		
		/**
		 * For each child, extract two parents and make the crossover
		 */
		for (int c = 0; c < num_children; c++) {
			Genotype g1 = genotypes.get(RouletteSel(normalizedWeights));
			Genotype g2 = genotypes.get(RouletteSel(normalizedWeights));
			children.add(makeCrossover(g1,g2));
		}
		
		return children;
	}
	
	/***
	 * Roulette selection
	 * Weights should sum to one
	 * Returns the index of the extracted element
	 */
	int RouletteSel(ArrayList<Float> weights) {		
		
		float extracted = parent.random(0,1);			// Start from a random number in 0-1
				
		for (int i = 0; i < weights.size(); i++) {      // keep removing weights until it gets beneath zero. 
			extracted = extracted - weights.get(i);		// As the weights sum to one, bigger weights are more likely to be selected
			if (extracted <= 0)
				return i;
		}
		
		// If we get here, something went terribly wrong 
		System.out.println("AAARGH! Roulette Selection did not work (perhaps the weights do not sum to one?");
		System.out.println("Weights: " + weights.toString());
		return 0;
	}
	
	/*
	 * Crossover two genotypes and add random mutation
	 */
	Genotype makeCrossover(Genotype g1, Genotype g2) {
			// Extract the gene data
			GeneData d1 = g1.geneData;
			GeneData d2 = g2.geneData;
			  
			// Select new number of colors and points, between those of the parents
			int new_ncols = PApplet.floor(parent.random(d1.ncolors, d2.ncolors+1));
			int new_npoints = PApplet.floor(parent.random(d1.npoints, d2.npoints+1));
			  
			int[] newcolors = new int[new_ncols]; 
			float[] newthetas = new float[new_npoints];
			float[] newrs = new float[new_npoints];

			  
			for (int i = 0; i < new_ncols; i++) {   // Select the new colors, picking from the father or from the mother
				
				if (i >= d1.ncolors)			    // No father colors left, pick from the mother 
					newcolors[i] = d2.colors[i];
				else if ( i >= d2.ncolors)			// No mother colors left, pick from the father
					newcolors[i] = d1.colors[i];
				else {								// We have father and mother colors, pick one of the two
					float g = parent.random(2);
					if (g < 1)
						newcolors[i] = d1.colors[i];
					else
						newcolors[i] = d2.colors[i];
				}
			}
			  
			/**
			 * Now select new coordinates, picking from father or mother (or whichever's left if one ends earlier than the other. 
			 * NOTE: radii and angles are selected independently, so the child might have a point with the angle of the fatherìs point 
			 * and the radius of the mother's point
			 */
			for (int i = 0; i < new_npoints; i++) {	
				if (i >= d1.npoints){
					newthetas[i] = d2.thetas[i];
					newrs[i] = d2.rs[i];
				  }
				  else if (i >= d2.npoints) {
					  newthetas[i] = d1.thetas[i];
					  newrs[i] = d1.rs[i];
				  }
				  else {
					  float g = parent.random(2);
					  if (g < 1)
						  newthetas[i] = d1.thetas[i];
					  else
						  newthetas[i] = d2.thetas[i];
					  g = parent.random(2);
					  if (g < 1)
						  newrs[i] = d1.rs[i];
					  else
						  newrs[i] = d2.rs[i];
				  }
			}
			
			// Store the new gene data in a new genotype
			GeneData newGeneData = new GeneData(new_ncols, new_npoints, newthetas, newrs, newcolors);
			int newLifetime = 0;
			Genotype child = new Genotype(parent, newLifetime, newGeneData);
			
			mutateGenotype(child); // And add some mutation to taste
			
			return child;
		  }
}