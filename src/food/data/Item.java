package food.data;

public class Item {
	int itemId;
	String itemName;
	String itemDesc;
	
	public Item(int itemId, String itemName, String itemString) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemDesc = itemString;
	}
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
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
}
