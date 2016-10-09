package entity;

import java.util.List;

public class WebConfEntity extends CommonConfEntity{
	private List<WebConfEntity> webInfo;

	public List<WebConfEntity> getNewsInfo() {
		return webInfo;
	}

	public void setNewsInfo(List<WebConfEntity> webInfo) {
		this.webInfo = webInfo;
	}

}
