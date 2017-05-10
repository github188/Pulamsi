package com.pulamsi.myinfo.order.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pulamsi.R;
import com.taobao.uikit.feature.view.TRecyclerView;


/**
 * 订单列表产品子项
 */
public class OrderProductViewHolder extends RecyclerView.ViewHolder {

  public ImageView itemIcon; //产品图片
  public TextView name; //产品名字
  public TextView pice; //产品价格
  public TextView parameter; //产品参数
  public TextView num; //产品数量
  public TextView share; //分享有礼

  public OrderProductViewHolder(View itemView) {
    super(itemView);

    itemIcon = (ImageView) itemView.findViewById(R.id.orderlist_product_item_icon);
    name = (TextView) itemView.findViewById(R.id.orderlist_product_item_name);
    pice = (TextView) itemView.findViewById(R.id.orderlist_product_item_pice);
    parameter = (TextView) itemView.findViewById(R.id.orderlist_product_item_parameter);
    num = (TextView) itemView.findViewById(R.id.orderlist_product_item_num);
    share = (TextView) itemView.findViewById(R.id.orderlist_product_item_share);
  }
}
