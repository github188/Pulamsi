package com.pulamsi.start.init.entity;

import com.lidroid.xutils.db.annotation.Id;

import java.io.Serializable;


/**
 * 区域
 */
public class District implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    /**
     * 名字
     */
    private String name;
    /**
     * 城市id
     */
    private String cityId;
    /**
     *
     */
    private String postCode;

    public String getId() {
        return id;
    }

    public District setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public District setName(String name) {
        this.name = name;
        return this;
    }

    public String getCityId() {
        return cityId;
    }

    public District setCityId(String cityId) {
        this.cityId = cityId;
        return this;
    }

    public String getPostCode() {
        return postCode;
    }

    public District setPostCode(String postCode) {
        this.postCode = postCode;
        return this;
    }

}
