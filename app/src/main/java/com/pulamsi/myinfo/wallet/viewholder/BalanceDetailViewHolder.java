package com.pulamsi.myinfo.wallet.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pulamsi.R;


/**
 * 交易明细子项
 */
public class BalanceDetailViewHolder extends RecyclerView.ViewHolder {

  public TextView dealTime; //时间
  public TextView dealSum; //金额
  public TextView comment; //备注
  public TextView status; //状态

  public BalanceDetailViewHolder(View itemView) {
    super(itemView);

    dealTime = (TextView) itemView.findViewById(R.id.balancedetail_fragment_dealtime);
    dealSum = (TextView) itemView.findViewById(R.id.balancedetail_fragment_dealsum);
    comment = (TextView) itemView.findViewById(R.id.balancedetail_fragment_comment);
    status = (TextView) itemView.findViewById(R.id.balancedetail_fragment_status);
  }
}
