package org.leo.wechat4j.wxmsg.template;

/**
 * 模板数据
 * @author Leo
 * @create 2018-08-03
 *
 */
public class TemplateData {
	
	private String value;   //值
	
	private String color;  //字体颜色 16进制

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
