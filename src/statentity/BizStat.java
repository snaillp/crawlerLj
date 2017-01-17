package statentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import entity.CommonEntity;

public class BizStat extends CommonEntity implements Comparable<BizStat> {
	private String city;
	private String districtId;
	private String district;
	private String bizId;
	private String bizname;
	private String xiaoquId;
	private String xiaoqu;
	private String sellstat;
	private double latestAverage;
	private int latestHousenum;
	List<BizStatData> dataList;
	//用于统计当次均价时，记录所在小区所有房源的单价
	private List<Integer> houseUnitpriceList;
	
	public String getBizname() {
		return bizname;
	}
	public void setBizname(String bizname) {
		this.bizname = bizname;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getXiaoqu() {
		return xiaoqu;
	}
	public void setXiaoqu(String xiaoqu) {
		this.xiaoqu = xiaoqu;
	}
	public String getDistrictId() {
		return districtId;
	}
	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String biznameId) {
		this.bizId = biznameId;
	}
	public String getXiaoquId() {
		return xiaoquId;
	}
	public void setXiaoquId(String xiaoquId) {
		this.xiaoquId = xiaoquId;
	}
	public String getSellstat() {
		return sellstat;
	}
	public void setSellstat(String sellstat) {
		this.sellstat = sellstat;
	}
	public List<BizStatData> getDataList() {
		return dataList;
	}
	public void setDataList(List<BizStatData> dataList) {
		this.dataList = dataList;
	}
	public double getLatestAverage() {
		if(!this.dataList.isEmpty()){
			int size = this.dataList.size();
			return this.dataList.get(size-1).getAverage();
		}
		return 0;
	}
	public int getLatestHousenum() {
		if(!this.dataList.isEmpty()){
			int size = this.dataList.size();
			return this.dataList.get(size-1).getHousenum();
		}
		return 0;
	}
	public void addDataList(BizStatData bs){
		if(null == this.dataList){
			this.dataList = new ArrayList();
		}
		this.dataList.add(bs);
	}
	public List<Integer> getHouseUnitpriceList() {
		return houseUnitpriceList;
	}
	public void setHouseUnitpriceList(List<Integer> houseUnitpriceList) {
		this.houseUnitpriceList = houseUnitpriceList;
	}
	public void addHouseUnitprice(int price){
		if(null == houseUnitpriceList){
			houseUnitpriceList = new ArrayList();
		}
		houseUnitpriceList.add(price);
	}
	
	/**
	 * 需要使用mongo update接口的需要重载此接口，返回更新条件
	 */
	@Override
	public String getJsonAppendCond()
	{
		Map<String, String> appendCondMap = new HashMap(1);
		//通用key
		appendCondMap.put("city", city);
		if(districtId != null){
			appendCondMap.put("districtId", districtId);
		}
		if(bizId != null){
			appendCondMap.put("bizId", bizId);
		}
		if(xiaoquId != null){
			appendCondMap.put("xiaoquId", xiaoquId);
		}
		//库里字段满足条件
		return new Gson().toJson(appendCondMap);
	}
	@Override
	public boolean appendList(CommonEntity f)
	{
		BizStat bs = (BizStat)f;
		List<BizStatData> dataList = this.dataList;
		if(dataList == null || dataList.isEmpty()){
			return false;
		}
		BizStatData lastData = dataList.get(dataList.size()-1);
		List<BizStatData> oldDataList = bs.dataList;
		oldDataList.add(lastData);
		this.dataList = oldDataList;
		return true;
	}
	
	@Override
	public String toJson()
	{
		List<Integer> hList = this.houseUnitpriceList;
		this.houseUnitpriceList = null;
		String retStr = new Gson().toJson(this);
		this.houseUnitpriceList = hList;
		return retStr;
	}
	
	@Override
	public int compareTo(BizStat arg0) {
		//优先级：区->商圈->均价->房源数->小区名
		int ret = 0;
		if(this.district != null && arg0.district != null){
			ret = this.district.compareTo(arg0.district);
		}else if(this.district == null){
			ret = -1;
		}else if(arg0.district == null){
			return 1;
		}
		if(ret != 0){
			return ret;
		}
		if(this.bizname != null && arg0.bizname != null){
			ret = this.bizname.compareTo(arg0.bizname);
		}else if(this.bizname == null){
			return -1;
		}else if(arg0.bizname == null){
			return 1;
		}
		if(ret != 0){
			return ret;
		}
		int sizeSelf = this.dataList.size();
		double  averageSelf = this.dataList.get(sizeSelf-1).getAverage();
		int sizeArg = arg0.dataList.size();
		double averageArg = arg0.dataList.get(sizeArg-1).getAverage();
		if(averageSelf > averageArg){
			ret = 1;
		}else if(averageSelf < averageArg){
			ret = -1;
		}
		if(ret != 0){
			return ret;
		}
		int housenumSelf = this.dataList.get(sizeSelf-1).getHousenum();
		int housenumArg = arg0.dataList.get(sizeArg-1).getHousenum();
		if(housenumSelf > housenumArg){
			ret = 1;
		}else if(housenumSelf < housenumArg){
			ret = -1;
		}
		if(ret != 0){
			return ret;
		}
		if(this.xiaoqu != null && arg0.xiaoqu != null){
			ret = this.xiaoqu.compareTo(arg0.xiaoqu);
		}else if(this.xiaoqu == null){
			ret = -1;
		}else if(arg0.xiaoqu == null){
			ret = 1;
		}
		if(ret != 0){
			return ret;
		}
		return ret;
	}
	public static void main(String[] args){
		BizStat b1 = new Gson().fromJson("{\"district\":\"顺义\",\"bizname\":\"顺义城\",\"xiaoqu\":\"金汉绿港二区\",\"average\":40988.333333333336,\"variance\":719200.2222222224,\"housenum\":3.0}", BizStat.class);
		BizStat b2 = new Gson().fromJson("{'district':'顺义','bizname':'顺义城','xiaoqu':'金汉绿港五区','average':35958.27272727273,'variance':1544201.289256198,'housenum':11.0}", BizStat.class);
		System.out.println(b1.compareTo(b2));
	}
}
