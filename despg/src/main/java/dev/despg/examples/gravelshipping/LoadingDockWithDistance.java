package dev.despg.examples.gravelshipping;

public class LoadingDockWithDistance
{

	private LoadingDock loadingDock = null;
	private Long drivingTime =  null;

	public LoadingDockWithDistance(LoadingDock loadingDock, Long drivingTime)
	{
		this.loadingDock = loadingDock;
		this.drivingTime = drivingTime;
	}

	/**
	 *
	 * @return {@link LoadingDock}
	 */
	public LoadingDock getloadingDock()
	{
		return loadingDock;
	}

	/**
	 *
	 * @return Long
	 */
	public Long getDrivingTime()
	{
		return drivingTime;
	}

}
