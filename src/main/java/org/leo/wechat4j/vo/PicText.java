package org.leo.wechat4j.vo;



/**
 * 素材管理-图文
 * @author Leo
 * @create 2018-08-03
 */
public class PicText extends BaseEntity{

	public PicText() {
		super();
	}

	public PicText(int id, int picTextNameId, String title, String description,
			String picUrl, String url, int isMain) {
		super();
		setId(id);
		setPicTextNameId(picTextNameId);
		setTitle(title);
		setDescription(description);
		setPicUrl(picUrl);
		setUrl(url);
		setIsMain(isMain);
	}

	private int id;
	private int picTextNameId;// 图文名称ID
	private String title;// 标题
	private String description;// 图文消息描述
	private String picUrl;// 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
	private String url;// 点击图文消息跳转链接
	private int isMain;// 是否是主显示 1、是 2、否

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIsMain() {
		return isMain;
	}

	public void setIsMain(int isMain) {
		this.isMain = isMain;
	}

	public int getPicTextNameId() {
		return picTextNameId;
	}

	public void setPicTextNameId(int picTextNameId) {
		this.picTextNameId = picTextNameId;
	}
}
