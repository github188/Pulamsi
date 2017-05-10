package com.pulamsi.utils;

import android.app.Activity;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.angel.bean.AngelProductBean;
import com.pulamsi.gooddetail.GoodDetailActivity;
import com.pulamsi.myinfo.order.entity.Product;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-10-11
 * Time: 14:25
 * FIXME
 */
public class GoodsHelper {

    /**
     * 跳转普通商品详情页
     *
     * @param pid
     */
    public static void gotoDetail(final String pid, final Activity activity) {
        DialogUtil.showLoadingDialog(activity, "加载中");
        String showDetailsurlPath = activity.getString(R.string.serverbaseurl) + activity.getString(R.string.showDetails);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, showDetailsurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        Product product = gson.fromJson(result, Product.class);
                        Intent intent = new Intent(activity, GoodDetailActivity.class);
                        intent.putExtra("product", product);
                        activity.startActivity(intent);
                    } catch (Exception e) {

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
                map.put("pid", pid);
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(stringRequest);
    }



    /**
     * 跳转天使商品详情
     * @param id
     */
    public static  void gotoAngelDetail(final String id, final Activity activity) {
        DialogUtil.showLoadingDialog(activity, "加载中");
        String showDetailsurlPath = activity.getString(R.string.serverbaseurl)+activity.getString(R.string.angelGoodsDetails);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, showDetailsurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        LogUtils.e(result);
                        Gson gson = new Gson();
                        AngelProductBean angelProductBean = gson.fromJson(result, AngelProductBean.class);
                        Intent intent = new Intent(activity, com.pulamsi.angelgooddetail.gooddetail.GoodDetailActivity.class);
                        intent.putExtra("product", angelProductBean);
                        activity.startActivity(intent);
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



    /**
     * 跳转抢购商品详情页
     *
     * @param pid
     */
    public static void gotoSnapUpDetail(final String pid, final Activity activity) {
        DialogUtil.showLoadingDialog(activity, "加载中");
        String showDetailsurlPath = activity.getString(R.string.serverbaseurl)+"android/panicBuy/panicBuyInfo.html";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, showDetailsurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        Product product = gson.fromJson(result, Product.class);
                        Intent intent = new Intent(activity, GoodDetailActivity.class);
                        intent.putExtra("product", product);
                        activity.startActivity(intent);
                    } catch (Exception e) {
                    }
                }
                DialogUtil.hideLoadingDialog();
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                DialogUtil.hideLoadingDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("pid", pid);
                return map;
            }
        };
        PulamsiApplication.requestQueue.add(stringRequest);
    }

}
