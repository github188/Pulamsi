package com.pulamsi.utils.update.bean;

/**
 * Created by hehe on 2016/3/3.
 */
public class Apkinfo {

    private String version;
    private String introduction;
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        return "Apkinfo{" +
                "version=" + version +
                ", introduction='" + introduction + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
