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
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import dev.despg.core.SimulationObject;
import dev.despg.examples.gravelshipping.GravelLoadingEventTypes;
import dev.despg.examples.gravelshipping.WeighingStation;
 


//TODO
public final class ShipmentsToLoadingDocks extends HashMap<Shipment, LoadingDock>
{
	private static Logger logger = Logger.getLogger("dev.despg.examples.gravelshipping.ShipmentsToLoadingDocks");

	private static final long serialVersionUID = 1L;
	private static final int MAX_LOCATIONS = 10000;

	private ShipmentsToLoadingDocks()
	{
		super(MAX_LOCATIONS);
	}

	//TODO
	private static class Inner
	{
		private static ShipmentsToLoadingDocks shipmentsToLoadingDocks = new ShipmentsToLoadingDocks();
	}

	//TODO
	public static ShipmentsToLoadingDocks getInstance()
	{
		return Inner.shipmentsToLoadingDocks;
	}
}

