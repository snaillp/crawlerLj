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
	private List<BizStat> readAvg(String avgfile){
		List<BizStat> avgList = new ArrayList();
		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(avgfile), "utf-8"));
			String line;
			Gson parser = new Gson();
			boolean beginFlag = false;
			while ((line = br.readLine()) != null) {
				if(line.startsWith("小区统计")){
					beginFlag = true;
					continue;
				}
				if(!beginFlag){
					continue;
				}
				line = line.trim();
				BizStat xiaoquAvg = parser.fromJson(line, BizStat.class);
				avgList.add(xiaoquAvg);
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return avgList;
	}
	public void findHouseBlowAvg(String avgfile, String city, String sellstat)
	{
		List<BizStat> avgList = readAvg(avgfile);
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		CommonMongoDao dao = new FangyuanDao();
		dao.init(serverConfEntity);
		for(BizStat bizStat: avgList){
			String xiaoqu = bizStat.getXiaoqu();
			double xiaoquAvg = bizStat.getLatestAverage();
			String cond = "{'dealstat':'{sellstat}', 'city':'{city}', 'community_name':'{xiaoqu}'}".
					replace("{city}", city).replace("{sellstat}", sellstat).replace("{xiaoqu}", xiaoqu);
			List<Object> fangyuanList = dao.find(cond, FangyuanHistEntity.class);
			System.out.println("xiaoqustat: "+bizStat.toJson());
			for(Object fangyuanOb: fangyuanList){
				FangyuanHistEntity fangHist = (FangyuanHistEntity)fangyuanOb;
				List<PriceEntity> houseUnitprice = fangHist.getUnitpriceList();
				List<PriceEntity> housePrice = fangHist.getPriceList();
				double price = housePrice.get(housePrice.size()-1).getPrice();
				if(price > 5000000){
					continue;
				}
				String district = fangHist.getDistrict();
				if(null != district && (district.equals("顺义") || district.equals("大兴") || district.equals("房山") || district.equals("门头沟")
						|| district.equals("通州") || district.equals("亦庄开发区") || district.equals("怀柔") || district.equals("延庆")
						 || district.equals("密云"))){
					continue;
				}
				double unitprice = houseUnitprice.get(houseUnitprice.size()-1).getPrice();
				if(unitprice <= xiaoquAvg){
					System.out.println(fangHist.toJson());
				}
			}
			System.out.println();
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String avgfile = args[0];
		new HouseBlowAvg().findHouseBlowAvg(avgfile, "bj", "sell");
	}

}
