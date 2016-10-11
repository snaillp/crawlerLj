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
import entity.HousePriceMonthStatDbEntity;
import entity.HouseQuantityMonthStatDbEntity;
import entity.HouseRequestEntity;
import entity.HouseStat4CrawlEntity;
import entity.HouseStatDayDbDataEntity;
import entity.HouseStatRequestEntity;
import entity.PriceEntity;
import entity.PriceTrend4Crawl;
import entity.ServerConfEntity;
import entity.SupplyDemandTrend4Crawl;

public class HouseStatCrawler {
private HttpClientHelper httpclient = new HttpClientHelper();
	
	public void crawl()
	{
//		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
//		CommonMongoDao dao = new FangyuanDao();
//		dao.init(serverConfEntity);
//		Pipeline pipeline = new MongoAppendfieldPipeLine(dao);
		String url = "http://m.api.lianjia.com/house/mfangjia/search?city_id=110000&quanpin=&community_id=&query=&access_token=&utm_source=&device_id=73484007dc0c59d482d901a6cd221951";
		try {
			String content = httpclient.doGet_String(url);
			HouseStatRequestEntity hse = new Gson().fromJson(content, HouseStatRequestEntity.class);
			HouseStatRequestEntity.RequestDataEntity rde = hse.getData();
			//按天数据
			HouseStat4CrawlEntity hs4ce = rde.getCard();
			HouseStatDayDbDataEntity hsdde = new HouseStatDayDbDataEntity(hs4ce);
			System.out.println(hsdde.toJson());
			//按月数据
			boolean isFirstCrawl = true;
			//月价格
			PriceTrend4Crawl  pt4c = rde.getPrice_trend();
			entity.PriceTrend4Crawl.CurrentLevel curStatInfo = pt4c.getCurrentLevel();
			if(null == curStatInfo){
				System.out.println("curlevel got failed");
				System.exit(1);
			}
			HousePriceMonthStatDbEntity hpmsde = new HousePriceMonthStatDbEntity(curStatInfo);
			System.out.println(hpmsde.toJson());
			//月成交量
			HouseQuantityMonthStatDbEntity hqmsde = new HouseQuantityMonthStatDbEntity(curStatInfo);
			System.out.println(hqmsde.toJson());
			//供需关系
			SupplyDemandTrend4Crawl sdt4c = rde.getSupply_demand_trend();
			//月供需关系
			
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
