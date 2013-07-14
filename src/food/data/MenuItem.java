package food.data;

public class MenuItem {
	String itemID;
	double rating;
	
	public MenuItem(String item, double rating) {
		this.itemID = item;
		this.rating = rating;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
	
}
