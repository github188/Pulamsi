package com.pulamsi.myinfo.order.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.angel.AngelDetailsActivity;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.evaluate.AddEvaluateActivity;
import com.pulamsi.myinfo.order.ChoicePayTypeActivity;
import com.pulamsi.myinfo.order.MyOrderFragment;
import com.pulamsi.myinfo.order.OrderDetailActivity;
import com.pulamsi.myinfo.order.TraceOrderActivity;
import com.pulamsi.myinfo.order.entity.Order;
import com.pulamsi.myinfo.order.entity.PickupBean;
import com.pulamsi.myinfo.order.viewholder.OrderItemViewHolder;
import com.pulamsi.start.MainActivity;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.dialog.CommonAlertDialog;
import com.taobao.uikit.feature.view.TRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单列表适配器
 */
public class OrderListAdapter extends HaiBaseListAdapter<Order> {

    private OrderProductAdapter orderProductAdapter;
    private Activity activity;
    private MyOrderFragment myOrderFragment;
    private String[] cleanOrderStr = {"现在不想购买", "价格波动", "重复下单", "收货人信息有误", "发货时间长", "其他原因"};
    private int DividerIte = 5;


    public  OrderListAdapter(Activity activity, MyOrderFragment myOrderFragment) {
        super(activity);
        this.activity = activity;
        this.myOrderFragment = myOrderFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = mInflater.inflate(R.layout.orderlist_item, null);
        if (convertView == null) {
            return null;
        }

        return new OrderItemViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder == null || !(holder instanceof OrderItemViewHolder) || !(getItem(position) instanceof Order)) {
            return;
        }

        final OrderItemViewHolder newHolder = (OrderItemViewHolder) holder;
        final Order newItem = (Order) getItem(position);


