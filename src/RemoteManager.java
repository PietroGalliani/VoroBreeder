import java.io.*;
import java.net.*;

/**
 * This is for passing messages to the server and back. Not much to say about this, just standard 
 * input/output stuff
 *
 */
public class RemoteManager {
	/**
	 * Try to read a file from the server
	 */
	static String readFile(String base, String filename){
		String myInput = "";
		String line = "";
		try {
			///String base = "http://users.sussex.ac.uk/~pg221/VoroBreeder/";
			URL url = new URL(base+filename);
			URLConnection connection = url.openConnection();
			InputStream inStream = connection.getInputStream();
			BufferedReader input = new BufferedReader(new InputStreamReader(
					inStream));
			while ((line = input.readLine()) != null){
				myInput += line;
			}
			
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return myInput;
	}
	
	/**
	 * Read the seekers and the avoiders files from the server, return them as strings
	 */
	static String[] readFromServer(String base){
		String[] genotypes = {"", ""}; 
		genotypes[0] = readFile(base, MyConstants.SEEKERS_FILENAME);
		genotypes[1] = readFile(base, MyConstants.AVOIDERS_FILENAME);
		return genotypes;
	}
	
	/**
	 * Send the  lists of seekers and avoiders (represented by JSON strings) to the server
	 */
	static void sendToServer(String base, String str_seekers, String str_avoiders) {
		try {
			String encodedData = URLEncoder.encode("seekers","UTF-8") + "=" + URLEncoder.encode(str_seekers, "UTF-8") +
					"&" + URLEncoder.encode("avoiders", "UTF-8")+"=" + URLEncoder.encode(str_avoiders, "UTF-8");
			URL url = new URL(base + "updateSpecimen.php");
			
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(encodedData);
			writer.flush();
			
			BufferedReader reader = new BufferedReader
					(new InputStreamReader(connection.getInputStream()));			
			StringBuilder sb = new StringBuilder();
			String line = null;
			
			// Read Server Response
			while((line = reader.readLine()) != null)
			{
			sb.append(line);
			break;
			}
			String s = sb.toString();
			System.out.println(s);
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Read the seekers and the avoiders files from local files, return them as strings
	 */
	static String[] readFromFile(){
		String[] genotypes = {"", ""}; 
		BufferedReader seekerReader = null;
		BufferedReader avoiderReader = null;
		 
		try {
 
			String sCurrentLine;
 
			seekerReader = new BufferedReader(new FileReader(MyConstants.SEEKERS_FILENAME));
			avoiderReader = new BufferedReader(new FileReader(MyConstants.AVOIDERS_FILENAME));

			genotypes[0] = seekerReader.readLine();
			genotypes[1] = avoiderReader.readLine();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (seekerReader != null)seekerReader.close();
				if (avoiderReader != null)avoiderReader.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return genotypes;
	}
	
	/**
	 * Writes the  lists of seekers and avoiders (represented by JSON strings) to a local file
	 */
	static void writeToFile(String str_seekers, String str_avoiders) {
		BufferedWriter seekerWriter = null;
		BufferedWriter avoiderWriter = null;
		try
		{
		    seekerWriter = new BufferedWriter( new FileWriter(MyConstants.SEEKERS_FILENAME));
		    avoiderWriter = new BufferedWriter( new FileWriter(MyConstants.AVOIDERS_FILENAME));
		    seekerWriter.write(str_seekers);
		    seekerWriter.flush();
		    avoiderWriter.write(str_avoiders);
		    avoiderWriter.flush();
		}
		catch ( IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
		    try
		    {
		        if (seekerWriter != null)
		        	seekerWriter.close( );
		        if (seekerWriter != null)
		        	seekerWriter.close( );
		    }
		    catch ( IOException e)
		    {
		    }
		}
	}
}
