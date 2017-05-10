package com.pulamsi.angel;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.pulamsi.R;
import com.pulamsi.angel.presenter.AngelActivityPrestener;
import com.pulamsi.angel.view.IAngelActivity;
import com.pulamsi.base.BaseAppManager.BaseAppManager;
import com.pulamsi.base.baseActivity.BaseActivity;
import com.pulamsi.base.baseListview.CommonListLoadMoreHandler;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.views.LoadView.LoadViewHelper;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.HashMap;
import java.util.Map;

import tr.xip.errorview.ErrorView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-09-13
 * Time: 09:57
 * FIXME
 */
public class AngelActivity extends BaseActivity implements IAngelActivity, ErrorView.RetryListener {


    private AngelActivityPrestener angelActivityPrestener;//天使界面Prestener
    /**
     * 下拉控件
     */
    private PtrStylelFrameLayout ptr;
    /**
     * 列表控件
     */
    private TRecyclerView angelRecyclerView;
    /**
     * 搜索控件
     */
    private ImageView searchView;
    /**
     * 动态加载帮助类
     */
    private LoadViewHelper loadViewHelper;
    /**
     * 加载更多包装类
     */
    private CommonListLoadMoreHandler commonListLoadMoreHandler;

    private boolean isSearch;
    private String searchContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseAppManager.getInstance().addActivity(this);
        setContentView(R.layout.home_angel_acticity);
        init();
        initView();
        angelActivityPrestener = new AngelActivityPrestener(this);
        request();

    }


    private void init() {
        setHeaderTitle("聚天使");
        setRightImage(R.drawable.angel_search);
        isSearch = getIntent().getBooleanExtra("isSearch", false);
        searchContent = getIntent().getStringExtra("searchContent");
    }

    /**
     * 一般用于获取控件
     */
    private void initView() {
        ptr = (PtrStylelFrameLayout) findViewById(R.id.angel_material_style_ptr_frame);
        angelRecyclerView = (TRecyclerView) findViewById(R.id.angel_trecyclerview);
        searchView = getRightImageView();
        View view = findViewById(R.id.content_layout);//动态状态的布局
        loadViewHelper = new LoadViewHelper(view);
        commonListLoadMoreHandler = new CommonListLoadMoreHandler();
    }

    private void request() {
        //初次请求
        Map<String, String> mapDatePage = new HashMap<String, String>();
        mapDatePage.put("pageNumber", "1");
        mapDatePage.put("pageSize", "8");
        if (isSearch && searchContent != null) {
            mapDatePage.put("shopName", searchContent);
        }
        angelActivityPrestener.RequestDate(mapDatePage);//请求数据
        showLoading();
    }

    @Override
    public PtrStylelFrameLayout getPtrStylelFrameLayout() {
        return ptr;
    }

    @Override
    public TRecyclerView getTRecyclerView() {
        return angelRecyclerView;
    }

    @Override
    public View getRightSearchView() {
        return searchView;
    }

    @Override
    public CommonListLoadMoreHandler getCommonListLoadMoreHandler() {
        return commonListLoadMoreHandler;
    }

    @Override
    public Context getContext() {
        return this;
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
    public void onRetry() {
        //重新加载按钮在这里响应
        request();
    }
}
