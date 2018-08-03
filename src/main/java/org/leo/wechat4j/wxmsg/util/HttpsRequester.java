package org.leo.wechat4j.wxmsg.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 *  HTTPS请求工具类
 */
public class HttpsRequester {

	private static final HttpsRequester httpsRequester = new HttpsRequester();
	
	private static Map<String,Object> properties;

	private HttpConnectionManager connectionManager;
	
	private static final String PROTOCOL = "https";//协议类型
	
	private static final String CHARSET = "UTF-8";//编码
	
	private static final int DEFAULTPORT = 443;//默认端口
	
	public static final String HTTPS_TIMEOUT = "https.timeout";//连接超时时间
	
	public static final String HTTPS_SO_TIMEOUT = "https.so.timeout";//连接超时时间
	
	public static final String HTTPS_PER_HOST = "https.per.host";//单个服务器最大并发数
	
	public static final String HTTPS_TOTAL_CONN = "https.total.conn";//所有服务器最大并发数
	
	private int httpsTimeOut = 30000;
	
	private int httpsSOTimeOut = 30000;
	
	private int perHost = 15;
	
	private int totalConn = 80;

	private HttpsRequester(){
		
	}
	
	public static HttpsRequester getInstance() {
		properties = new HashMap<String, Object>();
		return httpsRequester;
	}

	public void setProperty(String key, Object value){
	    properties.put(key, value);
	}
	
	public void removeProperty(String key){
	    properties.remove(key);
	}

	public String sendPost(String sendUrl,String sendXML,Map<String, String> propertys) throws Exception {
		PostMethod method = null;
		try {
			//连接管理对象
			connectionManager = new MultiThreadedHttpConnectionManager();
			
			//设置协议参数
			HttpConnectionManagerParams params = new HttpConnectionManagerParams();
			params.setConnectionTimeout(getHttpsTimeOut());
			params.setSoTimeout(getHttpsSOTimeOut());
			params.setDefaultMaxConnectionsPerHost(getPerHost());
			params.setMaxTotalConnections(getTotalConn());
			connectionManager.setParams(params);
			
			HttpClient httpClient = new HttpClient(connectionManager);
			if (sendUrl.startsWith("https:")) {
				setSSLProtocol(sendUrl, httpClient);
			}
			
			//设置请求头参数
			method = new PostMethod(sendUrl);
			if (propertys != null){
				for (String key : propertys.keySet()) {
					method.addRequestHeader(key, propertys.get(key));
				}
			}
			
			//设置请求XML实体对象
			method.setRequestEntity(new StringRequestEntity(sendXML, null,CHARSET));
			
			//发送请求
			httpClient.executeMethod(method);
			
			//获取返回结果
			String recvXML = method.getResponseBodyAsString();
			
			return recvXML;
		} catch (Exception e) {
			throw e;
		} finally {
			if(method != null){
				method.releaseConnection();
			}
		}
	}

	private void setSSLProtocol(String strUrl, HttpClient client)throws Exception {
		URL url = new URL(strUrl);
		String host = url.getHost();
		int port = url.getPort();
		if (port <= 0) {
			port = DEFAULTPORT;
		}

		Protocol authhttps = new Protocol(PROTOCOL,new MyProtocolSocketFactory(), port);
		Protocol.registerProtocol(PROTOCOL, authhttps);

		//SET HTTPS PROTOCOL
		System.setProperty("apache.commons.httpclient.cookiespec","compatibility");
		client.getHostConfiguration().setHost(host, port, authhttps);
	}
	
	private int getHttpsTimeOut() {
		if(properties.get(HTTPS_TIMEOUT) != null 
				&& isNumeric(properties.get(HTTPS_TIMEOUT))){
			return Integer.parseInt(properties.get(HTTPS_TIMEOUT).toString());
		}
		return httpsTimeOut;
	}
	
	private int getHttpsSOTimeOut() {
		if(properties.get(HTTPS_SO_TIMEOUT) != null 
				&& isNumeric(properties.get(HTTPS_SO_TIMEOUT))){
			return Integer.parseInt(properties.get(HTTPS_SO_TIMEOUT).toString());
		}
		return httpsSOTimeOut;
	}
	
	private int getPerHost() {
		if(properties.get(HTTPS_PER_HOST) != null 
				&& isNumeric(properties.get(HTTPS_PER_HOST))){
			return Integer.parseInt(properties.get(HTTPS_PER_HOST).toString());
		}
		return perHost;
	}
	
	private int getTotalConn() {
		if(properties.get(HTTPS_TOTAL_CONN) != null 
				&& isNumeric(properties.get(HTTPS_TOTAL_CONN))){
			return Integer.parseInt(properties.get(HTTPS_TOTAL_CONN).toString());
		}
		return totalConn;
	}
	
	private static boolean isNumeric(Object str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str.toString());
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public class MyProtocolSocketFactory implements ProtocolSocketFactory {
		private SSLContext sslcontext = null;

		private SSLContext createSSLContext() {
			SSLContext sslcontext = null;
			try {
				sslcontext = SSLContext.getInstance("SSL");
				sslcontext.init(null,
						new TrustManager[] { new TrustAnyTrustManager() },
						new java.security.SecureRandom());
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e.getMessage());
			} catch (KeyManagementException e) {
				throw new RuntimeException(e.getMessage());
			}
			return sslcontext;
		}

		private SSLContext getSSLContext() {
			if (this.sslcontext == null) {
				this.sslcontext = createSSLContext();
			}
			return this.sslcontext;
		}

		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return getSSLContext().getSocketFactory().createSocket(socket,
					host, port, autoClose);
		}

		public Socket createSocket(String host, int port) throws IOException,
				UnknownHostException {
			return getSSLContext().getSocketFactory().createSocket(host, port);
		}

		public Socket createSocket(String host, int port,
				InetAddress clientHost, int clientPort) throws IOException,
				UnknownHostException {
			return getSSLContext().getSocketFactory().createSocket(host, port,
					clientHost, clientPort);
		}

		public Socket createSocket(String host, int port,
				InetAddress localAddress, int localPort,
				HttpConnectionParams params) throws IOException,
				UnknownHostException, ConnectTimeoutException {
			if (params == null) {
				throw new IllegalArgumentException("Parameters may not be null");
			}
			int timeout = params.getConnectionTimeout();
			SocketFactory socketfactory = getSSLContext().getSocketFactory();
			if (timeout == 0) {
				return socketfactory.createSocket(host, port, localAddress,
						localPort);
			} else {
				Socket socket = socketfactory.createSocket();
				SocketAddress localaddr = new InetSocketAddress(localAddress,
						localPort);
				SocketAddress remoteaddr = new InetSocketAddress(host, port);
				socket.bind(localaddr);
				socket.connect(remoteaddr, timeout);
				return socket;
			}
		}

		private class TrustAnyTrustManager implements X509TrustManager {
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}
		}
		
	}

}
