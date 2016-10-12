package entity;

import java.util.List;

import com.google.gson.Gson;

public class SupplyDemandTrend4Crawl {

	public MonthDayTrend getMonth() {
		return month;
	}

	public void setMonth(MonthDayTrend month) {
		this.month = month;
	}

	public MonthDayTrend getDay() {
		return day;
	}

	public void setDay(MonthDayTrend day) {
		this.day = day;
	}

	private MonthDayTrend month;
	private MonthDayTrend day;
	
	public class MonthDayTrend{
		
		public List<String> getDuration() {
			return duration;
		}
		public void setDuration(List<String> duration) {
			this.duration = duration;
		}
		public List<Integer> getHouseAmount() {
			return houseAmount;
		}
		public void setHouseAmount(List<Integer> houseAmount) {
			this.houseAmount = houseAmount;
		}
		public List<Integer> getCustomerAmount() {
			return customerAmount;
		}
		public void setCustomerAmount(List<Integer> customerAmount) {
			this.customerAmount = customerAmount;
		}
		public List<Integer> getShowAmount() {
			return showAmount;
		}
		public void setShowAmount(List<Integer> showAmount) {
			this.showAmount = showAmount;
		}
		public List<Double> getCustomerHouseRatio() {
			return customerHouseRatio;
		}
		public void setCustomerHouseRatio(List<Double> customerHouseRatio) {
			this.customerHouseRatio = customerHouseRatio;
		}
		public double getYoyHouse() {
			return yoyHouse;
		}
		public void setYoyHouse(double yoyHouse) {
			this.yoyHouse = yoyHouse;
		}
		public double getYoyCustomer() {
			return yoyCustomer;
		}
		public void setYoyCustomer(double yoyCustomer) {
			this.yoyCustomer = yoyCustomer;
		}
		public double getYoyRatio() {
			return yoyRatio;
		}
		public void setYoyRatio(double yoyRatio) {
			this.yoyRatio = yoyRatio;
		}
		public double getMomHouse() {
			return momHouse;
		}
		public void setMomHouse(double momHouse) {
			this.momHouse = momHouse;
		}
		public double getMomCustomer() {
			return momCustomer;
		}
		public void setMomCustomer(double momCustomer) {
			this.momCustomer = momCustomer;
		}
		public double getMomRatio() {
			return momRatio;
		}
		public void setMomRatio(double momRatio) {
			this.momRatio = momRatio;
		}
		private List<String> duration;  //时间标签，单位为月或天(如9月或12日)
		private List<Integer> houseAmount;  //新增房源量
		private List<Integer> customerAmount; //新增客源量
		private List<Integer> showAmount;     //带看量
		private List<Double> customerHouseRatio; //客房比
		private double yoyHouse;
		private double yoyCustomer;
		private double yoyRatio;
		private double momHouse;   //相比前一次的房源增长，百分比
		private double momCustomer; //相比前一个duration客户增长幅度
		private double momRatio;    //客房比的增长
	}
}
