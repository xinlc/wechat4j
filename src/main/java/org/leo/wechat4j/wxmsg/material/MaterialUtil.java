package org.leo.wechat4j.wxmsg.material;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.multipart.MultipartFile;

import org.leo.wechat4j.wxmsg.util.Constants;


/**
 * 素材工具类
 * @author Leo
 * @create 2018-08-03
 */
public class MaterialUtil {
	
	/**
	 * 保存
	 */
	public static String save(String accessToken, List<MultipartFile> imgs, String type) {
		String id = null;
		for (MultipartFile file : imgs) {
			if (!file.isEmpty()) {
				try {
					String fileName = file.getOriginalFilename();
					InputStream is = file.getInputStream();
					long length = file.getSize();
					String content = UploadMaterialUtil.uploadMaterial(accessToken,fileName,length,is);
					JSONObject json = JSONObject.fromObject(content);
					Object ids = json.get("media_id");
					if(ids != null){
						id = ids.toString();
					}
				} catch (IOException e) {
					System.out.println("******保存素材失败******");
					e.printStackTrace();
				}	
			}
		}
		return id;
	}

	/**
	 * 分页获取
	 */
	public static JSONArray getMaterals( String accessToken,int start, int len,String type) {
		String url = Constants.WEIXIN_API.BATCHGET_MATERIAL + "access_token=" + accessToken;
		JSONObject param = new JSONObject();
		param.put("type", type);
		param.put("offset", start);
		param.put("count", len);
		HttpRequesterJson.HttpRespons respons;
		JSONArray ja = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("paramJsonType", param.toString());
			respons = HttpRequesterJson.sendPost(url,5000, 5000,params);
			JSONObject json = JSONObject.fromObject(respons.getContent());
			//Object count = json.get("total_count");  	//素材总数
			Object itemCount = json.get("item_count");  //本次调用总数
			if(itemCount != null){
				ja = json.getJSONArray("item");
			}else{
				System.out.println("**********获取素材失败******* " + json);
			}
		} catch (Exception e) {
			System.out.println("********获取列表异常********");
			e.printStackTrace();
		}
		return ja;
	}

	/**
	 * 获取总数
	 */
	public static int getCount(String accessToken,String type) {
		String url = Constants.WEIXIN_API.GET_MATERIALCOUNT + "access_token=" + accessToken;
		HttpRequesterJson.HttpRespons respons;
		String content = null;
		int imgCount = 0;
		try {
			respons = HttpRequesterJson.sendGet(url,5000, 5000);
			content = respons.getContent();
			JSONObject json = JSONObject.fromObject(content);
			Object count = json.get(type);
			if(count != null){
				imgCount = Integer.parseInt(count.toString());
			}else{
				System.out.println("*******获取素材列表总数是失败******"+json);
			}
		} catch (Exception e) {
			System.out.println("*********获取总数异常******");
			e.printStackTrace();
		}
		return imgCount;
	}

	/**
	 * 根据id获取  接口返回信息无用，暂不继续实现
	 */
	public static String getMaterialById(String accessToken, String id) {
		String url = Constants.WEIXIN_API.GET_MATERIALCOUNT + "access_token=" + accessToken;
		HttpRequesterJson.HttpRespons respons;
		String content = null;
		JSONObject param = new JSONObject();
		param.put("media_id", id);
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("paramJsonType", param.toString());
			respons = HttpRequesterJson.sendPost(url,5000, 5000,params);
			content = respons.getContent();
			JSONObject json = JSONObject.fromObject(content);
			System.out.println(json);
		} catch (Exception e) {
			System.out.println("**********根据id获取异常***********");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据 id删除
	 * @return
	 */
	public static int delete(String accessToken,String id) {
		String url = Constants.WEIXIN_API.DEL_MATERIAL + "access_token=" + accessToken;
		HttpRequesterJson.HttpRespons respons;
		String content = null;
		int errcode = 0;
		JSONObject param = new JSONObject();
		param.put("media_id", id);
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("paramJsonType", param.toString());
			respons = HttpRequesterJson.sendPost(url,5000, 5000,params);
			content = respons.getContent();
			JSONObject json = JSONObject.fromObject(content);
			String errs = json.getString("errcode");
			if(errs != null && "0".equals(errs)){  //成功
				errcode = 1;
			}else{
				System.out.println("删除素材失败："+content);
			}
		} catch (Exception e) {
			System.out.println("*********删除素材异常******");
			e.printStackTrace();
		}
		return errcode;
	}
	
	
}
