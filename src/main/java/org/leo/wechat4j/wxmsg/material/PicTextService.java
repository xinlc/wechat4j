package org.leo.wechat4j.wxmsg.material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.multipart.MultipartFile;

import org.leo.wechat4j.vo.PicText;

import com.google.gson.Gson;

/**
 * 微信素材-图文
 * @author Leo
 * @create 2018-08-03
 *
 */
public class PicTextService{

	private final static String type = "news"; //上传类型
	
	/**
	 * 保存
	 */
	public static String savePicText(String accessToken,List<MultipartFile> imgs) {
		return MaterialUtil.save(accessToken,imgs,type);
	}

	/**
	 * 分页获取
	 */
	public static List<PicText> getPicTextsPager(int start, int len, Map<String, Object> map) {
		List<PicText> pics = new ArrayList<PicText>();
		try {
			JSONArray ja = MaterialUtil.getMaterals(map.get("accessToken").toString(), start, len, type);
			if(ja != null && ja.size() > 0){
				for (int i = 0; i < ja.size(); i++) {
					JSONObject jItem = ja.getJSONObject(i);
					Gson gj = new Gson();
					PicText pic = (PicText) gj.fromJson(jItem.toString(), PicText.class);
					pics.add(pic);
				}
			}
		} catch (Exception e) {
			System.out.println("*******************获取列表异常********************");
			e.printStackTrace();
		}
		return pics;
	}

	/**
	 * 获取总数
	 */
	public static int getPicTextCount(Map<String, Object> map) {
		return MaterialUtil.getCount(map.get("accessToken").toString(),"news_count");
	}

	/**
	 * 根据id获取
	 */
	public static PicText getPicTextById(String accessToken, String id) {
		
		return null;
	}

	/**
	 * 根据 id删除
	 * @return
	 */
	public static int deletePicTextById(String accessToken,String id) {
		return 	MaterialUtil.delete(accessToken, id);
	}

}
