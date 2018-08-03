package org.leo.wechat4j.wxmsg.material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.multipart.MultipartFile;

import org.leo.wechat4j.vo.Voice;

/**
 * 微信素材-语音
 * @author Leo
 * @create 2018-08-03
 */
public class VoiceService{

	private final static String type = "voice"; //上传类型
	
	/**
	 * 保存
	 */
	public static String saveVoice(String accessToken,List<MultipartFile> imgs) {
		return MaterialUtil.save(accessToken,imgs,type);
	}

	/**
	 * 分页获取
	 */
	public static List<Voice> getVoicesPager(int start, int len, Map<String, Object> map) {
		List<Voice> voices = new ArrayList<Voice>();
		try {
			JSONArray ja = MaterialUtil.getMaterals(map.get("accessToken").toString(), start, len, type);
			if(ja != null && ja.size() > 0){
				for (int i = 0; i < ja.size(); i++) {
					JSONObject jItem = ja.getJSONObject(i);
					Voice voice = (Voice) JSONObject.toBean(jItem, Voice.class);
					voices.add(voice);
				}
			}
		} catch (Exception e) {
			System.out.println("*******************获取列表异常********************");
			e.printStackTrace();
		}
		return voices;
	}

	/**
	 * 获取总数
	 */
	public static int getVoiceCount(Map<String, Object> map) {
		return MaterialUtil.getCount(map.get("accessToken").toString(),"voice_count");
	}

	/**
	 * 根据id获取
	 */
	public static Voice getVoiceById(String accessToken, String id) {
		
		return null;
	}

	/**
	 * 根据 id删除
	 * @return
	 */
	public static int deleteVoiceById(String accessToken,String id) {
		return 	MaterialUtil.delete(accessToken, id);
	}

}
