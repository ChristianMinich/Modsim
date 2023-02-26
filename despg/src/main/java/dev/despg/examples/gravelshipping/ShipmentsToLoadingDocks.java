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
 * Maintains a HashMap of every initialized {@link Shipment} as a key with
 * it's corresponding {@link LoadingDock} as it's value.
 */
public final class ShipmentsToLoadingDocks extends HashMap<Shipment, LoadingDockWithDistance>
{
	private static Logger logger = Logger.getLogger("dev.despg.examples.gravelshipping.ShipmentsToLoadingDocks");

	private static final long serialVersionUID = 1L;
	private static final int MAX_LOCATIONS = 10000;

	private ShipmentsToLoadingDocks()
	{
		super(MAX_LOCATIONS);
	}

	/**
	 * Initializes {@link ShipmentsToLoadingDocks} inside the Inner Class
	 * creating a Singleton instance of itself.
	 *
	 */
	private static class Inner
	{
		private static ShipmentsToLoadingDocks shipmentsToLoadingDocks = new ShipmentsToLoadingDocks();
	}

	/**
	 * Gets the instance of {@link ShipmentsToLoadingDocks}.
	 *
	 * @return The Singleton instance of {@link ShipmentsToLoadingDocks}
	 */
	public static ShipmentsToLoadingDocks getInstance()
	{
		return Inner.shipmentsToLoadingDocks;
	}
}

