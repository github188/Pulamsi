package com.pulamsi.gooddetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pulamsi.R;
import com.pulamsi.base.otto.BusProvider;


/**
 * 图文详情顶部的 TabLayout 模块，用于切换不同的 Tab
 *
 * @author WilliamChik on 15/10/8.
 */
public class GoodDetailImgDetailTabFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private View mFragmentRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentRootView = inflater.inflate(R.layout.good_detail_img_detail_tab_fragment, null);
        initUI(mFragmentRootView);
        return mFragmentRootView;
    }

    private void initUI(View fragmentRootView) {
        TabLayout tabLayout = (TabLayout) fragmentRootView.findViewById(R.id.tl_good_detail_img_detail_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.good_detail_img_detail_tab_str));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.good_detail_product_parameter_tab_str));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.good_detail_tax_faq_tab_str));
        // 设置 tab 模式，当前为系统默认模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        BusProvider.getInstance().post(new NotifyImgDetailChangeWebViewEvent(tab.getPosition()));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     */
    public class NotifyImgDetailChangeWebViewEvent {

        public int tabPos;

        public NotifyImgDetailChangeWebViewEvent(int tabPos) {
            this.tabPos = tabPos;
        }
    }
}
