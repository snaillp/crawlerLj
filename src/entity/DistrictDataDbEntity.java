package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.TimeUtil;

import com.google.gson.Gson;

public class DistrictDataDbEntity extends CommonEntity{
	
	public DistrictDataDbEntity(String districtDataContent)
	{
		crawltime = TimeUtil.getCurSecond();
		statdate = TimeUtil.getDayInc("yyyyMMdd", -1);
		dataList = new ArrayList();
		Map<String, Map<String, Object>> districtDataMap = new Gson().fromJson(districtDataContent, Map.class);
		for(int i=0; i<Integer.MAX_VALUE; ++i){
			String no = String.valueOf(i);
			if(districtDataMap.containsKey(no)){
//				System.out.println(districtDataMap.get(no));
				Map<String, Object> dataMap = districtDataMap.get(no);
				DistrictDataEntity dde = new DistrictDataEntity();
				dde.setName((String)dataMap.get("name"));
				dde.setQuanpin_url((String)dataMap.get("quanpin_url"));
				dde.setTransPrice(((Double)dataMap.get("transPrice")).intValue());
				int quantity = (Integer)dataMap.get("quantity");
				dde.setQuantity(quantity);
				dataList.add(dde);
			}else{
				break;
			}
		}
	}
	final HouseDataType dataType = HouseDataType.HouseDistricDayStat;
	private long crawltime;
	private String statdate;
	List<DistrictDataEntity> dataList;
	
	public class DistrictDataEntity extends CommonEntity{
		private String name;
		private String quanpin_url;
		private int transPrice;
		private int quantity;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getQuanpin_url() {
			return quanpin_url;
		}
		public void setQuanpin_url(String quanpin_url) {
			this.quanpin_url = quanpin_url;
		}
		public int getTransPrice() {
			return transPrice;
		}
		public void setTransPrice(int transPrice) {
			this.transPrice = transPrice;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
	}
	public static void main(String[] args)
	{
		String aa = "{'longitude':116.403874,'latitude':39.914889,'0':{'id':23008614,'name':'\u4e1c\u57ce','quanpin_url':'dongcheng','longitude':116.42447316303,'latitude':39.918649000289,'transPrice':81784,'quantity':'941'},'1':{'id':23008626,'name':'\u897f\u57ce','quanpin_url':'xicheng','longitude':116.36960374452,'latitude':39.910041817755,'transPrice':92456,'quantity':'1382'},'2':{'id':23008613,'name':'\u671d\u9633','quanpin_url':'chaoyang','longitude':116.51560779421,'latitude':39.941870226449,'transPrice':59378,'quantity':'7851'},'3':{'id':23008618,'name':'\u6d77\u6dc0','quanpin_url':'haidian','longitude':116.31928440197,'latitude':39.988146111109,'transPrice':73254,'quantity':'3211'},'4':{'id':23008617,'name':'\u4e30\u53f0','quanpin_url':'fengtai','longitude':116.2745350184,'latitude':39.856482924451,'transPrice':47101,'quantity':'2986'},'5':{'id':23008623,'name':'\u77f3\u666f\u5c71','quanpin_url':'shijingshan','longitude':116.1881220088,'latitude':39.927380788933,'transPrice':43113,'quantity':'856'},'6':{'id':23008625,'name':'\u901a\u5dde','quanpin_url':'tongzhou','longitude':116.66910860993,'latitude':39.899697215983,'transPrice':41761,'quantity':'1123'},'7':{'id':23008611,'name':'\u660c\u5e73','quanpin_url':'changping','longitude':116.23936485567,'latitude':40.2254152452,'transPrice':34181,'quantity':'2835'},'8':{'id':23008615,'name':'\u5927\u5174','quanpin_url':'daxing','longitude':116.35616519116,'latitude':39.732475753738,'transPrice':35025,'quantity':'1662'},'9':{'id':23008629,'name':'\u4ea6\u5e84\u5f00\u53d1\u533a','quanpin_url':'yizhuangkaifaqu','longitude':116.51437283867,'latitude':39.787661925087,'transPrice':36786,'quantity':'449'},'10':{'id':23008624,'name':'\u987a\u4e49','quanpin_url':'shunyi','longitude':116.65925154446,'latitude':40.143652317363,'transPrice':31826,'quantity':'1367'},'11':{'id':23008616,'name':'\u623f\u5c71','quanpin_url':'fangshan','longitude':116.1380205325,'latitude':39.729988560946,'transPrice':22110,'quantity':'1083'},'12':{'id':23008620,'name':'\u95e8\u5934\u6c9f','quanpin_url':'mentougou','longitude':116.11693954783,'latitude':39.914751032922,'transPrice':26433,'quantity':'274'},'13':{'id':23008622,'name':'\u5e73\u8c37','quanpin_url':'pinggu','longitude':117.12371117513,'latitude':40.149397302875,'transPrice':20029,'quantity':'0'},'14':{'id':23008619,'name':'\u6000\u67d4','quanpin_url':'huairou','longitude':116.65006125307,'latitude':40.347397522906,'transPrice':22811,'quantity':'1'},'15':{'id':23008621,'name':'\u5bc6\u4e91','quanpin_url':'miyun','longitude':116.85485407577,'latitude':40.391839887884,'transPrice':20303,'quantity':'4'},'16':{'id':23008628,'name':'\u5ef6\u5e86','quanpin_url':'yanqing','longitude':115.98166187113,'latitude':40.461932306287,'transPrice':28690,'quantity':null},'17':{'id':23008631,'name':'\u71d5\u90ca','quanpin_url':'yanjiao','longitude':116.83169234643,'latitude':39.956500361571,'transPrice':25742,'quantity':'0'}}";
		DistrictDataDbEntity ddde = new DistrictDataDbEntity(aa);
		System.out.println(ddde.toJson());
	}
}

