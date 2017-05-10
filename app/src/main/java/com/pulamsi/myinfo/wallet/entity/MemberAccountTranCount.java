package com.pulamsi.myinfo.wallet.entity;

import java.io.Serializable;
import java.util.List;


public class MemberAccountTranCount implements Serializable{
	
	/**
	 * 钱包交易明细
	 */
	private static final long serialVersionUID = 1L;
	private List<MemberAccountTran> accountTranMobileList;
	private int count;
	
	public List<MemberAccountTran> getAccountTranMobileList() {
		return accountTranMobileList;
	}
	public void setAccountTranMobileList(List<MemberAccountTran> accountTranMobileList) {
		this.accountTranMobileList = accountTranMobileList;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
