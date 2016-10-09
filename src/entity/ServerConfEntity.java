package entity;

public class ServerConfEntity extends CommonConfEntity{
	public ServerConfEntity(String mongoHost, int mongoPort, String mongoDBName, String redisHost, String chromeDriver, int threadnum)
	{
		super();
		this.mongoHost = mongoHost;
		this.mongoPort = mongoPort;
		this.mongoDBName = mongoDBName;
		this.redisHost = redisHost;
		this.chromeDriver = chromeDriver;
		this.threadnum = threadnum;
	}
	public ServerConfEntity()
	{
		mongoHost = "182.92.157.71";
		 mongoPort = 27017;
		 mongoDBName = "newstest";
		 redisHost = "182.92.157.71";
		 chromeDriver = "";
		 threadnum = 1;
		 pageNeedCheck=5;
	}
	
	public String getmongoHost() {
		return mongoHost;
	}
	public void setmongoHost(String mongoHost) {
		this.mongoHost = mongoHost;
	}
	public int getmongoPort() {
		return mongoPort;
	}
	public void setmongoPort(int mongoPort) {
		this.mongoPort = mongoPort;
	}
	public String getmongoDBName() {
		return mongoDBName;
	}
	public void setmongoDBName(String mongoDBName) {
		this.mongoDBName = mongoDBName;
	}
	public String getRedisHost() {
		return redisHost;
	}
	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}
	public String getChromeDriver() {
		return chromeDriver;
	}
	public void setChromeDriver(String chromeDriver) {
		this.chromeDriver = chromeDriver;
	}
	public int getThreadnum() {
		return threadnum;
	}
	public void setThreadnum(int threadnum) {
		this.threadnum = threadnum;
	}
	public int getPageNeedCheck() {
		return pageNeedCheck;
	}
	public void setPageNeedCheck(int pageNeedCheck) {
		this.pageNeedCheck = pageNeedCheck;
	}
	public String getImageDBName() {
		return imageDBName;
	}
	public void setImageDBName(String imageDBName) {
		this.imageDBName = imageDBName;
	}
	public String getImageUrlPrefix() {
		return imageUrlPrefix;
	}
	public void setImageUrlPrefix(String imageUrlPrefix) {
		this.imageUrlPrefix = imageUrlPrefix;
	}
	
	public String getFangyuanTableName() {
		return fangyuanTableName;
	}
	public void setFangyuanTableName(String fangyuanTableName) {
		this.fangyuanTableName = fangyuanTableName;
	}



	private String mongoHost;
	private int mongoPort;
	private String mongoDBName;
//TODO table name
	private String fangyuanTableName;
	private String imageDBName;
	private String imageUrlPrefix;
	private String redisHost;
	private String chromeDriver;
	private int threadnum;
	private int pageNeedCheck;

}
