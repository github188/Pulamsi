package com.pulamsi.integral.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lanqiang on 16/1/7.
 * 积分明细
 */
public class IntegralDetailResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pageNumber;
    private String  count;
    private List<IntegralDetail> list;

    public String getPageNumber() {
        return pageNumber;
    }

    public IntegralDetailResult setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public String getCount() {
        return count;
    }

    public IntegralDetailResult setCount(String count) {
        this.count = count;
        return this;
    }

    public List<IntegralDetail> getList() {
        return list;
    }

    public IntegralDetailResult setList(List<IntegralDetail> list) {
        this.list = list;
        return this;
    }
}
