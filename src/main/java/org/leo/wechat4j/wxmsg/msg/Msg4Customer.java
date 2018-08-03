package org.leo.wechat4j.wxmsg.msg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.leo.wechat4j.wxmsg.WXXmlElementName;

public class Msg4Customer extends Msg {

	private String msgId;
	
	/**
	 * 默认构造
	 * 初始化head对象，主要由开发者调用
	 * */
	public Msg4Customer() {
		this.head = new Msg4Head();
		this.head.setMsgType(Msg.MSG_CUSTOMER);//设置消息类型
	}
	
	/**
	 * 此构造由程序内部接收微信服务器消息调用
	 * */
	public Msg4Customer(Msg4Head head) {
		this.head = head;
	}
	
	@Override
	public void write(Document document) {
		Element root = document.createElement(WXXmlElementName.ROOT);
		head.write(root, document);
		document.appendChild(root);
	}

	@Override
	public void read(Document document) {
		this.msgId = document.getElementsByTagName(WXXmlElementName.MSG_ID).item(0).getTextContent();
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}


}
