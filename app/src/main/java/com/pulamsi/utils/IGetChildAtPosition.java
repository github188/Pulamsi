package com.pulamsi.utils;

import android.widget.TextView;

/**
 * 用于获取当前列表指定位置的 item view 的接口
 *
 * @author WilliamChik on 15/9/25.
 */
public interface IGetChildAtPosition {

  /**
   * 在列表中根据指定位置找到对应的 View
   *
   * 注意这里不能使用 RecyclerView 的 getChildAt(position) 和 StaggeredGridLayoutManager 的 getChildAt(position) 方法获取指定位置的 child view。
   * 因为列表 item 的重用问题，该两个方法获取到的 child view 数量上限是列表在一页中最多显示的 item 数量，例如列表一页只能显示10个 item，
   * 则 getChildAt() 方法的索引最多只能是9。用 StaggeredGridLayoutManager 的 findViewByPosition(position) 可解决问题。
   *
   * @param clickPosition 点击位置
   */
  TextView getChildAtPosition(int clickPosition);
  
}
