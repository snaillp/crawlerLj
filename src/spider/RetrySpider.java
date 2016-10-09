package spider;

import java.io.IOException;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import util.TimeUtil;

/*
 * 在spider爬取网页的时候加入多次尝试
 */

public class RetrySpider extends Spider{
	protected int trytime = 3;
	protected int sleepTime = 500;
	
	public RetrySpider(PageProcessor pageProcessor) {
		super(pageProcessor);
	}
	
	public static RetrySpider create(PageProcessor pageProcessor) {
        return new RetrySpider(pageProcessor);
    }
	
	@Override
    public Spider addUrl(String... urls) {
        super.addUrl(urls);
        return this;
    }
	
	@Override
	public Spider setDownloader(Downloader downloader) {
        super.setDownloader(downloader);
        return this;
    }
	
	public Spider setTrytime(int trytime)
	{
		this.trytime = trytime;
		return this;
	}
	@Override
	protected void processRequest(Request request) {
		Page page = null;
		if(site.getRetryTimes() != 0){
			trytime = site.getRetryTimes();
		}
		if(site.getSleepTime() != 0){
			sleepTime = site.getSleepTime();
		}
		int tryno = 0;
		while(tryno<trytime){
			try{
				page = downloader.download(request, this);
			}catch(Exception e){
				e.printStackTrace();
				System.out.println(TimeUtil.getCurrentTime()+" RetrySpider Try to download "+request.getUrl()+" "+(tryno+1)+" times, error case by "+e.getClass().getSimpleName());
		        ++tryno;
		        sleep(sleepTime);
		        continue;
			}
			if (page == null) {
				System.out.println(TimeUtil.getCurrentTime()+" RetrySpider Try to download "+request.getUrl()+" "+(tryno+1)+" times, error case by get page null");
	            sleep(sleepTime);
	            ++tryno;
	        }else{
	        	break;
	        }
		}
		if(page == null){
			onError(request);
            return;
		}
        // for cycle retry
        if (page.isNeedCycleRetry()) {
            extractAndAddRequests(page, true);
            sleep(site.getSleepTime());
            return;
        }
        pageProcessor.process(page);
        extractAndAddRequests(page, spawnUrl);
        if (!page.getResultItems().isSkip()) {
            for (Pipeline pipeline : pipelines) {
                pipeline.process(page.getResultItems(), this);
            }
        }
        sleep(sleepTime);
    }
}
