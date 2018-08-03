package org.leo.wechat4j.wxmsg.msg;

import java.util.ArrayList;
import java.util.List;

import org.leo.wechat4j.wxmsg.exception.WeixinMenuOutOfBoundException;


/**
 * Button
 * @author Leo
 * @create 2018-08-03
 */
public class Data4Button {

	// 一级菜单数组，个数应为1~3个
	public List<Data4Menu> button = new ArrayList<Data4Menu>(3);

	
	/**
	 * 添加一级菜单
	 * @param menu 
	 * @throws WeixinMenuOutOfBoundException
	 */
	public void addMenu(Data4Menu menu) throws WeixinMenuOutOfBoundException {
		if(button.size() < 3){
			button.add(menu);
		}else{
			throw new WeixinMenuOutOfBoundException();
		}
	}
	
	
}
