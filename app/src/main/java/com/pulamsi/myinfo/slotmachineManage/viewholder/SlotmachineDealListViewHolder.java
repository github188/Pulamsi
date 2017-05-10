package com.pulamsi.myinfo.slotmachineManage.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


/**
 * 售货机交易查询视图容器
 *
 */
public class SlotmachineDealListViewHolder extends RecyclerView.ViewHolder {
  public TextView slotmachine_id;// 货机号
  public TextView dealTime;//交易时间
  public TextView goodsRoadid;// 货道号
  public TextView goodsName;// 商品名
  public TextView price;// 金额

  public SlotmachineDealListViewHolder(View itemView) {
    super(itemView);
    slotmachine_id = (TextView) itemView.findViewById(1);
    dealTime = (TextView) itemView.findViewById(2);
    goodsRoadid = (TextView) itemView.findViewById(3);
    goodsName = (TextView) itemView.findViewById(4);
    price = (TextView) itemView.findViewById(5);
  }

}
