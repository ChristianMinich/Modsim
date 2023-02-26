package dev.despg.examples.gravelshipping;

public class ShipmentWithDistance
{

	private Shipment shipment = null;
	private Long drivingTime =  null;

	public ShipmentWithDistance(Shipment shipment, Long drivingTime)
	{
		this.shipment = shipment;
		this.drivingTime = drivingTime;
	}

	/**
	 *
	 * @return {@link Shipment}
	 */
	public Shipment getShipment()
	{
		return shipment;
	}

	/**
	 *
	 * @return long
	 */
	public Long getDrivingTime()
	{
		return drivingTime;
	}

}
