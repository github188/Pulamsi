package com.pulamsi.integral.entity;


import java.io.Serializable;

/**
 * Created by lanqiang on 16/1/7.
 * 积分明细
 */
public class IntegralDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private int state;// 0:支出;1:收入
    private int num;// 积分
    private String createDate;//明细日期
    private String description;//明细表述

    public int getState() {
        return state;
    }

    public IntegralDetail setState(int state) {
        this.state = state;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IntegralDetail setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCreateDate() {
        return createDate;
    }

    public IntegralDetail setCreateDate(String createDate) {
        this.createDate = createDate;
        return this;
    }

    public int getNum() {
        return num;
    }

    public IntegralDetail setNum(int num) {
        this.num = num;
        return this;
    }
}
