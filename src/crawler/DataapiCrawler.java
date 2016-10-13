package crawler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.pipeline.Pipeline;
import util.HttpClientHelper;
import util.LogUtil;
import util.TimeUtil;
import Pipeline.MongoAppendfieldPipeLine;
import Pipeline.MongoPipeLine;

import com.google.gson.Gson;

import config.ConfParse;
import dao.CommonMongoDao;
import dao.FangyuanDao;
import dao.HousestatDao;
import entity.FangyuanHistEntity;
import entity.HouseEntity;
import entity.HouseRequestEntity;
import entity.PriceEntity;
import entity.ServerConfEntity;

public class DataapiCrawler {
	private HttpClientHelper httpclient = new HttpClientHelper();
	private Logger logger = LogUtil.getLogger("fangyuanLog");
	
	public void crawl(String url)
	{
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		CommonMongoDao dao = new FangyuanDao();
		dao.init(serverConfEntity);
		Pipeline pipeline = new MongoAppendfieldPipeLine(dao);
//		String url = "http://m.api.lianjia.com/house/ershoufang/search?channel=ershoufang&city_id=110000&is_suggestion=0&limit_count={count}&limit_offset=";
		int singleCount = 100;
		url = url.replace("{count}", String.valueOf(singleCount));
		logger.info("begin to crawl "+url);
		try {
			for(int offset=0; offset<Integer.MAX_VALUE; ++offset){
				int curOffset = offset*singleCount;
				String housedataUrl = url+curOffset;
				logger.info("crawl offset:"+curOffset);
				String content = httpclient.doGet_String(housedataUrl);
				HouseRequestEntity hre = new Gson().fromJson(content, HouseRequestEntity.class);
	//			System.out.println(hre.getRequest_id());
				List<HouseEntity> houseList = hre.getData().getList();
				for(HouseEntity he: houseList){
					FangyuanHistEntity fhe = new FangyuanHistEntity(he);
					logger.info(fhe.toJson());
					ResultItems resultItems = new ResultItems();
					resultItems.put("houseinfo", fhe);
					pipeline.process(resultItems, null);
				}
				int hasMoreData = hre.getData().getHas_more_data();
				if(hasMoreData != 1){
					break;
				}
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
		logger.info("crawl done");
	}

	public static void main(String[] args)
	{
		DataapiCrawler dc = new DataapiCrawler();
		String fangyuanurl = "http://m.api.lianjia.com/house/ershoufang/search?channel=ershoufang&city_id=110000&is_suggestion=0&limit_count={count}&limit_offset=";
		dc.crawl(fangyuanurl);
		String chengjiaoUrl = "http://m.api.lianjia.com/house/chengjiao/search?channel=ershoufang&city_id=110000&is_suggestion=0&limit_count={count}&limit_offset=";
		dc.crawl(chengjiaoUrl);
	}
}
