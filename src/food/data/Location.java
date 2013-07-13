package food.data;

public class Location {
	double lat;
	double lng;
	String address;
	String city;
	String state;
	String country;
	
	public Location(double lat, double lng, String address, String city,
			String state, String country) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.address = address;
		this.city = city;
		this.state = state;
		this.country = country;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
}
