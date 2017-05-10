package com.pulamsi.shoppingcar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseFragment;
import com.pulamsi.shoppingcar.module.ShoppingCarController;

/**
 * 购物车界面
 */
public class ShoppingCarFragment extends BaseFragment {

    /**
     * 购物车数据处理类
     */
    private ShoppingCarController shoppingCarController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shoppingcar_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shoppingCarController = new ShoppingCarController();
        shoppingCarController.initUI(view, getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        shoppingCarController.refreshList();
    }
}

