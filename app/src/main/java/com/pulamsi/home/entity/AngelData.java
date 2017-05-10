package com.pulamsi.home.entity;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-12
 * Time: 11:59
 * FIXME
 */
public class AngelData {

    private int  totalPage;//总页数
    private int  sum;//天使数量
    private List<AngelMerchantsBean> sellerList;

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public List<AngelMerchantsBean> getSellerList() {
        return sellerList;
    }

    public void setSellerList(List<AngelMerchantsBean> sellerList) {
        this.sellerList = sellerList;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
