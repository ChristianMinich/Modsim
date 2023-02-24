package dev.despg.examples.gravelshipping;

import dev.despg.core.SimulationObject;

public class Location {
	
	private String name;
	private Double latitude;
	private Double longitude;
	
	/**
	 * Constructor for new Location, contains the Parameters Latitude and Longitude,
	 * which are being used by {@link Reader}} to store it's Parameters inside an ArrayList,
	 * which are later used to initialize the {@link SimulationObject}'s.
	 * 
	 * @param name - Name
	 * @param latitude - Latitude
	 * @param longitude - Longitude
	 */
	public Location(String name, Double latitude, Double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}
	
	@Override
    public String toString()
    {
        return "Name:" + name + "\tLatitude:" + latitude + "\tLongitude:" + longitude;
    }
}
