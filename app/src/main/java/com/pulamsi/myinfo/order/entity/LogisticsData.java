package com.pulamsi.myinfo.order.entity;

import java.io.Serializable;


/**
 * 物流信息子项
 */
public class LogisticsData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String context;//内容
	private String ftime;
	private String time;//时间
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getFtime() {
		return ftime;
	}
	public void setFtime(String ftime) {
		this.ftime = ftime;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}


	@Override
	public String toString() {
		return "LogisticsData{" +
				"context='" + context + '\'' +
				", ftime='" + ftime + '\'' +
				", time='" + time + '\'' +
				'}';
	}
}
