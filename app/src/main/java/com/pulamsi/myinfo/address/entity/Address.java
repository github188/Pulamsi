package com.pulamsi.myinfo.address.entity;

import java.io.Serializable;

public class Address implements Serializable {

	private static final long serialVersionUID = 1L;
	private String  id;//地址id
	private String mid;
	private String name;//收货人
	private String mobile;// 手机
	private String zipCode;// 邮编
	private String isDefault;// 是否是默认地址
	private String provinceId;
	private String provinceName;//省名字
	private String cityId;
	private String cityName;//城市名字
	private String countyId;
	private String countyName;//城区名字
	private String addressAlias;//详细地址

	public String getIsDefault() {
		return isDefault;
	}

	public Address setIsDefault(String isDefault) {
		this.isDefault = isDefault;
		return this;
	}

	public String getMid() {
		return mid;
	}

	public Address setMid(String mid) {
		this.mid = mid;
		return this;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCountyId() {
		return countyId;
	}
	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	public String getAddressAlias() {
		return addressAlias;
	}
	public void setAddressAlias(String addressAlias) {
		this.addressAlias = addressAlias;
	}

}
