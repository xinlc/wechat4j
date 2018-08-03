package org.leo.wechat4j.wxmsg.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import org.leo.wechat4j.wxmsg.util.ChangeAudioFormat;
import org.leo.wechat4j.util.Constant;
import org.leo.wechat4j.wxmsg.util.HttpRequester.HttpRespons;


/**
 *  微信公众平台工具类
 */
public class MediaUtil {
	
	/**
	 * 语音下载
	 * @param mediaId
	 * @param accessToken
	 * @param isSpeech
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public static Map<String, Object> downloadVoice(String mediaId,String accessToken,String isSpeech) 
			throws IOException{
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("accessToken", accessToken);  //微信密钥
		params.put("mediaId", mediaId);         //媒体id
		params.put("isSpeech", isSpeech);       // 1 是语音订单
		HttpRespons respons = HttpRequester.getInstance().sendPost(Constant.VOICE_TOOL_URL + "weixinApi/getVoice", 5000, 5000, params);
		String content = respons.getContent();
		JSONObject jo = JSONObject.parseObject(content);
		String mediaName = null;
		if(jo.get("success") != null && "1".equals(jo.get("success"))){  //成功
			mediaName = jo.get("data").toString();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mediaName",mediaName);
		return map;
	}
	
	
	/**
	 * 媒体下载
	 * @param mediaId
	 * @param accessToken
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> downloadMedia(String mediaId,String accessToken,String path) 
			throws IOException{
		
		String url = Constants.WEIXIN_API.DOWNLOAD_MEDIA + "access_token=" + accessToken + "&media_id=" + mediaId;
		//下载文件
		HttpRequesterMedia.HttpRespons HttpRespons = HttpRequesterMedia.sendGet(url, 5000, 5000,path); 
		
		String fileName = HttpRespons.fileName;  //amr name
		String mediaName = fileName.substring(0,fileName.indexOf(".")) + ".mp3";  //mp3 name
		
		//amr 转 mp3
		ChangeAudioFormat.changeToMp3(HttpRespons.getFilePath(), path + mediaName);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mediaName",mediaName);
		return map;
	}
	
	public static void main(String[] args) {
		try {
			MediaUtil.downloadMedia("HJndI-RhOWen-jnaGb_lC570XIzqOVFqDJHGEg0hTs9PYU1ILKU_INByO5W-HT6F","1vkqj4DWZ2h6scWkhgczYjZbaXqaJGvvy2PiURm5ZomMOecLesTnTECk1NmoRlqmVgVyuGjvLNQqD9F3vB-cN9cO-cMEdxQrRKFZ76Pzsxs","E:/");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
