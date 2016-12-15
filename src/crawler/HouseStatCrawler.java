package crawler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.log4j.Logger;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.pipeline.Pipeline;
import util.HttpClientHelper;
import util.LogUtil;
import util.TimeUtil;
import Pipeline.MongoPipeLine;

import com.google.gson.Gson;

import config.ConfParse;
import dao.CommonMongoDao;
import dao.HousestatDao;
import entity.DistrictDataDbEntity;
import entity.HousePriceMonthStatDbEntity;
import entity.HouseQuantityMonthStatDbEntity;
import entity.HouseRequestEntity;
import entity.HouseStat4CrawlEntity;
import entity.HouseStatDayDbDataEntity;
import entity.HouseStatRequestEntity;
import entity.PriceTrend4Crawl;
import entity.ServerConfEntity;
import entity.SupplyDemandDbEntity;
import entity.SupplyDemandTrend4Crawl;

public class HouseStatCrawler {
private HttpClientHelper httpclient = new HttpClientHelper();
private Logger logger = LogUtil.getLogger("housestatLog");
	
	public void crawl()
	{
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		CommonMongoDao dao = new HousestatDao();
		dao.init(serverConfEntity);
		Pipeline pipeline = new MongoPipeLine(dao);
		ResultItems resultItems = new ResultItems();
//		resultItems.put("houseinfo", fhe);
//		pipeline.process(resultItems, null);
		//供需关系按天统计
		String supplyDemandDayUrl = "http://bj.lianjia.com/fangjia/priceTrend/?analysis=1&duration=day";
		//供需关系按月统计
		String supplyDemandMonthUrl = "http://bj.lianjia.com/fangjia/priceTrend/?analysis=1";
		//价格趋势按月统计
		String priceTrendMonthUrl = "http://bj.lianjia.com/fangjia/priceTrend/";
		String url = "http://m.api.lianjia.com/house/mfangjia/search?city_id=110000&quanpin=&community_id=&query=&access_token=&utm_source=&device_id=73484007dc0c59d482d901a6cd221951";
		String housedataUrl = "http://m.api.lianjia.com/house/ershoufang/search?channel=ershoufang&city_id=110000&is_suggestion=0&limit_count=2&limit_offset=0";
		String chengjiaoDataUrl = "http://m.api.lianjia.com/house/chengjiao/search?channel=ershoufang&city_id=110000&is_suggestion=0&limit_count=2&limit_offset=0";
		String districtDataUrl = "http://bj.lianjia.com/fangjia/priceMap/";
		logger.info("begin to crawl");
		try {
			String content = httpclient.doGet_String(url);
			System.out.println(content);
			HouseStatRequestEntity hse = new Gson().fromJson(content, HouseStatRequestEntity.class);
			HouseStatRequestEntity.RequestDataEntity rde = hse.getData();
			//按天数据
			HouseStat4CrawlEntity hs4ce = rde.getCard();
			HouseStatDayDbDataEntity hsdde = new HouseStatDayDbDataEntity(hs4ce);
			//在售房源数
			String houseDataConteng = httpclient.doGet_String(housedataUrl);
			HouseRequestEntity hre = new Gson().fromJson(houseDataConteng, HouseRequestEntity.class);
			int totalHouseNum = hre.getData().getTotal_count();
			hsdde.setTotalAmount(totalHouseNum);
			//成交房源数
			String dealDataContent = httpclient.doGet_String(chengjiaoDataUrl);
			hre = new Gson().fromJson(dealDataContent, HouseRequestEntity.class);
			int dealHouseNum = hre.getData().getTotal_count();
			hsdde.setTotalDealAmount(dealHouseNum);
//			System.out.println(hsdde.toJson());
			logger.info(hsdde.toJson());
			resultItems.put("daydata", hsdde);
			//各个区的均价和成交
			String districtDataContent = httpclient.doGet_String(districtDataUrl);
			DistrictDataDbEntity ddde = new DistrictDataDbEntity(districtDataContent);
			logger.info(ddde.toJson());
			resultItems.put("districtdaydata", ddde);
			//按月数据
			//月价格
			PriceTrend4Crawl  pt4c = rde.getPrice_trend();
			entity.PriceTrend4Crawl.CurrentLevel curStatInfo = pt4c.getCurrentLevel();
			if(null == curStatInfo){
				System.out.println("curlevel got failed");
				System.exit(1);
			}
			if(monthNeedCrawl()){
				HousePriceMonthStatDbEntity hpmsde = new HousePriceMonthStatDbEntity(curStatInfo);
//				System.out.println(hpmsde.toJson());
				logger.info(hpmsde.toJson());
				resultItems.put("monthprice", hpmsde);
			}
			//月成交量
			if(monthNeedCrawl()){
				HouseQuantityMonthStatDbEntity hqmsde = new HouseQuantityMonthStatDbEntity(curStatInfo);
//				System.out.println(hqmsde.toJson());
				logger.info(hqmsde.toJson());
				resultItems.put("monthquantity", hqmsde);
			}
			//供需关系
			SupplyDemandTrend4Crawl sdt4c = rde.getSupply_demand_trend();
			//月供需关系
			if(monthNeedCrawl()){
				SupplyDemandDbEntity monthSdde = new SupplyDemandDbEntity(sdt4c.getMonth(), "month");
//				System.out.println(monthSdde.toJson());
				logger.info(monthSdde.toJson());
				resultItems.put("monthSd", monthSdde);
			}
			//日供需关系
			SupplyDemandDbEntity daySdde = new SupplyDemandDbEntity(sdt4c.getDay(), "day");
//			System.out.println(daySdde.toJson());
			logger.info(daySdde.toJson());
			resultItems.put("daySd", daySdde);
			pipeline.process(resultItems, null);
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
		logger.info("crawl done");
	}
	
	public boolean monthNeedCrawl()
	{
		int day = TimeUtil.getCurDay();
//		System.out.println(day);
		if(day == 2){
			return true;
		}
		return false;
	}

	public static void main(String[] args)
	{
		HouseStatCrawler hsc = new HouseStatCrawler();
		hsc.crawl();
//		System.out.println(dc.monthNeedCrawl());
	}
}
