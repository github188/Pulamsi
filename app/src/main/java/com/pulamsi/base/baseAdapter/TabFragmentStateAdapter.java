package com.pulamsi.base.baseAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * TabLayout 与 ViewPager 共用时的 Fragment 数据适配器，继承 FragmentStatePagerAdapter，
 * 因为 FragmentStatePagerAdapter 会做一些对象的销毁的创建工作，可以节省内存，但切换 Fragment 时的开销较大，
 * 当 ViewPager 包含的 Fragment 较多时使用这个 Adapter 比较合适，但如果希望 Fragment 在移除视线时还能保存相应的对象而不销毁
 * 可以使用上一个Adapter，
 *
 * @author dai on 15/9/23.
 */
public class TabFragmentStateAdapter extends FragmentStatePagerAdapter {

  private List<Fragment> mTabFragments = new ArrayList<Fragment>();

  private List<String> mTabTitles = new ArrayList<String>();

  public TabFragmentStateAdapter(FragmentManager fm, List<Fragment> tabFragments, List<String> tabTitles) {
    super(fm);
    mTabFragments = tabFragments;
    mTabTitles = tabTitles;
  }

  @Override
  public Fragment getItem(int position) {
    return mTabFragments.get(position);
  }

  @Override
  public int getCount() {
    return mTabFragments.size();
  }

  public void addFragmentData(List<Fragment> tabFragments) {
    mTabFragments.addAll(tabFragments);
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return mTabTitles.get(position);
  }
}
