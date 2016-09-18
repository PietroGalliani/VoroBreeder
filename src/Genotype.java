import processing.core.*;
import org.json.simple.*;

/**
 * Keeps track of genotypes - including the time they were alive.
 * Also contains functions for loading a genotype from a JSON string (such as the one it is represented on on a server)
 * or storing it in one
 *
 */
public class Genotype implements Comparable<Genotype>{

	MainApplet parent;
	GeneData geneData;
	
	int lifetime = 0;
	
	/**
	 * Load a genotype from a gene data object (as in the case of newly generated children)
	 */
	Genotype(MainApplet _parent, int _lifetime, GeneData _geneData){
		parent = _parent; 
		lifetime = _lifetime;
		geneData = _geneData;
	}
	
	/**
	 * Create a random genotype from no information at all
	 */
	Genotype(MainApplet _parent) {
		parent = _parent;
		
		// Choose number of colors and points
		int ncolors = PApplet.round(parent.random(1)*MyConstants.MAXCOLORS + 1);
		int npoints = PApplet.round(parent.random(1) *MyConstants.MAXPOINTS + 1)
				;
		int[] colors = new int[ncolors];
		float[] thetas = new float[npoints];
		float[] rs = new float[npoints];
		  
		// Create random colors
		for (int i = 0; i < ncolors; i++)
			colors[i] = parent.color(parent.random(255), parent.random(255), parent.random(255));
		  
		// Create random points (in polar coordinates)
		for (int i = 0; i < npoints; i++) {
			rs[i] =parent.random(MyConstants.DIAMETER);
		  	thetas[i] = parent.random(2*PApplet.PI);
		}
		geneData = new GeneData(ncolors, npoints, thetas, rs, colors);
		
	}
	
	/**
	 * Load a genotype from a JSON string (this happen when reading from server)
	 */
	Genotype(MainApplet _parent, JSONObject myData) {
		parent = _parent;
		int ncolors = (int) (long) myData.get("ncolors");
		int npoints = (int) (long) myData.get("npoints");
		lifetime = (int) (long) myData.get("lifetime");
		
		int[] colors = new int[ncolors];
		float[] thetas = new float[npoints];
		float[] rs = new float[npoints];
		
		JSONArray Jcolors = (JSONArray) myData.get("colors");
		JSONArray Jthetas = (JSONArray) myData.get("thetas");
		JSONArray Jrs = (JSONArray) myData.get("rs");
		  
		for (int i = 0; i < ncolors; i++) {
			JSONObject Jcol = (JSONObject) Jcolors.get(i);
			float red = (float) (double) Jcol.get("red");
			float green = (float) (double) Jcol.get("green");
			float blue = (float) (double) Jcol.get("blue");
			
			colors[i] = parent.color(red, green, blue);
		}
		  
		for (int i = 0; i < npoints; i++) {
			rs[i] = (float) (double) Jrs.get(i);
		  	thetas[i] = (float) (double) Jthetas.get(i);
		}
		geneData = new GeneData(ncolors, npoints, thetas, rs, colors);
	}
	
	JSONObject toJSON(){
		JSONObject J_geno = new JSONObject();
		
		J_geno.put("lifetime", lifetime); 
		JSONArray J_thetas = new JSONArray(); 
		JSONArray J_rs = new JSONArray();
		JSONArray J_colors = new JSONArray(); 
		
		
		J_geno.put("npoints", geneData.npoints); 		
		J_geno.put("ncolors", geneData.ncolors);
		
		for (int i = 0; i < geneData.npoints; i++) {
			J_thetas.add(geneData.thetas[i]);
			J_rs.add(geneData.rs[i]);
		}
		
		for (int i = 0; i < geneData.ncolors; i++) {
			float red = parent.red(geneData.colors[i]);
			float green = parent.green(geneData.colors[i]);
			float blue = parent.blue(geneData.colors[i]);
			JSONObject J_col = new JSONObject();
			J_col.put("red", red);
			J_col.put("green", green);
			J_col.put("blue", blue);
			J_colors.add(J_col);
		}
	
		J_geno.put("thetas", J_thetas);
		J_geno.put("rs", J_rs);
		J_geno.put("colors", J_colors);
		J_geno.put("lifetime", lifetime);
		
		return J_geno;
	}

	/**
	 * Which of the two genotype lived more? 
	 * Necessary for ordering the seekers and avoiders by succcess in BreedingManager
	 */
	@Override
	public int compareTo(Genotype other) {
		return this.lifetime - other.lifetime;
	}
}
