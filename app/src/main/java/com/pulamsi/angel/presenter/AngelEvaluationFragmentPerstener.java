package com.pulamsi.angel.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

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
import com.pulamsi.angel.adapter.AngelEvaluationAdapter;
import com.pulamsi.angel.bean.AngelEvaluatDateBean;
import com.pulamsi.angel.bean.AngelEvaluationBean;
import com.pulamsi.angel.view.IAngelEvaluationFragment;
import com.taobao.uikit.feature.view.TRecyclerView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-24
 * Time: 14:18
 * FIXME
 */
public class AngelEvaluationFragmentPerstener {

    private IAngelEvaluationFragment iAngelEvaluationFragment;

    private TRecyclerView tRecyclerView;

    private Context context;

    private AngelEvaluationAdapter angelEvaluationAdapter;

    private List<AngelEvaluationBean> angelEvaluationBeans;

    public AngelEvaluationFragmentPerstener(IAngelEvaluationFragment iAngelEvaluationFragment) {
        this.iAngelEvaluationFragment = iAngelEvaluationFragment;
        init();
        requestDate();
    }

    private void init() {
        context = iAngelEvaluationFragment.getContext();
        tRecyclerView = iAngelEvaluationFragment.getTRecyclerView();
        tRecyclerView.setHasFixedSize(true);
        tRecyclerView.setLayoutManager(new GridLayoutManager(context,1));
        angelEvaluationAdapter = new AngelEvaluationAdapter((Activity) context);
        tRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .color(context.getResources().getColor(R.color.app_divider_line_bg_cc))
                .size(1)
                .build());//分割线
        tRecyclerView.setAdapter(angelEvaluationAdapter);
    }


    /**
     * 请求网络数据
     */
    private void requestDate() {
        iAngelEvaluationFragment.showLoading();
        String url = context.getResources().getString(R.string.serverbaseurl) + context.getResources().getString(R.string.angelEvaluate);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Gson gson = new Gson();
                        AngelEvaluatDateBean angelEvaluatDateBean = gson.fromJson(response, AngelEvaluatDateBean.class);
                        angelEvaluationBeans = angelEvaluatDateBean.getList();
                        setAngelPudectDate(angelEvaluationBeans);//设置信息

                        //判断是否为空
                        if ((angelEvaluationBeans == null || angelEvaluationBeans.size() == 0) && angelEvaluationAdapter.getItemCount() == 0) {
                            iAngelEvaluationFragment.showEmpty();
                        } else {
                            iAngelEvaluationFragment.showSuccessful();
                        }

                    } catch (IllegalArgumentException e) {
                        LogUtils.e("天使商品包装错误");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iAngelEvaluationFragment.showError();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("sellerId", iAngelEvaluationFragment.getSellerId());
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        PulamsiApplication.requestQueue.add(request);
    }

    /**
     * 设置界面信息
     */
    public void setAngelPudectDate(List<AngelEvaluationBean> angelEvaluationBeans) {
        angelEvaluationAdapter.clearDataList();
        angelEvaluationAdapter.addDataList(angelEvaluationBeans);
        angelEvaluationAdapter.notifyDataSetChanged();
    }
}
