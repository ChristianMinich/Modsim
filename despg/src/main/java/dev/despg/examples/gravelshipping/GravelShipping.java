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

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import dev.despg.core.Event;
import dev.despg.core.EventQueue;
import dev.despg.core.Simulation;
import dev.despg.core.SimulationObject;
import dev.despg.core.Time;

public class GravelShipping extends Simulation
{
	private static Logger logger = Logger.getLogger("GravelShipping");
	private static String pathLoadingdocks = ("src/despgutils/LoadingDocks.csv");
	private static String pathWeighingstation = ("src/despgutils/WeighingStations.csv");
	private static String pathDestinations = ("src/despgutils/Destinations.csv");

	private static Integer gravelToShip = 2000;
	private static Integer gravelShipped = 0;
	private final int gravelToShippedFinal = gravelToShip;;

	private static Integer successfulLoadings = 0;
	private static Integer successfulLoadingSizes = 0;

	private static Integer unsuccessfulLoadings = 0;
	private static Integer unsuccessfulLoadingSizes = 0;
	
	private static Integer successfulUnloadings = 0;
    private static Integer successfulUnloadingSizes = 0;

    private static Integer unsuccessfulUnloadings = 0;
    private static Integer unsuccessfulUnloadingSizes = 0;

	private static final int NUM_TRUCKS = 2;
	private static final int NUM_LOADING_DOCKS = 3;
	private static final int NUM_WEIGHING_STATIONS = 2;
	private static final int NUM_SHIPMENTS = 10;
	
	private static ArrayList<Location> LOADING_DOCK_LOCATION = Reader.loadCoordinates(pathLoadingdocks);
	private static ArrayList<Location> WEIGHING_LOCATION = Reader.loadCoordinates(pathWeighingstation);
	private static ArrayList<Location> DESTINATION_LOCATION = Reader.loadCoordinates(pathDestinations);

	private static ArrayList<SimulationObject> FirstWeighingStation;
	/**
	 * Defines the setup of simulation objects and starting events before executing
	 * the simulation. Prints utilization statistics afterwards
	 *
	 * @param args not used
	 */
	public static void main(String[] args)
	{
		EventQueue eventqueue = EventQueue.getInstance();
		
		System.out.println(eventqueue);

		for (int i = 0; i < NUM_TRUCKS; i++)
			eventqueue.add(new Event(0L, GravelLoadingEventTypes.Loading, new Truck("T" + i), LoadingDock.class, null));

		for (int i = 0; i < NUM_LOADING_DOCKS; i++)
			new LoadingDock("LD " + LOADING_DOCK_LOCATION.get(i).getName(), LOADING_DOCK_LOCATION.get(i).getLatitude(), LOADING_DOCK_LOCATION.get(i).getLongitude());

		for (int i = 0; i < NUM_WEIGHING_STATIONS; i++) {
			new WeighingStation("WS " + WEIGHING_LOCATION.get(i).getName(), WEIGHING_LOCATION.get(i).getLatitude(), WEIGHING_LOCATION.get(i).getLongitude());
			while(i < 1) {
				FirstWeighingStation.add(new WeighingStation("WS " + WEIGHING_LOCATION.get(i).getName(), WEIGHING_LOCATION.get(i).getLatitude(), WEIGHING_LOCATION.get(i).getLongitude()));
			}
		}
		
		for (int i = 0; i < NUM_SHIPMENTS; i++)
			new Shipment("SP " + DESTINATION_LOCATION.get(i).getName(), DESTINATION_LOCATION.get(i).getLatitude(), DESTINATION_LOCATION.get(i).getLongitude());

		GravelShipping gs = new GravelShipping();
		long timeStep = gs.simulate();

		// output some statistics after simulation run
		logger.log(Level.INFO, "Gravel shipped\t\t = " + gravelShipped + " tons");
		logger.log(Level.INFO, "Mean Time / Gravel Unit\t = " + ((double) timeStep / gravelShipped) + " minutes");

		logger.log(Level.INFO,
				String.format("Successfull loadings\t = %d(%.2f%%), mean size %.2ft", successfulLoadings,
						(double) successfulLoadings / (successfulLoadings + unsuccessfulLoadings) * 100,
						(double) successfulLoadingSizes / successfulLoadings));

		logger.log(Level.INFO,
				String.format("Unsuccessfull loadings\t = %d(%.2f%%), mean size %.2ft", unsuccessfulLoadings,
						(double) unsuccessfulLoadings / (successfulLoadings + unsuccessfulLoadings) * 100,
						(double) unsuccessfulLoadingSizes / unsuccessfulLoadings));
		
		logger.log(Level.INFO,
				String.format("Successfull Unloadings\t = %d(%.2f%%), mean size %.2ft", successfulUnloadings,
						(double) successfulUnloadings / (successfulUnloadings + unsuccessfulUnloadings) * 100,
						(double) successfulUnloadingSizes / successfulUnloadings));
		
		logger.log(Level.INFO,
				String.format("Unsuccessfull Unloadings\t = %d(%.2f%%), mean size %.2ft", unsuccessfulUnloadings,
						(double) unsuccessfulUnloadings / (successfulUnloadings + unsuccessfulUnloadings) * 100,
						(double) unsuccessfulUnloadingSizes / unsuccessfulUnloadings));
		
	}

