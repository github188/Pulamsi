package com.pulamsi.shoppingcar.entity;

import com.pulamsi.myinfo.order.entity.Product;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *购物车货物 
 *
 */
public class CartCommodity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String id;
	private Product product;//货物
	private Product sellerProduct;//货物
	private int count;
	private BigDecimal totalPrice;//货物需要总金额
	private int totalintegralPrice;//货物需要总积分
	private boolean ischeck;
	private boolean isEdit;
	private boolean isupdate;//是否要更新数量
	private int integralPrice;// 所需积分
	private BigDecimal productPrice;// 所需金额
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}


	public boolean isupdate() {
		return isupdate;
	}

	public CartCommodity setIsupdate(boolean isupdate) {
		this.isupdate = isupdate;
		return this;
	}

	public boolean ischeck() {
		return ischeck;
	}

	public CartCommodity setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
		return this;
	}

	public BigDecimal getTotalPrice() {
		return productPrice.multiply(new BigDecimal(count));
	}

	public int getTotalintegralPrice() {
		return count * integralPrice;
	}

	public boolean isEdit() {
		return isEdit;
	}
	public void setIsEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public int getIntegralPrice() {
		return integralPrice;
	}

	public CartCommodity setIntegralPrice(int integralPrice) {
		this.integralPrice = integralPrice;
		return this;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public CartCommodity setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
		return this;
	}


	public Product getSellerProduct() {
		return sellerProduct;
	}

	public void setSellerProduct(Product sellerProduct) {
		this.sellerProduct = sellerProduct;
	}
}
