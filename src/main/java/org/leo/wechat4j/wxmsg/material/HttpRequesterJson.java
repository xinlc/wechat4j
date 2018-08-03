package org.leo.wechat4j.wxmsg.material;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Vector;

/**
 *  HTTP请求工具类 支持 json
 * @author Leo
 * @create 2018-08-03
 */
public class HttpRequesterJson {

	private static final HttpRequesterJson httpRequester = new HttpRequesterJson();

	private static String defaultContentEncoding = Charset.defaultCharset().name();

	private HttpRequesterJson() {
		
	}

	public static HttpRequesterJson getInstance() {
		return httpRequester;
	}

	/**
	 * 发送GET请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @return 响应对象
	 * @throws IOException
	 */
	public static HttpRespons sendGet(String urlString, int connTimeout,
			int readTimeout) throws IOException {
		return send(urlString, "GET", connTimeout, readTimeout, null, null);
	}

	/**
	 * 发送GET请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            参数集合
	 * @return 响应对象
	 * @throws IOException
	 */
	public static HttpRespons sendGet(String urlString, int connTimeout,
			int readTimeout, Map<String, String> params) throws IOException {
		return send(urlString, "GET", connTimeout, readTimeout, params, null);
	}

	/**
	 * 发送GET请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            参数集合
	 * @param propertys
	 *            请求属性
	 * @return 响应对象
	 * @throws IOException
	 */
	public static HttpRespons sendGet(String urlString, int connTimeout,
			int readTimeout, Map<String, String> params,
			Map<String, String> propertys) throws IOException {
		return send(urlString, "GET", connTimeout, readTimeout, params,
				propertys);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @return 响应对象
	 * @throws IOException
	 */
	public static HttpRespons sendPost(String urlString, int connTimeout,
			int readTimeout) throws IOException {
		return send(urlString, "POST", connTimeout, readTimeout, null, null);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            参数集合
	 * @return 响应对象
	 * @throws IOException
	 */
	public static HttpRespons sendPost(String urlString, int connTimeout,
			int readTimeout, Map<String, String> params) throws IOException {
		return send(urlString, "POST", connTimeout, readTimeout, params, null);
	}

	/**
	 * 发送POST请求
	 * 
	 * @param urlString
	 *            URL地址
	 * @param params
	 *            参数集合
	 * @param propertys
	 *            请求属性
	 * @return 响应对象
	 * @throws IOException
	 */
	public static HttpRespons sendPost(String urlString, int connTimeout,
			int readTimeout, Map<String, String> params,
			Map<String, String> propertys) throws IOException {
		return send(urlString, "POST", connTimeout, readTimeout, params,
				propertys);
	}

	/**
	 * 发送HTTP请求
	 * @param urlString
	 * @param method
	 * @param connTimeout
	 * @param readTimeout
	 * @param parameters
	 * @param propertys
	 * @return 响应对象
	 * @throws IOException
	 */
	private static HttpRespons send(String urlString, String method,
			int connTimeout, int readTimeout, Map<String, String> parameters,
			Map<String, String> propertys) throws IOException {
		HttpURLConnection urlConnection = null;

		if (method.equalsIgnoreCase("GET") && parameters != null) {
			StringBuffer param = new StringBuffer();
			int i = urlString.indexOf("?");
			for (String key : parameters.keySet()) {
				if (i == -1)
					param.append("?");
				else
					param.append("&");
				param.append(key).append("=").append(parameters.get(key));
				i++;
			}
			urlString += param;
		}
		URL url = new URL(urlString);
		urlConnection = (HttpURLConnection) url.openConnection();

		urlConnection.setRequestMethod(method);
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(false);
		urlConnection.setConnectTimeout(connTimeout);
		urlConnection.setReadTimeout(readTimeout);

		if (propertys != null)
			for (String key : propertys.keySet()) {
				urlConnection.addRequestProperty(key, propertys.get(key));
			}

		if (method.equalsIgnoreCase("POST") && parameters != null) {
			StringBuffer param = new StringBuffer();
			for (String key : parameters.keySet()) {
				if("paramJsonType".equals(key)){  //支持json 参数
					param.append(parameters.get(key));
				}else{
					param.append("&");
					param.append(key).append("=").append(parameters.get(key));
				}
			}
			urlConnection.getOutputStream().write(param.toString().getBytes());
			urlConnection.getOutputStream().flush();
			urlConnection.getOutputStream().close();
		}
		return makeContent(urlString, urlConnection);
	}

	/**
	 * 得到响应对象
	 * 
	 * @param urlConnection
	 * @return 响应对象
	 * @throws IOException
	 */
	private static HttpRespons makeContent(String urlString,
			HttpURLConnection urlConnection) throws IOException {
		HttpRespons httpResponser = new HttpRespons();
		try {
			int respCode = urlConnection.getResponseCode();
			if (respCode != HttpURLConnection.HTTP_OK) {
				throw new IOException("连接失败[" + respCode + ":" + urlString
						+ "]");
			}

			InputStream in = urlConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in,"UTF-8"));
			httpResponser.contentCollection = new Vector<String>();
			StringBuffer temp = new StringBuffer();
			String line = bufferedReader.readLine();
			while (line != null) {
				httpResponser.contentCollection.add(line);
				temp.append(line).append("\r\n");
				line = bufferedReader.readLine();
			}
			bufferedReader.close();

			String ecod = urlConnection.getContentEncoding();
			if (ecod == null)
				ecod = defaultContentEncoding;

			httpResponser.urlString = urlString;

			httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();
			httpResponser.file = urlConnection.getURL().getFile();
			httpResponser.host = urlConnection.getURL().getHost();
			httpResponser.path = urlConnection.getURL().getPath();
			httpResponser.port = urlConnection.getURL().getPort();
			httpResponser.protocol = urlConnection.getURL().getProtocol();
			httpResponser.query = urlConnection.getURL().getQuery();
			httpResponser.ref = urlConnection.getURL().getRef();
			httpResponser.userInfo = urlConnection.getURL().getUserInfo();

			httpResponser.content = new String(temp.toString().getBytes(), ecod);
			httpResponser.contentEncoding = ecod;
			httpResponser.code = urlConnection.getResponseCode();
			httpResponser.message = urlConnection.getResponseMessage();
			httpResponser.contentType = urlConnection.getContentType();
			httpResponser.method = urlConnection.getRequestMethod();
			httpResponser.connectTimeout = urlConnection.getConnectTimeout();
			httpResponser.readTimeout = urlConnection.getReadTimeout();

			return httpResponser;
		} catch (IOException e) {
			throw e;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}
	
	public static class HttpRespons {

		String urlString;

		int defaultPort;

		String file;

		String host;

		String path;

		int port;

		String protocol;

		String query;

		String ref;

		String userInfo;

		String contentEncoding;

		String content;

		String contentType;

		int code;

		String message;

		String method;

		int connectTimeout;

		int readTimeout;

		Vector<String> contentCollection;

		public String getContent() {
			return content;
		}

		public String getContentType() {
			return contentType;
		}

		public int getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

		public Vector<String> getContentCollection() {
			return contentCollection;
		}

		public String getContentEncoding() {
			return contentEncoding;
		}

		public String getMethod() {
			return method;
		}

		public int getConnectTimeout() {
			return connectTimeout;
		}

		public int getReadTimeout() {
			return readTimeout;
		}

		public String getUrlString() {
			return urlString;
		}

		public int getDefaultPort() {
			return defaultPort;
		}

		public String getFile() {
			return file;
		}

		public String getHost() {
			return host;
		}

		public String getPath() {
			return path;
		}

		public int getPort() {
			return port;
		}

		public String getProtocol() {
			return protocol;
		}

		public String getQuery() {
			return query;
		}

		public String getRef() {
			return ref;
		}

		public String getUserInfo() {
			return userInfo;
		}

	}
	
}
