package util;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupHelper {

	public static String USER_AGENT = "Mozilla/5.0 (Linux; U; Android 2.3.5; zh-cn; HTC_IncredibleS_S710e Build/GRJ90) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
	public static int TIMEOUT = 10000;

	public static Map<String, String> getCookieParams(String url) throws IOException {
		Connection.Response res = Jsoup.connect(url).userAgent(USER_AGENT).timeout(TIMEOUT).method(Method.GET)
				.execute();
		return res.cookies();
	}

	public static String getString(String url) throws Exception {
		return Jsoup.connect(url).userAgent(USER_AGENT).timeout(TIMEOUT).method(Method.GET).execute().toString();
	}

	public static void main(String[] args) throws Exception {
		String url = "http://m.lianjia.com/";
		// String str =
		// Jsoup.connect(url).userAgent(USER_AGENT).timeout(TIMEOUT).method(Method.GET).execute().parse().select("script[conf]")
		// .toString();
//		String str = getCookieParams(url).toString();
		String str = getString("http://m.api.lianjia.com/house/mfangjia/pricemap?city_id=110000&query=&p=1&access_token=&utm_source=&device_id=73484007dc0c59d482d901a6cd221951");
		System.out.println(str);

		// Document doc = Jsoup.connect(url).timeout(TIMEOUT).get();
		// Elements scriptElements = doc.getElementsByTag("script");
		//
		// for (Element element : scriptElements) {
		// for (DataNode node : element.dataNodes()) {
		// System.out.println(node.getWholeData());
		// }
		// System.out.println("-------------------");
		// }

	}
}
