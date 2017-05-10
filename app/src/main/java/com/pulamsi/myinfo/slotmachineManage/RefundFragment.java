package com.pulamsi.myinfo.slotmachineManage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.pulamsi.R;
import com.pulamsi.base.baseActivity.BaseLazyFragment;
import com.pulamsi.base.otto.BusProvider;
import com.pulamsi.base.otto.MessageEvent;
import com.pulamsi.constant.Constants;
import com.pulamsi.myinfo.slotmachineManage.adapter.RefundManagementAdapter;
import com.pulamsi.myinfo.slotmachineManage.entity.RefundBean;
import com.pulamsi.myinfo.slotmachineManage.module.RefundManagementWrapperPost;
import com.pulamsi.views.LoadView.LoadViewHelper;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.squareup.otto.Subscribe;
import com.taobao.uikit.feature.view.TRecyclerView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.HashMap;
import java.util.List;

import tr.xip.errorview.ErrorView;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-04-15
 * Time: 14:32
 * FIXME
 */
public class RefundFragment extends BaseLazyFragment implements ErrorView.RetryListener {

    public static final String TYPE_WECHAT = "微信支付";//微信
    public static final String TYPE_ALIPAY = "支付宝支付";//支付宝

    private boolean isPrepared;
    private View mRootView;
    private String refundType;
    View stateView;
    LoadViewHelper loadViewHelper;
    PtrStylelFrameLayout ptrRefundRefresh;
    TRecyclerView trvRefundList;
    Activity mActivity;
    RefundManagementWrapperPost refundManagementWrapperPost;
    RefundManagementAdapter refundManagementAdapter;


    public static RefundFragment newInstance(String type) {
        RefundFragment renfundFragment = new RefundFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        renfundFragment.setArguments(bundle);
        return renfundFragment;
    }


    @Override
    public void onAttach(Activity activity) {
        mActivity = activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.refund_fragment_layout, null);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (mRootView == null)
            return;
        Bundle bundle = getArguments();
        if (bundle == null)
            return;

        refundType = bundle.getString("type");
        isPrepared = true;//初始化完成
        initUI();
        lazyLoad();
    }

    private void initUI() {
        stateView = mRootView.findViewById(R.id.content_layout);
        loadViewHelper = new LoadViewHelper(stateView);
        ptrRefundRefresh = (PtrStylelFrameLayout) mRootView.findViewById(R.id.ptr_refund_refresh);
        trvRefundList = (TRecyclerView) mRootView.findViewById(R.id.trv_refund_list);
        trvRefundList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
                .color(getResources().getColor(R.color.background))
                .size(20)
                .build());//分割线
        refundManagementWrapperPost = new RefundManagementWrapperPost(mActivity);
        refundManagementAdapter = new RefundManagementAdapter(mActivity, this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        refundManagementWrapperPost
                .setListAdapter(refundManagementAdapter)
                .setLoadViewHelper(loadViewHelper)
                .setLayoutManager(staggeredGridLayoutManager)
                .setPtrStylelFrameLayout(ptrRefundRefresh)
                .setListView(trvRefundList)
                .setRefundType(refundType)
                .init();

        trvRefundList.setOnItemClickListener(new TRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(TRecyclerView parent, View view, int position, long id) {
                RefundBean mRefundBean = refundManagementAdapter.getItem(position);
                Intent intent = new Intent(mActivity, RefundDetailActivity.class);
                intent.putExtra("RefundBean", mRefundBean);
                startActivity(intent);
            }
        });

        refundManagementWrapperPost.setRequestListener(new RefundManagementWrapperPost.RequestListener() {
            @Override
            public void onRequestSuccess(List data) {


                if ((data == null || data.size() == 0) && trvRefundList.getItemCount() == 0) {
                    //显示订单列表为空提示
                    showBlankLayout();
                } else {
                    hideBlankLayout();
                }
                ptrRefundRefresh.refreshComplete();//加载成功
            }

            @Override
            public void onRequestError(VolleyError volleyError) {
                loadViewHelper.showError(RefundFragment.this);
            }
        });
    }

    private void request() {
        loadViewHelper.showLoading();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mid", Constants.userId);
        String renRundUrl = getString(R.string.serverbaseurl) + getString(R.string.refundManagementList);
        refundManagementWrapperPost.startQuery(renRundUrl, map);
    }

    /**
     * 设置为空和隐藏
     **/
    public void hideBlankLayout() {
        loadViewHelper.restore();
    }

    public void showBlankLayout() {
        loadViewHelper.showEmpty(getResources().getString(R.string.my_order_data)).hideBlankBtn();
    }

    /**
     * 请求网络写在此
     */
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        //请求网络
        request();
    }

    /**
     * 网络错误重新请求
     */
    @Override
    public void onRetry() {
        request();
    }

    /**
     * 刷新列表
     */
    public void refreshList() {
        if (ptrRefundRefresh == null) {
            return;
        }
        ptrRefundRefresh.post(new Runnable() {
            @Override
            public void run() {
                ptrRefundRefresh.autoRefresh(true);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);//注册

    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);//注销
    }


    /**
     * 退款详情返回刷新界面
     *
     * @param event
     */
    @Subscribe   //订阅事件DataChangedEvent
    public void OnEventRefresh(MessageEvent event) {
        if (event.getTag().equals("刷新"))
            refreshList();
    }

}