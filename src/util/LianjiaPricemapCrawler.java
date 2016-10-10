package util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.bj58.sfb.housePriceServer.HousePriceServerConfig;
import com.bj58.sfb.housePriceServer.crawler.util.HttpClientHelper;
import com.bj58.sfb.housePriceServer.crawler.util.JsoupHelper;
import com.bj58.sfb.housePriceServer.dao.HousePriceDao;
import com.bj58.sfb.housePriceServer.entity.House_locationBean;
import com.bj58.sfb.nlpcomutils.primitives.StringUtil;
import com.bj58.commons.utils.DisplocalUtils;
import com.google.common.collect.Multimap;
import com.google.common.collect.HashMultimap;
import com.google.gson.Gson;

public class LianjiaPricemapCrawler {

	private static final Logger logger = Logger.getLogger(LianjiaPricemapCrawler.class);

	private final String API_URL = "http://m.api.lianjia.com/house/mfangjia/pricemap?";
	private String HOME_URL = "http://m.lianjia.com/";

	private String cookie_lianjia_uuid = null;
	private String cookie_LJSESSID = null;

	private HousePriceDao housePriceDao = null;
	private DisplocalUtils displocalUtils;
	private Multimap<Integer, DisplocalUtils.Displocal> pid_displocalMap;
	private Gson gson = new Gson();

	private static final String[] ADDRSUFFIX = { "市", "县", "镇" };
	private static Set<String> ADDRSUFFIXSET = new HashSet<String>(Arrays.asList(ADDRSUFFIX));

	public LianjiaPricemapCrawler() {
	}

	public void init(HousePriceServerConfig config) throws Exception {
		updateCookie();
		HousePriceDao.initInstance(config);
		housePriceDao = HousePriceDao.getInstance();

		displocalUtils = DisplocalUtils.getInstance();
		pid_displocalMap = HashMultimap.create();
		for (DisplocalUtils.Displocal displocal : displocalUtils.getAllDisplocal()) {
			pid_displocalMap.put(displocal.getPID(), displocal);
		}
	}

