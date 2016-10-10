package entity;

public class PriceEntity {
	private int price;
	private String date;
	
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public PriceEntity(int price, String date) {
		super();
		this.price = price;
		this.date = date;
	}

	public PriceEntity()
	{
		
	}
	
}
