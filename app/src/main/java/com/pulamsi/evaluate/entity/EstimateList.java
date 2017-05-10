package com.pulamsi.evaluate.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 评论列表
 * Created by lanqiang on 16/1/5.
 */
public class EstimateList implements Serializable {

    private static final long serialVersionUID = -1;

    private String count;// 总记录数
    private String pageNumber;// 当前页码
    private ArrayList<Estimate> list;// 数据
    private String totalPage;// 总页数

    public String getCount() {
        return count;
    }

    public EstimateList setCount(String count) {
        this.count = count;
        return this;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public EstimateList setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public ArrayList<Estimate> getList() {
        return list;
    }

    public EstimateList setList(ArrayList<Estimate> list) {
        this.list = list;
        return this;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public EstimateList setTotalPage(String totalPage) {
        this.totalPage = totalPage;
        return this;
    }
}
