package com.pulamsi.angel.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-08-25
 * Time: 14:26
 * FIXME
 * 抢购的Tab适配器
 */
public class AngelPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> tabFragments;
    private List<String> tabTitles;

    public AngelPagerAdapter(FragmentManager fm, List<Fragment> tabFragments, List<String> tabTitles) {
        super(fm);
        this.tabFragments = tabFragments;
        this.tabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return tabFragments.get(position);
    }

    @Override
    public int getCount() {
        return tabFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}
