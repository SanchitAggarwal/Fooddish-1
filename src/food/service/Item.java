package food.service;

public class Item {
	int itemId;
	String itemName;
	String imgurl;
	int rating;
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int id) {
		this.itemId = id;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String name) {
		this.itemName = name;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	
}
