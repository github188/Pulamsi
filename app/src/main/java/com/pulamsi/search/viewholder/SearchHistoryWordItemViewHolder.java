package com.pulamsi.search.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pulamsi.R;

/**
 * 搜索历史视图容器
 *
 * @author WilliamChik on 15/9/29.
 */
public class SearchHistoryWordItemViewHolder extends RecyclerView.ViewHolder {

  public TextView historyWordItemTv;

  public SearchHistoryWordItemViewHolder(View itemView) {
    super(itemView);
    historyWordItemTv = (TextView) itemView.findViewById(R.id.tv_search_door_history_word_item);
  }
}
