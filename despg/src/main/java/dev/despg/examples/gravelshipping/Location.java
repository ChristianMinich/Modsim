package dev.despg.examples.gravelshipping;

public class Location {
	
	private String name;
	private Long latitude;
	private Long longitude;
	
	public Location(String name, Long latitude, Long longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	private String getName() {
		return name;
	}

	private Long getLatitude() {
		return latitude;
	}

	private Long getLongitude() {
		return longitude;
	}

}
