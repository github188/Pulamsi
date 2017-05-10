package com.pulamsi.integral.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pulamsi.R;


/**
 * 积分明细视图容器
 *
 */
public class IntegralDetailViewHolder extends RecyclerView.ViewHolder {

  public TextView createDate;//明细日期
  public TextView num;//积分
  public TextView description;//明细描述

  public IntegralDetailViewHolder(View itemView) {
    super(itemView);

    description = (TextView) itemView.findViewById(R.id.integral_detail_description);
    num = (TextView) itemView.findViewById(R.id.integral_detail_num);
    createDate = (TextView) itemView.findViewById(R.id.integral_detail_createDate);
  }

}
