package com.pulamsi.myinfo.setting.entity;


import com.lidroid.xutils.db.annotation.NoAutoIncrement;

import java.io.Serializable;

/**
 *	帐户信息
 */

public class Member implements Serializable {

	private static final long serialVersionUID = 1L;
	@NoAutoIncrement
	private int id = 1;
	private String username;// 用户名
	private String email;// E-mail
	private String safeQuestion;// 密码保护问题
	private String safeAnswer;// 密码保护问题答案
	private int point;// 积分
	private int deposit;// 预存款
	private int amount;// 消费金额
	private int balance;// 余额
	private String phone;// 联系电话
	private int zipCode;// 邮编
	private String mobile;// 手机号
	private String hideMobile;// 手机号
	private String imgUrl;// 头像地址/
	private String name;// 昵称
	private String mobileValidate;// 手机验证
	private String emailValidate;// 邮箱验证
	private String sex;// 性别
	private String birthDay;// 生日
	private String pskin;// 肤质
	private String realName;// 姓名
	private String cardNo;// 身份证
	private String provinceId;// 省
	private String provinceName;// 省
	private String cityId;// 城市
	private String cityName;// 城市
	private String districtId;// 街道
	private String districtName;// 街道
	private String adress;// 详细地址
	private String qq;// qq


	public String getAdress() {
		return adress;
	}

	public Member setAdress(String adress) {
		this.adress = adress;
		return this;
	}

	public int getAmount() {
		return amount;
	}

	public Member setAmount(int amount) {
		this.amount = amount;
		return this;
	}

	public int getBalance() {
		return balance;
	}

	public Member setBalance(int balance) {
		this.balance = balance;
		return this;
	}

	public String getCardNo() {
		return cardNo;
	}

	public Member setCardNo(String cardNo) {
		this.cardNo = cardNo;
		return this;
	}

	public String getCityId() {
		return cityId;
	}

	public Member setCityId(String cityId) {
		this.cityId = cityId;
		return this;
	}

	public String getCityName() {
		return cityName;
	}

	public Member setCityName(String cityName) {
		this.cityName = cityName;
		return this;
	}

	public int getDeposit() {
		return deposit;
	}

	public Member setDeposit(int deposit) {
		this.deposit = deposit;
		return this;
	}

	public String getDistrictId() {
		return districtId;
	}

	public Member setDistrictId(String districtId) {
		this.districtId = districtId;
		return this;
	}

	public String getDistrictName() {
		return districtName;
	}

	public Member setDistrictName(String districtName) {
		this.districtName = districtName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Member setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getEmailValidate() {
		return emailValidate;
	}

	public Member setEmailValidate(String emailValidate) {
		this.emailValidate = emailValidate;
		return this;
	}

	public String getHideMobile() {
		return hideMobile;
	}

	public Member setHideMobile(String hideMobile) {
		this.hideMobile = hideMobile;
		return this;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public Member setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
		return this;
	}

	public String getMobile() {
		return mobile;
	}

	public Member setMobile(String mobile) {
		this.mobile = mobile;
		return this;
	}

	public String getMobileValidate() {
		return mobileValidate;
	}

	public Member setMobileValidate(String mobileValidate) {
		this.mobileValidate = mobileValidate;
		return this;
	}

	public String getName() {
		return name;
	}

	public Member setName(String name) {
		this.name = name;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public Member setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public int getPoint() {
		return point;
	}

	public Member setPoint(int point) {
		this.point = point;
		return this;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public Member setProvinceId(String provinceId) {
		this.provinceId = provinceId;
		return this;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public Member setProvinceName(String provinceName) {
		this.provinceName = provinceName;
		return this;
	}

	public String getPskin() {
		return pskin;
	}

	public Member setPskin(String pskin) {
		this.pskin = pskin;
		return this;
	}

	public String getQq() {
		return qq;
	}

	public Member setQq(String qq) {
		this.qq = qq;
		return this;
	}

	public String getRealName() {
		return realName;
	}

	public Member setRealName(String realName) {
		this.realName = realName;
		return this;
	}

	public String getSafeAnswer() {
		return safeAnswer;
	}

	public Member setSafeAnswer(String safeAnswer) {
		this.safeAnswer = safeAnswer;
		return this;
	}

	public String getSafeQuestion() {
		return safeQuestion;
	}

	public Member setSafeQuestion(String safeQuestion) {
		this.safeQuestion = safeQuestion;
		return this;
	}

	public String getSex() {
		return sex;
	}

	public Member setSex(String sex) {
		this.sex = sex;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public Member setUsername(String username) {
		this.username = username;
		return this;
	}

	public int getZipCode() {
		return zipCode;
	}

	public Member setZipCode(int zipCode) {
		this.zipCode = zipCode;
		return this;
	}
	public String getBirthDay() {
		return birthDay;
	}

	public Member setBirthDay(String birthDay) {
		this.birthDay = birthDay;
		return this;
	}

	@Override
	public String toString() {
		return "Member{" +
				"username='" + username + '\'' +
				", email='" + email + '\'' +
				", safeQuestion='" + safeQuestion + '\'' +
				", safeAnswer='" + safeAnswer + '\'' +
				", point=" + point +
				", deposit=" + deposit +
				", amount=" + amount +
				", balance=" + balance +
				", phone='" + phone + '\'' +
				", zipCode=" + zipCode +
				", mobile='" + mobile + '\'' +
				", hideMobile='" + hideMobile + '\'' +
				", imgUrl='" + imgUrl + '\'' +
				", name='" + name + '\'' +
				", mobileValidate='" + mobileValidate + '\'' +
				", emailValidate='" + emailValidate + '\'' +
				", sex='" + sex + '\'' +
				", birthDay='" + birthDay + '\'' +
				", pskin='" + pskin + '\'' +
				", realName='" + realName + '\'' +
				", cardNo='" + cardNo + '\'' +
				", provinceId='" + provinceId + '\'' +
				", provinceName='" + provinceName + '\'' +
				", cityId='" + cityId + '\'' +
				", cityName='" + cityName + '\'' +
				", districtId='" + districtId + '\'' +
				", districtName='" + districtName + '\'' +
				", adress='" + adress + '\'' +
				", qq='" + qq + '\'' +
				'}';
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
