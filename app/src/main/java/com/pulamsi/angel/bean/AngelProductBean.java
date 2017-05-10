package com.pulamsi.angel.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-23
 * Time: 15:29
 * FIXME
 */
public class AngelProductBean implements Serializable {

    String id;
    private String seller_id;// 天使id
    private String shopName;// 店铺名
    private String shopImg;// 店主展示照片
    private Boolean isPass;// 审核处理状态 0未审核 1待审核 2审核通过 3审核失败 2=true 其他=fasle
    private String sn;// 商品编号
    private String name;// 商品名称
    private String fullName;// 商品名称
    private String pic;// 商品展示图片
    private BigDecimal price;
    private BigDecimal marketPrice;// 市场价格
    private String seoTitle;// 商品title
    private String introduction;// 介绍
    private String productCategoryName;// 商品类别
    private boolean isAutoSell = false;//是否可以自取
    private List<String> productImages;// 产品图片

    private Boolean isMarketable;// 是否上架
    private Boolean isRecommend;// 是否作为店主推荐展示商品 0否 1是

    private Long sales; // 销量
    private Long weekSales;// 每星期销量
    private Long monthSales;// 月销量

    private float averagScore;// 商品评价 平均分
    private int count;// 评价次数

    private Boolean isSeller;// 是否天使商品

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }

    public Boolean getIsPass() {
        return isPass;
    }

    public void setIsPass(Boolean isPass) {
        this.isPass = isPass;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public List<String> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<String> productImages) {
        this.productImages = productImages;
    }

    public Boolean getIsMarketable() {
        return isMarketable;
    }

    public void setIsMarketable(Boolean isMarketable) {
        this.isMarketable = isMarketable;
    }

    public Boolean getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Boolean isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Long getSales() {
        return sales;
    }

    public void setSales(Long sales) {
        this.sales = sales;
    }

    public Long getWeekSales() {
        return weekSales;
    }

    public void setWeekSales(Long weekSales) {
        this.weekSales = weekSales;
    }

    public Long getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(Long monthSales) {
        this.monthSales = monthSales;
    }

    public float getAveragScore() {
        return averagScore;
    }

    public void setAveragScore(float averagScore) {
        this.averagScore = averagScore;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boolean getIsSeller() {
        return isSeller;
    }

    public void setIsSeller(Boolean isSeller) {
        this.isSeller = isSeller;
    }


    public boolean isAutoSell() {
        return isAutoSell;
    }

    public void setIsAutoSell(boolean isAutoSell) {
        this.isAutoSell = isAutoSell;
    }
}
