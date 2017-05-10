package com.pulamsi.snapup.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-08-24
 * Time: 14:06
 * FIXME
 */
public interface ISnapUpActivity {

    Context getContext();//拿到上下文对象

    ViewPager getViewPager();//拿到viewPager对象

    TabLayout getTabLayout();//拿到TabLayout对象
}
