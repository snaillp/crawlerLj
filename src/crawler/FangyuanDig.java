package crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.ConfParse;
import dao.CommonMongoDao;
import dao.FangyuanDao;
import entity.FangyuanHistEntity;
import entity.PriceEntity;
import entity.ServerConfEntity;

public class FangyuanDig {
	
	private static <K, V> List<Map.Entry<K, V>> sortMapByValue(Map<K, V> map,
			final int sort) {
		List<Map.Entry<K, V>> orderList = new ArrayList<Map.Entry<K, V>>(
				map.entrySet());
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
	
	public void findHousePricecut(){
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		CommonMongoDao dao = new FangyuanDao();
		dao.init(serverConfEntity);
		List<Object> fangyuanList = dao.find("{'dealstat' : 'sell'}", FangyuanHistEntity.class);
		Map<FangyuanHistEntity, Integer> cutMap = new HashMap();
		for(Object fangyuanOb: fangyuanList){
			FangyuanHistEntity fangHist = (FangyuanHistEntity)fangyuanOb;
			List<PriceEntity> histPriceList = fangHist.getPriceList();
			PriceEntity lastPrice = histPriceList.get(histPriceList.size()-1);
			int pricediff = 0;
			for(PriceEntity pe: histPriceList){
				if(pe.compareto(lastPrice) > 0){
					int curdiff = pe.getPrice() - lastPrice.getPrice();
					if(curdiff > pricediff){
						pricediff = curdiff;
					}
				}
			}
			cutMap.put(fangHist, pricediff);
		}
		List<Map.Entry<FangyuanHistEntity, Integer>> cutList = sortMapByValue(cutMap, -1);
		for(Map.Entry<FangyuanHistEntity, Integer> cutHist: cutList){
			System.out.println(cutHist.getValue()+" "+cutHist.getKey().toJson());
		}
	}
	
	public static void main(String[] args)
	{
		FangyuanDig fd = new FangyuanDig();
		fd.findHousePricecut();
	}
}
