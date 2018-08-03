package org.leo.wechat4j.wxmsg.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

/**
 * 微信下载media 专用
 */
public class HttpRequesterMedia {

	private static final HttpRequesterMedia httpRequester = new HttpRequesterMedia();

	private HttpRequesterMedia() {
		
	}

	public static HttpRequesterMedia getInstance() {
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
			int readTimeout,String path) throws IOException {
		return send(urlString, "GET", connTimeout, readTimeout, null, null, path);
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
			Map<String, String> propertys,String path) throws IOException {
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
				param.append("&");
				param.append(key).append("=").append(parameters.get(key));
			}
			urlConnection.getOutputStream().write(param.toString().getBytes());
			urlConnection.getOutputStream().flush();
			urlConnection.getOutputStream().close();
		}
		return makeContent(urlString, urlConnection,path);
	}

	/**
	 * 得到响应对象
	 * 
	 * @param urlConnection
	 * @return 响应对象
	 * @throws IOException
	 */
	private static HttpRespons makeContent(String urlString,
			HttpURLConnection urlConnection,String path) throws IOException {
		HttpRespons httpResponser = new HttpRespons();
		try {
			int respCode = urlConnection.getResponseCode();
			if (respCode != HttpURLConnection.HTTP_OK) {
				throw new IOException("连接失败[" + respCode + ":" + urlString
						+ "]");
			}

			/*String type = urlConnection.getContentType();
			type = type.substring(type.indexOf("/")+1);*/
			String type = ".amr";
			
			path = path + "amr" + File.separator;
			File f = new File(path);
	        // 创建文件夹
	        if (!f.exists()) {
	            f.mkdirs();
	        }
	        //下载 amr 到指定文件夹下
			String mediaName = Calendar.getInstance().getTimeInMillis() + type;  // 文件名称
			path += mediaName;
			
			InputStream inStream = urlConnection.getInputStream();
			int byteread = 0;
			FileOutputStream fs = new FileOutputStream(path);
			byte[] buffer = new byte[1204];
			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}

			httpResponser.fileName = mediaName;
			httpResponser.filePath = path;
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
		
		String fileName;
		
		String filePath;
		
		public String getFilePath() {
			return filePath;
		}
		
		public String getFileName() {
			return fileName;
		}

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
