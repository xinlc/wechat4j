package org.leo.wechat4j.wxmsg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.tools.ant.filters.StringInputStream;
import org.leo.wechat4j.wxmsg.msg.Msg;
import org.leo.wechat4j.wxmsg.msg.Msg4Customer;
import org.leo.wechat4j.wxmsg.msg.Msg4Video;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import org.leo.wechat4j.wxmsg.msg.Msg4Event;
import org.leo.wechat4j.wxmsg.msg.Msg4Head;
import org.leo.wechat4j.wxmsg.msg.Msg4Image;
import org.leo.wechat4j.wxmsg.msg.Msg4Link;
import org.leo.wechat4j.wxmsg.msg.Msg4Location;
import org.leo.wechat4j.wxmsg.msg.Msg4Text;
import org.leo.wechat4j.wxmsg.msg.Msg4Voice;


/**
 * 抽象会话
 * 此会话声明周期在一个请求响应内。
 * 通过继承类实现各种消息的处理方法
 * @author Leo
 * @create 2018-08-03
 *
 * */
public abstract class Session {

	/** 时间格式化 */
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	
	//输入流
	private InputStream is;
	//输出流
	private OutputStream os;
	
	/** Document构建类 */
	private static DocumentBuilder builder;
	private static TransformerFactory tffactory;
	  
	static{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		//格式化工厂对象
		tffactory = TransformerFactory.newInstance();
	}
	
	/**
	 * Session 
	 * 
	 */
	public Session() { }


	
	/**
	 * 解析微信消息，并传递给对应方法
	 * @param is 输入流
	 * @param os 输出流
	 */
	public void process(InputStream is, OutputStream os) {
		this.os = os;
		try {
			int flag = 1;  //标识 是否读取异常
			StringBuffer sb = new StringBuffer();
			try {
				int i = -1;
				byte[] b = new byte[1024];
				
				while ((i = is.read(b)) != -1) {
				    sb.append(new String(b, 0, i));
				}
			} catch (Exception e) {
				flag = 0;
				e.printStackTrace();
			}
			if(flag == 1){//如果没有异常 在转成InputStream
				is = new StringInputStream(sb.toString());
			}
	        
			Document document = builder.parse(is);
			Msg4Head head = new Msg4Head();
			head.read(document);
			String type = head.getMsgType();
			System.out.println("Session:type" + type);
			if(Msg.MSG_TYPE_TEXT.equals(type)){//文本消息
				Msg4Text msg = new Msg4Text(head);
				msg.read(document);
				this.onTextMsg(msg);
			}else if(Msg.MSG_TYPE_IMAGE.equals(type)){//图片消息
				Msg4Image msg = new Msg4Image(head);
				msg.read(document);
				this.onImageMsg(msg);
			}else if(Msg.MSG_TYPE_EVENT.equals(type)){//事件推送
				Msg4Event msg = new Msg4Event(head);
				msg.read(document);
				this.onEventMsg(msg);
			}else if(Msg.MSG_TYPE_LINK.equals(type)){//链接消息
				Msg4Link msg = new Msg4Link(head);
				msg.read(document);
				this.onLinkMsg(msg);
			}else if(Msg.MSG_TYPE_LOCATION.equals(type)){//地理位置消息
				Msg4Location msg = new Msg4Location(head);
				msg.read(document);
				this.onLocationMsg(msg);
			}else if(Msg.MSG_TYPE_VOICE.equals(type)){
				Msg4Voice msg = new Msg4Voice(head);
				msg.read(document);
				this.onVoiceMsg(msg);
			}else if(Msg.MSG_TYPE_VIDEO.equals(type)){
				Msg4Video msg = new Msg4Video(head);
				msg.read(document);
				this.onVideoMsg(msg);
			}else{
				this.onErrorMsg(-1);//这里暂时这样处理的
			}
			
			//全部转发到多客服
			if(Msg.MSG_TYPE_TEXT.equals(type) || Msg.MSG_TYPE_IMAGE.equals(type) || Msg.MSG_TYPE_LINK.equals(type) || Msg.MSG_TYPE_LOCATION.equals(type)
					|| Msg.MSG_TYPE_VOICE.equals(type) || Msg.MSG_TYPE_VIDEO.equals(type)){
				
				Msg4Customer msg = new Msg4Customer(head);
				msg.read(document);
				this.onCustomerMsg(msg);
			}

			
		} catch (SAXException e) { 
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}


	/**
	 * 回传消息给微信服务器
	 * 只能再接收到微信服务器消息后，才能调用此方法
	 * @param msg 消息对象（支持：文本、音乐、图文）
	 * */
	public void callback(Msg msg){
		Document document = builder.newDocument();
		msg.write(document);
		//System.out.println(toStringFromDoc(document));//打印XML
		try {
			Transformer transformer = tffactory.newTransformer();
			
			;
			transformer.transform(new DOMSource(document), new StreamResult(new OutputStreamWriter(os,"utf-8")));
		} catch ( Exception e) { 
			e.printStackTrace();// 保存dom至目输出流
		}
	}
	
	
	/**
	 * 关闭
	 * */
	public void close(){
		try {
			if(is != null){
				is.close();
			}
			if(os != null){
				os.flush();
				os.close();	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 收到文本消息
	 * @param msg
	 */
	public abstract void onTextMsg(Msg4Text msg);
	
	/**
	 * 收到图片消息
	 * @param msg
	 */
	public abstract void onImageMsg(Msg4Image msg);
	
	/**
	 * 收到事件推送消息
	 * @param msg
	 */
	public abstract void onEventMsg(Msg4Event msg);
	
	/**
	 * 收到链接消息
	 * @param msg
	 */
	public abstract void onLinkMsg(Msg4Link msg);
	
	/**
	 * 收到地理位置消息
	 * @param msg
	 */
	public abstract void onLocationMsg(Msg4Location msg);
	
	/**
	 * 收到语音识别消息
	 * @param msg
	 */
	public abstract void onVoiceMsg(Msg4Voice msg);
	

	/**
	 * 收到视频消息
	 * @param msg
	 */
	public abstract void onVideoMsg(Msg4Video msg);
	
	/**
	 * 多客服
	 * @param msg
	 */
	public abstract void onCustomerMsg(Msg4Customer msg);
	
	/**
	 * 错误消息
	 * @param msg
	 */
	public abstract void onErrorMsg(int errorCode);
	
	
	/*  
     * 把dom文件转换为xml字符串  
     */  
    public static String toStringFromDoc(Document document) {  
        String result = null;  
  
        if (document != null) {  
            StringWriter strWtr = new StringWriter();  
            StreamResult strResult = new StreamResult(strWtr);  
            TransformerFactory tfac = TransformerFactory.newInstance();  
            try {  
                javax.xml.transform.Transformer t = tfac.newTransformer();  
                t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");  
                t.setOutputProperty(OutputKeys.INDENT, "yes");  
                t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,  
                // text  
                t.setOutputProperty(  
                        "{http://xml.apache.org/xslt}indent-amount", "4");  
                t.transform(new DOMSource(document.getDocumentElement()),  
                        strResult);  
            } catch (Exception e) {  
                System.err.println("XML.toString(Document): " + e);  
            }  
            result = strResult.getWriter().toString();  
            try {  
                strWtr.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
  
        return result;  
    }  

}
