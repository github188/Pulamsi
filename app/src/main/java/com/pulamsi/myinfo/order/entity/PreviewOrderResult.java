package com.pulamsi.myinfo.order.entity;

import com.pulamsi.base.entity.ResultContext;
import com.pulamsi.myinfo.address.entity.Address;
import com.pulamsi.shoppingcar.entity.CartCommodity;

import java.io.Serializable;
import java.util.List;

/**
 * 生产订单请求返回对象
 */
public class PreviewOrderResult extends ResultContext {

	private PreviewOrder return_context;//返回数据

	public PreviewOrder getReturn_context() {
		return return_context;
	}

	public PreviewOrderResult setReturn_context(PreviewOrder return_context) {
		this.return_context = return_context;
		return this;
	}
}
