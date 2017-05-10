package com.pulamsi.home.entity;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-07-07
 * Time: 16:56
 * FIXME
 */
public class SnapUpData {

    private long  panicBuyEndTime;//结束时间
    private List<HotSellProduct> proList;//抢购数据


    public long  getPanicBuyEndTime() {
        return panicBuyEndTime;
    }
    public void setPanicBuyEndTime(long  panicBuyEndTime) {
        this.panicBuyEndTime = panicBuyEndTime;
    }
    public List<HotSellProduct> getProList() {
        return proList;
    }
    public void setProList(List<HotSellProduct> proList) {
        this.proList = proList;
    }
}
