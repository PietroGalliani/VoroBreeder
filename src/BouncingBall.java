import processing.core.*;
import megamu.mesh.*;

/***
 * Creates ball images from genotypes, moves them around on screen if prompted, displays them
 *
 */
public class BouncingBall {
	MainApplet parent; // The main applet
	PVector position;  // Ball position
	PVector velocity;  // Ball velocity
	float lifespan;    // Ball lifespan
	int click_count;   // How many times the ball has been clicked (mostly for debug purposes)

	Genotype genotype; // The ball's genotype
	
	float angle = 0;   // The current angle of rotation
	
	
	PImage myImage; 
	
	BouncingBall(MainApplet _parent, Genotype _genotype){
		parent = _parent;
		genotype = _genotype;
		
		// Start from a random on-screen position
		position = new PVector(parent.random(MyConstants.HEADER_HEIGHT, MyConstants.APPLET_WIDTH-MyConstants.HEADER_HEIGHT), 
												parent.random(2*MyConstants.HEADER_HEIGHT, MyConstants.APPLET_HEIGHT - MyConstants.HEADER_HEIGHT));
		
		// and a random angle
		float movement_angle = parent.random(0, 2*PApplet.PI);
		
		// and a random direction
		velocity = new PVector(MyConstants.SPEED * PApplet.cos(movement_angle), MyConstants.SPEED*PApplet.sin(movement_angle));
		buildPhenotype(genotype);	
	}
	
	/**
	 * How much lifes has the ball left?
	 */
	float getLifespan(){
		return lifespan;
	}
	
	
	/**
	 * Create the ball image from the genotype
	 */
	void buildPhenotype(Genotype g){
		
		// We will load the ball image on pg
		PGraphics pg = parent.createGraphics(PApplet.round(MyConstants.DIAMETER*2), PApplet.round(MyConstants.DIAMETER*2)); 
		pg.beginDraw();
		pg.background(0);
		
		// Find the points for the Voronoi diagram, converting from polar to cartesian coordinates
		int num_points = g.geneData.thetas.length;
		float[][] points = new float[num_points][2];
		for (int i = 0; i < num_points; i++) {
			points[i][0] = MyConstants.DIAMETER + g.geneData.rs[i] * PApplet.cos(g.geneData.thetas[i]);
			points[i][1] = MyConstants.DIAMETER + g.geneData.rs[i] * PApplet.sin(g.geneData.thetas[i]);
		}
		
		// Compute Voronoi diagram
		Voronoi myVoronoi = new Voronoi( points );
		MPolygon[] myRegions = myVoronoi.getRegions();
		pg.noStroke();
		
		// Color each region (if less colors than region, loop back)
		int c = 0;
		for(int i=0; i<myRegions.length; i++)
		  {
		    pg.fill(g.geneData.colors[c]);
		    c++; 

		    if (c >= g.geneData.colors.length)
		    	c = 0;
		    
		    myRegions[i].draw(pg);
		  }
		
		// TO SHOW THE POINTS, UNCOMMENT THIS AND BELOW
		/*for (int i = 0; i < num_points; i++){
			pg.fill(0);
			pg.ellipse(points[i][0], points[i][1], 10, 10);
		}*/
		pg.endDraw();
		myImage = pg.get();
		
		// Add transparency mask, so that the ball is clearer on the center, fuzzier on the border, and invisible outside it
		PGraphics mask = parent.createGraphics(PApplet.round(MyConstants.DIAMETER*2), PApplet.round(MyConstants.DIAMETER*2)); 
		mask.beginDraw();
		mask.noFill();
		mask.stroke(255);
		for(int r = 0; r < MyConstants.DIAMETER; r++) {
			mask.stroke(255);
			mask.ellipse(MyConstants.DIAMETER, MyConstants.DIAMETER, r, r);
			
		}
		// TO SHOW THE POINTS, UNCOMMENT THIS AND ABOVE
		/*for (int i = 0; i < num_points; i++){
			mask.fill(255);
			mask.ellipse(points[i][0], points[i][1], 10, 10);
		}*/
		
		mask.endDraw(); 
		myImage.mask(mask);
		
		// Start with initial lifespan
		lifespan = MyConstants.LIFESPAN;
	}
	
	void update() {
		lifespan -= 1.0; 		// decrease ball lifespan
		genotype.lifetime++;	// tell the genotype that it has survived another turn
		
		/**
		 * Update for position, check for boundaries, bounce if needed - note, (x,y) is the *center* of the ball.
		 */
	    position.add(velocity); 
	    angle += MyConstants.ROTATION;
	    if (position.x < MyConstants.DIAMETER/2)
	    {
	    	velocity.x = -velocity.x; 
	    	position.x = MyConstants.DIAMETER/2; 
	    }
	    if (position.x > parent.width - MyConstants.DIAMETER/2) {
	    	velocity.x = -velocity.x;
	    	position.x = parent.width-MyConstants.DIAMETER/2; 
	    }
	    if (position.y < MyConstants.DIAMETER/2 + MyConstants.HEADER_HEIGHT) {
	    	velocity.y = -velocity.y;
	    	position.y = MyConstants.DIAMETER/2 + MyConstants.HEADER_HEIGHT;
	    }
	    
	    if (position.y > parent.height-MyConstants.DIAMETER/2) {
	    	velocity.y = -velocity.y;
	    	position.y = parent.height-MyConstants.DIAMETER/2;
	    }
	}
	
	/**
	 * Display the ball on the screen
	 */
	void display() {
	    parent.pushMatrix();
	    parent.translate(position.x, position.y);
	    
	    parent.rotate(angle);
	    parent.tint(255, lifespan);

	    parent.image(myImage, -MyConstants.DIAMETER, -MyConstants.DIAMETER); // the coordinates are supposed to be the center
	    parent.popMatrix();
	    parent.pushMatrix();
		
		parent.popMatrix();
	  }
	  
	  // Is the ball still alive at all?
	  boolean isDead() {
	    if (lifespan < 0.0) {
	      return true;
	    } else {
	      return false;
	    }
	  }
	
	  /**
	   * Update the ball and display it on screen 
	   */
	  void run() {
		   //System.out.format("x: %f,  y: %f\n", position.x, position.y);
		   update();
		   display();
		 }
	  
	  /**
	   * The user clicked on the screen - might it have clicked on the ball?
	   */
	  void clicked(float x, float y) {
		  float dist = PApplet.sqrt(PApplet.pow(x - position.x, 2) + PApplet.pow(y - position.y, 2)); 
		  if (dist < MyConstants.DIAMETER) {		// Yes!
			  lifespan = MyConstants.LIFESPAN;  // Restore lifespan
			  parent.playPing();				// Play 'ping'
			  click_count++;					// Update click count
		  }		  
	  }
}
