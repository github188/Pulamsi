package com.pulamsi.home.entity;

import java.io.Serializable;
import java.util.List;

//搜索货物
public class SearchGoodsList implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int totalCount;
	private List<HotSellProduct> list;
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<HotSellProduct> getList() {
		return list;
	}
	public void setList(List<HotSellProduct> list) {
		this.list = list;
	}
	

}
