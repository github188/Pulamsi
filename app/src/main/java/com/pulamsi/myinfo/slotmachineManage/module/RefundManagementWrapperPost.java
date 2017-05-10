package com.pulamsi.myinfo.slotmachineManage.module;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.myinfo.order.entity.Order;
import com.pulamsi.myinfo.slotmachineManage.entity.RefundBean;
import com.pulamsi.views.LoadView.LoadViewHelper;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2016-04-18
 * Time: 14:20
 * FIXME
 */
public class RefundManagementWrapperPost extends RecyclerView.OnScrollListener implements PtrHandler {
    /**
     * activity引用
     */
    private Activity mActivity;

    /**
     * 业务请求参数
     */
    private Map<String, String> mBizParams;

    /**
     * 保存请求的URL
     */
    private String url;

    /**
     * 列表对应数据适配器
     */
    private HaiBaseListAdapter<Order> mListAdapter;

    /**
     * 加载进度条
     */
    private LoadViewHelper mLoadViewHelper;

    /**
     * 是否开启下拉刷新功能
     */
    private boolean mIsPullToRefreshOpen;

    /**
     * 下拉刷新组件
     */
    private PtrStylelFrameLayout ptrStylelFrameLayout;

    /**
     * 列表对应的布局管理器
     */
    private StaggeredGridLayoutManager mLayoutManager;

    private TRecyclerView mRecyclerView;

    /**
     * 列表数据加载监听器
     */
    private RequestListener mRequestListener;

    /**
     * 支付方式
     */
    private String mRefundType;


    public RefundManagementWrapperPost(Activity activity) {
        mActivity = activity;
    }

    public RefundManagementWrapperPost setListAdapter(HaiBaseListAdapter adapter) {
        mListAdapter = adapter;
        return this;
    }

    public RefundManagementWrapperPost setLoadViewHelper(LoadViewHelper mLoadViewHelper) {
        this.mLoadViewHelper = mLoadViewHelper;
        return this;
    }

    public RefundManagementWrapperPost setPtrStylelFrameLayout(PtrStylelFrameLayout ptrStylelFrameLayout) {
        this.ptrStylelFrameLayout = ptrStylelFrameLayout;
        mIsPullToRefreshOpen = true;
        return this;
    }

    public RefundManagementWrapperPost setLayoutManager(StaggeredGridLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        return this;
    }

    public RefundManagementWrapperPost setListView(TRecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
        return this;
    }

    public RefundManagementWrapperPost setRequestListener(RequestListener requestListener) {
        mRequestListener = requestListener;
        return this;
    }

    public RefundManagementWrapperPost setRefundType(String refundType) {
        mRefundType = refundType;
        return this;
    }


    /**
     * UI元素初始化，注意这里不能有非UI（即数据）相关的初始化操作，因为UI初始化总是在网络请求之后进行
     */
    public void init() {
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(this);
        mRecyclerView.setItemAnimator(null);//去除动画
        // 如果启动了下拉刷新，让 PtrFrameLayout 设置 PtrHandler 用于监听下拉事件
        if (mIsPullToRefreshOpen) {
            ptrStylelFrameLayout.setPtrHandler(this);
        }
    }

    /**
     * 判断是否可以下拉刷新。 UltraPTR 的 Content 可以包含任何内容，用户在这里判断决定是否可以下拉。
     *
     * @param frame
     * @param content
     * @param header
     * @return
     */
    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        // 默认实现，根据实际情况做改动
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }


    /**
     * 第一次加载数据
     */

    private void getFirstData() {
        StringRequest StringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<RefundBean>>() {
                        }.getType();
                        List<RefundBean> refundList = gson.fromJson(result, type);
                        // 下拉刷新时，需要清空数据
                        int itemCount = mListAdapter.getItemCount();
                        mListAdapter.clearDataList();
                        mListAdapter.notifyItemRangeRemoved(0, itemCount);
                        List newList = listFilter(refundList);//过滤支付方式
                        updateListData(newList);
                        //应该放在过滤数据之后
                        if (mRequestListener != null) {
                            mRequestListener.onRequestSuccess(newList);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mRequestListener != null) {
                    mRequestListener.onRequestError(error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return mBizParams;
            }
        };
        PulamsiApplication.requestQueue.add(StringRequest);
    }

    /**
     * 网络请求后更新列表数据到当前数据集的尾部
     *
     * @param data 列表数据
     */
    public void updateListData(final List data) {
        if (data != null && data.size() > 0) {

            int headerCount = mRecyclerView.getHeaderViewsCount();
            int footerCount = mRecyclerView.getFooterViewsCount();
            int originalItemCount = mListAdapter.getItemCount();
            mListAdapter.addDataList(data);

            // 这里不要采用notifyDataSetChanged()，因为该方法会导致数据数组全部更新，从而导致列表的全局刷新；而使用notifyItemRangeInserted()只会让列表局部刷新。
            // 这里采用notifyItemRangeInserted() | notifyItemRangeChanged() 的另外一个好处就是，RecyclerView局部刷新会支持动画效果，用户体验较佳。
            mListAdapter.notifyItemRangeInserted(originalItemCount + headerCount + footerCount, data.size());
//      mListAdapter.notifyDataSetChanged();
            // 只有在有数据返回的情况下，才更新业务参数，
            mLoadViewHelper.restore();
            mRecyclerView.setVisibility(View.VISIBLE);
            ptrStylelFrameLayout.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 集合过滤
     *
     * @param data
     * @return
     */
    @NonNull
    private List listFilter(List data) {
        if (data != null && data.size() > 0) {
            ListIterator<RefundBean> lt = data.listIterator();
            List newList = new ArrayList<>();
            while (lt.hasNext()) {
                RefundBean refundBean = lt.next();
                if (mRefundType.equals(refundBean.getPaymentConfigName())) {
                    newList.add(refundBean);
                }
            }
            return newList;
        }
        return  null;
    }

    /**
     * 开始发起请求
     *
     * @param url
     * @param bizParams
     */
    public void startQuery(String url, HashMap<String, String> bizParams) {
        mBizParams = bizParams;
        this.url = url;
        getFirstData();
    }

    /**
     * 刷新回调函数，用户在这里写自己的刷新功能实现，处理业务数据的刷新。
     *
     * @param ptrFrameLayout
     */
    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {

        if (null == mBizParams && null == mListAdapter) {
            return;
        }
        getFirstData();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }


    /**
     * 列表数据加载监听器，供业务类使用，由于 CommonRequestHandler 不对外暴露，业务类只能通过该接口来监听列表数据加载的时机
     * <p/>
     * 1. 用于在列表加载完成时实现一些列表的业务操作，如添加 header 和 footer 等
     * 2. 用于缓存加载完成的列表数据到具体业务之中
     */
    public static abstract class RequestListener {

        /**
         * 请求成功的回调
         *
         * @data 网络框架返回的数据，有可能为 null
         */
        public abstract void onRequestSuccess(List data);

        /**
         * 请求成功但返回的信息有误，即 code != 200 的情况
         */
        public abstract void onRequestError(VolleyError volleyError);

    }
}
