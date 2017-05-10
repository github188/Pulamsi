package com.pulamsi.myinfo.order;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.myinfo.order.entity.Order;
import com.pulamsi.myinfo.order.entity.refundDetail;
import com.pulamsi.views.LoadView.LoadViewHelper;
import com.pulamsi.views.progress.DotsProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.xip.errorview.ErrorView;

/**
 * User: daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-04-13
 * Time: 16:07
 * FIXME
 */
public class OrderRefundActivity extends BaseActivity implements ErrorView.RetryListener {
    /**
     * 节点进度条
     */
    private DotsProgressBar mDotsProgressBar;
    private TextView submit, cancel, process, finish;
    private List<TextView> tv_list;
    private RelativeLayout content_layout;
    /**
     * 状态显示帮助类
     */
    private LoadViewHelper loadViewHelper;
    /**
     * 订单数据
     */
    private Order order;
    /**
     * 退款状态
     */
    TextView refund_status;

    /**
     * 退款编号
     */
    TextView refund_sn;

    /**
     * 创建时间
     */
    TextView refund_create_date;
    /**
     * 列表编号
     */
    TextView list_refund_sn;
    /**
     * 电话
     */
    TextView list_refund_mobile;
    /**
     * 总金额
     */
    TextView list_refund_totalAmount;
    /**
     * 取消原因
     */
    TextView list_refund_cancelReason;
    /**
     * 退款人姓名
     */
    TextView list_refund_name;
    /**
     * 申请时间
     */
    TextView refund_submit_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_refund_layout);
        initDatas();
        initView();
        initDate();
    }

    /**
     * 初始化跳转数据
     */
    private void initDatas() {
        if (getIntent() != null) {
            order = (Order) getIntent().getSerializableExtra("order");
        }
    }

    private void initDate() {
        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        loadViewHelper.showLoading();
        String url = getResources().getString(R.string.serverbaseurl) + getString(R.string.refund_detail_info);
        StringRequest orderRefundRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        refundDetail refundDetail = gson.fromJson(result, refundDetail.class);
                        setData(refundDetail);
                        loadViewHelper.restore();//获取成功
                    } catch (IllegalArgumentException e) {
                        LogUtils.e("退款详情数据装载出错");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadViewHelper.showError(OrderRefundActivity.this);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("orderId", order.getId());
                return map;
            }
        };
        orderRefundRequest.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        PulamsiApplication.requestQueue.add(orderRefundRequest);
    }

    private void setData(refundDetail refundDetail) {
        if (refundDetail == null)
            return;
        refund_sn.setText(refundDetail.getOrderSn());
        refund_submit_date.setText(refundDetail.getCreateDate());
        refund_create_date.setText(refundDetail.getCreateDate());
        list_refund_sn.setText(refundDetail.getOrderSn());
        if (refundDetail.getTotalAmount().equals("0") && !refundDetail.getExpendTotalIntegral().equals("0")) {
            list_refund_totalAmount.setText(refundDetail.getExpendTotalIntegral() + "积分");
        } else if (!refundDetail.getTotalAmount().equals("0") && refundDetail.getExpendTotalIntegral().equals("0")) {
            list_refund_totalAmount.setText(refundDetail.getTotalAmount() + "元");
        } else if (!refundDetail.getTotalAmount().equals("0") && !refundDetail.getExpendTotalIntegral().equals("0")) {
            list_refund_totalAmount.setText(refundDetail.getTotalAmount() + "元" + "+" + refundDetail.getExpendTotalIntegral() + "积分");
        }
        list_refund_name.setText(refundDetail.getShipName());
        list_refund_mobile.setText(refundDetail.getShipMobile());
        list_refund_cancelReason.setText(refundDetail.getCancelReason());
        /**
         * 退款状态
         */
        switch (refundDetail.getRefundStatus()) {
            case 1://退款中
                refund_status.setText("退款处理中");
                toggleTextViewColor(2);
                mDotsProgressBar.setRunCountPosition(2);
                break;
            case 2://成功
                refund_status.setText("退款成功");
                finish.setText("退款成功");
                toggleTextViewColor(3);
                mDotsProgressBar.setRunCountPosition(3);
                break;
            case 3://失败
                refund_status.setText("退款失败");
                finish.setText("退款失败");
                toggleTextViewColor(3);
                mDotsProgressBar.setRunCountPosition(3);
                break;
        }
    }

    private void initView() {
        setHeaderTitle("退款详情");
        // 自定义节点进度条的 DotsProgressBar
        mDotsProgressBar = (DotsProgressBar) findViewById(R.id.dots_progress_bar_one);
        content_layout = (RelativeLayout) findViewById(R.id.content_layout);
        loadViewHelper = new LoadViewHelper(content_layout);
        refund_status = (TextView) findViewById(R.id.tv_refund_status);
        refund_sn = (TextView) findViewById(R.id.tv_refund_sn);
        refund_create_date = (TextView) findViewById(R.id.tv_refund_create_date);
        refund_submit_date = (TextView) findViewById(R.id.tv_refund_submit_date);

        list_refund_sn = (TextView) findViewById(R.id.tv_list_refund_sn);
        list_refund_totalAmount = (TextView) findViewById(R.id.tv_list_refund_totalAmount);
        list_refund_cancelReason = (TextView) findViewById(R.id.tv_list_refund_cancelReason);
        list_refund_name = (TextView) findViewById(R.id.tv_list_refund_name);
        list_refund_mobile = (TextView) findViewById(R.id.tv_list_refund_mobile);

        tv_list = new ArrayList<>();
        submit = (TextView) findViewById(R.id.tv_order_refund_submit);
        cancel = (TextView) findViewById(R.id.tv_order_refund_cancel);
        process = (TextView) findViewById(R.id.tv_order_refund_processing);
        finish = (TextView) findViewById(R.id.tv_order_refund_finish);
        tv_list.add(submit);
        tv_list.add(cancel);
        tv_list.add(process);
        tv_list.add(finish);
    }

    public void toggleTextViewColor(int position) {
        tv_list.get(position).setTextColor(getResources().getColor(android.R.color.holo_blue_light));
    }

    @Override
    public void onRetry() {
        requestData();
    }

    @Override
    public boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    public TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }
}

