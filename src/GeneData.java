/**
 * The information of a genotype consists in a list of colors and points (in polar coordinates).
 * This class is only for storing all this information in a single variable.
 *
 */
public class GeneData {
	int ncolors;
	int npoints;
	float[] thetas;
	float[] rs;
	int[] colors; 
	
	GeneData(int _ncolors, int _npoints, float[] _thetas, float[] _rs, int[] _colors) {
		ncolors = _ncolors;
		npoints = _npoints; 
		thetas = _thetas; 
		rs = _rs; 
		colors = _colors;
	}
}
