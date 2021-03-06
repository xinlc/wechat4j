package org.leo.wechat4j.wxpay;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.ant.filters.StringInputStream;
import org.leo.wechat4j.wxpay.util.MD5Util;
import org.leo.wechat4j.wxpay.util.TenpayUtil;
import org.leo.wechat4j.wxpay.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import org.leo.wechat4j.wxmsg.util.HttpUtil;

/* 
 '微信支付服务器签名支付请求请求类 
 '============================================================================ 
 'api说明： 
 'init(app_id,app_key); 
 '初始化函数，默认给一些参数赋值，如cmdno,date等。 
 'setKey(key_)'设置商户密钥 
 'getLasterrCode(),获取最后错误号 
 'createMd5Sign(signParams);生成Md5签名 
 'genPackage(packageParams);获取package包 
 'sendPrepay(packageParams);提交预支付 
 'getDebugInfo(),获取debug信息 
 '============================================================================ 
 '*/  
public class RequestHandler {  
    /** 预支付网关url地址 */  
    private String gateUrl;  
    /** 查询支付通知网关URL */  
    private String notifyUrl;  
    /** 商户参数 */  
    private String appid;  
    private String appkey;  
    private String partnerkey;  
    private String key;  
    /** 请求的参数 */  
    @SuppressWarnings("rawtypes")
	private SortedMap parameters;  
    private String charset;  
    /** debug信息 */  
    private String debugInfo;  
    private String last_errcode;  
  
    private HttpServletRequest request;  
  
    private HttpServletResponse response;
  
    /** 
     * 初始构造函数。 
     *  
     * @return 
     */  
    @SuppressWarnings("rawtypes")
	public RequestHandler(HttpServletRequest request,  
            HttpServletResponse response) {  
        this.last_errcode = "0";  
        this.request = request;  
        this.response = response;  
        this.charset = "UTF-8";  
        this.parameters = new TreeMap();  
        // 验证notify支付订单网关  
        notifyUrl = "https://gw.tenpay.com/gateway/simpleverifynotifyid.xml";
        gateUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
          
    }  
  
    /** 
     * 初始化函数。 
     */  
    public void init(String app_id, String partner_key) {  
        this.last_errcode = "0";  
        this.debugInfo = "";  
        this.appid = app_id;
        this.partnerkey = partner_key;  
        this.key = partner_key;//原demo没有，手动加上  
    }  
  
    public void init() {  
    }  
  
    /** 
     * 获取最后错误号 
     */  
    public String getLasterrCode() {  
        return last_errcode;  
    }  
  
    /** 
     *获取入口地址,不包含参数值 
     */  
    public String getGateUrl() {  
        return gateUrl;  
    }  
  
    /** 
     * 获取参数值 
     *  
     * @param parameter 
     *            参数名称 
     * @return String 
     */  
    public String getParameter(String parameter) {  
        String s = (String) this.parameters.get(parameter);  
        return (null == s) ? "" : s;  
    }  
  
      
    //设置密钥  
    public void setKey(String key) {  
        this.partnerkey = key;  
    }  
    //设置微信密钥  
    public void  setAppKey(String key){  
        this.appkey = key;  
    }  
      
    // 特殊字符处理  
    public String UrlEncode(String src) throws UnsupportedEncodingException {  
        return URLEncoder.encode(src, this.charset).replace("+", "%20");  
    }  
  
    // 获取package的签名包  
    @SuppressWarnings("rawtypes")
	public String genPackage(SortedMap<String, String> packageParams)  
            throws UnsupportedEncodingException {  
        String sign = createSign(packageParams);  
  
        StringBuffer sb = new StringBuffer();  
        Set es = packageParams.entrySet();  
        Iterator it = es.iterator();  
        while (it.hasNext()) {  
            Map.Entry entry = (Map.Entry) it.next();  
            String k = (String) entry.getKey();  
            String v = (String) entry.getValue();  
            //sb.append(k + "=" + UrlEncode(v) + "&");  
            sb.append(k + "=" + v + "&");
        }
  
        // 去掉最后一个&  
        String packageValue = sb.append("sign=" + sign).toString();  
        System.out.println("packageValue=" + packageValue);  
        return packageValue;  
    }  
  
