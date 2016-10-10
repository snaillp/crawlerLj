package entity;

public class HouseRequestEntity {
	private String request_id;
	private HouseListEntity data;
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public HouseListEntity getData() {
		return data;
	}
	public void setData(HouseListEntity data) {
		this.data = data;
	}
	
}
