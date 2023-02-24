package dev.despg.examples.gravelshipping;

public class WeighingStationWithDistance {
	
	private WeighingStation weighingStation = null;
	private Long drivingTime =  null;
	
	public WeighingStationWithDistance(WeighingStation weighingStation, Long drivingTime) {
		this.weighingStation = weighingStation;
		this.drivingTime = drivingTime;
	}

	public WeighingStation getWeighingStation() {
		return weighingStation;
	}

	public Long getDrivingTime() {
		return drivingTime;
	}

}
