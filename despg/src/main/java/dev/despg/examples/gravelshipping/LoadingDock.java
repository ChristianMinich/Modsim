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

import dev.despg.core.Event;
import dev.despg.core.EventQueue;
import dev.despg.core.Randomizer;
import dev.despg.core.SimulationObject;
import dev.despg.core.SimulationObjects;

public class LoadingDock extends SimulationObject
{
	private String name;
	private Truck truckCurrentlyLoaded;
	private Boolean dockFailed = false;
	private static Long drivingToWeighingStation;

	private static EventQueue eventQueue;

	private static Randomizer loadingWeight;
	private static Randomizer loadingTime;
	private static Randomizer dockFailureRepairTime;
	
	/**
	 * Constructor for new LoadingDocks, injects its dependency to SimulationObjects
	 * and creates the required randomizer instances.
	 *
	 * @param name Name of the LoadingDock instance
	 */
	public LoadingDock(String name, Double latitude, Double longitude)
	{
		this.name = name;
	    this.latitude = latitude;
		this.longitude = longitude;

		eventQueue = EventQueue.getInstance();
		SimulationObjects.getInstance().add(this);

		loadingWeight = new Randomizer();
		loadingWeight.addProbInt(0.3, 34);
		loadingWeight.addProbInt(0.6, 38);
		loadingWeight.addProbInt(1.0, 41);

		loadingTime = new Randomizer();
		loadingTime.addProbInt(0.3, 60);
		loadingTime.addProbInt(0.8, 120);
		loadingTime.addProbInt(1.0, 180);
		
		dockFailureRepairTime = new Randomizer();
		dockFailureRepairTime.addProbInt(0.8, 0);
		dockFailureRepairTime.addProbInt(0.98, 1440);
		dockFailureRepairTime.addProbInt(1.0, 2880);
	}

	@Override
	public final String toString()
	{
		String toString = "Loading Dock:" + name;
		if (truckCurrentlyLoaded != null)
			toString += " " + "loading: " + truckCurrentlyLoaded;
		return toString;
	}

	/**
	 * Gets called every timeStep.
	 *
	 * If it is not currently occupied ({@link #truckCurrentlyLoaded} == null) and
	 * the simulation goal still is not archived, it checks if there is an event in
	 * the queue that got assigned to this class with the correct event description.
	 * Then proceeds in checking if the attached object is indeed a Truck. In that
	 * case the event gets removed from the queue and the event will get handled:
	 * {@link #truckCurrentlyLoaded} is set to the events attached object, and the
	 * truck gets loaded. Adds a new event to the event queue for when the loading
	 * is done and returns true.
	 *
	 * When the loading is done, it grabs the corresponding event from the event
	 * queue and handles it by removing it from the queue, setting
	 * {@link truckCurrentlyLoaded} to null and adding a new event to the event
	 * queue assigned to the {@link WeighingStation} class.
	 *
	 * @return true if an assignable event got found and handled, false if no event
	 *         could get assigned
	 */
	@Override
	public boolean simulate(long timeStep)
	{
		if (dockFailed){
			Event event = eventQueue.getNextEvent(timeStep, true, GravelLoadingEventTypes.DockRepaired, null, this);
			if (event != null){
				eventQueue.remove(event);
				dockFailed = false;
				return true;
			}
		}
		else if (truckCurrentlyLoaded == null && GravelShipping.getGravelToShip() > 0)
		{
			Event event = eventQueue.getNextEvent(timeStep, true, GravelLoadingEventTypes.Loading, this.getClass(), null);
			if (event != null && event.getObjectAttached() != null
					&& event.getObjectAttached().getClass() == Truck.class)
			{
				eventQueue.remove(event);

				truckCurrentlyLoaded = (Truck) event.getObjectAttached();
				// verlade das Gravel was du noch hast
				truckCurrentlyLoaded.load(Math.min(loadingWeight.nextInt(), GravelShipping.getGravelToShip()));
				GravelShipping.setGravelToShip(GravelShipping.getGravelToShip() - truckCurrentlyLoaded.getLoad());

				eventQueue.add(new Event(timeStep + truckCurrentlyLoaded.addUtilization(loadingTime.nextInt()),
						GravelLoadingEventTypes.LoadingDone, truckCurrentlyLoaded, null, this));

				utilStart(timeStep);
				return true;
			}
		}
		else
		{
			Event event = eventQueue.getNextEvent(timeStep, true, GravelLoadingEventTypes.LoadingDone, null, this);
			if (event != null && event.getObjectAttached() != null
					&& event.getObjectAttached().getClass() == Truck.class)
			{
				eventQueue.remove(event);

				SimulationObject nextEvent = eventQueue.getNextWeighingStation(timeStep);
				//Test ob WeighingStation korrekt ausgelesen werden kann
				
				WeighingStation ws = (WeighingStation) nextEvent;
				
				if(ws == null) {
					ws = (WeighingStation) GravelShipping.getFirstWeighingStation();
				}
				
				drivingToWeighingStation = Routing.customizableRouting(this.latitude, this.longitude, ws.getLatitude(), ws.getLongitude());
				
				eventQueue.add(new Event(
						timeStep + event.getObjectAttached().addUtilization(drivingToWeighingStation),
						GravelLoadingEventTypes.Weighing, truckCurrentlyLoaded, WeighingStation.class, null));

				truckCurrentlyLoaded = null;
				utilStop(timeStep);
				
				int repairTime = dockFailureRepairTime.nextInt(); 
				if(repairTime > 0) 
				{
					dockFailed = true;
					eventQueue.add(new Event(timeStep + repairTime, GravelLoadingEventTypes.DockRepaired,
							null, null, this));
				} 
				
				return true;
			}
		}

		return false;
	}
}
