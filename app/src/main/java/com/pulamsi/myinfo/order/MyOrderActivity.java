package com.pulamsi.myinfo.order;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.pulamsi.R;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseAdapter.TabFragmentStateAdapter;
import com.pulamsi.views.OrderViewPager;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的订单界面
 */
public class MyOrderActivity extends BaseActivity {

    private OrderViewPager viewPager;
    private String argOpenPage;
    public final static String KEY_OPEN_PAGE = "open_page";
    List<Fragment> tabFragments;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseAppManager.getInstance().addActivity(this);
        initDatas();
        setContentView(R.layout.myorder_activity);
        initUI();
    }

    /**
     * 初始化跳转页面
     */
    private void initDatas() {
        if (getIntent() != null) {
            argOpenPage = getIntent().getStringExtra(KEY_OPEN_PAGE);
        }
    }

    private void initUI() {
        setHeaderTitle(R.string.order_list_title_all);

        tabFragments = new ArrayList<Fragment>();
        tabFragments.add(MyOrderFragment.newInstance(MyOrderFragment.TYPE_ALL));
        tabFragments.add(MyOrderFragment.newInstance(MyOrderFragment.TYPE_NOT_PAY));
        tabFragments.add(MyOrderFragment.newInstance(MyOrderFragment.TYPE_NOT_SHIP));
        tabFragments.add(MyOrderFragment.newInstance(MyOrderFragment.TYPE_NOT_RECEIVE));
        tabFragments.add(MyOrderFragment.newInstance(MyOrderFragment.TYPE_NOT_EVALUATION));

        List<String> tabTitles = new ArrayList<String>();
        tabTitles.add(getResources().getString(R.string.my_order_all_tab_str));
        tabTitles.add(getResources().getString(R.string.my_order_to_be_paid_tab_str));
        tabTitles.add(getResources().getString(R.string.my_order_to_be_send_tab_str));
        tabTitles.add(getResources().getString(R.string.my_order_to_be_received_tab_str));
        tabTitles.add(getResources().getString(R.string.my_info_to_be_evaluation));

        viewPager = (OrderViewPager) findViewById(R.id.vp_my_order_view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_my_order_tabs);

        TabFragmentStateAdapter tabFragmentAdapter =
                new TabFragmentStateAdapter(getSupportFragmentManager(), tabFragments, tabTitles);

        viewPager.setOffscreenPageLimit(1);//小于0时默认为1
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
                    viewPager.setCurrentItem(1);//从【我的】点击待付款到这里
                    break;
                case MyOrderFragment.TYPE_NOT_SHIP:
                    viewPager.setCurrentItem(2);
                    break;
                case MyOrderFragment.TYPE_NOT_RECEIVE:
                    viewPager.setCurrentItem(3);
                    break;
                case MyOrderFragment.TYPE_NOT_EVALUATION:
                    viewPager.setCurrentItem(4);
                    break;
            }
        }

    }

    @Override
    public TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

    @Override
    public boolean toggleOverridePendingTransition() {
        return true;
    }
}
