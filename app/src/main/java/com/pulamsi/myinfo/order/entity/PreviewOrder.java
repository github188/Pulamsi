package com.pulamsi.myinfo.order.entity;

import com.pulamsi.myinfo.address.entity.Address;
import com.pulamsi.shoppingcar.entity.CartCommodity;

import java.io.Serializable;
import java.util.List;

/**
 * 生产订单返回对象，包含，地址，付款方式，配送方式，购物清单
 */
public class PreviewOrder implements Serializable {

	private static final long serialVersionUID = 1L;
	// 收货人信息
	private List<Address> receiverList;
	// 付款方式
	private List<MasterplateEntity> payTypeList;
	// 配送方式
	private List<MasterplateEntity> deliveryTypeList;
	// 购物清单
	private List<CartCommodity> cartItems;
	// 抢购清单
	private List<CartCommodity> panicCartItems;

	//会员积分
	private int point;

	public int getPoint() {
		return point;
	}

	public PreviewOrder setPoint(int point) {
		this.point = point;
		return this;
	}

	public List<Address> getReceiverList() {
		return receiverList;
	}

	public PreviewOrder setReceiverList(List<Address> receiverList) {
		this.receiverList = receiverList;
		return this;
	}

	public List<MasterplateEntity> getPayTypeList() {
		return payTypeList;
	}

	public PreviewOrder setPayTypeList(List<MasterplateEntity> payTypeList) {
		this.payTypeList = payTypeList;
		return this;
	}

	public List<MasterplateEntity> getDeliveryTypeList() {
		return deliveryTypeList;
	}

	public PreviewOrder setDeliveryTypeList(List<MasterplateEntity> deliveryTypeList) {
		this.deliveryTypeList = deliveryTypeList;
		return this;
	}

	public List<CartCommodity> getCartItems() {
		return cartItems;
	}

	public PreviewOrder setCartItems(List<CartCommodity> cartItems) {
		this.cartItems = cartItems;
		return this;
	}

	public List<CartCommodity> getPanicCartItems() {
		return panicCartItems;
	}

	public void setPanicCartItems(List<CartCommodity> panicCartItems) {
		this.panicCartItems = panicCartItems;
	}


	@Override
	public String toString() {
		return "PreviewOrder{" +
				"receiverList=" + receiverList +
				", payTypeList=" + payTypeList +
				", deliveryTypeList=" + deliveryTypeList +
				", cartItems=" + cartItems +
				", panicCartItems=" + panicCartItems +
				", point=" + point +
				'}';
	}
}
