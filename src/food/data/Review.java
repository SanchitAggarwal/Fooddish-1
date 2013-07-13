package food.data;

import java.util.Date;

public class Review {
	String reviewId;
	String authorName;
	String desc;
	int rating;
	Date date;
	
	public Review(String reviewId, String authorName, String desc, int rating,
			Date date) {
		this.reviewId = reviewId;
		this.authorName = authorName;
		this.desc = desc;
		this.rating = rating;
		this.date = date;
	}
	public String getReviewId() {
		return reviewId;
	}
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
