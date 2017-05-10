package com.pulamsi.angel.view;

import android.content.Context;
import android.view.View;

import com.pulamsi.base.baseListview.CommonListLoadMoreHandler;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-13
 * Time: 10:00
 * FIXME
 */
public interface IAngelActivity {

    PtrStylelFrameLayout getPtrStylelFrameLayout();//获取下拉刷新控件

    TRecyclerView getTRecyclerView();//获取列表控件

    View getRightSearchView();//获取右边搜索按钮

    CommonListLoadMoreHandler getCommonListLoadMoreHandler();

    Context getContext();//获取下下文对象

    void showLoading();

    void showError();

    void showSuccessful();

    void showEmpty();
}
