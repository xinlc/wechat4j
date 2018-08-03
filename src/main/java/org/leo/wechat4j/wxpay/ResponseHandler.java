package org.leo.wechat4j.wxpay;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jdom.JDOMException;

import org.leo.wechat4j.wxpay.util.MD5Util;
import org.leo.wechat4j.wxpay.util.Sha1Util;
import org.leo.wechat4j.wxpay.util.TenpayUtil;
import org.leo.wechat4j.wxpay.util.XMLUtil;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

/** 
 * 微信支付服务器签名支付请求应答类 api说明： getKey()/setKey(),获取/设置密钥 
 * getParameter()/setParameter(),获取/设置参数值 getAllParameters(),获取所有参数 
 * isTenpaySign(),是否财付通签名,true:是 false:否 getDebugInfo(),获取debug信息 
 */  
public class ResponseHandler {  
  
    private static final String appkey = "";  
    /** 密钥 */  
    private String key;  
  
    /** 应答的参数 */  
    @SuppressWarnings("rawtypes")
	private SortedMap parameters;  
  
    /** debug信息 */  
    private String debugInfo;  
  
    private HttpServletRequest request;  
  
    private HttpServletResponse response;  
  
    private String uriEncoding;  
      
    @SuppressWarnings("rawtypes")
	private SortedMap smap;  
  
    @SuppressWarnings("rawtypes")
	public SortedMap getSmap() {  
        return smap;  
    }  
  
    private String k;  
  
    /** 
     * 构造函数 
     *  
     * @param request 
     * @param response 
     */  
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseHandler(HttpServletRequest request,  
            HttpServletResponse response) {  
        this.request = request;  
        this.response = response;  
        this.smap = new TreeMap<String, String>();  
        this.key = "";  
        this.parameters = new TreeMap();  
        this.debugInfo = "";  
  
        this.uriEncoding = "";  
          
        /*Map m = this.request.getParameterMap();  
        Iterator it = m.keySet().iterator();  
        while (it.hasNext()) {  
            String k = (String) it.next();  
            String v = ((String[]) m.get(k))[0];  
            System.out.println("微信支付返回参数11： "+k + " \t -> \t" +v);
            this.setParameter(k, v); 
        }  */
        BufferedReader reader  =null;  
        try{  
            reader = new BufferedReader(new InputStreamReader(request.getInputStream()));      
            StringBuilder sb = new StringBuilder();      
            String line = null;      
            while ((line = reader.readLine()) != null) {      
                sb.append(line);      
            }  
            Document doc = DocumentHelper.parseText(sb.toString());  
            Element root = doc.getRootElement();   
            for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {  
                Element e = (Element) iterator.next();    
                smap.put(e.getName(), e.getText());  
                this.setParameter(e.getName(), e.getText());  //保存微信参数
                //System.out.println("微信支付返回参数11： "+e.getName() + " \t -> \t" +e.getText());
            }  
        }catch (Exception e) {  
            e.printStackTrace();  
        }  
          
    }  
  
    /** 
     * 获取密钥 
     */  
    public String getKey() {  
        return key;  
    }  
  
