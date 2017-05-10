package com.pulamsi.start.init.entity;

import com.lidroid.xutils.db.annotation.Id;

import java.io.Serializable;


/**
 * 城市
 */
public class City implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    /**
     * 名字
     */
    private String name;
    /**
     * 省份id
     */
    private String provinceId;

    private String areaCode;

    private String orderid;


    public String getOrderid() {
        return orderid;
    }

    public City setOrderid(String orderid) {
        this.orderid = orderid;
        return this;
    }

    public String getId() {
        return id;
    }

    public City setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public City setName(String name) {
        this.name = name;
        return this;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public City setProvinceId(String provinceId) {
        this.provinceId = provinceId;
        return this;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public City setAreaCode(String areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    @Override
    public String toString() {
        return "City{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", orderid='" + orderid + '\'' +
                '}';
    }
}
