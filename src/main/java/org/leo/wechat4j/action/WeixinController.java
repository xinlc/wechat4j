package org.leo.wechat4j.action;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.leo.wechat4j.util.Constant;
import org.leo.wechat4j.vo.AutoReply;
import org.leo.wechat4j.vo.PicText;
import org.leo.wechat4j.vo.PicTextName;
import org.leo.wechat4j.wxmsg.DefaultSession;
import org.leo.wechat4j.wxmsg.HandleMessageAdapter;
import org.leo.wechat4j.wxmsg.MySecurity;
import org.leo.wechat4j.wxmsg.Session;
import org.leo.wechat4j.wxmsg.msg.*;
import org.leo.wechat4j.wxmsg.util.HttpUtil;
import org.leo.wechat4j.wxmsg.util.WeiXinUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 微信接口
 * @author Leo
 * @create 2018-08-03
 */
@Controller
@RequestMapping("/Weixin")
public class WeixinController {
	
	public static final String TOKEN = "text123";      //微信后台配置 验证 token

	@Resource
	private WeixinTicketService weixinTicketService;   //微信ticket
	
	@Resource
	private AutoReplyService autoReplyService;       //自动回复
	
	@Resource
	private PicTextNameService picTextNameService;   //图文


	//服务器认证
	@RequestMapping(method = RequestMethod.GET)
	public String weixinGet(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		String signature = request.getParameter("signature");// 微信加密签名
		String timestamp = request.getParameter("timestamp");// 时间戳
		String nonce = request.getParameter("nonce");// 随机数
		String echostr = request.getParameter("echostr");// 随机字符串
		if(signature!=null){
			
			// 重写totring方法，得到三个参数的拼接字符串
			List<String> list = new ArrayList<String>(3) {
			private static final long serialVersionUID = 2621444383666420433L;
				public String toString() {
					return this.get(0) + this.get(1) + this.get(2);
				}
			};
			list.add(TOKEN);
			list.add(timestamp);
			list.add(nonce);
			Collections.sort(list);// 排序
			String tmpStr = new MySecurity().encode(list.toString(),
					MySecurity.SHA_1);// SHA-1加密		
			Writer out = response.getWriter();
			if (signature.equals(tmpStr)) {
				out.write(echostr);// 请求验证成功，返回随机码
			} else {
				out.write("");
			}
			out.flush();
			out.close();
		}
		return null;
	}
	
	//oauth2 权限认证
	@RequestMapping(method = RequestMethod.GET,value="/weixinBaseInfo")
	public String weixinBaseInfo(HttpServletRequest request,Model model) throws Exception {
		//微信不退出不会清除session,每次重新进入清除session
		request.getSession().invalidate();
		
		String code = request.getParameter("code");
		try {
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+Constant.getWXAppId() +"&secret="+Constant.getWXSecret() + "&code="+code + "&grant_type=authorization_code";
			String result = HttpUtil.sendHttpsGET(url,null);
			JSONObject obj = JSON.parseObject(result);
			String openid = obj.getString("openid");
			model.addAttribute("openId", openid);
			getUserInfo(request,openid);
		} catch (Exception e) {
			java.lang.System.out.println("weixinBaseInfo位置异常！！！！！！！");
			e.printStackTrace();
		}
		
		//1 为 跳转index ，2 个人中心  , 3  我的订单 , 4 我的足迹，5 我的收藏
		String state = request.getParameter("state");
		model.addAttribute("redirectState", state);
		
		return "redirect:/mIndex";
	}
	
	//获取用户信息
	private void getUserInfo(HttpServletRequest request,String openId) throws Exception{
		if(openId != null && !"".equals(openId)){
			String lang = "zh_CN";
			String accessToken = WeiXinUtil.getWXAccessToken(request, weixinTicketService, null);
			String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openId+"&lang="+lang;
			String result = HttpUtil.sendHttpsGET(url,"UTF-8");
			JSONObject obj = JSON.parseObject(result);
			String imgUrl = obj.getString("headimgurl");
			request.getSession().setAttribute("wxUserHeadImg", imgUrl);
		}
	}
	
