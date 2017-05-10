package com.pulamsi.home.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 首页店主列表元素视图容器
 *
 * @author WilliamChik on 15/8/12.
 */
public class HomeDianzhuViewHolder extends RecyclerView.ViewHolder {

  public ImageView imageView;
  public TextView dianzhuName;

  public HomeDianzhuViewHolder(View itemView) {
    super(itemView);

    imageView = (ImageView) itemView.findViewById(1);
    dianzhuName = (TextView) itemView.findViewById(2);

  }

}
