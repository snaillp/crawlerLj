package entity;

import java.util.List;

public class WebSiteConfEntity extends CommonConfEntity{

	private String Processor;
	private String site;

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getProcessor() {
		return Processor;
	}

	public void setProcessor(String processor) {
		this.Processor = processor;
	}
}
