package org.leo.wechat4j.vo;

import java.util.Date;

/**
 * 基类
 * @author Leo
 * @create 2018-08-03
 *
 */
public class BaseEntity {
	
	private Integer updatedUserId;//最后修改者
	
	private Date updatedTime;//最后修改时间
	
	public Integer getUpdatedUserId() {
		return updatedUserId;
	}

	public void setUpdatedUserId(Integer updatedUserId) {
		this.updatedUserId = updatedUserId;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

}
