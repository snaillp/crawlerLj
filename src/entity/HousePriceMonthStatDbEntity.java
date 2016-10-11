package entity;

import java.util.ArrayList;
import java.util.List;

import util.TimeUtil;


public class HousePriceMonthStatDbEntity extends CommonEntity{

	public HousePriceMonthStatDbEntity(PriceTrend4Crawl.CurrentLevel cur)
	{
		crawltime = TimeUtil.getCurSecond();
		listprice = new HousePriceStatDbEntity(cur.getListPrice());
		dealprice = new HousePriceStatDbEntity(cur.getDealPrice());
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
	}
	
	private long crawltime;
	private String monthTime;
	private int month;
	private HousePriceStatDbEntity listprice;
	private HousePriceStatDbEntity dealprice;
	
	public class HousePriceStatDbEntity{
		private double onebedroomPrice;
		private double twobedroomPrice;
		private double threebedroomPrice;
		private double otherPrice;
		private double totalPrice;

		public HousePriceStatDbEntity(PriceTrend4Crawl.CurrentLevel.Price pr) 
		{
			List<Double> bed1List = pr.getBed_1();
			onebedroomPrice = bed1List.get(bed1List.size()-1);
			List<Double> bed2List = pr.getBed_2();
			twobedroomPrice = bed2List.get(bed2List.size()-1);
			List<Double> bed3List = pr.getBed_3();
			threebedroomPrice = bed3List.get(bed3List.size()-1);
			List<Double> otherList = pr.getOther();
			otherPrice = otherList.get(otherList.size()-1);
			List<Double> totalList = pr.getTotal();
			totalPrice = totalList.get(totalList.size()-1);
		}
	}
	
	public static void main(String[] args)
	{
		List<Integer> aList = new ArrayList();
		aList.add(1);
		aList.add(2);
		System.out.println(aList.get(-1));
	}
}
