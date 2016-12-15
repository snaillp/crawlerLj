package entity;

public class HouseStat4CrawlEntity {
	private String month;  //月份（上一月）
	private String monthTrans; //月参考价（成交均价？）
	private int tradeCount; //月成交量  ?
	private String dealMonthRatio; // ?
	private double ratio; //昨日新增客房比(新增购房者与售房者的比值（供需比）该指标一定程度反应供需双方对当前市场的评判和对后市的预期）
	private double momRatio; //相比昨日的增加幅度
	private int showAmount; //昨日房源带看量/次
	private double momShow; //带看量的增幅
	private int transAmount; //昨日成交量/套
	private String momQuantity; //成交量增幅
	private int houseAmount; //新增房源
	private double momHouse; //新增房源增幅
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getMonthTrans() {
		return monthTrans;
	}
	public void setMonthTrans(String monthTrans) {
		this.monthTrans = monthTrans;
	}
	public int getTradeCount() {
		return tradeCount;
	}
	public void setTradeCount(int tradeCount) {
		this.tradeCount = tradeCount;
	}
	public String getDealMonthRatio() {
		return dealMonthRatio;
	}
	public void setDealMonthRatio(String dealMonthRatio) {
		this.dealMonthRatio = dealMonthRatio;
	}
	public double getRatio() {
		return ratio;
	}
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	public double getMomRatio() {
		return momRatio;
	}
	public void setMomRatio(double momRatio) {
		this.momRatio = momRatio;
	}
	public int getShowAmount() {
		return showAmount;
	}
	public void setShowAmount(int showAmount) {
		this.showAmount = showAmount;
	}
	public double getMomShow() {
		return momShow;
	}
	public void setMomShow(double momShow) {
		this.momShow = momShow;
	}
	public int getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(int transAmount) {
		this.transAmount = transAmount;
	}
	public String getMomQuantity() {
		return momQuantity;
	}
	public void setMomQuantity(String momQuantity) {
		this.momQuantity = momQuantity;
	}
	public int getHouseAmount() {
		return houseAmount;
	}
	public void setHouseAmount(int houseAmount) {
		this.houseAmount = houseAmount;
	}
	public double getMomHouse() {
		return momHouse;
	}
	public void setMomHouse(double momHouse) {
		this.momHouse = momHouse;
	}

	
}
