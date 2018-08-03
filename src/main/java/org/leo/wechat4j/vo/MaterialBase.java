package org.leo.wechat4j.vo;


/**
 * 素材基类 : 图片、语音、视频
 * @author Leo
 * @create 2018-08-03
 */
public class MaterialBase {
		
	private String media_id; // 媒体id ，唯一标识
	
	private String name;  //文件名称
	
	private String update_time;  //最后更新时间
	
	private String url;  //访问路径

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
