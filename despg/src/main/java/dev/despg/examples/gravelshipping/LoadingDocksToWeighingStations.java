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

import java.util.HashMap;
import java.util.logging.Logger;
 

/**
 * Maintains a HashMap of every initialized {@link LoadingDock} as a key with
 * it's corresponding {@link WeighingStation} as it's value.
 */
public final class LoadingDocksToWeighingStations extends HashMap<LoadingDock, WeighingStationWithDistance>
{
	private static Logger logger = Logger.getLogger("dev.despg.examples.gravelshipping.LoadingDocksToWeighingStations");

	private static final long serialVersionUID = 1L;
	private static final int MAX_LOCATIONS = 10000;

	private LoadingDocksToWeighingStations()
	{
		super(MAX_LOCATIONS);
	}

	/**
	 * Initializes {@link LoadingDocksToWeighingStations} inside the Inner Class
	 * creating a Singleton instance of itself.
	 *
	 */
	private static class Inner
	{
		private static LoadingDocksToWeighingStations loadingDocksToWeighingStations = new LoadingDocksToWeighingStations();
	}
	

	/**
	 * Gets the instance of {@link LoadingDocksToWeighingStations}.
	 *
	 * @return The Singleton instance of {@link LoadingDocksToWeighingStations}
	 */
	public static LoadingDocksToWeighingStations getInstance()
	{
		return Inner.loadingDocksToWeighingStations;
	}
	
	/*public void RouteCalculation () {		
			LoadingDocksToWeighingStations.entrySet().forEach(entry -> {
			    System.out.println(entry.getKey() + " " + entry.getValue());
			});*/
		
	}


	