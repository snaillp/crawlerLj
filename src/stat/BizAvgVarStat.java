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
	public void comAvgVar(String city, int level){
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		CommonMongoDao dao = new FangyuanDao();
		dao.init(serverConfEntity);
		String cond = "{'dealstat':'sell', 'city':'{city}'}".replace("{city}", city);
		List<Object> fangyuanList = dao.find(cond, FangyuanHistEntity.class);
		Map<String, BizStat> bizStatMap = new HashMap();
		for(Object fangyuanOb: fangyuanList){
			FangyuanHistEntity fangHist = (FangyuanHistEntity)fangyuanOb;
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
				String district = fangHist.getDistrict();
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
			bstat.setHousenum(unitpriceList.size());
			System.out.println(bstat.toJson());
		}
	}
	public static void main(String[] args)
	{
		//商圈统计
		System.out.println("商圈统计：");
		new BizAvgVarStat().comAvgVar("bj", 1);
		//小区统计
		System.out.println("小区统计：");
		new BizAvgVarStat().comAvgVar("bj", 0);
	}
}
