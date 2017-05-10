package com.pulamsi.evaluate;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.pulamsi.R;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseAdapter.TabFragmentStateAdapter;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.evaluate.entity.EstimateCount;
import com.pulamsi.myinfo.order.MyOrderFragment;
import com.pulamsi.utils.DialogUtil;
import com.pulamsi.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 评论界面
 */
public class EvaluateActivity extends BaseActivity {

    private ViewPager viewPager;
    private String argOpenPage;
    /**
     * 商品对象
     */
    public String productId;
    public final static String KEY_OPEN_PAGE = "open_page";

    public Boolean isAngelProduct;//判断是否为天使商品

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluate_activity);
        DialogUtil.showLoadingDialog(this, "加载中");
        initDatas();
    }

    /**
     * 初始化跳转页面
     */
    private void initDatas() {
        if (getIntent() != null) {
            argOpenPage = getIntent().getStringExtra(KEY_OPEN_PAGE);
            productId = getIntent().getStringExtra("productId");
            isAngelProduct = getIntent().getBooleanExtra("isAngelProduct",false);
        }
        /**
         * 请求用户数据
         */
        String showAccount;
        if (isAngelProduct){
            showAccount = getString(R.string.serverbaseurl) + getString(R.string.angelGoodsEvaluatenumber) + "?productId=" + productId;
        }else {
            showAccount = getString(R.string.serverbaseurl) + getString(R.string.getProductEstimateCount) + "?productId=" + productId;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, showAccount, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        EstimateCount estimateCount = gson.fromJson(result, EstimateCount.class);
                        initUI(estimateCount);
                    } catch (Exception e) {
                        HaiLog.e("pulamsi", "评论总数信息装配出错");
                    }
                    DialogUtil.hideLoadingDialog();
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                DialogUtil.hideLoadingDialog();
                ToastUtil.showToast(R.string.notice_networkerror);
            }
        });
        PulamsiApplication.requestQueue.add(stringRequest);
    }


    private void initUI(EstimateCount estimateCount) {
        setHeaderTitle(R.string.evaluate_title);


        List<Fragment> tabFragments = new ArrayList<Fragment>();
        tabFragments.add(EvaluateFragment.newInstance(EvaluateFragment.TYPE_ALL, productId,isAngelProduct));
        tabFragments.add(EvaluateFragment.newInstance(EvaluateFragment.TYPE_GOOD, productId,isAngelProduct));
        tabFragments.add(EvaluateFragment.newInstance(EvaluateFragment.TYPE_ZHONG, productId,isAngelProduct));
        tabFragments.add(EvaluateFragment.newInstance(EvaluateFragment.TYPE_BAD, productId,isAngelProduct));

        List<String> tabTitles = new ArrayList<String>();
        tabTitles.add(getResources().getString(R.string.good_detail_evaluate_tab_all) + "(" + estimateCount.getTotal() + ")");
        tabTitles.add(getResources().getString(R.string.good_detail_evaluate_tab_good) + "(" + estimateCount.getGood() + ")");
        tabTitles.add(getResources().getString(R.string.good_detail_evaluate_tab_zhong) + "(" + estimateCount.getZhong() + ")");
        tabTitles.add(getResources().getString(R.string.good_detail_evaluate_tab_bad) + "(" + estimateCount.getBad() + ")");

        viewPager = (ViewPager) findViewById(R.id.evaluate_view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.evaluate_tabs);
        TabFragmentStateAdapter tabFragmentAdapter =
                new TabFragmentStateAdapter(getSupportFragmentManager(), tabFragments, tabTitles);
        viewPager.setAdapter(tabFragmentAdapter);
        // 设置 tab 模式，当前为系统默认模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        // 将 TabLayout 和 ViewPager 关联起来。
        tabLayout.setupWithViewPager(viewPager);
        // 给 TabLayout 设置适配器
        tabLayout.setTabsFromPagerAdapter(tabFragmentAdapter);

        if (!TextUtils.isEmpty(argOpenPage)) {
            switch (argOpenPage) {
                case MyOrderFragment.TYPE_NOT_PAY:
                    viewPager.setCurrentItem(1);
                    break;
            }
        }
    }


}
