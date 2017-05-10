package com.pulamsi.myinfo.order.entity;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-05-16
 * Time: 14:51
 * FIXME
 */
public class refundDetail {
    private String auditState;
    private String shipMobile;//电话号码
    private String totalAmount;//总金额
    private String shipName;//姓名
    private String expendTotalIntegral;//积分
    private String paymentStatus;//支付状态
    private String modifyDate;//修改时间
    private String createDate;//创建时间
    private String orderSn;//退款编号
    private String cancelReason;//取消原因
    private int refundStatus;//退款状态  1退款中  2成功 3失败   空则不需要退款

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }



    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getAuditState() {
        return auditState;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public String getShipMobile() {
        return shipMobile;
    }

    public void setShipMobile(String shipMobile) {
        this.shipMobile = shipMobile;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getExpendTotalIntegral() {
        return expendTotalIntegral;
    }

    public void setExpendTotalIntegral(String expendTotalIntegral) {
        this.expendTotalIntegral = expendTotalIntegral;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    @Override
    public String toString() {
        return "refundDetail{" +
                "auditState='" + auditState + '\'' +
                ", shipMobile='" + shipMobile + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", shipName='" + shipName + '\'' +
                ", expendTotalIntegral='" + expendTotalIntegral + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", modifyDate='" + modifyDate + '\'' +
                ", createDate='" + createDate + '\'' +
                ", orderSn='" + orderSn + '\'' +
                ", cancelReason='" + cancelReason + '\'' +
                '}';
    }
}
