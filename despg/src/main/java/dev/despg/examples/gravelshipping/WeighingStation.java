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

public final class WeighingStation extends SimulationObject
{
	private static final int TIME_TO_WEIGH_TRUCK = 10;
	private static final int MAXLOAD = 40;

	private String name;
	private Truck truckInWeighingStation;

	private static Randomizer drivingToCustomer;
	private static EventQueue eventQueue;

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

		eventQueue = EventQueue.getInstance();

		//TODO Replace with Shipment
		drivingToCustomer = new Randomizer();
		drivingToCustomer.addProbInt(0.5, 120);
		drivingToCustomer.addProbInt(0.8, 150);
		drivingToCustomer.addProbInt(1.0, 180);

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

			if (truckToWeighLoad != null && truckToWeighLoad > MAXLOAD)
			{
				GravelShipping.setGravelToShip(GravelShipping.getGravelToShip() + truckToWeighLoad);
				GravelShipping.increaseUnsuccessfulLoadingSizes(truckToWeighLoad);
				GravelShipping.increaseUnsuccessfulLoadings();
				driveToLoadingStation = truckInWeighingStation.addUtilization(drivingToCustomer.nextInt());
			}
			else
			{
				GravelShipping.increaseGravelShipped(truckToWeighLoad);
				GravelShipping.increaseSuccessfulLoadingSizes(truckToWeighLoad);
				GravelShipping.increaseSuccessfulLoadings();
				driveToLoadingStation = truckInWeighingStation.addUtilization(drivingToCustomer.nextInt());
			}
			eventQueue.add(new Event(timeStep + driveToLoadingStation, GravelLoadingEventTypes.Loading,
					truckInWeighingStation, LoadingDock.class, null));

			truckInWeighingStation.unload();
			truckInWeighingStation = null;
			utilStop(timeStep);
			return true;
		}

		return false;
	}
}
