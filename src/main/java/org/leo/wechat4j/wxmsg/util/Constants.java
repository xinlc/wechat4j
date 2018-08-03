package org.leo.wechat4j.wxmsg.util;

/**
 *  WAP常量定义
 */
public class Constants {
	/**
	 * 基础key
	 */
    public final static class BASE_KEY{
    	
    }
    
    /**
     * 活动配置
     */
    public final static class CODE{
    	public final static String SUCCESS = "0000";
    	public final static String ERROR = "9999";
    }
    
    /**
     * 微信API
     */
    public final static class WEIXIN_API{
    	public static String ACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    	public static String JSAPITICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
    	public static String OAUTH2ACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code";
    	public static String SNSAPIUSERINFO = "https://api.weixin.qq.com/sns/userinfo?";
    	public static String USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info?";
    	public static String DOWNLOAD_MEDIA = "http://file.api.weixin.qq.com/cgi-bin/media/get?"; //access_token=ACCESS_TOKEN&media_id=MEDIA_ID
    	public static String UPLOAD_MEDIA = "http://file.api.weixin.qq.com/cgi-bin/media/upload?"; //access_token=ACCESS_TOKEN&type=TYPE
    	public static String TEMPLATE_SEND = "https://api.weixin.qq.com/cgi-bin/message/template/send?"; //access_token=ACCESS_TOKEN
    	public static String ADD_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/add_material?"; // 永久素材添加 access_token=ACCESS_TOKEN&type=image
    	public static String GET_MATERIALCOUNT = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?"; // 获取素材总数 access_token=ACCESS_TOKEN
    	public static String BATCHGET_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?"; // 获取素材列表 access_token=ACCESS_TOKEN
    	public static String GET_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/get_material?"; // 获取素材 access_token=ACCESS_TOKEN
    	public static String DEL_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/del_material?"; // 删除素材 access_token=ACCESS_TOKEN

    }
}