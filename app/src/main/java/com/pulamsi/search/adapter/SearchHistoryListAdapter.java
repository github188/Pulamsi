package com.pulamsi.search.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.pulamsi.R;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.search.entity.SearchHistoryItemDO;
import com.pulamsi.search.viewholder.SearchHistoryWordItemViewHolder;


/**
 * 搜索历史列表数据适配器
 *
 * @author WilliamChik on 15/9/29.
 */
public class SearchHistoryListAdapter extends HaiBaseListAdapter<SearchHistoryItemDO> {

  public SearchHistoryListAdapter(Activity activity) {
    super(activity);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = mInflater.inflate(R.layout.search_door_history_word_list_item, null);
    if (convertView == null) {
      return null;
    }
    return new SearchHistoryWordItemViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (!(holder instanceof SearchHistoryWordItemViewHolder) || !(getItem(position) instanceof SearchHistoryItemDO)) {
      return;
    }

    final SearchHistoryWordItemViewHolder newHolder = (SearchHistoryWordItemViewHolder) holder;
    final SearchHistoryItemDO newItem = (SearchHistoryItemDO) getItem(position);

    if (!TextUtils.isEmpty(newItem.getHistoryWordStr())) {
      newHolder.historyWordItemTv.setText(newItem.getHistoryWordStr());
    }
  }
}
