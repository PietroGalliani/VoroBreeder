import processing.core.*;

import java.util.ArrayList;
import java.util.Collections;
import org.json.simple.*;
import org.json.simple.parser.*;

/**
 * 
 * Loads/creates the two populations, reproduces the genotypes, and sends them back to the server when we're done
 *
 */
public class BreedingManager {
	MainApplet parent;
	
	CrossoverMaker myCrossoverMaker;
	
	
	String base;
	
	ArrayList<Genotype> seekers; 
	ArrayList<Genotype> avoiders; 
	
	
	BreedingManager(MainApplet _parent){
		parent = _parent;
		
		if (MyConstants.APPLET_MODE)
			base = parent.getCodeBase().toString();
		else
			base = MyConstants.NONAPPLET_BASE;
		
		myCrossoverMaker = new CrossoverMaker(parent);
		
		seekers = new ArrayList<Genotype>();
		avoiders = new ArrayList<Genotype>();
		
	}
	
	/**
	 * Create random genotypes for seekers and avoiders
	 */
	void loadRandom() {
		for (int i = 0; i < MyConstants.NUMSEEKERS; i++) 
			seekers.add(new Genotype(parent));
		for (int i = 0; i < MyConstants.NUMAVOIDERS; i++) 
			avoiders.add(new Genotype(parent));
	}
	
	/**
	 * Load seeker and avoider genotypes from the server (or from local files, when in local mode)
	 */
	void loadGenotypes() {
		String[] JSgenotypes = null;
		if (! parent.localMode)
			JSgenotypes = RemoteManager.readFromServer(base);
		else {
			JSgenotypes = RemoteManager.readFromFile();
		}
		JSONParser parser = new JSONParser();
		
		try {
			float mean_seekers = 0;
			float mean_avoiders =0;
			
			JSONArray Jseekers = (JSONArray) parser.parse(JSgenotypes[0]);
			JSONArray Javoiders = (JSONArray) parser.parse(JSgenotypes[1]);
			for (int i = 0; i < Jseekers.size(); i++) {
				Genotype newSeeker = new Genotype(parent, (JSONObject)Jseekers.get(i));
				seekers.add(newSeeker);
				mean_seekers +=newSeeker.lifetime;
			}
			mean_seekers = mean_seekers/Jseekers.size();
			for (int i = 0; i < Javoiders.size(); i++) {
				Genotype newAvoider = new Genotype(parent, (JSONObject)Javoiders.get(i));
				avoiders.add(newAvoider); 
				mean_avoiders += newAvoider.lifetime;
			}
			mean_avoiders = mean_avoiders/Javoiders.size();

			System.out.format("Seekers loaded: %d, mean lifetime: %f\n", Jseekers.size(), mean_seekers);
			System.out.format("Avoiders loaded: %d, mean lifetime: %f\n", Javoiders.size(), mean_avoiders);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send the seekers and avoiders back to the server - or to the file
	 */
	void saveData() {
		order_genotypes();
		JSONArray Jseekers = new JSONArray();
		JSONArray Javoiders = new JSONArray();
		
		for (int i = 0; i < MyConstants.NUMSEEKERS; i++) {
			Genotype g = seekers.get(i);
			Jseekers.add(g.toJSON());
		}
		
		for (int i = 0; i < MyConstants.NUMAVOIDERS; i++) {
			Genotype g = avoiders.get(i);
			Javoiders.add(g.toJSON());
		}
		/*System.out.println(Jseekers.toJSONString());
		System.out.println();
		System.out.println(Javoiders.toJSONString());*/
		if (!parent.localMode)
			RemoteManager.sendToServer(base, Jseekers.toJSONString(), Javoiders.toJSONString());
		else
			RemoteManager.writeToFile(Jseekers.toJSONString(), Javoiders.toJSONString());
	}
	
	/**
	 * Reproduces the genotypes, updates the lists
	 */
	public void reproduce(){
		ArrayList<Genotype>seekerChildren = myCrossoverMaker.crossoverSeekers(seekers, MyConstants.NUMSEEKERCHILDREN);
		for (int i = 0; i < MyConstants.NUMSEEKERCHILDREN;i++) {
			seekers.set(MyConstants.NUMSEEKERS-i-1, seekerChildren.get(i));
		}
		seekers = new ArrayList<Genotype>(seekers.subList(0, MyConstants.NUMSEEKERS)); // now that we reproduced, let us keep only the best and the children
		
		ArrayList<Genotype>avoiderChildren = myCrossoverMaker.crossoverAvoiders(avoiders, MyConstants.NUMAVOIDERCHILDREN);
		for (int i = 0; i < MyConstants.NUMAVOIDERCHILDREN; i++) {
			avoiders.set(MyConstants.NUMAVOIDERS - i-1, avoiderChildren.get(i));
		}
		avoiders = new ArrayList<Genotype>(avoiders.subList(0, MyConstants.NUMAVOIDERS)); // now that we reproduced, let us keep only the best and the children
	}
	
	/**
	 * Order seeker genotypes in decreasing survival length, the avoiders in increasing survival length
	 */
	public void order_genotypes(){
		Collections.sort(seekers, Collections.reverseOrder());
		Collections.sort(avoiders);
	}
	
	/**
	 * Returns all genotypes, with no distinction between seekers and avoiders, in a random order
	 */
	ArrayList<Genotype> allGenotypes(){
		ArrayList<Genotype> allGT = new ArrayList<Genotype>();
		allGT.addAll(seekers);
		allGT.addAll(avoiders);
		Collections.shuffle(allGT);
		return allGT;
	}

	/**
	 * Remove the information about previous survival lengths
	 */
	public void forgetHistories() {
		for (int i = 0; i < seekers.size(); i++) 
			seekers.get(i).lifetime = 0;
		
		for (int i = 0; i < avoiders.size(); i++) 
			avoiders.get(i).lifetime = 0;
	}
	
}