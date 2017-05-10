package com.pulamsi.home.entity;

import com.lidroid.xutils.db.annotation.Id;

import java.io.Serializable;


/**
 * 图片轮换
 */
public class IndexSource implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private int id;
    private String img;//图片路径
    private String name;//轮换名
    private String price;//价格
    private String url;//跳转url
    private String memo;//备注
    private String mold;//类型：1商品，2分类，3通知

    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getMold() {
        return mold;
    }

    public void setMold(String mold) {
        this.mold = mold;
    }

    @Override
    public String toString() {
        return "IndexSource{" +
                "id=" + id +
                ", img='" + img + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", url='" + url + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
}
