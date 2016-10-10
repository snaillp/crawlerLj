package util;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.bj58.sfb.nlpcomutils.primitives.StringUtil;

public class HttpClientHelper {

	public static String USER_AGENT = "Mozilla/5.0 (Linux; U; Android 2.3.5; zh-cn; HTC_IncredibleS_S710e Build/GRJ90) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
	CloseableHttpClient httpclient;
	RequestConfig requestConfig;

	public HttpClientHelper() {
		httpclient = HttpClients.createDefault();
		requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000)
				.setRedirectsEnabled(false).setConnectionRequestTimeout(10000).setContentCompressionEnabled(false)
				.build();
	}

	public void close() throws IOException {
		if (httpclient != null) {
			httpclient.close();
			httpclient = null;
		}
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		close();
	}

	public void setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}

	public String doGet_String(String url) throws URISyntaxException, IOException, ConnectException {
		URI uri = new URI(url);
		HttpGet httpget = new HttpGet(uri);
		httpget.setConfig(requestConfig);
		httpget.setHeader("User-Agent", USER_AGENT);

		CloseableHttpResponse response = httpclient.execute(httpget);
		try {
			HttpEntity entity = response.getEntity();
			String returnStr = EntityUtils.toString(entity);
			if (StringUtil.isEmpty(returnStr) && response.getStatusLine().getStatusCode() >= 400) {
				throw new ConnectException(response.getStatusLine().getReasonPhrase() + " Code: "
						+ response.getStatusLine().getStatusCode());

			}
			return returnStr;
		} finally {
			response.close();
		}
	}

	public static void main(String[] args) {
		HttpClientHelper op = new HttpClientHelper();
		String url = "http://m.api.lianjia.com/house/mfangjia/pricemap?city_id=110000&query=&p=1&access_token=&utm_source=&device_id=73484007dc0c59d482d901a6cd221951";
		try {
			System.out.println(op.doGet_String(url));
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
