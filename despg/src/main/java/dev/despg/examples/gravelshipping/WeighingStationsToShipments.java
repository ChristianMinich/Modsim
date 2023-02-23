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

//TODO
public final class WeighingStationsToShipments extends HashMap<WeighingStation, Shipment>
{
	private static Logger logger = Logger.getLogger("dev.despg.examples.gravelshipping.WeighingStationsToShipments");

	private static final long serialVersionUID = 1L;
	private static final int MAX_LOCATIONS = 10000;

	private WeighingStationsToShipments()
	{
		super(MAX_LOCATIONS);
	}

	//TODO
	private static class Inner
	{
		private static WeighingStationsToShipments weighingStationsToShipments = new WeighingStationsToShipments();
	}

	//TODO
	public static WeighingStationsToShipments getInstance()
	{
		return Inner.weighingStationsToShipments;
	}
}

