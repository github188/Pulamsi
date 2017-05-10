package com.pulamsi.slotmachine.entity;

import com.lidroid.xutils.db.annotation.Id;

import java.io.Serializable;


/**
 * 城市
 */
public class Area implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    /**
     * 名字
     */
    private String name;
    /**
     * 父类id
     */
    private String pid;

    public String getId() {
        return id;
    }

    public Area setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Area setName(String name) {
        this.name = name;
        return this;
    }

    public String getPid() {
        return pid;
    }

    public Area setPid(String pid) {
        this.pid = pid;
        return this;
    }
}
