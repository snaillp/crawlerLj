package statentity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class BizStat {
	private String bizname;
	private double average;
	private double variance;
	private List<Integer> houseUnitpriceList;
	public String getBizname() {
		return bizname;
	}
	public void setBizname(String bizname) {
		this.bizname = bizname;
	}
	public double getAverage() {
		return average;
	}
	public void setAverage(double average) {
		this.average = average;
	}
	public double getVariance() {
		return variance;
	}
	public void setVariance(double variance) {
		this.variance = variance;
	}
	public List<Integer> getHouseUnitpriceList() {
		return houseUnitpriceList;
	}
	public void setHouseUnitpriceList(List<Integer> houseUnitpriceList) {
		this.houseUnitpriceList = houseUnitpriceList;
	}
	public void addHouseUnitprice(int price){
		if(null == houseUnitpriceList){
			houseUnitpriceList = new ArrayList();
		}
		houseUnitpriceList.add(price);
	}
	
	public String toJson()
	{
		List<Integer> hList = this.houseUnitpriceList;
		this.houseUnitpriceList = null;
		String retStr = new Gson().toJson(this);
		this.houseUnitpriceList = hList;
		return retStr;
	}
}
