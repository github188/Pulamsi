package com.pulamsi.myinfo.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.otto.BusProvider;
import com.pulamsi.base.otto.MessageEvent;
import com.pulamsi.evaluate.AddEvaluateActivity;
import com.pulamsi.myinfo.order.adapter.OrderProductAdapter;
import com.pulamsi.myinfo.order.entity.Order;
import com.pulamsi.myinfo.order.entity.PickupBean;
import com.pulamsi.pay.wx.WxPay;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.GoodsHelper;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.dialog.CommonAlertDialog;
import com.taobao.uikit.feature.view.TRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单详情界面
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 订单对象
     */
    private Order order;

    private String[] cleanOrderStr = {"现在不想购买", "价格波动", "重复下单", "收货人信息有误", "发货时间长", "其他原因"};

    /**
     * 订单状态
     */
    private TextView orderStatus;
    /**
     * 订单编号
     */
    private TextView orderId;
    /**
     * 创建时间
     */
    private TextView ordertime;
    /**
     * 收货人
     */
    private TextView receiverName;
    /**
     * 收货人电话
     */
    private TextView receiverphone;
    /**
     * 收货人地址
     */
    private TextView receiveraddress;

    /**
     * 商品列表
     */
    private TRecyclerView goodsTRecyclerView;
    /**
     * 支付方式
     */
    private TextView orderPayType;
    /**
     * 订单总金额
     */
    private TextView ordertotalprice;
    /**
     * 消耗总积分
     */
    private TextView expendTotalIntegral;
    /**
     * 订单运费
     */
    private TextView orderdeliverfee;

    /**
     * 优惠卷
     */
    private TextView ordercoupon;
    /**
     * 付款按钮
     */
    private TextView submit;
    /**
     * 物流查询按钮
     */
    private TextView trace;
    /**
     * 查看评价按钮
     */
    private TextView evluate;
    /**
     * 确认收货按钮
     */
    private TextView affirm;
    /**
     * 取货码按钮
     */
    private TextView pickupcode;
    /**
     * 取消订单按钮
     */
    private TextView grey;
    /**
     * 退款详情按钮
     */
    private Button refund;

    private OrderProductAdapter orderProductAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WxPay.getInstance().initWxPay(this);
        initDatas();
        setContentView(R.layout.order_detail_layout);
        initUI();
        setData();
    }

    private void setData() {
        //订单状态
        if ("0".equals(order.getPaymentStatus()) && !"3".equals(order.getOrderStatus())) {
            orderStatus.setText("待付款");
        } else {
            if ("2".equals(order.getPaymentStatus())) {
                if (Integer.parseInt(order.getShippingStatus()) == 0) {
                    if ("自取".equals(order.getDeliveryType())) {
                        if (Integer.parseInt(order.getOrderStatus()) == 3) {
                            orderStatus.setText("已取消");
                        } else {
                            orderStatus.setText("待取货");
                        }
                    } else {
                        if (Integer.parseInt(order.getOrderStatus()) == 3) {
                            orderStatus.setText("已取消");
                        } else {
                            orderStatus.setText("待发货");
                        }
                    }
                } else if (Integer.parseInt(order.getShippingStatus()) == 5) {
                    if (order.getEstimateSate() == 2) {
                        orderStatus.setText("已评论");
                    } else {
                        orderStatus.setText("已完成");
                    }
                } else if (Integer.parseInt(order.getShippingStatus()) == 2) {
                    if ("自取".equals(order.getDeliveryType())) {
                        orderStatus.setText("待取货");
                    } else {
                        orderStatus.setText("待收货");
                    }
                } else if (Integer.parseInt(order.getShippingStatus()) == 4) {
                    orderStatus.setText("已退货");
                } else {
                    orderStatus.setText("待收货");
                }
            }
            if ("3".equals(order.getOrderStatus())) {
                orderStatus.setText("已取消");
            }
        }
        //商品总金额
        ordertotalprice.setText("¥" + order.getProductTotalPrice());
        //消耗总积分
        expendTotalIntegral.setText(order.getExpendTotalIntegral() + "积分");
        //收货人
        receiverName.setText(order.getShipName());
        //收货人电话
        receiverphone.setText(order.getShipMobile());
        //收货人地址
        receiveraddress.setText(order.getShipAddress());
        //支付方式
        orderPayType.setText(order.getPaymentConfigName());
        //订单编号
        orderId.setText(order.getOrderSn());
        //创建时间
        ordertime.setText(order.getCreateDate());
        //订单运费
        orderdeliverfee.setText("¥0.00");
        //设置删除线
        orderdeliverfee.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        /**
         * 取消订单显示文字
         */
        grey.setVisibility(View.VISIBLE);
        if (("0".equals(order.getPaymentStatus()) || "2".equals(order.getPaymentStatus())) && !"3".equals(order.getOrderStatus()) && Integer.parseInt(order.getShippingStatus()) != 5) {
            grey.setText(R.string.my_order_cancel_order_btn_str);
        } else {
            grey.setText(R.string.my_order_delete_order_btn_str);
        }

        //如果是待收货就不能取消和删除
        if (Integer.parseInt(order.getShippingStatus()) == 2) {
            grey.setVisibility(View.GONE);
        }
        /**
         * 确认订单按钮隐藏和显示
         */
        if (Integer.parseInt(order.getShippingStatus()) == 2) {
            affirm.setVisibility(View.VISIBLE);
        } else {
            affirm.setVisibility(View.GONE);
        }

        /**
         * 取货码按钮隐藏和显示
         */
        if ("自取".equals(order.getDeliveryType()) && "2".equals(order.getPaymentStatus()) && Integer.parseInt(order.getShippingStatus()) != 5) {
            pickupcode.setVisibility(View.VISIBLE);
        } else {
            pickupcode.setVisibility(View.GONE);
        }

        /**
         * 查看物流按钮隐藏和显示
         */
        if (!(Integer.parseInt(order.getShippingStatus()) == 0) && !"自取".equals(order.getDeliveryType())) {
            trace.setVisibility(View.VISIBLE);
        } else {
            trace.setVisibility(View.GONE);
        }

        /**
         * 付款按钮隐藏和显示
         */
        if ("0".equals(order.getPaymentStatus()) && Integer.parseInt(order.getOrderStatus()) != 3) {
            submit.setVisibility(View.VISIBLE);
        } else {
            submit.setVisibility(View.GONE);
        }

        if (Integer.parseInt(order.getShippingStatus()) == 5 && order.getEstimateSate() == 1) {
            submit.setVisibility(View.VISIBLE);
            submit.setText("立即评价");
        }


        //订单货物
        orderProductAdapter = new OrderProductAdapter(this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        goodsTRecyclerView.setLayoutManager(layoutManager);
        goodsTRecyclerView.setAdapter(orderProductAdapter);
        goodsTRecyclerView.setHasFixedSize(true);
        goodsTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {

                //跳转商品详情页
                if (orderProductAdapter.getItem(position).getSellerProduct() != null){//是否为天使商品
                    GoodsHelper.gotoAngelDetail(orderProductAdapter.getItem(position).getSellerProduct().getId(), OrderDetailActivity.this);
                }else {
                    GoodsHelper.gotoDetail(orderProductAdapter.getItem(position).getProduct().getId(), OrderDetailActivity.this);
                }

            }
        });

        //  状态还原
        int formerItemCount = orderProductAdapter.getItemCount();
        orderProductAdapter.clearDataList();
        orderProductAdapter.notifyItemRangeRemoved(0, formerItemCount);
        orderProductAdapter.addDataList(order.getOrderItemSet());
        orderProductAdapter.notifyItemRangeInserted(0, order.getOrderItemSet().size());
        goodsTRecyclerView.getLayoutParams().height =
                PulamsiApplication.context.getResources().getDimensionPixelOffset(R.dimen.orderlist_product_height) * order.getOrderItemSet().size();
        /**
         * 退款状态
         */
        switch (order.getRefundStatus()) {
            case 0:
                refund.setVisibility(View.GONE);
                break;
            case 1:
                refund.setText("退款中");
                break;
            case 2:
                refund.setText("退款成功");
                break;
            case 3:
                refund.setText("退款失败");
                break;
        }
    }

    /**
     * 初始化跳转页面
     */
    private void initDatas() {
        if (getIntent() != null) {
            order = (Order) getIntent().getSerializableExtra("order");
        }

    }

    private void initUI() {
        setHeaderTitle(R.string.order_detail_title);
        refund = (Button) findViewById(R.id.btn_order_list_item_refund);
        orderStatus = (TextView) findViewById(R.id.order_detail_orderStatus);
        orderId = (TextView) findViewById(R.id.order_detail_orderid);
        ordertime = (TextView) findViewById(R.id.order_detail_ordertime);
        receiverName = (TextView) findViewById(R.id.order_detail_receiver_name);
        receiverphone = (TextView) findViewById(R.id.order_detail_receiver_phone);
        receiveraddress = (TextView) findViewById(R.id.order_detail_receiver_address);
        goodsTRecyclerView = (TRecyclerView) findViewById(R.id.order_detail_trecyclerview);
        orderPayType = (TextView) findViewById(R.id.order_detail_payType);
        ordertotalprice = (TextView) findViewById(R.id.order_detail_ordertotalprice);
        orderdeliverfee = (TextView) findViewById(R.id.order_detail_orderdeliverfee);
        ordercoupon = (TextView) findViewById(R.id.order_detail_ordercoupon);
        submit = (TextView) findViewById(R.id.tv_btn_my_order_list_item_submit);
        trace = (TextView) findViewById(R.id.tv_btn_my_order_list_item_trace);
        evluate = (TextView) findViewById(R.id.tv_btn_my_order_list_item_evluate);
        affirm = (TextView) findViewById(R.id.tv_btn_my_order_list_item_affirm);
        grey = (TextView) findViewById(R.id.tv_btn_my_order_list_item_grey);
        pickupcode = (TextView) findViewById(R.id.tv_btn_my_order_list_item_pickupcode);
        expendTotalIntegral = (TextView) findViewById(R.id.order_detail_expendTotalIntegral);
        submit.setOnClickListener(this);
        trace.setOnClickListener(this);
        affirm.setOnClickListener(this);
        grey.setOnClickListener(this);
        refund.setOnClickListener(this);
        evluate.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_btn_my_order_list_item_submit:
                //付款
                if (Integer.parseInt(order.getShippingStatus()) == 5 && order.getEstimateSate() == 1) {
                    //评价
                    Intent addEvaluate = new Intent(OrderDetailActivity.this, AddEvaluateActivity.class);
                    addEvaluate.putExtra("order", order);
                    OrderDetailActivity.this.startActivity(addEvaluate);
                } else {
                    Intent pay = new Intent(OrderDetailActivity.this, ChoicePayTypeActivity.class);
                    pay.putExtra("order", order);
                    OrderDetailActivity.this.startActivity(pay);
                }
                break;
            case R.id.tv_btn_my_order_list_item_trace:
                //物流查询
                Intent intent = new Intent(this, TraceOrderActivity.class);
                intent.putExtra("orderSn", order.getOrderSn());
                this.startActivity(intent);
                break;
            case R.id.tv_btn_my_order_list_item_evluate:
                //已评论
                Intent addEvaluate = new Intent(this, AddEvaluateActivity.class);
                addEvaluate.putExtra("order", order);
                this.startActivity(addEvaluate);
                break;
            case R.id.tv_btn_my_order_list_item_affirm:
                //确认订单
                affirmGoods();
                break;
            case R.id.tv_btn_my_order_list_item_grey:
                //取消订单
                if (("0".equals(order.getPaymentStatus()) || "2".equals(order.getPaymentStatus())) && !"3".equals(order.getOrderStatus()) && Integer.parseInt(order.getShippingStatus()) != 5) {
                    //取消订单方法
                    new AlertDialog.Builder(OrderDetailActivity.this).setSingleChoiceItems(cleanOrderStr, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cleanOrder(cleanOrderStr[which]);
                            dialog.dismiss();
                        }
                    }).create().show();

                } else {
                    //删除订单方法
                    CommonAlertDialog alertDialog = new CommonAlertDialog(OrderDetailActivity.this, "确定删除?", "确定", "取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteOrder();
                        }
                    });
                    alertDialog.show();
                }
                break;
            case R.id.tv_btn_my_order_list_item_pickupcode:
                //获取取货吗
                getPickUpCode();
                break;
            case R.id.btn_order_list_item_refund:
                Intent intent_refund = new Intent();
                intent_refund.putExtra("order",order);
                intent_refund.setClass(this, OrderRefundActivity.class);
                startActivity(intent_refund);
                break;
        }

    }

    /**
     * 确认收货方法
     */
    private void affirmGoods() {
        String getPickupListurlPath = this.getString(R.string.serverbaseurl) + this.getString(R.string.updataShippingState);
        StringRequest showOrderListRequest = new StringRequest(Request.Method.POST, getPickupListurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("successful")) {
                            ToastUtil.showToast(jsonObject.getString("message"));
                            OrderDetailActivity.this.finish();
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
                map.put("orderId", order.getId());
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(showOrderListRequest);
    }

    /**
     * 获取取货码
     */
    public void getPickUpCode() {
        DialogUtil.showLoadingDialog(this, "获取中");
        String getPickupListurlPath = this.getString(R.string.serverbaseurl) + this.getString(R.string.getPickupList);
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
                    new AlertDialog.Builder(OrderDetailActivity.this).setTitle("取货码").setItems(items, null)
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
                map.put("orderId", order.getId());
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(showOrderListRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        WxPay.getInstance().dismiss();
    }


    /**
     * 取消订单方法
     */
    private void cleanOrder(final String cleanCause) {
        DialogUtil.showLoadingDialog(OrderDetailActivity.this, "取消中");
        String urlPath = OrderDetailActivity.this.getString(R.string.serverbaseurl) + OrderDetailActivity.this.getString(R.string.cleanOrder);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("successful")) {
                            ToastUtil.showToast(jsonObject.getString("message"));
                            OrderDetailActivity.this.finish();
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
                map.put("orderId", order.getId());
                map.put("cleanCause", cleanCause);//取消原因
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(stringRequest);
    }

    /**
     * 删除订单方法
     */
    private void deleteOrder() {
        DialogUtil.showLoadingDialog(OrderDetailActivity.this, "删除中");
        String urlPath = OrderDetailActivity.this.getString(R.string.serverbaseurl) + OrderDetailActivity.this.getString(R.string.deleteOrder);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("successful")) {
                            ToastUtil.showToast(jsonObject.getString("message"));
                            OrderDetailActivity.this.finish();
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
                map.put("orderId", order.getId());
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(stringRequest);
    }


    @Override
    public TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    @Override
    public boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().post(new MessageEvent(MyOrderFragment.RETURN_REFRESH));//发送刷新的消息
        super.onDestroy();
    }
}
