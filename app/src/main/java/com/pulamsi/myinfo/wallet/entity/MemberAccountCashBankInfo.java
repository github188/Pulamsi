package com.pulamsi.myinfo.wallet.entity;

import java.io.Serializable;


public class MemberAccountCashBankInfo implements Serializable{
	
	/**
	 * 银行卡信息
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String bankAccountName;
	private String bankName;
	private Boolean isDefault = false;// '0：非默认1：默认',
	private String hideID;// 银行卡号
	private String tailID;// 后四位
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBankAccountName() {
		return bankAccountName;
	}
	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public String getHideID() {
		return hideID;
	}
	public void setHideID(String hideID) {
		this.hideID = hideID;
	}
	public String getTailID() {
		return tailID;
	}
	public void setTailID(String tailID) {
		this.tailID = tailID;
	}
	
	
	
}
