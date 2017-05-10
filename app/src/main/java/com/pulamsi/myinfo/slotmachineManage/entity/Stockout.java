package com.pulamsi.myinfo.slotmachineManage.entity;

import java.io.Serializable;
import java.util.List;

public class Stockout implements Serializable {

	private static final long serialVersionUID = -1;
	private String createDate;
	private String id;//售货机编号
	private String modifyDate;
	private String orderDirection;
	private String orderProperty;
	private String pageNo;
	private String searchProperty;
	private String searchValue;
	private String venderName;//售货机名字
	private List<PortBean> portList;
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getOrderDirection() {
		return orderDirection;
	}
	public void setOrderDirection(String orderDirection) {
		this.orderDirection = orderDirection;
	}
	public String getOrderProperty() {
		return orderProperty;
	}
	public void setOrderProperty(String orderProperty) {
		this.orderProperty = orderProperty;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getSearchProperty() {
		return searchProperty;
	}
	public void setSearchProperty(String searchProperty) {
		this.searchProperty = searchProperty;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	public String getVenderName() {
		return venderName;
	}
	public void setVenderName(String venderName) {
		this.venderName = venderName;
	}
	public List<PortBean> getPortList() {
		return portList;
	}
	public void setPortList(List<PortBean> portList) {
		this.portList = portList;
	}
	

}
