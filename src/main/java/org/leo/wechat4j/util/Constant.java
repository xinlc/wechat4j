package org.leo.wechat4j.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;


public class Constant {

	//素材管理-图片 上传路径
	public static final String FILE_PATH = "resources"+File.separator+"upload_file"+File.separator+"material"+File.separator+"pic"+File.separator;
	
	//素材管理-语音 上传路径
	public static final String VOICE_FILE_PATH = "resources"+File.separator+"upload_file"+File.separator+"material"+File.separator+"voice"+File.separator;
	
	//素材管理-视频 上传路径
	public static final String VIDEO_FILE_PATH = "resources"+File.separator+"upload_file"+File.separator+"material"+File.separator+"video"+File.separator;
	
	//微信素材路径
	public static final String W_FILE_PATH = "resources/upload_file/material/pic/";
	public static final String W_VOICE_FILE_PATH = "resources/upload_file/material/voice/";
	public static final String W_VIDEO_FILE_PATH = "resources/upload_file/material/video/";

	public static final String VOICE_TOOL_URL = "http://xxx/";


	//web接口
	private static String web = null;
	
	public static String getWeb() {
		return web;
	}

	public static String getWeb(HttpServletRequest request) {
		if (null == web ) {
			Properties prop = new Properties();
			String path = request.getSession().getServletContext().getRealPath(File.separator) + "META-INF" + File.separator + "properties" + File.separator + "sys.properties";
			File file = new File(path);
			if (!file.exists()) {
				return null;
			}
			try {
				InputStream in = new FileInputStream(file);
				prop.load(in);
				web = prop.getProperty("web").trim();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return web;
	}
	
	//获取项目路径
	private static String attachUrl = null;
	
	public static String getAttachUrl() {
		return attachUrl;
	}

	public static String getAttachUrl(HttpServletRequest request) {
		if (null == attachUrl ) {
			Properties prop = new Properties();
			String path = request.getSession().getServletContext().getRealPath(File.separator) + "META-INF" + File.separator + "properties" + File.separator + "sys.properties";
			File file = new File(path);
			if (!file.exists()) {
				return null;
			}
			try {
				InputStream in = new FileInputStream(file);
				prop.load(in);
				attachUrl = prop.getProperty("attach_url").trim();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return attachUrl;
	}
	
	//获取上传路径
	private static String attachPath = null;
	
	public static String getAttachPath() {
		return attachPath;
	}

	public static String getAttachPath(HttpServletRequest request) {
		if (null == attachPath ) {
			Properties prop = new Properties();
			String path = request.getSession().getServletContext().getRealPath(File.separator) + "META-INF" + File.separator + "properties" + File.separator + "sys.properties";
			File file = new File(path);
			if (!file.exists()) {
				return null;
			}
			try {
				InputStream in = new FileInputStream(file);
				prop.load(in);
				attachPath = prop.getProperty("attach_path").trim();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return attachPath;
	}
	
	//获取商户安全码
	private static String securityCode = null;
	
	public static String getSecurityCode() {
		return securityCode;
	}

	public static String getSecurityCode(HttpServletRequest request) {
		if (null == securityCode ) {
			Properties prop = new Properties();
			String path = request.getSession().getServletContext().getRealPath(File.separator) + "META-INF" + File.separator + "properties" + File.separator + "sys.properties";
			File file = new File(path);
			if (!file.exists()) {
				return null;
			}
			try {
				InputStream in = new FileInputStream(file);
				prop.load(in);
				securityCode = prop.getProperty("a").trim();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return securityCode;
	}
	
	/**
	 * 生成红包安全码，25位随机数
	 */
	public static String createSecurityCode(){
		int temp[] = new int[25];
		for (int i = 0; i < 25; i++) {
			temp[i] = (int) (Math.random() * 10);
		}
	
		String shareGive = "";
	
		for (int i = 0; i < 25; i++) {
			shareGive += temp[i];
		}
		
		return shareGive;
	}
	

	// 修改为开发者申请的appid
	public static String WXAPPID = null;
	public static String getWXAppId() {
		if (null == WXAPPID ) {
			String path = new File(Constant.class.getResource("\u002f").getPath())
			 .getParentFile().getParentFile().getPath();
			Properties prop = new Properties();
			path = path + File.separator + "META-INF" + File.separator + "properties" + File.separator + "sys.properties";
			File file = new File(path);
			if (!file.exists()) {
				return null;
			}
			try {
				InputStream in = new FileInputStream(file);
				prop.load(in);
				WXAPPID = prop.getProperty("WXAPPID").trim();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return WXAPPID;
	}
	
	// 修改为开发者申请的secret密钥
	public static String WXSECRET = null;
	public static String getWXSecret() {
		
		if (null == WXSECRET ) {
			String path = new File(Constant.class.getResource("\u002f").getPath())
			 .getParentFile().getParentFile().getPath();
			Properties prop = new Properties();
			path = path + File.separator + "META-INF" + File.separator + "properties" + File.separator + "sys.properties";
			File file = new File(path);
			if (!file.exists()) {
				return null;
			}
			try {
				InputStream in = new FileInputStream(file);
				prop.load(in);
				WXSECRET = prop.getProperty("WXSECRET").trim();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return WXSECRET;
	}


	/**
	 * 时间戳
	 */
	public static String createTimeStamp(Date date){
		StringBuilder sb = new StringBuilder();
		
		sb.append(date.getTime());
		return sb.toString();
	}
	
	//微信支付key
	public static String WXPAYKEY = null;
	public static String getWxPayKey() {
		if (null == WXPAYKEY ) {
			String path = new File(Constant.class.getResource("\u002f").getPath())
			 .getParentFile().getParentFile().getPath();
			Properties prop = new Properties();
			path = path + File.separator + "META-INF" + File.separator + "properties" + File.separator + "sys.properties";
			File file = new File(path);
			if (!file.exists()) {
				return null;
			}
			try {
				InputStream in = new FileInputStream(file);
				prop.load(in);
				WXPAYKEY = prop.getProperty("WXPAYKEY").trim();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return WXPAYKEY;
	}
	//微信支付商户号 
	public static String WXPARTNER= null;
	public static String getWxpartner() {
		if (null == WXPARTNER ) {
			String path = new File(Constant.class.getResource("\u002f").getPath())
			 .getParentFile().getParentFile().getPath();
			Properties prop = new Properties();
			path = path + File.separator + "META-INF" + File.separator + "properties" + File.separator + "sys.properties";
			File file = new File(path);
			if (!file.exists()) {
				return null;
			}
			try {
				InputStream in = new FileInputStream(file);
				prop.load(in);
				WXPARTNER = prop.getProperty("WXPARTNER").trim();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return WXPARTNER;
	}
	
	//数据库备份文件夹
	public static String BACKUP= null;
	public static String getBackup() {
		if (null == BACKUP ) {
			String path = new File(Constant.class.getResource("\u002f").getPath())
			 .getParentFile().getParentFile().getPath();
			Properties prop = new Properties();
			path = path + File.separator + "META-INF" + File.separator + "properties" + File.separator + "sys.properties";
			File file = new File(path);
			if (!file.exists()) {
				return null;
			}
			try {
				InputStream in = new FileInputStream(file);
				prop.load(in);
				BACKUP = prop.getProperty("attach_path").trim() + "/" + prop.getProperty("BACKUP").trim() + "/";
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return BACKUP;
	}
	
}
