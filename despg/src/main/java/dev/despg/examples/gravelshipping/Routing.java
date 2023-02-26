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
import com.graphhopper.config.LMProfile;
import com.graphhopper.routing.weighting.custom.CustomProfile;
import com.graphhopper.util.*;
import com.graphhopper.util.shapes.GHPoint;

import java.util.Locale;

import static com.graphhopper.json.Statement.If;
import static com.graphhopper.json.Statement.Op.LIMIT;
import static com.graphhopper.json.Statement.Op.MULTIPLY;


public final class Routing
{

	private Routing()
	{

	}

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

	public static long customizableRouting(double fromLat, double fromLon, double toLat, double toLo)
	{
        GraphHopper hopper = new GraphHopper();
        //hopper.setOSMFile("" + "C:/Users/Chris/Desktop/JavaTest/germany-latest.osm.pbf");
        //hopper.setOSMFile("" + "H:/OSM/germany-latest.osm");
        hopper.setOSMFile("" + "/Users/rene/Desktop/Studium/HS-Osnabrueck/Eclipse/OSM-files.nosync/germany-latest.osm.pbf");
        //hopper.setGraphHopperLocation("target/routing-custom-graph-cache");
        //hopper.setGraphHopperLocation("H:\\OSM\\neuer_cache");
        hopper.setGraphHopperLocation("/Users/rene/Desktop/Studium/HS-Osnabrueck/Eclipse/OSM-files.nosync/routing-custom-graph-cache");
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
        model.addToPriority(If("road_class != CYCLEWAY", MULTIPLY, "0.8"));


        model.addToPriority(If("true", LIMIT, "80"));

        res = hopper.route(req);
        if (res.hasErrors())
            throw new RuntimeException(res.getErrors().toString());

        assert Math.round(res.getBest().getTime() / 1000d) == 165;

        ResponsePath path = res.getBest();

        double distance = path.getDistance();
        long timeInMs = path.getTime();

        System.out.println(distance + " " + (((timeInMs / 1000) / 60)) + " Time in Minutes");
        Translation tr = hopper.getTranslationMap().getWithFallBack(Locale.ENGLISH);

        return ((timeInMs / 1000) / 60);

    }
}
