package stat;

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
	public void comAvgVar(String city){
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		CommonMongoDao dao = new FangyuanDao();
		dao.init(serverConfEntity);
		String cond = "{'dealstat':'sell', 'city':'{city}'}".replace("{city}", city);
		List<Object> fangyuanList = dao.find(cond, FangyuanHistEntity.class);
		Map<String, BizStat> bizStatMap = new HashMap();
		for(Object fangyuanOb: fangyuanList){
			FangyuanHistEntity fangHist = (FangyuanHistEntity)fangyuanOb;
			String bizname = fangHist.getBizcircle_name();
			String district = fangHist.getDistrict();
			String bizId = fangHist.getBizcircle_id();
			List<PriceEntity> houseUnitprice = fangHist.getUnitpriceList();
			if(bizStatMap.containsKey(bizId)){
				bizStatMap.get(bizId).addHouseUnitprice(houseUnitprice.get(houseUnitprice.size()-1).getPrice());
			}else{
				BizStat bstat = new BizStat();
				bstat.setBizname(bizname);
				bstat.addHouseUnitprice(houseUnitprice.get(houseUnitprice.size()-1).getPrice());
				bizStatMap.put(bizId, bstat);
			}
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
			bstat.setAverage(avg);
			bstat.setVariance(var);
			System.out.println(bstat.toJson());
		}
	}
	public static void main(String[] args)
	{
		new BizAvgVarStat().comAvgVar("bj");
	}
}
