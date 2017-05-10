package com.pulamsi.myinfo.wallet.entity;

import java.io.Serializable;


public class MemberAccount implements Serializable{
	
	/**
	 * 用户钱包
	 */
	private static final long serialVersionUID = 1L;
	private float memberAccountBalance;// 用户余额
	private float  machineAccountBalance;// 售货机余额
	private float  employAccountBalance;// 佣金余额

	private float  balanceCashYes;
	private float  balanceCashNo;
	private int lockStatus;// '0：正常,1：锁定',
	public float getMemberAccountBalance() {
		return memberAccountBalance;
	}
	public void setMemberAccountBalance(float memberAccountBalance) {
		this.memberAccountBalance = memberAccountBalance;
	}
	public float getMachineAccountBalance() {
		return machineAccountBalance;
	}
	public void setMachineAccountBalance(float machineAccountBalance) {
		this.machineAccountBalance = machineAccountBalance;
	}
	public float getEmployAccountBalance() {
		return employAccountBalance;
	}
	public void setEmployAccountBalance(float employAccountBalance) {
		this.employAccountBalance = employAccountBalance;
	}
	public float getBalanceCashYes() {
		return balanceCashYes;
	}
	public void setBalanceCashYes(float balanceCashYes) {
		this.balanceCashYes = balanceCashYes;
	}
	public float getBalanceCashNo() {
		return balanceCashNo;
	}
	public void setBalanceCashNo(float balanceCashNo) {
		this.balanceCashNo = balanceCashNo;
	}
	public int getLockStatus() {
		return lockStatus;
	}
	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}
}
