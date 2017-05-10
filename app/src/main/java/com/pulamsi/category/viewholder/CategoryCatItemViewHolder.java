package com.pulamsi.category.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulamsi.R;


/**
 * 【分类】页面分类项视图容器（目录和国家两种分类公用）
 *
 * @author WilliamChik on 2015/8/10.
 */
public class CategoryCatItemViewHolder extends RecyclerView.ViewHolder {

  public ImageView catImg;

  public TextView catNameTv;

  public CategoryCatItemViewHolder(View itemView) {
    super(itemView);

    catImg = (ImageView) itemView.findViewById(R.id.civ_category_cat_list_item_img);
    catNameTv = (TextView) itemView.findViewById(R.id.tv_category_cat_list_item_name);
  }
}
