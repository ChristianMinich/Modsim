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

import java.util.Map;

import dev.despg.core.Event;
import dev.despg.core.EventQueue;
import dev.despg.core.Randomizer;
import dev.despg.core.SimulationObject;
import dev.despg.core.SimulationObjects;

public final class WeighingStation extends SimulationObject
{
	private static final int TIME_TO_WEIGH_TRUCK = 10;
	private static final int MAXLOAD = 40;

	private String name;
	private Truck truckInWeighingStation;

	private static Long drivingToCustomer;
	private static EventQueue eventQueue;
	private static Randomizer timeToRemoveGravel;
	
	private static WeighingStationsToShipments wsts = WeighingStationsToShipments.getInstance();


	/**
	 * Constructor for new WeightingStations, injects its dependency to
	 * SimulationObjects and creates the required randomizer instances.
	 *
	 * @param name Name of the WeightingStation instance
	 */
	public WeighingStation(String name, Double latitude, Double longitude)
	{
		this.name = name;
		
		this.latitude = latitude;
		this.longitude = longitude;
		
		timeToRemoveGravel = new Randomizer();
		timeToRemoveGravel.addProbInt(0.3, 60);
		timeToRemoveGravel.addProbInt(0.8, 120);
		timeToRemoveGravel.addProbInt(1.0, 180);

		eventQueue = EventQueue.getInstance();

		SimulationObjects.getInstance().add(this);
	}

	@Override
	public String toString()
	{
		String toString = "Weighing Station:" + name;
		if (truckInWeighingStation != null)
			toString += " " + "loading: " + truckInWeighingStation;
		return toString;
	}
	
	// Shipment as alternative
	public long ClosestShipment() {
		long currentSmallestDistance = 0;
		long currentDistance = 0;
		//Shipment currShipment = null;
		for(Map.Entry<WeighingStation, Shipment> set :
			wsts.entrySet())
		{
			
			currentDistance = Routing.customizableRouting(this.latitude, this.longitude, set.getValue().getLatitude(), set.getValue().getLongitude());
			
			if( currentDistance < currentSmallestDistance) {
				currentSmallestDistance = currentDistance;
				//currShipment = set.getValue();
				
			}
			
			/*System.out.println(set.getKey() + " = "
                    + set.getValue());*/
		}
		return currentSmallestDistance;
	}
	/**
	 * Gets called every timeStep
	 *
	 * Checks events from the event queue that either are assigned to this class or
	 * to an object of this class. If it is assigned to this class, the object of
	 * which the simulate function got called, checks if it is currently occupied
	 * and if the attached object is indeed a truck. In that case, the event gets
	 * removed from the queue, gets executed and a new event gets added to the queue
	 * which gets triggered when the weighting is done.
	 *
	 * A "weighting is done" event gets pulled from the queue if the receiving
	 * object is the object on which the simulate function got called on. In that
	 * case the event gets removed from the queue and handled by checking if trucks
	 * load is above the maximum allowed load or not. If it is above, it will count
	 * as an unsuccessful loading, else it will count ass successful and be shipped.
	 * In either case there will be a new event added to the event queue with no
	 * difference in parameters.
	 *
	 * @return true if an assignable event got found and handled, false if no event
	 *         could get assigned
	 */
	@Override
	public boolean simulate(long timeStep)
	{
		Event event = eventQueue.getNextEvent(timeStep, true, GravelLoadingEventTypes.Weighing, this.getClass(), null);
		if (truckInWeighingStation == null && event != null && event.getObjectAttached() != null
				&& event.getObjectAttached().getClass() == Truck.class)
		{
			eventQueue.remove(event);
			truckInWeighingStation = (Truck) event.getObjectAttached();
			eventQueue.add(new Event(timeStep + truckInWeighingStation.addUtilization(TIME_TO_WEIGH_TRUCK),
					GravelLoadingEventTypes.WeighingDone, truckInWeighingStation, null, this));
			utilStart(timeStep);
			return true;
		}

		event = eventQueue.getNextEvent(timeStep, true, GravelLoadingEventTypes.WeighingDone, null, this);
		if (event != null && event.getObjectAttached() != null && event.getObjectAttached().getClass() == Truck.class)
		{
			eventQueue.remove(event);
			final Integer truckToWeighLoad = truckInWeighingStation.getLoad();
			long driveToLoadingStation;
			
			//drivingToCustomer = Routing.customizableRouting(this.latitude, this.longitude, this.ClosestShipment().getLatitude(), this.ClosestShipment().getLongitude());
			drivingToCustomer = this.ClosestShipment();

			if (truckToWeighLoad != null && truckToWeighLoad > MAXLOAD)
			{
				GravelShipping.setGravelToShip(GravelShipping.getGravelToShip() + truckToWeighLoad);
				GravelShipping.increaseUnsuccessfulLoadingSizes(truckToWeighLoad);
				GravelShipping.increaseUnsuccessfulLoadings();
				
				//drivingToCustomer = ClosestShipment();
				//drivingToCustomer = Routing.customizableRouting(this.latitude, this.longitude, sp.getLatitude(), sp.getLongitude());
				//drivingToCustomer = Routing.customizableRouting(this.latitude, this.longitude, 52.37589, 9.73201);
				driveToLoadingStation = truckInWeighingStation.addUtilization(timeToRemoveGravel.nextInt());
			}
			else
			{
				GravelShipping.increaseGravelShipped(truckToWeighLoad);
				GravelShipping.increaseSuccessfulLoadingSizes(truckToWeighLoad);
				GravelShipping.increaseSuccessfulLoadings();
				
				//drivingToCustomer = Routing.customizableRouting(this.latitude, this.longitude, sp.getLatitude(), sp.getLongitude());
				//drivingToCustomer = Routing.customizableRouting(this.latitude, this.longitude, 52.37589, 9.73201);
				driveToLoadingStation = truckInWeighingStation.addUtilization(drivingToCustomer);
			}
			
			eventQueue.add(new Event(timeStep + driveToLoadingStation, GravelLoadingEventTypes.Unloading,
					truckInWeighingStation, Shipment.class, null));

			truckInWeighingStation.unload();
			truckInWeighingStation = null;
			utilStop(timeStep);
			return true;
		}

		return false;
	}
}
