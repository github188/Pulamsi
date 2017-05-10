package com.pulamsi.snapup.bean;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-08-26
 * Time: 09:56
 * FIXME
 */
public class SnapUpBean {
    String id;
    String name;
    String price;
    String panicBuyPrice;
    String marketPrice;// 市场价格
    String image;
    int orderlist;
    String categoryId;
    String sn;// 商品编号
    long panicBuyBeginTimeLong;// 抢购开始时间时间截
    String sales;// 销量
    String totalEstimate;// 总评论数
    int panicBuyQuantity;//抢购限购数量

    String beginTime;//抢购开始时间，转化string
    String endTime;//抢购结束时间,转化String


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPanicBuyPrice() {
        return panicBuyPrice;
    }

    public void setPanicBuyPrice(String panicBuyPrice) {
        this.panicBuyPrice = panicBuyPrice;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getOrderlist() {
        return orderlist;
    }

    public void setOrderlist(int orderlist) {
        this.orderlist = orderlist;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public long getPanicBuyBeginTimeLong() {
        return panicBuyBeginTimeLong;
    }

    public void setPanicBuyBeginTimeLong(long panicBuyBeginTimeLong) {
        this.panicBuyBeginTimeLong = panicBuyBeginTimeLong;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getTotalEstimate() {
        return totalEstimate;
    }

    public void setTotalEstimate(String totalEstimate) {
        this.totalEstimate = totalEstimate;
    }

    public int getPanicBuyQuantity() {
        return panicBuyQuantity;
    }

    public void setPanicBuyQuantity(int panicBuyQuantity) {
        this.panicBuyQuantity = panicBuyQuantity;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
