/**
 * Copyright (C) 2021 despg.dev, Ralf Buschermöhle
 *
 * DESPG is made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * see LICENSE
 * 
 *@author Christian Minich
 */
package dev.despg.examples.gravelshipping;

import java.util.Locale;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointList;
import com.graphhopper.util.Translation;

public class Routing {
	
	private String relDir;
	
	public Routing(String[] args) {
		String relDir = args.length == 1 ? args[0] : "";
	}
	// File Path
	private String ghLoc = "C:/Users/chris/Desktop/despgutils/berlin-latest.osm.pbf";
	
	/**
	 * This method creates an Instance of the GraphHopper Class and returns it.
	 * @param relDir - Addition of Run Configuration Arguments 
	 * @param ghLoc - File Location of the Openstreetmap pbf File
	 * @return {@link GraphHopper}
	 */
	public static GraphHopper createGraphHopperInstance(String relDir, String ghLoc) {
		GraphHopper hopper = new GraphHopper().
        hopper.setOSMFile(ghLoc);
        // specify where to store graphhopper files
        hopper.setGraphHopperLocation("target/routing-graph-cache");

        // see docs/core/profiles.md to learn more about profiles
        hopper.setProfiles(new Profile("car").setVehicle("car").setWeighting("fastest").setTurnCosts(false));

        // this enables speed mode for the profile we called car
        hopper.getCHPreparationHandler().setCHProfiles(new CHProfile("car"));

        // now this can take minutes if it imports or a few seconds for loading of course this is dependent on the area you import
        hopper.importOrLoad();
        return hopper;
	}
	
	/**
	 * This method calculates the approximate Distance and
	 * Time that is needed to travel from the Start to the End.
	 * @param hopper - {@link GraphHopper}
	 * @param fromLat - Start Latitude
	 * @param fromLon - Start Longitude
	 * @param toLat - End Latitude
	 * @param toLon - End Longitude
	 */
	public static void routingCalc(GraphHopper hopper, double fromLat, double fromLon, double toLat, double toLon) {
		//TODO Return Type
		GHRequest req = new GHRequest(fromLat, fromLon, toLat, toLon)
                        .setProfile("car")
                        .setLocale(Locale.US);
        GHResponse rsp = hopper.route(req);

        if (rsp.hasErrors())
            throw new RuntimeException(rsp.getErrors().toString());

        ResponsePath path = rsp.getBest();

        // points, distance in meters and time in millis of the full path
        PointList pointList = path.getPoints();
        double distance = path.getDistance();
        long timeInMs = path.getTime();

        Translation tr = hopper.getTranslationMap().getWithFallBack(Locale.UK);
        InstructionList il = path.getInstructions();
        // iterate over all turn instructions
        for (Instruction instruction : il) {
            System.out.println("distance " + instruction.getDistance() + " for instruction: " + instruction.getTurnDescription(tr));
        }
        
        
        //TODO Custom Profile Implementation
        //TODO SERVER Connection
        //TODO Connection to GravelShipping
	}
}
