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

import java.util.Map;

import dev.despg.core.Event;
import dev.despg.core.EventQueue;
import dev.despg.core.Randomizer;
import dev.despg.core.SimulationObject;
import dev.despg.core.SimulationObjects;

public class Shipment extends SimulationObject{

	private String name;
	private Truck truckCurrentlyLoaded;
	private static EventQueue eventQueue;
	private static Randomizer unloadingTime;
	private static Long drivingToLoadingDock;
	
	private static ShipmentsToLoadingDocks stld = ShipmentsToLoadingDocks.getInstance();
	
	/**
	 * Constructor for new Shipments, injects its dependency to SimulationObjects
	 * and creates the required randomizer instances.
	 *
	 * @param name
	 * @param latitude
	 * @param longitude
	 */
	public Shipment(String name, Double latitude, Double longitude)
	{
		this.name = name;
	    this.latitude = latitude;
		this.longitude = longitude;

		eventQueue = EventQueue.getInstance();
		SimulationObjects.getInstance().add(this);

		unloadingTime = new Randomizer();
		unloadingTime.addProbInt(0.3, 60);
		unloadingTime.addProbInt(0.8, 120);
		unloadingTime.addProbInt(1.0, 180);
		
	}
	
	
	@Override
	public final String toString()
	{
		String toString = "Shipment:" + name;
		if (truckCurrentlyLoaded != null)
			toString += " " + "shipping with: " + truckCurrentlyLoaded;
		return toString;
	}
	
	/**
	 * Calculates the Time it takes to drive from this Shipment instance 
	 * to the closest {@link LoadingDock} instance.
	 * 
	 * @return long - Time to closest {@link LoadingDock} in Minutes.
	 */
	public long ClosestLoadingDock() {
		long currentSmallestDistance = 0;
		long currentDistance = 0;
		for(Map.Entry<Shipment, LoadingDock> set :
			stld.entrySet())
		{
			System.out.println(set.getKey());
			currentDistance = Routing.customizableRouting(this.latitude, this.longitude, set.getValue().getLatitude(), set.getValue().getLongitude());
			if( currentDistance < currentSmallestDistance) {
				currentSmallestDistance = currentDistance;
				
			}
			
			/*System.out.println(set.getKey() + " = "
                    + set.getValue());*/
		}
		return currentSmallestDistance;
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
	 * truck gets unloaded. Adds a new event to the event queue for when the unloading
	 * is done and returns true.
	 *
	 * When the unloading is done, it grabs the corresponding event from the event
	 * queue and handles it by removing it from the queue, setting
	 * {@link truckCurrentlyLoaded} to null and adding a new event to the event
	 * queue assigned to the {@link LoadingDock} class.
	 *
	 * @return true if an assignable event got found and handled, false if no event
	 *         could get assigned
	 */
	@Override
	public boolean simulate(long timeStep) {
		
		if (truckCurrentlyLoaded == null && GravelShipping.getGravelToShip() > 0)
		{
			Event event = eventQueue.getNextEvent(timeStep, true, GravelLoadingEventTypes.Unloading, this.getClass(), null);
			if (event != null && event.getObjectAttached() != null
					&& event.getObjectAttached().getClass() == Truck.class)
			{
				eventQueue.remove(event);

				truckCurrentlyLoaded = (Truck) event.getObjectAttached();
				truckCurrentlyLoaded.unload();
				
				eventQueue.add(new Event(timeStep + truckCurrentlyLoaded.addUtilization(unloadingTime.nextInt()),
						GravelLoadingEventTypes.UnloadingDone, truckCurrentlyLoaded, null, this));

				utilStart(timeStep);
				return true;
			}
		}
		else
		{
			Event event = eventQueue.getNextEvent(timeStep, true, GravelLoadingEventTypes.UnloadingDone, null, this);
			if (event != null && event.getObjectAttached() != null
					&& event.getObjectAttached().getClass() == Truck.class)
			{
				eventQueue.remove(event);

				drivingToLoadingDock = this.ClosestLoadingDock();
				//drivingToLoadingDock = Routing.customizableRouting(this.latitude, this.longitude, ld.getLatitude(), ld.getLongitude());
				//drivingToLoadingDock = Routing.customizableRouting(this.latitude, this.longitude, 48.77585, 9.18293);
				eventQueue.add(new Event(
						timeStep + event.getObjectAttached().addUtilization(drivingToLoadingDock),
						GravelLoadingEventTypes.Loading, truckCurrentlyLoaded, LoadingDock.class, null));

				truckCurrentlyLoaded = null;
				utilStop(timeStep);		
				return true;
			}
		}

		return false;
	}
}
