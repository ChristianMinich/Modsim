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

		/*//TODO Replace with Shipment
		drivingToWeighingStation = new Randomizer();
		drivingToWeighingStation.addProbInt(0.5, 30);
		drivingToWeighingStation.addProbInt(0.78, 45);
		drivingToWeighingStation.addProbInt(1.0, 60);*/
		
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
	 * @param timeStep
	 * @return true, if an Event has been processed - false, if it failed to process an Event.
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
				// verlade das Gravel was du noch hast
				truckCurrentlyLoaded.unload();
				SimulationObject nextLoadingDock = eventQueue.getNextLoadingDock(timeStep, false, null, null, null);
				LoadingDock ld = (LoadingDock) nextLoadingDock;
				drivingToLoadingDock = Routing.customizableRouting(this.latitude, this.longitude, ld.getLatitude(), ld.getLongitude());
				eventQueue.add(new Event(timeStep + truckCurrentlyLoaded.addUtilization(unloadingTime.nextInt() + drivingToLoadingDock),
						GravelLoadingEventTypes.UnloadingDone, truckCurrentlyLoaded, null, this));

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

				SimulationObject nextLoadingDock = eventQueue.getNextLoadingDock(timeStep, false, null, null, null);
				LoadingDock ld = (LoadingDock) nextLoadingDock;
				
				drivingToLoadingDock = Routing.customizableRouting(this.latitude, this.longitude, ld.getLatitude(), ld.getLongitude());
				
				eventQueue.add(new Event(
						timeStep + event.getObjectAttached().addUtilization(drivingToLoadingDock),
						GravelLoadingEventTypes.Unloading, truckCurrentlyLoaded, LoadingDock.class, null));

				truckCurrentlyLoaded = null;
				utilStop(timeStep);		
				return true;
			}
		}

		return false;
	}
}
