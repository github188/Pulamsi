package com.pulamsi.integral;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.pulamsi.R;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseAdapter.TabFragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * 积分商城界面
 */
public class IntegralStoreActivity extends BaseActivity {


    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_store_activity);
        BaseAppManager.getInstance().addActivity(this);
        initUI();
    }

    private void initUI() {
        setHeaderTitle(R.string.integral_title);


        List<Fragment> tabFragments = new ArrayList<Fragment>();
        tabFragments.add(IntegralStoreFragment.newInstance(IntegralStoreFragment.TYPE_JF));
        tabFragments.add(IntegralStoreFragment.newInstance(IntegralStoreFragment.TYPE_JFANDMONEY));

        List<String> tabTitles = new ArrayList<String>();
        tabTitles.add(getResources().getString(R.string.integral_store_jf));
        tabTitles.add(getResources().getString(R.string.integral_store_jfandmoney));

        viewPager = (ViewPager) findViewById(R.id.integral_store_view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.integral_store_tabs);
        TabFragmentStateAdapter tabFragmentAdapter =
                new TabFragmentStateAdapter(getSupportFragmentManager(), tabFragments, tabTitles);
        viewPager.setAdapter(tabFragmentAdapter);
        // 设置 tab 模式，当前为系统默认模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        // 将 TabLayout 和 ViewPager 关联起来。
        tabLayout.setupWithViewPager(viewPager);
        // 给 TabLayout 设置适配器
        tabLayout.setTabsFromPagerAdapter(tabFragmentAdapter);
    }
}
