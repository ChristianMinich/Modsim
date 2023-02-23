package dev.despg.examples.gravelshipping;

public class Location {
	
	private String name;
	private Double latitude;
	private Double longitude;
	
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
