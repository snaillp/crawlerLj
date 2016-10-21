package crawler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
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
		int singleCount = 100;
		url = url.replace("{count}", String.valueOf(singleCount));
		logger.info("begin to crawl "+url);
		String lastTimestamp = null;
		String curTimestamp = null;
		String toRecordTimestamp = null;
		boolean stopFlag = false;
		if(url.contains("chengjiao")){
			lastTimestamp = getTimestap("./soldhousetimestamp");
		}
		try {
			for(int offset=0; offset<Integer.MAX_VALUE; ++offset){
				int curOffset = offset*singleCount;
				String housedataUrl = url+curOffset;
				logger.info("crawl offset:"+curOffset);
				String content = httpclient.doGet_String(housedataUrl);
				HouseRequestEntity hre = new Gson().fromJson(content, HouseRequestEntity.class);
				List<HouseEntity> houseList = hre.getData().getList();
				for(HouseEntity he: houseList){
					FangyuanHistEntity fhe = new FangyuanHistEntity(he);
					logger.info(fhe.toJson());
//					System.out.println(fhe.toJson());
					ResultItems resultItems = new ResultItems();
					resultItems.put("houseinfo", fhe);
					pipeline.process(resultItems, null);
					curTimestamp = fhe.getDealDate();
					if(lastTimestamp!=null && null != curTimestamp){
						if(null == toRecordTimestamp || TimeUtil.compareDate("yyyy-MM-dd", toRecordTimestamp, curTimestamp) < 0){
							toRecordTimestamp = curTimestamp;
						}
						if(TimeUtil.compareDate("yyyy-MM-dd", curTimestamp, lastTimestamp)<=0){
							stopFlag = true;
							break;
						}
					}
				}
				int hasMoreData = hre.getData().getHas_more_data();
				if(hasMoreData != 1 || stopFlag){
					break;
				}
			}
			if(null != toRecordTimestamp){
				toRecordTimestamp = TimeUtil.addDay("yyyy-MM-dd", toRecordTimestamp, -2);
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
		if(url.contains("chengjiao")){
			setTimestap("./soldhousetimestamp", toRecordTimestamp);
		}
		logger.info("crawl done");
	}
	public String getTimestap(String timestampfile)
	{
		
		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(timestampfile), "utf-8"));
			String line = br.readLine().trim();
			br.close();
			return line;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void setTimestap(String timestampfile, String timestamp)
	{
		try {
			Writer writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(timestampfile, false)));
			writer.write(timestamp);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
		String runningType = "all";
		if(args.length != 1){
			System.out.println("Usage error");
			
		}else{
			runningType = args[0].toLowerCase();
		}

		DataapiCrawler dc = new DataapiCrawler();
		String fangyuanurl = "http://m.api.lianjia.com/house/ershoufang/search?channel=ershoufang&city_id=110000&is_suggestion=0&limit_count={count}&limit_offset=";
		String chengjiaoUrl = "http://m.api.lianjia.com/house/chengjiao/search?channel=ershoufang&city_id=110000&is_suggestion=0&limit_count={count}&limit_offset=";
		if(runningType.equals("all") || runningType.equals("ershoufang")){
			dc.crawl(fangyuanurl);
		}
		if(runningType.equals("all") || runningType.equals("chengjiao")){
			dc.crawl(chengjiaoUrl);
		}
	}
}
