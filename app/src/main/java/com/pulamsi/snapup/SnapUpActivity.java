package com.pulamsi.snapup;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.pulamsi.R;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.snapup.presenter.SnapUpPresenter;
import com.pulamsi.snapup.view.ISnapUpActivity;

/**
 * 注意本类采用Mvp模式
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-08-24
 * Time: 10:38
 * FIXME
 */
public class SnapUpActivity extends BaseActivity implements ISnapUpActivity {

    /**
     * 抢购的Persenter
     */
    private SnapUpPresenter snapUpPresenter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseAppManager.getInstance().addActivity(this);//加载到栈中
        setContentView(R.layout.activity_snap_up_more);
        initView();
        init();
    }

    private void initView() {
        setHeaderTitle(R.string.snapup_title);
        viewPager = (ViewPager) findViewById(R.id.snap_up_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.snap_up_tablayout);
    }

    private void init() {
        snapUpPresenter = new SnapUpPresenter(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public TabLayout getTabLayout() {
        return tabLayout;
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
