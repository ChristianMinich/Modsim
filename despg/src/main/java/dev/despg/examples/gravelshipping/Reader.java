package dev.despg.examples.gravelshipping;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Reader {
    public static ArrayList<Location> loadCoordinates(String path) {
        ArrayList<Location> locations = new ArrayList<Location>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
            	String[] token = line.split(",");
                //System.out.println(token[0] + " | "+ token[1]+ " | "+ token[2]);
                locations.add(new Location(token[0], Double.parseDouble(token[1]),Double.parseDouble(token[2])));
            }
            br.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
       return locations;
    }
}