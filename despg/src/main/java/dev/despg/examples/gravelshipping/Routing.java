/**
 * Copyright (C) 2021 despg.dev, Ralf Buschermöhle
 *
 * DESPG is made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * see LICENSE
 * 
 * @author Christian Minich
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
	public static void customizableRouting(double fromLat, double fromLon, double toLat, double toLo) {
		GraphHopper hopper = new GraphHopper();
		//hopper.setOSMFile("" + "C:/Users/andre/Documents/GitHub/Modsim/despg/src/despgutils/germany-latest.osm.pbf");
		hopper.setOSMFile("src/despgutils/germany-latest.osm.pbf");
		hopper.setGraphHopperLocation("target/routing-custom-graph-cache");
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
		model.addToPriority(If("road_class == PRIMARY", MULTIPLY, "0.5"));

		// unconditional limit to 100km/h
		model.addToPriority(If("true", LIMIT, "100"));

		req.setCustomModel(model);
		res = hopper.route(req);
		if (res.hasErrors())
			throw new RuntimeException(res.getErrors().toString());

		assert Math.round(res.getBest().getTime() / 1000d) == 165;

		ResponsePath path = res.getBest();

		PointList pointList = path.getPoints();
		double distance = path.getDistance();
		long timeInMs = path.getTime();

		System.out.println(distance + " " + (((timeInMs / 1000) / 60)) + " Time in Minutes");
		Translation tr = hopper.getTranslationMap().getWithFallBack(Locale.ENGLISH);

		InstructionList il = path.getInstructions();
		// iterate over all turn instructions
		for (Instruction instruction : il) {
			System.out.println("distance " + Math.round(instruction.getDistance()) + 
					" for instruction: " + instruction.getName() + " " + instruction.getTurnDescription(tr) + " Time: " + (instruction.getTime() / 1000) + " seconds");
		}
		hopper.close();
	}}
