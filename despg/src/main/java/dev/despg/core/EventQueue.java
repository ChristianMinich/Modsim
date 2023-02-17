/**
 * Copyright (C) 2021 despg.dev, Ralf Buschermöhle
 *
 * DESPG is made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * see LICENSE
 *
 */
package dev.despg.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import dev.despg.examples.gravelshipping.GravelLoadingEventTypes;
import dev.despg.examples.gravelshipping.WeighingStation;
 


/**
 * The EventQueue class manages an ArrayList of all scheduled Events.
 */
public final class EventQueue extends ArrayList<Event>
{
	private static Logger logger = Logger.getLogger("dev.despg.core.EventQueue");

	private static final long serialVersionUID = 1L;
	private static final int MAX_EVENTS = 10000;

	private EventQueue()
	{
		super(MAX_EVENTS);
	}

	/**
	 * Nested class that holds the EventQueue instance.
	 */
	private static class Inner
	{
		private static EventQueue eventqueue = new EventQueue();
	}

	/**
	 * Gets the Instance of the EventQueue.
	 *
	 * @return The EventQueue Instance
	 */
	public static EventQueue getInstance()
	{
		return Inner.eventqueue;
	}

	/**
	 * Adds an Event to the EventQueue.
	 *
	 * @param e the event to add
	 */
	public boolean add(Event e)
	{
		boolean success = super.add(e);
		logger.log(Level.FINEST, "addEvent '" + e + "' " + this);
		return success;
	}

	/**
	 * Removes an Event from the EventQueue.
	 *
	 * @param e Event to be removed
	 */
	public void remove(Event e)
	{
		super.remove(e);
		logger.log(Level.FINEST, "removeEvent '" + e + "' " + this);
	}

	private ArrayList<Event> filterEvents(long timeStep, boolean past, UniqueEventDescription eventTypeNumber,
			Class<? extends SimulationObject> receiverClass, SimulationObject receiverObject)
	{
		ArrayList<Event> subevents = new ArrayList<Event>(this.size());

		for (Event e : this)
		{
			if ((past && timeStep >= e.getTimeStep() || !past && timeStep <= e.getTimeStep())
					&& (receiverClass == null || receiverClass == e.getReceiverClass())
					&& (receiverObject == null || receiverObject == e.getReceiver())
					&& (eventTypeNumber == null || eventTypeNumber == e.getEventDescription()))
				subevents.add(e);
		}
		return subevents;
	}

	/**
	 * The getNextEvent method creates an ArrayList of all sub events that match the
	 * defined filters and returns the Event with the lowest timeStep from that List.
	 *
	 * @param timeStep        Gets events from this timeStep forwards
	 * @param past            Include events from the past
	 * @param eventTypeNumber Filter for specific event type
	 * @param receiverClass   Filter for specific receiving class
	 * @param receiverObject  Filter for specific receiving object
	 * @return returns the Event in the EventQueue with the lowest timeStep which
	 *         matches the defined filters or null of no Event could be filtered
	 */
	public Event getNextEvent(long timeStep, boolean past, UniqueEventDescription eventTypeNumber,
			Class<? extends SimulationObject> receiverClass, SimulationObject receiverObject)
	{
		ArrayList<Event> events = filterEvents(timeStep, past, eventTypeNumber, receiverClass, receiverObject);

		if (events.size() > 0)
		{
			Collections.sort(events);
			return events.get(0);
		}

		return null;
	}

	public int countEvents(long timeStep, boolean past, UniqueEventDescription eventTypeNumber,
			Class<? extends SimulationObject> receiverClass, SimulationObject receiverObject)
	{
		return filterEvents(timeStep, past, eventTypeNumber, receiverClass, receiverObject).size();
	}


	/*public Event getNextFreeObject(long timeStep, boolean past, UniqueEventDescription eventTypeNumber,
		Class<? extends SimulationObject> receiverClass, SimulationObject receiverObject)
	{
			ArrayList<Event> subevents = new ArrayList<Event>(this.size());

			for (Event e : this)
			{
				if ((past && timeStep >= e.getTimeStep())
						&& (receiverClass == e.getReceiverClass())
						&& (receiverObject == e.getReceiver())
						&& (eventTypeNumber == e.getEventDescription()))
					subevents.add(e);
			}
	ArrayList<Event> events = subevents;

	if (events.size() > 0)
	{
		Collections.sort(events);
		return events.get(0);
	}

	return null;*/
	
	public SimulationObject getNextWeighingStation(long timeStep, boolean past, UniqueEventDescription eventTypeNumber,
            Class<? extends SimulationObject> receiverClass, SimulationObject receiverObject)
    {
        ArrayList<Event> events = filterEvents(timeStep, past, GravelLoadingEventTypes.WeighingDone, receiverClass, receiverObject);

        if (events.size() > 0)
        {
            Collections.sort(events);
                    return events.get(0).getReceiver();
                }
        return null; 
    }
	
	public SimulationObject getNextLoadingDock(long timeStep, boolean past, UniqueEventDescription eventTypeNumber,
            Class<? extends SimulationObject> receiverClass, SimulationObject receiverObject)
    {
        ArrayList<Event> events = filterEvents(timeStep, past, GravelLoadingEventTypes.LoadingDone, receiverClass, receiverObject);

        if (events.size() > 0)
        {
            Collections.sort(events);
                    return events.get(0).getReceiver();
                }
        return null; 
    }
}

