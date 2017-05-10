package com.pulamsi.evaluate.entity;

import java.io.Serializable;

/**
 * 评论实体类
 * Created by lanqiang on 16/1/5.
 */
public class Estimate implements Serializable {

    private static final long serialVersionUID = -1;
    private String productId;// 商品id 普通商品
    private String sellerProductId;// 天使商品id
    private String userName;// 评价的用户名
    private String content;// 评价内容
    private Integer productStars;// 商品评分
    private String createDate;// 购买时间

    private String id;// 订单Id
    private int auditState;// 是否显示到前端 0否1是
    private String memberId;// 评价人会员id
    private String orderItem_id;// 订单明细

    public String getProductId() {
        return productId;
    }

    public Estimate setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public Estimate setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Estimate setContent(String content) {
        this.content = content;
        return this;
    }

    public Integer getProductStars() {
        return productStars;
    }

    public Estimate setProductStars(Integer productStars) {
        this.productStars = productStars;
        return this;
    }

    public String getCreateDate() {
        return createDate;
    }

    public Estimate setCreateDate(String createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getSellerProductId() {
        return sellerProductId;
    }

    public void setSellerProductId(String sellerProductId) {
        this.sellerProductId = sellerProductId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAuditState() {
        return auditState;
    }

    public void setAuditState(int auditState) {
        this.auditState = auditState;
    }

    public String getOrderItem_id() {
        return orderItem_id;
    }

    public void setOrderItem_id(String orderItem_id) {
        this.orderItem_id = orderItem_id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
