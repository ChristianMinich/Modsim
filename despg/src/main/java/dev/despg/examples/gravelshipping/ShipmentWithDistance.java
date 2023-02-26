package dev.despg.examples.gravelshipping;

public class ShipmentWithDistance {

	private Shipment shipment = null;
	private Long drivingTime =  null;
	
	public ShipmentWithDistance(Shipment shipment, Long drivingTime) {
		this.shipment = shipment;
		this.drivingTime = drivingTime;
	}

	public Shipment getShipment() {
		return shipment;
	}

	public Long getDrivingTime() {
		return drivingTime;
	}

}
