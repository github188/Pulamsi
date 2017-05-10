package com.pulamsi.search.entity;

/**
 * 热门搜索词数据对象
 *
 * @author WilliamChik on 15/9/29.
 */
public class HotSearchWordItemDO {

  private int hotWordId;

  private String hotWordName;

  public String getHotWordName() {
    return hotWordName;
  }

  public HotSearchWordItemDO setHotWordName(String hotWordName) {
    this.hotWordName = hotWordName;
    return this;
  }

  public int getHotWordId() {
    return hotWordId;
  }

  public HotSearchWordItemDO setHotWordId(int hotWordId) {
    this.hotWordId = hotWordId;
    return this;
  }
}
