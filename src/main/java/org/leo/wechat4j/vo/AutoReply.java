package org.leo.wechat4j.vo;

/**
 * 自动回复
 * @author Leo
 * @create 2018-08-03
 *
 */
public class AutoReply extends BaseEntity{

	private int id;
	
	private String name;
	
	private String key;
	
	private int event;     //1、订阅 2、取消订阅 3、菜单单击事件
	
	private int type;      //1、文本 2、图片 3、语音 4、视频 5、图文
	
	private String content;  //文本或路径
	
	private String eventKey; //事件名称

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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	@Override
	public String toString() {
		return "AutoReply [id=" + id + ", name=" + name + ", key=" + key
				+ ", event=" + event + ", type=" + type + ", content="
				+ content + ", eventKey=" + eventKey + "]";
	}
	
}
