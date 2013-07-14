package food.data;

import java.util.Date;

public class Deal {
	Date startDate;
	Date endDate;
	String itemId;
	String hotelId;
	double discount;
	public Deal(Date startDate, Date endDate, String itemId, String hotelId,
			double discount) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.itemId = itemId;
		this.hotelId = hotelId;
		this.discount = discount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getHotelId() {
		return hotelId;
	}
	public void setHotelId(String hotelId) {
		this.hotelId = hotelId;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	
}
