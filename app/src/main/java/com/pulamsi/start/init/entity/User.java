package com.pulamsi.start.init.entity;

import com.lidroid.xutils.db.annotation.Id;

import java.io.Serializable;


/**
 * 登录用户
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private int id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String pass;
    /**
     * 是否登录
     */
    private boolean hasLogin;
    /**
     * 电话
     */
    private String mobile;

    /**
     * 版本号
     */
    private int versionCode;

    public String getUserId() {
        return userId;
    }

    public User setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public User setVersionCode(int versionCode) {
        this.versionCode = versionCode;
        return this;
    }

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPass() {
        return pass;
    }

    public User setPass(String pass) {
        this.pass = pass;
        return this;
    }

    public boolean isHasLogin() {
        return hasLogin;
    }

    public User setHasLogin(boolean hasLogin) {
        this.hasLogin = hasLogin;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public User setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", pass='" + pass + '\'' +
                ", hasLogin=" + hasLogin +
                ", mobile='" + mobile + '\'' +
                ", versionCode=" + versionCode +
                '}';
    }
}
