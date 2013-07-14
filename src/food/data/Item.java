package food.data;

public class Item {
	String itemId;
	String itemName;
	String itemDesc;
	String itemurl;
	
	public Item(String itemId, String itemName, String itemString, String itemurl) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemDesc = itemString;
		this.itemurl = itemurl;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String id) {
		this.itemId = id;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String name) {
		this.itemName = name;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	
	public String getItemurl() {
		return itemurl;
	}
	
	public void setItemurl(String itemurl) {
		this.itemurl = itemurl;
	}
}
