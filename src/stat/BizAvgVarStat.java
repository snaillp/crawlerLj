package stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import statentity.BizStat;
import config.ConfParse;
import dao.CommonMongoDao;
import dao.FangyuanDao;
import entity.FangyuanHistEntity;
import entity.PriceEntity;
import entity.ServerConfEntity;

public class BizAvgVarStat {
//按商圈统计均价和方差
	public void comAvgVar(String city, int level, String sellstat){
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		CommonMongoDao dao = new FangyuanDao();
		dao.init(serverConfEntity);
		String cond = "{'dealstat':'{sellstat}', 'city':'{city}'}".replace("{city}", city).replace("{sellstat}", sellstat);
		List<Object> fangyuanList = dao.find(cond, FangyuanHistEntity.class);
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
				mapkey = fangHist.getBizcircle_id(); //商圈id
			}
			List<PriceEntity> houseUnitprice = fangHist.getUnitpriceList();
			if(bizStatMap.containsKey(mapkey)){
				bizStatMap.get(mapkey).addHouseUnitprice(houseUnitprice.get(houseUnitprice.size()-1).getPrice());
			}else{
				String bizname = fangHist.getBizcircle_name();
//				String district = fangHist.getDistrict();
				BizStat bstat = new BizStat();
				bstat.setBizname(bizname);
				bstat.setDistrict(district);
				if(level == 0){
					//小区
					String xiaoqu = fangHist.getCommunity_name();
					bstat.setXiaoqu(xiaoqu);
				}
				bstat.addHouseUnitprice(houseUnitprice.get(houseUnitprice.size()-1).getPrice());
				bizStatMap.put(mapkey, bstat);
			}
		}
		if(bizStatMap.isEmpty()){
			return;
		}
		List<Map.Entry<String, BizStat>> bizList = sortMapByValue(bizStatMap, 1);
		for(Map.Entry<String, BizStat> bizEntity: bizList){
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
			bstat.setAverage(avg);
			bstat.setVariance(var);
			bstat.setHousenum(unitpriceList.size());
			System.out.println(bstat.toJson());
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
		System.out.println("商圈统计：sell");
		bavs.comAvgVar("bj", 1, "sell");
//		System.out.println("商圈统计：sold");
//		bavs.comAvgVar("bj", 1, "sold");
		//小区统计
		System.out.println("小区统计：sell");
		bavs.comAvgVar("bj", 0, "sell");
//		System.out.println("小区统计：sold");
//		bavs.comAvgVar("bj", 0, "sold");
	}
}
