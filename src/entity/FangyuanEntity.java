package entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class FangyuanEntity extends CommonEntity{
	private String fangyuanId;
	private PriceEntity price;
	private PriceEntity unitprice;
	public String getFangyuanId() {
		return fangyuanId;
	}
	public void setFangyuanId(String fangyuanId) {
		this.fangyuanId = fangyuanId;
	}
	public PriceEntity getPrice() {
		return price;
	}
	public void setPrice(PriceEntity price) {
		this.price = price;
	}
	public PriceEntity getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(PriceEntity unitprice) {
		this.unitprice = unitprice;
	}
	public FangyuanEntity(String fangyuanId, PriceEntity price,
			PriceEntity unitprice) {
		super();
		this.fangyuanId = fangyuanId;
		this.price = price;
		this.unitprice = unitprice;
	}
	public FangyuanEntity(){
		
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
}
