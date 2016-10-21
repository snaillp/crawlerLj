package crawler;

import java.util.List;

import config.ConfParse;
import dao.CommonMongoDao;
import dao.FangyuanDao;
import entity.FangyuanHistEntity;
import entity.PriceEntity;
import entity.ServerConfEntity;

public class FangyuanDig {
	
	public void findHousePricecut(){
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		CommonMongoDao dao = new FangyuanDao();
		dao.init(serverConfEntity);
		List<Object> fangyuanList = dao.find("{'dealstat' : 'sell'}", FangyuanHistEntity.class);
		for(Object fangyuanOb: fangyuanList){
			FangyuanHistEntity fangHist = (FangyuanHistEntity)fangyuanOb;
			List<PriceEntity> histPriceList = fangHist.getPriceList();
			PriceEntity lastPrice = histPriceList.get(histPriceList.size()-1);
			for(PriceEntity pe: histPriceList){
				if(pe.compareto(lastPrice) > 0){
					System.out.println(fangHist.toJson());
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		FangyuanDig fd = new FangyuanDig();
		fd.findHousePricecut();
	}
}
