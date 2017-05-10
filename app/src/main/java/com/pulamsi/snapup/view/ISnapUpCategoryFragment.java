package com.pulamsi.snapup.view;

import android.content.Context;

import com.pulamsi.views.PtrStylelFrameLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-08-25
 * Time: 17:27
 * FIXME
 */
public interface ISnapUpCategoryFragment {

    PtrStylelFrameLayout getPtrStylelFrameLayout();//获取下拉刷新控件

    TRecyclerView getTRecyclerView();//获取列表控件

    Context getContext();//获取下下文对象

    void showLoading();

    void showError();

    void showSuccessful();

    void showEmpty();

    String getSnapUpType();

}
