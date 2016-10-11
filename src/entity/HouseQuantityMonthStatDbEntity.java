package entity;

import java.util.List;

import util.TimeUtil;

public class HouseQuantityMonthStatDbEntity extends CommonEntity{
	public HouseQuantityMonthStatDbEntity(PriceTrend4Crawl.CurrentLevel cur)
	{
		PriceTrend4Crawl.CurrentLevel.Quantity qt = cur.getQuantity();
		crawltime = TimeUtil.getCurSecond();
		List<String> monthList = cur.getMonth();
		String monthStr = monthList.get(monthList.size()-1).replace("月", "");
		month = Integer.parseInt(monthStr);
		int curMonth = TimeUtil.getCurMonth(); //Java日历中的月份从0开始
		int lastMonth = curMonth-1;
		if(lastMonth<=0){
			lastMonth = 12;
		}
		if(month == lastMonth){
			monthTime = TimeUtil.getMonthInc("yyyyMM", -1);
		}
		List<Integer> bed1List = qt.getBed_1();
		onebedroomQuantity = bed1List.get(bed1List.size()-1);
		List<Integer> bed2List = qt.getBed_2();
		twobedroomQuantity = bed2List.get(bed2List.size()-1);
		List<Integer> bed3List = qt.getBed_3();
		threebedroomQuantity = bed3List.get(bed3List.size()-1);
		List<Integer> otherList = qt.getOther();
		otherQuantity = otherList.get(otherList.size()-1);
		List<Integer> totalList = qt.getTotal();
		totalQuantity = totalList.get(totalList.size()-1);
	}
	private long crawltime;
	private String monthTime;
	private int month;
	private int onebedroomQuantity;
	private int twobedroomQuantity;
	private int threebedroomQuantity;
	private int otherQuantity;
	private int totalQuantity;
}
