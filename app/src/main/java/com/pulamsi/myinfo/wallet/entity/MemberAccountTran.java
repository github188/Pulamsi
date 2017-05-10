package com.pulamsi.myinfo.wallet.entity;

import java.io.Serializable;


public class MemberAccountTran implements Serializable{
	
	/**
	 * 钱包交易明细
	 */
	private static final long serialVersionUID = 1L;
	
	private String tradeNo;// 交易号
	private String outTradeNo;// 订单号
	private String consumeTitle;// 消费标题
	private Integer transactionMode; // '0：入账（充值等增值类型）;1：出帐（消费等扣减类型）',
	private String transactionDate;
	private Integer transactionInTypeBig;// '1：充值;2：收入; 3：赠送; 4：退款; ',
	private Integer transactionInTypeSmall;// '待定; ',
	private Integer transactionOutTypeBig;// '1：消费;2：转出;3：押金; 4：提现;5：发送短信; ',
	private Integer transactionOutTypeSmall; // '待定
	private Float transactionMoney;
	private Float memberAccountBalance;
	private String transactionRemark;
	private Integer transactionStatus;// 状态 0交易失败 ；1交易成功 ；2等待付款 ； 3处理中
	private String transactionStatusName;// 状态 0交易失败 ；1交易成功 ；2等待付款 ； 3处理中
	private Boolean isCashFlag;// '0：不可提现 1：可提现',//默认可提现
	
	
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getConsumeTitle() {
		return consumeTitle;
	}
	public void setConsumeTitle(String consumeTitle) {
		this.consumeTitle = consumeTitle;
	}
	public Integer getTransactionMode() {
		return transactionMode;
	}
	public void setTransactionMode(Integer transactionMode) {
		this.transactionMode = transactionMode;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public Integer getTransactionInTypeBig() {
		return transactionInTypeBig;
	}
	public void setTransactionInTypeBig(Integer transactionInTypeBig) {
		this.transactionInTypeBig = transactionInTypeBig;
	}
	public Integer getTransactionInTypeSmall() {
		return transactionInTypeSmall;
	}
	public void setTransactionInTypeSmall(Integer transactionInTypeSmall) {
		this.transactionInTypeSmall = transactionInTypeSmall;
	}
	public Integer getTransactionOutTypeBig() {
		return transactionOutTypeBig;
	}
	public void setTransactionOutTypeBig(Integer transactionOutTypeBig) {
		this.transactionOutTypeBig = transactionOutTypeBig;
	}
	public Integer getTransactionOutTypeSmall() {
		return transactionOutTypeSmall;
	}
	public void setTransactionOutTypeSmall(Integer transactionOutTypeSmall) {
		this.transactionOutTypeSmall = transactionOutTypeSmall;
	}
	public Float getTransactionMoney() {
		return transactionMoney;
	}
	public void setTransactionMoney(Float transactionMoney) {
		this.transactionMoney = transactionMoney;
	}
	public Float getMemberAccountBalance() {
		return memberAccountBalance;
	}
	public void setMemberAccountBalance(Float memberAccountBalance) {
		this.memberAccountBalance = memberAccountBalance;
	}
	public String getTransactionRemark() {
		return transactionRemark;
	}
	public void setTransactionRemark(String transactionRemark) {
		this.transactionRemark = transactionRemark;
	}
	public Integer getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(Integer transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public String getTransactionStatusName() {
		return transactionStatusName;
	}
	public void setTransactionStatusName(String transactionStatusName) {
		this.transactionStatusName = transactionStatusName;
	}
	public Boolean getIsCashFlag() {
		return isCashFlag;
	}
	public void setIsCashFlag(Boolean isCashFlag) {
		this.isCashFlag = isCashFlag;
	}
	
	
	
}
