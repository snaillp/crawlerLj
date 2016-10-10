package crawler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.util.List;

import util.HttpClientHelper;
import util.TimeUtil;

import com.google.gson.Gson;

import Pipeline.MongoAppendfieldPipeLine;
import config.ConfParse;
import dao.CommonMongoDao;
import dao.FangyuanDao;
import entity.FangyuanEntity;
import entity.FangyuanHistEntity;
import entity.HouseEntity;
import entity.HouseRequestEntity;
import entity.PriceEntity;
import entity.ServerConfEntity;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.pipeline.Pipeline;

public class DataapiCrawler {
	private HttpClientHelper httpclient = new HttpClientHelper();
	
	public void crawl()
	{
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		CommonMongoDao dao = new FangyuanDao();
		dao.init(serverConfEntity);
		Pipeline pipeline = new MongoAppendfieldPipeLine(dao);
		String url = "http://m.api.lianjia.com/house/ershoufang/search?channel=ershoufang&city_id=110000&is_suggestion=0&limit_count=50&limit_offset=1";
		try {
			String content = httpclient.doGet_String(url);
			HouseRequestEntity hre = new Gson().fromJson(content, HouseRequestEntity.class);
//			System.out.println(hre.getRequest_id());
			List<HouseEntity> houseList = hre.getData().getList();
			for(HouseEntity he: houseList){
				System.out.println(he.getHouse_code()+" "+he.getSign_price()+" "+he.getSign_unit_price());
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
				ResultItems resultItems = new ResultItems();
				resultItems.put("houseinfo", fhe);
				pipeline.process(resultItems, null);
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
		DataapiCrawler dc = new DataapiCrawler();
		dc.crawl();
	}
}
