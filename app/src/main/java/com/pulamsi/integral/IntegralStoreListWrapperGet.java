package com.pulamsi.integral;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.pulamsi.activity.PulamsiApplication;
import com.pulamsi.base.baseAdapter.HaiBaseListAdapter;
import com.pulamsi.base.baseListview.CommonListLoadMoreHandler;
import com.pulamsi.base.baseUtil.HaiLog;
import com.pulamsi.base.otto.BusProvider;
import com.pulamsi.integral.entity.ResultProduct;
import com.pulamsi.myinfo.order.entity.Product;
import com.pulamsi.views.PtrStylelFrameLayout;
import com.squareup.otto.Subscribe;
import com.taobao.uikit.feature.view.TRecyclerView;

import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * common列表包装类，列表基于RecyclerView，下拉刷新基于 ptr，封装一些列表的基础操作（如网络请求、数据异步更新、翻页、下拉刷新等）
 */
public class IntegralStoreListWrapperGet extends RecyclerView.OnScrollListener implements PtrHandler {


    public final String PAGE_PARAM = "pageNumber";

    /**
     * activity引用
     */
    private Activity mActivity;

    /**
     * 列表
     */
    private TRecyclerView mListView;

    /**
     * 列表对应数据适配器
     */
    private HaiBaseListAdapter<Product> mListAdapter;

    /**
     * 列表对应的布局管理器
     */
    private StaggeredGridLayoutManager mLayoutManager;

    /**
     * 业务请求参数
     */
    private Map<String, String> mBizParams;

    /**
     * 前一次请求成功时的请求参数
     */
    private Map<String, String> mFormerBizParams;

    /**
     * 滚动翻页提示工具
     */
    private CommonListLoadMoreHandler mLoadMoreHandler;

    /**
     * 下拉刷新组件
     */
    private PtrStylelFrameLayout ptrStylelFrameLayout;

    /**
     * 列表是否正在滚动
     */
    private boolean mIsScrolling;

    /**
     * 加载进度条
     */
    private ProgressWheel progressWheel;

    /**
     * 是否开启滚动翻页功能
     */
    private boolean mIsLoadMoreOpen;

    /**
     * 是否开启下拉刷新功能
     */
    private boolean mIsPullToRefreshOpen;

    /**
     * 保存请求的URL
     */
    private String url;

    /**
     * 列表数据加载监听器
     */
    private RequestListener mRequestListener;

    /**
     * 暴露给外层使用的滚动监听器
     */
    private OnScrollListener mOnScrollListener;

    public IntegralStoreListWrapperGet(Activity activity) {
        mActivity = activity;
    }

    public IntegralStoreListWrapperGet setListView(TRecyclerView listView) {
        mListView = listView;
        return this;
    }

    public IntegralStoreListWrapperGet setLayoutManager(StaggeredGridLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        return this;
    }

    public IntegralStoreListWrapperGet setListAdapter(HaiBaseListAdapter adapter) {
        mListAdapter = adapter;
        return this;
    }

    public IntegralStoreListWrapperGet setProgressWheel(ProgressWheel progressWheel) {
        this.progressWheel = progressWheel;
        return this;
    }

    public IntegralStoreListWrapperGet setLoadMoreHandler(CommonListLoadMoreHandler loadMoreHandler) {
        mLoadMoreHandler = loadMoreHandler;
        mIsLoadMoreOpen = true;
        return this;
    }

    public IntegralStoreListWrapperGet setPtrStylelFrameLayout(PtrStylelFrameLayout ptrStylelFrameLayout) {
        this.ptrStylelFrameLayout = ptrStylelFrameLayout;
        mIsPullToRefreshOpen = true;
        return this;
    }

    public IntegralStoreListWrapperGet setRequestListener(RequestListener requestListener) {
        mRequestListener = requestListener;
        return this;
    }

