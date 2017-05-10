package com.pulamsi.home.entity;

import com.lidroid.xutils.db.annotation.Id;

import java.io.Serializable;


/**
 * 
 * @author lq 热销商品
 *
 */

public class HotSellProduct implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;

	private String name;// 商品名字
	private String pic;// 图片路径
	private String price;// 现价
	private String historicalPrice;// 历史价格
	private String productCategory;//分类
	private String sn;//编码
	private String panicBuyPrice;//抢购的价格
	private String priceId;//价格ID

	public String getPriceId() {
		return priceId;
	}

	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getPanicBuyPrice() {
		return panicBuyPrice;
	}

	public void setPanicBuyPrice(String panicBuyPrice) {
		this.panicBuyPrice = panicBuyPrice;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	private String sales;//销售量


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getHistoricalPrice() {
		return historicalPrice;
	}

	public void setHistoricalPrice(String historicalPrice) {
		this.historicalPrice = historicalPrice;
	}
}
