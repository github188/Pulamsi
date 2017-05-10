package com.pulamsi.base.baseAdapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;


import java.util.ArrayList;
import java.util.List;


/**
 * 基础RecyclerView列表适配器，封装一些基本的操作
 *
 * @author WilliamChik on 15/7/31.
 *
 * 通常需要继承这个Adapter
 */
public abstract class HaiBaseListAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  protected LayoutInflater mInflater;
  protected List<T> mData = new ArrayList<T>();
  protected Activity mActivity;

  public HaiBaseListAdapter(Activity activity) {
    mActivity = activity;
    mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public void clearDataList() {
    mData.clear();
  }

  /**
   * 在原数据的基础上添加新的数据集
   */
  public void addDataList(List<T> newDataList) {
    this.mData.addAll(newDataList);
  }
  //获取Item
  public T getItem(int position) {
    if (position < getItemCount()) {
      return this.mData.get(position);
    }
    return null;
  }

  public void removeItem(int position) {
    mData.remove(position);
  }

  public int getItemPosition(T item) {
    return mData.indexOf(item);
  }

  public List<T> getDataList() {
    return mData;
  }
  /**
   * 销毁时的相关操作，通常在这里销毁一些大对象，如图片、Handler、PopupWindow、Activity、Dialog等可能引起内存泄露的元素。
   */
  public void destroy() {

  }
}
