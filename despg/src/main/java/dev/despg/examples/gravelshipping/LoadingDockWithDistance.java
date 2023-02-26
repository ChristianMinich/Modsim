package dev.despg.examples.gravelshipping;

public class LoadingDockWithDistance {
	
	private LoadingDock loadingDock = null;
	private Long drivingTime =  null;
	
	public LoadingDockWithDistance(LoadingDock loadingDock, Long drivingTime) {
		this.loadingDock = loadingDock;
		this.drivingTime = drivingTime;
	}

	public LoadingDock getloadingDock() {
		return loadingDock;
	}

	public Long getDrivingTime() {
		return drivingTime;
	}

}
