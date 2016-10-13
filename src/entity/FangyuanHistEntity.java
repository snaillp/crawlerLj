package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.TimeUtil;

import com.google.gson.Gson;

public class FangyuanHistEntity extends CommonEntity{
	public FangyuanHistEntity(HouseEntity he)
	{
		fangyuanId = he.getHouse_code();
		bedroomnum = he.getBlueprint_bedroom_num();
		hallnum = he.getBlueprint_hall_num();
		area = he.getArea();
		dealstat = he.getKv_house_type();
		dealDate = he.getSign_date();
		int price = he.getSign_price();
		int unitPrice = he.getSign_unit_price();
		String curDate = TimeUtil.getCurrentDate("yyyyMMdd");
		PriceEntity priceEntity = new PriceEntity(price, curDate);
		PriceEntity unitpriceEntity = new PriceEntity(unitPrice, curDate);
		addPrice(priceEntity);
		addUnitPrice(unitpriceEntity);
	}
	public FangyuanHistEntity()
	{
		
	}
	
	private final HouseDataType dataType = HouseDataType.FangyuanData;
	private String fangyuanId;
	private int bedroomnum;
	private int hallnum;
	private double area;
	private String dealstat;
	private String dealDate;
	private List<PriceEntity> priceList;
	private List<PriceEntity> unitpriceList;
	
	public String getFangyuanId() {
		return fangyuanId;
	}
	public int getBedroomnum() {
		return bedroomnum;
	}

	public void setBedroomnum(int bedroomnum) {
		this.bedroomnum = bedroomnum;
	}

	public int getHallnum() {
		return hallnum;
	}

	public void setHallnum(int hallnum) {
		this.hallnum = hallnum;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public String getDealstat() {
		return dealstat;
	}

	public void setDealstat(String dealstat) {
		this.dealstat = dealstat;
	}

	public HouseDataType getDataType() {
		return dataType;
	}

	public void setFangyuanId(String fangyuanId) {
		this.fangyuanId = fangyuanId;
	}
	public List<PriceEntity> getPriceList() {
		return priceList;
	}
	public void setPriceList(List<PriceEntity> priceList) {
		this.priceList = priceList;
	}
	public List<PriceEntity> getUnitpriceList() {
		return unitpriceList;
	}
	public void setUnitpriceList(List<PriceEntity> unitpriceList) {
		this.unitpriceList = unitpriceList;
	}
	public void addPrice(PriceEntity p)
	{
		if(priceList == null){
			priceList = new ArrayList();
		}
		priceList.add(p);
	}
	public void addUnitPrice(PriceEntity p)
	{
		if(unitpriceList == null){
			unitpriceList = new ArrayList();
		}
		unitpriceList.add(p);
	}
	/**
	 * 需要使用mongo update接口的需要重载此接口，返回更新条件
	 */
	@Override
	public String getJsonAppendCond()
	{
		Map<String, String> appendCondMap = new HashMap(1);
		//通用key
		appendCondMap.put("fangyuanId", fangyuanId);
		//库里字段满足条件
		return new Gson().toJson(appendCondMap);
	}
	
	private boolean compare(PriceEntity p1, PriceEntity p2)
	{
		if(p1.getPrice() != p2.getPrice()){
			return true;
		}
		return false;
	}
	@Override
	public boolean appendList(CommonEntity f)
	{
		FangyuanHistEntity ff = (FangyuanHistEntity)f;
		boolean appendFlag = false;
		//deal stat
		String dealStat = this.getDealstat();
		String f_dealStat = ff.getDealstat();
		//add price
		List<PriceEntity> peList = getPriceList();
		PriceEntity lastPrice = peList.get(peList.size()-1);
		List<PriceEntity> toAddPeList = ff.getPriceList();
		PriceEntity f_lastPrice = toAddPeList.get(toAddPeList.size()-1);
		if(!lastPrice.equals(f_lastPrice) || !dealStat.equals(f_dealStat)){
			toAddPeList.add(lastPrice);
			appendFlag = true;
		}
		this.setPriceList(toAddPeList);
		//add unitprice
		List<PriceEntity> upeList = getUnitpriceList();
		PriceEntity lastUprice = upeList.get(upeList.size()-1);
		List<PriceEntity> toAddUpeList = ff.getUnitpriceList();
		PriceEntity f_lastUprice = toAddUpeList.get(toAddUpeList.size()-1);
		if(!lastUprice.equals(f_lastUprice) || !dealStat.equals(f_dealStat)){
			toAddUpeList.add(lastUprice);
			appendFlag = true;
		}
		this.setUnitpriceList(toAddUpeList);
		return appendFlag;
	}

	
}
