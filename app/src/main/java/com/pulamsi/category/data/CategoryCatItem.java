package com.pulamsi.category.data;

import java.io.Serializable;
import java.util.List;

/**
 * 【分类】页面 分类数据对象
 *
 */
public class CategoryCatItem implements Serializable {

  private static final long serialVersionUID = 1L;
  private String id;
  private String name;
  private String price;
  private String image;
  private Integer orderlist;
  private List<CategoryCatItem> list;

  public String getId() {
    return id;
  }

  public CategoryCatItem setId(String id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public CategoryCatItem setName(String name) {
    this.name = name;
    return this;
  }

  public String getPrice() {
    return price;
  }

  public CategoryCatItem setPrice(String price) {
    this.price = price;
    return this;
  }

  public String getImage() {
    return image;
  }

  public CategoryCatItem setImage(String image) {
    this.image = image;
    return this;
  }

  public Integer getOrderlist() {
    return orderlist;
  }

  public CategoryCatItem setOrderlist(Integer orderlist) {
    this.orderlist = orderlist;
    return this;
  }

  public List<CategoryCatItem> getList() {
    return list;
  }

  public CategoryCatItem setList(List<CategoryCatItem> list) {
    this.list = list;
    return this;
  }
}
