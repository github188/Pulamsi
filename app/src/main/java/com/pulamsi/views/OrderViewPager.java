package com.pulamsi.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-10-13
 * Time: 11:26
 *
 * 去除切换动画的ViewPager
 */
public class OrderViewPager extends ViewPager {
    public OrderViewPager(Context context) {
        super(context);
    }

    public OrderViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //去除页面切换时的滑动翻页效果
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        // TODO Auto-generated method stub
        super.setCurrentItem(item, false);
    }
}
