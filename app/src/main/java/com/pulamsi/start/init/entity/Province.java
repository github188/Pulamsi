package com.pulamsi.start.init.entity;

import com.lidroid.xutils.db.annotation.Id;

import java.io.Serializable;


/**
 * 省份
 */
public class Province implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    /**
     * 名字
     */
    private String name;

    private String orderid;

    public String getOrderid() {
        return orderid;
    }

    public Province setOrderid(String orderid) {
        this.orderid = orderid;
        return this;
    }

    public String getId() {
        return id;
    }

    public Province setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Province setName(String name) {
        this.name = name;
        return this;
    }
}
