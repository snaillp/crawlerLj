package entity;

public class PriceEntity {
	private double price;
	private String date;
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public PriceEntity(double price, String date) {
		super();
		this.price = price;
		this.date = date;
	}
	public PriceEntity()
	{
		
	}
	
}
