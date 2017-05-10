package com.pulamsi.myinfo.slotmachineManage;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.otto.BusProvider;
import com.pulamsi.base.otto.MessageEvent;
import com.pulamsi.myinfo.order.adapter.OrderProductAdapter;
import com.pulamsi.myinfo.order.entity.Order;
import com.pulamsi.myinfo.order.entity.OrderItem;
import com.pulamsi.myinfo.slotmachineManage.entity.RefundBean;
import com.pulamsi.utils.GoodsHelper;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.LoadView.LoadViewHelper;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.pulamsi.views.SweetAlert.SweetAlertDialog;
import com.taobao.uikit.feature.view.TRecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import tr.xip.errorview.ErrorView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-04-21
 * Time: 09:57
 * FIXME
 */
public class RefundDetailActivity extends BaseActivity implements PtrHandler, ErrorView.RetryListener,View.OnClickListener {


    private PtrStylelFrameLayout ptr_refund_style_ptr_frame;
    private View contentLayout;
    private LoadViewHelper loadHelper;
    private RefundBean mRefundBean;
    SweetAlertDialog pDialog;
    SweetAlertDialog sDialog;
    /**
     * 退款状态
     */
    private TextView refundState;
    /**
     * 退款编号
     */
    private TextView refundSerialNumber;
    /**
     * 退款时间
     */
    private TextView refundCreateDate;
    /**
     * 商品列表
     */
    private TRecyclerView refundOrderList;
    /**
     * 支付方式
     */
    private TextView refundPayment;
    /**
     * 订单总额
     */
    private TextView orderTotalPrice;
    /**
     * 订单总积分
     */
    private TextView orderTotalIntegral;
    /**
     * 退款总额
     */
    private TextView refundAmount;
    /**
     * 提交退款
     */
    private TextView refundSubmit;
    /**
     * 运费
     */
    private TextView orderFreight;
    private ScrollView mScrollView;

    private boolean isOperation;

    OrderProductAdapter orderProductAdapter;
    private String refundType;


