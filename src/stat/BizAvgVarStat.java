package stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Pipeline.MongoAppendfieldPipeLine;
import statentity.BizStat;
import statentity.BizStatData;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.pipeline.Pipeline;
import util.TimeUtil;
import config.ConfParse;
import dao.BizAvgStatDao;
import dao.CommonMongoDao;
import dao.FangyuanDao;
import entity.FangyuanHistEntity;
import entity.PriceEntity;
import entity.ServerConfEntity;

public class BizAvgVarStat {
//按商圈统计均价和方差
	public void comAvgVar(String city, int level, String sellstat){
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		CommonMongoDao bizDao = new BizAvgStatDao();
		bizDao.init(serverConfEntity);
		Pipeline pipeline = new MongoAppendfieldPipeLine(bizDao);
		CommonMongoDao fangyuandao = new FangyuanDao();
		fangyuandao.init(serverConfEntity);
		String cond = "{'dealstat':'{sellstat}', 'city':'{city}'}".replace("{city}", city).replace("{sellstat}", sellstat);
		List<Object> fangyuanList = fangyuandao.find(cond, FangyuanHistEntity.class);
		Map<String, BizStat> bizStatMap = new HashMap();
		for(Object fangyuanOb: fangyuanList){
			FangyuanHistEntity fangHist = (FangyuanHistEntity)fangyuanOb;
			String district = fangHist.getDistrict();
			if(null == district){
				continue;
			}
			String mapkey = null;
			if(level == 0){
				mapkey = fangHist.getCommunity_name(); //小区名
			}else if(level == 1){
				mapkey = fangHist.getBizcircle_name(); //商圈id
			}
			List<PriceEntity> houseUnitprice = fangHist.getUnitpriceList();
			if(bizStatMap.containsKey(mapkey)){
				bizStatMap.get(mapkey).addHouseUnitprice(houseUnitprice.get(houseUnitprice.size()-1).getPrice());
			}else{
				String bizname = fangHist.getBizcircle_name();
//				String district = fangHist.getDistrict();
				BizStat bstat = new BizStat();
				bstat.setCity(city);
				bstat.setSellstat(sellstat);
				
				String bizId = fangHist.getBizcircle_id();
				if(bizId != null){
					bstat.setBizId(bizId);
				}
				if(bizname != null){
					bstat.setBizname(bizname);
				}
				String districtId = fangHist.getDistrict_id();
				if(districtId != null){
					bstat.setDistrictId(districtId);
				}
				bstat.setDistrict(district);
				if(level == 0){
					//小区
					String xiaoquId = fangHist.getCommunity_id();
					if(xiaoquId != null){
						bstat.setXiaoquId(xiaoquId);
					}
					String xiaoqu = fangHist.getCommunity_name();
					if(xiaoqu != null){
						bstat.setXiaoqu(xiaoqu);
					}
				}
				bstat.addHouseUnitprice(houseUnitprice.get(houseUnitprice.size()-1).getPrice());
				bizStatMap.put(mapkey, bstat);
			}
		}
		if(bizStatMap.isEmpty()){
			return;
		}
		for(Map.Entry<String, BizStat> bizEntity: bizStatMap.entrySet()){
			BizStat bstat = bizEntity.getValue();
			List<Integer> unitpriceList = bstat.getHouseUnitpriceList();
			double avg = 0.0;
			for(int uprice: unitpriceList){
				avg += uprice;
			}
			avg /= unitpriceList.size();
			double var = 0.0;
			for(int uprice: unitpriceList){
				double diff = uprice - avg;
				var += diff*diff;
			}
			var /= unitpriceList.size();
			var = Math.sqrt(var);
			BizStatData bsd = new BizStatData();
			String date = TimeUtil.getCurrentDate();
			bsd.setDate(date);
			long comtime = TimeUtil.getCurSecond();
			bsd.setComtime(comtime);
			bsd.setAverage(avg);
			bsd.setVariance(var);
			bsd.setHousenum(unitpriceList.size());
			bstat.addDataList(bsd);
			bstat.getLatestAverage();
			bstat.getLatestHousenum();
//			System.out.println(bstat.toJson());
			ResultItems resultItems = new ResultItems();
			resultItems.put("bizstatinfo", bstat);
			pipeline.process(resultItems, null);
		}
		List<Map.Entry<String, BizStat>> bizList = sortMapByValue(bizStatMap, 1);
		for(Map.Entry<String, BizStat> bizEntity: bizList){
			System.out.println(bizEntity.getValue().toJson());
		}
	}
	private static <K, V> List<Map.Entry<K, V>> sortMapByValue(Map<K, V> map,
			final int sort) {
		List<Map.Entry<K, V>> orderList = new ArrayList<Map.Entry<K, V>>(
				map.entrySet());
//		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		Collections.sort(orderList, new Comparator<Map.Entry<K, V>>() {
			// @Override
			@SuppressWarnings("unchecked")
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (((Comparable<V>) o1.getValue()).compareTo(o2.getValue()))
						* sort;
			}
		});
		return orderList;
	}
	public static void main(String[] args)
	{
		//level=1(商圈)|0(小区), sellstat=sell|sold
		BizAvgVarStat bavs = new BizAvgVarStat();
		//商圈统计, 
//		System.out.println("商圈统计：sell");
//		bavs.comAvgVar("bj", 1, "sell");
//		System.out.println("商圈统计：sold");
//		bavs.comAvgVar("bj", 1, "sold");
		//小区统计
		System.out.println("小区统计：sell");
		bavs.comAvgVar("bj", 0, "sell");
//		System.out.println("小区统计：sold");
//		bavs.comAvgVar("bj", 0, "sold");
	}
}
