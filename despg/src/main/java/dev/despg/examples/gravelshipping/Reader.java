package dev.despg.examples.gravelshipping;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public final class Reader
{

	private Reader()
	{

	}
	/**
	 * This Method will return an ArrayList containing all the
	 * Parameters specified inside the {@link Location} Class.
	 *
	 * @param path - FilePath
	 * @return ArrayList<Location>
	 */
    public static ArrayList<Location> loadCoordinates(String path)
    {
        ArrayList<Location> locations = new ArrayList<Location>();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null)
            {
            	String[] token = line.split(",");
                locations.add(new Location(token[0], Double.parseDouble(token[1]), Double.parseDouble(token[2])));
            }
            br.close();
        } catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }
       return locations;
    }
}
