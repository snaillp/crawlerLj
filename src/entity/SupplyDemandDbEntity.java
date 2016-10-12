package entity;

import java.util.List;

import util.TimeUtil;

public class SupplyDemandDbEntity extends CommonEntity{
	
	public SupplyDemandDbEntity(SupplyDemandTrend4Crawl.MonthDayTrend mdt, String type){
		this.type = type;
		crawltime = TimeUtil.getCurSecond();
		List<String> timeList = mdt.getDuration();
		String duration = timeList.get(timeList.size()-1);
		if(duration.contains("月") && type.equals("month")){
			String monthStr = duration.replace("月", "");
			int month = Integer.parseInt(monthStr);
			int lastMonth = Integer.parseInt(TimeUtil.getMonthInc("M", -1)); //Java日历中的月份从0开始
			if(month == lastMonth){
				datetime = TimeUtil.getMonthInc("yyyyMM", -1);
			}
			dataType = HouseDataType.HouseSupDemMonthStat;
		}else if(duration.contains("日") && type.equals("day")){
			String dayStr = duration.replace("日", "");
			int day = Integer.parseInt(dayStr);
			int lastDay = Integer.parseInt(TimeUtil.getDayInc("d", -1)); //Java日历中的月份从0开始
			if(day == lastDay){
				datetime = TimeUtil.getDayInc("yyyyMMdd", -1);
			}
			dataType = HouseDataType.HouseSupDemDayStat;
		}
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
	private HouseDataType dataType;
	private String type;
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
