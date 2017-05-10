package com.pulamsi.angel;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pulamsi.R;
import com.pulamsi.angel.presenter.AngelGoodsFragmentPresenter;
import com.pulamsi.angel.view.IAngelGoodsFragment;
import com.pulamsi.utils.ToastUtil;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.views.LoadView.LoadViewHelper;
import com.taobao.uikit.feature.view.TRecyclerView;

import tr.xip.errorview.ErrorView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-21
 * Time: 17:31
 * FIXME
 */
public class AngelGoodsFragment extends Fragment implements IAngelGoodsFragment, ErrorView.RetryListener {

    private TRecyclerView mRecycleView;
    private LoadViewHelper loadViewHelper;
    private AngelGoodsFragmentPresenter angelGoodsFragmentPresenter;


    String sellerId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.angel_goods_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycleView = (TRecyclerView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        View loadHelpView = view.findViewById(R.id.content_layout);
        loadViewHelper = new LoadViewHelper(loadHelpView);
        if (getArguments() != null)
            sellerId = getArguments().getString("sellerId");

        init();
    }

    private void init() {
        angelGoodsFragmentPresenter = new AngelGoodsFragmentPresenter(this);
    }

    @Override
    public TRecyclerView getTRecyclerView() {
        return mRecycleView;
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void showLoading() {
        loadViewHelper.showLoading();
    }

    @Override
    public void showError() {
        loadViewHelper.showError(this);
    }

    @Override
    public void showSuccessful() {
        loadViewHelper.restore();
    }

    @Override
    public void showEmpty() {
        BlankLayout blankLayout = loadViewHelper.showEmpty("暂无数据");
    }

    @Override
    public String getSellerId() {
        return sellerId;
    }

    @Override
    public void onRetry() {
        ToastUtil.showToast("重新加载");
    }
}
