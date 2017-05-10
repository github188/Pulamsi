package com.pulamsi.myinfo.slotmachineManage.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulamsi.R;


/**
 * 售货机货道列表视图容器
 *
 */
public class SlotmachineGoodsRoadListViewHolder extends RecyclerView.ViewHolder {
  public TextView goodsroadNum;// 货道号
  public TextView goodsroadName;// 名字
  public TextView goodsSurplus;// 剩余
  public TextView price;// 价格

  public SlotmachineGoodsRoadListViewHolder(View itemView) {
    super(itemView);
    goodsroadNum = (TextView) itemView.findViewById(1);
    goodsroadName = (TextView) itemView.findViewById(2);
    goodsSurplus = (TextView) itemView.findViewById(3);
    price = (TextView) itemView.findViewById(4);
  }

}
