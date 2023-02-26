package dev.despg.examples.gravelshipping;

public class WeighingStationWithDistance
{

	private WeighingStation weighingStation = null;
	private Long drivingTime =  null;

	public WeighingStationWithDistance(WeighingStation weighingStation, Long drivingTime)
	{
		this.weighingStation = weighingStation;
		this.drivingTime = drivingTime;
	}

	/**
	 *
	 * @return {@link WeighingStation}
	 */
	public WeighingStation getWeighingStation()
	{
		return weighingStation;
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
