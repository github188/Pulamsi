package com.pulamsi.evaluate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.util.LogUtils;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.angel.AngelDetailsActivity;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.base.otto.BusProvider;
import com.pulamsi.base.otto.MessageEvent;
import com.pulamsi.evaluate.adapter.AddEvaluateListAdapter;
import com.pulamsi.evaluate.entity.Estimate;
import com.pulamsi.myinfo.order.MyOrderFragment;
import com.pulamsi.myinfo.order.entity.Order;
import com.pulamsi.myinfo.order.entity.OrderItem;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.LoadView.LoadViewHelper;
import com.taobao.uikit.feature.view.TRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.xip.errorview.ErrorView;


/**
 * 评论新增界面
 * <p/>
 * 噩梦般的activity
 */
public class AddEvaluateActivity extends BaseActivity implements View.OnClickListener, ErrorView.RetryListener {

    private Order order;

    private AddEvaluateListAdapter addEvaluateListAdapter;

    /**
     * 商家评论
     */
    private TRecyclerView tRecyclerView;


    private LinearLayout evaluateAngel;


    private TextView angelShopName;

    private RatingBar angelServer;
    private RatingBar angelSpeed;
    private RatingBar angelSales;

    private EditText angelEvaluate;

    private RelativeLayout loadLayout;
    /**
     * 加载状态帮助类
     */
    private LoadViewHelper loadViewHelper;
    private List<Estimate> estimateList;
    private Order mOrder;//网络请求的Oeder

