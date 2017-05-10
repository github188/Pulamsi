package com.pulamsi.home.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 首页分类列表元素视图容器
 *
 * @author WilliamChik on 15/8/12.
 */
public class HomeCateGoodViewHolder extends RecyclerView.ViewHolder {

  public ImageView imageView;
  public TextView explain;
  public TextView price;
  public TextView historical;

  public HomeCateGoodViewHolder(View itemView) {
    super(itemView);

    imageView = (ImageView) itemView.findViewById(1);
    explain = (TextView) itemView.findViewById(2);
    price = (TextView) itemView.findViewById(3);
    historical = (TextView) itemView.findViewById(4);

  }

}
