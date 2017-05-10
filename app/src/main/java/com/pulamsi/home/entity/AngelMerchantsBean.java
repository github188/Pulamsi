package com.pulamsi.home.entity;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-10
 * Time: 16:20
 * FIXME
 */
public class AngelMerchantsBean {

    private String id;
    private String sellerImg;// 店主展示照片
    private String shopName;// 店铺名称
    private String statement;// 个性签名
    private Long sales;// 店铺销量
    private String address;// 店铺地址
    private String sellerMobile;// 天使联络人手机
    private Boolean isBindingSm;// 是否实名
    private Boolean isBindingYh;// 是否绑定银行卡

    // 方法上写入
    // private float sellerSpeed;// 天使速度
    private float sellerAfter;// 天使售后
    private float sellerServe;// 天使服务
    private float sellerCredit;// 正能量

    // private String typeImg;// 等级图片
    private String typeName;// 等级名称
    private int typeDigit;// 等級数字

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerImg() {
        return sellerImg;
    }

    public void setSellerImg(String sellerImg) {
        this.sellerImg = sellerImg;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Long getSales() {
        return sales;
    }

    public void setSales(Long sales) {
        this.sales = sales;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSellerMobile() {
        return sellerMobile;
    }

    public void setSellerMobile(String sellerMobile) {
        this.sellerMobile = sellerMobile;
    }

    public Boolean getIsBindingSm() {
        return isBindingSm;
    }

    public void setIsBindingSm(Boolean isBindingSm) {
        this.isBindingSm = isBindingSm;
    }

    public Boolean getIsBindingYh() {
        return isBindingYh;
    }

    public void setIsBindingYh(Boolean isBindingYh) {
        this.isBindingYh = isBindingYh;
    }

    public float getSellerAfter() {
        return sellerAfter;
    }

    public void setSellerAfter(float sellerAfter) {
        this.sellerAfter = sellerAfter;
    }

    public float getSellerServe() {
        return sellerServe;
    }

    public void setSellerServe(float sellerServe) {
        this.sellerServe = sellerServe;
    }

    public float getSellerCredit() {
        return sellerCredit;
    }

    public void setSellerCredit(float sellerCredit) {
        this.sellerCredit = sellerCredit;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeDigit() {
        return typeDigit;
    }

    public void setTypeDigit(int typeDigit) {
        this.typeDigit = typeDigit;
    }
}
