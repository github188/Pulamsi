package com.pulamsi.search.entity;

import com.lidroid.xutils.db.annotation.Id;

/**
 * 搜索历史数据对象
 *
 * @author WilliamChik on 15/9/29.
 */
public class SearchHistoryItemDO {

  @Id
  private int id;

  private String historyWordStr;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getHistoryWordStr() {
    return historyWordStr;
  }

  public void setHistoryWordStr(String historyWordStr) {
    this.historyWordStr = historyWordStr;
  }
}
