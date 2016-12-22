package statentity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class BizStat implements Comparable<BizStat>{
	private String district;
	private String bizname;
	private String xiaoqu;
	private double average;
	private double variance;
	private double housenum;
	private List<Integer> houseUnitpriceList;
	public String getBizname() {
		return bizname;
	}
	public void setBizname(String bizname) {
		this.bizname = bizname;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getXiaoqu() {
		return xiaoqu;
	}
	public void setXiaoqu(String xiaoqu) {
		this.xiaoqu = xiaoqu;
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
	public double getHousenum() {
		return housenum;
	}
	public void setHousenum(double housenum) {
		this.housenum = housenum;
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
	@Override
	public int compareTo(BizStat arg0) {
		//优先级：区->商圈->均价->房源数->小区名
		int ret = 0;
		if(district != null){
			ret = this.district.compareTo(arg0.district);
		}
		if(ret != 0){
			return ret;
		}
		if(this.bizname != null){
			ret = this.bizname.compareTo(arg0.bizname);
		}
		if(ret != 0){
			return ret;
		}
		if(this.average>arg0.average){
			ret = 1;
		}else if(this.average < arg0.average){
			ret = -1;
		}
		if(ret != 0){
			return ret;
		}
		if(this.housenum > arg0.housenum){
			ret = 1;
		}else if(this.housenum < arg0.housenum){
			ret = -1;
		}
		if(ret != 0){
			return ret;
		}
		if(this.xiaoqu != null){
			ret = this.xiaoqu.compareTo(arg0.xiaoqu);
		}
		if(ret != 0){
			return ret;
		}
		return ret;
	}
}
