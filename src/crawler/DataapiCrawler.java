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
import java.util.ArrayList;
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
	private String baseurl = "http://m.api.lianjia.com/house/{housestat}/search?channel=ershoufang&city_id={cityid}&is_suggestion=0&limit_count={count}&limit_offset=";
	
	public void run(String[] args)
	{
		String runningType = "all";
		String city = "all";
		if(args.length != 2){
			System.out.println("Usage error");
		}else{
			runningType = args[0].toLowerCase();
			city = args[1].toLowerCase();
		}
		List<String> cityidList = new ArrayList(10);

		if(runningType.equals("all")){
			cityidList.add("110000");
		}else if(runningType.equals("bj")){
			cityidList.add("110000");
			cityidList.add("120000");
		}else if(runningType.equals("tj")){
			cityidList.add("120000");
		}
		List<String> housestatList = new ArrayList(2);
		if(runningType.equals("all")){
			housestatList.add("ershoufang");
			housestatList.add("chengjiao");
		}else if(runningType.equals("ershoufang")){
			housestatList.add("ershoufang");
		}else if(runningType.equals("chengjiao")){
			housestatList.add("chengjiao");
		}
		DataapiCrawler dc = new DataapiCrawler();
		for(String cityid: cityidList){
			for(String housestat: housestatList){
				String url = baseurl.replace("{cityid}", cityid).replace("{housestat}", housestat);
				dc.crawl(url);
			}
		}
	}
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
					if(url.contains("110000")){
						fhe.setCity("bj");
					}else if(url.contains("120000")){
						fhe.setCity("tj");
					}
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
		
	}
}
