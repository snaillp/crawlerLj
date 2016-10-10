package entity;

import java.util.List;

public class HouseListEntity {
	private List<HouseEntity> list;
	private int has_more_data;
	private int return_count;
	private int total_count;
	public List<HouseEntity> getList() {
		return list;
	}
	public void setList(List<HouseEntity> list) {
		this.list = list;
	}
	public int getHas_more_data() {
		return has_more_data;
	}
	public void setHas_more_data(int has_more_data) {
		this.has_more_data = has_more_data;
	}
	public int getReturn_count() {
		return return_count;
	}
	public void setReturn_count(int return_count) {
		this.return_count = return_count;
	}
	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	
}
