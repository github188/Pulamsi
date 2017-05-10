package com.pulamsi.myinfo.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;// 商品编号
	private String seoTitle;// 长标题
	private String pic;// 图片路径
	private BigDecimal price;
	private BigDecimal marketPrice;
	private String name;
	private String sn;
	private String fullName;
	private boolean isAutoSell;//是否可以自取
	private List<String> productImages;


	/**
	 * 积分商城需要字段
	 */
	private Integer point = 0;// 赠送积分
	private Boolean isIntegral;// 是否是积分商品 0-否;1-是
	private Boolean isMarketable;// 是否上架 0-否;1-是
	private List<ProductPrice> productPrices;// 其他的价格

	private Long sales; // 销量
	private Long weekSales;// 每星期销量
	private Long monthSales;// 月销量


	/**
	 * 抢购字段
	 */
	private boolean isPanicBuy;//'是否是抢购商品
	private BigDecimal panicBuyPrice;//抢购价格
	private Long panicBuyBeginTime;//抢购开始时间
	private Long panicBuyEndTime;//抢购结束时间
	private Integer panicBuyQuantity;//抢购限制数量


	/**
	 * 天使字段
	 */
	private String seller_id;// 天使id
	private String shopName;// 店铺名
	private String shopImg;// 店主展示照片
	private Boolean isPass;// 审核处理状态 0未审核 1待审核 2审核通过 3审核失败 2=true 其他=fasle
	private String introduction;// 介绍
	private String productCategoryName;// 商品类别
	private Boolean isRecommend;// 是否作为店主推荐展示商品 0否 1是
	private float averagScore;// 商品评价 平均分
	private int count;// 评价次数
	private Boolean isSeller = false;// 是否天使商品





	public Long getSales() {
		return sales;
	}

	public Product setSales(Long sales) {
		this.sales = sales;
		return this;
	}

	public Long getWeekSales() {
		return weekSales;
	}

	public Product setWeekSales(Long weekSales) {
		this.weekSales = weekSales;
		return this;
	}

	public Long getMonthSales() {
		return monthSales;
	}

	public Product setMonthSales(Long monthSales) {
		this.monthSales = monthSales;
		return this;
	}

	public Boolean getIsMarketable() {
		return isMarketable;
	}

	public Product setIsMarketable(Boolean isMarketable) {
		this.isMarketable = isMarketable;
		return this;
	}

	public boolean isAutoSell() {
		return isAutoSell;
	}

	public void setIsAutoSell(boolean isAutoSell) {
		this.isAutoSell = isAutoSell;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public List<String> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<String> productImages) {
		this.productImages = productImages;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}


	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getPoint() {
		return point;
	}

	public Product setPoint(Integer point) {
		this.point = point;
		return this;
	}

	public Boolean getIsIntegral() {
		return isIntegral;
	}

	public Product setIsIntegral(Boolean isIntegral) {
		this.isIntegral = isIntegral;
		return this;
	}

	public List<ProductPrice> getProductPrices() {
		return productPrices;
	}

	public Product setProductPrices(List<ProductPrice> productPrices) {
		this.productPrices = productPrices;
		return this;
	}


	public boolean isPanicBuy() {
		return isPanicBuy;
	}

	public void setIsPanicBuy(boolean isPanicBuy) {
		this.isPanicBuy = isPanicBuy;
	}

	public BigDecimal getPanicBuyPrice() {
		return panicBuyPrice;
	}

	public void setPanicBuyPrice(BigDecimal panicBuyPrice) {
		this.panicBuyPrice = panicBuyPrice;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getPanicBuyBeginTime() {
		return panicBuyBeginTime;
	}

	public void setPanicBuyBeginTime(Long panicBuyBeginTime) {
		this.panicBuyBeginTime = panicBuyBeginTime;
	}

	public Long getPanicBuyEndTime() {
		return panicBuyEndTime;
	}

	public void setPanicBuyEndTime(Long panicBuyEndTime) {
		this.panicBuyEndTime = panicBuyEndTime;
	}

	public Integer getPanicBuyQuantity() {
		return panicBuyQuantity;
	}

	public void setPanicBuyQuantity(Integer panicBuyQuantity) {
		this.panicBuyQuantity = panicBuyQuantity;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopImg() {
		return shopImg;
	}

	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}

	public Boolean getIsPass() {
		return isPass;
	}

	public void setIsPass(Boolean isPass) {
		this.isPass = isPass;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public Boolean getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(Boolean isRecommend) {
		this.isRecommend = isRecommend;
	}

	public float getAveragScore() {
		return averagScore;
	}

	public void setAveragScore(float averagScore) {
		this.averagScore = averagScore;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Boolean getIsSeller() {
		return isSeller;
	}

	public void setIsSeller(Boolean isSeller) {
		this.isSeller = isSeller;
	}


	@Override
	public String toString() {
		return "Product{" +
				"id='" + id + '\'' +
				", seoTitle='" + seoTitle + '\'' +
				", pic='" + pic + '\'' +
				", price=" + price +
				", marketPrice=" + marketPrice +
				", name='" + name + '\'' +
				", sn='" + sn + '\'' +
				", fullName='" + fullName + '\'' +
				", isAutoSell=" + isAutoSell +
				", productImages=" + productImages +
				", point=" + point +
				", isIntegral=" + isIntegral +
				", isMarketable=" + isMarketable +
				", productPrices=" + productPrices +
				", sales=" + sales +
				", weekSales=" + weekSales +
				", monthSales=" + monthSales +
				", isPanicBuy=" + isPanicBuy +
				", panicBuyPrice=" + panicBuyPrice +
				", panicBuyBeginTime=" + panicBuyBeginTime +
				", panicBuyEndTime=" + panicBuyEndTime +
				", panicBuyQuantity=" + panicBuyQuantity +
				", seller_id='" + seller_id + '\'' +
				", shopName='" + shopName + '\'' +
				", shopImg='" + shopImg + '\'' +
				", isPass=" + isPass +
				", introduction='" + introduction + '\'' +
				", productCategoryName='" + productCategoryName + '\'' +
				", isRecommend=" + isRecommend +
				", averagScore=" + averagScore +
				", count=" + count +
				", isSeller=" + isSeller +
				'}';
	}
}
