package com.pulamsi.myinfo.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 实体类 - 订单
 */

public class Order implements Serializable {

    private static final long serialVersionUID = -1;

    private String id;// id
    private String deliveryFee;// 配送费用
    private String deliveryTypeName;// 配送方式名称
    private String memo;// 附言
    private String orderSn;// 订单编号
    private String orderStatus;// 订单状态
    private String paidAmount;// 已付金额
    private String paymentConfigName;// 支付方式名称
    private String paymentFee;// 支付费用
    private String taxes;// 税金
    private String paymentStatus;// 支付状态
    private BigDecimal productTotalPrice;// 商品总价格
    private Integer totalIntegral; // 获得积分总额
    private Integer expendTotalIntegral; // 消耗积分总额
    private String productTotalQuantity;// 商品总数
    private String productWeight;// 商品重量
    private String productWeightUnit;// 商品重量单位
    private String createDate;//创建时间
    private String shipAddress;// 收货地址
    private String shipArea;// 收货地区
    private String shipAreaPath;// 收货地区路径
    private String shipMobile;// 收货手机
    private String shipName;// 收货人姓名
    private String shipPhone;// 收货电话
    private String shipZipCode;// 收货邮编
    private String shippingStatus;// 发货状态
    private String totalAmount;// 订单总额
    private String invoiceHead;// 发票抬头
    private String isInvoice;// 是否发票
    private String isDelete;// 订单是否删除 0否1是
    private String provinceName; // 收货省
    private String cityName; // 收货市
    private String districtName; // 收货区
    private String deliveryType;// 配送方式
    private String paymentConfig;// 支付方式
    private int refundStatus;//退款状态  1退款中  2成功 3失败   空则不需要退款
    private List<OrderItem> orderItemSet;// 订单项

    /**
     * 天使新增参数
     */
    private Integer estimateSate;//商品评价状态 // 默认是1没评论，2是全部评论完成，3是部分评论
    private Integer orderType;// 0-普通定单；1-采购定单；2-总订单（拆单）
    private List<Order> orderSet;// 子订单（拆单）
    private String seller_id;//天使ID
    private String shopName;//店铺名
    private Boolean isSellerOrder;// 判断是否为商家订单


    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }



    public String getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getDeliveryTypeName() {
        return deliveryTypeName;
    }

    public void setDeliveryTypeName(String deliveryTypeName) {
        this.deliveryTypeName = deliveryTypeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaymentConfigName() {
        return paymentConfigName;
    }

    public void setPaymentConfigName(String paymentConfigName) {
        this.paymentConfigName = paymentConfigName;
    }

    public String getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(String paymentFee) {
        this.paymentFee = paymentFee;
    }

    public String getTaxes() {
        return taxes;
    }

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getProductTotalQuantity() {
        return productTotalQuantity;
    }

    public void setProductTotalQuantity(String productTotalQuantity) {
        this.productTotalQuantity = productTotalQuantity;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductWeightUnit() {
        return productWeightUnit;
    }

    public void setProductWeightUnit(String productWeightUnit) {
        this.productWeightUnit = productWeightUnit;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getShipArea() {
        return shipArea;
    }

    public void setShipArea(String shipArea) {
        this.shipArea = shipArea;
    }

    public String getShipAreaPath() {
        return shipAreaPath;
    }

    public void setShipAreaPath(String shipAreaPath) {
        this.shipAreaPath = shipAreaPath;
    }

    public String getShipMobile() {
        return shipMobile;
    }

    public void setShipMobile(String shipMobile) {
        this.shipMobile = shipMobile;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipPhone() {
        return shipPhone;
    }

    public void setShipPhone(String shipPhone) {
        this.shipPhone = shipPhone;
    }

    public String getShipZipCode() {
        return shipZipCode;
    }

    public void setShipZipCode(String shipZipCode) {
        this.shipZipCode = shipZipCode;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getInvoiceHead() {
        return invoiceHead;
    }

    public void setInvoiceHead(String invoiceHead) {
        this.invoiceHead = invoiceHead;
    }

    public String getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(String isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getPaymentConfig() {
        return paymentConfig;
    }

    public void setPaymentConfig(String paymentConfig) {
        this.paymentConfig = paymentConfig;
    }

    public List<OrderItem> getOrderItemSet() {
        return orderItemSet;
    }

    public void setOrderItemSet(List<OrderItem> orderItemSet) {
        this.orderItemSet = orderItemSet;
    }

    public String getCreateDate() {
        return createDate;
    }

    public Order setCreateDate(String createDate) {
        this.createDate = createDate;
        return this;
    }

    public Integer getExpendTotalIntegral() {
        return expendTotalIntegral;
    }

    public Order setExpendTotalIntegral(Integer expendTotalIntegral) {
        this.expendTotalIntegral = expendTotalIntegral;
        return this;
    }

    public Integer getTotalIntegral() {
        return totalIntegral;
    }

    public Order setTotalIntegral(Integer totalIntegral) {
        this.totalIntegral = totalIntegral;
        return this;
    }

    public Integer getEstimateSate() {
        return estimateSate;
    }

    public Order setEstimateSate(Integer estimateSate) {
        this.estimateSate = estimateSate;
        return this;
    }


    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }


    public List<Order> getOrderSet() {
        return orderSet;
    }

    public void setOrderSet(List<Order> orderSet) {
        this.orderSet = orderSet;
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

    public Boolean getIsSellerOrder() {
        return isSellerOrder;
    }

    public void setIsSellerOrder(Boolean isSellerOrder) {
        this.isSellerOrder = isSellerOrder;
    }




    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", deliveryFee='" + deliveryFee + '\'' +
                ", deliveryTypeName='" + deliveryTypeName + '\'' +
                ", memo='" + memo + '\'' +
                ", orderSn='" + orderSn + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", paidAmount='" + paidAmount + '\'' +
                ", paymentConfigName='" + paymentConfigName + '\'' +
                ", paymentFee='" + paymentFee + '\'' +
                ", taxes='" + taxes + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", productTotalPrice=" + productTotalPrice +
                ", totalIntegral=" + totalIntegral +
                ", expendTotalIntegral=" + expendTotalIntegral +
                ", productTotalQuantity='" + productTotalQuantity + '\'' +
                ", productWeight='" + productWeight + '\'' +
                ", productWeightUnit='" + productWeightUnit + '\'' +
                ", createDate='" + createDate + '\'' +
                ", shipAddress='" + shipAddress + '\'' +
                ", shipArea='" + shipArea + '\'' +
                ", shipAreaPath='" + shipAreaPath + '\'' +
                ", shipMobile='" + shipMobile + '\'' +
                ", shipName='" + shipName + '\'' +
                ", shipPhone='" + shipPhone + '\'' +
                ", shipZipCode='" + shipZipCode + '\'' +
                ", estimateSate=" + estimateSate +
                ", shippingStatus='" + shippingStatus + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", invoiceHead='" + invoiceHead + '\'' +
                ", isInvoice='" + isInvoice + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", deliveryType='" + deliveryType + '\'' +
                ", paymentConfig='" + paymentConfig + '\'' +
                ", refundStatus='" + refundStatus + '\'' +
                ", orderItemSet=" + orderItemSet +
                '}';
    }
}