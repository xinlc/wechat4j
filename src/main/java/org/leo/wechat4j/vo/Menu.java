package org.leo.wechat4j.vo;

import java.util.List;

/**
 * 微信——自定义菜单
 * @author Leo
 * @create 2018-08-03
 */
public class Menu extends BaseEntity{

	public Menu() {
		super();
	}

	public Menu(int id, String name, String type, String key, String url,
			Menu parent) {
		super();
		setId(id);
		setName(name);
		setType(type);
		setKey(key);
		setUrl(url);
		setParent(parent);
	}

	private int id;// 编号
	private String name;// 名称
	private String type;// 可选值：click、view,scancode_push,scancode_waitmsg,pic_sysphoto,pic_photo_or_album,pic_weixin,location_select
	private String key;// 与自动回复的key对应,type=click的时候有效
	private String url;// type=view的时候有效
	private Menu parent;// 父菜单

	private List<Menu> children;// 子菜单，用于发布菜单时拼接JSON

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

}
