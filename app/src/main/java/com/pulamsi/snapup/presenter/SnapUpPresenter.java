package com.pulamsi.snapup.presenter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.snapup.SnapUpCategoryFragment;
import com.pulamsi.snapup.adapter.SnapUpPagerAdapter;
import com.pulamsi.snapup.view.ISnapUpActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-08-24
 * Time: 14:05
 * FIXME
 */
public class SnapUpPresenter {

    /**
     * 抢购的接口模式 V
     */
    private ISnapUpActivity iSnapUpActivity;
    /**
     * tabfragment的集合
     */
    private List<Fragment> tabFragments;

    /**
     * 上下文对象
     */
    private Context context;

    public SnapUpPresenter(ISnapUpActivity iSnapUpActivity) {
        this.iSnapUpActivity = iSnapUpActivity;
        init();
        initTab();
    }

    private void init() {
        context = iSnapUpActivity.getContext();
    }


    public void initTab() {
        tabFragments = new ArrayList<Fragment>();
        tabFragments.add(SnapUpCategoryFragment.newInstance(SnapUpCategoryFragment.SNAPUP_NOW));
        tabFragments.add(SnapUpCategoryFragment.newInstance(SnapUpCategoryFragment.SNAPUP_LATER));

        List<String> tabTitles = new ArrayList<String>();
        tabTitles.add(context.getResources().getString(R.string.snapup_now));
        tabTitles.add(context.getResources().getString(R.string.snapup_later));

        SnapUpPagerAdapter snapUpAdapter = new SnapUpPagerAdapter(((BaseActivity)context).getSupportFragmentManager(),tabFragments,tabTitles);
        iSnapUpActivity.getViewPager().setAdapter(snapUpAdapter);
        iSnapUpActivity.getTabLayout().setTabMode(TabLayout.MODE_FIXED);
        iSnapUpActivity.getTabLayout().setupWithViewPager(iSnapUpActivity.getViewPager());
    }
}