    /** 
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。 
     */  
    @SuppressWarnings("rawtypes")
	public String createSign(SortedMap<String, String> packageParams) {  
        StringBuffer sb = new StringBuffer();  
        Set es = packageParams.entrySet();  
        Iterator it = es.iterator();  
        while (it.hasNext()) {  
            Map.Entry entry = (Map.Entry) it.next();  
            String k = (String) entry.getKey();  
            String v = (String) entry.getValue();  
            if (null != v && !"".equals(v) && !"sign".equals(k)  
                    && !"key".equals(k)) {  
                sb.append(k + "=" + v + "&");  
            }  
        }  
        sb.append("key=" + this.getKey());
        /*String sbs = sb.toString();
        if(sbs.endsWith("&")){
        	sbs = sbs.substring(0,sbs.lastIndexOf("&"));
        }*/
        System.out.println("md5 sb:" + sb);  
        String sign = MD5Util.MD5Encode(sb.toString(), this.charset)
                .toUpperCase();
  
        return sign;  
    }
    
    /** 
     * 创建package签名 
     */  
    @SuppressWarnings("rawtypes")
	public boolean createMd5Sign(String signParams) {  
        StringBuffer sb = new StringBuffer();  
        Set es = this.parameters.entrySet();  
        Iterator it = es.iterator();  
        while (it.hasNext()) {  
            Map.Entry entry = (Map.Entry) it.next();  
            String k = (String) entry.getKey();  
            String v = (String) entry.getValue();  
            if (!"sign".equals(k) && null != v && !"".equals(v)) {  
                sb.append(k + "=" + v + "&");  
            }  
        }  
  
        // 算出摘要  
        String enc = TenpayUtil.getCharacterEncoding(this.request,
                this.response);  
        String sign = MD5Util.MD5Encode(sb.toString(), enc).toLowerCase();  
  
        String tenpaySign = this.getParameter("sign").toLowerCase();  
  
        // debug信息  
        this.setDebugInfo(sb.toString() + " => sign:" + sign + " tenpaySign:"  
                + tenpaySign);  
  
        return tenpaySign.equals(sign);  
    }  
  
      
  
    //输出XML  
    @SuppressWarnings("rawtypes")
	public String parseXML() {  
       StringBuffer sb = new StringBuffer();  
       sb.append("<xml>");  
       Set es = this.parameters.entrySet();  
       Iterator it = es.iterator();  
       while(it.hasNext()) {  
            Map.Entry entry = (Map.Entry)it.next();  
            String k = (String)entry.getKey();  
            String v = (String)entry.getValue();  
            if(null != v && !"".equals(v) && !"appkey".equals(k)) {  
                  
                sb.append("<" + k +">" + getParameter(k) + "</" + k + ">\n");  
            }  
        }  
       sb.append("</xml>");  
       return sb.toString();  
    }  
  
    /** 
     * 设置debug信息 
     */  
    protected void setDebugInfo(String debugInfo) {  
        this.debugInfo = debugInfo;  
    }  
    public void setPartnerkey(String partnerkey) {  
        this.partnerkey = partnerkey;  
    }  
    public String getDebugInfo() {  
        return debugInfo;  
    }  
    public String getKey() {  
        return key;  
    }  
    
    /**
     * 提交预支付
     * @return
     */
    public String sendPrepay(String param){
    	//生成xml
    	Document document = XMLUtil.builder.newDocument();
    	Element root = document.createElement("xml");
    	String [] params = param.split("&");
    	for (int i = 0; i < params.length; i++) {
			String [] p = params[i].split("=");
			Element contentElement = document.createElement(p[0]);
			contentElement.setTextContent(p[1]);
			root.appendChild(contentElement);
		}
    	document.appendChild(root);
    	String doms = XMLUtil.toStringFromDoc(document);
    	//提交预支付
    	String result = HttpUtil.sendHttpsPOST(gateUrl, doms);
    	java.io.InputStream is = new StringInputStream(result);
    	String prepayId = null;
    	try {
			Document readdom = XMLUtil.builder.parse(is);
			prepayId = readdom.getElementsByTagName("prepay_id").item(0).getTextContent();  //预支付交易会话标识
			//readdom.getElementsByTagName("code_url").item(0).getTextContent();   //二维码链接
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	if(prepayId == null){
    		System.out.println("预支付提交失败:" + result);
    	}
    	return prepayId;
    }
  
} 