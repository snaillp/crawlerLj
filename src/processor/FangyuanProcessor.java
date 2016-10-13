package processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import config.ConfParse;
import Pipeline.MongoAppendfieldPipeLine;
import Pipeline.MongoPipeLine;
import dao.CommonMongoDao;
import dao.FangyuanDao;
import entity.FangyuanEntity;
import entity.FangyuanHistEntity;
import entity.PriceEntity;
import entity.ServerConfEntity;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import util.TimeUtil;
import util.UrlUtil;

public class FangyuanProcessor extends ProcessorBaseClass{
	private String URL_BEGIN = "http://bj.lianjia.com/ershoufang/rs/";
	private String URL_LIST_REGEX = "http://bj\\.lianjia\\.com/ershoufang/pg\\d+/";
	private String URL_DETAIL_REGEX = "http://bj\\.lianjia\\.com/ershoufang/\\d+\\.html";
	
	private int ljTotalListNum = 100;
	private String city = "bj";
	private String ljListurl = "http://{city}.lianjia.com/ershoufang/pg{pageno}/";
	
	private int realprice(String numStr, String unitStr)
	{
		unitStr = unitStr.trim();
		int num = Integer.parseInt(numStr);
		int unit = 1;
		if(unitStr.equals("亿")){
			unit = 100000000;
		}else if(unitStr.equals("万")){
			unit = 10000;
		}else if(unitStr.equals("千")){
			unit = 1000;
		}else if(unitStr.equals("百")){
			unit = 100;
		}else if(unitStr.equals("十")){
			unit = 10;
		}
		return num*unit;
	}
	@Override
	public void process(Page page){
		String curUrl = page.getRequest().getUrl();
		System.out.println(TimeUtil.getCurrentTime()+" "+this.getClass().getSimpleName()+":"+curUrl);
		if(curUrl.equals(URL_BEGIN)){
			List<String> listUrlList = new ArrayList(100);
			for(int i=1; i<=ljTotalListNum; ++i){
				String listurl = ljListurl.replace("{city}", city).replace("{pageno}", String.valueOf(i));
				listUrlList.add(listurl);
			}
			System.out.println(TimeUtil.getCurrentTime()+" "+this.getClass().getSimpleName()+":"+curUrl+" got list url size:"+listUrlList.size());
            page.addTargetRequests(listUrlList);
            System.out.println(page.getTargetRequests().size());
		}else if(curUrl.matches(URL_LIST_REGEX)){
			//从列表页提取详情页连接
//			List<String> linkall = page.getHtml().xpath("//ul[@class='listContent']").links().all();
//			System.out.println(linkall.size());
//			for(String lk: linkall){
//				System.out.println(lk);
//			}
			List<String> detailUrlList = page.getHtml().xpath("//ul[@class='listContent']").links().regex(URL_DETAIL_REGEX).all();
			Set<String> detailUrlSet = new HashSet();
			detailUrlSet.addAll(detailUrlList);
			detailUrlList.clear();
			detailUrlList.addAll(detailUrlSet);
            System.out.println(TimeUtil.getCurrentTime()+" "+this.getClass().getSimpleName()+":"+curUrl+" got detail url size:"+detailUrlList.size());
            page.addTargetRequests(detailUrlList);
//            System.out.println(page.getTargetRequests().size());
//			for(Request r: page.getTargetRequests()){
//				System.out.println(r.getUrl());
//			}
		}else if(curUrl.matches(URL_DETAIL_REGEX)){
			///html/body/div[5]/div[2]/div[3]/div[5]/span[2]/text()
			String fangyuanIdStr = page.getHtml().xpath("//div[@class='content']/div[@class='aroundInfo']/div[@class='houseRecord']/span[@class='info']/text()").get();
			String priceStr = page.getHtml().xpath("//div[@class='content']/div[@class='price']/span[@class='total']/text()").get();
			String priceUnitStr = page.getHtml().xpath("//div[@class='content']/div[@class='price']/span[@class='unit']/span/text()").get();
			int price = realprice(priceStr, priceUnitStr);
			String unitPriceStr = page.getHtml().xpath("//div[@class='content']/div[@class='price']/div[@class='text']/div[@class='unitPrice']/span[@class='unitPriceValue']/text()").get();
			FangyuanHistEntity fangyuanEntity = new FangyuanHistEntity();
			fangyuanEntity.setFangyuanId(fangyuanIdStr);
			String curDate = TimeUtil.getCurrentDate("yyyyMMdd");
			fangyuanEntity.addPrice(new PriceEntity(price, curDate));
			fangyuanEntity.addUnitPrice(new PriceEntity(Integer.parseInt(unitPriceStr), curDate));
//			System.out.println(fangyuanEntity.toJson());
			page.putField("fangyuan", fangyuanEntity);
		}
	}
	
	public void start()
	{
		ServerConfEntity serverConfEntity = (ServerConfEntity)ConfParse.setEntity("./config/server.conf", ServerConfEntity.class);
		dao = new FangyuanDao();
		dao.init(serverConfEntity);
		System.out.println(serverConfEntity.getFangyuanTableName());
		System.out.println(serverConfEntity.getmongoHost());
		//http://bbs.310win.com/showforum-17-1.html
		Spider.create(this)
//			.addUrl("http://bj.lianjia.com/ershoufang/rs/")
			.addUrl("http://bj.lianjia.com/ershoufang/101100637189.html")
			.addPipeline(new MongoAppendfieldPipeLine(dao))
//			.addPipeline(new ConsolePipeline())
			.run();	
	}

	public static void main(String[] args)
	{
		FangyuanProcessor processor = new FangyuanProcessor();
		processor.start();
	}
}
