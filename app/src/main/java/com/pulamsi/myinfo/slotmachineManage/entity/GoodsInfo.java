package com.pulamsi.myinfo.slotmachineManage.entity;

import java.io.Serializable;

/**
 * 货道商品
 */
public class GoodsInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String goodsname;//商品名字
	private String price;
	private String picname;
	private String des1;
	private String des2;
	private String des3;
	private Integer groupid;
	private String searchValue;
	private String inventory;
	private boolean isselect;
	
	
	
	public boolean isIsselect() {
		return isselect;
	}
	public void setIsselect(boolean isselect) {
		this.isselect = isselect;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPicname() {
		return picname;
	}
	public void setPicname(String picname) {
		this.picname = picname;
	}
	public String getDes1() {
		return des1;
	}
	public void setDes1(String des1) {
		this.des1 = des1;
	}
	public String getDes2() {
		return des2;
	}
	public void setDes2(String des2) {
		this.des2 = des2;
	}
	public String getDes3() {
		return des3;
	}
	public void setDes3(String des3) {
		this.des3 = des3;
	}
	public Integer getGroupid() {
		return groupid;
	}
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	public String getInventory() {
		return inventory;
	}
	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
}
