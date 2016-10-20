package entity;

import java.util.Map;

import com.google.gson.Gson;

import util.TimeUtil;

public class HouseStatDayDbDataEntity extends CommonEntity{
	final HouseDataType dataType = HouseDataType.HouseDayStat;
	private long crawltime;
	private String statdate;
	private double ratio; //昨日新增客房比(新增购房者与售房者的比值（供需比）该指标一定程度反应供需双方对当前市场的评判和对后市的预期）
	private double momRatio; //相比昨日的增加幅度
	private int showAmount; //昨日房源带看量/次
	private double momShow; //带看量的增幅
	private int transAmount; //昨日成交量/套
	private double momQuantity; //成交量增幅
	private int houseAmount; //新增房源
	private double momHouse; //新增房源增幅
	private int totalAmount;  //截止到爬取，总的在售房源数
	private int totalDealAmount; //截止到爬取，总的成交房源数（成交房源展示会有2周的延迟）
	
	public HouseStatDayDbDataEntity(){
		
	}
	public HouseStatDayDbDataEntity(HouseStat4CrawlEntity hsce)
	{
		crawltime = TimeUtil.getCurSecond();
		statdate = TimeUtil.getDayInc("yyyyMMdd", -1);
		this.ratio = hsce.getRatio();
		momRatio = hsce.getMomRatio();
		showAmount = hsce.getShowAmount();
		momShow = hsce.getMomShow();
		transAmount = hsce.getTransAmount();
		momQuantity = hsce.getMomQuantity();
		houseAmount = hsce.getHouseAmount();
		momHouse = hsce.getMomHouse();
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
	public double getMomQuantity() {
		return momQuantity;
	}
	public void setMomQuantity(double momQuantity) {
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
	public int getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	public int getTotalDealAmount() {
		return totalDealAmount;
	}
	public void setTotalDealAmount(int totalDealAmount) {
		this.totalDealAmount = totalDealAmount;
	}
}
