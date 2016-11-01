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
	
	public void findHousePricecut(String city){
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		CommonMongoDao dao = new FangyuanDao();
		dao.init(serverConfEntity);
		String cond = "{'dealstat':'sell', 'city':'{city}'}".replace("{city}", city);
		List<Object> fangyuanList = dao.find(cond, FangyuanHistEntity.class);
		Map<FangyuanHistEntity, Integer> cutMap = new HashMap();
		Map<FangyuanHistEntity, Double> ratiocutMap = new HashMap();
		Map<FangyuanHistEntity, Integer> unitcutMap = new HashMap();
		Map<FangyuanHistEntity, Double> unitratiocutMap = new HashMap();
		for(Object fangyuanOb: fangyuanList){
			FangyuanHistEntity fangHist = (FangyuanHistEntity)fangyuanOb;
			//price
			List<PriceEntity> histPriceList = fangHist.getPriceList();
			PriceEntity lastPrice = histPriceList.get(histPriceList.size()-1);
			int pricediff = 0;
			double cutratio = 0.0;
			for(PriceEntity pe: histPriceList){
				if(pe.compareto(lastPrice) > 0){
					int curdiff = pe.getPrice() - lastPrice.getPrice();
					double curRatiocut = 0;
					if(pe.getPrice() != 0){
						curRatiocut = ((double)curdiff)/pe.getPrice();
					}
					if(curdiff > pricediff){
						pricediff = curdiff;
					}
					if(curRatiocut > cutratio){
						cutratio = curRatiocut;
					}
				}
			}
			if(pricediff != 0){
				cutMap.put(fangHist, pricediff);
				ratiocutMap.put(fangHist, cutratio);
			}
			//unit price
			List<PriceEntity> histUnitpriceList = fangHist.getUnitpriceList();
			PriceEntity lastUnitprice = histUnitpriceList.get(histUnitpriceList.size()-1);
			int unitpriceCut = 0;
			double unitcutratio = 0.0;
			for(PriceEntity pe: histUnitpriceList){
				if(pe.compareto(lastUnitprice) > 0){
					int curUnitcut = pe.getPrice() - lastUnitprice.getPrice();
					double curUnitRatiocut = 0;
					if(pe.getPrice() != 0){
						curUnitRatiocut = ((double)curUnitcut)/pe.getPrice();
					}
					if(curUnitcut > unitpriceCut){
						unitpriceCut = curUnitcut;
					}
					if(curUnitRatiocut > unitcutratio){
						unitcutratio = curUnitRatiocut;
					}
				}
			}
			if(unitpriceCut != 0){
				unitcutMap.put(fangHist, unitpriceCut);
				unitratiocutMap.put(fangHist, unitcutratio);
			}
		}
		List<Map.Entry<FangyuanHistEntity, Integer>> cutList = sortMapByValue(cutMap, -1);
		System.out.println("Price cut sort:");
		for(Map.Entry<FangyuanHistEntity, Integer> cutHist: cutList){
			System.out.println(cutHist.getValue()+" "+cutHist.getKey().toJson());
		}
		List<Map.Entry<FangyuanHistEntity, Double>> ratiocutList = sortMapByValue(ratiocutMap, -1);
		System.out.println("Ratio cut sort:");
		for(Map.Entry<FangyuanHistEntity, Double> cutHist: ratiocutList){
			System.out.println(cutHist.getValue()+" "+cutHist.getKey().toJson());
		}
		//unit price
		List<Map.Entry<FangyuanHistEntity, Integer>> unitcutList = sortMapByValue(unitcutMap, -1);
		System.out.println("Unitprice cut sort");
		for(Map.Entry<FangyuanHistEntity, Integer> cutHist: unitcutList){
			System.out.println(cutHist.getValue()+" "+cutHist.getKey().toJson());
		}
		List<Map.Entry<FangyuanHistEntity, Double>> unitcutratiotList = sortMapByValue(unitratiocutMap, -1);
		System.out.println("Unit ratio cut sort:");
		for(Map.Entry<FangyuanHistEntity, Double> cutHist: unitcutratiotList){
			System.out.println(cutHist.getValue()+" "+cutHist.getKey().toJson());
		}
	}
	
	public static void main(String[] args)
	{
		if(args.length != 1){
			System.out.println("city should be given");
			System.exit(1);
		}
		String city = args[0];
		FangyuanDig fd = new FangyuanDig();
		fd.findHousePricecut(city);
	}
}
