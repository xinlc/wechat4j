package org.leo.wechat4j.wxmsg.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *  HttpServlet帮助工具类
 */
public class HttpServletHelper {
	/**
	 * 读取请求输入流
	 * @param obj java.util.Map and javax.servlet.http.HttpServletRequest
	 * @return Map<String, String>
	 * @throws Exception
	 */
	public static String read(HttpServletRequest request,String charsetName) throws Exception {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		InputStream instream = null;
		try {
			int c = 0;
			instream = request.getInputStream();
			while ((c = instream.read()) != -1) {
				buffer.write(c);
			}
			return new String(buffer.toByteArray(), charsetName);			
		} finally {
			if (buffer != null) {
				buffer.close();
				buffer = null;
			}
			if (instream != null) {
				instream.close();
				instream = null;
			}
		}
	}
	
	/**
	 * 读取请求输入流
	 * @param obj java.util.Map and javax.servlet.http.HttpServletRequest
	 * @return Map<String, String>
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, Object> read(Object obj) throws Exception {
		if (obj == null) {
			throw new RuntimeException("请求对象为NULL");
		}
		if ((obj instanceof Map))
			return ((Map) obj);
		if ((obj instanceof HttpServletRequest)) {
			HttpServletRequest request = (HttpServletRequest) obj;
			Map<String, Object> fieldMap = new HashMap<String, Object>();
			Enumeration names = request.getParameterNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				String values = request.getParameter(name);
				if (null != values)
					values = values.trim();
				fieldMap.put(name, values);
			}
			return fieldMap;
		}
		throw new Exception("数据集合只支持java.util.Map 和 javax.servlet.http.HttpServletRequest");
	}
	
	public static void write(HttpServletResponse response, String text,String charsetName) throws Exception {
		OutputStream outstream = null;
		try {
			outstream = response.getOutputStream();
			outstream.write(text.getBytes(charsetName));
		} catch (Exception e) {
		} finally {
			if (outstream != null) {
				outstream.flush();
				outstream.close();
				outstream = null;
			}
		}
	}
	
	/**
	* 根据类型从Spring中取得Bean
	*
	* @param <T>    Bean的类型
	* @param beanClass    Bean的Class对象
	* @param servletContext   
	* @return
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T getSpringBean(Class<T> beanClass,
	        ServletContext servletContext) {
		try {
			WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			Map beans = appContext.getBeansOfType(beanClass);
		    if (beans == null || beans.isEmpty()) {
		        return null;
		    }
		    return (T) beans.values().toArray()[0];
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public static int getParamInt(HttpServletRequest request,String name){
		String v = request.getParameter(name);
		return (v!=null)?Integer.parseInt(v):0;
	}
	public static int getParamInt(HttpServletRequest request,String name,int def){
		String v = request.getParameter(name);
		return (v!=null)?Integer.parseInt(v):def;
	}	
}
