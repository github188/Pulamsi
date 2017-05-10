package com.pulamsi.myinfo.slotmachineManage.entity;

import java.io.Serializable;

//货道
public class PortBean implements Serializable{

	private static final long serialVersionUID = 1L;
	public static final int SLOT_OBJ_SIZE = 12;
	public static final int SLOTNAME_OBJ_SIZE = 16;
	private String id;
	private String machineid;//货机号
	private String innerid;
	private String amount;//现有的数量
	private float price;//当前价格
	private float discountPrice;//为0时，不打折，否则，原价
	private String discount;
	private String goodroadname;
	private String updatetime;
	private String error_id;
	private String productId;
	private String desc;
	private String PauseFlg;
	private String errorinfo;//故障信息
	private String lastErrorTime;//故障时间
	private String huodongid;
	private String product_img;
	private String inneridname;//货道号
	private String qrcode;
	private String qrcodeurl;
	private String wx_qrcode;
	private String wx_qrcodeurl;
	private String goodsid;
	private String deviceid;
	private String capacity;//货道容量
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMachineid() {
		return machineid;
	}
	public void setMachineid(String machineid) {
		this.machineid = machineid;
	}
	public String getInnerid() {
		return innerid;
	}
	public void setInnerid(String innerid) {
		this.innerid = innerid;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getGoodroadname() {
		return goodroadname;
	}
	public void setGoodroadname(String goodroadname) {
		this.goodroadname = goodroadname;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getError_id() {
		return error_id;
	}
	public void setError_id(String error_id) {
		this.error_id = error_id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public float getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(float discountPrice) {
		this.discountPrice = discountPrice;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPauseFlg() {
		return PauseFlg;
	}
	public void setPauseFlg(String pauseFlg) {
		PauseFlg = pauseFlg;
	}
	public String getErrorinfo() {
		return errorinfo;
	}
	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}
	public String getLastErrorTime() {
		return lastErrorTime;
	}
	public void setLastErrorTime(String lastErrorTime) {
		this.lastErrorTime = lastErrorTime;
	}
	public String getHuodongid() {
		return huodongid;
	}
	public void setHuodongid(String huodongid) {
		this.huodongid = huodongid;
	}
	public String getProduct_img() {
		return product_img;
	}
	public void setProduct_img(String product_img) {
		this.product_img = product_img;
	}
	public String getInneridname() {
		return inneridname;
	}
	public void setInneridname(String inneridname) {
		this.inneridname = inneridname;
	}
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public String getQrcodeurl() {
		return qrcodeurl;
	}
	public void setQrcodeurl(String qrcodeurl) {
		this.qrcodeurl = qrcodeurl;
	}
	public String getWx_qrcode() {
		return wx_qrcode;
	}
	public void setWx_qrcode(String wx_qrcode) {
		this.wx_qrcode = wx_qrcode;
	}
	public String getWx_qrcodeurl() {
		return wx_qrcodeurl;
	}
	public void setWx_qrcodeurl(String wx_qrcodeurl) {
		this.wx_qrcodeurl = wx_qrcodeurl;
	}
	public String getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
}
