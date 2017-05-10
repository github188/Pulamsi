package com.pulamsi.myinfo.slotmachineManage.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-04-19
 * Time: 14:21
 * FIXME
 */
public class RefundBean implements Serializable {
    private String id;
    private String refundSn;// 退款编号
    private Integer refundType;// 退款类型
    private String paymentConfigName;// 支付配置名称
    private String bankName;// 退款银行名称
    private String bankAccount;// 退款银行账号
    private BigDecimal totalAmount;// 退款金额
    private String payee;// 收款人
    private String operator;// 操作员
    private String memo;// 备注
    private String mobilePhone;// 手机
    private Integer auditState; // 处理状态 RefundState
    private String paymentConfig;// 支付配置
    // private Deposit deposit;// 预存款
    private String order;// 订单
    private String createDate;


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPaymentConfig() {
        return paymentConfig;
    }

    public void setPaymentConfig(String paymentConfig) {
        this.paymentConfig = paymentConfig;
    }

    public Integer getAuditState() {
        return auditState;
    }

    public void setAuditState(Integer auditState) {
        this.auditState = auditState;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPaymentConfigName() {
        return paymentConfigName;
    }

    public void setPaymentConfigName(String paymentConfigName) {
        this.paymentConfigName = paymentConfigName;
    }

    public Integer getRefundType() {
        return refundType;
    }

    public void setRefundType(Integer refundType) {
        this.refundType = refundType;
    }

    public String getRefundSn() {
        return refundSn;
    }

    public void setRefundSn(String refundSn) {
        this.refundSn = refundSn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RefundBean{" +
                "id='" + id + '\'' +
                ", refundSn='" + refundSn + '\'' +
                ", refundType=" + refundType +
                ", paymentConfigName='" + paymentConfigName + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", totalAmount=" + totalAmount +
                ", payee='" + payee + '\'' +
                ", operator='" + operator + '\'' +
                ", memo='" + memo + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", auditState=" + auditState +
                ", paymentConfig='" + paymentConfig + '\'' +
                ", order='" + order + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
