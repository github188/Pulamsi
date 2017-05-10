package com.pulamsi.angel.bean;

import com.pulamsi.home.entity.AngelMerchantsBean;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-13
 * Time: 15:47
 * FIXME
 */
public class AngelDateBean {

    private int totalCount;//天使总数
    private List<AngelMerchantsBean> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<AngelMerchantsBean> getList() {
        return list;
    }

    public void setList(List<AngelMerchantsBean> list) {
        this.list = list;
    }
}
