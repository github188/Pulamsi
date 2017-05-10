package com.pulamsi.start.init.utils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.home.entity.AngelData;
import com.pulamsi.home.entity.AngelMerchantsBean;
import com.pulamsi.home.entity.ChannelMobile;
import com.pulamsi.home.entity.ChannelMobileTotal;
import com.pulamsi.home.entity.HotSellProduct;
import com.pulamsi.home.entity.IndexSource;
import com.pulamsi.home.entity.SearchGoodsList;

import java.util.List;

/**
 * Created by lanqiang on 15/11/26.
 */
public class InitUtils {

    /**
     * 获取首页轮播图片数据
     */
    public static void getSliderBannerData(){
        String indexSourceurlPath = PulamsiApplication.context.getString(R.string.serverbaseurl)+PulamsiApplication.context.getString(R.string.indexSource)+"?begin=2002&end=2005";
        StringRequest indexSourceRequest = new StringRequest(Request.Method.GET,
                indexSourceurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        List<IndexSource> indexSources = gson.fromJson(
                                result, new TypeToken<List<IndexSource>>() {
                                }.getType());
                        PulamsiApplication.dbUtils.deleteAll(IndexSource.class);
                        PulamsiApplication.dbUtils.configAllowTransaction(true);
                        PulamsiApplication.dbUtils.saveAll(indexSources);
                   } catch (Exception e) {
                        HaiLog.e("pulamsi", "获取首页轮播图片数据装配错误");
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
            }
        });
        indexSourceRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(indexSourceRequest);
    }

    /**
     * 获取店主推荐数据
     */
    public static  void getChannelData(){
        String channelMgrImplurlPath = PulamsiApplication.context.getString(R.string.serverbaseurl)
                + PulamsiApplication.context.getString(R.string.channelMgrImplurl)
                + "?pageNumber=0&pageSize=8";
        StringRequest channelstringRequest = new StringRequest(Request.Method.GET,
                channelMgrImplurlPath, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        ChannelMobileTotal channelMobileTotal = gson
                                .fromJson(result,
                                        ChannelMobileTotal.class);
                        PulamsiApplication.dbUtils.deleteAll(ChannelMobile.class);
                        PulamsiApplication.dbUtils.configAllowTransaction(true);
                        PulamsiApplication.dbUtils.saveAll(channelMobileTotal.getList());
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "首页店主推荐数据装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
            }
        });
        channelstringRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(channelstringRequest);
    }
    /**
     * 获取推荐商品数据
     */
    public static  void getHotSellProductData(){
        String URL1 = PulamsiApplication.context.getString(R.string.serverbaseurl) +PulamsiApplication.context.getString(R.string.productSearch) + "?productName=&pageNumber=1&pageSize=10&searchProperty=&searchValue=";
        StringRequest dataRequest = new StringRequest(Request.Method.GET,
                URL1, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        SearchGoodsList searchGoodsList = gson.fromJson(
                                result, SearchGoodsList.class);
                        PulamsiApplication.dbUtils.deleteAll(HotSellProduct.class);
                        PulamsiApplication.dbUtils.configAllowTransaction(true);
                        PulamsiApplication.dbUtils.saveAll(searchGoodsList.getList());
                    } catch (Exception e) {
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                //请求网络失败
            }
        });
        dataRequest.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(dataRequest);
    }


    /**
     * 初始化天使商家数据
     */
    public static void getAngleData() {
        String snapUpUrl =  PulamsiApplication.context.getString(R.string.serverbaseurl) +  PulamsiApplication.context.getString(R.string.angleHomeList);
        StringRequest angleRequest = new StringRequest(Request.Method.POST, snapUpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        AngelData angerData = gson.fromJson(result, AngelData.class);
                        List<AngelMerchantsBean> angelMerchantsBeanList = angerData.getSellerList();
                        PulamsiApplication.dbUtils.deleteAll(AngelMerchantsBean.class);
                        PulamsiApplication.dbUtils.configAllowTransaction(true);
                        PulamsiApplication.dbUtils.saveAll(angelMerchantsBeanList);
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "首页天使数据装配出错");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        angleRequest.setRetryPolicy(new DefaultRetryPolicy(1000 * 10, 0, 1.0f));
        PulamsiApplication.requestQueue.add(angleRequest);

    }
}
