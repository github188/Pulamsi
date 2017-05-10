package com.pulamsi.snapup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseLazyFragment;
import com.pulamsi.snapup.presenter.SnapUpCategoryFragmentPresenter;
import com.pulamsi.snapup.view.ISnapUpCategoryFragment;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.views.LoadView.LoadViewHelper;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

import tr.xip.errorview.ErrorView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-08-25
 * Time: 13:50
 * FIXME
 */
public class SnapUpCategoryFragment extends BaseLazyFragment implements ISnapUpCategoryFragment, ErrorView.RetryListener {
    /**
     * 类型
     *
     * @param type
     * @return
     */
    public final static String SNAPUP_NOW = "0";
    public final static String SNAPUP_LATER = "1";

    /**
     * 上下文
     */
    private Context context;
    /**
     * 根部局
     */
    View mRootView;
    /**
     * 抢购标识
     */
    String snapUpType;
    /**
     * View有没有加载完
     */
    private boolean isPrepared;
    /**
     * Fragment的Presenter
     */
    private SnapUpCategoryFragmentPresenter snapUpCategoryFragmentPresenter;
    /**
     * 下拉刷新控件
     */
    private PtrStylelFrameLayout ptr;
    /**
     * 列表控件
     */
    private TRecyclerView snapUpRecyclerView;

    /**
     * 动态布局帮助类
     */
    private LoadViewHelper loadViewHelper;//动态布局帮助类


    public static SnapUpCategoryFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        SnapUpCategoryFragment instance = new SnapUpCategoryFragment();
        instance.setArguments(bundle);
        return instance;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.snap_up_list, null);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init();//初始化
        lazyLoad();//懒加载，一般用于请求网络
    }

    /**
     * 初始化数据
     */
    private void init() {
        if (mRootView == null) {
            return;
        }
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        snapUpType = bundle.getString("type");
        isPrepared = true;//view已经加载完成
        initView();//findviebyid
        snapUpCategoryFragmentPresenter = new SnapUpCategoryFragmentPresenter(this);
    }

    /**
     * 一般用于获取控件
     */
    private void initView() {
        ptr = (PtrStylelFrameLayout) mRootView.findViewById(R.id.snap_up_material_style_ptr_frame);
        snapUpRecyclerView = (TRecyclerView) mRootView.findViewById(R.id.snap_up_trecyclerview);
        View view = mRootView.findViewById(R.id.content_layout);//动态状态的布局
        loadViewHelper = new LoadViewHelper(view);
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible)
            return;
        //进行请求网络的代码
        snapUpCategoryFragmentPresenter.request(snapUpCategoryFragmentPresenter.MOVE_REFRESH);
    }

    @Override
    public PtrStylelFrameLayout getPtrStylelFrameLayout() {
        return ptr;
    }

    @Override
    public TRecyclerView getTRecyclerView() {
        return snapUpRecyclerView;
    }

    @Override
    public Context getContext() {
        return context;
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
        BlankLayout blankLayout =loadViewHelper.showEmpty("暂无数据");

    }

    @Override
    public String getSnapUpType() {
        return snapUpType;
    }

    /**
     * 加载失败布局的重新加载按钮
     */
    @Override
    public void onRetry() {
        //重新加载按钮在这里响应
        snapUpCategoryFragmentPresenter.request(snapUpCategoryFragmentPresenter.MOVE_REFRESH);
    }
}
