package crawler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.pipeline.Pipeline;
import util.HttpClientHelper;
import util.TimeUtil;
import Pipeline.MongoAppendfieldPipeLine;

import com.google.gson.Gson;

import config.ConfParse;
import dao.CommonMongoDao;
import dao.FangyuanDao;
import entity.FangyuanHistEntity;
import entity.HouseEntity;
import entity.HouseRequestEntity;
import entity.PriceEntity;
import entity.ServerConfEntity;

public class HouseStatCrawler {
private HttpClientHelper httpclient = new HttpClientHelper();
	
	public void crawl()
	{
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		CommonMongoDao dao = new FangyuanDao();
		dao.init(serverConfEntity);
		Pipeline pipeline = new MongoAppendfieldPipeLine(dao);
		String url = "http://m.api.lianjia.com/house/mfangjia/search?city_id=110000&quanpin=&community_id=&query=&access_token=&utm_source=&device_id=73484007dc0c59d482d901a6cd221951";
		try {
			String content = httpclient.doGet_String(url);
			HouseRequestEntity hre = new Gson().fromJson(content, HouseRequestEntity.class);
//			System.out.println(hre.getRequest_id());
			List<HouseEntity> houseList = hre.getData().getList();
			for(HouseEntity he: houseList){
//					System.out.println(he.getHouse_code()+" "+he.getSign_price()+" "+he.getSign_unit_price());
				String fangyuanId = he.getHouse_code();
				int price = he.getSign_price();
				int unitPrice = he.getSign_unit_price();
				String curDate = TimeUtil.getCurrentDate("yyyyMMdd");
				PriceEntity priceEntity = new PriceEntity(price, curDate);
				PriceEntity unitpriceEntity = new PriceEntity(unitPrice, curDate);
				FangyuanHistEntity fhe = new FangyuanHistEntity();
				fhe.setFangyuanId(fangyuanId);
				fhe.addPrice(priceEntity);
				fhe.addUnitPrice(unitpriceEntity);
				fhe.setIsDeal(0);
				ResultItems resultItems = new ResultItems();
				resultItems.put("houseinfo", fhe);
				pipeline.process(resultItems, null);
//					fangyuanIdSet.add(he.getHouse_code());
			}
			int hasMoreData = hre.getData().getHas_more_data();
			if(hasMoreData != 1){
				break;
			}
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		HouseStatCrawler dc = new HouseStatCrawler();
		dc.crawl();
	}
}
