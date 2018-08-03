package org.leo.wechat4j.wxmsg.util;

import java.util.ArrayList;
import java.util.List;

import org.leo.wechat4j.vo.Menu;

public class TableTree {
	
	/**
	 * 将集合按树形重新排列
	 * @param oldList 原集合
	 * @return 排列后的集合
	 */
	public static List<Menu> sortMenu(List<Menu> oldList){
		List<Menu> newList  = new ArrayList<Menu>();
		if (null != oldList && oldList.size() > 0) {
			for (Menu menu : oldList) {
				if (null == menu.getParent()) {//取最上级，没有父级的数据
					addChildren(menu, newList, oldList);
				}
			}
		}
		return newList;
	}
	
	/**
	 * 循环目标子集，将子集添的元素排列到目标后
	 * @param menu 目标
	 * @param newList 排列后的集合
	 * @param oldList 原集合
	 */
	public static void addChildren(Menu menu,List<Menu> newList,List<Menu> oldList){
		int id = menu.getId();
		if (haveChildren(menu, oldList)) {
			newList.add(menu);
			for (Menu item : oldList) {
				if (null != item.getParent() && item.getParent().getId() == id) {
					addChildren(item, newList, oldList);//如果有子集，调用递归
				}
			}
		} else {
			newList.add(menu);//如果没有子集，结束递归
			return;
		}
	}
	
	/**
	 * 判断目标是否存在子集
	 * @param menu 目标
	 * @param oldList 原集合
	 * @return true表示有子集,false表示没有子集
	 */
	public static boolean haveChildren(Menu menu,List<Menu> oldList){
		int id = menu.getId();
		for(Menu item : oldList){
			if (null != item.getParent() && item.getParent().getId() == id) {
				return true;
			}
		}
		return false;
	}

}
