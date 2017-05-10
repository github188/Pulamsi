package com.pulamsi.angel.bean;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-26
 * Time: 09:24
 * FIXME
 */
public class AngelEvaluationBean {

    private String id;
    private String createDate;// 创建时间
    private String modifyDate;// 修改时间
    private String estimateContent;// 评价内容
    private int auditState;// 是否显示到前端 0否 1是
    private float sellerCredit;// 正能量
    private float sellerServe;// 天使服务
    private float sellerAfter;// 天使售后
    // private float sellerIndex;// 天使指数 评分(5-4好评，3-2中评，1分差评)备用
    private float sellerSpeed;// 天使速度 五星制 1-5
    // private float deliveryStars;// 配送人员态度 备用
    private String userName;// 用户名
    private String memberimg;// 评价用户头像地址
    private String memberId;// 评价用户ID
    private String sellerReply;// 商家回复内容
    private String replyDate;// 商家回复时间

    private String sellerId;// 商家
    private String orderId;// 定单
    // 回复 评论的父节点,取值该表的id字段,如果该字段为0,则是一个普通评论,否则该条评论就是该字段的值所对应的评论的回复
    private String parentId;//


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getEstimateContent() {
        return estimateContent;
    }

    public void setEstimateContent(String estimateContent) {
        this.estimateContent = estimateContent;
    }

    public int getAuditState() {
        return auditState;
    }

    public void setAuditState(int auditState) {
        this.auditState = auditState;
    }

    public float getSellerCredit() {
        return sellerCredit;
    }

    public void setSellerCredit(float sellerCredit) {
        this.sellerCredit = sellerCredit;
    }

    public float getSellerServe() {
        return sellerServe;
    }

    public void setSellerServe(float sellerServe) {
        this.sellerServe = sellerServe;
    }

    public float getSellerAfter() {
        return sellerAfter;
    }

    public void setSellerAfter(float sellerAfter) {
        this.sellerAfter = sellerAfter;
    }

    public float getSellerSpeed() {
        return sellerSpeed;
    }

    public void setSellerSpeed(float sellerSpeed) {
        this.sellerSpeed = sellerSpeed;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMemberimg() {
        return memberimg;
    }

    public void setMemberimg(String memberimg) {
        this.memberimg = memberimg;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getSellerReply() {
        return sellerReply;
    }

    public void setSellerReply(String sellerReply) {
        this.sellerReply = sellerReply;
    }

    public String getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(String replyDate) {
        this.replyDate = replyDate;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