	/**
	 * Prints information after every timeStep in which an event got triggered.
	 */
	@Override
	protected void printEveryStep(long numberOfSteps, long timeStep)
	{
		String time = numberOfSteps + ". " + Time.stepsToString(timeStep);
		String eventQueue = "EventQueue: " + EventQueue.getInstance().toString();

		int numberOfTrucksLoadingQueue = EventQueue.getInstance().countEvents(timeStep, true, GravelLoadingEventTypes.Loading, null, null);
		int numberOfTrucksWeighingQueue = EventQueue.getInstance().countEvents(timeStep, true, GravelLoadingEventTypes.Weighing, null, null);
		int numberOfTrucksUnloadingQueue = EventQueue.getInstance().countEvents(timeStep, true, GravelLoadingEventTypes.Unloading, null, null);
		String shipped = String.format("shipped/toShip : %dt(%.2f%%) / %dt", gravelShipped,
				(double) gravelShipped / gravelToShippedFinal * 100, gravelToShip);

		logger.log(Level.INFO, time + " " + shipped + "\n " + eventQueue
				+ " #Trucks Loading Queue: " + numberOfTrucksLoadingQueue + ", # Trucks Weighing Queue: " + numberOfTrucksWeighingQueue + " ," + numberOfTrucksUnloadingQueue);
	}
	
	public static SimulationObject getFirstWeighingStation()
	{
		return FirstWeighingStation.get(0);
	}

	public static Integer getGravelToShip()
	{
		return gravelToShip;
	}

	public static void setGravelToShip(Integer gravelToShip)
	{
		GravelShipping.gravelToShip = gravelToShip;
	}

	public static Integer getGravelShipped()
	{
		return gravelShipped;
	}

	public static void increaseGravelShipped(Integer gravelShipped)
	{
		GravelShipping.gravelShipped += gravelShipped;
	}

	public static void increaseSuccessfulLoadings()
	{
		successfulLoadings++;
	}

	public static void increaseSuccessfulLoadingSizes(Integer successfulLoadingSizes)
	{
		GravelShipping.successfulLoadingSizes += successfulLoadingSizes;
	}

	public static void increaseUnsuccessfulLoadings()
	{
		GravelShipping.unsuccessfulLoadings++;
	}

	public static void increaseUnsuccessfulLoadingSizes(Integer unsuccessfulLoadingSizes)
	{
		GravelShipping.unsuccessfulLoadingSizes += unsuccessfulLoadingSizes;
	}
	
	public static void increaseSuccessfulUnloadings()
	{
		successfulUnloadings++;
	}
	
	public static void increaseUnsuccessfulUnloadings()
	{
		GravelShipping.unsuccessfulUnloadings++;
	}
	
	public static void increaseSuccessfulUnloadingSizes(Integer successfulUnloadingSizes)
	{
		GravelShipping.successfulUnloadingSizes += successfulUnloadingSizes;
	}
	
	public static void increaseUnsuccessfulUnloadingSizes(Integer unsuccessfulUnloadingSizes)
	{
		GravelShipping.unsuccessfulUnloadingSizes += unsuccessfulUnloadingSizes;
	}
}
