package processor;

import dao.CommonMongoDao;
import entity.CommonConfEntity;
import entity.ServerConfEntity;
import us.codecraft.webmagic.processor.PageProcessor;

public interface ProcessorBaseInterface extends PageProcessor{
	public void start();
	public void setConfig(CommonConfEntity cce);
	public void setDao(CommonMongoDao nd);
	public void setRedisHost(String redisHost);
	public void setChromeDriverPath(String chromeDriverPath);
	public void setSeverConfEntity(ServerConfEntity severConfEntity);
}
