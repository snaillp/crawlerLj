package entity;

import java.util.List;

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
	private String title;
	private String district_id;
	private String bizcircle_id;
	private String bizcircle_name; //商圈
	private String community_id;
	private String community_name; //小区名
	private String floor_state;
	private String orientation; //朝向
	private String decoration; //装修
	private String building_type; //板楼or塔楼
	private List<SchoolInfo> school_info;
	private List<SubwayInfo> subway_info;
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBizcircle_name() {
		return bizcircle_name;
	}
	public void setBizcircle_name(String bizcircle_name) {
		this.bizcircle_name = bizcircle_name;
	}
	public String getDistrict_id() {
		return district_id;
	}
	public void setDistrict_id(String district_id) {
		this.district_id = district_id;
	}
	public String getCommunity_name() {
		return community_name;
	}
	public void setCommunity_name(String community_name) {
		this.community_name = community_name;
	}
	public String getFloor_state() {
		return floor_state;
	}
	public void setFloor_state(String floor_state) {
		this.floor_state = floor_state;
	}
	public String getBizcircle_id() {
		return bizcircle_id;
	}
	public void setBizcircle_id(String bizcircle_id) {
		this.bizcircle_id = bizcircle_id;
	}
	public String getCommunity_id() {
		return community_id;
	}
	public void setCommunity_id(String community_id) {
		this.community_id = community_id;
	}
	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	public String getDecoration() {
		return decoration;
	}
	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}
	public String getBuilding_type() {
		return building_type;
	}
	public void setBuilding_type(String building_type) {
		this.building_type = building_type;
	}
	public List<SchoolInfo> getSchool_info() {
		return school_info;
	}
	public void setSchool_info(List<SchoolInfo> school_info) {
		this.school_info = school_info;
	}
	public List<SubwayInfo> getSubway_info() {
		return subway_info;
	}
	public void setSubway_info(List<SubwayInfo> subway_info) {
		this.subway_info = subway_info;
	}
	
}
