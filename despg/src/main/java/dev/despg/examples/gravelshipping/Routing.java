/**
 * Copyright (C) 2021 despg.dev, Ralf Buschermöhle
 *
 * DESPG is made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * see LICENSE
 * 
 */
package dev.despg.examples.gravelshipping;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.LMProfile;
import com.graphhopper.config.Profile;
import com.graphhopper.routing.ev.MaxHeight;
import com.graphhopper.routing.weighting.custom.CustomProfile;
import com.graphhopper.util.*;
import com.graphhopper.util.shapes.GHPoint;

import java.util.Arrays;
import java.util.Locale;

import static com.graphhopper.json.Statement.If;
import static com.graphhopper.json.Statement.Op.LIMIT;
import static com.graphhopper.json.Statement.Op.MULTIPLY;


public class Routing {
	
	//private static GraphHopper hopper = new GraphHopper();
	
	/**
	 * This Method calculates the Time it takes to drive from one {@link Location} to another one
	 * using the Parameters Latitude and Longitude and returns it as a Long that represents the
	 * Time utilized in Minutes.
	 * 
	 * @param fromLat - From Latitude
	 * @param fromLon - From Longitude
	 * @param toLat - To Latitude
	 * @param toLo - To Longitude
	 * @return Long - timeInMinutes
	 */
	
	//private static long timeInMs = 0;
	
	public static long customizableRouting(double fromLat, double fromLon, double toLat, double toLo) {
        GraphHopper hopper = new GraphHopper();
        //hopper.setOSMFile("" + "C:/Users/Chris/Desktop/JavaTest/germany-latest.osm.pbf");
        hopper.setOSMFile("" + "H:/OSM/germany-latest.osm"); 
        //hopper.setGraphHopperLocation("target/routing-custom-graph-cache");
        hopper.setGraphHopperLocation("H:\\OSM\\neuer_cache");
        hopper.setProfiles(new CustomProfile("car_custom").setCustomModel(new CustomModel()).setVehicle("car"));

        hopper.getLMPreparationHandler().setLMProfiles(new LMProfile("car_custom"));
        hopper.importOrLoad();

        GHRequest req = new GHRequest().setProfile("car_custom").
                addPoint(new GHPoint(fromLat, fromLon)).addPoint(new GHPoint(toLat, toLo));

        GHResponse res = hopper.route(req);
        if (res.hasErrors())
            throw new RuntimeException(res.getErrors().toString());

        assert Math.round(res.getBest().getTime() / 1000d) == 96;

        // 2. now avoid primary roads and reduce maximum speed, see docs/core/custom-models.md for an in-depth explanation
        // and also the blog posts https://www.graphhopper.com/?s=customizable+routing
        
        CustomModel model = new CustomModel();
        //model.addToPriority(If("road_class == PRIMARY", MULTIPLY, "0.5"));
        model.addToPriority(If("road_class != CYCLEWAY", MULTIPLY, "0.8"));

        
        model.addToPriority(If("true", LIMIT, "80"));
        
        res = hopper.route(req);
        if (res.hasErrors())
            throw new RuntimeException(res.getErrors().toString());

        assert Math.round(res.getBest().getTime() / 1000d) == 165;
        
        ResponsePath path = res.getBest();
        
        PointList pointList = path.getPoints();
        double distance = path.getDistance();
        //long timeInMs = path.getTime();
        long timeInMs = path.getTime();
        
        System.out.println(distance + " " + (((timeInMs / 1000) / 60)) + " Time in Minutes");
        Translation tr = hopper.getTranslationMap().getWithFallBack(Locale.ENGLISH);
        
        return ((timeInMs / 1000) / 60); 
        
      /*  logger.log(Level.INFO, distance + " " + (((timeInMs / 1000) / 60)) + " Time in Minutes");
        
        logger.log(Level.INFO, "distance " + Math.round(instruction.getDistance()) + 
       		 " for instruction: " + instruction.getName() + " " + instruction.getTurnDescription(tr) + " Time: " + (instruction.getTime() / 1000) + " seconds"); 
        */
//        InstructionList il = path.getInstructions();
//        // iterate over all turn instructions
//        for (Instruction instruction : il) {
//             System.out.println("distance " + Math.round(instruction.getDistance()) + 
//            		 " for instruction: " + instruction.getName() + " " + instruction.getTurnDescription(tr) + " Time: " + (instruction.getTime() / 1000) + " seconds");
//        }
    }
	
//	public static long getTimeInMs(double fromLat, double fromLon, double toLat, double toLo) {
//		customizableRouting(fromLat, fromLon, toLat, toLo);
//		return ((timeInMs / 1000) / 60);
//	}
//	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	public static Long customizableRouting(double fromLat, double fromLon, double toLat, double toLo) {
//		
//		//############ Bitte den für euch passenden Befehl aktivieren und den rest nur auskommentieren und NICHT löschen. 
//		//hopper.setOSMFile("" + "C:/Users/andre/Documents/GitHub/Modsim/despg/src/despgutils/germany-latest.osm.pbf");
//		hopper.setOSMFile("H:/OSM/germany-latest.osm");
//		hopper.setGraphHopperLocation("H:\\OSM\\neuer_cache");
//		//hopper.setGraphHopperLocation("/Users/rene/Desktop/Studium/HS-Osnabrueck/Eclipse/OSM-files.nosync/routing-custom-graph-cache");
//		// Chris
//		//hopper.setOSMFile("C:/Users/chris/Desktop/javatest/germany-latest.osm.pbf");
//		//hopper.setGraphHopperLocation("C:/Users/chris/Desktop/javatest/cache");
//		
//		// Set and Load the Profile
//		hopper.setProfiles(new CustomProfile("car_custom").setCustomModel(new CustomModel()).setVehicle("car"));
//		hopper.getLMPreparationHandler().setLMProfiles(new LMProfile("car_custom"));
//		hopper.importOrLoad();
//
//		// Creates a Request containing the Latitude and Longitude from Start to Finish
//		GHRequest req = new GHRequest().setProfile("car_custom").
//				addPoint(new GHPoint(fromLat, fromLon)).addPoint(new GHPoint(toLat, toLo));
//
//		// Calculate the Route
//		/*GHResponse res = hopper.route(req);
//		if (res.hasErrors())
//			throw new RuntimeException(res.getErrors().toString()); */
//
//		//assert Math.round(res.getBest().getTime() / 1000d) == 96;
//
//		// Create a new Custom Model simulating a Truck
//		CustomModel model = new CustomModel();
//
//		// LIMIT MAXIMUM SPEED TO 80 KM/H
//		model.addToPriority(If("true", LIMIT, "80"));
//
//		// Sets the Custom Model to be used as the Simulation Model
//		req.setCustomModel(model);
//		GHResponse res = hopper.route(req);
//		if (res.hasErrors())
//			throw new RuntimeException(res.getErrors().toString());
//
//		assert Math.round(res.getBest().getTime() / 1000d) == 165;
//
//		// Calculate the best Path based on the Model
//		ResponsePath path = res.getBest();
//
//		// Calculate the time neccessary to  
//		long timeInMs = path.getTime();
//
//		hopper.close();
//		return ((timeInMs / 1000) / 60); // Conversion from Ms to Minutes.
//	}
}
