package org.leo.wechat4j.wxmsg.material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.multipart.MultipartFile;

import org.leo.wechat4j.vo.Pic;


/**
 * 微信素材-图片
 * @author Leo
 * @create 2018-08-03
 */
public class PicService{

	private final static String type = "image"; //上传类型
	
	/**
	 * 保存
	 */
	public static String savePic(String accessToken,List<MultipartFile> imgs) {
		return MaterialUtil.save(accessToken,imgs,type);
	}

	/**
	 * 分页获取
	 */
	public static List<Pic> getPicsPager(int start, int len, Map<String, Object> map) {
		List<Pic> pics = new ArrayList<Pic>();
		try {
			JSONArray ja = MaterialUtil.getMaterals(map.get("accessToken").toString(), start, len, type);
			if(ja != null && ja.size() > 0){
				for (int i = 0; i < ja.size(); i++) {
					JSONObject jItem = ja.getJSONObject(i);
					Pic pic = (Pic) JSONObject.toBean(jItem, Pic.class);
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
	public static int getPicCount(Map<String, Object> map) {
		return MaterialUtil.getCount(map.get("accessToken").toString(),"image_count");
	}

	/**
	 * 根据id获取
	 */
	public static Pic getPicById(String accessToken, String id) {
		
		return null;
	}

	/**
	 * 根据 id删除
	 * @return
	 */
	public static int deletePicById(String accessToken,String id) {
		return 	MaterialUtil.delete(accessToken, id);
	}
}