	public void updateCookie() {
		Map<String, String> cookieMap;
		try {
			cookieMap = JsoupHelper.getCookieParams(HOME_URL);
			if (cookieMap != null) {
				// {lianjia_uuid=c4a17b7094c5b3b8a105e49b8bdce9c6,
				// select_city=110000, LJSESSID=qq1anr8kth9uj3a7h5qnhqeig3}
				cookie_lianjia_uuid = cookieMap.get("lianjia_uuid");
				cookie_LJSESSID = cookieMap.get("LJSESSID");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getHOME_URL() {
		return HOME_URL;
	}

	public String getCookie_lianjia_uuid() {
		return cookie_lianjia_uuid;
	}

	public String getCookie_LJSESSID() {
		return cookie_LJSESSID;
	}

	public void setHOME_URL(String hOME_URL) {
		HOME_URL = hOME_URL;
	}

	public void setCookie_lianjia_uuid(String cookie_lianjia_uuid) {
		this.cookie_lianjia_uuid = cookie_lianjia_uuid;
	}

	public void setCookie_LJSESSID(String cookie_LJSESSID) {
		this.cookie_LJSESSID = cookie_LJSESSID;
	}

	public String getRequest(String city_id, String query, String p, String access_token, String utm_source,
			String device_id) {
		if (StringUtil.isEmpty(city_id)) {
			throw new IllegalArgumentException("city_id missing");
		}

		if (query == null) {
			query = "";
		}
		if (p == null) {
			p = "";
		}
		if (access_token == null) {
			access_token = "";
		}
		if (utm_source == null) {
			utm_source = "";
		}

		// city_id=110000&query=&p=1&access_token=&utm_source=&device_id=73484007dc0c59d482d901a6cd221951
		StringBuilder requestBuilder = new StringBuilder();
		requestBuilder.append(API_URL).append("city_id=").append(city_id).append("&query=").append(query).append("&p=")
				.append(p).append("&access_token=").append(access_token).append("&utm_source=").append(utm_source)
				.append("&device_id=").append(device_id);

		try {
			System.out.println("request: " + requestBuilder.toString());
			HttpClientHelper httpClientHelper = new HttpClientHelper();
			String resultJson = httpClientHelper.doGet_String(requestBuilder.toString());
			return resultJson;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public PricemapResponse getPricemapResponse(String responseStr) {
		if (StringUtil.isEmpty(responseStr)) {
			return null;
		}

		return gson.fromJson(responseStr, PricemapResponse.class);
	}

	public List<House_locationBean> getLocationBeanLevel(int level, long pid) throws SQLException, Exception {
		if (level == 1)
			return housePriceDao
					.selectHouse_location("where state=1 and source='lianjia' and level=" + level + " and pid=" + pid);
		else
			return housePriceDao.selectHouse_location(
					"where state=1 and source='lianjia' and level=" + level + " and local_id=" + pid);
	}

	public void batchUpdateHouseLocation(int level, long pid, int maxLevel) throws Exception {
		System.out.println("batchUpdateHouseLocation Level: " + level + " pid: " + pid);
		List<House_locationBean> processedLocations = new ArrayList<House_locationBean>();

		List<House_locationBean> levelLocations = getLocationBeanLevel(level, pid);
		if (levelLocations == null || levelLocations.isEmpty()) {
			System.out.println("levelLocations is null/empty");
			return;
		}
		System.out.println("Get level locations: " + levelLocations.size());

		for (House_locationBean levelLocation : levelLocations) {
			String q = "";
			if (level > 1) {
				q = levelLocation.getLocal_name();
			}
			PricemapResponse pricemap = getPricemapResponse(
					getRequest(levelLocation.getLocal_path().split(",")[0], q, "1", "", "", cookie_lianjia_uuid));
			if (pricemap != null && pricemap.getData() != null) {
				for (PricemapResponse.List priceList : pricemap.getData().getList()) {
					House_locationBean locat = new House_locationBean();
					locat.setLocal_id(Long.parseLong(priceList.getId()));
					locat.setPid(levelLocation.getLocal_id());
					if (StringUtil.isEmpty(levelLocation.getLocal_path())) {
						locat.setLocal_path(priceList.getId());
					} else {
						locat.setLocal_path(levelLocation.getLocal_path() + "," + priceList.getId());
					}
					locat.setLocal_name(priceList.getName());
					locat.setList_name(priceList.getQuanpin_url());
					locat.setLevel(level + 1);
					locat.setLongitude(priceList.getLongitude());
					locat.setLatitude(priceList.getLatitude());
					locat.setSource("lianjia");
					locat.setState(1);
					String localName = locat.getLocal_name().trim();
					if ((localName.length() > 1)
							&& ADDRSUFFIXSET.contains(String.valueOf(localName.charAt(localName.length() - 1)))) {
						localName = localName.substring(0, localName.length() - 1);
					}
					boolean flag_within = false, flag_origin = false;
					int cmc_displocal_id = 0;
					String displocal_idstr = levelLocation.getCmc_displocal_id();
					if (displocal_idstr == null) {
						continue;
					}
					String[] displocal_idstrs = displocal_idstr.split(",");
					int[] displocal_ids = new int[displocal_idstrs.length];
					for (int i = 0; i < displocal_idstrs.length; i++) {
						displocal_ids[i] = Integer.valueOf(displocal_idstrs[i]);
					}
					for (int i = 0; i < displocal_ids.length; i++) {
						if (pid_displocalMap.containsKey(displocal_ids[i])) {
							for (DisplocalUtils.Displocal displocal : pid_displocalMap.get(displocal_ids[i])) {

								String disp_localName = displocal.getLocalName().trim();
								if ((disp_localName.length() > 1) && ADDRSUFFIXSET
										.contains(String.valueOf(disp_localName.charAt(disp_localName.length() - 1)))) {
									disp_localName = disp_localName.substring(0, disp_localName.length() - 1);
								}
								if (displocal.getLocalName().equals(locat.getLocal_name())) {
									locat.setCmc_displocal_id(String.valueOf(displocal.getDispLocalID()));
									flag_origin = true;
									break;
								}
								if (localName.equals(disp_localName)) {
									cmc_displocal_id = displocal.getDispLocalID();
									flag_within = true;
								}

								if (!flag_origin && flag_within) {
									locat.setCmc_displocal_id(String.valueOf(cmc_displocal_id));
								}
							}
							try {
								// System.out.println("db insert : " +
								// gson.toJson(locat));
								// housePriceDao.insertHouse_location(locat);
								housePriceDao.updateInsertHouse_location(locat);
								processedLocations.add(locat);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							System.out.println("pid_displocalMap null on " + levelLocation.getLocal_name());
						}
					}
				}
			}
			if (!processedLocations.isEmpty()) {
				if ((level + 1) < maxLevel) {
					for (House_locationBean location : processedLocations) {
						batchUpdateHouseLocation(level + 1, location.getLocal_id(), maxLevel);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		// org.apache.log4j.PropertyConfigurator.configure("./log4j.properties");
		HousePriceServerConfig config = new HousePriceServerConfig();
		LianjiaPricemapCrawler op = new LianjiaPricemapCrawler();
		try {
			op.init(config);
			op.batchUpdateHouseLocation(1, 0, 3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("DONE");
		System.exit(0);
	}
}
