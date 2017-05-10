package com.pulamsi.myinfo.slotmachineManage.entity;

import java.io.Serializable;

public class TradeBean implements Serializable {

	private static final long serialVersionUID = -1;
	private String id;
	private String liushuiid;/* 订单号 */
	private String cardinfo;/* 卡片信息，刷卡交易时有效 */
	private String price;/* 商品价格 */
	private String mobilephone; /* 用户手机号码 */
	private String receivetime; /**/
	private String tradetype;/* 交易类型 */
	private String goodmachineid;/* 机器编号 */
	private String goodroadid;/* 货道编号 */
	private String changestatus;/* 支付状态 */
	private String sendstatus;/* 出货状态 */
	private String SmsContent;/* 短信内容 */
	private String orderid;/**/
	private String changes;/* 找零金额 */
	private String goodsName;/* 商品名称 */
	private String inneridname;
	private String xmlstr;
	private String status;
	private String bill_credit;/* 纸币实收金额 */
	private String coin_credit;/* 硬币实收金额 */
	private String pageNo;// 分页号;
	private String orderProperty;
	private String orderDirection;
	private String searchValue;
	private String searchProperty;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLiushuiid() {
		return liushuiid;
	}

	public void setLiushuiid(String liushuiid) {
		this.liushuiid = liushuiid;
	}

	public String getCardinfo() {
		return cardinfo;
	}

	public void setCardinfo(String cardinfo) {
		this.cardinfo = cardinfo;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getReceivetime() {
		return receivetime;
	}

	public void setReceivetime(String receivetime) {
		this.receivetime = receivetime;
	}

	public String getTradetype() {
		return tradetype;
	}

	public void setTradetype(String tradetype) {
		this.tradetype = tradetype;
	}

	public String getGoodmachineid() {
		return goodmachineid;
	}

	public void setGoodmachineid(String goodmachineid) {
		this.goodmachineid = goodmachineid;
	}

	public String getGoodroadid() {
		return goodroadid;
	}

	public void setGoodroadid(String goodroadid) {
		this.goodroadid = goodroadid;
	}

	public String getChangestatus() {
		return changestatus;
	}

	public void setChangestatus(String changestatus) {
		this.changestatus = changestatus;
	}

	public String getSendstatus() {
		return sendstatus;
	}

	public void setSendstatus(String sendstatus) {
		this.sendstatus = sendstatus;
	}

	public String getSmsContent() {
		return SmsContent;
	}

	public void setSmsContent(String smsContent) {
		SmsContent = smsContent;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getChanges() {
		return changes;
	}

	public void setChanges(String changes) {
		this.changes = changes;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getInneridname() {
		return inneridname;
	}

	public void setInneridname(String inneridname) {
		this.inneridname = inneridname;
	}

	public String getXmlstr() {
		return xmlstr;
	}

	public void setXmlstr(String xmlstr) {
		this.xmlstr = xmlstr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBill_credit() {
		return bill_credit;
	}

	public void setBill_credit(String bill_credit) {
		this.bill_credit = bill_credit;
	}

	public String getCoin_credit() {
		return coin_credit;
	}

	public void setCoin_credit(String coin_credit) {
		this.coin_credit = coin_credit;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getOrderProperty() {
		return orderProperty;
	}

	public void setOrderProperty(String orderProperty) {
		this.orderProperty = orderProperty;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getSearchProperty() {
		return searchProperty;
	}

	public void setSearchProperty(String searchProperty) {
		this.searchProperty = searchProperty;
	}

}
