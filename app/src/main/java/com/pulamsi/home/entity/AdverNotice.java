package com.pulamsi.home.entity;

/**
 * Created by Administrator on 2016/3/20.
 */
public class AdverNotice {
    private String title;
    private String url;
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public AdverNotice(String title, String tag,String url) {
        this.title = title;
        this.url = url;
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
