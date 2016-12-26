package stat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import config.ConfParse;
import dao.CommonMongoDao;
import dao.FangyuanDao;
import entity.FangyuanHistEntity;
import entity.PriceEntity;
import entity.ServerConfEntity;
import statentity.BizStat;

public class HouseBlowAvg {
	private Map<String, BizStat> readAvg(String avgfile){
		Map<String, BizStat> avgMap = new HashMap();
		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(avgfile), "utf-8"));
			String line;
			Gson parser = new Gson();
			while ((line = br.readLine()) != null) {
				line = line.trim();
				BizStat xiaoquAvg = parser.fromJson(line, BizStat.class);
				avgMap.put(xiaoquAvg.getXiaoqu(), xiaoquAvg);
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return avgMap;
	}
	public void findHouseBlowAvg(String avgfile, String city, String sellstat)
	{
		Map<String, BizStat> avgMap = readAvg(avgfile);
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		CommonMongoDao dao = new FangyuanDao();
		dao.init(serverConfEntity);
		for(Map.Entry<String, BizStat> bizStat: avgMap.entrySet()){
			String xiaoqu = bizStat.getKey();
			double xiaoquAvg = bizStat.getValue().getAverage();
			String cond = "{'dealstat':'{sellstat}', 'city':'{city}', 'community_name':'{xiaoqu}'}".
					replace("{city}", city).replace("{sellstat}", sellstat).replace("{xiaoqu}", xiaoqu);
			List<Object> fangyuanList = dao.find(cond, FangyuanHistEntity.class);
			System.out.println(bizStat);
			for(Object fangyuanOb: fangyuanList){
				FangyuanHistEntity fangHist = (FangyuanHistEntity)fangyuanOb;
				List<PriceEntity> houseUnitprice = fangHist.getUnitpriceList();
				double unitprice = houseUnitprice.get(houseUnitprice.size()-1).getPrice();
				if(unitprice < xiaoquAvg){
					System.out.println(fangHist);
				}
			}
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String avgfile = args[0];
		new HouseBlowAvg().findHouseBlowAvg(avgfile, "bj", "sell");
	}

}
