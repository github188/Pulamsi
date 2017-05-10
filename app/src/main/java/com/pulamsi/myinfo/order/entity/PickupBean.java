package com.pulamsi.myinfo.order.entity;

import java.io.Serializable;

public class PickupBean implements Serializable {

	private static final long serialVersionUID = -1;
	private String id;
	private String gmtpayment;// 支付时间
	private String partner;// 支付者编号（可以为空）
	private String buyeremail;// 支付人
	private String tradeno;// 取货码
	private String totalfee;// 金额
	private String gmtcreate;// 创建时间
	private String subject;// 商品名称
	private String tradestatus;// 交易状态
	private String machineid;// 机器编号
	private String goodsid;// 商品编号
	private String inneridname;// 货道名
	private String transforstatus;// 取货状态(0是没有取货，1是取货成功，2是取货失败)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGmtpayment() {
		return gmtpayment;
	}
	public void setGmtpayment(String gmtpayment) {
		this.gmtpayment = gmtpayment;
	}
	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getBuyeremail() {
		return buyeremail;
	}
	public void setBuyeremail(String buyeremail) {
		this.buyeremail = buyeremail;
	}
	public String getTradeno() {
		return tradeno;
	}
	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}
	public String getTotalfee() {
		return totalfee;
	}
	public void setTotalfee(String totalfee) {
		this.totalfee = totalfee;
	}
	public String getGmtcreate() {
		return gmtcreate;
	}
	public void setGmtcreate(String gmtcreate) {
		this.gmtcreate = gmtcreate;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTradestatus() {
		return tradestatus;
	}
	public void setTradestatus(String tradestatus) {
		this.tradestatus = tradestatus;
	}
	public String getMachineid() {
		return machineid;
	}
	public void setMachineid(String machineid) {
		this.machineid = machineid;
	}
	public String getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	public String getInneridname() {
		return inneridname;
	}
	public void setInneridname(String inneridname) {
		this.inneridname = inneridname;
	}
	public String getTransforstatus() {
		return transforstatus;
	}
	public void setTransforstatus(String transforstatus) {
		this.transforstatus = transforstatus;
	}

}
