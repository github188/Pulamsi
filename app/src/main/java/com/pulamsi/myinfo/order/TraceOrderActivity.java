package com.pulamsi.myinfo.order;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.myinfo.order.adapter.TraceOderExpandableListAdapter;
import com.pulamsi.myinfo.order.entity.Logistics;
import com.pulamsi.myinfo.order.entity.LogisticsData;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.NodeProgressView.NodeProgressAdapter;
import com.pulamsi.views.NodeProgressView.NodeProgressView;
import com.pulamsi.views.TraceOrderExpandableList;

import org.json.JSONObject;

import java.util.List;


/**
 * 查看物流界面
 */
public class TraceOrderActivity extends BaseActivity {

    private Logistics logistics;
    private TraceOrderExpandableList expandlistView;
    private TraceOderExpandableListAdapter expandableListAdapter;
    private LinearLayout contentlayout;
    private TextView empty, com, codenumber;
    private ProgressWheel progressWheel;
    /**
     * 节点进度条
     */
    private NodeProgressView nodeProgressView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String orderSn = getIntent().getStringExtra("orderSn");
        setContentView(R.layout.traceorder_activity);
        initUI();
        initData(orderSn);
    }

    private void initData(String orderSn) {
        String searchkuaiDiInfosurl = getString(R.string.serverbaseurl) + getString(R.string.searchkuaiDiInfos) + orderSn;
        StringRequest showOrderListRequest = new StringRequest(Request.Method.GET,
                searchkuaiDiInfosurl, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if ("ok".equals(jsonObject.get("message"))) {
                            Gson gson = new Gson();
                            logistics = gson.fromJson(result, Logistics.class);
                            if ("shunfeng".equals(logistics.getCom())) {
                                com.setText("顺丰速递");
                            } else if ("yunda".equals(logistics.getCom())) {
                                com.setText("韵达快运");
                            } else if ("zhongtong".equals(logistics.getCom())) {
                                com.setText("中通速递");
                            } else {
                                com.setText("暂无信息");
                            }
                            codenumber.setText(logistics.getNu());
                            //设置节点进度条的适配器
                            nodeProgressView.setNodeProgressAdapter(new NodeProgressAdapter() {
                                @Override
                                public int getCount() {
                                    return logistics.getData().size();
                                }

                                @Override
                                public List<LogisticsData> getData() {
                                    return logistics.getData();
                                }
                            });




                            expandableListAdapter = new TraceOderExpandableListAdapter(
                                    TraceOrderActivity.this,
                                    logistics.getData());

                            expandlistView.setAdapter(expandableListAdapter);
                            expandlistView.setGroupIndicator(null); // 去掉默认带的箭头
                            contentlayout.setVisibility(View.VISIBLE);
                            empty.setVisibility(View.GONE);
                        } else {
                            empty.setVisibility(View.VISIBLE);
                            empty.setText("正在为您发货中...");
                        }
                        progressWheel.setVisibility(View.GONE);
                    } catch (Exception e) {
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                empty.setVisibility(View.VISIBLE);
                empty.setText("暂无订单信息");
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        });
        PulamsiApplication.requestQueue.add(showOrderListRequest);
    }

    private void initUI() {
        setHeaderTitle(R.string.order_trace_title);
        progressWheel = (ProgressWheel) findViewById(R.id.traceorder_pw);
        expandlistView = (TraceOrderExpandableList) findViewById(R.id.traceorder_expandlist);
        empty = (TextView) findViewById(R.id.traceorder_empty);
        contentlayout = (LinearLayout) findViewById(R.id.traceorder_contentlayout);
        com = (TextView) findViewById(R.id.traceorder_com);
        codenumber = (TextView) findViewById(R.id.traceorder_codenumber);
        nodeProgressView = (NodeProgressView) findViewById(R.id.npv_nodeProgressView);
    }
}
