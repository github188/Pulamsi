package com.pulamsi.myinfo.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.base.entity.ResultContext;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.address.AddressListActivity;
import com.pulamsi.myinfo.address.entity.Address;
import com.pulamsi.myinfo.order.adapter.OrderConfirmationAdapter;
import com.pulamsi.myinfo.order.entity.MasterplateEntity;
import com.pulamsi.myinfo.order.entity.OrderResult;
import com.pulamsi.myinfo.order.entity.PreviewOrder;
import com.pulamsi.myinfo.order.entity.PreviewOrderResult;
import com.pulamsi.shoppingcar.entity.CartCommodity;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.GoodsHelper;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.utils.Utils;
import com.pulamsi.views.dialog.CommonAlertDialog;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 确认订单界面
 */
public class OrderConfirmationActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 列表
     */
    private TRecyclerView goodsTRecyclerView;
    /**
     * 适配器
     */
    private OrderConfirmationAdapter orderConfirmationAdapter;
    /**
     * 选中的收货地址
     */
    private Address address = null;
    /**
     * 地址
     */
    private TextView orderConfirmationConsigneeName, orderConfirmationConsigneePhone, orderConfirmationConsigneeAddress;
    private RelativeLayout consigneeLayoutNor, consigneeLayout, submitLayout;
    /**
     *
     */
    private TextView totaPrice, totalFee, amount, bottomPrice, deliveType, payDefult, point, havepoint;
    /**
     * 买家留言
     */
    private EditText memo;
    /**
     * 配送方式
     */
    private RelativeLayout deliveryLayout, payLayout;
    /**
     * 选中的配送方式
     */
    private MasterplateEntity delive = null;
    /**
     * 选中的支付方式
     */
    private MasterplateEntity pay = null;

    /**
     * 后端返回的订单数据
     */
    private PreviewOrder previewOrder;

    private boolean isonce;

    /**
     * 立即购买传过来的参数
     */
    private String priceId;
    private String sn;
    private String count;

    private Boolean isSnapUp;
    private Boolean isAngel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseAppManager.getInstance().addActivity(this);
        isonce = getIntent().getBooleanExtra("isonce", false);
        isSnapUp = getIntent().getBooleanExtra("isSnapUp", false);
        isAngel = getIntent().getBooleanExtra("isAngel", false);
        requestData();
        setContentView(R.layout.order_confirmation_activity);
        initUI();
    }

    /**
     * 网络请求数据
     */
    private void requestData() {
        DialogUtil.showLoadingDialog(OrderConfirmationActivity.this, "生成订单中");
        if (isonce) {
            /**
             * 从立即购买进来
             */
            priceId = getIntent().getStringExtra("priceId");//价格id
            sn = getIntent().getStringExtra("sn");//商品编号
            count = getIntent().getStringExtra("count");//商品数量

            if (isSnapUp) {
                getSnapUpData();//抢购购买
            } else if (isAngel) {
                getAngelDate();//天使购买
            } else {
                getOnceData();//正常购买
            }
        } else {
            /**
             * 从购物车进来
             */
            getCartData();
        }
    }


    /**
     * 抢购数据
     */
    private void getSnapUpData() {
        String showAddressurlPath = getString(R.string.serverbaseurl) + getString(R.string.toOrderSnapUp);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, showAddressurlPath, new Response.Listener<String>() {
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        PreviewOrderResult previewOrderResult = gson.fromJson(result, PreviewOrderResult.class);
                        if (previewOrderResult.getReturn_code().equals(ResultContext.SUCCESS)) {
                            previewOrder = previewOrderResult.getReturn_context();
                            setUI();
                        } else {
                            ToastUtil.showToast(previewOrderResult.getReturn_msg());
                        }
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "获取默认收货地址数据装配出错");
                        ToastUtil.showToast("订单生成失败,请重新下单");
                        DialogUtil.hideLoadingDialog();
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                DialogUtil.hideLoadingDialog();
                ToastUtil.showToast("订单生成失败,请重新下单");
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mid", Constants.userId);
                map.put("sn", sn);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);

    }

    /**
     * 获取立即购买数据
     */
    private void getOnceData() {
        String showAddressurlPath = getString(R.string.serverbaseurl) + getString(R.string.toOrderOnce);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, showAddressurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        PreviewOrderResult previewOrderResult = gson.fromJson(result, PreviewOrderResult.class);
                        if (previewOrderResult.getReturn_code().equals(ResultContext.SUCCESS)) {
                            previewOrder = previewOrderResult.getReturn_context();
                            setUI();
                        } else {
                            ToastUtil.showToast(previewOrderResult.getReturn_msg());
                        }
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "获取默认收货地址数据装配出错");
                        ToastUtil.showToast("订单生成失败,请重新下单");
                        DialogUtil.hideLoadingDialog();
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                DialogUtil.hideLoadingDialog();
                ToastUtil.showToast("订单生成失败,请重新下单");
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mid", Constants.userId);
                map.put("sn", sn);
                map.put("priceId", priceId);
                map.put("count", count);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);

    }


    /**
     * 天使购买
     */
    private void getAngelDate() {
        String showAddressurlPath = getString(R.string.serverbaseurl) + getString(R.string.toOrderOnce);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, showAddressurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        PreviewOrderResult previewOrderResult = gson.fromJson(result, PreviewOrderResult.class);
                        if (previewOrderResult.getReturn_code().equals(ResultContext.SUCCESS)) {
                            previewOrder = previewOrderResult.getReturn_context();
                            setUI();
                        } else {
                            ToastUtil.showToast(previewOrderResult.getReturn_msg());
                        }
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "获取默认收货地址数据装配出错");
                        ToastUtil.showToast("订单生成失败,请重新下单");
                        DialogUtil.hideLoadingDialog();
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                DialogUtil.hideLoadingDialog();
                ToastUtil.showToast("订单生成失败,请重新下单");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mid", Constants.userId);
                map.put("ssn", sn);//天使编码
                map.put("priceId", priceId);
                map.put("count", count);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);

    }


    // 获取订单数据
    private void getCartData() {
        String showAddressurlPath = getString(R.string.serverbaseurl) + getString(R.string.toOrder);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, showAddressurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                    Gson gson = new Gson();
                    PreviewOrderResult previewOrderResult = gson.fromJson(result, PreviewOrderResult.class);
                    if (previewOrderResult.getReturn_code().equals(ResultContext.SUCCESS)) {
                        previewOrder = previewOrderResult.getReturn_context();
                        setUI();
                    } else {
                        ToastUtil.showToast(previewOrderResult.getReturn_msg());
                    }
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "获取默认收货地址数据装配出错");
                        ToastUtil.showToast("订单生成失败,请重新下单");
                        DialogUtil.hideLoadingDialog();
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                DialogUtil.hideLoadingDialog();
                ToastUtil.showToast("订单生成失败,请重新下单");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("mid", Constants.userId);
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);
    }


    private void initUI() {
        setHeaderTitle(R.string.order_confirmation_title);
        goodsTRecyclerView = (TRecyclerView) findViewById(R.id.order_confirmation_trecyclerview);

        consigneeLayout = (RelativeLayout) findViewById(R.id.order_confirmation_consignee_layout);
        consigneeLayoutNor = (RelativeLayout) findViewById(R.id.order_confirmation_consignee_layout_nor);//没默认地址
        consigneeLayoutNor.setOnClickListener(this);
        orderConfirmationConsigneeName = (TextView) findViewById(R.id.order_confirmation_consignee_name);//收件人姓名
        orderConfirmationConsigneePhone = (TextView) findViewById(R.id.order_confirmation_consignee_phone);//收件人手机号
        orderConfirmationConsigneeAddress = (TextView) findViewById(R.id.order_confirmation_consignee_address);//收件人地址
        consigneeLayout.setOnClickListener(this);
        submitLayout = (RelativeLayout) findViewById(R.id.order_confirmation_bottom_submit_layout);
        submitLayout.setOnClickListener(this);

        totaPrice = (TextView) findViewById(R.id.order_confirmation_tota_price);//商品合计
        totalFee = (TextView) findViewById(R.id.order_confirmation_total_fee);//运费
        amount = (TextView) findViewById(R.id.order_confirmation_amount);//商品总额
        bottomPrice = (TextView) findViewById(R.id.order_confirmation_bottom_price);
        deliveryLayout = (RelativeLayout) findViewById(R.id.order_confirmation_delivery_layout);//配送方式
        deliveType = (TextView) findViewById(R.id.order_confirmation_delivery);
        point = (TextView) findViewById(R.id.order_confirmation_point);
        havepoint = (TextView) findViewById(R.id.order_confirmation_havepoint);
        deliveryLayout.setOnClickListener(this);
        payLayout = (RelativeLayout) findViewById(R.id.order_confirmation_pay_layout);//支付方式
        payDefult = (TextView) findViewById(R.id.order_confirmation_pay_defult);
        payLayout.setOnClickListener(this);
        memo = (EditText) findViewById(R.id.order_confirmation_message);//买家留言

        //订单货物
        orderConfirmationAdapter = new OrderConfirmationAdapter(this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        goodsTRecyclerView.setLayoutManager(layoutManager);
        goodsTRecyclerView.setAdapter(orderConfirmationAdapter);
        goodsTRecyclerView.setHasFixedSize(true);
        goodsTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                if (orderConfirmationAdapter.getItem(position).getProduct() != null){
                    //点击事件
                    GoodsHelper.gotoDetail(orderConfirmationAdapter.getItem(position).getProduct().getId(), OrderConfirmationActivity.this);
                }else {
                    //点击事件
                    GoodsHelper.gotoAngelDetail(orderConfirmationAdapter.getItem(position).getSellerProduct().getId(), OrderConfirmationActivity.this);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_confirmation_consignee_layout_nor:
            case R.id.order_confirmation_consignee_layout:
                //选择地址
                Intent address = new Intent(OrderConfirmationActivity.this, AddressListActivity.class);
                address.putExtra("iscallback", true);
                OrderConfirmationActivity.this.startActivityForResult(address, 0);
                break;
            case R.id.order_confirmation_pay_layout:
                //支付方式点击
                if (null == previewOrder) {
                    return;
                }
                new AlertDialog.Builder(OrderConfirmationActivity.this).setSingleChoiceItems(Utils.objtoStringArrary(previewOrder.getPayTypeList()), 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pay = previewOrder.getPayTypeList().get(which);
                        payDefult.setText(previewOrder.getPayTypeList().get(which).getName());
                        dialog.dismiss();
                    }
                }).create().show();
                break;
            case R.id.order_confirmation_delivery_layout:
                //配送方式点击
                if (null == previewOrder) {
                    return;
                }
                new AlertDialog.Builder(OrderConfirmationActivity.this).setSingleChoiceItems(Utils.objtoStringArrary(previewOrder.getDeliveryTypeList()), 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delive = previewOrder.getDeliveryTypeList().get(which);
                        deliveType.setText(previewOrder.getDeliveryTypeList().get(which).getName());
                        dialog.dismiss();
                    }
                }).create().show();

                break;
            case R.id.order_confirmation_bottom_submit_layout:
                //提交订单
                if (null == this.address) {
                    ToastUtil.showToast("请先选择收货地址");
                    return;
                }
                if (null == delive || null == pay) {
                    ToastUtil.showToast("未获取到全部信息请重新提交");
                    return;
                }
                if (previewOrder.getPoint() < getTotalPoint()) {
                    ToastUtil.showToast("您的积分不够支付");
                    return;
                }
                submitOrder();
                break;
        }

    }

    /**
     * 提交订单
     */
    private void submitOrder() {
        DialogUtil.showLoadingDialog(OrderConfirmationActivity.this, "提交订单中");
        String saveOrderurlPath = getString(R.string.serverbaseurl) + getString(R.string.saveOrderurl);
        StringRequest saveOrderRequest = new StringRequest(Request.Method.POST, saveOrderurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                try {
                    if (result != null) {
                        Gson gson = new Gson();
                        OrderResult orderResult = gson.fromJson(result, OrderResult.class);
                        if (orderResult.getReturn_code().equals(ResultContext.SUCCESS)) {
                            if (orderResult.getReturn_context().getProductTotalPrice().floatValue() <= 0) {
                                CommonAlertDialog alertDialog = new CommonAlertDialog(OrderConfirmationActivity.this, "成功支付" + orderResult.getReturn_context().getExpendTotalIntegral() + "积分", "确定", "取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent notReceive = new Intent(OrderConfirmationActivity.this, MyOrderActivity.class);
                                        notReceive.putExtra(MyOrderActivity.KEY_OPEN_PAGE, MyOrderFragment.TYPE_ALL);
                                        startActivity(notReceive);
                                        BaseAppManager.getInstance().clear();
                                    }
                                });
                                alertDialog.show();
                            } else {
                                Intent pay = new Intent(OrderConfirmationActivity.this, ChoicePayTypeActivity.class);
                                pay.putExtra("order", orderResult.getReturn_context());
                                OrderConfirmationActivity.this.startActivity(pay);
                            }
                        } else {
                            ToastUtil.showToast(orderResult.getReturn_msg());
                        }

                    }
                } catch (Exception e) {
                    HaiLog.e("pulamsi", "提交订单数据装配错误");
                }
                DialogUtil.hideLoadingDialog();
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                DialogUtil.hideLoadingDialog();
                ToastUtil.showToast(R.string.notice_networkerror);
                ToastUtil.showToast(arg0.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return getRequestParams();
            }
        };
        saveOrderRequest.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        PulamsiApplication.requestQueue.add(saveOrderRequest);
    }

    // 选择收货地址回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            address = (Address) bundle.getSerializable("address");
            orderConfirmationConsigneeName.setText(address.getName());
            orderConfirmationConsigneeAddress.setText(address.getProvinceName() + address.getCityName() + address.getCountyName() + address.getAddressAlias());
            orderConfirmationConsigneePhone.setText(address.getMobile());
            consigneeLayoutNor.setVisibility(View.GONE);
            consigneeLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 商品总价
     */
    private BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        for (int i = 0; i < previewOrder.getCartItems().size(); i++) {
            CartCommodity cartCommodity = previewOrder.getCartItems().get(i);
            totalPrice = totalPrice.add(cartCommodity.getTotalPrice());
        }
        return totalPrice;
    }

    /**
     * 商品总积分
     */
    private int getTotalPoint() {
        int totalPoint = 0;
        for (int i = 0; i < previewOrder.getCartItems().size(); i++) {
            CartCommodity cartCommodity = previewOrder.getCartItems().get(i);
            totalPoint += cartCommodity.getTotalintegralPrice();
        }
        return totalPoint;
    }


    //装配数据
    private void setUI() {
        if (isSnapUp) {//如为抢购的话就把普通商品清单设置为抢购的清单
            previewOrder.getCartItems().clear();
            previewOrder.setCartItems(previewOrder.getPanicCartItems());
        }


        //初始化收货地址
        for (int i = 0; i < previewOrder.getReceiverList().size(); i++) {
            if ("true".equals(previewOrder.getReceiverList().get(i).getIsDefault())) {
                address = previewOrder.getReceiverList().get(i);
                orderConfirmationConsigneeName.setText(address.getName());
                orderConfirmationConsigneeAddress.setText(address.getProvinceName() + address.getCityName() + address.getCountyName() + address.getAddressAlias());
                orderConfirmationConsigneePhone.setText(address.getMobile());
                consigneeLayoutNor.setVisibility(View.GONE);
                consigneeLayout.setVisibility(View.VISIBLE);
                break;
            }
            if (i == (previewOrder.getReceiverList().size() - 1)) {
                address = previewOrder.getReceiverList().get(0);
                orderConfirmationConsigneeName.setText(address.getName());
                orderConfirmationConsigneeAddress.setText(address.getProvinceName() + address.getCityName() + address.getCountyName() + address.getAddressAlias());
                orderConfirmationConsigneePhone.setText(address.getMobile());
                consigneeLayoutNor.setVisibility(View.GONE);
                consigneeLayout.setVisibility(View.VISIBLE);
            }
        }

        //初始化配送方式
        delive = previewOrder.getDeliveryTypeList().get(0);
        deliveType.setText(delive.getName());

        if (!isAngel) {//所有天使商品都不需要自取
            if (!judgeCartsGoodsisAutoSell()) {
                for (int i = 0; i < previewOrder.getDeliveryTypeList().size(); i++) {
                    if ("自取".equals(previewOrder.getDeliveryTypeList().get(i).getName())) {
                        previewOrder.getDeliveryTypeList().remove(i);
                    }
                }
            }
        }

        //初始化支付方式
        pay = previewOrder.getPayTypeList().get(0);
        payDefult.setText(pay.getName());
        //设置用户积分
        havepoint.setText(previewOrder.getPoint() + "积分");
        totaPrice.setText("¥" + getTotalPrice());
        /**
         * 设置底部价格和积分
         */
        String total = "¥0.00";
        if (getTotalPrice().floatValue() <= 0) {
            if (getTotalPoint() > 0) {
                total = getTotalPoint() + "积分";
            }
        } else {
            if (getTotalPoint() > 0) {
                total = "¥" + getTotalPrice() + "+" + getTotalPoint() + "积分";
            } else {
                total = "¥" + getTotalPrice();
            }
        }
        bottomPrice.setText(total);
        amount.setText("¥" + getTotalPrice());
        point.setText(getTotalPoint() + "积分");

        //设置删除线
        totalFee.setText("¥0.00");
        totalFee.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        //  列表数据状态还原
        int formerItemCount = orderConfirmationAdapter.getItemCount();
        orderConfirmationAdapter.clearDataList();
        orderConfirmationAdapter.notifyItemRangeRemoved(0, formerItemCount);
        orderConfirmationAdapter.addDataList(previewOrder.getCartItems());
        orderConfirmationAdapter.notifyItemRangeInserted(0, previewOrder.getCartItems().size());
        goodsTRecyclerView.getLayoutParams().height =
                PulamsiApplication.context.getResources().getDimensionPixelOffset(R.dimen.orderlist_product_height) * previewOrder.getCartItems().size();
        //隐藏加载框
        DialogUtil.hideLoadingDialog();
    }


    /**
     * 判断购物清单里面有没有不可自取的货物
     */
    private boolean judgeCartsGoodsisAutoSell() {
        boolean flag = true;
        for (int i = 0; i < previewOrder.getCartItems().size(); i++) {
            if (previewOrder.getCartItems().get(i).getProduct() != null) {
                if (!previewOrder.getCartItems().get(i).getProduct().isAutoSell()) {
                    flag = false;
                    break;
                }
            } else if (previewOrder.getCartItems().get(i).getSellerProduct() != null) {
                if (!previewOrder.getCartItems().get(i).getSellerProduct().isAutoSell()) {
                    flag = false;
                    break;
                }
            }

        }
        return flag;
    }

    private String getcarts() {
        StringBuffer carts = new StringBuffer();
        for (int i = 0; i < previewOrder.getCartItems().size(); i++) {
            if (i == previewOrder.getCartItems().size() - 1) {
                carts.append(previewOrder.getCartItems().get(i).getId());
            } else {
                carts.append(previewOrder.getCartItems().get(i).getId() + ",");
            }
        }
        return carts.toString();
    }


    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mid", Constants.userId);// 用户ID
        map.put("addressid", address.getId());// 收货地址ID
        map.put("payid", pay.getId());// 支付方式ID
        map.put("deliveryid", delive.getId());// 配送方式ID
        map.put("memo", memo.getText().toString());// 买家留言
        map.put("orderSource", 2 + "");// //订单来源 PC端(0), 售货机(1), Android(2), IOS(3)
        if (isonce) {
            if (isAngel) {
                map.put("ssn", sn);//编码
            }
            map.put("sn", sn);//编码
            map.put("count", count);//数量
            map.put("priceId", priceId);//价格id
        }
        if (isSnapUp) {
            map.put("qianggou", "1");//抢购标识//未修复麦进业
        }
        return map;
    }
}
