package com.pulamsi.base.baseAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 不会自动销毁 Fragment 对象
 * TabLayout 与 ViewPager 共用时的 Fragment 数据适配器，继承 FragmentPagerAdapter，Fragment 移除视线时不会自动销毁 Fragment 对象，
 * 根据业务需求判断使用本类还是其他
 *
 * @author dai on 15/9/23.
 *         <p/>
 *         没有实现getPageTitle，需要子类自己实现
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mTabFragments = new ArrayList<Fragment>();
    private List<String> mTabTitles = new ArrayList<String>();


    public TabFragmentAdapter(FragmentManager fm, List<Fragment> tabFragments, List<String> tabTitles) {
        super(fm);
        mTabFragments = tabFragments;
        mTabTitles = tabTitles;

    }

    public TabFragmentAdapter(FragmentManager fm, List<Fragment> tabFragments) {
        super(fm);
        mTabFragments = tabFragments;
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
