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
import dev.despg.core.Time;

public class GravelShipping extends Simulation
{
	private static Logger logger = Logger.getLogger("GravelShipping");

	private static Integer gravelToShip = 2000;
	private static Integer gravelShipped = 0;
	private final int gravelToShippedFinal = gravelToShip;

	private static Integer successfulLoadings = 0;
	private static Integer successfulLoadingSizes = 0;

	private static Integer unsuccessfulLoadings = 0;
	private static Integer unsuccessfulLoadingSizes = 0;

	private static final int NUM_TRUCKS = 2;
	private static final int NUM_LOADING_DOCKS = 3;
	private static final int NUM_WEIGHING_STATIONS = 2;
	
	//private static ArrayList<Double> LOADING_DOCK_LOCATION = Reader.loadCoordinates(null);
	//private static ArrayList<Double> WEIGHING_LOCATION = Reader.loadCoordinates(null);

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
			new LoadingDock("LD" + i);

		for (int i = 0; i < NUM_WEIGHING_STATIONS; i++)
			new WeighingStation("WS" + i);

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
		
		Routing.customizableRouting(52.677722,7.294407, 52.517879,7.321728);
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

		String shipped = String.format("shipped/toShip : %dt(%.2f%%) / %dt", gravelShipped,
				(double) gravelShipped / gravelToShippedFinal * 100, gravelToShip);

		logger.log(Level.INFO, time + " " + shipped + "\n " + eventQueue
				+ " #Trucks Loading Queue: " + numberOfTrucksLoadingQueue + ", # Trucks Weighing Queue: " + numberOfTrucksWeighingQueue);
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
}
