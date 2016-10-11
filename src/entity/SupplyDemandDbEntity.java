package entity;

import java.util.List;

import util.TimeUtil;

public class SupplyDemandMonthDbEntity {
	
	public SupplyDemandMonthDbEntity(SupplyDemandTrend4Crawl.MonthDayTrend mdt){
		crawltime = TimeUtil.getCurSecond();
		List<String> timeList = mdt.getDuration();
		//月与日的时间标签不同
		List<Integer> houseAountList = mdt.getHouseAmount();
		houseAmount = houseAountList.get(houseAountList.size()-1);
		List<Integer> customerAmountList = mdt.getCustomerAmount();
		customerAmount = customerAmountList.get(customerAmountList.size()-1);
		List<Integer> showAmountList = mdt.getShowAmount();
		showAmount = showAmountList.get(showAmountList.size()-1);
		List<Double> customerHouseRatioList = mdt.getCustomerHouseRatio();
		customerHouseRatio = customerHouseRatioList.get(customerHouseRatioList.size()-1);
		yoyHouse = mdt.getYoyHouse();
		yoyCustomer = mdt.getYoyCustomer();
		yoyRatio = mdt.getYoyRatio();
		momHouse = mdt.getMomHouse();
		momCustomer = mdt.getMomCustomer();
		momRatio = mdt.getMomRatio();
	}
	
	private long crawltime;
	private String datetime;  //时间标签
	private int houseAmount;  //新增房源量
	private int customerAmount; //新增客源量
	private int showAmount;     //带看量
	private double customerHouseRatio; //客房比
	private double yoyHouse;
	private double yoyCustomer;
	private double yoyRatio;
	private double momHouse;   //相比前一次的房源增长，百分比
	private double momCustomer; //相比前一个duration客户增长幅度
	private double momRatio;    //客房比的增长
}
