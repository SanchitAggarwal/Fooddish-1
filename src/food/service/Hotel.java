package food.service;

import java.util.ArrayList;

public class Hotel {
	int hotelId;
	String hotelName;
	ArrayList<Item> menuItems;
	double lat;
	double lng;
	ArrayList<Review> userReviews;
	public int getHotelId() {
		return hotelId;
	}
	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public ArrayList<Item> getMenuItems() {
		return menuItems;
	}
	public void setMenuItems(ArrayList<Item> menuItems) {
		this.menuItems = menuItems;
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
	public ArrayList<Review> getUserReviews() {
		return userReviews;
	}
	public void setUserReviews(ArrayList<Review> userReviews) {
		this.userReviews = userReviews;
	}
}
