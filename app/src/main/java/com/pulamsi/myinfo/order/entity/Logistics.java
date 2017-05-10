package com.pulamsi.myinfo.order.entity;

import java.io.Serializable;
import java.util.List;

/**
 *	物流信息
 */

public class Logistics implements Serializable {

	private static final long serialVersionUID = 1L;
	private String codenumber;//物流单号
	private String com;//物流公司代码
	private String companytype;
	private String condition;
	private List<LogisticsData> data;
	private String ischeck;
	private String message;
	private String nu;
	private String state;
	private String status;
	private String updatetime;
	public String getCodenumber() {
		return codenumber;
	}
	public void setCodenumber(String codenumber) {
		this.codenumber = codenumber;
	}
	public String getCom() {
		return com;
	}
	public void setCom(String com) {
		this.com = com;
	}
	public String getCompanytype() {
		return companytype;
	}
	public void setCompanytype(String companytype) {
		this.companytype = companytype;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public List<LogisticsData> getData() {
		return data;
	}
	public void setData(List<LogisticsData> data) {
		this.data = data;
	}
	public String getIscheck() {
		return ischeck;
	}
	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getNu() {
		return nu;
	}
	public void setNu(String nu) {
		this.nu = nu;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
}
