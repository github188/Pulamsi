package com.pulamsi.category.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pulamsi.R;


/**
 * @author WilliamChik on 15/9/24.
 */
public class CategoryTopCatItemViewHolder extends RecyclerView.ViewHolder {

  public TextView topCatItemTv;

  public CategoryTopCatItemViewHolder(View itemView) {
    super(itemView);
    topCatItemTv = (TextView) itemView.findViewById(R.id.tv_category_top_cat_item);
  }
}
