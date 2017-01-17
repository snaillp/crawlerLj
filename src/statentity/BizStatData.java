package statentity;

import entity.CommonEntity;

public class BizStatData extends CommonEntity{
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public long getComtime() {
		return comtime;
	}
	public void setComtime(long comtime) {
		this.comtime = comtime;
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
	public int getHousenum() {
		return housenum;
	}
	public void setHousenum(int housenum) {
		this.housenum = housenum;
	}
	private String date;
	private long comtime;
	private double average;
	private double variance;
	private int housenum;
}
