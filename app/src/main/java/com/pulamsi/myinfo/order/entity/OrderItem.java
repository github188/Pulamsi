package com.pulamsi.myinfo.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Bean类 - 订单项
 */

public class OrderItem implements Serializable {

	private static final long serialVersionUID = -1;

	private String id;
	private String productSn;// 商品货号
	private String productName;// 商品名称
	private BigDecimal productPrice;// 商品价格
	private int integralPrice;// 积分价格
	private String productHtmlFilePath;// 商品HTML静态文件路径
	private String productQuantity;// 商品数量
	private String deliveryQuantity;// 发货数量
	private String totalDeliveryQuantity;// 总发货量
	private String twoCode;// 二维码
	private Product product;// 商品
	private Product sellerProduct;//天使商品
	private Integer estimateStatus;// 默认是1没评论，2是全部评论完成，3是部分评论

	/**
	 * 评价用的字段，其他地方不用
	 */
	private int point;//评论评分
	private String content;//评论内容

	public int getIntegralPrice() {
		return integralPrice;
	}

	public OrderItem setIntegralPrice(int integralPrice) {
		this.integralPrice = integralPrice;
		return this;
	}

	public String getProductSn() {
		return productSn;
	}
	public void setProductSn(String productSn) {
		this.productSn = productSn;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductHtmlFilePath() {
		return productHtmlFilePath;
	}
	public void setProductHtmlFilePath(String productHtmlFilePath) {
		this.productHtmlFilePath = productHtmlFilePath;
	}
	public String getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(String productQuantity) {
		this.productQuantity = productQuantity;
	}
	public String getDeliveryQuantity() {
		return deliveryQuantity;
	}
	public void setDeliveryQuantity(String deliveryQuantity) {
		this.deliveryQuantity = deliveryQuantity;
	}
	public String getTotalDeliveryQuantity() {
		return totalDeliveryQuantity;
	}
	public void setTotalDeliveryQuantity(String totalDeliveryQuantity) {
		this.totalDeliveryQuantity = totalDeliveryQuantity;
	}
	public String getTwoCode() {
		return twoCode;
	}
	public void setTwoCode(String twoCode) {
		this.twoCode = twoCode;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	public String getId() {
		return id;
	}

	public OrderItem setId(String id) {
		this.id = id;
		return this;
	}

	public int getPoint() {
		return point;
	}

	public OrderItem setPoint(int point) {
		this.point = point;
		return this;
	}

	public String getContent() {
		return content;
	}

	public OrderItem setContent(String content) {
		this.content = content;
		return this;
	}


	public Product getSellerProduct() {
		return sellerProduct;
	}

	public void setSellerProduct(Product sellerProduct) {
		this.sellerProduct = sellerProduct;
	}

	public Integer getEstimateStatus() {
		return estimateStatus;
	}

	public void setEstimateStatus(Integer estimateStatus) {
		this.estimateStatus = estimateStatus;
	}
}