package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class FangyuanHistEntity extends CommonEntity{
	private String fangyuanId;
	private List<PriceEntity> priceList;
	private List<PriceEntity> unitpriceList;
//	private double totalhousearea;
//	private double inhousearea;
//	public double getTotalhousearea() {
//		return totalhousearea;
//	}
//	public void setTotalhousearea(double totalhousearea) {
//		this.totalhousearea = totalhousearea;
//	}
//	public double getInhousearea() {
//		return inhousearea;
//	}
//	public void setInhousearea(double inhousearea) {
//		this.inhousearea = inhousearea;
//	}
	public String getFangyuanId() {
		return fangyuanId;
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
	@Override
	public boolean appendList(CommonEntity f)
	{
		FangyuanHistEntity ff = (FangyuanHistEntity)f;
		boolean appendFlag = false;
		//add price
		List<PriceEntity> peList = getPriceList();
		PriceEntity lastPrice = peList.get(peList.size()-1);
		List<PriceEntity> toAddPeList = ff.getPriceList();
		for(PriceEntity p: toAddPeList){
			if(lastPrice.getPrice() != p.getPrice()){
				peList.add(p);
				appendFlag = true;
			}
		}
		//add unitprice
		List<PriceEntity> upeList = getUnitpriceList();
		PriceEntity lastUprice = upeList.get(upeList.size()-1);
		List<PriceEntity> toAddUpeList = ff.getUnitpriceList();
		for(PriceEntity p: toAddUpeList){
			if(lastUprice.getPrice() != p.getPrice()){
				upeList.add(p);
				appendFlag = true;
			}
		}
		return appendFlag;
	}
	
}
