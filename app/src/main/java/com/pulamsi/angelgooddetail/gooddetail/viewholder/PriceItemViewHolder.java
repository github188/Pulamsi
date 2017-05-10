package com.pulamsi.angelgooddetail.gooddetail.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pulamsi.R;


/**
 * 商品价格视图容器
 *
 */
public class PriceItemViewHolder extends RecyclerView.ViewHolder {

  public CheckBox checkBox;
  public TextView priceContent;//商品价格

  public PriceItemViewHolder(View itemView) {
    super(itemView);

    checkBox = (CheckBox) itemView.findViewById(R.id.good_detail_price_checkbox);
    priceContent = (TextView) itemView.findViewById(R.id.good_detail_price_content);
  }

}
