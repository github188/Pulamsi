package com.pulamsi.home.entity;

import com.lidroid.xutils.db.annotation.Id;

/**
 * @author lq 淘宝商家
 */
public class ChannelMobile {

    @Id
    private String id;
    private String logo;// 店铺图片

    private String name;// 店铺名称

    private String nameDetail;// 店铺简述

    private String orderList;// 排序

    private String url;// 店铺地址

    private String userSn;// 客户编号

    private String phone;

    private String tel;

    private String qq;

    private String sinaWeibo;

    private String weiXin;

    private String signDate;

    private String userName;// 客户名称

    private String adress;// 住址

    private String principal;// 负责人

    private String describes;// 描述

    private String qqWeibo;

    private String auditState; // 审核状态 0否1是 2待审核

    private String invalidState; // 作废状态

    private String channelType;

    private String role;// 授权等级

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() { return logo;}

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameDetail() {
        return nameDetail;
    }

    public void setNameDetail(String nameDetail) {
        this.nameDetail = nameDetail;
    }

    public String getOrderList() {
        return orderList;
    }

    public void setOrderList(String orderList) {
        this.orderList = orderList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserSn() {
        return userSn;
    }

    public void setUserSn(String userSn) {
        this.userSn = userSn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getSinaWeibo() {
        return sinaWeibo;
    }

    public void setSinaWeibo(String sinaWeibo) {
        this.sinaWeibo = sinaWeibo;
    }

    public String getWeiXin() {
        return weiXin;
    }

    public void setWeiXin(String weiXin) {
        this.weiXin = weiXin;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getQqWeibo() {
        return qqWeibo;
    }

    public void setQqWeibo(String qqWeibo) {
        this.qqWeibo = qqWeibo;
    }

    public String getAuditState() {
        return auditState;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public String getInvalidState() {
        return invalidState;
    }

    public void setInvalidState(String invalidState) {
        this.invalidState = invalidState;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
