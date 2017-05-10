package com.pulamsi.myinfo.wallet.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulamsi.R;


/**
 * 银行卡列表子项
 */
public class BankinfoViewHolder extends RecyclerView.ViewHolder {

  public ImageView bankicon; //银行图标
  public TextView bankhideid; //银行卡号
  public TextView bankaccountname; //姓名
  public TextView del; //删除

  public BankinfoViewHolder(View itemView) {
    super(itemView);

    bankicon = (ImageView) itemView.findViewById(R.id.bankinfo_item_bankicon);
    bankhideid = (TextView) itemView.findViewById(R.id.bankinfo_item_bankhideid);
    bankaccountname = (TextView) itemView.findViewById(R.id.bankinfo_item_bankaccountname);
    del = (TextView) itemView.findViewById(R.id.bankinfo_item_del);
    itemView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (null != bankinfoCallback){
          bankinfoCallback.deleBankinfoPosition();
        }
      }
    });

  }

  public interface BankinfoCallback {
    public abstract void deleBankinfoPosition();
  }
  private BankinfoCallback bankinfoCallback;

  public void setDeleAddressCallback(BankinfoCallback bankinfoCallback) {
    this.bankinfoCallback = bankinfoCallback;
  }
}
