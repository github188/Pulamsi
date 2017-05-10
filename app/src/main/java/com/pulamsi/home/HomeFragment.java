package com.pulamsi.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseFragment;
import com.pulamsi.home.module.HomeController;

public class HomeFragment extends BaseFragment{

    /**
     * 首页控制器
     */
    private HomeController homeController;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /**
         * 回顶按钮
         */
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.iv_home_back_top);
        View rootView = view.findViewById(R.id.home_rootview);
        View headerView = HomeFragment.this.getActivity().getLayoutInflater().inflate(R.layout.home_header_layout, null);
        homeController = new HomeController();
        homeController.initUI(floatingActionButton, rootView, headerView, HomeFragment.this.getActivity());
        homeController.RequestData();
    }


    @Override
    public void onStop() {
        super.onStop();
        homeController.rollAdverView.stop();//普兰氏快报停止滚动
    }

    @Override
    public void onResume() {
        super.onStart();
        homeController.rollAdverView.start();//普兰氏快报开始滚动
    }
}


