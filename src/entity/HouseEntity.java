package entity;

public class HouseEntity {
	private String house_code;
	private String kv_house_type;
	private int blueprint_bedroom_num;
	private int blueprint_hall_num;
	private double area;
	private String sign_date;
	private int sign_price;
	private int price;
	private int sign_unit_price;
	private int unit_price;
	public String getHouse_code() {
		return house_code;
	}
	public void setHouse_code(String house_code) {
		this.house_code = house_code;
	}
	public int getSign_price() {
		return sign_price;
	}
	public void setSign_price(int sign_price) {
		this.sign_price = sign_price;
	}
	public int getSign_unit_price() {
		return sign_unit_price;
	}
	public void setSign_unit_price(int sign_unit_price) {
		this.sign_unit_price = sign_unit_price;
	}
	public String getKv_house_type() {
		return kv_house_type;
	}
	public void setKv_house_type(String kv_house_type) {
		this.kv_house_type = kv_house_type;
	}
	public int getBlueprint_bedroom_num() {
		return blueprint_bedroom_num;
	}
	public void setBlueprint_bedroom_num(int blueprint_bedroom_num) {
		this.blueprint_bedroom_num = blueprint_bedroom_num;
	}
	public int getBlueprint_hall_num() {
		return blueprint_hall_num;
	}
	public void setBlueprint_hall_num(int blueprint_hall_num) {
		this.blueprint_hall_num = blueprint_hall_num;
	}
	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public String getSign_date() {
		return sign_date;
	}
	public void setSign_date(String sign_date) {
		this.sign_date = sign_date;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(int unit_price) {
		this.unit_price = unit_price;
	}
	
}
