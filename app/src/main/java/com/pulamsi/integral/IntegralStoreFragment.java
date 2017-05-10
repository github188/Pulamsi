package com.pulamsi.integral;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.R;
import com.pulamsi.base.baseListview.CommonListLoadMoreHandler;
import com.pulamsi.integral.adapter.IntegralStoreListAdapter;
import com.pulamsi.utils.GoodsHelper;
import com.pulamsi.views.BlankLayout;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.HashMap;
import java.util.List;

/**
 * 积分商城 Fragment 页面，纯积分兑换区，积分加钱兑换区两个 tab 的订单 Fragment 页面共同
 */
public class IntegralStoreFragment extends Fragment {
    /**
     * 订单列表
     */
    private TRecyclerView evaluateTRecyclerView;

    /**
     * 列表适配器
     */
    private IntegralStoreListAdapter integralStoreListAdapter;

    /**
     * 加载进度条
     */
    private ProgressWheel progressWheel;
    /**
     * 下拉刷新控件
     */
    private PtrStylelFrameLayout ptrStylelFrameLayout;

    /**
     * 请求的类别
     */
    private String evaluateType;
    /**
     * 主布局
     */
    private View mRootView;
    /**
     * activity
     */
    private Activity mActivity;
    /**
     * 为空的时候显示布局
     */
    private BlankLayout mBlankLayout;
    /**
     * 商品评论，包括网络请求和下一页操作
     */
    private IntegralStoreListWrapperGet integralStoreListWrapperGet;

    public static final String TYPE_JF = "1";//纯积分兑换区
    public static final String TYPE_JFANDMONEY = "2";//积分加钱兑换区

    //静态方法构造带参数Fragment
    public static IntegralStoreFragment newInstance(String type) {
        IntegralStoreFragment instance = new IntegralStoreFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onAttach(Activity activity) {
        mActivity = activity;

        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.evaluatelist_activity, null);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        if (mRootView == null) {
            return;
        }

        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        evaluateType = bundle.getString("type");
        initUI();

    }


    private void initUI() {
        evaluateTRecyclerView = (TRecyclerView) mRootView.findViewById(R.id.evaluate_trecyclerview);
        mBlankLayout = (BlankLayout) mRootView.findViewById(R.id.blank_layout);
        ptrStylelFrameLayout = (PtrStylelFrameLayout) mRootView.findViewById(R.id.evaluate_ptr_frame);
        progressWheel = (ProgressWheel) mRootView.findViewById(R.id.evaluate_activity_pw);
        integralStoreListAdapter = new IntegralStoreListAdapter(mActivity, evaluateType);
        integralStoreListWrapperGet = new IntegralStoreListWrapperGet(mActivity);
        CommonListLoadMoreHandler loadMoreHandler = new CommonListLoadMoreHandler();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        integralStoreListWrapperGet.setListAdapter(integralStoreListAdapter).setPtrStylelFrameLayout(ptrStylelFrameLayout).setProgressWheel(progressWheel).setListView(evaluateTRecyclerView).setLayoutManager(layoutManager)
                .setLoadMoreHandler(loadMoreHandler).init();
        evaluateTRecyclerView.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                GoodsHelper.gotoDetail(integralStoreListAdapter.getItem(position).getId(), mActivity);

            }
        });
        integralStoreListWrapperGet.setRequestListener(new IntegralStoreListWrapperGet.RequestListener() {
            @Override
            public void onRequestSuccess(List data) {
                if ((data == null || data.size() == 0) && integralStoreListAdapter.getItemCount() == 0) {
                    //显示订单列表为空提示
                    showBlankLayout();
                } else {
                    hideBlankLayout();
                }
                ptrStylelFrameLayout.refreshComplete();
            }

            @Override
            public void onRequestError(VolleyError volleyError) {
            }
        });

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("pageNumber", "1");
        String url = getString(R.string.serverbaseurl) + getString(R.string.showIntegralShop) + "?pageSize=20&parm=" + evaluateType;
        integralStoreListWrapperGet.startQuery(url, map);
    }

    /**
     * 刷新列表
     */
    public void refreshList() {
        if (ptrStylelFrameLayout == null) {
            return;
        }
        ptrStylelFrameLayout.post(new Runnable() {
            @Override
            public void run() {
                ptrStylelFrameLayout.autoRefresh(true);
            }
        });
    }


    /**
     * 设置为空和隐藏
     **/
    public void hideBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setVisibility(View.INVISIBLE);
        evaluateTRecyclerView.setVisibility(View.VISIBLE);
        progressWheel.setVisibility(View.GONE);
    }

    public void showBlankLayout() {
        mBlankLayout.hideBlankBtn();
        mBlankLayout.setBlankLayoutInfo(R.drawable.ic_space_order, R.string.integral_detail_store_empty);
        mBlankLayout.setVisibility(View.VISIBLE);
        evaluateTRecyclerView.setVisibility(View.INVISIBLE);
        progressWheel.setVisibility(View.GONE);
    }
}

