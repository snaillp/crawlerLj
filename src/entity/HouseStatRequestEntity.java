package entity;

import java.util.Calendar;

public class HouseStatRequestEntity {
	public class RequestDataEntity{
		private HouseStat4CrawlEntity card;
		private PriceTrend4Crawl price_trend;
		private SupplyDemandTrend4Crawl supply_demand_trend;
		public HouseStat4CrawlEntity getCard() {
			return card;
		}
		public void setCard(HouseStat4CrawlEntity card) {
			this.card = card;
		}
		public PriceTrend4Crawl getPrice_trend() {
			return price_trend;
		}
		public void setPrice_trend(PriceTrend4Crawl price_trend) {
			this.price_trend = price_trend;
		}
		public SupplyDemandTrend4Crawl getSupply_demand_trend() {
			return supply_demand_trend;
		}
		public void setSupply_demand_trend(SupplyDemandTrend4Crawl supply_demand_trend) {
			this.supply_demand_trend = supply_demand_trend;
		}
		
	}
	private String request_id;
	private RequestDataEntity data;
	public String getRequest_id() {
		return request_id;
	}
	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
	public RequestDataEntity getData() {
		return data;
	}
	public void setData(RequestDataEntity data) {
		this.data = data;
	}
	public static void main(String[] args)
	{
		System.out.println(Calendar.getInstance().get(Calendar.MONTH));
	}
}
