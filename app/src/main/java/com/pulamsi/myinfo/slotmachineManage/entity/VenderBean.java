package com.pulamsi.myinfo.slotmachineManage.entity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 售货机对象
 */
public class VenderBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String sn;// 售货机编号
	private String terminalName;// 售货机名称
	private String AdminId;
//	private String BTime;
	private String terminalAddress;// 售货机地址
//	private String UpdateTime;// 更新时间
	private boolean isOnline;// 连接状态
	private String HuodongId;// 活动ID
	private String SellerTyp;// 售货机型号
	private String GoodsPortCount;// 货道数量
	private String TipMesOnLcd;
	private String CanUse;// 是否允许使用
	private String queueMaxLength;
	private String MdbDeviceStatus;
	private String gprs_Sign;// GPRS信号
	private String isActive;
	private String groupid;
	private String temperature;
	private byte[] priavte_key;
	private byte[] public_key;
	private String flags1;
	private String flags2;
	private String function_flg;// 实时状态
	private String gprs_event_flg;
	private String vmc_firmfile;
	private String parent_id;
	private String memberId;
	private String pos_TERM_NO;
	private String pos_INST_NO;
	private String pos_MERCH_NO;
	private String pos_CLIENTAUTHCOD;
	private String pos_USERNAME;
	private String pos_PWD;
	private String TelNum;// 服务电话
	private String bills;
	private String coinAtbox;
	private String coinAttube;
	private String IRErrCnt;// 红外异常次数
	private int LstSltE;// 最近故障
	private String jindu, weidu;// 经纬度
	private boolean isselect ;
	
	public boolean isIsselect() {
		return isselect;
	}
	public void setIsselect(boolean isselect) {
		this.isselect = isselect;
	}
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getAdminId() {
		return AdminId;
	}
	public void setAdminId(String adminId) {
		AdminId = adminId;
	}
//	public String getBTime() {
//		return BTime;
//	}
//	public void setBTime(String bTime) {
//		BTime = bTime;
//	}
	public String getTerminalAddress() {
		return terminalAddress;
	}
	public void setTerminalAddress(String terminalAddress) {
		this.terminalAddress = terminalAddress;
	}
