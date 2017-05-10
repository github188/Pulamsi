package com.pulamsi.angel.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

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
import com.pulamsi.angel.adapter.AngelGoodsAdapter;
import com.pulamsi.angel.bean.AngelProductBean;
import com.pulamsi.angel.view.IAngelGoodsFragment;
import com.pulamsi.utils.DialogUtil;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-23
 * Time: 15:12
 * FIXME
 */
public class AngelGoodsFragmentPresenter {

    private IAngelGoodsFragment iAngelGoodsFragment;

    private TRecyclerView tRecyclerView;

    private Context context;

    private AngelGoodsAdapter angelGoodsAdapter;

    private List<AngelProductBean> angelProductList;


    public AngelGoodsFragmentPresenter(IAngelGoodsFragment iAngelGoodsFragment) {
        this.iAngelGoodsFragment = iAngelGoodsFragment;
        init();
        requestDate();
    }


    private void init() {
        context = iAngelGoodsFragment.getContext();

        tRecyclerView = iAngelGoodsFragment.getTRecyclerView();
        tRecyclerView.setHasFixedSize(true);
        tRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        angelGoodsAdapter = new AngelGoodsAdapter((Activity) context);
        tRecyclerView.setAdapter(angelGoodsAdapter);
        tRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                gotoAngelDetail(angelProductList.get(position).getId());//跳转天使详情
            }
        });

    }

    /**
     * 请求数据
     */
    private void requestDate() {
        iAngelGoodsFragment.showLoading();
        String url = context.getResources().getString(R.string.serverbaseurl) + context.getResources().getString(R.string.angelProduct);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Gson gson = new Gson();
                        angelProductList = gson.fromJson(response, new TypeToken<List<AngelProductBean>>() {
                        }.getType());
                        setAngelPudectDate(angelProductList);//设置信息

                        //判断是否为空
                        if ((angelProductList == null || angelProductList.size() == 0) && angelGoodsAdapter.getItemCount() == 0) {
                            iAngelGoodsFragment.showEmpty();
                        } else {
                            iAngelGoodsFragment.showSuccessful();
                        }

                    } catch (IllegalArgumentException e) {
                        LogUtils.e("天使商品包装错误");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iAngelGoodsFragment.showError();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("sellerId", iAngelGoodsFragment.getSellerId());
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        PulamsiApplication.requestQueue.add(request);
    }


    /**
     * 设置信息
     */
    private void setAngelPudectDate(List<AngelProductBean> angelProductList) {

        angelGoodsAdapter.clearDataList();
        angelGoodsAdapter.addDataList(angelProductList);
        angelGoodsAdapter.notifyDataSetChanged();
    }


    /**
     * 跳转天使商品详情
     * @param id
     */
    private void gotoAngelDetail(final String id) {
        DialogUtil.showLoadingDialog(context, "加载中");
        String showDetailsurlPath = context.getString(R.string.serverbaseurl)+context.getString(R.string.angelGoodsDetails);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, showDetailsurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        LogUtils.e(result);
                        Gson gson = new Gson();
                        AngelProductBean angelProductBean = gson.fromJson(result, AngelProductBean.class);
                        Intent intent = new Intent(context, com.pulamsi.angelgooddetail.gooddetail.GoodDetailActivity.class);
                        intent.putExtra("product", angelProductBean);
                        context.startActivity(intent);
                        LogUtils.e(result);
                    } catch (Exception e) {
                        LogUtils.e("天使详情包装出错");
                    }
                }
                DialogUtil.hideLoadingDialog();
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                LogUtils.e(arg0.toString());
                DialogUtil.hideLoadingDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("pid", id);
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(stringRequest);

    }


}
