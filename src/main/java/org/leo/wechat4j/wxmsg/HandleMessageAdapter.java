package org.leo.wechat4j.wxmsg;

import org.leo.wechat4j.wxmsg.msg.Msg4Customer;
import org.leo.wechat4j.wxmsg.msg.Msg4Event;
import org.leo.wechat4j.wxmsg.msg.Msg4Image;
import org.leo.wechat4j.wxmsg.msg.Msg4Link;
import org.leo.wechat4j.wxmsg.msg.Msg4Location;
import org.leo.wechat4j.wxmsg.msg.Msg4Text;
import org.leo.wechat4j.wxmsg.msg.Msg4Video;
import org.leo.wechat4j.wxmsg.msg.Msg4Voice;


/**
 * 处理消息适配器(适配器模式)
 * */
public class HandleMessageAdapter implements HandleMessageListener {

	public void onTextMsg(Msg4Text msg) {
		// TODO Auto-generated method stub
		
	}

	public void onImageMsg(Msg4Image msg) {
		// TODO Auto-generated method stub
		
	}

	public void onEventMsg(Msg4Event msg) {
		// TODO Auto-generated method stub
		
	}

	public void onLinkMsg(Msg4Link msg) {
		// TODO Auto-generated method stub
		
	}

	public void onLocationMsg(Msg4Location msg) {
		// TODO Auto-generated method stub
		
	}

	public void onErrorMsg(int errorCode) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.marker.weixin.HandleMessageListener#onVoiceMsg(org.marker.weixin.msg.Msg4Voice)
	 */
	public void onVoiceMsg(Msg4Voice msg) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.marker.weixin.HandleMessageListener#onVideoMsg(org.marker.weixin.msg.Msg4Video)
	 */
	public void onVideoMsg(Msg4Video msg) {
		// TODO Auto-generated method stub
		
	}

	public void onCustomerMsg(Msg4Customer msg) {
		// TODO Auto-generated method stub
		
	}

}