    private boolean isComplete;//店铺是否评价
    private LinearLayout angelEvalaute;
    private LinearLayout haveEvaluation;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_evaluate_activity);
        init();
        initDatas();
        initUI();
    }

    private void init() {
        order = (Order) getIntent().getSerializableExtra("order");
        loadLayout = (RelativeLayout) findViewById(R.id.content_layout);
        loadViewHelper = new LoadViewHelper(loadLayout);
    }


    private void initDatas() {
        loadViewHelper.showLoading();
        String url;
        if (order.getIsSellerOrder()) { //天使和商品为不同
            url = getResources().getString(R.string.serverbaseurl) + getResources().getString(R.string.showAngelProductEstimate);
        } else {
            url = getResources().getString(R.string.serverbaseurl) + getResources().getString(R.string.showProductEstimate);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONObject jb = new JSONObject(response);
                        JSONObject orderMobile = jb.getJSONObject("orderMobile");
                        Gson gson = new Gson();
                        mOrder = gson.fromJson(orderMobile.toString(), Order.class);
                        List<OrderItem> orderItemSet = mOrder.getOrderItemSet();
                        updateDate(orderItemSet);
                        int evaluateCount = jb.getInt("count");//评价数量
                        JSONArray jsonArray = jb.getJSONArray("estimateList");
                        estimateList = gson.fromJson(jsonArray.toString(), new TypeToken<List<Estimate>>() {
                        }.getType());
                        addEvaluateListAdapter.setEstimateList(estimateList);//设置已评价内容
                        if (order.getIsSellerOrder()) {
                            isComplete = jb.getBoolean("isComplete");
                            setEvaluateState(isComplete);
                        }
                    } catch (JSONException e) {
                        LogUtils.e("查看评价装配错误");
                    }
                }
                loadViewHelper.restore();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadViewHelper.showError(AddEvaluateActivity.this);
                LogUtils.e(error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("orderId", order.getId());
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);
    }

    private void setEvaluateState(boolean isComplete) {
        if (isComplete) {
            angelEvalaute.setVisibility(View.GONE);
            haveEvaluation.setVisibility(View.VISIBLE);
        }
    }


    private void initUI() {


        angelEvalaute = (LinearLayout) findViewById(R.id.ll_angel_evalaute);
        haveEvaluation = (LinearLayout) findViewById(R.id.ll_have_evaluation);

        tRecyclerView = (TRecyclerView) findViewById(R.id.addevaluate_trecyclerview);
        TextView addevaluate = (TextView) findViewById(R.id.addevaluate_evaluate);
        addevaluate.setOnClickListener(this);

        addEvaluateListAdapter = new AddEvaluateListAdapter(this);

        tRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tRecyclerView.setAdapter(addEvaluateListAdapter);
        tRecyclerView.setHasFixedSize(true);


        // 默认是1没评论，2是全部评论完成，3是部分评论
        if (order.getEstimateSate() == 2) {
            setHeaderTitle("查看评价");
            addevaluate.setVisibility(View.GONE);
        } else {
            setHeaderTitle(R.string.addevaluate_title);
        }
        /**
         * 是否需要显示商家评论框
         */
        evaluateAngel = (LinearLayout) findViewById(R.id.i_evaluate_angel);
        if (order.getIsSellerOrder()) {
            evaluateAngel.setVisibility(View.VISIBLE);
            initAngelShopEvaluation();//初始化
        } else {
            evaluateAngel.setVisibility(View.GONE);
        }
    }

    public void updateDate(List<OrderItem> orderItemSet) {
        addEvaluateListAdapter.clearDataList();
        addEvaluateListAdapter.addDataList(orderItemSet);
        addEvaluateListAdapter.notifyDataSetChanged();
    }


    /**
     * 店铺评价
     */
    private void initAngelShopEvaluation() {
        angelShopName = (TextView) findViewById(R.id.tv_angel_shop_name);

        angelServer = (RatingBar) findViewById(R.id.rat_angel_server);
        angelSpeed = (RatingBar) findViewById(R.id.rat_angel_speed);
        angelSales = (RatingBar) findViewById(R.id.rat_angel_sales);

        angelEvaluate = (EditText) findViewById(R.id.add_angel_evaluate_content);

        angelShopName.setOnClickListener(this);
        angelShopName.setText(order.getShopName());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addevaluate_evaluate:
                /**
                 * 立即评价
                 */
                if (assetAnyInputIsNull()) {
                    return;
                }
                DialogUtil.showLoadingDialog(this, "正在提交");
                String showAccount;
                if (order.getIsSellerOrder()) {//如果为天使商品
                    showAccount = getString(R.string.serverbaseurl) + getString(R.string.addAngelProductEstimate);
                } else {
                    showAccount = getString(R.string.serverbaseurl) + getString(R.string.addEstimate);
                }
                StringRequest stringRequest = new StringRequest(Request.Method.POST, showAccount, new Response.Listener<String>() {

                    public void onResponse(String result) {
                        if (result != null) {
                            try {
                                HaiLog.d("新增评论", result);
                                JSONObject jsonObject = new JSONObject(result);

                                if (jsonObject.getInt("errorNum") > 0) {
                                    ToastUtil.showToast("部分提交失败，请重新提交！");
                                    return;
                                }
                                if (jsonObject.getBoolean("successful")) {

                                    if (order.getIsSellerOrder() && !isComplete) {
                                        submitAngelEvaluate();//提交店铺评价
                                    } else {
                                        DialogUtil.hideLoadingDialog();
                                        ToastUtil.showToast(jsonObject.getString("message"));
                                        AddEvaluateActivity.this.finish();
                                    }
                                } else {
                                    DialogUtil.hideLoadingDialog();
                                    ToastUtil.showToast(jsonObject.getString("message"));
                                }

                            } catch (Exception e) {
                                HaiLog.e("pulamsi", "新增评论装配出错");
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    public void onErrorResponse(VolleyError arg0) {
                        ToastUtil.showToast(getResources().getString(R.string.notice_networkerror));
                        DialogUtil.hideLoadingDialog();
                        LogUtils.e(arg0.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return getRequestParams();
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
                PulamsiApplication.requestQueue.add(stringRequest);
                break;

            case R.id.tv_angel_shop_name:
                Intent intent = new Intent(this, AngelDetailsActivity.class);
                intent.putExtra("sellerId", order.getSeller_id());//传递天使ID
                this.startActivity(intent);
                break;
        }
    }


    /**
     * 提交天使商家评论
     */
    private void submitAngelEvaluate() {
        String url = getString(R.string.serverbaseurl) + getString(R.string.addAngelEstimate);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("successful")) {
                            ToastUtil.showToast("评论成功");
                            AddEvaluateActivity.this.finish();
                        } else {
                            ToastUtil.showToast(jsonObject.getString("message"));
                        }
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "新增天使商家评论装配出错");
                    }

                }
                DialogUtil.hideLoadingDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.e(error.toString());
            }
        }) {
            //            orderId（订单id）、sellerServe（天使服务）、sellerSpeed（天使速度）、sellerAfter（天使售后）、estimateContent（店铺评价内容）
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("orderId", order.getId());
                map.put("sellerServe", angelServer.getRating() + "");
                map.put("sellerSpeed", angelSpeed.getRating() + "");
                map.put("sellerAfter", angelSales.getRating() + "");
                map.put("estimateContent", angelEvaluate.getText().toString());
                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(stringRequest);

    }

    /**
     * 装配请求参数
     *
     * @return
     */
    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < mOrder.getOrderItemSet().size(); i++) {
            if (estimateList.size() != 0) {
                for (Estimate estimate : estimateList) {
                    if (!estimate.getOrderItem_id().equals(mOrder.getOrderItemSet().get(i).getId())) {//已经评论的过的不在提交
                        map.put("orderId", mOrder.getId());
                        map.put("estimateContent_" + mOrder.getOrderItemSet().get(i).getId(), mOrder.getOrderItemSet().get(i).getContent());
                        map.put("point_" + mOrder.getOrderItemSet().get(i).getId(), mOrder.getOrderItemSet().get(i).getPoint() + "");
                    }
                }
            } else {
                map.put("orderId", mOrder.getId());
                map.put("estimateContent_" + mOrder.getOrderItemSet().get(i).getId(), mOrder.getOrderItemSet().get(i).getContent());
                map.put("point_" + mOrder.getOrderItemSet().get(i).getId(), mOrder.getOrderItemSet().get(i).getPoint() + "");
            }
        }
        return map;
    }


    /**
     * 判断是否存在空的输入项，并弹窗提示
     *
     * @return true 有一个输入项为空，false 全部输入项都不为空
     */
    private boolean assetAnyInputIsNull() {

        if (order.getIsSellerOrder() && !isComplete) {//如果为天使商品，需要做此店铺评价，判断
            if (Float.compare(angelServer.getRating(), 0.0f) == 0 || Float.compare(angelSpeed.getRating(), 0.0f) == 0 || Float.compare(angelSales.getRating(), 0.0f) == 0) {
                ToastUtil.showToast("店铺评分不能为空");
                return true;
            }
            if (angelEvaluate.getText().toString().length() <= 5 || angelEvaluate.getText().toString().length() > 500) {
                ToastUtil.showToast("评价内容在5-500字数之间");
                return true;
            }
        }


        for (int i = 0; i < mOrder.getOrderItemSet().size(); i++) {
            if ((TextUtils.isEmpty(mOrder.getOrderItemSet().get(i).getContent())) || mOrder.getOrderItemSet().get(i).getPoint() == 0) {
                ToastUtil.showToast("评价内容和评分不能为空");
                return true;
            }
            if (mOrder.getOrderItemSet().get(i).getContent().length() <= 5 || mOrder.getOrderItemSet().get(i).getContent().length() > 500) {
                ToastUtil.showToast("评价内容在5-500字数之间");
                return true;
            }
        }

        return false;
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

    @Override
    public void onRetry() {
        initDatas();
    }
}
