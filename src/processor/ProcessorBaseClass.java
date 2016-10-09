package processor;

import dao.CommonMongoDao;
import entity.CommonConfEntity;
import entity.ServerConfEntity;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;

public abstract class ProcessorBaseClass implements ProcessorBaseInterface{

	protected CommonMongoDao dao;
	protected CommonConfEntity commonConfEntity;
    //redis host
  	protected String redisHost = "127.0.0.1";
	protected String chromeDriverPath = "D:\\Programs\\Chrome\\chromedriver.exe";
	protected ServerConfEntity serverConfEntity;
	private Site site = Site
            .me()
            .setRetryTimes(3)
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
	
	abstract public void process(Page page);

	public Site getSite() {
		return site;
	}

	public void start() {
	}

	public void setConfig(CommonConfEntity commonConfEntity) {
		this.commonConfEntity = commonConfEntity;
	}

	public void setDao(CommonMongoDao nd) {
		this.dao = nd;
		
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
		
	}

	public void setChromeDriverPath(String chromeDriverPath) {
		this.chromeDriverPath = chromeDriverPath;		
	}

	public void setSeverConfEntity(ServerConfEntity severConfEntity) {
		this.serverConfEntity = severConfEntity;
		
	}
	public ServerConfEntity getSeverConfEntity(){
		return this.serverConfEntity;
	}

}
