package org.leo.wechat4j.wxmsg.template;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import org.leo.wechat4j.util.Constant;
import org.leo.wechat4j.wxmsg.util.Constants;
import org.leo.wechat4j.wxmsg.util.HttpUtil;

/**
 * 发送模板消息
 * @author Leo
 * @create 2018-08-03
 */
public class TemplateMsg {
	
	/**
	 * 取消订单提醒
	 * @param accessToken
	 * @param opeId
	 * @param orderNo
	 * @param name
	 * @param num
	 * @param price
	 * @param time
	 * @param tel
	 */
	public static void sendCancelOrderMsg(String accessToken, String opeId, String orderNo,String name,
			String num,String price,String time,String tel){
		
		WxTemplate t = new WxTemplate();   //创建模板
		
		//模板 head
		t.setUrl(Constant.getProjectUrl() + "mIndex/orderDetails?code="+orderNo);
		t.setTouser(opeId);
		t.setTopcolor("#FF0000");
		t.setTemplate_id(Constant.getCancelOrderTemID());
		
		//模板content
		Map<String,TemplateData> map = new HashMap<String,TemplateData>();
		TemplateData firstData = new TemplateData();
		firstData.setColor("#000000");
		firstData.setValue("您有一笔订单已经生成但尚未支付，请尽快到“我的订单”支付。");
		map.put("first", firstData);
		
		TemplateData orderNoData = new TemplateData();  //订单号
		orderNoData.setColor("#000000");
		orderNoData.setValue(orderNo);
		map.put("keyword1", orderNoData);
		
		TemplateData nameData = new TemplateData();  //商品名称
		nameData.setColor("#000000");
		nameData.setValue(name);
		map.put("keyword2", nameData);
		
		TemplateData numData = new TemplateData();  //商品数量
		numData.setColor("#000000");
		numData.setValue(num);
		map.put("keyword3", numData);
		
		TemplateData priceData = new TemplateData();  //支付金额
		priceData.setColor("#000000");
		priceData.setValue(price);
		map.put("keyword4", priceData);
		
		//模板footer
		TemplateData remarkData = new TemplateData();
		remarkData.setColor("#000000");
		remarkData.setValue("订单"+time+"分钟后自动取消，若有疑虑请拨打客服热线"+tel+"。");
		map.put("remark", remarkData);
		
		t.setData(map);
		
		HttpUtil.sendHttpsPOST(Constants.WEIXIN_API.TEMPLATE_SEND + "access_token=" + accessToken, JSONObject.fromObject(t).toString());
		
		/*{{first.DATA}}
		订单号码：{{keyword1.DATA}}
		商品名称：{{keyword2.DATA}}
		商品数量：{{keyword3.DATA}}
		支付金额：{{keyword4.DATA}}
		{{remark.DATA}}
		
		您有一笔订单已经生成但尚未支付，请尽快到“我的订单”支付。
		订单号码:20140815888
		商品名称:钟姐杏仁牛轧糖
		商品数量:4盒
		支付金额:100元
		订单8小时后自动取消，若有疑虑请拨打客服热线4008859059。*/
	}
	
	public static void main(String[] args) {
		TemplateMsg.sendCancelOrderMsg("IS_kvk89kX2IPq6Sbc-eLYsH_mXRa9DynqPQO7EQGBtEjZepocb3Q0JxicmR7Y7ptai2WEUIiR_KcvSEpb659AjWld-uP0xX-o1Ge5Ng8aM",
				"oSGykwH71--Bgwc7P5lwsPM2Wzo8", "12345678932165", "测试商品测试商品测试商品测试商品测试商品测试商品测试商品", "4", "2000.0", "30", "4008859059");
	}
}
