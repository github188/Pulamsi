package com.pulamsi.myinfo.wallet.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.pulamsi.R;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.myinfo.wallet.entity.MemberAccountTran;
import com.pulamsi.myinfo.wallet.viewholder.BalanceDetailViewHolder;

/**
 *
 * 交易明细适配器
 *
 */
public class BalanceDetailListAdapter extends HaiBaseListAdapter<MemberAccountTran> {

  private Activity activity;


  public BalanceDetailListAdapter(Activity activity) {
    super(activity);
    this.activity = activity;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View convertView = mInflater.inflate(R.layout.balancedetail_fragment_item, null);
    if (convertView == null) {
      return null;
    }

    return new BalanceDetailViewHolder(convertView);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    if (holder == null || !(holder instanceof BalanceDetailViewHolder) || !(getItem(position) instanceof MemberAccountTran)) {
      return;
    }

    BalanceDetailViewHolder newHolder = (BalanceDetailViewHolder) holder;
    final MemberAccountTran newItem = (MemberAccountTran) getItem(position);

    //时间
    if (!TextUtils.isEmpty(newItem.getTransactionDate())){
      newHolder.dealTime.setText(newItem.getTransactionDate().substring(0,10));
    }else {
      newHolder.dealTime.setText("暂无时间");
    }

    //金额
    if (newItem.getTransactionMode() == 0){
      newHolder.dealSum.setText("+" + newItem.getTransactionMoney());
    }else if (newItem.getTransactionMode() == 1){
      newHolder.dealSum.setText("-" + newItem.getTransactionMoney());
    }
    //备注
    if (!TextUtils.isEmpty(newItem.getConsumeTitle())){
      newHolder.comment.setText(newItem.getConsumeTitle());
    }else {
      newHolder.comment.setText("暂无信息");
    }

    //状态
    if (!TextUtils.isEmpty(newItem.getTransactionStatusName())){
      newHolder.status.setText(newItem.getTransactionStatusName());
    }else {
      newHolder.status.setText("暂无信息");
    }

  }
}
