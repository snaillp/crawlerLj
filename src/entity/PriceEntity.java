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
	@Override
	public boolean equals(Object p)
	{
		if(!(p instanceof PriceEntity)){
			return false;
		}
		PriceEntity pe = (PriceEntity)p;
		if(price == pe.getPrice()){
			return true;
		}
		return false;
	}
	public int compareto(PriceEntity p)
	{
		if(price > p.price){
			return 1;
		}else if(price == p.price){
			return 0;
		}else{
			return -1;
		}
	}
}
