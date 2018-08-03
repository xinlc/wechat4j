package org.leo.wechat4j.wxmsg.material;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.multipart.MultipartFile;

import org.leo.wechat4j.vo.Video;

/**
 * 微信素材-视频
 * @author Leo
 * @create 2018-08-03
 */
public class VideoService{

	private final static String type = "video"; //上传类型
	
	/**
	 * 保存
	 */
	public static String saveVideo(String accessToken,String title,String introduction, List<MultipartFile> imgs) {
		String id = null;
		for (MultipartFile file : imgs) {
			if (!file.isEmpty()) {
				try {
					String fileName = file.getOriginalFilename();
					InputStream is = file.getInputStream();
					long length = file.getSize();
					String content = UploadMaterialUtil.uploadVideoMaterial(accessToken, fileName, length, title, introduction, is);
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
	public static List<Video> getVideosPager(int start, int len, Map<String, Object> map) {
		List<Video> videos = new ArrayList<Video>();
		try {
			JSONArray ja = MaterialUtil.getMaterals(map.get("accessToken").toString(), start, len, type);
			if(ja != null && ja.size() > 0){
				for (int i = 0; i < ja.size(); i++) {
					JSONObject jItem = ja.getJSONObject(i);
					Video video = (Video) JSONObject.toBean(jItem, Video.class);
					videos.add(video);
				}
			}
		} catch (Exception e) {
			System.out.println("*******************获取列表异常********************");
			e.printStackTrace();
		}
		return videos;
	}

	/**
	 * 获取总数
	 */
	public static int getVideoCount(Map<String, Object> map) {
		return MaterialUtil.getCount(map.get("accessToken").toString(),"video_count");
	}

	/**
	 * 根据id获取
	 */
	public static Video getVideoById(String accessToken, String id) {
		
		return null;
	}

	/**
	 * 根据 id删除
	 * @return
	 */
	public static int deleteVideoById(String accessToken,String id) {
		return 	MaterialUtil.delete(accessToken, id);
	}
}
