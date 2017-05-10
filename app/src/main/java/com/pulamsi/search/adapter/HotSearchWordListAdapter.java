package com.pulamsi.search.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.pulamsi.R;
import com.pulamsi.search.SearchDoorActivity;
import com.pulamsi.search.SearchListActivity;
import com.pulamsi.search.entity.HotSearchWordItemDO;
import com.pulamsi.search.viewholder.HotSearchWordItemViewHolder;
import com.pulamsi.views.AutoFitViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 热门搜索词列表的数据适配器
 *
 * @author WilliamChik on 15/9/29.
 */
public class HotSearchWordListAdapter extends AutoFitViewGroup.Adapter<AutoFitViewGroup.ViewHolder> {

  private List<Object> mData = new ArrayList<Object>();

  private LayoutInflater mInflater;

  private SearchDoorActivity searchDoorActivity;

  public HotSearchWordListAdapter(Activity activity) {
    mInflater = activity.getLayoutInflater();
    this.searchDoorActivity = (SearchDoorActivity) activity;
  }

  @Override
  public HotSearchWordItemViewHolder onCreateViewHolder() {
    View convertView = mInflater.inflate(R.layout.hot_search_word_list_item, null);
    if (convertView == null) {
      return null;
    }
    return new HotSearchWordItemViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(AutoFitViewGroup.ViewHolder holder, final int position) {
    if (holder == null || !(holder instanceof HotSearchWordItemViewHolder) || !(getItem(
        position) instanceof HotSearchWordItemDO)) {
      return;
    }

    final HotSearchWordItemViewHolder newHolder = (HotSearchWordItemViewHolder) holder;
    final HotSearchWordItemDO newItem = (HotSearchWordItemDO) getItem(position);

    if (!TextUtils.isEmpty(newItem.getHotWordName())) {
      newHolder.hotSearchWordItemTv.setText(newItem.getHotWordName());
    }

    newHolder.hotSearchWordItemTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent search = new Intent(searchDoorActivity,SearchListActivity.class);
        search.putExtra("keyword",newItem.getHotWordName());
        searchDoorActivity.startActivity(search);
      }
    });
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  public Object getItem(int position) {
    if (position < getItemCount()) {
      return mData.get(position);
    }

    return null;
  }

  public List<Object> getList() {
    return mData;
  }

  public void addDataList(List<HotSearchWordItemDO> newDataList) {
    this.mData.addAll(newDataList);
  }

}