    private final int STATE_PENDING = 0;//待处理
    private final int STATE_REFUND_SUCCESS = 1;//退款成功
    private final int STATE_REFUND_FAILURE = 3;//退款失败

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refund_detail_layout);
        init();
        initView();
        initData();
    }

    private void init() {
        contentLayout = findViewById(R.id.content_layout);
        loadHelper = new LoadViewHelper(contentLayout);
    }

    private void setData(Order refundOrder) {
        if (mRefundBean == null)
            return;
        if (refundOrder == null)
            return;

        switch (mRefundBean.getAuditState()) {
            case STATE_PENDING:
                refundState.setText("待处理");
                break;
            case STATE_REFUND_SUCCESS:
                refundState.setText("退款成功");
                break;
            case STATE_REFUND_FAILURE:
                refundState.setText("退款失败");
                break;
        }
        refundSerialNumber.setText(mRefundBean.getRefundSn());
        refundCreateDate.setText(mRefundBean.getCreateDate());
        refundPayment.setText(mRefundBean.getPaymentConfigName());
        refundAmount.setText(mRefundBean.getTotalAmount() + "");
        //商品总金额
        orderTotalPrice.setText("¥" + refundOrder.getProductTotalPrice());
        orderTotalIntegral.setText(refundOrder.getExpendTotalIntegral() + "积分");
        orderFreight.setText("¥0.00");
        orderFreight.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    private void initData() {
        loadHelper.showLoading();
        if (getIntent() != null) {
            mRefundBean = (RefundBean) getIntent().getSerializableExtra("RefundBean");
            refundType = mRefundBean.getPaymentConfigName();
        }
        requestData();//请求网络
    }

    private void requestData() {
        String url = getResources().getString(R.string.serverbaseurl) + getResources().getString(R.string.queryOrder);
        StringRequest refundOrderRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        Order refundOrder = gson.fromJson(result, Order.class);
                        setData(refundOrder);
                        List<OrderItem> orderItemList = refundOrder.getOrderItemSet();
                        // 下拉刷新时，需要清空数据
                        int itemCount = orderProductAdapter.getItemCount();
                        orderProductAdapter.clearDataList();
                        orderProductAdapter.notifyItemRangeRemoved(0, itemCount);

                        updateListData(orderItemList);
                    } catch (Exception e) {
                        LogUtils.e("退款订单装载失败");
                        ToastUtil.showToast("退款订单装载失败");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadHelper.showError(RefundDetailActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("orderid", mRefundBean.getOrder());
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(refundOrderRequest);
    }

    private void initView() {
        setHeaderTitle("退款详情");
        ptr_refund_style_ptr_frame = (PtrStylelFrameLayout) findViewById(R.id.ptr_refund_style_ptr_frame);
        ptr_refund_style_ptr_frame.setPtrHandler(this);
        mScrollView = (ScrollView) findViewById(R.id.rotate_header_scroll_view);
        refundState = (TextView) findViewById(R.id.refund_detail_refundStatus);
        refundSerialNumber = (TextView) findViewById(R.id.order_detail_orderid);
        refundCreateDate = (TextView) findViewById(R.id.refund_detail_refundTime);
        refundOrderList = (TRecyclerView) findViewById(R.id.order_detail_trecyclerview);
        refundPayment = (TextView) findViewById(R.id.order_detail_payType);
        orderTotalPrice = (TextView) findViewById(R.id.order_detail_ordertotalprice);
        orderTotalIntegral = (TextView) findViewById(R.id.order_detail_expendTotalIntegral);
        orderFreight = (TextView) findViewById(R.id.order_detail_orderdeliverfee);
        refundAmount = (TextView) findViewById(R.id.order_detail_refundAmount);
        refundSubmit = (TextView) findViewById(R.id.tv_btn_my_order_list_item_refund);
        refundSubmit.setOnClickListener(this);
        //订单货物
        orderProductAdapter = new OrderProductAdapter(this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        refundOrderList.setLayoutManager(layoutManager);
        refundOrderList.setAdapter(orderProductAdapter);
        refundOrderList.setHasFixedSize(true);
        refundOrderList.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                //跳转商品详情页
                if (orderProductAdapter.getItem(position).getSellerProduct() != null){//是否为天使商品
                    GoodsHelper.gotoAngelDetail(orderProductAdapter.getItem(position).getSellerProduct().getId(), RefundDetailActivity.this);
                }else {
                    GoodsHelper.gotoDetail(orderProductAdapter.getItem(position).getProduct().getId(), RefundDetailActivity.this);
                }
            }
        });
    }


    /**
     * 网络请求后更新列表数据到当前数据集的尾部
     *
     * @param data 列表数据
     */
    public void updateListData(List data) {
        int headerCount = refundOrderList.getHeaderViewsCount();
        int footerCount = refundOrderList.getFooterViewsCount();
        int originalItemCount = orderProductAdapter.getItemCount();
        orderProductAdapter.addDataList(data);
        // 这里不要采用notifyDataSetChanged()，因为该方法会导致数据数组全部更新，从而导致列表的全局刷新；而使用notifyItemRangeInserted()只会让列表局部刷新。
        // 这里采用notifyItemRangeInserted() | notifyItemRangeChanged() 的另外一个好处就是，RecyclerView局部刷新会支持动画效果，用户体验较佳。
        orderProductAdapter.notifyItemRangeInserted(originalItemCount + headerCount + footerCount, data.size());
        //动态更新list高度
        refundOrderList.getLayoutParams().height =
                PulamsiApplication.context.getResources().getDimensionPixelOffset(R.dimen.orderlist_product_height) * orderProductAdapter.getItemCount();
        //mListAdapter.notifyDataSetChanged();
        // 只有在有数据返回的情况下，才更新业务参数，
        loadHelper.restore();
        refundOrderList.setVisibility(View.VISIBLE);
        ptr_refund_style_ptr_frame.setVisibility(View.VISIBLE);

        // 如果启动了下拉刷新，则在数据加载完成时通知 PtrFrameLayout 完成刷新操作
        if (ptr_refund_style_ptr_frame.isRefreshing()) {
            ptr_refund_style_ptr_frame.refreshComplete();
        }
    }


    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View view, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mScrollView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        requestData();
    }

    /**
     * 重新加载
     */
    @Override
    public void onRetry() {
        loadHelper.showLoading();
        requestData();
    }




    /**
     * 刷新列表
     */
    public void refreshList() {
        if (ptr_refund_style_ptr_frame == null) {
            return;
        }
        ptr_refund_style_ptr_frame.post(new Runnable() {
            @Override
            public void run() {
                ptr_refund_style_ptr_frame.autoRefresh(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_btn_my_order_list_item_refund :
                if (mRefundBean.getAuditState() == 1){//判断是否已经退款
                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("该订单已退款，无需再次申请!")
                            .setConfirmText("       确定      ")
                            .showCancelButton(false)
                            .setCancelClickListener(null)
                            .setConfirmClickListener(null)
                            .show();
                }else {
                    ShowDialogMessage();
                }
                break;
        }
    }

    /**
     * 对话框
     */
    private void ShowDialogMessage() {
        sDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sDialog.setTitleText("您确定要退款吗?")
                .setCancelText("         取消      ")
                .setConfirmText("       确定       ")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setTitleText("取消操作!")
                                .setConfirmText("       返回      ")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();

                        //支付宝未完善
                        if (refundType.equals(RefundFragment.TYPE_ALIPAY)) {
                            ToastUtil.showToast("支付宝退款尚未完善");
                            return;
                        }

                        pDialog = new SweetAlertDialog(RefundDetailActivity.this, SweetAlertDialog.PROGRESS_TYPE)
                                .setTitleText("Loading...");
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.app_pulamsi_main_color));
                        pDialog.show();
                        pDialog.setCancelable(false);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                refundSubmit(mRefundBean);//退款
                            }
                        },1000);
                    }
                })
                .show();
    }

    private void refundSubmit(final RefundBean mRefundBean) {
        if (mRefundBean == null)
            return;
        String url = getResources().getString(R.string.serverbaseurl) + getResources().getString(R.string.weChatRefund);
        StringRequest refundSubmitRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null){
                    LogUtils.e(result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getBoolean("successful")) {
                            refundSuccess();
                        }else {
                            refundFailure();
                        }
                        isOperation = true;//操作了，返回刷新
                    } catch (JSONException e) {
                        e.printStackTrace();
                        refundFailure();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refundFailure();//退款失败
                Toast.makeText(RefundDetailActivity.this, R.string.notice_networkerror, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("refundId", mRefundBean.getId());
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(refundSubmitRequest);
    }

    /**
     * 退款成功
     */
    private void refundSuccess() {
        pDialog.setTitleText("退款成功!")
                .setConfirmText("   确定  ")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                    }
                })
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
    }

    /**
     * 退款失败
     */
    private void refundFailure() {
        pDialog.setTitleText("退款失败!")
                .setConfirmText("   确定  ")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        finish();
                    }
                })
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
    }


    /**
     * 返回时发送事件通知刷新列表
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isOperation)
            BusProvider.getInstance().post(new MessageEvent("刷新"));
    }
}
