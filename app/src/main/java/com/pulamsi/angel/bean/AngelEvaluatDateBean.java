package com.pulamsi.angel.bean;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-13
 * Time: 15:47
 * FIXME
 */
public class AngelEvaluatDateBean {

    private int totalCount;//天使总数
    private List<AngelEvaluationBean> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<AngelEvaluationBean> getList() {
        return list;
    }

    public void setList(List<AngelEvaluationBean> list) {
        this.list = list;
    }
}
