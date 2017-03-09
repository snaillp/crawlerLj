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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
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
	private Map<String, String> cityidmapping = new HashMap() {{
		put("110000", "bj");
		put("120000", "tj");
	}};
	private Map<String, String> localMap;
	
	public void run(String[] args)
	{
		getLocal();
		String runningType = "all";
		String city = "all";
		if(args.length != 2){
			System.out.println("Usage error");
		}else{
			runningType = args[0].toLowerCase();
			city = args[1].toLowerCase();
		}
		List<String> cityidList = new ArrayList(10);

		if(city.equals("all")){
			cityidList.add("110000");
			cityidList.add("120000");
		}else if(city.equals("bj")){
			cityidList.add("110000");
		}else if(city.equals("tj")){
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
		for(String cityid: cityidList){
			for(String housestat: housestatList){
				crawl(cityid, housestat);
			}
		}
	}
	public void getLocal(){
		this.localMap = new HashMap();
		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(new FileInputStream("local.txt"), "utf-8"));
			String line;
			while((line = br.readLine()) != null){
				String[] lineArray = line.trim().split("\\s+");
				if(lineArray.length != 2){
					continue;
				}
				localMap.put(lineArray[0], lineArray[1]);
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void crawl(String cityid, String housestat)
	{
		String cityname = cityidmapping.get(cityid);
		String url = baseurl.replace("{cityid}", cityid).replace("{housestat}", housestat);
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		CommonMongoDao dao = new FangyuanDao();
		dao.init(serverConfEntity);
		Pipeline pipeline = new MongoAppendfieldPipeLine(dao);
		int singleCount = 100;
		url = url.replace("{count}", String.valueOf(singleCount));
		logger.info("begin to crawl "+url);
		String timestampfile = "./soldhousetimestamp."+cityname;
		String lastTimestamp = null;
		String curTimestamp = null;
		String toRecordTimestamp = null;
		boolean stopFlag = false;
		Random random = new Random();
		Map<String, String> xiaoquMap = getXiaoquMap(cityname);
		if("chengjiao".equals(housestat)){
			lastTimestamp = getTimestap(timestampfile);
			logger.info("get timestamp "+lastTimestamp+" from "+timestampfile);
		}
		try {
			int totalHouseNum = 0;
			for(int offset=0; offset<Integer.MAX_VALUE; ++offset){
				int curOffset = offset*singleCount;
				String housedataUrl = url+curOffset;
				logger.info("crawl offset:"+curOffset);
				String content = httpclient.doGet_String(housedataUrl);
				HouseRequestEntity hre = new Gson().fromJson(content, HouseRequestEntity.class);
				List<HouseEntity> houseList = hre.getData().getList();
				totalHouseNum += houseList.size();
				for(HouseEntity he: houseList){
					FangyuanHistEntity fhe = new FangyuanHistEntity(he);
					fhe.setCity(cityname);
					String xiaoquId = he.getCommunity_id();
					String xiaoquName = he.getCommunity_name();
					if("ershoufang".equals(housestat)){
						if(xiaoquId != null && xiaoquName != null && !xiaoquMap.containsKey(xiaoquId)){
							xiaoquMap.put(xiaoquId, xiaoquName);
						}
					}else{
						if(xiaoquName == null && xiaoquId !=null && xiaoquMap.containsKey(xiaoquId)){
							fhe.setCommunity_name(xiaoquMap.get(xiaoquId));
						}
					}
					if(null == he.getBizcircle_name()){
						String bizcircle_id = he.getBizcircle_id();
						if(this.localMap.containsKey(bizcircle_id)){
							fhe.setBizcircle_name(this.localMap.get(bizcircle_id));
						}
					}
					String district_id = he.getDistrict_id();
					if(this.localMap.containsKey(district_id)){
						fhe.setDistrict(this.localMap.get(district_id));
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
					logger.info("has more data:"+hasMoreData+", or stopflag:"+stopFlag+", total update size:"+totalHouseNum);
					break;
				}
//				int sleepno = random.nextInt(100);
//				try {
//					Thread.sleep(sleepno);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			if(null != toRecordTimestamp){
				toRecordTimestamp = TimeUtil.addDay("yyyy-MM-dd", toRecordTimestamp, -5);
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
		if("chengjiao".contains(housestat) && toRecordTimestamp!=null){
			logger.info("write "+toRecordTimestamp+" to "+timestampfile);
			setTimestap(timestampfile, toRecordTimestamp);
		}
		if("ershoufang".equals(housestat)){
			writeXiaoquMap(xiaoquMap, cityname);
		}
		logger.info("crawl done");
	}
	public Map<String, String> getXiaoquMap(String city)
	{
		Map<String, String> xiaoquMap = new HashMap();
		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader(new FileInputStream("xiaoqu."+city), "utf-8"));
			String line;
			while((line = br.readLine()) != null){
				if(line.startsWith("#")){
					continue;
				}
				String[] lineArray = line.trim().split("\t");
				if(lineArray.length != 2){
					continue;
				}
				xiaoquMap.put(lineArray[0], lineArray[1]);
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("get "+xiaoquMap.size()+" xiaoqu map");
		return xiaoquMap;
	}
	private void writeXiaoquMap(Map<String, String> xiaoquMap, String city)
	{
		try {
			Writer writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream("xiaoqu."+city, false)));
			for(Map.Entry<String, String> xiaoquEntity: xiaoquMap.entrySet()){
				writer.write(xiaoquEntity.getKey()+"\t"+xiaoquEntity.getValue()+"\n");
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("write "+xiaoquMap.size()+" xiaoqu");
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
		DataapiCrawler dc = new DataapiCrawler();
		dc.run(args);
	}
}