	//微信回调消息处理
	@RequestMapping(method = RequestMethod.POST)
	public String weixinPost(final HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		InputStream is  = request.getInputStream();
		OutputStream os = response.getOutputStream();
		final DefaultSession session = DefaultSession.newInstance();
		
		//客服
		session.addOnHandleMessageListener(new HandleMessageAdapter(){
			@Override
			public void onCustomerMsg(Msg4Customer msg) {
				Msg4Customer reMsg = new Msg4Customer();
				reMsg.setFromUserName(msg.getToUserName());
				reMsg.setToUserName(msg.getFromUserName());
				reMsg.setCreateTime(msg.getCreateTime());
				session.callback(reMsg);//传回到多客服系统中
			}
		});
		
		//接收消息-文本
		session.addOnHandleMessageListener(new HandleMessageAdapter(){
			@Override
			public void onTextMsg(Msg4Text msg) {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("key", msg.getContent());
				autoReply(map,msg,session);
			}
		});
		
		//接收消息-图片
		session.addOnHandleMessageListener(new HandleMessageAdapter(){
			@Override
			public void onImageMsg(Msg4Image msg) {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("key", msg.getPicUrl());
				autoReply(map,msg,session);
			}
		});
		
		//语音识别消息
		session.addOnHandleMessageListener(new HandleMessageAdapter(){ 
			@Override
			public void onVoiceMsg(Msg4Voice msg) {
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("key", msg.getRecognition());
				autoReply(map,msg,session);
			}
		});
		
		// 处理事件
		session.addOnHandleMessageListener(new HandleMessageAdapter() {
			public void onEventMsg(Msg4Event msg) {
				String eventType = msg.getEvent();
				if(Msg4Event.VIEW.equals(eventType)){// 点击事件
					//System.out.println("用户："+ msg.getFromUserName());
					//System.out.println("点击Key："+msg.getEventKey());
					
				}else if(Msg4Event.SUBSCRIBE.equalsIgnoreCase(eventType)){// 订阅
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("event",1);//1、订阅 2、取消订阅 3、菜单单击事件
					autoReply(map,msg,session);
				}else if(Msg4Event.UNSUBSCRIBE.equalsIgnoreCase(eventType)){// 取消订阅
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("event",2);//1、订阅 2、取消订阅 3、菜单单击事件
					autoReply(map,msg,session);
					
				}else if(Msg4Event.CLICK.equals(eventType)){// 点击事件
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("eventKey", msg.getEventKey());
					//map.put("event",3); //3、菜单单击事件
					autoReply(map,msg,session);
				}
			}
		});
		
		// 处理地理位置
		session.addOnHandleMessageListener(new HandleMessageAdapter(){
			public void onLocationMsg(Msg4Location msg) {
				System.out.println("收到地理位置消息：");
				System.out.println("X:"+msg.getLocation_X());
				System.out.println("Y:"+msg.getLocation_Y());
				System.out.println("Scale:"+msg.getScale());
			}
		});
		session.process(is, os);//处理微信消息 
		session.close();//关闭Session
		return null;
	}
	
	/**
	 * 自动回复
	 * @param Map
	 * @param Msg
	 * @param Session
	 * @throws Exception 
	 */
	private void autoReply (Map<String,Object> ConditionMap,Msg msg,Session session){
		
		List<AutoReply> autoReplys = autoReplyService.getAutoReplysByCondition(); //获取自动回复信息
		AutoReply autoReply = null;
		if(autoReplys.size() > 0){ //有回复信息
			autoReply = autoReplys.get(0);
		}
		if(autoReply != null){
			if(autoReply.getType() == 1){  //1、文本 2、图片 3、语音 4、视频 5、图文
				Msg4Text reMsg = new Msg4Text();
				reMsg.setFromUserName(msg.getToUserName());
				reMsg.setToUserName(msg.getFromUserName());
				reMsg.setCreateTime(msg.getCreateTime());
				reMsg.setContent(autoReply.getContent());
				session.callback(reMsg);//回传文本
				
			}else if(autoReply.getType() == 2){ //图片 
				Msg4Image reMsg = new Msg4Image();
				reMsg.setFromUserName(msg.getToUserName());
				reMsg.setToUserName(msg.getFromUserName());
				reMsg.setCreateTime(msg.getCreateTime());
				reMsg.setMediaId(autoReply.getContent());
				session.callback(reMsg);
				
			}else if(autoReply.getType() == 3){//语音
				Msg4Voice reMsg = new Msg4Voice();
				reMsg.setFromUserName(msg.getToUserName());
				reMsg.setToUserName(msg.getFromUserName());
				reMsg.setCreateTime(msg.getCreateTime());
				reMsg.setMediaId(autoReply.getContent());
				session.callback(reMsg);
				
			}else if(autoReply.getType() == 4){//视频
				Msg4Video reMsg = new Msg4Video();
				reMsg.setFromUserName(msg.getToUserName());
				reMsg.setToUserName(msg.getFromUserName());
				reMsg.setCreateTime(msg.getCreateTime());
				reMsg.setMediaId(autoReply.getContent());
				//reMsg.setTitle(title);   //视频标题  可省略
				//reMsg.setDescription(description);  //视频描述 可省略
				session.callback(reMsg);
				
			}else if(autoReply.getType() == 5){//图文
				
				Map<String, Object> map = new HashMap<String, Object>();
				Msg4ImageText reMsg = new Msg4ImageText();  //图文消息
				reMsg.setFromUserName(msg.getToUserName());
				reMsg.setToUserName(msg.getFromUserName());
				reMsg.setCreateTime(msg.getCreateTime());
				int picTextNameId = 0;
				if(autoReply.getContent()!=null && !"".equals(autoReply.getContent())){
					picTextNameId = Integer.parseInt(autoReply.getContent());
				}
				map.put("id", picTextNameId);
				PicTextName picTextName = picTextNameService.getPicTextNameById(map);  //根据图文名称获取图文名称
				List<PicText> picTexts = null;
				if(picTextName != null){
					picTexts = picTextName.getPicTexts();  //获取图文
				}
				for (PicText picText : picTexts) {
					Data4Item item = new Data4Item();   //图文消息对象
					item.setDescription(picText.getDescription());
					item.setTitle(picText.getTitle());
					item.setUrl(picText.getUrl());
					item.setPicUrl(picText.getPicUrl());
					reMsg.addItem(item); //添加图文消息对象
				}
				session.callback(reMsg);//回传图文
			}
		}else{
			Msg4Text reMsg = new Msg4Text();
			reMsg.setFromUserName(msg.getToUserName());
			reMsg.setToUserName(msg.getFromUserName());
			reMsg.setCreateTime(msg.getCreateTime());
			reMsg.setContent("没有找到您想要的信息！");
			session.callback(reMsg);//回传文本
		}
	}
}