    /** 
     * 设置密钥 
     */  
    public void setKey(String key) {  
        this.key = key;  
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
  
    /** 
     * 设置参数值 
     *  
     * @param parameter 
     *            参数名称 
     * @param parameterValue 
     *            参数值 
     */  
    @SuppressWarnings("unchecked")
	public void setParameter(String parameter, String parameterValue) {  
        String v = "";  
        if (null != parameterValue) {  
            v = parameterValue.trim();  
        }  
        this.parameters.put(parameter, v);  
    }  
  
    /** 
     * 返回所有的参数 
     *  
     * @return SortedMap 
     */  
    @SuppressWarnings("rawtypes")
	public SortedMap getAllParameters() {  
        return this.parameters;  
    }  
  
    @SuppressWarnings("rawtypes")
	public void doParse(String xmlContent) throws JDOMException, IOException {  
        this.parameters.clear();  
        // 解析xml,得到map  
        Map m = XMLUtil.doXMLParse(xmlContent);
  
        // 设置参数  
        Iterator it = m.keySet().iterator();  
        while (it.hasNext()) {  
            String k = (String) it.next();  
            String v = (String) m.get(k);  
            this.setParameter(k, v);  
        }  
    }  
  
    /** 
     * 是否财付通签名,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。 
     *  
     * @return boolean 
     */  
    @SuppressWarnings("rawtypes")
	public boolean isValidSign() {  
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
  
        sb.append("key=" + this.getKey());  
  
        // 算出摘要  
        String enc = TenpayUtil.getCharacterEncoding(this.request,
                this.response);  
        String sign = MD5Util.MD5Encode(sb.toString(), enc).toLowerCase();
  
        String ValidSign = this.getParameter("sign").toLowerCase();  
  
        // debug信息  
        this.setDebugInfo(sb.toString() + " => sign:" + sign + " ValidSign:"  
                + ValidSign);  
        //System.out.println("财付通签名："+this.getDebugInfo());  
  
        return ValidSign.equals(sign);  
    }  
  
    /** 
     * 判断微信签名 
     */  
    @SuppressWarnings("rawtypes")
	public boolean isWXsign() {  
  
        StringBuffer sb = new StringBuffer();  
        SortedMap<String, String> signParams = new TreeMap<String, String>();  
        Set es = this.smap.entrySet();  
        Iterator it = es.iterator();  
        while (it.hasNext()) {  
            Map.Entry entry = (Map.Entry) it.next();  
            String k = (String) entry.getKey();  
            String v = (String) entry.getValue();  
            if (k != "sign") {  
                signParams.put(k, v);  
            }  
        }  
  
        Set set = signParams.entrySet();  
        Iterator pit = set.iterator();
        while (pit.hasNext()) {  
            Map.Entry entry = (Map.Entry) pit.next();  
            String k = (String) entry.getKey();  
            String v = (String) entry.getValue();  
            if (sb.length() == 0) {  
                sb.append(k + "=" + v);  
            } else {  
                sb.append("&" + k + "=" + v);  
            }  
        }
        sb.append("&key=" + this.getKey());
        
        // 算出摘要 
        String enc = TenpayUtil.getCharacterEncoding(this.request,  
                this.response);  
        String sign = MD5Util.MD5Encode(sb.toString(), enc).toUpperCase();
  
        return sign.equals(this.smap.get("sign"));  
  
    }  
  
    // 判断微信维权签名  
    @SuppressWarnings({ "rawtypes", "static-access" })
	public boolean isWXsignfeedback() {  
  
        StringBuffer sb = new StringBuffer();  
        Hashtable signMap = new Hashtable();  
        Set es = this.parameters.entrySet();  
        Iterator it = es.iterator();  
        while (it.hasNext()) {  
            Map.Entry entry = (Map.Entry) it.next();  
            String k = (String) entry.getKey();  
            String v = (String) entry.getValue();  
            if (k != "SignMethod" && k != "AppSignature") {  
  
                sb.append(k + "=" + v + "&");  
            }  
        }  
        signMap.put("appkey", this.appkey);  
  
        // ArrayList akeys = new ArrayList();  
        // akeys.Sort();  
        while (it.hasNext()) {  
            String v = k;  
            if (sb.length() == 0) {  
                sb.append(k + "=" + v);  
            } else {  
                sb.append("&" + k + "=" + v);  
            }  
        }  
  
        String sign = Sha1Util.getSha1(sb.toString()).toString().toLowerCase();
  
        this.setDebugInfo(sb.toString() + " => SHA1 sign:" + sign);  
  
        return sign.equals(this.smap.get("AppSignature"));  
    }  
  
    /** 
     * 返回处理结果给财付通服务器。 
     *  
     * @param msg 
     *            Success or fail 
     * @throws IOException 
     */  
    public void sendToCFT(String msg) throws IOException {  
        String strHtml = msg;  
        PrintWriter out = this.getHttpServletResponse().getWriter();  
        out.println(strHtml);  
        out.flush();  
        out.close();  
    }  
    
    /** 
     * 返回处理结果给微信服务器
     * @param msg 
     *            Success or fail 
     * @throws IOException 
     */  
    public void sendToWX(Map<String,String> msg) throws IOException {
    	//生成xml
    	org.w3c.dom.Document document = XMLUtil.builder.newDocument();
    	org.w3c.dom.Element root = document.createElement("xml");
    	for(Map.Entry<String, String> entry:msg.entrySet()){
    		org.w3c.dom.Element contentElement = document.createElement(entry.getKey());
 			contentElement.setTextContent(entry.getValue());
 			root.appendChild(contentElement);
    	}
    	document.appendChild(root);
    	String doms = XMLUtil.toStringFromDoc(document);
        PrintWriter out = this.getHttpServletResponse().getWriter();  
        out.println(doms);
        out.flush();
        out.close();
    }
  
    /** 
     * 获取uri编码 
     *  
     * @return String 
     */  
    public String getUriEncoding() {  
        return uriEncoding;  
    }  
  
    /** 
     * 设置uri编码 
     *  
     * @param uriEncoding 
     * @throws UnsupportedEncodingException 
     */  
    @SuppressWarnings("rawtypes")
	public void setUriEncoding(String uriEncoding)  
            throws UnsupportedEncodingException {  
        if (!"".equals(uriEncoding.trim())) {  
            this.uriEncoding = uriEncoding;  
            // 编码转换  
            String enc = TenpayUtil.getCharacterEncoding(request, response);  
            Iterator it = this.parameters.keySet().iterator();  
            while (it.hasNext()) {  
                String k = (String) it.next();  
                String v = this.getParameter(k);  
                v = new String(v.getBytes(uriEncoding.trim()), enc);  
                this.setParameter(k, v);  
            }  
        }  
    }  
  
    /** 
     * 获取debug信息 
     */  
    public String getDebugInfo() {  
        return debugInfo;  
    }  
  
    /** 
     * 设置debug信息 
     */  
    protected void setDebugInfo(String debugInfo) {  
        this.debugInfo = debugInfo;  
    }  
  
    protected HttpServletRequest getHttpServletRequest() {  
        return this.request;  
    }  
  
    protected HttpServletResponse getHttpServletResponse() {  
        return this.response;  
    }  
  
}  