//	public String getUpdateTime() {
//		return UpdateTime;
//	}
//	public void setUpdateTime(String updateTime) {
//		UpdateTime = updateTime;
//	}
	
	public boolean isOnline() {
		return isOnline;
	}
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	public String getHuodongId() {
		return HuodongId;
	}
	public void setHuodongId(String huodongId) {
		HuodongId = huodongId;
	}
	public String getSellerTyp() {
		return SellerTyp;
	}
	public void setSellerTyp(String sellerTyp) {
		SellerTyp = sellerTyp;
	}
	public String getGoodsPortCount() {
		return GoodsPortCount;
	}
	public void setGoodsPortCount(String goodsPortCount) {
		GoodsPortCount = goodsPortCount;
	}
	public String getTipMesOnLcd() {
		return TipMesOnLcd;
	}
	public void setTipMesOnLcd(String tipMesOnLcd) {
		TipMesOnLcd = tipMesOnLcd;
	}
	public String getCanUse() {
		return CanUse;
	}
	public void setCanUse(String canUse) {
		CanUse = canUse;
	}
	public String getQueueMaxLength() {
		return queueMaxLength;
	}
	public void setQueueMaxLength(String queueMaxLength) {
		this.queueMaxLength = queueMaxLength;
	}
	public String getMdbDeviceStatus() {
		return MdbDeviceStatus;
	}
	public void setMdbDeviceStatus(String mdbDeviceStatus) {
		MdbDeviceStatus = mdbDeviceStatus;
	}
	public String getGprs_Sign() {
		return gprs_Sign;
	}
	public void setGprs_Sign(String gprs_Sign) {
		this.gprs_Sign = gprs_Sign;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public byte[] getPriavte_key() {
		return priavte_key;
	}
	public void setPriavte_key(byte[] priavte_key) {
		this.priavte_key = priavte_key;
	}
	public byte[] getPublic_key() {
		return public_key;
	}
	public void setPublic_key(byte[] public_key) {
		this.public_key = public_key;
	}
	public String getFlags1() {
		return flags1;
	}
	public void setFlags1(String flags1) {
		this.flags1 = flags1;
	}
	public String getFlags2() {
		return flags2;
	}
	public void setFlags2(String flags2) {
		this.flags2 = flags2;
	}
	public String getFunction_flg() {
		return function_flg;
	}
	public void setFunction_flg(String function_flg) {
		this.function_flg = function_flg;
	}
	public String getGprs_event_flg() {
		return gprs_event_flg;
	}
	public void setGprs_event_flg(String gprs_event_flg) {
		this.gprs_event_flg = gprs_event_flg;
	}
	public String getVmc_firmfile() {
		return vmc_firmfile;
	}
	public void setVmc_firmfile(String vmc_firmfile) {
		this.vmc_firmfile = vmc_firmfile;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getPos_TERM_NO() {
		return pos_TERM_NO;
	}
	public void setPos_TERM_NO(String pos_TERM_NO) {
		this.pos_TERM_NO = pos_TERM_NO;
	}
	public String getPos_INST_NO() {
		return pos_INST_NO;
	}
	public void setPos_INST_NO(String pos_INST_NO) {
		this.pos_INST_NO = pos_INST_NO;
	}
	public String getPos_MERCH_NO() {
		return pos_MERCH_NO;
	}
	public void setPos_MERCH_NO(String pos_MERCH_NO) {
		this.pos_MERCH_NO = pos_MERCH_NO;
	}
	public String getPos_CLIENTAUTHCOD() {
		return pos_CLIENTAUTHCOD;
	}
	public void setPos_CLIENTAUTHCOD(String pos_CLIENTAUTHCOD) {
		this.pos_CLIENTAUTHCOD = pos_CLIENTAUTHCOD;
	}
	public String getPos_USERNAME() {
		return pos_USERNAME;
	}
	public void setPos_USERNAME(String pos_USERNAME) {
		this.pos_USERNAME = pos_USERNAME;
	}
	public String getPos_PWD() {
		return pos_PWD;
	}
	public void setPos_PWD(String pos_PWD) {
		this.pos_PWD = pos_PWD;
	}
	public String getTelNum() {
		return TelNum;
	}
	public void setTelNum(String telNum) {
		TelNum = telNum;
	}
	public String getBills() {
		return bills;
	}
	public void setBills(String bills) {
		this.bills = bills;
	}
	public String getCoinAtbox() {
		return coinAtbox;
	}
	public void setCoinAtbox(String coinAtbox) {
		this.coinAtbox = coinAtbox;
	}
	public String getCoinAttube() {
		return coinAttube;
	}
	public void setCoinAttube(String coinAttube) {
		this.coinAttube = coinAttube;
	}
	public String getIRErrCnt() {
		return IRErrCnt;
	}
	public void setIRErrCnt(String iRErrCnt) {
		IRErrCnt = iRErrCnt;
	}
	
	public int getLstSltE() {
		return LstSltE;
	}
	public void setLstSltE(int lstSltE) {
		LstSltE = lstSltE;
	}
	public String getJindu() {
		return jindu;
	}
	public void setJindu(String jindu) {
		this.jindu = jindu;
	}
	public String getWeidu() {
		return weidu;
	}
	public void setWeidu(String weidu) {
		this.weidu = weidu;
	}


	public boolean isselect() {
		return isselect;
	}

	public void setIsOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	@Override
	public String toString() {
		return "VenderBean{" +
				"id='" + id + '\'' +
				", sn='" + sn + '\'' +
				", terminalName='" + terminalName + '\'' +
				", AdminId='" + AdminId + '\'' +
				", terminalAddress='" + terminalAddress + '\'' +
				", isOnline=" + isOnline +
				", HuodongId='" + HuodongId + '\'' +
				", SellerTyp='" + SellerTyp + '\'' +
				", GoodsPortCount='" + GoodsPortCount + '\'' +
				", TipMesOnLcd='" + TipMesOnLcd + '\'' +
				", CanUse='" + CanUse + '\'' +
				", queueMaxLength='" + queueMaxLength + '\'' +
				", MdbDeviceStatus='" + MdbDeviceStatus + '\'' +
				", gprs_Sign='" + gprs_Sign + '\'' +
				", isActive='" + isActive + '\'' +
				", groupid='" + groupid + '\'' +
				", temperature='" + temperature + '\'' +
				", priavte_key=" + Arrays.toString(priavte_key) +
				", public_key=" + Arrays.toString(public_key) +
				", flags1='" + flags1 + '\'' +
				", flags2='" + flags2 + '\'' +
				", function_flg='" + function_flg + '\'' +
				", gprs_event_flg='" + gprs_event_flg + '\'' +
				", vmc_firmfile='" + vmc_firmfile + '\'' +
				", parent_id='" + parent_id + '\'' +
				", memberId='" + memberId + '\'' +
				", pos_TERM_NO='" + pos_TERM_NO + '\'' +
				", pos_INST_NO='" + pos_INST_NO + '\'' +
				", pos_MERCH_NO='" + pos_MERCH_NO + '\'' +
				", pos_CLIENTAUTHCOD='" + pos_CLIENTAUTHCOD + '\'' +
				", pos_USERNAME='" + pos_USERNAME + '\'' +
				", pos_PWD='" + pos_PWD + '\'' +
				", TelNum='" + TelNum + '\'' +
				", bills='" + bills + '\'' +
				", coinAtbox='" + coinAtbox + '\'' +
				", coinAttube='" + coinAttube + '\'' +
				", IRErrCnt='" + IRErrCnt + '\'' +
				", LstSltE=" + LstSltE +
				", jindu='" + jindu + '\'' +
				", weidu='" + weidu + '\'' +
				", isselect=" + isselect +
				'}';
	}
}
