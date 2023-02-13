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

import dev.despg.core.EventQueue;
import dev.despg.core.SimulationObject;
import dev.despg.core.SimulationObjects;

public class Shipment extends SimulationObject{

	private String name;
	private Truck truckCurrentlyLoaded;
	
	public Shipment(String name) {
		this.name = name;
		
		EventQueue eventQueue = EventQueue.getInstance();
		SimulationObjects.getInstance().add(this);
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
		//TODO Implementation - interact with Class Routing
		return false;
	}

}
