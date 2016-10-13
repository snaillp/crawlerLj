package crawler;

import processor.ProcessorBaseInterface;
import util.TimeUtil;
import config.ConfParse;
import crawler.factory.CommonProcessorFactory;
import dao.FangyuanDao;
import entity.ServerConfEntity;
import entity.WebConfEntity;

public class FangyuanCrawler {
	private WebConfEntity nce;
	private FangyuanDao dao;
	private ServerConfEntity serverConfEntity;
	
	public FangyuanCrawler()
	{	
		
	}
	public FangyuanCrawler(String crawlconf, String serverconf)
	{	
		nce = (WebConfEntity) ConfParse.setEntity(crawlconf, WebConfEntity.class);
		serverConfEntity = (ServerConfEntity)ConfParse.setEntity(serverconf, ServerConfEntity.class);
		dao = new FangyuanDao();
		dao.init(serverConfEntity);
	}

	public void crawl() {
		System.out.println(TimeUtil.getCurrentTime() + " begin to crawl");
		String processorName = "FangyuanProcessor";
		ProcessorBaseInterface newsProcessor = CommonProcessorFactory.getProcessor(processorName);
		if (dao != null) {
			newsProcessor.setDao(dao);
			newsProcessor.setSeverConfEntity(serverConfEntity);
		}
		newsProcessor.start();
		System.out.println("crawl done...");
	}
	
	public static void help()
	{
		System.out.println("Usage: java -jar FangyuanCrawler.java newsconf serverconf");		
	}

	public static void main(String[] args)
	{
//		if(args.length != 2){
//			help();
//			System.exit(1);
//		}
//		FangyuanCrawler newsCrawler = new FangyuanCrawler("./config/testmatchnews.conf", "./config/server.conf");
		System.out.println(TimeUtil.getCurrentTime()+" begin to crawl news");
		FangyuanCrawler newsCrawler = new FangyuanCrawler(args[0], args[1]);
		newsCrawler.crawl();
		System.out.println(TimeUtil.getCurrentTime()+" crawl news done");
		System.exit(0);
	}
}
