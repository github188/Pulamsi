package com.pulamsi.myinfo.myteam.entity;

import java.io.Serializable;

/**
 * 
 * @author 强
 *	团队
 */
public class TeamBean implements Serializable {

	private static final long serialVersionUID = -1;
	private String cDate;
	private String[] childrenModules;
	private String fatherId;
	private String id;
	private int level;
	private String memberId;
	private String mobile;
	private String modifyDate;
	private String orderDirection;
	private String orderProperty;
	private String pageNo;
	private String parent;
	private String path;
	private String path_length;
	private String searchProperty;
	private String searchValue;
	private String username;
	private String venderRankName;//等级
	
	public String getVenderRankName() {
		return venderRankName;
	}
	public void setVenderRankName(String venderRankName) {
		this.venderRankName = venderRankName;
	}
	public String getcDate() {
		return cDate;
	}
	public void setcDate(String cDate) {
		this.cDate = cDate;
	}
	public String[] getChildrenModules() {
		return childrenModules;
	}
	public void setChildrenModules(String[] childrenModules) {
		this.childrenModules = childrenModules;
	}
	public String getFatherId() {
		return fatherId;
	}
	public void setFatherId(String fatherId) {
		this.fatherId = fatherId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPath_length() {
		return path_length;
	}
	public void setPath_length(String path_length) {
		this.path_length = path_length;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
