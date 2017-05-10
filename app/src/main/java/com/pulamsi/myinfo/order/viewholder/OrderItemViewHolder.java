package com.pulamsi.myinfo.order.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pulamsi.R;
import com.taobao.uikit.feature.view.TRecyclerView;


/**
 * 商品子项viewholder
 */
public class OrderItemViewHolder extends RecyclerView.ViewHolder {

  public ImageView itemIcon; //店铺图片
  public TextView storename; //店铺名字
  public TextView orderstate; //订单状态
  public TRecyclerView item_trecyclerview; //订单货物列表
  public TextView ordercontent; //订单信息
  public TextView grey; //取消订单
  public TextView affirm; //确认订单
  public TextView pickupcode; //取货码
  public TextView trace; //查看物流
  public TextView evluate; //查看物流
  public TextView submit; //付款
  public TextView refundStatus;//退款
  public LinearLayout merchants;//商户

  public OrderItemViewHolder(View itemView) {
    super(itemView);

    itemIcon = (ImageView) itemView.findViewById(R.id.orderlist_item_icon);
    storename = (TextView) itemView.findViewById(R.id.orderlist_item_storename);
    orderstate = (TextView) itemView.findViewById(R.id.orderlist_orderstate);
    refundStatus = (TextView) itemView.findViewById(R.id.orderlist_refund);
    item_trecyclerview = (TRecyclerView) itemView.findViewById(R.id.orderlist_item_trecyclerview);
    ordercontent = (TextView) itemView.findViewById(R.id.orderlist_ordercontent);
    grey = (TextView) itemView.findViewById(R.id.tv_btn_my_order_list_item_grey);
    affirm = (TextView) itemView.findViewById(R.id.tv_btn_my_order_list_item_affirm);
    pickupcode = (TextView) itemView.findViewById(R.id.tv_btn_my_order_list_item_pickupcode);
    trace = (TextView) itemView.findViewById(R.id.tv_btn_my_order_list_item_trace);
    evluate = (TextView) itemView.findViewById(R.id.tv_btn_my_order_list_item_evluate);
    submit = (TextView) itemView.findViewById(R.id.tv_btn_my_order_list_item_submit);
    merchants = (LinearLayout) itemView.findViewById(R.id.orderlist_item_merchants);
  }
}
