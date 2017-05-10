package com.pulamsi.start.init.entity;

import com.lidroid.xutils.db.annotation.Id;

import java.io.Serializable;


/**
 * 权限实体类
 */
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    /**
     * 权限名
     */
    private String name;
    /**
     * 父名字
     */
    private String pname;

    public String getId() {
        return id;
    }

    public Role setId(String id) {
        this.id = id;
        return this;
    }

    public String getPname() {
        return pname;
    }

    public Role setPname(String pname) {
        this.pname = pname;
        return this;
    }

    public String getName() {
        return name;
    }

    public Role setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pname='" + pname + '\'' +
                '}';
    }
}
