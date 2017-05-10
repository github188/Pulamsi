package com.pulamsi.angel.view;

import android.content.Context;
import android.support.v4.view.ViewPager;

import com.gxz.PagerSlidingTabStrip;
import com.pulamsi.home.entity.AngelMerchantsBean;
import com.pulamsi.views.StickyNavLayout;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-21
 * Time: 17:15
 * FIXME
 */
public interface IAngelDetailsActivity {
    Context getContext();//拿到上下文对象

    ViewPager getViewPager();//拿到viewPager对象

    PagerSlidingTabStrip getPagerSlidingTabStrip();//拿到PagerSlidingTabStrip对象

    StickyNavLayout getStickyNavLayout();//拿到StickyNavLayout根部局

    void showLoading();

    void showError();

    void showSuccessful();

    void showEmpty();

    String getSellerId();

    void setAngelDaetailDate(AngelMerchantsBean angelMerchantsBean);
}
