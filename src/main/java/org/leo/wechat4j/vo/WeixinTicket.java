package org.leo.wechat4j.vo;

import java.util.Date;

/**
 * 微信token
 * @author Leo
 * @create 2018-08-03
 *
 */
public class WeixinTicket extends BaseEntity{
	
	private int id;
	
	private String accessToken;
	
	private String ticket;
	
	private Date expires;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	@Override
	public String toString() {
		return "WeixinTicket [id=" + id + ", accessToken=" + accessToken
				+ ", ticket=" + ticket + ", expires=" + expires + "]";
	}

}
