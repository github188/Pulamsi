package com.pulamsi.home.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 淘宝商家请求包装类，返回淘宝商家数据列表和分页信息
 */
public class ChannelMobileTotal implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int pageNumber;
	private int pageSize;
	private int total;
	private List<ChannelMobile> list;
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<ChannelMobile> getList() {
		return list;
	}
	public void setList(List<ChannelMobile> list) {
		this.list = list;
	}
	

}
