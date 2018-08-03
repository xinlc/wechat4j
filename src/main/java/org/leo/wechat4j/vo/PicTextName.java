package org.leo.wechat4j.vo;

import java.util.List;

/**
 * 素材管理-图文名称
 * @author Leo
 * @create 2018-08-03
 * 
 */
public class PicTextName extends BaseEntity{

	public PicTextName() {
		super();
	}

	public PicTextName(int id, String name, List<PicText> picTexts) {
		super();
		setId(id);
		setName(name);
		setPicTexts(picTexts);
	}

	private int id;// 编号
	private String name;// 名称
	private List<PicText> picTexts;// 图文

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

	public List<PicText> getPicTexts() {
		return picTexts;
	}

	public void setPicTexts(List<PicText> picTexts) {
		this.picTexts = picTexts;
	}

	@Override
	public String toString() {
		return "PicTextName [id=" + id + ", name=" + name + ", picTexts="
				+ picTexts + "]";
	}

}
