package food.data;

import java.util.ArrayList;

public class Hotel {
	String hotelId;
	String hotelName;
	ArrayList<MenuItem> menuItems;
	ArrayList<Review> userReviews;
	Location location;
	long checkinsCount;
	long usersCount;
	long tipCount;
	double ratings;
	String contact;
	
	public Hotel(String hotelId, String hotelName, ArrayList<MenuItem> menuItems,
			ArrayList<Review> userReviews, Location location,
			long checkinsCount, long usersCount, long tipCount, double ratings,
			String contact) {
		super();
		this.hotelId = hotelId;
		this.hotelName = hotelName;
		this.menuItems = menuItems;
		this.userReviews = userReviews;
		this.location = location;
		this.checkinsCount = checkinsCount;
		this.usersCount = usersCount;
		this.tipCount = tipCount;
		this.ratings = ratings;
		this.contact = contact;
	}
	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	public String getHotelName() {
		return hotelName;
	}
	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}
	public ArrayList<MenuItem> getMenuItems() {
		return menuItems;
	}
	public void setMenuItems(ArrayList<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}
	public ArrayList<Review> getUserReviews() {
		return userReviews;
	}
	public void setUserReviews(ArrayList<Review> userReviews) {
		this.userReviews = userReviews;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public long getCheckinsCount() {
		return checkinsCount;
	}
	public void setCheckinsCount(long checkinsCount) {
		this.checkinsCount = checkinsCount;
	}
	public long getUsersCount() {
		return usersCount;
	}
	public void setUsersCount(long usersCount) {
		this.usersCount = usersCount;
	}
	public long getTipCount() {
		return tipCount;
	}
	public void setTipCount(long tipCount) {
		this.tipCount = tipCount;
	}
	public double getRatings() {
		return ratings;
	}
	public void setRatings(double ratings) {
		this.ratings = ratings;
	}
	
}
