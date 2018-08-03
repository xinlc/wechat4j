package org.leo.wechat4j.wxmsg.template;

import java.util.Map;

/**
 * 模板
 * @author Leo
 * @create 2018-08-03
 */
public class WxTemplate {

	private String template_id;   //模板id
	
	private String touser;		  //openid
	
	private String url;           //点击跳转 url
	
	private String topcolor;      //模板顶部颜色   16进制
	
	private Map<String,TemplateData> data;  //模板数据

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTopcolor() {
		return topcolor;
	}

	public void setTopcolor(String topcolor) {
		this.topcolor = topcolor;
	}

	public Map<String, TemplateData> getData() {
		return data;
	}

	public void setData(Map<String, TemplateData> data) {
		this.data = data;
	}
}
