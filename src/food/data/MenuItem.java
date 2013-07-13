package food.data;

import org.bson.types.ObjectId;

public class MenuItem {
	ObjectId itemID;
	int rating;
	String imgurl;
	
	public MenuItem(ObjectId item, int rating, String imgurl) {
		super();
		this.itemID = item;
		this.rating = rating;
		this.imgurl = imgurl;
	}
	public ObjectId getItem() {
		return itemID;
	}
	public void setItem(ObjectId item) {
		this.itemID = item;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
}
