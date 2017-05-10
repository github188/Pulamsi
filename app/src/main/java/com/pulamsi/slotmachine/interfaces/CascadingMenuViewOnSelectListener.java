package com.pulamsi.slotmachine.interfaces;


import com.pulamsi.slotmachine.entity.Area;

/**
 * 通用级联菜单接口
 * @author LILIN
 * 下午3:21:35
 */
public interface CascadingMenuViewOnSelectListener {
	public void getValue(Area province,Area city,Area district);
}
