package com.pulamsi.evaluate.entity;

import java.io.Serializable;

/**
 * 评论总记录数
 * Created by lanqiang on 16/1/5.
 */
public class EstimateCount implements Serializable {

    private static final long serialVersionUID = -1;

    private String total;//全部
    private String zhong;//中评
    private String good;//好评
    private String bad;//差评

    public String getTotal() {
        return total;
    }

    public EstimateCount setTotal(String total) {
        this.total = total;
        return this;
    }

    public String getZhong() {
        return zhong;
    }

    public EstimateCount setZhong(String zhong) {
        this.zhong = zhong;
        return this;
    }

    public String getGood() {
        return good;
    }

    public EstimateCount setGood(String good) {
        this.good = good;
        return this;
    }

    public String getBad() {
        return bad;
    }

    public EstimateCount setBad(String bad) {
        this.bad = bad;
        return this;
    }
}
