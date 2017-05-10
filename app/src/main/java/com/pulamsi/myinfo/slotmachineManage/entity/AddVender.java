package com.pulamsi.myinfo.slotmachineManage.entity;

import java.io.Serializable;


/**
 * 添加售货机
 */
public class AddVender implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String id;//售货机序号
	private String sn;//售货机编号
	private String goodRoadId;
	private String terminalName;//终端名字
	private String pos_PWD;//机器密码
	private String SellerTyp;//售货机型号
	private String TelNum;//服务电话
	private String provinceId;//省份id
	private String cityId;//城市id
	private String districtId;//区县id
	private String jiedao;//街道

	public String getSn() {
		return sn;
	}

	public AddVender setSn(String sn) {
		this.sn = sn;
		return this;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGoodRoadId() {
		return goodRoadId;
	}
	public void setGoodRoadId(String goodRoadId) {
		this.goodRoadId = goodRoadId;
	}
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public String getPos_PWD() {
		return pos_PWD;
	}
	public void setPos_PWD(String pos_PWD) {
		this.pos_PWD = pos_PWD;
	}
	public String getSellerTyp() {
		return SellerTyp;
	}
	public void setSellerTyp(String sellerTyp) {
		SellerTyp = sellerTyp;
	}
	public String getTelNum() {
		return TelNum;
	}
	public void setTelNum(String telNum) {
		TelNum = telNum;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getDistrictId() {
		return districtId;
	}
	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}
	public String getJiedao() {
		return jiedao;
	}
	public void setJiedao(String jiedao) {
		this.jiedao = jiedao;
	}
}
