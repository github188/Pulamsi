package com.pulamsi.angel.view;

import android.content.Context;

import com.taobao.uikit.feature.view.TRecyclerView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-23
 * Time: 14:26
 * FIXME
 */
public interface IAngelEvaluationFragment {

    TRecyclerView getTRecyclerView();//获取列表控件

    Context getContext();//获取下下文对象

    void showLoading();

    void showError();

    void showSuccessful();

    void showEmpty();

    String getSellerId();
}
