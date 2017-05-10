package com.pulamsi.snapup.bean;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-08-26
 * Time: 14:48
 * FIXME
 */
public class SnapUpPackBean {
    private String name;//分类名称

    private List<SnapUpBean> list;//抢购列表数据

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SnapUpBean> getList() {
        return list;
    }

    public void setList(List<SnapUpBean> list) {
        this.list = list;
    }
}
