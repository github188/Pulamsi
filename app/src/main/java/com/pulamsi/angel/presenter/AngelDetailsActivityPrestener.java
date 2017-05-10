package com.pulamsi.angel.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.gxz.PagerSlidingTabStrip;
import com.lidroid.xutils.util.LogUtils;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.angel.AngelEvaluationFragment;
import com.pulamsi.angel.AngelGoodsFragment;
import com.pulamsi.angel.adapter.AngelPagerAdapter;
import com.pulamsi.angel.view.IAngelDetailsActivity;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.home.entity.AngelMerchantsBean;
import com.pulamsi.views.StickyNavLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-21
 * Time: 17:05
 * FIXME
 */
public class AngelDetailsActivityPrestener {


    private IAngelDetailsActivity iAngelDetailsActivity;

    /**
     * tabfragment的集合
     */
    private List<Fragment> tabFragments;
    /**
     * 上下文对象
     */
    private Context context;
    private ViewPager viewPager;
    private PagerSlidingTabStrip pagerSlidingTabStrip;//指示器
    private StickyNavLayout stickyNavLayout;//悬停根部局

    private boolean lastIsTopHidden;//记录上次是否悬浮

    public AngelDetailsActivityPrestener(IAngelDetailsActivity iAngelDetailsActivity) {
        this.iAngelDetailsActivity = iAngelDetailsActivity;
        init();
        request();//请求网络数据
    }

    private void init() {
        context = iAngelDetailsActivity.getContext();
        viewPager = iAngelDetailsActivity.getViewPager();
        pagerSlidingTabStrip = iAngelDetailsActivity.getPagerSlidingTabStrip();
        stickyNavLayout = iAngelDetailsActivity.getStickyNavLayout();

        tabFragments = new ArrayList<>();
        Fragment angelGoodsFragment = new AngelGoodsFragment();
        Fragment angelEvaluationFragment = new AngelEvaluationFragment();

        Bundle bundle = new Bundle();
        bundle.putString("sellerId", iAngelDetailsActivity.getSellerId());

        angelEvaluationFragment.setArguments(bundle);
        angelGoodsFragment.setArguments(bundle);

        tabFragments.add(angelGoodsFragment);
        tabFragments.add(angelEvaluationFragment);


        List<String> tabTitles = new ArrayList<String>();
        tabTitles.add(context.getResources().getString(R.string.angel_goods));
        tabTitles.add(context.getResources().getString(R.string.angel_evaluation));

        AngelPagerAdapter angelPagerAdapter = new AngelPagerAdapter(((BaseActivity) context).getSupportFragmentManager(), tabFragments, tabTitles);
        viewPager.setAdapter(angelPagerAdapter);
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setOnPageChangeListener(mPageChangeListener);

        stickyNavLayout.setOnStickStateChangeListener(onStickStateChangeListener);
    }


    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };

    private StickyNavLayout.onStickStateChangeListener onStickStateChangeListener = new StickyNavLayout.onStickStateChangeListener() {
        @Override
        public void isStick(boolean isStick) {
            if (lastIsTopHidden != isStick) {
                lastIsTopHidden = isStick;
                if (isStick) {
//                    Toast.makeText(context, "本宝宝悬浮了", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(context, "本宝宝又不悬浮了", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void scrollPercent(float percent) {
        }
    };


    /**
     * 请求网络数据
     */
    public void request() {
        iAngelDetailsActivity.showLoading();
        String url = context.getString(R.string.serverbaseurl) + context.getString(R.string.angelDetails);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtils.e(response);
                if (response != null) {
                    try {
                        Gson gson = new Gson();
                        AngelMerchantsBean angelMerchantsBean = gson.fromJson(response, AngelMerchantsBean.class);
                        iAngelDetailsActivity.setAngelDaetailDate(angelMerchantsBean);//设置数据
                        iAngelDetailsActivity.showSuccessful();
                    } catch (IllegalArgumentException e) {
                        LogUtils.e("天使商家详情包装错误");
                    }
                } else {
                    iAngelDetailsActivity.showError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iAngelDetailsActivity.showError();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("sellerId", iAngelDetailsActivity.getSellerId());
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        PulamsiApplication.requestQueue.add(request);
    }


}