        orderProductAdapter = new OrderProductAdapter(activity);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        newHolder.item_trecyclerview.setLayoutManager(layoutManager);
        newHolder.item_trecyclerview.setAdapter(orderProductAdapter);
        newHolder.item_trecyclerview.setHasFixedSize(true);
        newHolder.item_trecyclerview.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                HaiLog.d("点击订单子项", newItem.getId());
                Intent orderDetail = new Intent(activity, OrderDetailActivity.class);
                orderDetail.putExtra("order", newItem);
                activity.startActivity(orderDetail);
            }
        });

        //  状态还原
        int formerItemCount = orderProductAdapter.getItemCount();
        orderProductAdapter.clearDataList();
        orderProductAdapter.notifyItemRangeRemoved(0, formerItemCount);
        orderProductAdapter.addDataList(newItem.getOrderItemSet());
        orderProductAdapter.notifyItemRangeInserted(0, newItem.getOrderItemSet().size());
        newHolder.item_trecyclerview.getLayoutParams().height =
                PulamsiApplication.context.getResources().getDimensionPixelOffset(R.dimen.orderlist_product_height) * newItem.getOrderItemSet().size();


        //确认订单
        newHolder.affirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonAlertDialog alertDialog = new CommonAlertDialog(activity, "确定收货?", "确定", "取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        affirmGoods(position);
                    }
                });
                alertDialog.show();
            }
        });
        //取消订单
        newHolder.grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (("0".equals(newItem.getPaymentStatus()) || "2".equals(newItem.getPaymentStatus())) && !"3".equals(newItem.getOrderStatus()) && Integer.parseInt(newItem.getShippingStatus()) != 5) {
                    //取消订单方法
                    new AlertDialog.Builder(activity).setSingleChoiceItems(cleanOrderStr, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cleanOrder(position, cleanOrderStr[which]);
                            dialog.dismiss();
                        }
                    }).create().show();

                } else {
                    //删除订单方法
                    CommonAlertDialog alertDialog = new CommonAlertDialog(activity, "确定删除?", "确定", "取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteOrder(position);
                        }
                    });
                    alertDialog.show();
                }
            }
        });
        //查看物流
        newHolder.trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TraceOrderActivity.class);
                intent.putExtra("orderSn", newItem.getOrderSn());
                activity.startActivity(intent);
            }
        });
        //付款
        newHolder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(newItem.getShippingStatus()) == 5 && newItem.getEstimateSate() == 1) {
                    //未评论
                    Intent addEvaluate = new Intent(activity, AddEvaluateActivity.class);
                    addEvaluate.putExtra("order", newItem);
                    activity.startActivity(addEvaluate);
                }else {
                    //付款
                    Intent pay = new Intent(activity, ChoicePayTypeActivity.class);
                    pay.putExtra("order", newItem);
                    activity.startActivity(pay);
                }
            }
        });


        newHolder.evluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(newItem.getShippingStatus()) == 5 && newItem.getEstimateSate() == 2) {
                    //已评论
                    Intent addEvaluate = new Intent(activity, AddEvaluateActivity.class);
                    addEvaluate.putExtra("order", newItem);
                    activity.startActivity(addEvaluate);
                }
            }
        });


        newHolder.pickupcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPickUpCode(position);
            }
        });

        // <c:if test="${order.orderStatus==0}">未处理</c:if>
        // <c:if test="${order.orderStatus==1}" >已处理</c:if>
        // <c:if test="${order.orderStatus==2}" >已完成</c:if>
        // <c:if test="${order.orderStatus==3}" >已作废</c:if>
        // <c:if test="${order.paymentStatus==0}" >未支付</c:if>
        // <c:if test="${order.paymentStatus==1}" >部分支付</c:if>
        // <c:if test="${order.paymentStatus==2}" >已支付</c:if>
        // <c:if test="${order.paymentStatus==3}" >部分退款</c:if>
        // <c:if test="${order.paymentStatus==4}" >全额退款</c:if>
        // <c:if test="${order.shippingStatus==0}" >未发货</c:if>
        // <c:if test="${order.shippingStatus==1}" >部分发货</c:if>
        // <c:if test="${order.shippingStatus==2}" >已发货</c:if>
        // <c:if test="${order.shippingStatus==3}" >部分退货</c:if>
        // <c:if test="${order.shippingStatus==4}" >已退货</c:if>
        // <c:if test="${order.shippingStatus==5}" >确认收货</c:if>


        if ("0".equals(newItem.getPaymentStatus()) && !"3".equals(newItem.getOrderStatus())) {
            newHolder.orderstate.setText("待付款");
        } else {
            if ("2".equals(newItem.getPaymentStatus())) {
                if (Integer.parseInt(newItem.getShippingStatus()) == 0) {
                    if ("自取".equals(newItem.getDeliveryType())) {
                        if (Integer.parseInt(newItem.getOrderStatus()) == 3) {
                            newHolder.orderstate.setText("已取消");
                        } else {
                            newHolder.orderstate.setText("待取货");
                        }
                    } else {
                        if (Integer.parseInt(newItem.getOrderStatus()) == 3) {
                            newHolder.orderstate.setText("已取消");
                        } else {
                            newHolder.orderstate.setText("待发货");
                        }
                    }
                } else if (Integer.parseInt(newItem.getShippingStatus()) == 5) {
                    newHolder.orderstate.setText("已完成");
                } else if (Integer.parseInt(newItem.getShippingStatus()) == 2) {
                    if ("自取".equals(newItem.getDeliveryType())) {
                        newHolder.orderstate.setText("待取货");
                    } else {
                        newHolder.orderstate.setText("待收货");
                    }
                } else if (Integer.parseInt(newItem.getShippingStatus()) == 4) {
                    newHolder.orderstate.setText("已退货");
                } else {
                    newHolder.orderstate.setText("待收货");
                }
            }
            if ("3".equals(newItem.getOrderStatus())) {
                newHolder.orderstate.setText("已取消");
            }
        }
        /**
         * 退款状态  1退款中  2成功 3失败   空或者0则不需要退款
         */
        switch (newItem.getRefundStatus()) {
            case 0:
                newHolder.refundStatus.setText("");
                break;
            case 1:
                newHolder.refundStatus.setText("退款中");
                break;
            case 2:
                newHolder.refundStatus.setText("退款成功");
                break;
            case 3:
                newHolder.refundStatus.setText("退款失败");
                break;
        }
        /**
         * 共计商品和总价
         */

        String total = "¥0.00";
        if (newItem.getProductTotalPrice().floatValue() <= 0) {
            if (newItem.getExpendTotalIntegral() > 0) {
                total = newItem.getExpendTotalIntegral() + "积分";
            }
        } else {
            if (newItem.getExpendTotalIntegral() > 0) {
                total = "¥" + newItem.getProductTotalPrice() + "+" + newItem.getExpendTotalIntegral() + "积分";
            } else {
                total = "¥" + newItem.getProductTotalPrice();
            }
        }

        newHolder.ordercontent.setText("共" + newItem.getProductTotalQuantity() + "件商品,合计:" + total);


        /**
         * 取消订单显示文字
         */
        newHolder.grey.setVisibility(View.VISIBLE);
        if (("0".equals(newItem.getPaymentStatus()) || "2".equals(newItem.getPaymentStatus())) && !"3".equals(newItem.getOrderStatus()) && Integer.parseInt(newItem.getShippingStatus()) != 5) {
            newHolder.grey.setText(R.string.my_order_cancel_order_btn_str);
        } else {
            newHolder.grey.setText(R.string.my_order_delete_order_btn_str);
        }
        //如果是待收货就不能取消和删除
        if (Integer.parseInt(newItem.getShippingStatus()) == 2) {
            newHolder.grey.setVisibility(View.GONE);
        }
        /**
         * 确认订单按钮隐藏和显示
         */
        if (Integer.parseInt(newItem.getShippingStatus()) == 2) {
            newHolder.affirm.setVisibility(View.VISIBLE);
        } else {
            newHolder.affirm.setVisibility(View.GONE);
        }

        /**
         * 取货码按钮隐藏和显示
         */
        if ("自取".equals(newItem.getDeliveryType()) && "2".equals(newItem.getPaymentStatus()) && Integer.parseInt(newItem.getShippingStatus()) != 5) {
            newHolder.pickupcode.setVisibility(View.VISIBLE);
        } else {
            newHolder.pickupcode.setVisibility(View.GONE);
        }

        /**
         * 查看物流按钮隐藏和显示
         */
        if (!(Integer.parseInt(newItem.getShippingStatus()) == 0) && !"自取".equals(newItem.getDeliveryType())) {
            newHolder.trace.setVisibility(View.VISIBLE);
        } else {
            newHolder.trace.setVisibility(View.GONE);
        }

        /**
         * 付款按钮隐藏和显示
         */
        if ("0".equals(newItem.getPaymentStatus()) && !"3".equals(newItem.getOrderStatus())) {
            newHolder.submit.setText("立即付款");
            newHolder.submit.setVisibility(View.VISIBLE);
        } else {
            newHolder.submit.setVisibility(View.GONE);
        }

        if (Integer.parseInt(newItem.getShippingStatus()) == 5 && newItem.getEstimateSate() == 1) {
            newHolder.submit.setVisibility(View.VISIBLE);
            newHolder.submit.setText("立即评价");
        }

        /**
         * 查看评价
         */
        if (Integer.parseInt(newItem.getShippingStatus()) == 5 && newItem.getEstimateSate() == 2) {
            newHolder.evluate.setVisibility(View.VISIBLE);
            newHolder.evluate.setText("查看评价");
        }else {
            newHolder.evluate.setVisibility(View.GONE);
        }

        /**
         * 店铺名字的显示
         */
        if (newItem.getIsSellerOrder()){
            newHolder.storename.setText(newItem.getShopName());
        }else {
            newHolder.storename.setText("普兰氏");
        }

        /**
         * 商户图标跳转
         */
        newHolder.merchants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newItem.getIsSellerOrder()){
                    Intent intent = new Intent(activity, AngelDetailsActivity.class);
                    intent.putExtra("sellerId", newItem.getSeller_id());//传递天使ID
                    activity.startActivity(intent);
                }else {
                    BaseAppManager.getInstance().clear();//清除activity
                    MainActivity.mTabView.setCurrentTab(0);
                }
            }
        });
    }


    /**
     * 确认收货方法
     */
    private void affirmGoods(final int position) {
        String getPickupListurlPath = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.updataShippingState);
        StringRequest showOrderListRequest = new StringRequest(Request.Method.POST, getPickupListurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("successful")) {
                            ToastUtil.showToast(jsonObject.getString("message"));
                            myOrderFragment.refreshList();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("orderId", getItem(position).getId());
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(showOrderListRequest);
    }

    /**
     * 取消订单方法
     */
    private void cleanOrder(final int position, final String cleanCause) {
        DialogUtil.showLoadingDialog(activity, "取消中");
        String urlPath = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.cleanOrder);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("successful")) {
                            ToastUtil.showToast(jsonObject.getString("message"));
                            myOrderFragment.refreshList();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                DialogUtil.hideLoadingDialog();
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                DialogUtil.hideLoadingDialog();
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("orderId", getItem(position).getId());
                map.put("cleanCause", cleanCause);//取消原因
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(stringRequest);
    }

    /**
     * 删除订单方法
     */
    private void deleteOrder(final int position) {
        DialogUtil.showLoadingDialog(activity, "删除中");
        String urlPath = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.deleteOrder);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("successful")) {
                            ToastUtil.showToast(jsonObject.getString("message"));
                            myOrderFragment.refreshList();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    DialogUtil.hideLoadingDialog();
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                DialogUtil.hideLoadingDialog();
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("orderId", getItem(position).getId());
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(stringRequest);
    }

    /**
     * 获取取货码
     */
    public void getPickUpCode(final int position) {
        DialogUtil.showLoadingDialog(activity, "获取中");
        String getPickupListurlPath = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.getPickupList);
        StringRequest showOrderListRequest = new StringRequest(Request.Method.POST, getPickupListurlPath, new Response.Listener<String>() {
            public void onResponse(String result) {
                if (result != null) {
                    Gson gson = new Gson();
                    List<PickupBean> pickupBeans = gson.fromJson(result, new TypeToken<List<PickupBean>>() {
                    }.getType());
                    String[] items = new String[pickupBeans.size()];
                    for (int i = 0; i < pickupBeans.size(); i++) {
                        items[i] = pickupBeans.get(i).getSubject() + ":" + pickupBeans.get(i).getTradeno();
                    }
                    new AlertDialog.Builder(activity).setTitle("取货码").setItems(items, null)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create().show();
                    DialogUtil.hideLoadingDialog();
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                DialogUtil.hideLoadingDialog();
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("orderId", getItem(position).getId());
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(showOrderListRequest);
    }
}
