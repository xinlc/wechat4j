package org.leo.wechat4j.wxmsg.msg;

import org.leo.wechat4j.wxmsg.WXXmlElementName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 语音
 */
public class Msg4Voice extends Msg{

	// 语音消息媒体id，可以调用多媒体文件下载接口拉取该媒体
	private String mediaId;
	// 语音格式：amr
	private String format;
	// 语音识别结果，UTF8编码
	private String recognition;
	// 消息id，64位整型
	private String msgId;
	
	/**
	 * 开发者调用
	 * */
	public Msg4Voice() {
		this.head = new Msg4Head();
		this.head.setMsgType(Msg.MSG_TYPE_VOICE);//设置消息类型
	}
	
	public Msg4Voice(Msg4Head head){
		this.head = head;
	}
	
	
	@Override
	public void write(Document document) {
		Element root = document.createElement(WXXmlElementName.ROOT);
		head.write(root, document);
		Element picUrlElement = document.createElement(WXXmlElementName.MEDIAID);
		picUrlElement.setTextContent(this.mediaId);
		Element voice = document.createElement(WXXmlElementName.VOICE);
		voice.appendChild(picUrlElement);
		root.appendChild(voice);
		document.appendChild(root);
	}

	
	@Override
	public void read(Document document) {
		this.mediaId = getElementContent(document, WXXmlElementName.MEDIAID);
		this.format = getElementContent(document, WXXmlElementName.FORMAT);
		this.recognition = getElementContent(document, WXXmlElementName.RECOGNITION);
		this.msgId = getElementContent(document, WXXmlElementName.MSG_ID);
	}


	/**
	 * @return the mediaId
	 */
	public String getMediaId() {
		return mediaId;
	}


	/**
	 * @param mediaId the mediaId to set
	 */
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}


	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}


	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}


	/**
	 * @return the recognition
	 */
	public String getRecognition() {
		return recognition;
	}


	/**
	 * @param recognition the recognition to set
	 */
	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}


	/**
	 * @return the msgId
	 */
	public String getMsgId() {
		return msgId;
	}


	/**
	 * @param msgId the msgId to set
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	
	
	
	
	
	
}
