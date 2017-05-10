package com.pulamsi.search.viewholder;

import android.view.View;
import android.widget.TextView;

import com.pulamsi.R;
import com.pulamsi.views.AutoFitViewGroup;


/**
 * 热门搜索词视图容器
 *
 * @author WilliamChik on 15/9/29.
 */
public class HotSearchWordItemViewHolder extends AutoFitViewGroup.ViewHolder {

  public TextView hotSearchWordItemTv;

  public HotSearchWordItemViewHolder(View itemView) {
    super(itemView);
    hotSearchWordItemTv = (TextView) itemView.findViewById(R.id.tv_hot_search_word_item);
  }
}
