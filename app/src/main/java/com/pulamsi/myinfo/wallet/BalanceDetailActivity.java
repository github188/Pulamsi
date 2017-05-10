package com.pulamsi.myinfo.wallet;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseAdapter.TabFragmentStateAdapter;
import com.pulamsi.myinfo.order.MyOrderFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 交易明细界面
 */
public class BalanceDetailActivity extends BaseActivity {
  private ViewPager viewPager;
  private String argOpenPage;
  public final static String KEY_OPEN_PAGE = "open_page";




  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initDatas();
    setContentView(R.layout.balancedetail_activity);
    initUI();
  }

  private void initUI() {
    setHeaderTitle(R.string.my_info_wallet_balancedetail_title);

    List<Fragment> tabFragments = new ArrayList<Fragment>();
    tabFragments.add(BalanceDetailFragment.newInstance(BalanceDetailFragment.TYPE_ALL));
    tabFragments.add(BalanceDetailFragment.newInstance(BalanceDetailFragment.TYPE_TERRACE));
    tabFragments.add(BalanceDetailFragment.newInstance(BalanceDetailFragment.TYPE_SLOTMACHINE));
    tabFragments.add(BalanceDetailFragment.newInstance(BalanceDetailFragment.TYPE_STORE));

    List<String> tabTitles = new ArrayList<String>();
    tabTitles.add(getResources().getString(R.string.my_info_wallet_all_tab_str));
    tabTitles.add(getResources().getString(R.string.my_info_wallet_to_be_terrace_tab_str));
    tabTitles.add(getResources().getString(R.string.my_info_wallet_to_be_slotmachine_tab_str));
    tabTitles.add(getResources().getString(R.string.my_info_wallet_to_be_store_tab_str));

    viewPager = (ViewPager) findViewById(R.id.balandedetail_view_pager);
    TabLayout tabLayout = (TabLayout) findViewById(R.id.balandedetail_tabs);
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
        case BalanceDetailFragment.TYPE_ALL:
          viewPager.setCurrentItem(0);
          break;
        case BalanceDetailFragment.TYPE_SLOTMACHINE:
          viewPager.setCurrentItem(2);
          break;
        case BalanceDetailFragment.TYPE_STORE:
          viewPager.setCurrentItem(3);
          break;
      }
    }


  }

  /**
   * 初始化跳转页面
   */
  private void initDatas() {
    if (getIntent() != null) {
      argOpenPage = getIntent().getStringExtra(KEY_OPEN_PAGE);
    }

  }
}
