package com.pulamsi.myinfo.slotmachineManage;

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
 * User: daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-04-15
 * Time: 14:12
 * FIXME
 */
public class RefundManagementActivity extends BaseActivity {

    private TabLayout refundTab;
    private ViewPager refundPager;
    private List<Fragment> tabFragments;
    List<String> tabTitles;
    TabFragmentStateAdapter TabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseAppManager.getInstance().addActivity(this);
        setContentView(R.layout.refund_management_activity);
        initView();
    }

    private void initView() {
        setHeaderTitle(R.string.order_refund_management);
        refundTab = (TabLayout) findViewById(R.id.tab_refund_tab);
        refundPager = (ViewPager) findViewById(R.id.vp_refund_content);
        tabFragments = new ArrayList<Fragment>();
        tabFragments.add(RefundFragment.newInstance(RefundFragment.TYPE_WECHAT));
        tabFragments.add(RefundFragment.newInstance(RefundFragment.TYPE_ALIPAY));

        tabTitles = new ArrayList<String>();
        tabTitles.add(getResources().getString(R.string.order_refund_wechat));
        tabTitles.add(getResources().getString(R.string.order_refund_alipay));

        //设置TabLayout的模式
        refundTab.setTabMode(TabLayout.MODE_FIXED);
        TabAdapter = new TabFragmentStateAdapter(getSupportFragmentManager(), tabFragments, tabTitles);
        refundPager.setOffscreenPageLimit(1);//默认预加载一页
        refundPager.setAdapter(TabAdapter);
        refundTab.setupWithViewPager(refundPager);
    }
}