    public IntegralStoreListWrapperGet setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
        return this;
    }

    /**
     * UI元素初始化，注意这里不能有非UI（即数据）相关的初始化操作，因为UI初始化总是在网络请求之后进行
     */
    public void init() {
        mListView.setLayoutManager(mLayoutManager);
        mListView.setAdapter(mListAdapter);
        mListView.setHasFixedSize(true);
        mListView.setOnScrollListener(this);
        // 如果启动了下拉刷新，让 PtrFrameLayout 设置 PtrHandler 用于监听下拉事件
        if (mIsPullToRefreshOpen) {
            ptrStylelFrameLayout.setPtrHandler(this);
        }
    }

    /**
     * 进行网络请求，注意这里不能有非数据请求（即UI）相关的操作，因为网络请求总是在UI初始化之前进行
     *
     * @param bizParams 业务参数，无业务参数则传null
     */
    public void startQuery(String url, Map<String, String> bizParams) {
        mBizParams = bizParams;
        this.url = url;
        getFirstData();
    }

    /**
     * 网络请求后更新列表数据到当前数据集的尾部
     *
     * @param data 列表数据
     */
    public void updateListData(final List data) {
        if (data != null && data.size() > 0) {
            // 添加【加载更多】的脚部视图的操作必须放在 notifyItemRangeInserted()之前，否则 notifyItemRangeInserted() 的渐现动画会失效
            if (mIsLoadMoreOpen) {
                mLoadMoreHandler.onWaitToLoadMore(mListView);
            }


            int headerCount = mListView.getHeaderViewsCount();
            int footerCount = mListView.getFooterViewsCount();
            int originalItemCount = mListAdapter.getItemCount();
            mListAdapter.addDataList(data);
            // 这里不要采用notifyDataSetChanged()，因为该方法会导致数据数组全部更新，从而导致列表的全局刷新；而使用notifyItemRangeInserted()只会让列表局部刷新。
            // 这里采用notifyItemRangeInserted() | notifyItemRangeChanged() 的另外一个好处就是，RecyclerView局部刷新会支持动画效果，用户体验较佳。
            mListAdapter.notifyItemRangeInserted(originalItemCount + headerCount + footerCount, data.size());

            // 只有在有数据返回的情况下，才更新业务参数，
            progressWheel.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            ptrStylelFrameLayout.setVisibility(View.VISIBLE);
        } else {
            // 返回的数据为空，请求参数重置为上一次请求成功时的状态
            mBizParams = mFormerBizParams;
            if (mIsLoadMoreOpen) {
                mLoadMoreHandler.onLoadFinish(mListView);
            }
        }

        // 如果启动了下拉刷新，则在数据加载完成时通知 PtrFrameLayout 完成刷新操作
        if (mIsPullToRefreshOpen && ptrStylelFrameLayout.isRefreshing()) {
            ptrStylelFrameLayout.refreshComplete();
        }
    }

    /**
     * 删除脚部
     */
    public void removeFooter() {
        if (mIsLoadMoreOpen) {
            mIsLoadMoreOpen = false;
            mLoadMoreHandler.removeFooter(mListView);
        }
    }


    /**
     * 加载下一页
     */
    public void doNextPage() {
        String page = pagePlusOne();
        if ("-1".equals(page)) {
            return;
        }
        mFormerBizParams = mBizParams;
        mBizParams.put(PAGE_PARAM, page);
        getData(url, mBizParams);
    }

    /**
     * 请求数据的方法volley
     */

    private void getData(String url, final Map<String, String> bizParams) {
        String requesturl = url + "&pageNumber=" + mBizParams.get("pageNumber");
        StringRequest dataRequest = new StringRequest(Request.Method.GET,
                requesturl, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        ResultProduct resultProduct = gson.fromJson(result, ResultProduct.class);
                        if (resultProduct.getReturn_code().equals(resultProduct.SUCCESS)) {
                            updateListData(resultProduct.getReturn_context());
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                //请求网络失败
                if (mRequestListener != null) {
                    mRequestListener.onRequestError(arg0);
                }
            }
        });
        dataRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(dataRequest);
    }

    /**
     * 第一次加载数据
     */

    private void getFirstData() {

        String requesturl = url + "&pageNumber=" + mBizParams.get("pageNumber");
        StringRequest dataRequest = new StringRequest(Request.Method.GET,
                requesturl, new Response.Listener<String>() {

            public void onResponse(String result) {
                if (result != null) {
                    try {
                        Gson gson = new Gson();
                        ResultProduct resultProduct = gson.fromJson(result, ResultProduct.class);
                        if (resultProduct.getReturn_code().equals(resultProduct.SUCCESS)) {
                            if (resultProduct.getReturn_context() != null && resultProduct.getReturn_context().size() < 20) {
                                removeFooter();
                            }
                            if (mRequestListener != null) {
                                mRequestListener.onRequestSuccess(resultProduct.getReturn_context());
                            }
                            // 下拉刷新时，需要清空数据
                            int itemCount = mListAdapter.getItemCount();
                            mListAdapter.clearDataList();
                            mListAdapter.notifyItemRangeRemoved(0, itemCount);
                            updateListData(resultProduct.getReturn_context());
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError arg0) {
                //请求网络失败
                if (mRequestListener != null) {
                    mRequestListener.onRequestError(arg0);
                }
            }
        });
        dataRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        PulamsiApplication.requestQueue.add(dataRequest);
    }


    /**
     * “page” 参数加 1
     *
     * @return "-1" if error occurred
     */
    private String pagePlusOne() {
        if (mBizParams == null) {
            return "-1";
        }

        String page = mBizParams.get(PAGE_PARAM).toString();
        try {
            page = String.valueOf(Integer.valueOf(page) + 1);
        } catch (Exception e) {
            e.printStackTrace();
            page = "-1";
        }
        return page;
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(recyclerView, newState);
        }
        // 加载更多功能开启，则在列表滚动到底部时加入加载更多的逻辑
        if (mIsLoadMoreOpen) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // 列表已停止滑动
                mIsScrolling = false;
                int lastVisibleItemPosition = mLayoutManager.findLastCompletelyVisibleItemPositions(null)[0];

                HaiLog.d("pulamsi", "滚动到最后一排元素：" + lastVisibleItemPosition);
                int totalItemCount = mLayoutManager.getItemCount();
                // 由于首页的上身区域（banner、今明日精选列表等）作为头部插入到全球精选列表中，所以全球精选列表的item数量会多1，
                // 故这里totalItemCount需要减1才能得到全球精选列表真实的item数目。
                if (lastVisibleItemPosition >= totalItemCount - 1 && !mLoadMoreHandler.isLoadingMore()) {
                    HaiLog.d("pulamsi", "够了，滚动到底了！！！");
                    mLoadMoreHandler.onLoadStart();
                    doNextPage();
                }

            } else {
                // 列表正在滑动
                mIsScrolling = true;
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrolled(recyclerView, dx, dy);
        }
    }

    /**
     * 订阅CommonListLoadMoreHandler发来的翻页通知，进行翻页操作
     */
    @Subscribe
    public void onLoadMoreEvent(CommonListLoadMoreHandler.LoadMoreEvent event) {
        if (mIsLoadMoreOpen && !mLoadMoreHandler.isLoadingMore() && event.mLoadMoreHandler == mLoadMoreHandler) {
            mLoadMoreHandler.onLoadStart();
            doNextPage();
        }
    }

    public void onResume() {
        BusProvider.getInstance().register(this);
    }

    public void onPause() {
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if (null == mBizParams && null == mListAdapter) {
            return;
        }
        // 业务参数回到第一页
        mBizParams.put(PAGE_PARAM, "1");
        mFormerBizParams = mBizParams;
        getFirstData();


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

    /**
     * 由于 CommonListWrapper 内部实现了 OnScrollListener，这里再做多一个接口暴露给外层调用，
     * 接口参考 {@link RecyclerView.OnScrollListener}
     */
    public abstract static class OnScrollListener {

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        }

    }
}
